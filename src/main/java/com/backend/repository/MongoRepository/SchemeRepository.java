package com.backend.repository.MongoRepository;

import com.backend.Model.MongoDbTable.MutualFundCollection.Scheme;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SchemeRepository extends MongoRepository<Scheme, String> {
    Scheme findByDescription(String description);
}

