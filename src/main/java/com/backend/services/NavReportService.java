package com.backend.services;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return ResponseEntity.ok(response.body().string());
            }
        } catch (Exception e) {
           return ResponseEntity.ok("failed to fetch data");
        }
        return  null;
    }
}

