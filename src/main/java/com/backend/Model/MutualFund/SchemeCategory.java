package com.backend.Model.MutualFund;

public enum SchemeCategory {
    MULTI_CAP_FUND("Equity Scheme - Multi Cap Fund"),
    OTHER_CATEGORY("Other Category"); // Add more if needed

    private final String description;

    SchemeCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

