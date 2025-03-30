package com.backend.services;

import com.backend.Model.MongoDbTable.MutualFundCollection.Scheme;
import com.backend.repository.MongoRepository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchemeService {

    @Autowired
    private SchemeRepository schemeRepository;

    // Save a new scheme
    public Scheme saveScheme(Scheme scheme) {
        return schemeRepository.save(scheme);
    }

    // Get all schemes
    public List<Scheme> getAllSchemes() {
        return schemeRepository.findAll();
    }

    // Update scheme and modify the description
    public Scheme updateScheme(String id, String newDescription) {
        Scheme scheme = schemeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scheme not found!"));
        scheme.setDescription(newDescription);
        return schemeRepository.save(scheme);
    }

    // Get scheme by ID
    public Optional<Scheme> getSchemeById(String id) {
        return schemeRepository.findById(id);
    }
}
