package com.silver.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.silver.demo.dto.UsuarioDTO;
import com.silver.demo.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/listado")
	public ResponseEntity<Map<String, Object>> listarUsuariosPorEstado(@RequestParam String estado, @RequestParam(required = false) String rol) {
		return service.listarUsuariosPorEstado(estado, rol);
	}
	
	@PostMapping("/registro")
	public ResponseEntity<Map<String, Object>> registrarUsuario(@RequestBody @Valid UsuarioDTO dto) {
		return service.crearUsuario(dto);
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@PutMapping("/actualizacion/{id}")
	public ResponseEntity<Map<String, Object>> actualizarUsuario(@RequestBody @Valid UsuarioDTO dto, @PathVariable Long id) {
		return service.actualizarUsuario(dto, id);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/desactivacion/{id}")
	public ResponseEntity<Map<String, Object>> desactivarUsuario(@PathVariable Long id) {
		return service.desactivarUsuario(id);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/reactivacion/{id}")
	public ResponseEntity<Map<String, Object>> reactivarUsuario(@PathVariable Long id) {
		return service.reactivarUsuario(id);
	}
}
