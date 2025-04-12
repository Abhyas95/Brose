package com.backend.Model.MongoDbTable.MutualFundCollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.util.Date;

@Document(collection = "nav_calculation_date_history")
@CompoundIndex(name = "date_month_idx", def = "{'calculationDate': 1, 'calculationMonth': 1}", unique = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NavCalculationDateHistory {
    // Primary identifier fields
    private String calculationDate;  // Date for which NAV calculation is done (yyyy-MM-dd)
    private String calculationMonth;  // Month of calculation (yyyy-MM)

    // Calculation status
    private boolean isCalculated;  // Whether NAV is calculated for this date
    private Date calculationTimestamp;  // When the calculation was performed
    private String calculationStatus;  // Status of calculation (SUCCESS, FAILED, PENDING)

    // Calculation statistics
    private int totalFunds;  // Total number of funds
    private int calculatedFunds;  // Number of funds calculated
    private int failedFunds;  // Number of funds with failed calculations

    // Audit fields
    @CreatedDate
    private Date createdOn;
    @LastModifiedDate
    private Date lastUpdatedOn;
} 