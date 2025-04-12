package com.backend.services;

import com.backend.Model.MongoDbTable.MutualFundCollection.MutualFund;
import com.backend.Model.MongoDbTable.MutualFundCollection.MutualFundAmcName;
import com.backend.Model.MongoDbTable.MutualFundCollection.NavCalculationDateHistory;
import com.backend.Model.MongoDbTable.MutualFundCollection.NavInfo;
import com.backend.Model.MongoDbTable.MutualFundCollection.Scheme;
import com.backend.Model.MongoDbTable.MutualFundCollection.SchemeCategory;
import com.backend.Model.MutualFund.Enum.SchemeType;
import com.backend.repository.MongoRepository.MutualFundRepository;
import com.backend.repository.MongoRepository.MutualFundAmcRepository;
import com.backend.repository.MongoRepository.NavCalculationDateHistoryRepository;
import com.backend.repository.MongoRepository.SchemeCategoryRepository;
import com.backend.repository.MongoRepository.SchemeRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MarketDataImporterService {

    private final String AMFII_URL = "https://portal.amfiindia.com/DownloadNAVHistoryReport_Po" +
            ".aspx?frmdt=%s&todt=%s";

    private final String DATE_FORMAT_AMFII = "dd-MMM-yyyy";
    private final ZoneId TIMEZONE = ZoneId.of("Asia/Kolkata");
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT_AMFII);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Autowired
    private SchemeRepository schemeRepository;
    @Autowired
    private SchemeCategoryRepository schemeCategoryRepository;
    @Autowired
    private MutualFundAmcRepository mutualFundAmcRepository;
    @Autowired
    private MutualFundRepository mutualFundRepository;
    @Autowired
    private NavCalculationDateHistoryRepository navCalculationDateHistoryRepository;

    @Autowired
    OkHttpClient httpClient;

    /**
     * Check if NAV calculation exists for a specific date
     */
    public boolean isNavCalculatedForDate(String date) {
        return navCalculationDateHistoryRepository.findByCalculationDate(date)
                .map(NavCalculationDateHistory::isCalculated)
                .orElse(false);
    }

    /**
     * Initialize or update NAV calculation history for a specific date
     */
    public NavCalculationDateHistory initializeNavCalculation(String date) {
        String month = LocalDate.parse(date, DATE_FORMATTER).format(MONTH_FORMATTER);
        
        return navCalculationDateHistoryRepository.findByCalculationDateAndCalculationMonth(date, month)
                .orElseGet(() -> createNewNavCalculationHistory(date, month));
    }

    /**
     * Create a new NAV calculation history entry
     */
    private NavCalculationDateHistory createNewNavCalculationHistory(String date, String month) {
        NavCalculationDateHistory history = NavCalculationDateHistory.builder()
                .calculationDate(date)
                .calculationMonth(month)
                .isCalculated(false)
                .calculationStatus("PENDING")
                .totalFunds(0)
                .calculatedFunds(0)
                .failedFunds(0)
                .build();
        
        return navCalculationDateHistoryRepository.save(history);
    }

    /**
     * Update calculation progress
     */
    public void updateCalculationProgress(String date, int totalFunds, int calculatedFunds, int failedFunds) {
        String month = LocalDate.parse(date, DATE_FORMATTER).format(MONTH_FORMATTER);
        
        navCalculationDateHistoryRepository.findByCalculationDateAndCalculationMonth(date, month)
                .ifPresent(history -> {
                    history.setTotalFunds(totalFunds);
                    history.setCalculatedFunds(calculatedFunds);
                    history.setFailedFunds(failedFunds);
                    navCalculationDateHistoryRepository.save(history);
                });
    }

    /**
     * Mark calculation as complete for a specific date
     */
    public void markCalculationComplete(String date) {
        String month = LocalDate.parse(date, DATE_FORMATTER).format(MONTH_FORMATTER);
        
        navCalculationDateHistoryRepository.findByCalculationDateAndCalculationMonth(date, month)
                .ifPresent(history -> {
                    history.setCalculated(true);
                    history.setCalculationStatus("SUCCESS");
                    navCalculationDateHistoryRepository.save(history);
                });
    }

    /**
     * Mark calculation as failed for a specific date
     */
    public void markCalculationFailed(String date, List<String> errors) {
        String month = LocalDate.parse(date, DATE_FORMATTER).format(MONTH_FORMATTER);
        
        navCalculationDateHistoryRepository.findByCalculationDateAndCalculationMonth(date, month)
                .ifPresent(history -> {
                    history.setCalculated(false);
                    history.setCalculationStatus("FAILED");
                    navCalculationDateHistoryRepository.save(history);
                });
    }

    /**
     * Get calculation history for a specific month
     */
    public List<NavCalculationDateHistory> getCalculationHistoryForMonth(String month) {
        return navCalculationDateHistoryRepository.findByCalculationMonth(month);
    }

    /**
     * Get pending calculations for a date range
     */
    public List<NavCalculationDateHistory> getPendingCalculations(String startDate, String endDate) {
        return navCalculationDateHistoryRepository.findByCalculationDateBetweenAndIsCalculatedFalse(startDate, endDate);
    }

    public void importMarketData() throws IOException {
        // Get current date in the required format
        final long epochMillis = LocalDate.now(TIMEZONE).atStartOfDay(TIMEZONE).toInstant().toEpochMilli();
        final String toDate = dateFormatter.format(new Date(epochMillis));
        
        // Get yesterday's date
        final LocalDate dateMinus = LocalDate.now(TIMEZONE).minusDays(1);
        final long epochMillisfrom = dateMinus.atStartOfDay(TIMEZONE).toInstant().toEpochMilli();
        final String fromDate = dateFormatter.format(new Date(epochMillisfrom));
        
        // Convert to yyyy-MM-dd format for our history tracking
        String currentDate = LocalDate.now(TIMEZONE).format(DATE_FORMATTER);
        
        // Initialize NAV calculation history for today
        NavCalculationDateHistory history = initializeNavCalculation(currentDate);
        
        final String url = String.format(AMFII_URL, fromDate, toDate);
        final Request request = new Request.Builder()
                .headers(Headers.of("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;" +
                                "q=0.8,application/signed-exchange;v=b3;q=0.9"))
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMsg = "Failed to fetch data from AMFII. Response code: " + response.code();
                log.error(errorMsg);
                markCalculationFailed(currentDate, List.of(errorMsg));
                return;
            }
            
            if (response.body() == null) {
                String errorMsg = "Empty response body from AMFII";
                log.error(errorMsg);
                markCalculationFailed(currentDate, List.of(errorMsg));
                return;
            }
            
            // Store response body in a variable
            String responseBody = response.body().string();
            
            // Process NAV data
            List<MutualFund> mutualFundData = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.ByteArrayInputStream(responseBody.getBytes())))) {
                String line;
                Scheme currentScheme = null;
                SchemeCategory currentCategory = null;
                reader.readLine(); // Skip header line
                MutualFundAmcName currentAmc = null;
                long mutualfundsize = 0;
                int calculatedFunds = 0;
                int failedFunds = 0;
                List<String> errors = new ArrayList<>();

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    
                    // Header line: contains a schemes text and a category in parentheses.
                    if ((line.contains("Schemes") || line.contains("schemes"))
                            && line.contains("(") && line.contains(")")) {
                        // Text before '(' is the scheme name.
                        String schemeName = line.substring(0, line.indexOf("(")).trim();
                        // Text inside the parentheses is the scheme category.
                        String categoryName = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim();

                        // Fetch or create the Scheme.
                        currentScheme = schemeRepository.findByDescription(schemeName);
                        if (currentScheme == null) {
                            String currentSchemaType = schemeName.replaceAll("\\s", "_").toUpperCase();
                            currentScheme = Scheme.builder()
                                    .description(schemeName)
                                    .schemeType(SchemeType.fromName(currentSchemaType))
                                    .build();
                            currentScheme = schemeRepository.save(currentScheme);
                            log.info("Created new Scheme: {}", schemeName);
                        }

                        // Fetch or create the SchemeCategory using the scheme id.
                        currentCategory = schemeCategoryRepository.findByCategoryName(categoryName);
                        if (currentCategory == null) {
                            String categoryEnum = categoryName.replaceAll("[\\s-]+", "_").toUpperCase();
                            String categoryNameEnum = categoryName.replaceAll("[\\s-]+", " ").toUpperCase();
                            currentCategory = SchemeCategory.builder()
                                    .schemeId(currentScheme.getId())
                                    .categoryEnum(categoryEnum)
                                    .categoryName(categoryNameEnum)
                                    .description(categoryName)
                                    .categoryName(categoryName)
                                    .build();
                            currentCategory = schemeCategoryRepository.save(currentCategory);
                            log.info("Created new SchemeCategory: {}", categoryName);
                        }
                        // Reset AMC for the new header.
                        currentAmc = null;
                    }
                    // AMC line: no delimiter present.
                    else if (!line.contains(";")) {
                        String amcName = line;
                        // Fetch or create the AMC entity, linking to the current scheme category.
                        currentAmc = mutualFundAmcRepository.findByAmcName(amcName);

                        if (currentAmc == null) {
                            String amcNameEnum = amcName.replaceAll("[\\s-]+", "_").toUpperCase();
                            currentAmc = MutualFundAmcName.builder()
                                    .amcName(amcName)
                                    .amcNameEnum(amcNameEnum)
                                    .schemeCategoryId(currentCategory.getId())
                                    .build();
                            currentAmc = mutualFundAmcRepository.save(currentAmc);
                            log.info("Created new AMC: {}", amcName);
                        }
                    }
                    // Mutual fund record line: contains delimiter.
                    else if (line.contains(";")) {
                        // Expected columns (at least 8 columns):
                        // schemeCode;schemeName;ISIN Div Payout/ISIN Growth;ISIN Div Reinvestment;Net Asset Value;Repurchase Price;Sale Price;Date
                        String[] columns = line.split(";");
                        if (columns.length >= 8) {
                            mutualfundsize++;
                            try {
                                log.debug("Processing record: schemeCode={}, schemeName={}, ISIN Div Payout/ISIN Growth={}, ISIN Div Reinvestment={}, Net Asset Value={}, Repurchase Price={}, Sale Price={}, Date={}", 
                                        columns[0].trim(), columns[1].trim(), columns[2].trim(), columns[3].trim(), 
                                        columns[4].trim(), columns[5].trim(), columns[6].trim(), columns[7].trim());
                                
                                // Create NavInfo object
                                NavInfo navInfo = NavInfo.builder()
                                        .nav(new BigDecimal(columns[4].trim()))
                                        .date(columns[7].trim())
                                        .build();
                                
                                MutualFund mf = MutualFund.builder()
                                        .schemeCode(columns[0].trim())
                                        .schemeName(columns[1].trim())
                                        .isinDivGrowth(columns[2].trim().isEmpty() ? null : columns[2].trim())
                                        .isinDivReinvestment(columns[3].trim().isEmpty() ? null : columns[3].trim())
                                        .navInfo(navInfo)
                                        .amcId(currentAmc.getId())
                                        .build();
                                mutualFundRepository.save(mf);
                                calculatedFunds++;
                                log.debug("Inserted MutualFund record with scheme code: {}", columns[0].trim());
                            } catch (Exception e) {
                                failedFunds++;
                                String error = "Error processing record: " + columns[0].trim() + " - " + e.getMessage();
                                errors.add(error);
                                log.error(error, e);
                            }
                            
                            // Update progress after every 100 records
                            if (mutualfundsize % 100 == 0) {
                                updateCalculationProgress(currentDate, (int)mutualfundsize, calculatedFunds, failedFunds);
                            }
                        }
                    }
                }

                log.info("Total mutual fund size={}", mutualfundsize);
                
                // Final update of calculation progress
                updateCalculationProgress(currentDate, (int)mutualfundsize, calculatedFunds, failedFunds);
                
                // Mark calculation as complete or failed
                if (failedFunds == 0) {
                    markCalculationComplete(currentDate);
                } else {
                    markCalculationFailed(currentDate, errors);
                }
                
            } catch (IOException e) {
                String errorMsg = "Error reading Market.txt file: " + e.getMessage();
                log.error(errorMsg, e);
                markCalculationFailed(currentDate, List.of(errorMsg));
            }
        } catch (Exception e) {
            String errorMsg = "Error fetching data from AMFII: " + e.getMessage();
            log.error(errorMsg, e);
            markCalculationFailed(currentDate, List.of(errorMsg));
        }
    }
}