package com.silver.demo.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.silver.demo.model.Categoria;

public interface CategoriaService {
	
	public ResponseEntity<Map<String , Object>> listaCategorias();
	
	// @PreAuthorize("hasRole('ADMIN')") - FALTA DEFINIR QUIEN USARIA ESTE METODO
	public ResponseEntity<Map<String , Object>> listaCategoriasId(Long id);
	
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String , Object>> agregarCategorias(Categoria categoria);
	
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String , Object>> editarCategorias(Categoria categoria , Long id);
	
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String , Object>> eliminarCategorias(Long id);

	
}
