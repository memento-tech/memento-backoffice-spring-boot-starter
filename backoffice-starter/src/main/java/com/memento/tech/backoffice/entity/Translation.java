package com.memento.tech.backoffice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

@Entity
@Table(indexes = {
        @Index(columnList = "id", unique = true),
})
public class Translation extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String code;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TranslationWrapper> translationWrappers;

    public void addOrUpdateTranslation(final TranslationWrapper translationWrapper) {
        requireNonNull(translationWrapper);

        var existingTranslation = CollectionUtils.emptyIfNull(translationWrappers)
                .stream()
                .filter(_translationWrapper -> _translationWrapper.getLanguage().getLangIsoCode().equals(translationWrapper.getLanguage().getLangIsoCode()))
                .findFirst()
                .orElse(null);

        if (isNull(existingTranslation)) {
            var newTranslation = TranslationWrapper
                    .builder()
                    .language(translationWrapper.getLanguage())
                    .translation(translationWrapper.getTranslation())
                    .build();

            if (isNull(translationWrappers)) {
                translationWrappers = new ArrayList<>();
            }

            translationWrappers.add(newTranslation);
        } else {
            existingTranslation.setTranslation(translationWrapper.getTranslation());
        }
    }

    @Override
    public String toString() {
        return "Translation{" +
                "id=" + super.getId() +
                ", code=" + code +
                ", translationWrappers=" + translationWrappers +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var translation = (Translation) o;
        return StringUtils.equals(super.getId(), translation.getId())
                && Objects.equals(code, translation.code)
                && Objects.equals(translationWrappers, translation.translationWrappers);
    }

    @Override
    public int hashCode() {
        final int PRIME = 24355;
        int result = 1;
        final var $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}
