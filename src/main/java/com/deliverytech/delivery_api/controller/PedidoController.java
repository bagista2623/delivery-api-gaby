package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;

    public PedidoController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findByAtivoTrue();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .filter(Pedido::isAtivo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<Pedido> buscarPorDescricao(@RequestParam String descricao) {
        return pedidoRepository.findByDescricaoContainingIgnoreCase(descricao);
    }

    @PostMapping
    public Pedido criar(@RequestBody Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody Pedido novoPedido) {
        return pedidoRepository.findById(id)
                .map(p -> {
                    p.setDescricao(novoPedido.getDescricao());
                    p.setTotal(novoPedido.getTotal());
                    p.setAtivo(novoPedido.isAtivo());
                    return ResponseEntity.ok(pedidoRepository.save(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(p -> {
                    p.setAtivo(false);
                    pedidoRepository.save(p);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
