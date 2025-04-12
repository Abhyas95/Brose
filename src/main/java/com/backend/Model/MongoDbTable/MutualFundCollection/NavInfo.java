package com.backend.Model.MongoDbTable.MutualFundCollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NavInfo {
    private BigDecimal nav;
    private String date;
} 