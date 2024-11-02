package com.memento.tech.backoffice.service;

import com.memento.tech.backoffice.entity.Media;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface MediaService {

    Optional<Media> getMediaForName(final String mediaName);

    byte[] getFileMediaForName(final String mediaName) throws IOException;

    void removeMedia(final String mediaName) throws IOException;

    Media updateMediaDescription(final String mediaName, final String mediaDescription);

    Media updateMediaMultipartFile(final String mediaName, final MultipartFile multipartFile) throws IOException;

    Media saveMedia(final MultipartFile multipartFile, final String mediaDescription) throws IOException;
}
