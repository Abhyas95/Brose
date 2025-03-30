package com.backend.services;

import com.backend.Model.MongoDbTable.MutualFundCollection.SchemeCategory;
import com.backend.repository.MongoRepository.SchemeCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchemeCategoryService {

    @Autowired
    private SchemeCategoryRepository schemeCategoryRepository;

    // ✅ Save a new scheme category
    public SchemeCategory saveSchemeCategory(SchemeCategory schemeCategory) {
        return schemeCategoryRepository.save(schemeCategory);
    }

    // ✅ Get all scheme categories
    public List<SchemeCategory> getAllSchemeCategories() {
        return schemeCategoryRepository.findAll();
    }

    // ✅ Get scheme category by ID
    public Optional<SchemeCategory> getSchemeCategoryById(String id) {
        return schemeCategoryRepository.findById(id);
    }

    // ✅ Update an existing scheme category
    public SchemeCategory updateSchemeCategory(String id, SchemeCategory updatedCategory) {
        SchemeCategory existingCategory = schemeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scheme Category not found!"));

        existingCategory.setSchemeType(updatedCategory.getSchemeType());
        existingCategory.setCategoryName(updatedCategory.getCategoryName());
        existingCategory.setDescription(updatedCategory.getDescription());

        return schemeCategoryRepository.save(existingCategory);
    }

    // ✅ Delete a scheme category by ID
    public void deleteSchemeCategory(String id) {
        schemeCategoryRepository.deleteById(id);
    }
}

