package com.silver.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.silver.demo.dto.PedidoRequestDTO;
import com.silver.demo.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
	
	@Autowired
	private PedidoService service;
	
	@GetMapping("/lista")
    public ResponseEntity<Map<String, Object>> listar() {
        return service.listarPedidos();
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> listaPorID(@PathVariable Long id) {
        return service.listarPedidoPorId(id);
    }
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/agregar")
    public ResponseEntity<Map<String, Object>> agregar(@Valid @RequestBody PedidoRequestDTO pedidoRequest) {
        return service.agregarPedido(pedidoRequest);
    }
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@PutMapping("/editar/{id}")
	public ResponseEntity<Map<String, Object>> editarPedido(
	        @Valid @RequestBody PedidoRequestDTO pedidoRequest,
	        @PathVariable Long id) {
	    return service.editarPedido(pedidoRequest, id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/eliminar/{pedidoId}")
	public ResponseEntity<Map<String, Object>> eliminarPedido(@PathVariable Long pedidoId) {
	    return service.eliminarPedido(pedidoId);
	}
	
}
