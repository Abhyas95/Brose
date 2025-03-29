//package com.backend.controller.Authentication;
//
//
//import com.backend.Config.securityConfig.JwtUtil;
//import com.backend.Model.Request.AuthenticationRequest;
//import com.backend.Model.response.JwtResponse;
//import com.backend.services.OtpService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.HttpStatus;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthenticationController {
//
//    @Autowired
//    private OtpService otpService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    // Endpoint to generate OTP
//    @PostMapping("/generate-otp")
//    public ResponseEntity<String> generateOtp(@RequestBody AuthenticationRequest request) {
//        otpService.generateOtp(request);
//        return ResponseEntity.ok("OTP sent successfully.");
//    }
//
//    // Endpoint to verify OTP and generate JWT
//    @PostMapping("/verify-otp")
//    public ResponseEntity<?> verifyOtp(@RequestBody AuthenticationRequest request) {
//        String mobileNumber = request.getMobileNumber();
//        String otp = request.getOTP();
//
//        if (otpService.validateOtp(mobileNumber, otp)) {
//            String token = jwtUtil.generateToken(mobileNumber);
//            return ResponseEntity.ok(new JwtResponse(token));
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
//        }
//    }
//}
//
