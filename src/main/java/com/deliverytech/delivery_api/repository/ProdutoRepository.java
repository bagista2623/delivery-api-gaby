package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByAtivoTrue();

    List<Produto> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    List<Produto> findByCategoriaContainingIgnoreCaseAndAtivoTrue(String categoria);

    List<Produto> findByRestauranteIdAndAtivoTrue(Long restauranteId);
}
