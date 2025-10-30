package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listarPedidosAtivos() {
        return pedidoRepository.findByAtivoTrue();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .filter(Pedido::isAtivo);
    }

    public List<Pedido> buscarPorDescricao(String descricao) {
        return pedidoRepository.findByDescricaoContainingIgnoreCase(descricao);
    }

    public Pedido criar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> atualizar(Long id, Pedido novoPedido) {
        return pedidoRepository.findById(id).map(p -> {
            p.setDescricao(novoPedido.getDescricao());
            p.setTotal(novoPedido.getTotal());
            p.setAtivo(novoPedido.isAtivo());
            return pedidoRepository.save(p);
        });
    }

    public boolean excluir(Long id) {
        return pedidoRepository.findById(id).map(p -> {
            p.setAtivo(false);
            pedidoRepository.save(p);
            return true;
        }).orElse(false);
    }
}
