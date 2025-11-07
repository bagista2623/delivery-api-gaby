package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByAtivoTrue();

    List<Pedido> findByDescricaoContainingIgnoreCase(String descricao);

    // 🔹 Novo método que o PedidoService usa
    List<Pedido> findByClienteIdAndAtivoTrue(Long clienteId);
}
