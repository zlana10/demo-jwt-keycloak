package com.amrut.prabhu.security;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @ConfigProperty(name = "jwt.secret.key")
    private String jwtSecretKey;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(60 * 60 * 24); // 1 day

        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(authentication.getName())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey.getBytes());

        return builder.compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(new SecretKeySpec(jwtSecretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName()))
                .parseClaimsJws(token)
                .getBody();
    }
}
