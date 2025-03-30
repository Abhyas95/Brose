package com.backend.Model.MutualFund;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MutualFundScheme {
    private int schemeCode;                 // Scheme Code
    private String schemeName;              // Scheme Name
    private String isinDivGrowth;           // ISIN for Growth/Dividend
    private String isinDivReinvestment;     // ISIN for IDCW Reinvestment
    private double nav;                     // Net Asset Value
    private String date;                    // Date of NAV
}

