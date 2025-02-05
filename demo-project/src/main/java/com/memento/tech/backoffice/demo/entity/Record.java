package com.memento.tech.backoffice.demo.entity;

import com.memento.tech.backoffice.annotations.BackofficeFieldForShowInList;
import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.entity.Media;
import com.memento.tech.backoffice.entity.Translation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.List;

@Entity
@BackofficeOrderPriority(98)
public class Record extends BaseEntity {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Translation> songs;

    @OneToOne(optional = false, orphanRemoval = true)
    private Media cover;

}
