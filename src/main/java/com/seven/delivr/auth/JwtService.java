package com.seven.delivr.auth;

import com.seven.delivr.base.AppService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@ApplicationScope
public class JwtService implements AppService {
    private final Environment environment;
    public JwtService(Environment environment){
        this.environment = environment;
    }
    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey(){
        byte[] bytes = environment.getProperty("jwt.signing.key").getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(String subject, Map<String, Object> claims){
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.from(ZonedDateTime.now().plusMonths(3))))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String subject){
        return generateToken(subject, new HashMap<String, Object>());
    }

    public boolean isTokenValid(Claims claims){
        return !isTokenExpired(claims);
    }

    private boolean isTokenExpired(Claims claims){
        return claims.getExpiration().before(Date.from(Instant.now()));
    }
}
