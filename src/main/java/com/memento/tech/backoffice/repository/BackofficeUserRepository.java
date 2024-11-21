package com.memento.tech.backoffice.repository;

import com.memento.tech.backoffice.entity.BackofficeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackofficeUserRepository extends JpaRepository<BackofficeUser, String> {

    Optional<BackofficeUser> findByUsername(final String username);

}
