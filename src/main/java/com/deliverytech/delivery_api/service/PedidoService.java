package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.PedidoResponseDTO;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import java.util.UUID;
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

    // 🔹 Listar pedidos ativos
    public List<PedidoResponseDTO> listarPedidosAtivos() {
        return pedidoRepository.findByAtivoTrue()
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 🔹 Buscar pedido por ID
    public Optional<PedidoResponseDTO> buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .filter(p -> Boolean.TRUE.equals(p.getAtivo()))
                .map(PedidoResponseDTO::new);
    }

    // 🔹 Buscar pedidos por descrição
    public List<PedidoResponseDTO> buscarPorDescricao(String descricao) {
        return pedidoRepository.findByDescricaoContainingIgnoreCase(descricao)
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 🔹 Criar novo pedido
    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        var cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        var restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        double total = calcularTotalPedido(dto.getItens());

        Pedido pedido = new Pedido();
        pedido.setDescricao("Pedido do cliente " + cliente.getNome());
        pedido.setTotal(total);
        pedido.setAtivo(true);
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setNumeroPedido(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        pedido.setStatus(StatusPedido.PENDENTE); // 🔹 Status inicial padrão

        Pedido salvo = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(salvo);
    }

    // 🔹 Atualizar status do pedido
    public void atualizarStatusPedido(Long id, String status) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        try {
            // Converte String para Enum (ex: "ENTREGUE" → StatusPedido.ENTREGUE)
            pedido.setStatus(StatusPedido.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Status inválido! Use: PENDENTE, CONFIRMADO, PREPARANDO, SAIU_PARA_ENTREGA, ENTREGUE ou CANCELADO"
            );
        }

        pedidoRepository.save(pedido);
    }

    // 🔹 Buscar pedidos por cliente ativo
    public List<PedidoResponseDTO> buscarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteIdAndAtivoTrue(clienteId)
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 🔹 Calcular total do pedido
    public Double calcularTotalPedido(List<ItemPedidoDTO> itens) {
        return itens.stream()
                .mapToDouble(i -> {
                    var produto = produtoRepository.findById(i.getProdutoId())
                            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
                    return produto.getPreco() * i.getQuantidade();
                })
                .sum();
    }

    // 🔹 Cancelar pedido (inativar)
    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        pedido.setAtivo(false);
        pedido.setStatus(StatusPedido.CANCELADO); // 🔹 Atualiza o status também
        pedidoRepository.save(pedido);
    }

    // 🔹 Atualizar informações de um pedido
    public Optional<PedidoResponseDTO> atualizar(Long id, PedidoRequestDTO dto) {
        return pedidoRepository.findById(id).map(p -> {
            p.setDescricao("Pedido atualizado de " + p.getCliente().getNome());
            p.setTotal(calcularTotalPedido(dto.getItens()));
            p.setEnderecoEntrega(dto.getEnderecoEntrega());
            Pedido atualizado = pedidoRepository.save(p);
            return new PedidoResponseDTO(atualizado);
        });
    }
}
