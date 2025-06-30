package com.silver.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class TemaDto {
	   private Long id;
	    private String titulo;
	    private String contenido;
	    private LocalDateTime fechaCreacion;
	    private Long autorId;
	    private String autorUsername;
	    private List<RespuestaDto> respuestas;
}