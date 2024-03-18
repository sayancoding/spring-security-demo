package com.arrowsmodule.springsecurityjwt.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtService {

    private static final String SECRET_KEY = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private static final long TOKEN_VALIDITY = 15 * 60; // 15 -> mins , multiply by 60 to make it seconds
    // generate token
    public String generateToken(String username){
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims,username);
    }

    private String createToken(HashMap<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * TOKEN_VALIDITY))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //get extract all claims
    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    // get username from claims
    public String getUsername(String token){
        return extractClaims(token,Claims::getSubject);
    }

    // get expiration date
    public Date getExpirationDate(String token) {
        return extractClaims(token,Claims::getExpiration);
    }

    // check validity of token
    public boolean isValidToken(String token, UserDetails userDetails){
        boolean isTokenExpired = getExpirationDate(token).before(new Date());
        boolean isSameUsername = getUsername(token).equals(userDetails.getUsername());

        return (!isTokenExpired && isSameUsername);
    }
}
