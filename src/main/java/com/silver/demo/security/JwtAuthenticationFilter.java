package com.silver.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.silver.demo.serviceImpl.UserDetailsServiceImp;
import com.silver.demo.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsServiceImp udsiService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			
			if (jwtUtils.validateToken(token)) {
				String username = jwtUtils.getUsernameFromToken(token);
				
				UserDetails userDetails = udsiService.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
																userDetails, null, userDetails.getAuthorities());
				
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
