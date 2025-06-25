package com.silver.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.access-token-expiration}")
	private Long accessTokenExpiration;

	@Value("${jwt.refresh-token-expiration}")
	private Long refreshTokenExpiration;

	public String getSecret() {
		return secret;
	}

	public Long getAccessTokenExpiration() {
		return accessTokenExpiration;
	}

	public Long getRefreshTokenExpiration() {
		return refreshTokenExpiration;
	}

}
