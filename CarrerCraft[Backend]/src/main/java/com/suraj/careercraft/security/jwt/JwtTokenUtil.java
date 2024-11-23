package com.suraj.careercraft.security.jwt;

import com.suraj.careercraft.helper.CustomUser;
import com.suraj.careercraft.service.impl.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private static final Logger log = LogManager.getLogger(JwtTokenUtil.class);
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateTokenFromEmail(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // Token valid for 10 hours
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String getUsernameFromToken(String token)
    {
        String username = getClaimFromToken(token, Claims::getSubject);
        log.info("Extracted Username: {}", username);
        return username;
    }


    //return data from JWT token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //return all the claims/data from JWT token
    private Claims getAllClaimsFromToken(String token)
    {
        try
        {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims;
        }
        catch (Exception e)
        {
            log.error("Could not parse token: {}", token, e);
            throw e;
        }
    }

    //return expiry date of JWT token
    public Date getExpirationDateFromToken(String token)
    {
        Date expiration = getClaimFromToken(token, Claims::getExpiration);
        log.info("Extracted Expiration: {}", expiration);
        return expiration;
    }

    //check whether JWT token is expired or not
    public Boolean isTokenExpired(String token)
    {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //check whether the JWT token is valid or not
    public Boolean validateToken(String token, CustomUser userDetails)
    {
        final String username = getUsernameFromToken(token);
        return ((username.equals(userDetails.getUsername()) || username.equals(userDetails.getEmail()) && !isTokenExpired(token)));
    }
}
