package com.backend.Model.MongoDbTable.MutualFundCollection;

import com.backend.Model.MutualFund.Enum.OpenEndedSchemeType;
import com.backend.Model.MutualFund.Enum.SchemeType;
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
@Document(collection = "scheme_category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"createdOn", "lastUpdatedOn"}, allowGetters = true)
public class SchemeCategory {

    @Id
    private String id;// MongoDB document ID

    private String schemeId;  // Name of the AMC (e.g., Aditya Birla Sun Life Mutual Fund)
    private SchemeType schemeType;       // Type of Open-Ended Scheme

    private String categoryName;// Name of the Category (e.g., Multi Cap Fund) Money Market
    private String categoryEnum;// Name of the Category (e.g., Multi Cap Fund) MONEY_MARKET


    @CreatedDate// Type of Scheme (enum)
    private Date createdOn;
    @LastModifiedDate// Date when the scheme was created
    private Date lastUpdatedOn;        // Date when the scheme was last updated
    private String description;             // Scheme Description



}
