package com.silver.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.silver.demo.model.DetallePedido;


public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long>{
	
	List<DetallePedido> findByPedidoId(Long pedidoId);
	
	@Modifying
	@Query("DELETE FROM DetallePedido d WHERE d.pedido.id = :pedidoId")
	void deleteByPedidoId(@Param("pedidoId") Long pedidoId);
	
}
