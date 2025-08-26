package com.llt.hope.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.llt.hope.entity.MediaFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public MediaFile uploadFile(MultipartFile file, String relatedName, String relatedId) throws IOException {
        String fileName = relatedName + "_" + relatedId + "_" + System.currentTimeMillis();
        Map uploadResult = cloudinary
                .uploader()
                .upload(file.getBytes(), ObjectUtils.asMap("public_id", fileName, "folder", "hope"));

        String publicId = (String) uploadResult.get("public_id");
        String url = (String) uploadResult.get("url");
        long fileSize = file.getSize();
        MediaFile mediaFile = new MediaFile();
        mediaFile.setUrl(url);
        mediaFile.setPublicId(publicId);
        mediaFile.setFileSize(fileSize);
        return mediaFile;
    }
    public String uploadFile(byte[] fileBytes, String relatedName, String relatedId) throws IOException {
        String fileName = relatedName + "_" + relatedId + "_" + System.currentTimeMillis();

        // Không cần đọc file từ đường dẫn nữa, vì bạn đã có mảng byte
        // byte[] fileBytes = Files.readAllBytes(file.toPath()); // <-- Dòng này sẽ bị xóa

        Map uploadResult = cloudinary
                .uploader()
                .upload(
                        fileBytes,
                        ObjectUtils.asMap("public_id", fileName, "folder", "hope")); // <-- Sử dụng fileBytes trực tiếp

        String url = (String) uploadResult.get("url");
        return url;
    }
    public MediaFile uploadFile(File file, String relatedName, String relatedId) throws IOException {
        String fileName = relatedName + "_" + relatedId + "_" + System.currentTimeMillis();
        Map uploadResult = cloudinary
                .uploader()
                .upload(file, ObjectUtils.asMap(
                        "public_id", fileName,
                        "folder", "hope"
                ));

        String publicId = (String) uploadResult.get("public_id");
        String url = (String) uploadResult.get("url");
        long fileSize = file.length();

        MediaFile mediaFile = new MediaFile();
        mediaFile.setUrl(url);
        mediaFile.setPublicId(publicId);
        mediaFile.setFileSize(fileSize);
        return mediaFile;
    }

    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "video"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete video: " + e.getMessage());
        }
    }
}
