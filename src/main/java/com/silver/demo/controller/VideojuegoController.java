package com.silver.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.silver.demo.model.Videojuego;
import com.silver.demo.service.VideojuegoService;

@RestController
@RequestMapping("/api/videojuegos")
public class VideojuegoController {
	
	@Autowired
    private VideojuegoService videojuegoService;

	
    @GetMapping("/lista")
    public ResponseEntity<Map<String, Object>> listarVideojuegos() {
        return videojuegoService.listarVideojuego();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerVideojuegoPorId(@PathVariable Long id) {
        return videojuegoService.listarVideojuegoId(id);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> agregarVideojuego(@RequestBody Videojuego videojuego) {
        return videojuegoService.agregarVideojeugo(videojuego);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editarVideojuego(@RequestBody Videojuego videojuego, @PathVariable Long id) {
        return videojuegoService.editarVideojuego(videojuego, id);
    }

    @PutMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Object>> eliminarLogico(@PathVariable Long id) {
        return videojuegoService.eliminarLogicoVideojuego(id);
    }

    @GetMapping("/estado") //// estado?estados=A - I - F
    public ResponseEntity<Map<String, Object>> filtrarPorEstado(@RequestParam List<String> estados) {
        return videojuegoService.EstadoVideojuego(estados);
    }
}
