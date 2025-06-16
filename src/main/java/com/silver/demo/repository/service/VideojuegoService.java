package com.silver.demo.repository.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.silver.demo.model.Videojuego;


public interface VideojuegoService {
	
	public ResponseEntity<Map<String, Object>> listarVideojuego();
	
	public ResponseEntity<Map<String, Object>> listarVideojuegoId(Long id);
	
	public ResponseEntity<Map<String, Object>> agregarVideojeugo(Videojuego videojuego);
	
	public ResponseEntity<Map<String, Object>> editarVideojuego(Videojuego videojuego, Long id);
	
	public ResponseEntity<Map<String, Object>> eliminarLogicoVideojuego(Long id);	

	public ResponseEntity<Map<String, Object>> EstadoVideojuego(List<String> estado);
}
