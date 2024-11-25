package com.memento.tech.backoffice.converter;

import com.memento.tech.backoffice.entity.Media;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MediaConverter implements Converter<Media, String> {

    @Override
    public String convert(MappingContext<Media, String> context) {
        return Optional.ofNullable(context.getSource())
                .map(Media::getMediaUrl)
                .orElse(StringUtils.EMPTY);
    }
}
