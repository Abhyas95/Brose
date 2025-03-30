package com.backend.repository.MongoRepository;

import com.backend.Model.MongoDbTable.MutualFundCollection.MutualFund;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MutualFundRepository extends MongoRepository<MutualFund, String> {

    // Find by Scheme Code
    Optional<MutualFund> findBySchemeCode(int schemeCode);

    // Find by ISIN for Growth/Dividend
    Optional<MutualFund> findByIsinDivGrowth(String isinDivGrowth);

    // Find by ISIN for Reinvestment
    Optional<MutualFund> findByIsinDivReinvestment(String isinDivReinvestment);

    // Find all by Scheme Name containing a keyword (case-insensitive)
    List<MutualFund> findBySchemeNameContainingIgnoreCase(String schemeName);

    // Find by NAV Date
    List<MutualFund> findByNavDate(String navDate);
}

