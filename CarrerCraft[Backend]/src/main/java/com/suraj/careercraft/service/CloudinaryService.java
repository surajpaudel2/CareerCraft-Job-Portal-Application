package com.suraj.careercraft.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    Map<String, String> uploadImage(MultipartFile file);
    Map<String, String> uploadDocument(MultipartFile file);

    void deleteFile(String publicId, String fileType);
}
