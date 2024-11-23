package com.suraj.careercraft.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.suraj.careercraft.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map<String, String> uploadImage(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "width", 800,
                    "height", 800,
                    "crop", "limit",
                    "quality", "auto:low",
                    "resource_type", "image"
            ));

            return Map.of(
                    "url", uploadResult.get("url").toString(),
                    "publicId", uploadResult.get("public_id").toString()
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to Cloudinary", e);
        }
    }

    @Override
    public Map<String, String> uploadDocument(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "raw"  // Specify resource type as raw for non-image files
            ));

            // Return both URL and public_id
            return Map.of(
                    "url", uploadResult.get("url").toString(),
                    "public_id", uploadResult.get("public_id").toString()
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload document to Cloudinary", e);
        }
    }

    @Override
    public void deleteFile(String publicId, String resourceType) {
        try {
            // Specify resource_type if provided, otherwise default to empty map
            Map<String, Object> options = resourceType != null
                    ? ObjectUtils.asMap("resource_type", resourceType)
                    : ObjectUtils.emptyMap();

            Map<?, ?> result = cloudinary.uploader().destroy(publicId, options);

            // Log and throw error if deletion was unsuccessful
            if (!"ok".equals(result.get("result"))) {
                throw new RuntimeException("File deletion unsuccessful. Result: " + result);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from Cloudinary", e);
        }
    }
}
