package com.backend.Model.MutualFund;

public enum FundHouse {
    ADITYA_BIRLA_SUN_LIFE("Aditya Birla Sun Life Mutual Fund"),
    AXIS_MUTUAL_FUND("Axis Mutual Fund"),
    OTHER_HOUSE("Other Fund House"); // Add more if required

    private final String fundHouseName;

    FundHouse(String fundHouseName) {
        this.fundHouseName = fundHouseName;
    }

    public String getFundHouseName() {
        return fundHouseName;
    }
}
