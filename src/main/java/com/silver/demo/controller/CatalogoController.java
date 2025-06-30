package com.silver.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.silver.demo.service.CategoriaService;
import com.silver.demo.service.VideojuegoService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/catalogo")
public class CatalogoController {
	
	@Autowired
    private VideojuegoService videojuegoService;
	
	@Autowired
    private CategoriaService categoriaService;


	
    @GetMapping("/listado")
    public ResponseEntity<Map<String, Object>> listarVideojuegos() {
        return videojuegoService.listarVideojuego();
    }
    

    @GetMapping("/categoria/{id}")
    public ResponseEntity<Map<String, Object>> listarPorCategoria(@PathVariable("id") Long id) {
        return videojuegoService.listarPorCategoriaId(id);
    }

    @GetMapping("/categorias")
    public ResponseEntity<Map<String , Object >> lista(){
		return categoriaService.listaCategorias();	
		}

    @GetMapping("/listado/{id}")
    public ResponseEntity<Map<String, Object>> obtenerVideojuegoPorId(@PathVariable("id")  Long id) {
        return videojuegoService.listarVideojuegoId(id);
    }
    
    
}
