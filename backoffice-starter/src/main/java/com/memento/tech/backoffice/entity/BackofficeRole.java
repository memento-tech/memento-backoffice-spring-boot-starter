package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeExclude;
import com.memento.tech.backoffice.annotations.BackofficeGroup;
import com.memento.tech.backoffice.annotations.BackofficeTitle;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

@Entity
@Table(indexes = {
        @Index(columnList = "id", unique = true),
})

@BackofficeTitle("Backoffice Role")
@BackofficeGroup(title = "Backoffice")
public class BackofficeRole extends BaseEntity implements BackofficeConfigurationMarker {

    @BackofficeTitle("Role ID")
    private String roleId;

    public SimpleGrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + this.getRoleId());
    }

    @Override
    public String toString() {
        return "BackofficeRole{" +
                "id=" + super.getId() +
                ", roleId=" + roleId +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BackofficeRole that = (BackofficeRole) o;
        return Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roleId);
    }
}
