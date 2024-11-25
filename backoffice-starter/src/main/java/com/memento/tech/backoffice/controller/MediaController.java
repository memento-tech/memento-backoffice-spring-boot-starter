package com.memento.tech.backoffice.controller;

import com.memento.tech.backoffice.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;

@RestController
@RequestMapping("/api/backoffice/media")
@RequiredArgsConstructor
@Slf4j
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/add")
    public ResponseEntity<String> saveMedia(@RequestParam MultipartFile media,
                                            @RequestParam(required = false) String mediaDescription) {
        try {
            mediaService.saveMedia(media, mediaDescription);
        } catch (IOException e) {
            log.error("Something went wrong!", e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(INTERNAL_BACKOFFICE_ERROR);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateMedia(@RequestParam String mediaName,
                                              @RequestParam(required = false) MultipartFile media,
                                              @RequestParam(required = false) String mediaDescription) {
        if (Objects.isNull(media) && StringUtils.isBlank(mediaDescription)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nothing to update!");
        }

        if (StringUtils.isNotBlank(mediaDescription)) {
            mediaService.updateMediaDescription(mediaName, mediaDescription);
        }

        try {
            if (Objects.nonNull(media) && !media.isEmpty()) {
                mediaService.updateMediaMultipartFile(mediaName, media);
            }
        } catch (IOException e) {
            log.error("Something went wrong!", e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(INTERNAL_BACKOFFICE_ERROR);
        }
        return ResponseEntity.ok().build();
    }
}
