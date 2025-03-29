package com.backend.repository.MongoRepository;

import com.backend.Model.MongoDbTable.StocksDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockDataRepository extends MongoRepository<StocksDetails, String> {
}
