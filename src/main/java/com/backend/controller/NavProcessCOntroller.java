package com.backend.controller;

import com.backend.services.NavReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NavProcessCOntroller {

    @Autowired
    NavReportService navReportService;

    @GetMapping("/nav")
    private ResponseEntity<String>processNav() {
        return navReportService.reportData();
    }

}
