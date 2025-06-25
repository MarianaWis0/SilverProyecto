package com.silver.demo.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.silver.demo.dto.Auth;
import com.silver.demo.dto.RefreshTokenRequest;
import com.silver.demo.service.AuthService;
import com.silver.demo.utils.JwtUtils;

@Service
public class AuthServiceImp implements AuthService{

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsServiceImp udService;
	
	@Override
	public ResponseEntity<Map<String, Object>> login(Auth a) {
	    Map<String, Object> respuesta = new HashMap<>();
		
		Authentication auth = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(a.getEmail(), a.getPassword()));

		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		String accessToken = jwtUtils.generateAccessToken(userDetails);
		String refreshToken = jwtUtils.generateRefreshToken(userDetails);

		respuesta.put("accessToken", accessToken);
		respuesta.put("refreshToken", refreshToken);
		respuesta.put("tokenType", "Bearer");
		respuesta.put("fecha", new Date());

		return ResponseEntity.status(HttpStatus.OK).body(respuesta);
	}

	@Override
	public ResponseEntity<Map<String, Object>> refresh(RefreshTokenRequest request) {
		Map<String, Object> respuesta = new HashMap<>();
		String refreshToken = request.getRefreshToken();

		if (!jwtUtils.validateToken(refreshToken) || jwtUtils.isTokenExpired(refreshToken)) {
			respuesta.put("mensaje", "Token inválido o expirado");
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		String username = jwtUtils.getUsernameFromToken(refreshToken);
		UserDetails userDetails = udService.loadUserByUsername(username);

		String newAccessToken = jwtUtils.generateAccessToken(userDetails);

		respuesta.put("accessToken", newAccessToken);
		respuesta.put("refreshToken", refreshToken);
		respuesta.put("tokenType", "Bearer");
		respuesta.put("fecha", new Date());

		return ResponseEntity.status(HttpStatus.OK).body(respuesta);
	}

}
