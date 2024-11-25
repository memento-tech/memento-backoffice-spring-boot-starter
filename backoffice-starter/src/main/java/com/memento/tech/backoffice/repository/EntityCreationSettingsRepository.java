package com.memento.tech.backoffice.repository;

import com.memento.tech.backoffice.entity.EntityCreationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityCreationSettingsRepository extends JpaRepository<EntityCreationSettings, Integer> {

}