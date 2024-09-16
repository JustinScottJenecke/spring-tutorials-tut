package io.github.justinscottjenecke.spring_security_jpa_tutorial.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtService {

    // NOTE: move to app properties
    private static final String SECRET_KEY = "446b7e462436572867695f666a337a7031377b3e7d733929212d7e663b";

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    /**
     * Method to return a single claim specified by the Type given
     * @return Claim
     */
    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
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
