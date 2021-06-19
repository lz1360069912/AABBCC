package com.company.file.controller.admin;

import com.company.server.dto.ResponseDto;
import com.company.server.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    @PostMapping("/upload")
    public ResponseDto upload(@RequestParam MultipartFile file) throws IOException {
        log.info("上传文件开始：{}", file);
        log.info(file.getOriginalFilename());
        log.info(String.valueOf(file.getSize()));

        // 保存文件到本地
        String fileName = file.getOriginalFilename();
        String key = UuidUtil.getShortUuid();
        // 需要提前创建
        String fullPath = FILE_PATH + "teacher/" + key + "-" + fileName;
        File dest = new File(fullPath);
        file.transferTo(dest);
        log.info(dest.getAbsolutePath());

        ResponseDto responseDto = new ResponseDto();
        responseDto.setContent(FILE_DOMAIN + "f/teacher/" + key + "-" + fileName);
        return responseDto;
    }

    @RequestMapping("/test")
    public String test() {

        return "responseDto";
    }
}