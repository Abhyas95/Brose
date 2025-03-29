package com.backend.services;

import com.backend.Model.MongoDbTable.StocksDetails;
import com.backend.repository.MongoRepository.StockDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StockDataService {
    @Autowired
    private StockDataRepository stockDataRepository;

    public List<StocksDetails> getAllStocks() {
        return stockDataRepository.findAll();
    }

    public StocksDetails saveStockData(StocksDetails stockData) {
        return stockDataRepository.save(stockData);
    }

    public StocksDetails getStockDataById(String id) {
        return stockDataRepository.findById(id).orElse(null);
    }

    public void deleteStockData(String id) {
        stockDataRepository.deleteById(id);
    }
}
