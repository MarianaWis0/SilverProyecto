package com.silver.demo.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.silver.demo.model.DetallePedido;

public interface DetallePedidoService {
	
	ResponseEntity<Map<String, Object>> listarDetalles();                
    ResponseEntity<Map<String, Object>> listarDetallePorId(Long id);     
    ResponseEntity<Map<String, Object>> listarPorPedido(Long pedidoId);  
    ResponseEntity<Map<String, Object>> agregarDetalle(DetallePedido d); 
    ResponseEntity<Map<String, Object>> editarDetalle(DetallePedido d, Long id); 
    ResponseEntity<Map<String, Object>> eliminarDetalle(Long id);        

}
