package io.github.justinscottjenecke.spring_security_jpa_tutorial.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public String extractUsername(String jwtToken) {
        return "";
    }

    public String getSignInKey() {
        return "";
    }

    public Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey()) // sets signing key from settings
                .build()
                .parseClaimsJws(jwtToken)  // decodes hash value reverse algorithm and parse into usable data
                .getBody(); // return body of jwt
    }
}
