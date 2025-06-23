package com.silver.demo.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.silver.demo.model.Pedido;

public interface PedidoService {
	
	public ResponseEntity<Map<String, Object>> listarPedidos();

    public ResponseEntity<Map<String, Object>> listarPedidoPorId(Long id);

    public ResponseEntity<Map<String, Object>> agregarPedido(Pedido pedido);

    public ResponseEntity<Map<String, Object>> editarPedido(Pedido pedido, Long id);

    public ResponseEntity<Map<String, Object>> eliminarPedido(Long id);
	
}
