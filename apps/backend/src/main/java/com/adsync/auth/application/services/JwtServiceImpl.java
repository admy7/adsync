package com.adsync.auth.application.services;

import com.adsync.auth.domain.models.AuthUser;
import com.adsync.auth.domain.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtServiceImpl implements JwtService {
    private SecretKey secretKey;

    private int expiration;

    public JwtServiceImpl(String secretKey, int expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expiration = expiration;
    }

    @Override
    public String tokenize(User user) {
        var claims = Jwts.claims()
                .subject(user.id())
                .add("email", user.email())
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public AuthUser parse(String token) {
        var jwtParser = Jwts.parser().verifyWith(secretKey).build();

        var claims = jwtParser.parseSignedClaims(token).getPayload();

        var id = claims.getSubject();
        var email = claims.get("email", String.class);

        return new AuthUser(id, email);
    }

    public double expiresIn() {
        return expiration;
    }
}
