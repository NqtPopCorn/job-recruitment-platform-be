package com.popcorn.jrp.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileHelper {

    /**
     * Lưu file vào một thư mục (location) với tên file duy nhất (UUID).
     */
    public static String storeFile(MultipartFile file, Path location) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(originalFilename);

        // ✅ Lấy tên file gốc (không bao gồm phần mở rộng)
        String baseName = originalFilename.replace("." + extension, "");

        // ✅ Sinh tên mới có cả UUID và tên gốc
        String uniqueFilename = UUID.randomUUID() + "_" + baseName + "." + extension;

        try (InputStream inputStream = file.getInputStream()) {
            Path targetLocation = location.resolve(uniqueFilename);
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + uniqueFilename, e);
        }
    }

    /**
     * Xóa file vật lý khỏi hệ thống.
     */
    public static void deleteFile(String filename, Path location) {
        try {
            Path file = location.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + filename + " from " + location);
        }
    }

    /**
     * Xây dựng URL để client có thể truy cập file.
     */
    public static String buildFileUrl(String pathPrefix, String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        return "/" + pathPrefix + "/" + filename;
    }
}
