package com.memento.tech.backoffice.demo.repository;

import com.memento.tech.backoffice.demo.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {

}
