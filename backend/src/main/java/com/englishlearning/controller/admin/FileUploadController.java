package com.englishlearning.controller.admin;

import com.englishlearning.common.R;
import com.englishlearning.service.FileUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/upload")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/audio")
    public R<?> uploadAudio(@RequestParam("file") MultipartFile file) {
        try {
            String url = fileUploadService.uploadAudio(file);
            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            data.put("name", file.getOriginalFilename());
            data.put("size", String.valueOf(file.getSize()));
            return R.ok(data);
        } catch (Exception e) {
            return R.fail("文件上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/image")
    public R<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String url = fileUploadService.uploadImage(file);
            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            data.put("name", file.getOriginalFilename());
            data.put("size", String.valueOf(file.getSize()));
            return R.ok(data);
        } catch (Exception e) {
            return R.fail("文件上传失败: " + e.getMessage());
        }
    }
}
