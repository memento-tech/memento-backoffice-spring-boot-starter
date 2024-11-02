package com.memento.tech.backoffice.repository;

import com.memento.tech.backoffice.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    Optional<Language> findByLangIsoCode(String langIsoCode);
}