package com.example.roommatesplitter.repository;


import com.example.roommatesplitter.model.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {

    List<Gasto> findByUsuarioId(Long usuarioId);

    List<Gasto> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    List<Gasto> findByQuienPago(String quienPago);

    List<Gasto> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
