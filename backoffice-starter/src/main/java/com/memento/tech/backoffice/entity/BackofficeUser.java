package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeExclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

@Entity
@Table(indexes = {
        @Index(columnList = "id", unique = true),
})

@BackofficeExclude
public class BackofficeUser extends BaseEntity implements UserDetails {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            joinColumns = @JoinColumn(name = "backoffice_user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "backoffice_role_id", nullable = false)
    )
    private List<BackofficeRole> assignedRoles;

    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return assignedRoles
                .stream()
                .map(BackofficeRole::getAuthority)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String toString() {
        return "BackofficeUser{" +
                "id=" + super.getId() +
                ", username=" + username +
                ", password=" + password +
                ", assignedRoles=" + assignedRoles +
                ", enabled=" + enabled +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BackofficeUser that = (BackofficeUser) o;
        return enabled == that.enabled && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(assignedRoles, that.assignedRoles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, assignedRoles, enabled);
    }
}
