package com.server.mentorgrowth.services;

import com.server.mentorgrowth.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret.key}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return (!isTokenExpired(token) && userDetails.getUsername().equals(extractUsername(token)));
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user) {
        List<String> roles = user.getAuthorities().stream()
                .map(authority -> Objects.requireNonNull(authority.getAuthority())
                        .replace("ROLE_", ""))
                        .toList();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("id", user.getId())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                .signWith(getSecretKey())
                .compact();
    }

    public SecretKey getSecretKey() {
        byte[] decodedKey = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}
