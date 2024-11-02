package com.memento.tech.backoffice.repository;

import com.memento.tech.backoffice.entity.EntitySettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntitySettingsRepository extends JpaRepository<EntitySettings, Integer> {

    Optional<EntitySettings> findByEntityName(String entityName);
}