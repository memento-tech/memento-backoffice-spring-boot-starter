package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "memento.tech.backoffice.media.storage.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class DefaultFileStorageService implements FileStorageService {

    @Value("${memento.tech.backoffice.media.file.import.upload.directory}")
    private String UPLOAD_DIRECTORY;

    @PostConstruct
    public void assureUploadDirectoryExists() throws IOException {
        initUploadDirectory();
    }

    @Override
    public byte[] getFile(String fileName) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalStateException("BACKOFFICE: Multipart file is empty!");
        }

        Path filePath = Paths.get(getUploadFolderDestination()).resolve(fileName).normalize();
        File file = filePath.toFile();

        if (file.exists()) {
            try (var inputStream = new FileInputStream(file)) {
                return inputStream.readAllBytes();
            }
        } else {
            throw new IOException("BACKOFFICE: File not found: " + fileName);
        }
    }

    @Override
    public String saveFile(MultipartFile multipartFile) throws IOException {
        Objects.requireNonNull(multipartFile);

        if (multipartFile.isEmpty()) {
            throw new IllegalStateException("BACKOFFICE: Multipart file is empty!");
        }

        var fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        File destFile = new File(getUploadFolderDestination() + fileName);

        Files.copy(multipartFile.getInputStream(),
                destFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    @Override
    public void removeFile(String fileName) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("BACKOFFICE: File name can not be empty string.");
        }

        Path filePath = Paths.get(getUploadFolderDestination()).resolve(fileName).normalize();
        File file = filePath.toFile();

        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("BACKOFFICE: File not removed: [" + fileName + "]");
            }
        } else {
            throw new IOException("BACKOFFICE: File not found: [" + fileName + "]");
        }
    }

    private String getUploadFolderDestination() {
        var separator = UPLOAD_DIRECTORY.endsWith("/") ? StringUtils.EMPTY : "/";
        return UPLOAD_DIRECTORY + separator;
    }

    private void initUploadDirectory() throws IOException {
        if (StringUtils.isBlank(UPLOAD_DIRECTORY)) {
            throw new IOException("BACKOFFICE: Folder upload directory path can not be empty! Please check [configuration backoffice.file.import.upload.directory]");
        }

        Path mediaDirectory = Paths.get(UPLOAD_DIRECTORY).toAbsolutePath().normalize();

        if (Files.notExists(mediaDirectory)) {
            Files.createDirectories(mediaDirectory);
            log.info("Media folder created at path [{}]", mediaDirectory);
        }
    }
}
