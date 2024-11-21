package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeExclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

@Entity
@Table(indexes = {
        @Index(columnList = "id", unique = true),
})

@BackofficeExclude
public class EntityCreationSettings extends BaseEntity {

    private boolean allowCreation;

    @ManyToMany
    private Set<EntityFieldSettings> creationFields;

    @Override
    public String toString() {
        return "EntityCreationSettings{" +
                "id=" + super.getId() +
                ", allowCreation=" + allowCreation +
                ", creationFields=" + creationFields +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var entityCreationSettings = (EntityCreationSettings) o;
        return StringUtils.equals(super.getId(), entityCreationSettings.getId())
                && allowCreation == entityCreationSettings.allowCreation
                && Objects.equals(creationFields, entityCreationSettings.creationFields);
    }

    @Override
    public int hashCode() {
        final int PRIME = 1909;
        int result = 1;
        final var $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}