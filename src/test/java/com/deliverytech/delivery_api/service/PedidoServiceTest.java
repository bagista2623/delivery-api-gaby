package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoRequestDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.security.SecurityUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private PedidoService pedidoService;

    private Cliente cliente;
    private Restaurante restaurante;
    private Produto produto;

    @BeforeEach
    void setup() {
        cliente = new Cliente();
        cliente.setId(1L);

        restaurante = new Restaurante();
        restaurante.setId(2L);

        produto = new Produto();
        produto.setId(3L);
        produto.setPreco(10.0);
        produto.setAtivo(true);
    }

    //------------------------------------------------------
    // CALCULAR TOTAL
    //------------------------------------------------------
    @Test
    void deveCalcularTotalPedido() {
        ItemPedidoDTO item1 = new ItemPedidoDTO();
        item1.setProdutoId(3L);
        item1.setQuantidade(2);

        when(produtoRepository.findById(3L)).thenReturn(Optional.of(produto));

        double total = pedidoService.calcularTotalPedido(Arrays.asList(item1));

        assertEquals(20.0, total);
    }

    @Test
    void calcularTotal_deveFalharSeProdutoNaoExiste() {
        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoId(99L);
        item.setQuantidade(1);

        when(produtoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> pedidoService.calcularTotalPedido(Arrays.asList(item)));
    }

    //------------------------------------------------------
    // CRIAR PEDIDO
    //------------------------------------------------------
    @Test
    void deveCriarPedidoComSucesso() {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(1L);
        dto.setRestauranteId(2L);
        dto.setEnderecoEntrega("Rua X");

        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoId(3L);
        item.setQuantidade(2);

        dto.setItens(Arrays.asList(item));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(restauranteRepository.findById(2L)).thenReturn(Optional.of(restaurante));
        when(produtoRepository.findById(3L)).thenReturn(Optional.of(produto));

        Pedido pedidoMock = new Pedido();
        pedidoMock.setId(10L);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoMock);

        var response = pedidoService.criarPedido(dto);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        verify(pedidoRepository, times(1)).save(any());
    }

    //------------------------------------------------------
    // ATUALIZAR STATUS
    //------------------------------------------------------
    @Test
    void deveAtualizarStatusDoPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.atualizarStatusPedido(1L, "ENTREGUE");

        assertEquals("ENTREGUE", pedido.getStatus().name());
    }

    @Test
    void atualizarStatus_deveLancarErroParaStatusInvalido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        assertThrows(IllegalArgumentException.class,
                () -> pedidoService.atualizarStatusPedido(1L, "NADA"));
    }

    //------------------------------------------------------
    // CANCELAR
    //------------------------------------------------------
    @Test
    void deveCancelarPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setAtivo(true);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.cancelarPedido(1L);

        assertFalse(pedido.getAtivo());
        verify(pedidoRepository, times(1)).save(pedido);
    }
}
