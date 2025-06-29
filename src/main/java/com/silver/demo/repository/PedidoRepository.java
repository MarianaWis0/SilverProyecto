package com.silver.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.silver.demo.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	
	@Query("SELECT p FROM Pedido p JOIN p.usuarios u WHERE p.id = :pedidoId AND u.id = :usuarioId")
	Optional<Pedido> findByIdAndUsuarioId(@Param("pedidoId") Long pedidoId, @Param("usuarioId") Long usuarioId);
	
}
