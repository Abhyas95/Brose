package com.backend.repository.MongoRepository;

import com.backend.Model.MongoDbTable.MutualFundCollection.SchemeCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SchemeCategoryRepository extends MongoRepository<SchemeCategory, String> {
    SchemeCategory  findByCategoryName (String categoryName);
}