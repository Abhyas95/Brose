package com.backend.Model.MongoDbTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data // Generates getters, setters, toString, equals, and hashCode
@AllArgsConstructor // Generates a constructor with all fields
@Document(collection = "StocksDetails")// Collection name in MongoDB
public class StocksDetails {

    @Id
    private String id;  // MongoDB automatically generates an id if not provided

    private String stockName;
    private double currentPrice;
    private String recommendedDate;
    private double firstTargetPrice;
    private double firstTargetGrowth;
    private double secondTargetPrice;
    private double secondTargetGrowth;
    private double thirdTargetPrice;
    private double thirdTargetGrowth;
    private String expectedTargetDate;
    private String companySize;
    private String sector;
    private int sharesOutstanding;
    private String marketCap;
    private double PEratio;
    private double dividendYield;

}
