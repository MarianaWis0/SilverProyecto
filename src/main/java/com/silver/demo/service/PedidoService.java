package com.silver.demo.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.silver.demo.dto.PedidoRequestDTO;
import com.silver.demo.model.Pedido;

public interface PedidoService {
	
	public ResponseEntity<Map<String, Object>> listarPedidos();

    public ResponseEntity<Map<String, Object>> listarPedidoPorId(Long id);

    public ResponseEntity<Map<String, Object>> agregarPedido(PedidoRequestDTO pedidoRequest );

    public ResponseEntity<Map<String, Object>> editarPedido(PedidoRequestDTO pedidoRequest, Long id);

    public ResponseEntity<Map<String, Object>> eliminarPedido(Long pedidoId);
	
}
