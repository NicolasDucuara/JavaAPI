package com.example.colorapijava;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "saludos")
public class Saludo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 7)
    private String color;

    @Column(nullable = false)
    private String mensaje;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    // Hibernate requiere un constructor vacío (sin argumentos) por defecto
    public Saludo() {
    }

    // Constructor de conveniencia para cuando creamos nuevos saludos
    public Saludo(String nombre, String color, String mensaje) {
        this.nombre = nombre;
        this.color = color;
        this.mensaje = mensaje;
        this.fechaHora = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
