package com.mongs.book_social_network.file;

import com.mongs.book_social_network.book.Book;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.currentTimeMillis;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    @Value("${application.upload.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(@NonNull MultipartFile sourceFile, @NonNull Book book, @NonNull Integer userId) {
        final String fileUploadSubPath = "users" + File.separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(@NonNull MultipartFile sourceFile, String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()) {
            boolean success = targetFolder.mkdirs();
            if (!success) {
                log.warn("Failed to create directory: {}", targetFolder.getAbsolutePath());
                //throw new RuntimeException("Failed to create directory: " + targetFolder.getAbsolutePath());
            }
        }

        //extract file extension
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + File.separator + currentTimeMillis() +"." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try{
            Files.write(targetPath, sourceFile.getBytes());
            log.info("Successfully uploaded file: {}", targetFilePath);
            return targetFilePath;
        }catch (IOException e){
            log.error("File was not saved: {}", e.getMessage());
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        } else {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
    }
}
