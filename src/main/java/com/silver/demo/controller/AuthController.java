package com.silver.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.silver.demo.dto.Auth;
import com.silver.demo.dto.RefreshTokenRequest;
import com.silver.demo.service.AuthService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService service;
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody Auth a) {
		return service.login(a);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<Map<String, Object>> refresh(@RequestBody RefreshTokenRequest request) {
		return service.refresh(request);
	}
}
