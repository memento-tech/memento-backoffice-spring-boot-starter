package com.memento.tech.backoffice.demo.service;

import com.memento.tech.backoffice.demo.entity.Artist;

import java.util.List;
import java.util.Optional;

public interface ArtistService {

    List<Artist> getAllArtists();

    Optional<Artist> getArtistForId(final String id);
}
