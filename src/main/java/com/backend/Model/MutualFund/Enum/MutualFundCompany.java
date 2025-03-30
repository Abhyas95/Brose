package com.backend.Model.MutualFund.Enum;

public enum MutualFundCompany {

    // List of all mutual fund companies
    ONE_MUTUAL_FUND_FORMERLY_KNOWN_AS_IIFL_MUTUAL_FUND("360 ONE Mutual Fund (Formerly Known as IIFL Mutual Fund)"),
    ADITYA_BIRLA_SUN_LIFE_MUTUAL_FUND("Aditya Birla Sun Life Mutual Fund"),
    ANGEL_ONE_MUTUAL_FUND("Angel One Mutual Fund"),
    AXIS_MUTUAL_FUND("Axis Mutual Fund"),
    BAJAJ_FINSERV_MUTUAL_FUND("Bajaj Finserv Mutual Fund"),
    BANDHAN_MUTUAL_FUND("Bandhan Mutual Fund"),
    BANK_OF_INDIA_MUTUAL_FUND("Bank of India Mutual Fund"),
    BARODA_BNP_PARIBAS_MUTUAL_FUND("Baroda BNP Paribas Mutual Fund"),
    CANARA_ROBECO_MUTUAL_FUND("Canara Robeco Mutual Fund"),
    DSP_MUTUAL_FUND("DSP Mutual Fund"),
    EDELWEISS_MUTUAL_FUND("Edelweiss Mutual Fund"),
    FRANKLIN_TEMPLETON_MUTUAL_FUND("Franklin Templeton Mutual Fund"),
    GROWW_MUTUAL_FUND("Groww Mutual Fund"),
    HDFC_MUTUAL_FUND("HDFC Mutual Fund"),
    HSBC_MUTUAL_FUND("HSBC Mutual Fund"),
    HELIOS_MUTUAL_FUND("Helios Mutual Fund"),
    ICICI_PRUDENTIAL_MUTUAL_FUND("ICICI Prudential Mutual Fund"),
    ITI_MUTUAL_FUND("ITI Mutual Fund"),
    INVESCO_MUTUAL_FUND("Invesco Mutual Fund"),
    JM_FINANCIAL_MUTUAL_FUND("JM Financial Mutual Fund"),
    KOTAK_MAHINDRA_MUTUAL_FUND("Kotak Mahindra Mutual Fund"),
    LIC_MUTUAL_FUND("LIC Mutual Fund"),
    MAHINDRA_MANULIFE_MUTUAL_FUND("Mahindra Manulife Mutual Fund"),
    MIRAE_ASSET_MUTUAL_FUND("Mirae Asset Mutual Fund"),
    MOTILAL_OSWAL_MUTUAL_FUND("Motilal Oswal Mutual Fund"),
    NJ_MUTUAL_FUND("NJ Mutual Fund"),
    NAVI_MUTUAL_FUND("Navi Mutual Fund"),
    NIPPON_INDIA_MUTUAL_FUND("Nippon India Mutual Fund"),
    OLD_BRIDGE_MUTUAL_FUND("Old Bridge Mutual Fund"),
    PGIM_INDIA_MUTUAL_FUND("PGIM India Mutual Fund"),
    PPFAS_MUTUAL_FUND("PPFAS Mutual Fund"),
    QUANTUM_MUTUAL_FUND("Quantum Mutual Fund"),
    SBI_MUTUAL_FUND("SBI Mutual Fund"),
    SAMCO_MUTUAL_FUND("Samco Mutual Fund"),
    SHRIRAM_MUTUAL_FUND("Shriram Mutual Fund"),
    SUNDARAM_MUTUAL_FUND("Sundaram Mutual Fund"),
    TATA_MUTUAL_FUND("Tata Mutual Fund"),
    TAURUS_MUTUAL_FUND("Taurus Mutual Fund"),
    TRUST_MUTUAL_FUND("Trust Mutual Fund"),
    UTI_MUTUAL_FUND("UTI Mutual Fund"),
    UNIFI_MUTUAL_FUND("Unifi Mutual Fund"),
    UNION_MUTUAL_FUND("Union Mutual Fund"),
    WHITEOAK_CAPITAL_MUTUAL_FUND("WhiteOak Capital Mutual Fund"),
    ZERODHA_MUTUAL_FUND("Zerodha Mutual Fund"),
    QUANT_MUTUAL_FUND("quant Mutual Fund");

    // Field to hold the company name
    private final String companyName;

    // Constructor
    MutualFundCompany(String companyName) {
        this.companyName = companyName;
    }

    // Getter method
    public String getCompanyName() {
        return companyName;
    }
}
