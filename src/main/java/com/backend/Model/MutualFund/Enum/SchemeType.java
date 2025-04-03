package com.backend.Model.MutualFund.Enum;

public enum SchemeType {
    OPEN_ENDED_SCHEMES("Open-Ended Scheme"),
    CLOSE_ENDED_SCHEMES("Closed-Ended Scheme"),
    FIXED_ENDED_SCHEMES("Fixed-Ended Scheme"),
    INTERVAL_FUND_SCHEMES("Interval Fund Scheme");

    private final String description;

    SchemeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static SchemeType fromName(String name) {
        try {
            return SchemeType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No enum found for name: " + name);
        }
    }

}
