package com.knight.hairreservation.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key SECRET_KEY =
            Keys.hmacShaKeyFor(
                    "knight-secret-key-knight-secret-key"
                            .getBytes()
            );

    public String createToken(
            Long userId
    ) {

        Date now = new Date();

        Date expire =
                new Date(
                        now.getTime()
                                + 1000 * 60 * 60
                );

        return Jwts.builder()

                .setSubject(
                        String.valueOf(userId)
                )

                .setIssuedAt(now)

                .setExpiration(expire)

                .signWith(
                        SECRET_KEY,
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    public boolean validateToken(
            String token
    ) {

        try {

            Jwts.parser()

                    .setSigningKey(SECRET_KEY)

                    .parseClaimsJws(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
    public Long getUserId(
            String token
    ) {

        String subject =

                Jwts.parser()

                        .setSigningKey(
                                SECRET_KEY
                        )

                        .parseClaimsJws(token)

                        .getBody()

                        .getSubject();

        return Long.parseLong(subject);
    }
}