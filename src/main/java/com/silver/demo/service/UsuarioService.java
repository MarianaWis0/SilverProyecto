package com.silver.demo.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.silver.demo.dto.UsuarioDTO;

public interface UsuarioService {

	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> listarUsuariosPorEstado(String estado);

	public ResponseEntity<Map<String, Object>> crearUsuario(UsuarioDTO dto);

	public ResponseEntity<Map<String, Object>> actualizarUsuario(UsuarioDTO dto, Long id);

	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> desactivarUsuario(Long id);

	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> reactivarUsuario(Long id);
}
