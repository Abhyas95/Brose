package com.backend.Model.MutualFund.Enum;

public enum OpenEndedSchemeType {

    // Money Market
    MONEY_MARKET("Money Market"),

    // Equity Schemes
    EQUITY_MULTI_CAP_FUND("Equity Scheme - Multi Cap Fund"),
    EQUITY_LARGE_CAP_FUND("Equity Scheme - Large Cap Fund"),
    EQUITY_LARGE_AND_MID_CAP_FUND("Equity Scheme - Large & Mid Cap Fund"),
    EQUITY_MID_CAP_FUND("Equity Scheme - Mid Cap Fund"),
    EQUITY_SMALL_CAP_FUND("Equity Scheme - Small Cap Fund"),
    EQUITY_DIVIDEND_YIELD_FUND("Equity Scheme - Dividend Yield Fund"),
    EQUITY_VALUE_FUND("Equity Scheme - Value Fund"),
    EQUITY_CONTRA_FUND("Equity Scheme - Contra Fund"),
    EQUITY_SECTORAL_THEMATIC("Equity Scheme - Sectoral/ Thematic"),
    EQUITY_ELSS("Equity Scheme - ELSS"),
    EQUITY_FLEXI_CAP_FUND("Equity Scheme - Flexi Cap Fund"),

    // Debt Schemes
    DEBT_OVERNIGHT_FUND("Debt Scheme - Overnight Fund"),
    DEBT_LIQUID_FUND("Debt Scheme - Liquid Fund"),
    DEBT_ULTRA_SHORT_DURATION_FUND("Debt Scheme - Ultra Short Duration Fund"),
    DEBT_LOW_DURATION_FUND("Debt Scheme - Low Duration Fund"),
    DEBT_MONEY_MARKET_FUND("Debt Scheme - Money Market Fund"),
    DEBT_SHORT_DURATION_FUND("Debt Scheme - Short Duration Fund"),
    DEBT_MEDIUM_DURATION_FUND("Debt Scheme - Medium Duration Fund"),
    DEBT_MEDIUM_TO_LONG_DURATION_FUND("Debt Scheme - Medium to Long Duration Fund"),
    DEBT_LONG_DURATION_FUND("Debt Scheme - Long Duration Fund"),
    DEBT_DYNAMIC_BOND("Debt Scheme - Dynamic Bond"),
    DEBT_CORPORATE_BOND_FUND("Debt Scheme - Corporate Bond Fund"),
    DEBT_CREDIT_RISK_FUND("Debt Scheme - Credit Risk Fund"),
    DEBT_BANKING_AND_PSU_FUND("Debt Scheme - Banking and PSU Fund"),
    DEBT_GILT_FUND("Debt Scheme - Gilt Fund"),
    DEBT_GILT_FUND_10_YEAR("Debt Scheme - Gilt Fund with 10 year constant duration"),

    // Hybrid Schemes
    HYBRID_CONSERVATIVE_HYBRID_FUND("Hybrid Scheme - Conservative Hybrid Fund"),
    HYBRID_BALANCED_HYBRID_FUND("Hybrid Scheme - Balanced Hybrid Fund"),
    HYBRID_AGGRESSIVE_HYBRID_FUND("Hybrid Scheme - Aggressive Hybrid Fund"),
    HYBRID_DYNAMIC_ASSET_ALLOCATION("Hybrid Scheme - Dynamic Asset Allocation or Balanced Advantage"),
    HYBRID_MULTI_ASSET_ALLOCATION("Hybrid Scheme - Multi Asset Allocation"),
    HYBRID_ARBITRAGE_FUND("Hybrid Scheme - Arbitrage Fund"),
    HYBRID_EQUITY_SAVINGS("Hybrid Scheme - Equity Savings"),

    // Solution Oriented Schemes
    SOLUTION_RETIREMENT_FUND("Solution Oriented Scheme - Retirement Fund"),
    SOLUTION_CHILDRENS_FUND("Solution Oriented Scheme - Children’s Fund"),

    // Other Schemes
    OTHER_INDEX_FUNDS("Other Scheme - Index Funds"),
    OTHER_GOLD_ETF("Other Scheme - Gold ETF"),
    OTHER_FOF_OVERSEAS("Other Scheme - FoF Overseas"),
    OTHER_FOF_DOMESTIC("Other Scheme - FoF Domestic");

    private final String description;

    OpenEndedSchemeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

