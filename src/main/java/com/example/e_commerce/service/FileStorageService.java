package com.example.e_commerce.service;

import ch.qos.logback.classic.Logger;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public String storeFile(MultipartFile file) {
        try {
            // Upload dizinini oluştur
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Dosya adını güvenli hale getir
            String fileName = System.currentTimeMillis() + "_" +
                    file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");

            // Dosyayı kaydet
            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // URL'yi oluştur
            return baseUrl + "/uploads/" + fileName;

        } catch (IOException ex) {

            throw new RuntimeException("Dosya yüklenirken hata oluştu: " + ex.getMessage());
        }
    }

    public void deleteFile(String publicId) {
        try {
            // publicId'den dosya adını çıkar
            String fileName = publicId; // Basit implementasyon - cloud storage'da publicId kullanılır

            Path filePath = Paths.get(uploadDir).resolve(fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException ex) {

            throw new RuntimeException("Dosya silinirken hata oluştu: " + ex.getMessage());
        }
    }
}
