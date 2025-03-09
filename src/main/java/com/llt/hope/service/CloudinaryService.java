package com.llt.hope.service;

import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // ✅ Upload file lên Cloudinary
    //    public MediaFile uploadFile(MultipartFile file, String relatedId, String type, User uploadedBy) throws
    // IOException {
    //        // Xác định loại file dựa vào phần mở rộng
    //        String originalFilename = file.getOriginalFilename();
    //        assert originalFilename != null;
    //        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
    //
    //        MediaType mediaType;
    //        String resourceType;
    //
    //        if (extension.matches("png|jpg|jpeg|gif")) {
    //            mediaType = MediaType.IMAGE;
    //            resourceType = "image";
    //        } else if (extension.matches("mp4|avi|mov|mkv")) {
    //            mediaType = MediaType.VIDEO;
    //            resourceType = "video";
    //        } else if (extension.matches("mp3|wav|ogg")) {
    //            mediaType = MediaType.AUDIO;
    //            resourceType = "video"; // Cloudinary lưu audio trong resource_type "video"
    //        } else {
    //            throw new IllegalArgumentException("Unsupported file type: " + extension);
    //        }
    //
    //        // Định danh file trên Cloudinary (Ví dụ: lesson_1234_timestamp.mp4)
    //        String fileName = type + "_" + relatedId + "_" + System.currentTimeMillis();
    //
    //        // Upload file lên Cloudinary
    //        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
    //                "public_id", fileName,
    //                "resource_type", resourceType,
    //                "folder", type + "s" // Tự động lưu vào thư mục courses, lessons, exercises
    //        ));
    //
    //        String publicId = (String) uploadResult.get("public_id");
    //        String url = (String) uploadResult.get("url");
    //        long fileSize = file.getSize();
    //
    //        // Nếu là audio hoặc video, lấy thời lượng (duration)
    //        int duration = uploadResult.containsKey("duration") ? ((Double) uploadResult.get("duration")).intValue() :
    // 0;
    //
    //        // Tạo bản ghi MediaFile
    //        MediaFile mediaFile = new MediaFile();
    //        mediaFile.setId(fileName);
    //        mediaFile.setFileType(mediaType);
    //        mediaFile.setUrl(url);
    //        mediaFile.setPublicId(publicId);
    //        mediaFile.setFileSize(fileSize);
    //        mediaFile.setDuration(duration);
    //        mediaFile.setUploadedBy(uploadedBy);
    //
    //        return mediaFile;
    //    }

    // ✅ Xóa file theo publicId trên Cloudinary
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "video"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete video: " + e.getMessage());
        }
    }
}
