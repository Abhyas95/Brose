package com.backend.Model.MongoDbTable.MutualFundCollection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "mutual_fund")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"createdOn", "lastUpdatedOn"}, allowGetters = true)
public class MutualFund {
    @Id
    private String id;  // MongoDB document ID

    private String schemeCode;  // Scheme Code (e.g., 148921)

    private String amcId; // 

    private String schemeName;  // Name of the Scheme (e.g., Aditya Birla Sun Life Multi-Cap Fund-Direct Growth)

    private String isinDivGrowth;  // ISIN for Growth/Dividend (e.g., INF209KB1Y49)

    private String isinDivReinvestment;  // ISIN for IDCW Reinvestment (if available, else null)

    private NavInfo navInfo;  // Composite class containing NAV and date information

    @CreatedDate
    private Date createdOn;  // Date when the document was created

    @LastModifiedDate
    private Date lastUpdatedOn;  // Date when the document was last updated
}
