package com.silver.demo.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.silver.demo.model.Categoria;

public interface CategoriaService {
	
	
	
	public ResponseEntity<Map<String , Object>> listaCategorias();
	
	public ResponseEntity<Map<String , Object>> listaCategoriasId(Long id);
	
	public ResponseEntity<Map<String , Object>> agregarCategorias(Categoria categoria);
	
	public ResponseEntity<Map<String , Object>> editarCategorias(Categoria categoria , Long id);
	
	public ResponseEntity<Map<String , Object>> eliminarCategorias(Long id);

	
}
