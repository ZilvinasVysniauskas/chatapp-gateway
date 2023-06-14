package com.chatapp.gateway.security;

import com.chatapp.gateway.config.AppConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenValidator {

    private final AppConfig appConfig;
    public String getUserIdFromToken(String token) {

        Jws<Claims> claimsJws = Jwts.parserBuilder()  //this is new since 0.11.0
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token);

        return claimsJws.getBody().getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException  ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(appConfig.getTokenSecret());
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
    }

}
