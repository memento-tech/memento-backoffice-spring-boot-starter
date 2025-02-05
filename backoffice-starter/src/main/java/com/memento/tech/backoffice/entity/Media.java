package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeFieldForShowInList;
import com.memento.tech.backoffice.annotations.BackofficeForbidUpdate;
import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.annotations.BackofficeTitle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

@Entity
@Table(indexes = {
        @Index(columnList = "id", unique = true),
        @Index(columnList = "name", unique = true)
})

@BackofficeFieldForShowInList("title")
public class Media extends BaseEntity implements BackofficeConfigurationMarker {

    @Column(unique = true, nullable = false)
    @BackofficeOrderPriority(100)
    @BackofficeForbidUpdate
    @BackofficeTitle("Name")
    private String name;

    @Column
    @BackofficeForbidUpdate
    @BackofficeOrderPriority(98)
    @BackofficeTitle("Original Name")
    private String originalFileName;

    @Column
    @BackofficeOrderPriority(99)
    @BackofficeTitle("Description")
    private String description;

    @Column(nullable = false)
    @BackofficeForbidUpdate
    @BackofficeOrderPriority(96)
    @BackofficeTitle("Extension")
    private String mediaExtension;

    @Column
    @BackofficeForbidUpdate
    @BackofficeOrderPriority(95)
    @BackofficeTitle("Size")
    private long mediaSize;

    @Column(nullable = false)
    @BackofficeForbidUpdate
    @BackofficeTitle("URL")
    private String mediaUrl;

    @Column
    @BackofficeForbidUpdate
    @BackofficeOrderPriority(97)
    @BackofficeTitle("Content Type")
    private String contentType;

    @Override
    public String toString() {
        return "Media{" +
                "id=" + super.getId() +
                ", name=" + name +
                ", originalFileName=" + originalFileName +
                ", description=" + description +
                ", mediaExtension=" + mediaExtension +
                ", mediaSize=" + mediaSize +
                ", mediaUrl=" + mediaUrl +
                ", contentType=" + contentType +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var media = (Media) o;
        return StringUtils.equals(super.getId(), media.getId())
                && mediaSize == media.mediaSize
                && Objects.equals(name, media.name)
                && Objects.equals(originalFileName, media.originalFileName)
                && Objects.equals(description, media.description)
                && Objects.equals(mediaExtension, media.mediaExtension)
                && Objects.equals(mediaUrl, media.mediaUrl)
                && Objects.equals(contentType, media.contentType);
    }

    @Override
    public int hashCode() {
        final int PRIME = 5233;
        int result = 1;
        final var $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}