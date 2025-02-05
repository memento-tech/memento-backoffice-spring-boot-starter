package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeFieldForShowInList;
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
})

@BackofficeFieldForShowInList("title")
@BackofficeOrderPriority(95)
public class Language extends BaseEntity implements BackofficeConfigurationMarker {

    @Column(unique = true, nullable = false)
    @BackofficeTitle("ISO Code")
    @BackofficeOrderPriority(100)
    private String langIsoCode;

    @BackofficeTitle("Title")
    private String title;

    @Override
    public String toString() {
        return "Language{" +
                "id=" + super.getId() +
                ", langIsoCode=" + langIsoCode +
                ", title=" + title +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var language = (Language) o;
        return StringUtils.equals(super.getId(), language.getId())
                && Objects.equals(langIsoCode, language.langIsoCode)
                && Objects.equals(title, language.title);
    }

    @Override
    public int hashCode() {
        final int PRIME = 4321;
        int result = 1;
        final var $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}