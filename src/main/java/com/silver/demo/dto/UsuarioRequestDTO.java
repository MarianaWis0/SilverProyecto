package com.silver.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {
	
	@NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no debe superar los 50 caracteres")
	private String nombre;

	@NotBlank(message = "El correo es obligatorio")
	@Pattern(regexp = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Formato de email inválido")
	private String email;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
        message = "La contraseña debe contener al menos una mayúscula, un número y un carácter especial"
    )
	private String password;
    
    @NotBlank(message = "El rol es obligatorio")
    private String rol;
}
