//package com.backend.Config.securityConfig;
//
//
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.io.Encoders;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.*;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JwtUtil {
//
//    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//// Use a strong secret key
//
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 60 * 1000)) // 10 hours
//                .signWith(key)
//                .compact();
//    }
//
//    // Extract Username from JWT Token
//    public String extractUsername(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    // Validate JWT Token
//    public boolean validateToken(String token) {
//        try {
//            Jws<Claims> claims = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token);
//            return !isTokenExpired(claims.getBody().getExpiration());
//        } catch (JwtException | IllegalArgumentException e) {
//            // Handle token parsing or validation errors
//            System.out.println("Invalid JWT token: " + e.getMessage());
//            return false;
//        }
//    }
//
//    // Check if the token has expired
//    private boolean isTokenExpired(Date expiration) {
//        return expiration.before(new Date());
//    }
//}
