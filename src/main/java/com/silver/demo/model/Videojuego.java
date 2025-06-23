package com.silver.demo.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "videojuegos")
@EntityListeners(AuditingEntityListener.class)
public class Videojuego {
	 
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String titulo;
	    private String descripcion;
	    private double precio;
	    private String estado;
	    private Long stock;

	    private String img; 

	    @ManyToOne
	    @JoinColumn(name = "categoria_id")
	    @JsonIgnoreProperties("videojuegos")
	    private Categoria categoria;
}
