package com.silver.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.silver.demo.model.Videojuego;


public interface VideojuegoService {
	
	public ResponseEntity<Map<String, Object>> listarVideojuego();
	
	// @PreAuthorize("hasRole('ADMIN')") - FALTA DEFINIR QUIEN USARIA ESTE METODO
	public ResponseEntity<Map<String, Object>> listarVideojuegoId(Long id);
	
	ResponseEntity<Map<String, Object>> listarPorCategoriaId(Long categoriaId);

	
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> agregarVideojeugo(Videojuego videojuego);
	
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> editarVideojuego(Videojuego videojuego, Long id);
	
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> eliminarLogicoVideojuego(Long id);	

	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> EstadoVideojuego(List<String> estado);
}
