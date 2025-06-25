package com.silver.demo.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.silver.demo.dto.Auth;
import com.silver.demo.dto.RefreshTokenRequest;

public interface AuthService {

	public ResponseEntity<Map<String, Object>> login(Auth a);
	
	public ResponseEntity<Map<String, Object>> refresh(RefreshTokenRequest request);
}
