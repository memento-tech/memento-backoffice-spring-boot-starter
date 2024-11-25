package com.memento.tech.backoffice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    byte[] getFile(final String fileName) throws IOException;

    String saveFile(final MultipartFile multipartFile) throws IOException;

    void removeFile(final String fileName) throws IOException;
}
