package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeExclude;
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

@BackofficeExclude
public class EntityFieldSettings extends BaseEntity implements Comparable<EntityFieldSettings> {

    private String entityName;

    private String fieldId;

    private String field;

    private String collectionFieldForShow;

    private String name;

    private boolean excludeField;

    private boolean creationExcludeField;

    private int fieldOrder;

    private boolean uniqueField;

    private boolean required;

    private boolean optional;

    private boolean changeOptionalAllowed;

    private boolean updatable;

    private boolean changeUpdatableAllowed;

    private boolean basic;

    private boolean multivalue;

    private Class<?> collectionClass;

    private boolean isPassword;

    private boolean isEnumerated;

    private boolean enumClass;

    private Class<?> fieldClass;

    @Override
    public int compareTo(EntityFieldSettings toCompare) {
        return StringUtils.compare(fieldId, toCompare.fieldId);
    }

    @Override
    public String toString() {
        return "EntityFieldSettings{" +
                "id=" + super.getId() + '\'' +
                ", entityName='" + entityName + '\'' +
                ", fieldId='" + fieldId + '\'' +
                ", field='" + field + '\'' +
                ", collectionFieldForShow='" + collectionFieldForShow + '\'' +
                ", name='" + name + '\'' +
                ", excludeField=" + excludeField +
                ", creationExcludeField=" + creationExcludeField +
                ", fieldOrder=" + fieldOrder +
                ", uniqueField=" + uniqueField +
                ", required=" + required +
                ", optional=" + optional +
                ", changeOptionalAllowed=" + changeOptionalAllowed +
                ", updatable=" + updatable +
                ", changeUpdatableAllowed=" + changeUpdatableAllowed +
                ", basic=" + basic +
                ", multivalue=" + multivalue +
                ", collectionClass=" + collectionClass +
                ", isPassword=" + isPassword +
                ", isEnumerated=" + isEnumerated +
                ", enumClass=" + enumClass +
                ", fieldClass=" + fieldClass +
                ", createdAt='" + super.getCreatedAt() +
                ", updatedAt='" + super.getUpdatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var entityFieldSettings = (EntityFieldSettings) o;
        return StringUtils.equals(super.getId(), entityFieldSettings.getId())
                && excludeField == entityFieldSettings.excludeField
                && creationExcludeField == entityFieldSettings.creationExcludeField
                && fieldOrder == entityFieldSettings.fieldOrder
                && uniqueField == entityFieldSettings.uniqueField
                && required == entityFieldSettings.required
                && optional == entityFieldSettings.optional
                && changeOptionalAllowed == entityFieldSettings.changeOptionalAllowed
                && updatable == entityFieldSettings.updatable
                && changeUpdatableAllowed == entityFieldSettings.changeUpdatableAllowed
                && basic == entityFieldSettings.basic
                && multivalue == entityFieldSettings.multivalue
                && isPassword == entityFieldSettings.isPassword
                && isEnumerated == entityFieldSettings.isEnumerated
                && enumClass == entityFieldSettings.enumClass
                && Objects.equals(entityName, entityFieldSettings.entityName)
                && Objects.equals(fieldId, entityFieldSettings.fieldId)
                && Objects.equals(field, entityFieldSettings.field)
                && Objects.equals(collectionFieldForShow, entityFieldSettings.collectionFieldForShow)
                && Objects.equals(name, entityFieldSettings.name)
                && Objects.equals(collectionClass, entityFieldSettings.collectionClass)
                && Objects.equals(fieldClass, entityFieldSettings.fieldClass);
    }

    @Override
    public int hashCode() {
        final int PRIME = 5322;
        int result = 1;
        final var $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}