package com.memento.tech.backoffice.repository;

import com.memento.tech.backoffice.entity.EntitySettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntitySettingsRepository extends JpaRepository<EntitySettings, Integer> {

    Optional<EntitySettings> findByEntityName(String entityName);
}