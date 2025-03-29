package com.backend.services;


import com.backend.Model.Request.AuthenticationRequest;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    // Generate and send OTP
    public void generateOtp(AuthenticationRequest request) {
        String otp = String.format("%06d", secureRandom.nextInt(999999));
        otpStore.put(request.getMobileNumber(), otp);
        sendOtpToUser(request.getMobileNumber(), otp);
    }

    // Validate OTP
    public boolean validateOtp(String username, String otp) {
        String storedOtp = otpStore.get(username);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStore.remove(username); // Remove OTP after successful verification
            return true;
        }
        return false;
    }

    // Simulate sending OTP to user
    private void sendOtpToUser(String mobileNumber, String otp) {
        // Implement actual sending logic (SMS, email, etc.)
        System.out.println("Sending OTP " + otp + " to user " + mobileNumber);
    }
}

