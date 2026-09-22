package com.raju.DevTrack.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long jwtExpiration;
    public JwtService(@Value("${jwt.secret}") String secret
    , @Value("${jwt.expiration}") long jwtExpiration){
        this.secretKey= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.jwtExpiration = jwtExpiration;
    }

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateToken(String username, long expiration) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
