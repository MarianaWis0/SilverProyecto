package com.silver.demo.dto;

import lombok.Data;

@Data
public class UsuarioResponseDTO {

	private Long id;
    private String nombre;
    private String email;
    private String estado;
    private String rol;
}
