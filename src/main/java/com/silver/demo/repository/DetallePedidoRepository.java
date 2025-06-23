package com.silver.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.silver.demo.model.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long>{
	
	List<DetallePedido> findByPedidoId(Long pedidoId);
	
}
