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
public class MutualFundAmcName {
    @Id
    private String id;  // MongoDB document ID

    private int schemeCategoryId;  // Scheme Code (e.g., 148921)

    private String amcName;  // Name of the AMC (e.g., Aditya Birla Sun Life Mutual Fund)
    private String amcNameEnum;  // Name of the AMC (e.g., Aditya Birla Sun Life Mutual Fund)

    private String schemeCategory;  // AMC Code (e.g., 101)

    @CreatedDate// Type of Scheme (enum)
    private Date createdOn;
    @LastModifiedDate// Date when the scheme was created
    private Date lastUpdatedOn;        // Date when the scheme was last updated
    private String description;
}
