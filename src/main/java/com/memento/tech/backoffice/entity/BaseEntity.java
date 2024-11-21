package com.memento.tech.backoffice.entity;

import com.memento.tech.backoffice.annotations.BackofficeForbidUpdate;
import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.annotations.BackofficeTitle;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OrderBy;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    @BackofficeOrderPriority(-1001)
    @BackofficeTitle("ID")
    @OrderBy
    private String id;

    @Column(updatable = false)
    @CreationTimestamp
    @BackofficeOrderPriority(-1000)
    @BackofficeTitle("Created at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @BackofficeOrderPriority(-1000)
    @BackofficeTitle("Updated at")
    @BackofficeForbidUpdate
    private LocalDateTime updatedAt;
}