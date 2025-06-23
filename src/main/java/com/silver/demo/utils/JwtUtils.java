package com.silver.demo.utils;

import java.util.Base64;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.silver.demo.security.JwtConfig;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	
	private final JwtConfig jwtConfig;

	public JwtUtils(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}
	
	public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtConfig.getExpiration()))
            .signWith(
                Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getSecret())),
                SignatureAlgorithm.HS256
            )
            .compact();
    }
	
	public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Base64.getDecoder().decode(jwtConfig.getSecret()))
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
	
	public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(jwtConfig.getSecret()))
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
