package com.backend.Model.MutualFund;

import com.backend.Model.MutualFund.Enum.SchemeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MutualFund {
    private FundHouse fundHouse;
    private SchemeType schemeType;
    private SchemeCategory schemeCategory;
    private List<MutualFundScheme> schemes;
}
