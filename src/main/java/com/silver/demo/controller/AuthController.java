package com.silver.demo.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.silver.demo.dto.Auth;
import com.silver.demo.utils.JwtUtils;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Auth a) {
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(a.getEmail(), a.getPassword()));
		String token = jwtUtils.generateToken((UserDetails) auth.getPrincipal());
		return ResponseEntity.ok(Collections.singletonMap("token", token));
	}
}
