
package com.backend.services;

import com.backend.Model.MongoDbTable.MutualFundCollection.MutualFund;
import com.backend.Model.MongoDbTable.MutualFundCollection.MutualFundAmcName;
import com.backend.Model.MongoDbTable.MutualFundCollection.Scheme;
import com.backend.Model.MongoDbTable.MutualFundCollection.SchemeCategory;
import com.backend.Model.MutualFund.Enum.SchemeType;
import com.backend.repository.MongoRepository.MutualFundRepository;
import com.backend.repository.MongoRepository.MutualFundAmcRepository;
import com.backend.repository.MongoRepository.SchemeCategoryRepository;
import com.backend.repository.MongoRepository.SchemeRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
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

@Slf4j
@Service
public class MarketDataImporterService {

    private final String AMFII_URL = "https://portal.amfiindia.com/DownloadNAVHistoryReport_Po" +
            ".aspx?frmdt=%s&todt=%s";

    private final String DATE_FORMAT_AMFII = "dd-MMM-yyyy";
    private final ZoneId TIMEZONE = ZoneId.of("Asia/Kolkata");
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT_AMFII);

    @Autowired
    private SchemeRepository schemeRepository;
    @Autowired
    private SchemeCategoryRepository schemeCategoryRepository;
    @Autowired
    private MutualFundAmcRepository mutualFundAmcRepository;
    @Autowired
    private MutualFundRepository mutualFundRepository;

    @Autowired
    OkHttpClient httpClient;

    public void importMarketData() throws IOException {

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

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {

                // Store response body in a variable
                String responseBody = response.body().string();

                ResponseEntity.ok(responseBody);

                // Process NAV data
                List<MutualFund> mutualFundData = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.ByteArrayInputStream(responseBody.getBytes())))) {
                    String line;
                    Scheme currentScheme = null;
                    SchemeCategory currentCategory = null;
                    reader.readLine();
                    MutualFundAmcName currentAmc = null;
                    long mutualfundsize=0;

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
                                log.info("Processing record: schemeCode={}, schemeName={}, ISIN Div Payout/ISIN Growth={}, ISIN Div Reinvestment={}, Net Asset Value={}, Repurchase Price={}, Sale Price={}, Date={}", columns[0].trim(), columns[1].trim(), columns[2].trim(), columns[3].trim(), columns[4].trim(), columns[5].trim(), columns[6].trim(), columns[7].trim());
                                MutualFund mf = MutualFund.builder()
                                        .schemeCode(columns[0].trim())
                                        .schemeName(columns[1].trim())
                                        .isinDivGrowth(columns[2].trim().isEmpty() ? null : columns[2].trim())
                                        .isinDivReinvestment(columns[3].trim().isEmpty() ? null : columns[3].trim())
                                        .nav(new BigDecimal(columns[4].trim()))
                                        .navDate(columns[7].trim())
                                        .amcId(currentAmc.getId())
                                        .build();
                                mutualFundRepository.save(mf);
                                log.info("Inserted MutualFund record with scheme code: {}", columns[0].trim());
                            }
                        }
                    }

                    log.info("total mutual fund size={}",mutualfundsize);
                } catch (IOException e) {
                    log.error("Error reading Market.txt file", e);
                }
            }
        } catch (Exception e) {
            log.error("Error fetching data from AMFII", e);
        }
    }
}