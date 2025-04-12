package com.backend.controller;

import com.backend.Model.MongoDbTable.MutualFundCollection.MutualFund;
import com.backend.Model.MongoDbTable.MutualFundCollection.MutualFundAmcName;
import com.backend.Model.MongoDbTable.MutualFundCollection.Scheme;
import com.backend.Model.MongoDbTable.MutualFundCollection.SchemeCategory;
import com.backend.repository.MongoRepository.MutualFundAmcRepository;
import com.backend.repository.MongoRepository.MutualFundRepository;
import com.backend.repository.MongoRepository.SchemeCategoryRepository;
import com.backend.repository.MongoRepository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mutual-funds")
public class MutualFundController {

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private SchemeCategoryRepository schemeCategoryRepository;

    @Autowired
    private MutualFundAmcRepository mutualFundAmcRepository;

    @Autowired
    private MutualFundRepository mutualFundRepository;

    /**
     * Get a hierarchical structure of all mutual funds
     * Scheme -> SchemeCategory -> MutualFundAMC -> MutualFund list
     */
    @GetMapping("/hierarchy")
    public ResponseEntity<List<Map<String, Object>>> getMutualFundHierarchy() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Get all schemes
        List<Scheme> schemes = schemeRepository.findAll();
        
        for (Scheme scheme : schemes) {
            Map<String, Object> schemeMap = new HashMap<>();
            schemeMap.put("id", scheme.getId());
            schemeMap.put("schemeType", scheme.getSchemeType());
            schemeMap.put("description", scheme.getDescription());
            
            // Get categories for this scheme
            List<SchemeCategory> categories = schemeCategoryRepository.findAll().stream()
                    .filter(category -> category.getSchemeId() != null && 
                            category.getSchemeId().equals(scheme.getId()))
                    .collect(Collectors.toList());
            
            List<Map<String, Object>> categoryList = new ArrayList<>();
            for (SchemeCategory category : categories) {
                Map<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("id", category.getId());
                categoryMap.put("categoryName", category.getCategoryName());
                categoryMap.put("categoryEnum", category.getCategoryEnum());
                categoryMap.put("description", category.getDescription());
                
                // Get AMCs for this category
                List<MutualFundAmcName> amcs = mutualFundAmcRepository.findAll().stream()
                        .filter(amc -> amc.getSchemeCategoryId() != null && 
                                amc.getSchemeCategoryId().equals(category.getId()))
                        .collect(Collectors.toList());
                
                List<Map<String, Object>> amcList = new ArrayList<>();
                for (MutualFundAmcName amc : amcs) {
                    Map<String, Object> amcMap = new HashMap<>();
                    amcMap.put("id", amc.getId());
                    amcMap.put("amcName", amc.getAmcName());
                    amcMap.put("amcNameEnum", amc.getAmcNameEnum());
                    
                    // Get mutual funds for this AMC
                    List<MutualFund> funds = mutualFundRepository.findAll().stream()
                            .filter(fund -> fund.getAmcId() != null && 
                                    fund.getAmcId().equals(amc.getId()))
                            .collect(Collectors.toList());
                    
                    List<Map<String, Object>> fundList = new ArrayList<>();
                    for (MutualFund fund : funds) {
                        Map<String, Object> fundMap = new HashMap<>();
                        fundMap.put("id", fund.getId());
                        fundMap.put("schemeCode", fund.getSchemeCode());
                        fundMap.put("schemeName", fund.getSchemeName());
                        fundMap.put("isinDivGrowth", fund.getIsinDivGrowth());
                        fundMap.put("isinDivReinvestment", fund.getIsinDivReinvestment());
                        fundMap.put("nav", fund.getNav());
                        fundMap.put("navDate", fund.getNavDate());
                        fundList.add(fundMap);
                    }
                    
                    amcMap.put("mutualFunds", fundList);
                    amcList.add(amcMap);
                }
                
                categoryMap.put("amcs", amcList);
                categoryList.add(categoryMap);
            }
            
            schemeMap.put("categories", categoryList);
            result.add(schemeMap);
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Get all mutual funds
     */
    @GetMapping
    public ResponseEntity<List<MutualFund>> getAllMutualFunds() {
        return ResponseEntity.ok(mutualFundRepository.findAll());
    }
    
    /**
     * Get mutual fund by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MutualFund> getMutualFundById(@PathVariable String id) {
        Optional<MutualFund> fund = mutualFundRepository.findById(id);
        return fund.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * Search mutual funds by scheme name
     */
    @GetMapping("/search")
    public ResponseEntity<List<MutualFund>> searchMutualFunds(@RequestParam String keyword) {
        List<MutualFund> funds = mutualFundRepository.findBySchemeNameContainingIgnoreCase(keyword);
        return ResponseEntity.ok(funds);
    }
    
    /**
     * Get mutual funds by AMC
     */
    @GetMapping("/by-amc/{amcId}")
    public ResponseEntity<List<MutualFund>> getMutualFundsByAmc(@PathVariable String amcId) {
        List<MutualFund> funds = mutualFundRepository.findAll().stream()
                .filter(fund -> fund.getAmcId() != null && fund.getAmcId().equals(amcId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(funds);
    }
    
    /**
     * Get mutual funds by category
     */
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<MutualFund>> getMutualFundsByCategory(@PathVariable String categoryId) {
        // Get all AMCs for this category
        List<MutualFundAmcName> amcs = mutualFundAmcRepository.findAll().stream()
                .filter(amc -> amc.getSchemeCategoryId() != null && 
                        amc.getSchemeCategoryId().equals(categoryId))
                .collect(Collectors.toList());
        
        // Get all funds for these AMCs
        List<MutualFund> funds = new ArrayList<>();
        for (MutualFundAmcName amc : amcs) {
            funds.addAll(mutualFundRepository.findAll().stream()
                    .filter(fund -> fund.getAmcId() != null && 
                            fund.getAmcId().equals(amc.getId()))
                    .collect(Collectors.toList()));
        }
        
        return ResponseEntity.ok(funds);
    }
} 