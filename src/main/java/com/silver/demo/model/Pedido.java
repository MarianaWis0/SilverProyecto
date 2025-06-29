package com.silver.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.persistence.CascadeType;

import lombok.Data;

@Data
@Entity
@Table(name = "pedido")
public class Pedido {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne
	@JoinColumn(name="usuario_id", nullable = false)
	@NotNull(message = "El usuario es obligatorio")
	private Usuario usuarios;
    
	@PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDateTime fecha;
    
    private double total;
    
    
    //@NotEmpty(message = "El pedido debe tener al menos un detalle")
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL,  orphanRemoval = true)
	private List<DetallePedido>detallePedido;
    
    @PrePersist
    public void asignarFecha() {
        this.fecha = LocalDateTime.now();
    }
}
