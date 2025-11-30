package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Listar apenas ativos
    List<Restaurante> findByAtivoTrue();

    // Paginação + filtros
    Page<Restaurante> findByCategoriaContainingIgnoreCaseAndAtivoTrue(String categoria, Pageable pageable);

    // Buscar por nome
    List<Restaurante> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    // Buscar por categoria (sem paginação)
    List<Restaurante> findByCategoriaContainingIgnoreCaseAndAtivoTrue(String categoria);

    // Buscar ativo por ID
    Optional<Restaurante> findByIdAndAtivoTrue(Long id);

    // Buscar por CEP (campo opcional — caso tenha)
    List<Restaurante> findByEnderecoContainingIgnoreCaseAndAtivoTrue(String cep);

    // Filtrar por ativo + categoria + nome juntos
    Page<Restaurante> findByCategoriaContainingIgnoreCaseAndNomeContainingIgnoreCaseAndAtivoTrue(
            String categoria,
            String nome,
            Pageable pageable
    );
}
