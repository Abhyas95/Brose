package com.backend.controller;

import com.backend.Model.MongoDbTable.MutualFundCollection.Scheme;
import com.backend.services.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schemes")
public class SchemeController {

    @Autowired
    private SchemeService schemeService;

    @PostMapping
    public Scheme createScheme(@RequestBody Scheme scheme) {
        return schemeService.saveScheme(scheme);
    }

    @GetMapping
    public List<Scheme> getAllSchemes() {
        return schemeService.getAllSchemes();
    }

    @GetMapping("/{id}")
    public Optional<Scheme> getSchemeById(@PathVariable String id) {
        return schemeService.getSchemeById(id);
    }

    @PutMapping("/{id}")
    public Scheme updateScheme(@PathVariable String id, @RequestBody String newDescription) {
        return schemeService.updateScheme(id, newDescription);
    }
}

