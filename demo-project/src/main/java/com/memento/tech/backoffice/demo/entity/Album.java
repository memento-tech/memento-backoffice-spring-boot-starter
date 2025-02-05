package com.memento.tech.backoffice.demo.entity;

import com.memento.tech.backoffice.annotations.BackofficeFieldForShowInList;
import com.memento.tech.backoffice.annotations.BackofficeForbidUpdate;
import com.memento.tech.backoffice.annotations.BackofficeGroup;
import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.entity.Translation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;
import java.util.List;

@Entity
@BackofficeFieldForShowInList("name")
@BackofficeOrderPriority(99)
@BackofficeGroup(title = "Some Group")
public class Album extends BaseEntity {

    @Column(nullable = false, unique = true)
    @BackofficeOrderPriority(100)
    private String name;

    @Column
    private LocalDate publishDate;

    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @BackofficeOrderPriority(98)
    private Translation description;

    @OneToMany
    private List<Record> records;
}
