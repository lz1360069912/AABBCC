package com.company.file.controller.admin;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetMezzanineInfoResponse;
import com.company.server.dto.FileDto;
import com.company.server.dto.ResponseDto;
import com.company.server.enums.FileUseEnum;
import com.company.server.service.FileService;
import com.company.server.util.Base64ToMultipartFile;
import com.company.server.util.VodUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@RequestMapping("/admin")
@RestController
public class UploadController {

    private static final String BUSINESS_NAME = "文件上传";

    @Value("${file.domain}")
    private String FILE_DOMAIN;

    @Value("${file.path}")
    private String FILE_PATH;

    @Value("${oss.domain}")
    private String ossDomain;

    @Value("${vod.accessKeyId}")
    private String accessKeyId;

    @Value("${vod.accessKeySecret}")
    private String accessKeySecret;

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseDto upload(@RequestBody FileDto fileDto) throws IOException, InterruptedException {
        log.info("上传文件开始");
        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String suffix = fileDto.getSuffix();
        String shardBase64 = fileDto.getShard();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        // 保存文件到本地
        FileUseEnum userEnum = FileUseEnum.getByCode(use);

        // 如果文件夹不存在则创建
        String dir = userEnum.name().toLowerCase();
        File fullDir = new File(FILE_PATH + dir);
        if (!fullDir.exists()) {
            fullDir.mkdir();
        }

        // 需要提前创建 File.separator：目录间的间隔符
        String path = new StringBuffer(dir).
                append(File.separator).
                append(key).append(".").
                append(suffix).
                toString();
        String localPath = new StringBuffer(path).
                append(".").
                append(fileDto.getShardIndex()).
                toString();
        String fullPath = FILE_PATH + localPath;
        File dest = new File(fullPath);
        shard.transferTo(dest);
        log.info(dest.getAbsolutePath());

        log.info("保存文件记录开始");
        fileDto.setPath(path);
        fileService.save(fileDto);

        ResponseDto responseDto = new ResponseDto();
        fileDto.setPath(FILE_DOMAIN + path);
        responseDto.setContent(fileDto);

        if (fileDto.getShardIndex().equals(fileDto.getShardTotal())) {
            this.merge(fileDto);
        }

        return responseDto;
    }

    public void merge(FileDto fileDto) throws IOException, InterruptedException {
        String path = fileDto.getPath();
        path = path.replace(FILE_DOMAIN, "");
        Integer shardTotal = fileDto.getShardTotal();
        File newFile = new File(FILE_PATH + path);
        FileOutputStream outputStream = new FileOutputStream(newFile, true); // 文件追加写入
        FileInputStream fileInputStream = null; // 分片文件
        byte[] byt = new byte[10 * 1024 * 1024]; // 10MB
        int len;
        log.info("分片合并开始");
        try {
            for (int i = 1; i < shardTotal + 1; i++) {
                fileInputStream = new FileInputStream(new File(FILE_PATH + path + "." + i));
                while ((len = fileInputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);
                }
            }
        } catch (IOException e) {
            log.error("分片合并异常", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.close();
                log.info("IO流关闭");
            } catch (Exception e) {
                log.error("IO流关闭", e);
            }
        }
        log.info("分片合并结束");


        System.gc();
        Thread.sleep(100);
        //删除分片
        log.info("分片删除开始");
        for (int i = 1; i < shardTotal + 1; i++) {
            String filePath = FILE_PATH + path + "." + i;
            File file = new File(filePath);
            boolean result = file.delete();
            log.info("删除{}，{}", filePath, result ? "成功" : "失败");
        }
        log.info("分片删除结束");
    }

    @GetMapping("/check/{key}")
    public ResponseDto check(@PathVariable String key) throws Exception {
        ResponseDto responseDto = new ResponseDto();
        FileDto fileDto = fileService.findByKey(key);

        if (fileDto != null) {
            if (StringUtils.isEmpty(fileDto.getVod())) {
                fileDto.setPath(ossDomain + fileDto.getPath());
            } else {
                DefaultAcsClient vodClient = VodUtil.initVodClient(accessKeyId, accessKeySecret);
                GetMezzanineInfoResponse response = VodUtil.getMezzanineInfo(vodClient, fileDto.getVod());
                System.out.println("获取视频信息, response : " + JSON.toJSONString(response));
                String fileUrl = response.getMezzanine().getFileURL();
                fileDto.setPath(fileUrl);
            }
        }
        responseDto.setContent(fileDto);
        return responseDto;
    }
}