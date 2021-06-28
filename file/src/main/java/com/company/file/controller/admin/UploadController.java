package com.company.file.controller.admin;

import com.company.server.dto.FileDto;
import com.company.server.dto.ResponseDto;
import com.company.server.enums.FileUseEnum;
import com.company.server.service.FileService;
import com.company.server.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
@RequestMapping("/admin")
@RestController
public class UploadController {

    private static final String BUSINESS_NAME = "文件上传";

    @Value("${file.domain}")
    private String FILE_DOMAIN;

    @Value("${file.path}")
    private String FILE_PATH;

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseDto upload(@RequestParam MultipartFile file, String use) throws IOException {
        log.info("上传文件开始");
        log.info(file.getOriginalFilename());
        log.info(String.valueOf(file.getSize()));

        // 保存文件到本地
        FileUseEnum userEnum = FileUseEnum.getByCode(use);
        String fileName = file.getOriginalFilename();
        String key = UuidUtil.getShortUuid();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // 如果文件夹不存在则创建
        String dir = userEnum.name().toLowerCase();
        File fullDir = new File(FILE_PATH + dir);
        if (!fullDir.exists()) {
            fullDir.mkdir();
        }

        // 需要提前创建 File.separator：目录间的间隔符
        String path = dir + File.separator + key + "." + suffix;
        String fullPath = FILE_PATH + path;
        File dest = new File(fullPath);
        file.transferTo(dest);
        log.info(dest.getAbsolutePath());

        log.info("保存文件记录开始");
        FileDto fileDto = new FileDto();
        fileDto.setPath(path);
        fileDto.setName(fileName);
        fileDto.setSize(Math.toIntExact(file.getSize()));
        fileDto.setSuffix(suffix);
        fileDto.setUse(use);
        fileService.save(fileDto);

        ResponseDto responseDto = new ResponseDto();
        fileDto.setPath(FILE_DOMAIN + path);
        responseDto.setContent(fileDto);
        return responseDto;
    }

    @GetMapping("/merge")
    public ResponseDto merge() throws IOException {
        File newFile = new File(FILE_PATH + "/course/test123.mp4");
        FileOutputStream outputStream = new FileOutputStream(newFile, true); // 文件追加写入
        FileInputStream fileInputStream = null; // 分片文件
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;
        try {
            fileInputStream = new FileInputStream(new File(FILE_PATH + "/course/111.blob"));
            while ((len = fileInputStream.read(byt)) != -1) {
                outputStream.write(byt, 0, len);
            }
            fileInputStream = new FileInputStream(new File(FILE_PATH + "/course/222.blob"));
            while ((len = fileInputStream.read(byt)) != -1) {
                outputStream.write(byt, 0, len);
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
        ResponseDto responseDto = new ResponseDto();
        return responseDto;
    }

    @RequestMapping("/test")
    public String test() {

        return "responseDto";
    }
}