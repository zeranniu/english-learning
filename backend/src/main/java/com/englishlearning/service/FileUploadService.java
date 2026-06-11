package com.englishlearning.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${upload.dir:uploads/}")
    private String uploadDir;

    public String uploadFile(MultipartFile file, String subDir) throws IOException {
        Path dirPath = Paths.get(uploadDir, subDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String newFilename = UUID.randomUUID().toString() + extension;

        Path filePath = dirPath.resolve(newFilename);
        file.transferTo(filePath.toFile());

        return "/api/uploads/" + subDir + "/" + newFilename;
    }

    public String uploadAudio(MultipartFile file) throws IOException {
        return uploadFile(file, "audio");
    }

    public String uploadImage(MultipartFile file) throws IOException {
        return uploadFile(file, "images");
    }
}
