package com.example.colorapijava;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class SaludoService {

    private final SaludoRepository saludoRepository;
    private final Random random = new Random();

    // Inyección de dependencias por constructor (mejor práctica recomendada por Spring)
    public SaludoService(SaludoRepository saludoRepository) {
        this.saludoRepository = saludoRepository;
    }

    public String generarColorAleatorio() {
        return String.format("#%06x", random.nextInt(0xFFFFFF + 1));
    }

    @Transactional
    public Saludo registrarSaludo(String nombre) {
        String color = generarColorAleatorio();
        String mensaje = "¡Hola " + nombre.trim() + "!";

        Saludo saludo = new Saludo(nombre.trim(), color, mensaje);
        return saludoRepository.save(saludo);
    }

    @Transactional(readOnly = true)
    public List<Saludo> obtenerHistorial() {
        return saludoRepository.findAllByOrderByFechaHoraDesc();
    }
}
