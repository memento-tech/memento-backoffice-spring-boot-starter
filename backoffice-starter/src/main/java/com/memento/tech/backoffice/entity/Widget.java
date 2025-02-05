package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeExclude;
import com.memento.tech.backoffice.annotations.BackofficeForbidUpdate;
import com.memento.tech.backoffice.annotations.BackofficeGroup;
import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.annotations.BackofficeTitle;
import com.memento.tech.backoffice.widget.WidgetHandler;
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
@BackofficeGroup(title = "Backoffice")
public class Widget extends BaseEntity implements BackofficeConfigurationMarker {

    @Column(nullable = false, unique = true)
    @BackofficeTitle("Widget ID")
    @BackofficeOrderPriority(100)
    private String widgetId;

    @BackofficeTitle("Entity Level")
    @BackofficeOrderPriority(97)
    private boolean entityLevel;

    @BackofficeTitle("Record Level")
    @BackofficeOrderPriority(96)
    private boolean recordLevel;

    @BackofficeTitle("Label")
    @BackofficeOrderPriority(99)
    private String label;

    @Column(nullable = false, unique = true)
    @SuppressWarnings("rawtypes")
    @BackofficeForbidUpdate
    private Class<? extends WidgetHandler> widgetHandlerClass;

    @Column(nullable = false, unique = true)
    @BackofficeTitle("Handler Mapping")
    @BackofficeOrderPriority(98)
    private String handlerMapping;

    @Override
    public String toString() {
        return "Widget{" +
                "id=" + super.getId() +
                ", widgetId=" + widgetId +
                ", entityLevel=" + entityLevel +
                ", recordLevel=" + recordLevel +
                ", label=" + label +
                ", widgetHandlerClass=" + widgetHandlerClass +
                ", handlerMapping=" + handlerMapping +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var widget = (Widget) o;
        return StringUtils.equals(super.getId(), widget.getId())
                && entityLevel == widget.entityLevel
                && recordLevel == widget.recordLevel
                && Objects.equals(widgetId, widget.widgetId)
                && Objects.equals(label, widget.label)
                && Objects.equals(widgetHandlerClass, widget.widgetHandlerClass)
                && Objects.equals(handlerMapping, widget.handlerMapping);
    }

    @Override
    public int hashCode() {
        final int PRIME = 9281;
        int result = 1;
        final var $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}
