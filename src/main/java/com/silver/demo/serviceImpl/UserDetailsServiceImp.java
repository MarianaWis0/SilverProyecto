package com.silver.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.silver.demo.model.Usuario;
import com.silver.demo.repository.UsuarioRepository;
import com.silver.demo.security.UserDetailsImp;

@Service
public class UserDetailsServiceImp implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario u = usuarioRepo.findByEmailIgnoreCase(email)
					.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
		
		return new UserDetailsImp(u);
	}
	
}
