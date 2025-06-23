package com.silver.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.silver.demo.model.Categoria;
import com.silver.demo.serviceImpl.CategoriaServiceImpl;

@RequestMapping("/api/categorias")
@RestController
public class CategoriaController {
	
	@Autowired
	private CategoriaServiceImpl cateService;
	
	
	@GetMapping
	public ResponseEntity<Map<String , Object >> lista(){
		return cateService.listaCategorias();	
		}


	@GetMapping("/{id}")
	public ResponseEntity<Map<String , Object >> listaCategoriaId(@PathVariable Long id){
		return cateService.listaCategoriasId(id);	
		}
	
	
	@PostMapping
	public ResponseEntity<Map<String , Object >> agregarCategoria(@RequestBody Categoria categoria){
		return cateService.agregarCategorias(categoria);	
		}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String , Object >> editarCategoria(@RequestBody Categoria categoria , @PathVariable Long id){
		return cateService.editarCategorias(categoria, id);	
		}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String , Object >> eliminarCategoria(@PathVariable Long id){
		return cateService.eliminarCategorias(id);	
		}
	
	
}
