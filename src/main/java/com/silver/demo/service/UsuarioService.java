package com.silver.demo.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.silver.demo.dto.UsuarioDTO;
import com.silver.demo.dto.UsuarioRequestDTO;

public interface UsuarioService {

	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> listarUsuariosPorEstado(String estado, String rol, int page, int size,
			String textoBusqueda);

	public ResponseEntity<Map<String, Object>> crearUsuario(UsuarioDTO dto);

	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Map<String, Object>> actualizarUsuario(UsuarioDTO dto, Long id);
	
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> actualizarUsuarioAdmin(UsuarioRequestDTO dto, Long id);

	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> desactivarUsuario(Long id);

	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> reactivarUsuario(Long id);
}
