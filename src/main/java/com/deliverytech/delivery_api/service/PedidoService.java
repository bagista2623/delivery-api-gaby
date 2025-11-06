package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.PedidoResponseDTO;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<PedidoResponseDTO> listarPedidosAtivos() {
        return pedidoRepository.findByAtivoTrue()
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<PedidoResponseDTO> buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .filter(Pedido::isAtivo)
                .map(PedidoResponseDTO::new);
    }

    public List<PedidoResponseDTO> buscarPorDescricao(String descricao) {
        return pedidoRepository.findByDescricaoContainingIgnoreCase(descricao)
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        // Validação básica
        var cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        var restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        // Validação e cálculo de total
        double total = calcularTotalPedido(dto.getItens());

        Pedido pedido = new Pedido();
        pedido.setDescricao("Pedido do cliente " + cliente.getNome());
        pedido.setTotal(total);
        pedido.setAtivo(true);
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());

        Pedido salvo = pedidoRepository.save(pedido);

        return new PedidoResponseDTO(salvo);
    }

    public void atualizarStatusPedido(Long id, String status) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        pedido.setStatus(status); // ou enum se tiver
        pedidoRepository.save(pedido);
    }

    public List<PedidoResponseDTO> buscarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteIdAndAtivoTrue(clienteId)
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Double calcularTotalPedido(List<ItemPedidoDTO> itens) {
        return itens.stream()
                .mapToDouble(i -> {
                    var produto = produtoRepository.findById(i.getProdutoId())
                            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
                    return produto.getPreco() * i.getQuantidade();
                })
                .sum();
    }

    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        pedido.setAtivo(false);
        pedidoRepository.save(pedido);
    }

    public Optional<PedidoResponseDTO> atualizar(Long id, PedidoRequestDTO dto) {
        return pedidoRepository.findById(id).map(p -> {
            p.setDescricao("Atualizado");
            p.setTotal(calcularTotalPedido(dto.getItens()));
            p.setEnderecoEntrega(dto.getEnderecoEntrega());
            Pedido atualizado = pedidoRepository.save(p);
            return new PedidoResponseDTO(atualizado);
        });
    }
}
