package com.silver.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pedido_id", nullable = false)
	@JsonIgnoreProperties({ "detalles", "hibernateLazyInitializer" })
	private Pedido pedido;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "videojuego_id", nullable = false)
	@JsonIgnoreProperties({ "categoria", "hibernateLazyInitializer" })
	private Videojuego videojuego;

	@Min(value = 1, message = "La cantidad debe ser al menos 1")
	private Integer cantidad; // nº unidades
	
	@Column(name = "precio_unitario")
	private Double precioUnitario; // copia del precio en el momento de la compra



}
