package com.memento.tech.backoffice.repository;

import com.memento.tech.backoffice.entity.EntityFieldSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityFieldSettingsRepository extends JpaRepository<EntityFieldSettings, Integer> {

}