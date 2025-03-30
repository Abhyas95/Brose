package com.backend.Model.MutualFund.Enum;

public enum SchemeType {
    OPEN_ENDED_SCHEME("Open-Ended Scheme"),
    CLOSED_ENDED_SCHEME("Closed-Ended Scheme"),
    FIXED_ENDED_SCHEME("Fixed-Ended Scheme");

    private final String description;

    SchemeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
