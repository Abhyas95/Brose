package com.backend.controller;


import com.backend.Model.MongoDbTable.StocksDetails;
import com.backend.services.StockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stocks")
public class StockDataController {

    @Autowired
    private StockDataService stockDataService;

    // Get all stock data
    @GetMapping
    public List<StocksDetails> getAllStockData() {
        return stockDataService.getAllStocks();
    }

    // Get stock data by ID
    @GetMapping("/{id}")
    public ResponseEntity<StocksDetails> getStockDataById(@PathVariable String id) {
        Optional<StocksDetails> stockData = Optional.ofNullable(stockDataService.getStockDataById(id));
        return stockData.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new stock data
    @PostMapping
    public StocksDetails createStockData(@RequestBody StocksDetails stockData) {
        return stockDataService.saveStockData(stockData);
    }

    // Update existing stock data
    @PutMapping("/{id}")
    public ResponseEntity<StocksDetails> updateStockData(@PathVariable String id, @RequestBody StocksDetails newStockData) {
        Optional<StocksDetails> existingStockData = Optional.ofNullable(stockDataService.getStockDataById(id));
        if (existingStockData.isPresent()) {
            StocksDetails stockData = existingStockData.get();
            stockData.setStockName(newStockData.getStockName());
            stockData.setCurrentPrice(newStockData.getCurrentPrice());
            stockData.setRecommendedDate(newStockData.getRecommendedDate());
            stockData.setFirstTargetPrice(newStockData.getFirstTargetPrice());
            stockData.setFirstTargetGrowth(newStockData.getFirstTargetGrowth());
            stockData.setSecondTargetPrice(newStockData.getSecondTargetPrice());
            stockData.setSecondTargetGrowth(newStockData.getSecondTargetGrowth());
            stockData.setThirdTargetPrice(newStockData.getThirdTargetPrice());
            stockData.setThirdTargetGrowth(newStockData.getThirdTargetGrowth());
            stockData.setExpectedTargetDate(newStockData.getExpectedTargetDate());
            stockData.setCompanySize(newStockData.getCompanySize());
            stockData.setSector(newStockData.getSector());
            stockData.setSharesOutstanding(newStockData.getSharesOutstanding());
            stockData.setMarketCap(newStockData.getMarketCap());
            stockData.setPEratio(newStockData.getPEratio());
            stockData.setDividendYield(newStockData.getDividendYield());

            StocksDetails updatedStockData = stockDataService.saveStockData(stockData);
            return ResponseEntity.ok(updatedStockData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete stock data by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockData(@PathVariable String id) {
        Optional<StocksDetails> stockData = Optional.ofNullable(stockDataService.getStockDataById(id));
        if (stockData.isPresent()) {
            stockDataService.deleteStockData(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
