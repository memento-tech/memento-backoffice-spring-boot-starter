package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeExclude;
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

@BackofficeExclude
public class EntityFieldSettings extends BaseEntity implements Comparable<EntityFieldSettings>, BackofficeConfigurationMarker {

    @Column(nullable = false)
    @BackofficeTitle("Entity Name")
    @BackofficeOrderPriority(101)
    private String entityName;

    @Column(nullable = false, unique = true)
    @BackofficeTitle("Field ID")
    @BackofficeOrderPriority(100)
    private String fieldId;

    @BackofficeExclude
    private String field;

    @BackofficeTitle("Collection Title For Show")
    private String collectionFieldForShow;

    @BackofficeTitle("Field Name")
    @BackofficeOrderPriority(99)
    private String name;

    @BackofficeTitle("Field Exclude")
    private boolean excludeField;

    @BackofficeTitle("Creation Field Exclude")
    private boolean creationExcludeField;

    @BackofficeTitle("Field Order")
    private int fieldOrder;

    @BackofficeTitle("Unique")
    @BackofficeOrderPriority(97)
    private boolean uniqueField;

    @BackofficeTitle("Required")
    @BackofficeOrderPriority(97)
    private boolean required;

    @BackofficeTitle("Optional")
    @BackofficeOrderPriority(96)
    private boolean optional;

    @Column(updatable = false)
    @BackofficeTitle("Change Optional Allowed")
    @BackofficeOrderPriority(95)
    private boolean changeOptionalAllowed;

    @BackofficeTitle("Updatable")
    @BackofficeOrderPriority(94)
    private boolean updatable;

    @Column(updatable = false)
    @BackofficeTitle("Change Updatable Allowed")
    @BackofficeOrderPriority(93)
    private boolean changeUpdatableAllowed;

    @Column(updatable = false)
    @BackofficeTitle("Basic")
    @BackofficeOrderPriority(98)
    private boolean basic;

    @BackofficeTitle("Multivalue Field")
    @BackofficeOrderPriority(98)
    private boolean multivalue;

    @BackofficeTitle("Collection Class")
    private Class<?> collectionClass;

    @BackofficeExclude
    private boolean isPassword;

    @BackofficeExclude
    private boolean isEnumerated;

    @BackofficeExclude
    private boolean enumClass;

    @BackofficeTitle("Field Class")
    @BackofficeOrderPriority(92)
    private Class<?> fieldClass;

    @Override
    public int compareTo(EntityFieldSettings toCompare) {
        return StringUtils.compare(fieldId, toCompare.fieldId);
    }

    @Override
    public String toString() {
        return "EntityFieldSettings{" +
                "id=" + super.getId() +
                ", entityName=" + entityName +
                ", fieldId=" + fieldId +
                ", field=" + field +
                ", collectionFieldForShow=" + collectionFieldForShow +
                ", name=" + name +
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
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                "}";
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