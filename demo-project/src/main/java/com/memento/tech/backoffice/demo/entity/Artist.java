package com.memento.tech.backoffice.demo.entity;

import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.entity.Translation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;
import java.util.List;

@Entity
@BackofficeOrderPriority(100)
public class Artist extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String firstName;

    @Column(nullable = false, unique = true)
    private String lastName;

    @Column
    private LocalDate dateOfBirth;

    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    private Translation bio;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Album> albums;

}
