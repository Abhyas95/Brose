package com.backend.services;

import com.backend.Model.MongoDbTable.MutualFundCollection.MutualFund;
import com.backend.repository.MongoRepository.MutualFundRepository;
import io.micrometer.common.util.StringUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class NavReportService {
    private final String AMFII_URL = "https://portal.amfiindia.com/DownloadNAVHistoryReport_Po" +
            ".aspx?frmdt=%s&todt=%s";

    private final String DATE_FORMAT_AMFII = "dd-MMM-yyyy";
    private final ZoneId TIMEZONE = ZoneId.of("Asia/Kolkata");
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT_AMFII);

    @Autowired
    ApiService apiService;

    @Autowired
    OkHttpClient httpClient;

    @Autowired
    MutualFundRepository mutualFundRepository;
    public static final String DELIMITER = ";";


    public ResponseEntity<String> reportData() {

        final long epochMillis =
                LocalDate.now(TIMEZONE).atStartOfDay(TIMEZONE).toInstant().toEpochMilli();
        final String fileName = "NAV_data_AMFII_" + epochMillis + ".txt";
        final String bucketName = System.getenv("S3_BUCKET_NAME");

        //Date will be in UTC, so by default we will reach a day before in UTC.
        final String toDate = dateFormatter.format(new Date(epochMillis));


        final LocalDate dateMinus = LocalDate.now(TIMEZONE).minusDays(1);

        final long epochMillisfrom = dateMinus.atStartOfDay(TIMEZONE).toInstant().toEpochMilli();

        final String fromDate = dateFormatter.format(new Date(epochMillisfrom));


        final String url = String.format(AMFII_URL, fromDate, toDate);
        final Request request = new Request.Builder()
                .headers(Headers.of("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;" +
                                "q=0.8,application/signed-exchange;v=b3;q=0.9"))
                .url(url)
                .get()
                .build();

//        try (Response response = httpClient.newCall(request).execute()) {
//            if (response.isSuccessful() && response.body() != null) {
//                ResponseEntity.ok(response.body().string());
//
//
//                // process nav data
//
//                List<MutualFund> mutualFundData = new ArrayList<>();
//                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()))) {
//                    // Read CSV headers
//                    String line = reader.readLine();
//                    String[] headers = line.split(DELIMITER);
//
//                    while ((line = reader.readLine()) != null) {
//                        String[] row = line.split(DELIMITER);
//
//                        if (row.length == headers.length) {
//                            String schemeCode = row[0].trim(); // e.g., 149366
//                            String schemeName = row[1].trim(); // e.g., HDFC Multi Cap Fund - Growth Option
//                            String isinDivGrowth = row[2].trim(); // e.g., INF179KC1BV9
//                            String isinDivReinvestment = row[3].trim(); // Could be blank
//                            BigDecimal nav = new BigDecimal(row[4].trim()); // e.g., 17.163
//
//                            BigDecimal purchasePrice = null;
//                            BigDecimal salePrice = null;
//
//                            // Check for optional values
//                            if (row.length > 5 && !StringUtils.isBlank(row[5].trim())) {
//                                purchasePrice = new BigDecimal(row[5].trim());
//                            }
//
//                            if (row.length > 6 && !StringUtils.isBlank(row[6].trim())) {
//                                salePrice = new BigDecimal(row[6].trim());
//                            }
//
//                            String navDate = null;
//                            if (row.length > 7 && !StringUtils.isBlank(row[7].trim())) {
//                                navDate = row[7].trim(); // e.g., 28-Mar-2025
//                            }
//
//                            mutualFundData.add(MutualFund.builder()
//                                    .schemeCode(Integer.parseInt(schemeCode))
//                                    .schemeName(schemeName)
//                                    .isinDivGrowth(isinDivGrowth)
//                                    .isinDivReinvestment(StringUtils.isBlank(isinDivReinvestment) ? null : isinDivReinvestment)
//                                    .nav(nav)
//                                    .navDate(navDate)
//                                    .build());
//                        }
//
//                    }
//
//                    log.info("Fetched {} mutual fund data", mutualFundData.size());
//                }
//            }
//
//        } catch (Exception e) {
//            log.info("Fetched {} mutual fund data", e);
//            return ResponseEntity.ok("failed to fetch data");
//        }
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {

                // Store response body in a variable
                String responseBody = response.body().string();

                ResponseEntity.ok(responseBody);

                // Process NAV data
                List<MutualFund> mutualFundData = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.ByteArrayInputStream(responseBody.getBytes())))) {
                    // Read CSV headers
                    String line = reader.readLine();
                    String[] headers = line.split(DELIMITER);

                    while ((line = reader.readLine()) != null) {
                        String[] row = line.split(DELIMITER);

                        if (row.length == headers.length) {
                            String schemeCode = row[0].trim();
                            String schemeName = row[1].trim();
                            String isinDivGrowth = row[2].trim();
                            String isinDivReinvestment = row[3].trim();
                            BigDecimal nav = null;
                            String navDate = null;

                            if (!StringUtils.isBlank(row[4].trim())) {
                                nav = new BigDecimal(row[4].trim());
                            }

                            if (row.length > 7 && !StringUtils.isBlank(row[7].trim())) {
                                navDate = row[7].trim();
                            }

                            mutualFundData.add(MutualFund.builder()
                                    .schemeCode(Integer.parseInt(schemeCode))
                                    .schemeName(schemeName)
                                    .isinDivGrowth(StringUtils.isBlank(isinDivGrowth) ? null : isinDivGrowth)
                                    .isinDivReinvestment(StringUtils.isBlank(isinDivReinvestment) ? null : isinDivReinvestment)
                                    .nav(nav)
                                    .navDate(navDate)
                                    .build());
                        }
                    }



                    log.info("Processed {} mutual fund records from AMFII", mutualFundData.size());

                    mutualFundRepository.saveAll(mutualFundData);

                } catch (IOException e) {
                    log.error("Error reading NAV data from AMFII response", e);
                    return ResponseEntity.internalServerError().body("Error processing NAV data");
                }

                return ResponseEntity.ok("NAV Data Processed Successfully");
            } else {
                log.error("Failed to fetch NAV data from AMFII. Response Code: {}", response.code());
                return ResponseEntity.status(response.code()).body("Failed to fetch NAV data");
            }
        } catch (IOException e) {
            log.error("Error fetching NAV data from AMFII", e);
            return ResponseEntity.internalServerError().body("Error fetching NAV data");
        }
    }
}


