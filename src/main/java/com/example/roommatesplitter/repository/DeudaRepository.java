package com.example.roommatesplitter.repository;

import com.example.roommatesplitter.model.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeudaRepository extends JpaRepository<Deuda, Long> {

    List<Deuda> findByUsuarioId(Long usuarioId);

    List<Deuda> findByDeudor(String deudor);

    List<Deuda> findByAcreedor(String acreedor);

    List<Deuda> findByEstado(String estado);

    List<Deuda> findByDeudorAndAcreedor(String deudor, String acreedor);
}
