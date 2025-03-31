package com.hss.healthyManager.controller;

import com.hss.healthyManager.advice.ExceptionEnums;
import com.hss.healthyManager.advice.MyException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Slf4j
@Api(description = "文件上传接口")
@RestController
@RequestMapping(value = "api/uploadFile")
public class UploadController {

    @Value("${file.upload-dir}") // 读取 application.yml 配置
    private String uploadDir;

    @Value("${server.port}") // 读取 application.yml 配置
    private String port;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadLocal(@RequestParam("file") MultipartFile file) throws IOException {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new MyException(ExceptionEnums.CHOOSE_FILE);
        }

        // 获取文件名和后缀
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.contains(".")) {
            throw new MyException(ExceptionEnums.UPLOAD_FAIL);
        }

        String suffix = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
        if (!suffix.matches("\\.(png|jpg|jpeg)$")) {
            throw new MyException(ExceptionEnums.UPLOAD_FAIL);
        }

        // 生成唯一文件名
        String newFileName = UUID.randomUUID().toString() + suffix;

        // 确保上传目录存在
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        // 保存文件
        File saveFile = new File(uploadDir + newFileName);
        file.transferTo(saveFile);

        // 返回可以在前端访问的 URL 地址
        // 因为 WebConfig 配置了 "/images/**" 映射到 D:/upload/
        String fileUrl = "http://localhost:" + port + "/images/" + newFileName;
        return ResponseEntity.ok(fileUrl);
    }


}
