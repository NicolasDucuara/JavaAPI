package com.example.colorapijava;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaludoRepository extends JpaRepository<Saludo, Long> {

    // Spring Data genera automáticamente la consulta SQL a partir del nombre del método:
    // SELECT * FROM saludos ORDER BY fecha_hora DESC;
    List<Saludo> findAllByOrderByFechaHoraDesc();
}
