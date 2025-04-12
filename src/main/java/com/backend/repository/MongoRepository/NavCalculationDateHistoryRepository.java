package com.backend.repository.MongoRepository;

import com.backend.Model.MongoDbTable.MutualFundCollection.NavCalculationDateHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NavCalculationDateHistoryRepository extends MongoRepository<NavCalculationDateHistory, String> {
    
    /**
     * Find NAV calculation history by calculation date
     */
    Optional<NavCalculationDateHistory> findByCalculationDate(String calculationDate);
    
    /**
     * Find NAV calculation history by calculation date and month
     */
    Optional<NavCalculationDateHistory> findByCalculationDateAndCalculationMonth(String calculationDate, String calculationMonth);
    
    /**
     * Find all NAV calculation histories for a specific month
     */
    List<NavCalculationDateHistory> findByCalculationMonth(String calculationMonth);
    
    /**
     * Find all pending calculations between two dates
     */
    List<NavCalculationDateHistory> findByCalculationDateBetweenAndIsCalculatedFalse(String startDate, String endDate);
    
    /**
     * Find all successful calculations for a specific month
     */
    List<NavCalculationDateHistory> findByCalculationMonthAndCalculationStatus(String calculationMonth, String calculationStatus);
} 