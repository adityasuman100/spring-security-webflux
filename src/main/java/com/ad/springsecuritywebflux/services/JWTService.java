package com.ad.springsecuritywebflux.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.xml.crypto.Data;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

@Service
public class JWTService {

    private SecretKey key;
    final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    // final private JwtParser parser;

    public JWTService() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
        // this.key = Jwts.SIG.HS256.key().build();
        // this.key = "lsdkfjlskdfj";
        // this.parser = Jwts.parser().decryptWith(this.key).build();

    }

    public String generate(String userName) {
        return Jwts.builder()
                .subject(userName)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(15, ChronoUnit.DAYS)))
                .signWith(this.key)
                .compact();
    }

    public String getUserName(String token) {
        return Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validate(UserDetails user, String token) {
        // Claims claims = parser.parseEncryptedClaims(token).getPayload();
        boolean unexpired = Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload().getExpiration().after(Date.from(Instant.now()));
        return unexpired && user.getUsername().equals(getUserName(token));
    }

}
