package com.silver.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class PedidoRequestDTO {

	 private Long usuarioId;
	 
	 private List<DetallePedidoRequestDTO> detalles;
}
