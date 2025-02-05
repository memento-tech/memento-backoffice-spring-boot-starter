package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeExclude;
import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.annotations.BackofficeTitle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
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

@BackofficeExclude
public class TranslationWrapper extends BaseEntity implements BackofficeConfigurationMarker {

    @ManyToOne
    @BackofficeOrderPriority(100)
    @BackofficeTitle("Language")
    private Language language;

    @Column(nullable = false, length = 1000)
    @BackofficeTitle("Translation")
    private String translation;

    @Override
    public String toString() {
        return "TranslationWrapper{" +
                "id=" + super.getId() +
                ", language=" + language +
                ", translation=" + translation +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var translationWrapper = (TranslationWrapper) o;
        return StringUtils.equals(super.getId(), language.getId())
                && Objects.equals(language, translationWrapper.language)
                && Objects.equals(translation, translationWrapper.translation);
    }

    @Override
    public int hashCode() {
        final int PRIME = 4467;
        int result = 1;
        final var $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}
