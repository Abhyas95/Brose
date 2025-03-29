package com.backend.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data // Generates getters, setters, toString, equals, and hashCode
@AllArgsConstructor // Generates a constructor with all fields
public class AuthenticationRequest {
    private String mobileNumber;
    private String OTP;
}
