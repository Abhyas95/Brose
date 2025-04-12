package com.backend.repository.MongoRepository;

import com.backend.Model.MongoDbTable.MutualFundCollection.MutualFundAmcName;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MutualFundAmcRepository extends MongoRepository<MutualFundAmcName, String> {
  MutualFundAmcName findByAmcName(String amcName);
}