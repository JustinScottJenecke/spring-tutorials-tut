package io.github.justinscottjenecke.spring_security_jpa_tutorial.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Determines whether a passed jwt token is valid or not
     * @param jwtToken string variable containing jwt token to be evaluated
     * @param userDetails used to confirm that the token passed in belongs to the expected user
     * @return boolean
     */
    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String username = extractUsername(jwtToken);
        return(username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
    }

    /**
     * Determines whether a passed in jwtToken is past its expiration date
     * @param jwtToken string variable containing jwt token to be evaluated
     * @return boolean
     */
    private boolean isTokenExpired(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration).before(new Date());
    }

    public Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey()) // sets signing key from settings
                .build()
                .parseClaimsJws(jwtToken)  // decodes hash value reverse algorithm and parse into usable data
                .getBody(); // return body of jwt
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
