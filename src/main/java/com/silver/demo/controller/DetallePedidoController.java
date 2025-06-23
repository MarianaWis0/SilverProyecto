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

import com.silver.demo.model.DetallePedido;
import com.silver.demo.service.DetallePedidoService;

@RestController
@RequestMapping("/api/detalle-pedido")
public class DetallePedidoController {
	
	@Autowired
    private DetallePedidoService detalleService;
	
	// Listar todos los detalles
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarDetalles() {
        return detalleService.listarDetalles();
    }
    
    // Listar detalle por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> listarDetallePorId(@PathVariable Long id) {
        return detalleService.listarDetallePorId(id);
    }
    
    // Listar detalles por pedido
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Map<String, Object>> listarPorPedido(@PathVariable Long pedidoId) {
        return detalleService.listarPorPedido(pedidoId);
    }
    
    
    // Agregar un detalle
    @PostMapping
    public ResponseEntity<Map<String, Object>> agregarDetalle(@RequestBody DetallePedido detalle) {
        return detalleService.agregarDetalle(detalle);
    }
    
    // Editar un detalle
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editarDetalle(@RequestBody DetallePedido detalle, @PathVariable Long id) {
        return detalleService.editarDetalle(detalle, id);
    }
    
    // Eliminar un detalle
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarDetalle(@PathVariable Long id) {
        return detalleService.eliminarDetalle(id);
    }

}
