package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeExclude;
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
public class Widget extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String widgetId;

    private boolean entityLevel;

    private boolean recordLevel;

    private String label;

    @Column(nullable = false)
    @SuppressWarnings("rawtypes")
    private Class<? extends WidgetHandler> widgetHandlerClass;

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
