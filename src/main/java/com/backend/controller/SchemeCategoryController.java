package com.backend.controller;

import com.backend.Model.MongoDbTable.MutualFundCollection.SchemeCategory;
import com.backend.services.SchemeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/scheme-categories")
public class SchemeCategoryController {

    @Autowired
    private SchemeCategoryService schemeCategoryService;

    // ✅ Create a new scheme category
    @PostMapping
    public SchemeCategory createSchemeCategory(@RequestBody SchemeCategory schemeCategory) {
        return schemeCategoryService.saveSchemeCategory(schemeCategory);
    }

    // ✅ Get all scheme categories
    @GetMapping
    public List<SchemeCategory> getAllSchemeCategories() {
        return schemeCategoryService.getAllSchemeCategories();
    }

    // ✅ Get scheme category by ID
    @GetMapping("/{id}")
    public Optional<SchemeCategory> getSchemeCategoryById(@PathVariable String id) {
        return schemeCategoryService.getSchemeCategoryById(id);
    }

    // ✅ Update an existing scheme category
    @PutMapping("/{id}")
    public SchemeCategory updateSchemeCategory(
            @PathVariable String id,
            @RequestBody SchemeCategory updatedCategory
    ) {
        return schemeCategoryService.updateSchemeCategory(id, updatedCategory);
    }

    // ✅ Delete a scheme category by ID
    @DeleteMapping("/{id}")
    public String deleteSchemeCategory(@PathVariable String id) {
        schemeCategoryService.deleteSchemeCategory(id);
        return "Scheme category with ID " + id + " has been deleted.";
    }
}

