package com.silver.demo.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario",
		indexes = {
			@Index(name = "idx_usuario_email", columnList = "email"),
			@Index(name = "idx_usuario_estado", columnList = "estado"),
			@Index(name = "idx_usuario_rol_id", columnList = "rol_id")
		})
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String estado;

	@ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
	private Rol rol;
}
