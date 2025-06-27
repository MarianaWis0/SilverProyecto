package com.silver.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Auth {

	
	@NotBlank(message = "El correo es obligatorio")
	@Pattern(regexp = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Formato de email inválido")
	private String email;
	
	private String password;
}
