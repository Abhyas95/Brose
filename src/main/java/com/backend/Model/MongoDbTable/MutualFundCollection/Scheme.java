package com.backend.Model.MongoDbTable.MutualFundCollection;

import com.backend.Model.MutualFund.Enum.SchemeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "scheme")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scheme {
        private SchemeType schemeType;
        @CreatedDate// Type of Scheme (enum)
        private Date createdOn;
        @LastModifiedDate// Date when the scheme was created
        private Date lastUpdatedOn;        // Date when the scheme was last updated
        private String description;             // Scheme Description

}
