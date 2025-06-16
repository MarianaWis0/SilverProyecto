package com.silver.demo.model;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "categoria")
@EntityListeners(AuditingEntityListener.class)
public class Categoria {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String nombre;

	    @OneToMany(mappedBy = "categoria")
	    private List<Videojuego> videojuegos;
}
