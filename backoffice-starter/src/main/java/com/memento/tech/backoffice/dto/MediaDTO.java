package com.memento.tech.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaDTO {

    private String name;

    private String description;

    private String mediaUrl;
}
