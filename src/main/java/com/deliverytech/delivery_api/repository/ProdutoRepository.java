package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Buscar produtos ativos
    List<Produto> findByAtivoTrue();

    // Buscar produtos por nome (contendo)
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
