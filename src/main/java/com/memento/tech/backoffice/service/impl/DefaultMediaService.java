package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.entity.Media;
import com.memento.tech.backoffice.repository.MediaRepository;
import com.memento.tech.backoffice.service.FileStorageService;
import com.memento.tech.backoffice.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultMediaService implements MediaService {

    private final MediaRepository mediaRepository;

    private final FileStorageService fileStorageService;

    @Value("${memento.tech.backoffice.media.mapping}")
    private String mediaMapping;

    @Override
    public Optional<Media> getMediaForName(final String mediaName) {
        return Optional.ofNullable(mediaName)
                .filter(StringUtils::isNotBlank)
                .flatMap(mediaRepository::findByName);
    }

    @Override
    public byte[] getFileMediaForName(final String mediaName) throws IOException {
        return fileStorageService.getFile(mediaName);
    }

    @Override
    public void removeMedia(final String mediaName) throws IOException {
        var existingMedia = Optional.ofNullable(mediaName)
                .filter(StringUtils::isNotBlank)
                .flatMap(this::getMediaForName)
                .orElse(null);

        if (Objects.nonNull(existingMedia)) {
            fileStorageService.removeFile(mediaName);
            mediaRepository.delete(existingMedia);
            mediaRepository.flush();
        }
    }

    @Override
    public Media updateMediaDescription(final String mediaName, final String mediaDescription) {
        var existingMedia = mediaRepository.findByName(mediaName)
                .orElseThrow(() -> new IllegalStateException("BACKOFFICE: Media not found for media name [" + mediaName + "]"));

        existingMedia.setDescription(mediaDescription);

        var result = mediaRepository.save(existingMedia);
        mediaRepository.flush();
        return result;
    }

    @Override
    public Media updateMediaMultipartFile(final String mediaName, final MultipartFile multipartFile) throws IOException {
        var existingMedia = mediaRepository.findByName(mediaName)
                .orElseThrow(() -> new IllegalStateException("BACKOFFICE: Media not found for media name [" + mediaName + "]"));

        fileStorageService.removeFile(mediaName);
        var fileName = fileStorageService.saveFile(multipartFile);

        existingMedia.setName(fileName);

        existingMedia.setOriginalFileName(multipartFile.getOriginalFilename());
        existingMedia.setMediaSize(multipartFile.getSize());
        existingMedia.setMediaExtension(getMediaExtension(multipartFile.getOriginalFilename()));
        existingMedia.setContentType(multipartFile.getContentType());
        existingMedia.setMediaUrl(getMediaUrl(fileName));

        var result = mediaRepository.save(existingMedia);
        mediaRepository.flush();
        return result;
    }

    @Override
    public Media saveMedia(final MultipartFile multipartFile, final String mediaDescription) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalStateException("BACKOFFICE: Multipart file should not be null.");
        }

        var fileName = fileStorageService.saveFile(multipartFile);

        var newMedia = Media
                .builder()
                .name(fileName)
                .description(mediaDescription)
                .originalFileName(multipartFile.getOriginalFilename())
                .mediaSize(multipartFile.getSize())
                .mediaUrl(getMediaUrl(fileName))
                .contentType(multipartFile.getContentType())
                .mediaExtension(getMediaExtension(multipartFile.getOriginalFilename()))
                .build();

        var result = mediaRepository.save(newMedia);
        mediaRepository.flush();
        return result;
    }

    private String getMediaExtension(String originalFileName) {
        var mediaExtension = StringUtils.EMPTY;

        if (StringUtils.isNotBlank(originalFileName)) {
            mediaExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        }

        return mediaExtension;
    }

    private String getMediaUrl(final String fileName) {
        var baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        return baseUrl + mediaMapping + "/" + fileName;
    }
}
