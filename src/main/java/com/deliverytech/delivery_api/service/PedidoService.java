package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.PedidoResponseDTO;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.security.SecurityUtils;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Autowired
    private SecurityUtils securityUtils;


    // ============================================================
    // LISTAR COM FILTROS + PAGINAÇÃO
    // Cache com chave única baseada nos filtros
    // ============================================================
    @Cacheable(value = "pedidosCache",
            key = "'filtros_' + #status + '_' + #dataInicio + '_' + #dataFim + '_' + #page + '_' + #size")
    public Page<PedidoResponseDTO> listarComFiltros(
            String status,
            LocalDate dataInicio,
            LocalDate dataFim,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        List<Pedido> lista = pedidoRepository.findByAtivoTrue();

        if (status != null && !status.isBlank()) {
            lista = lista.stream()
                    .filter(p -> p.getStatus().name().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        if (dataInicio != null) {
            lista = lista.stream()
                    .filter(p -> p.getDataCriacao().toLocalDate().isAfter(dataInicio.minusDays(1)))
                    .collect(Collectors.toList());
        }

        if (dataFim != null) {
            lista = lista.stream()
                    .filter(p -> p.getDataCriacao().toLocalDate().isBefore(dataFim.plusDays(1)))
                    .collect(Collectors.toList());
        }

        List<PedidoResponseDTO> dtoList = lista.stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());

        int start = Math.min(page * size, dtoList.size());
        int end = Math.min(start + size, dtoList.size());

        return new PageImpl<>(dtoList.subList(start, end), pageable, dtoList.size());
    }


    // ============================================================
    // CRIAR PEDIDO
    // Sempre invalida o cache
    // ============================================================
    @Transactional
    @CacheEvict(value = "pedidosCache", allEntries = true)
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {

        var cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        var restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        double total = calcularTotalPedido(dto.getItens());

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setTotal(total);
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setDescricao("Pedido do cliente " + cliente.getNome());
        pedido.setNumeroPedido(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setAtivo(true);

        Pedido salvo = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(salvo);
    }


    // ============================================================
    // BUSCAR POR ID
    // ============================================================
    @Cacheable(value = "pedidosCache", key = "#id")
    public Optional<PedidoResponseDTO> buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .filter(p -> Boolean.TRUE.equals(p.getAtivo()))
                .map(PedidoResponseDTO::new);
    }


    // ============================================================
    // BUSCAR POR CLIENTE
    // ============================================================
    @Cacheable(value = "pedidosCache", key = "'cliente_' + #clienteId")
    public List<PedidoResponseDTO> buscarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteIdAndAtivoTrue(clienteId)
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }


    // ============================================================
    // BUSCAR POR RESTAURANTE
    // ============================================================
    @Cacheable(value = "pedidosCache", key = "'restaurante_' + #restauranteId")
    public List<PedidoResponseDTO> buscarPedidosPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteIdAndAtivoTrue(restauranteId)
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }


    // ============================================================
    // CALCULAR TOTAL DO PEDIDO (não cacheia porque é dinâmico)
    // ============================================================
    public Double calcularTotalPedido(List<ItemPedidoDTO> itens) {
        return itens.stream()
                .mapToDouble(i -> {
                    var produto = produtoRepository.findById(i.getProdutoId())
                            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
                    return produto.getPreco() * i.getQuantidade();
                })
                .sum();
    }


    // ============================================================
    // ATUALIZAR STATUS — invalida cache
    // ============================================================
    @CacheEvict(value = "pedidosCache", allEntries = true)
    public void atualizarStatusPedido(Long id, String status) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        try {
            pedido.setStatus(StatusPedido.valueOf(status.toUpperCase()));
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Status inválido. Use: PENDENTE, CONFIRMADO, PREPARANDO, SAIU_PARA_ENTREGA, ENTREGUE, CANCELADO"
            );
        }

        pedidoRepository.save(pedido);
    }


    // ============================================================
    // CANCELAR PEDIDO — invalida cache
    // ============================================================
    @CacheEvict(value = "pedidosCache", allEntries = true)
    public void cancelarPedido(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.setAtivo(false);

        pedidoRepository.save(pedido);
    }


    // ============================================================
    // AUTORIZAÇÃO (sem cache por segurança)
    // ============================================================
    public boolean canAccess(Long pedidoId) {

        Usuario user = securityUtils.getCurrentUser();
        if (user == null) return false;

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElse(null);
        if (pedido == null) return false;

        if (user.getRole() == Role.ADMIN) return true;

        switch (user.getRole()) {

            case CLIENTE:
                return pedido.getCliente().getId().equals(user.getId());

            case RESTAURANTE:
                return pedido.getRestaurante() != null &&
                        pedido.getRestaurante().getId().equals(user.getRestauranteId());

            default:
                return false;
        }
    }
}
