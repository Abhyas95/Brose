package com.backend.Model.MongoDbTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data // Generates getters, setters, toString, equals, and hashCode
@AllArgsConstructor // Generates a constructor with all fields
@Document(collection = "UserAccounts")// Collection name in MongoDB
public class UserAccounts {

    @Id
    private String id;
    private String userName;
    private String mobileNumber;
    private String userStatus;

    // Constructors
    public UserAccounts() {}

    public UserAccounts(String userName, String mobileNumber, String userStatus) {
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.userStatus = userStatus;
    }

}

