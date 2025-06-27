package com.silver.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.silver.demo.model.Pedido;
import com.silver.demo.service.PedidoService;

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
	
	@PostMapping
    public ResponseEntity<Map<String, Object>> agregar(@Validated @RequestBody Pedido pedido) {
        return service.agregarPedido(pedido);
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editar(@RequestBody Pedido pedido, @PathVariable Long id) {
        return service.editarPedido(pedido, id);
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        return service.eliminarPedido(id);
    }
	
}
