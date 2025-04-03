package com.backend.controller;

import com.backend.services.MarketDataImporterService;
import com.backend.services.NavReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NavProcessCOntroller {

    @Autowired
    NavReportService navReportService;

    @Autowired
    MarketDataImporterService marketDataImporterService;

    @GetMapping("/nav")
    private ResponseEntity<String>processNav() {
        try {
            marketDataImporterService.importMarketData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("NAV Processed Successfully");
    }

}
