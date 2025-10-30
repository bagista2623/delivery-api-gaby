package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    List<Restaurante> findByAtivoTrue();

    List<Restaurante> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    List<Restaurante> findByCategoriaContainingIgnoreCaseAndAtivoTrue(String categoria);

    Optional<Restaurante> findByIdAndAtivoTrue(Long id);
}
