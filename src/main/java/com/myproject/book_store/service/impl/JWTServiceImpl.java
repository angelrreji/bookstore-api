package com.myproject.book_store.service.impl;

import com.myproject.book_store.service.JWTService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTServiceImpl implements JWTService {

    private static final Logger log = LoggerFactory.getLogger(JWTServiceImpl.class);

    private final SecretKey key;
    private final long expirationMs;

    public JWTServiceImpl(@Value("${jwt.secret:}") String secret,
                          @Value("${jwt.expiration-ms:1800000}") long expirationMs) {
        this.expirationMs = expirationMs;
        if (secret == null || secret.isBlank()) {
            log.warn("jwt.secret (JWT_SECRET) not set - generating a random key; tokens will not survive a restart");
            this.key = Jwts.SIG.HS256.key().build();
        } else {
            this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        }
    }

    @Override
    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(key)
                .compact();
    }

    @Override
    public String validateAndExtractUsername(String token) {
        try {
            return Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token).getPayload().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
