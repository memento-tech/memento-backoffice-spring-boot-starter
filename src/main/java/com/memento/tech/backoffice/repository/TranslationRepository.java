package com.memento.tech.backoffice.repository;

import com.memento.tech.backoffice.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, String> {

    Optional<Translation> findByCode(String code);
}
