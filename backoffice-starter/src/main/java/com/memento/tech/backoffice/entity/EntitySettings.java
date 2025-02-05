package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeDisableCreation;
import com.memento.tech.backoffice.annotations.BackofficeExclude;
import com.memento.tech.backoffice.annotations.BackofficeForbidUpdate;
import com.memento.tech.backoffice.annotations.BackofficeGroup;
import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.annotations.BackofficeTitle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;
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

@BackofficeDisableCreation
@BackofficeTitle("Entity Settings")
@BackofficeGroup(title = "Backoffice", order = -1000)
public class EntitySettings extends BaseEntity implements Comparable<EntitySettings>, BackofficeConfigurationMarker {

    @Column(nullable = false, unique = true)
    @BackofficeTitle("Name")
    @BackofficeOrderPriority(100)
    @BackofficeForbidUpdate
    private String entityName;

    @Column(nullable = false, unique = true)
    @BackofficeTitle("Title")
    @BackofficeOrderPriority(99)
    @BackofficeForbidUpdate
    private String title;

    @BackofficeTitle("Entity Exclude")
    private boolean excludeEntity;

    @BackofficeTitle("Full Entity Exclude")
    private boolean fullEntityExclude;

    @BackofficeTitle("Disable Creation")
    private boolean disableCreation;

    @Column(nullable = false, unique = true)
    @BackofficeTitle("Table Name")
    @BackofficeOrderPriority(98)
    @BackofficeForbidUpdate
    private String tableName;

    @BackofficeTitle("Entity Order")
    private int entityOrder;

    @BackofficeTitle("Entity Group")
    private String entityGroup;

    @BackofficeTitle("Group Order")
    private int groupOrder;

    @BackofficeTitle("Field For Show")
    private String fieldForShowInList;

    @BackofficeExclude
    private boolean translation;

    @BackofficeExclude
    private boolean media;

    @ManyToMany
    @Cascade(value = CascadeType.DELETE_ORPHAN)
    @BackofficeTitle("Field Settings")
    @BackofficeOrderPriority(95)
    private Set<EntityFieldSettings> fieldSettings;

    @ManyToOne
    @Cascade(value = CascadeType.DELETE_ORPHAN)
    @BackofficeTitle("Creation Settings")
    @BackofficeOrderPriority(96)
    private EntityCreationSettings creationSettings;

    @BackofficeTitle("Entity Class")
    @BackofficeOrderPriority(97)
    @BackofficeForbidUpdate
    private Class<? extends BaseEntity> entityClass;

    @OneToMany
    @BackofficeTitle("Entity Widgets")
    private List<Widget> entityWidgets;

    @Override
    public int compareTo(EntitySettings entitySettings) {
        return entityName.compareTo(entitySettings.getEntityName());
    }

    @Override
    public String toString() {
        return "EntitySettings{" +
                "id=" + super.getId() +
                ", entityName=" + entityName +
                ", title=" + title +
                ", excludeEntity=" + excludeEntity +
                ", fullEntityExclude=" + fullEntityExclude +
                ", disableCreation=" + disableCreation +
                ", tableName=" + tableName +
                ", entityOrder=" + entityOrder +
                ", entityGroup=" + entityGroup +
                ", fieldForShowInList=" + fieldForShowInList +
                ", translation=" + translation +
                ", media=" + media +
                ", fieldSettings=" + fieldSettings +
                ", creationSettings=" + creationSettings +
                ", entityClass=" + entityClass +
                ", entityWidgets=" + entityWidgets +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var entitySettings = (EntitySettings) o;
        return StringUtils.equals(super.getId(), entitySettings.getId())
                && excludeEntity == entitySettings.excludeEntity
                && fullEntityExclude == entitySettings.fullEntityExclude
                && disableCreation == entitySettings.disableCreation
                && entityOrder == entitySettings.entityOrder
                && translation == entitySettings.translation
                && media == entitySettings.media
                && Objects.equals(entityName, entitySettings.entityName)
                && Objects.equals(title, entitySettings.title)
                && Objects.equals(tableName, entitySettings.tableName)
                && Objects.equals(entityGroup, entitySettings.entityGroup)
                && Objects.equals(fieldForShowInList, entitySettings.fieldForShowInList)
                && Objects.equals(fieldSettings, entitySettings.fieldSettings)
                && Objects.equals(creationSettings, entitySettings.creationSettings)
                && Objects.equals(entityClass, entitySettings.entityClass)
                && Objects.equals(entityWidgets, entitySettings.entityWidgets);
    }

    @Override
    public int hashCode() {
        final int PRIME = 9928;
        int result = 1;
        final var $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}