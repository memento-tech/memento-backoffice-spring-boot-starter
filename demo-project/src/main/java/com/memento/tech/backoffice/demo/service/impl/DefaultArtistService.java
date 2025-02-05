package com.memento.tech.backoffice.demo.service.impl;

import com.memento.tech.backoffice.demo.entity.Artist;
import com.memento.tech.backoffice.demo.repository.ArtistRepository;
import com.memento.tech.backoffice.demo.service.ArtistService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultArtistService implements ArtistService {

    private final ArtistRepository artistRepository;

    public DefaultArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    @Override
    public Optional<Artist> getArtistForId(final String id) {
        return artistRepository.findById(id);
    }
}
