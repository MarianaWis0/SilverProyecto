package com.silver.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RespuestaDto {
    private Long id;
    private String contenido;
    private Long autorId;
    private LocalDateTime fecha;
    private String autorUsername;
    private Long temaId;


}

