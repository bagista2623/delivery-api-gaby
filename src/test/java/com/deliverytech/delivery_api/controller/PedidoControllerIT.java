package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoRequestDTO;
import org.springframework.context.annotation.Import;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@Import(TestSecurityConfig.class)
class PedidoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;
    private Restaurante restaurante;
    private Produto produto;


    // -------------------------------------------------------------
    // SETUP DO BANCO ANTES DE CADA TESTE
    // -------------------------------------------------------------
    @BeforeEach
    void setup() {

        pedidoRepository.deleteAll();
        produtoRepository.deleteAll();
        restauranteRepository.deleteAll();
        clienteRepository.deleteAll();

        cliente = clienteRepository.save(
                Cliente.builder()
                        .nome("Gaby")
                        .email("gaby@test.com")
                        .telefone("51999999999")
                        .endereco("Rua Teste 123")
                        .ativo(true)
                        .build()
        );

        restaurante = restauranteRepository.save(
                Restaurante.builder()
                        .nome("Restaurante Teste")
                        .categoria("Pizza")
                        .endereco("Rua X")
                        .telefone("51000000000")
                        .cnpj("00000000000100")
                        .faixaPreco("$$")
                        .ativo(true)
                        .build()
        );

        produto = produtoRepository.save(
                Produto.builder()
                        .nome("Pizza Calabresa")
                        .categoria("Pizza")
                        .preco(50.0)
                        .ativo(true)
                        .restaurante(restaurante)
                        .build()
        );
    }



    // -------------------------------------------------------------
    // TESTE 1 — Criar pedido
    // -------------------------------------------------------------
    @Test
    @DisplayName("Deve criar um pedido com sucesso")
    void deveCriarPedidoComSucesso() throws Exception {

        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(cliente.getId());
        dto.setRestauranteId(restaurante.getId());
        dto.setEnderecoEntrega("Rua Teste 200");
        dto.setItens(List.of(
                new ItemPedidoDTO(produto.getId(), 2)
        ));

        mockMvc.perform(
                        post("/api/pedidos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.total").value(100.0));
    }



    // -------------------------------------------------------------
    // TESTE 2 — Buscar pedido por ID
    // -------------------------------------------------------------
    @Test
    @DisplayName("Deve buscar pedido por ID")
    void deveBuscarPedidoPorId() throws Exception {

        Pedido pedido = pedidoRepository.save(
                Pedido.builder()
                        .cliente(cliente)
                        .restaurante(restaurante)
                        .descricao("Pedido teste")
                        .enderecoEntrega("Rua 123")
                        .total(50.0)
                        .status(StatusPedido.PENDENTE)
                        .ativo(true)
                        .build()
        );

        mockMvc.perform(get("/api/pedidos/" + pedido.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedido.getId()))
                .andExpect(jsonPath("$.total").value(50.0));
    }



    // -------------------------------------------------------------
    // TESTE 3 — Deve retornar erro ao buscar ID inexistente
    // -------------------------------------------------------------
    @Test
    @DisplayName("Deve retornar 404 ao buscar pedido inexistente")
    void deveRetornar404() throws Exception {

        mockMvc.perform(get("/api/pedidos/999"))
                .andExpect(status().isNotFound());
    }



    // -------------------------------------------------------------
    // TESTE 4 — Atualizar status
    // -------------------------------------------------------------
    @Test
    @DisplayName("Deve atualizar status de um pedido")
    void deveAtualizarStatus() throws Exception {

        Pedido pedido = pedidoRepository.save(
                Pedido.builder()
                        .cliente(cliente)
                        .restaurante(restaurante)
                        .descricao("Pedido")
                        .total(40.0)
                        .status(StatusPedido.PENDENTE)
                        .ativo(true)
                        .build()
        );

        mockMvc.perform(
                        patch("/api/pedidos/" + pedido.getId() + "/status")
                                .param("status", "CONFIRMADO")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Status atualizado com sucesso"));
    }



    // -------------------------------------------------------------
    // TESTE 5 — Calcular total
    // -------------------------------------------------------------
    @Test
    @DisplayName("Deve calcular valor total corretamente")
    void deveCalcularTotal() throws Exception {

        List<ItemPedidoDTO> itens = List.of(
                new ItemPedidoDTO(produto.getId(), 3)
        );

        mockMvc.perform(
                        post("/api/pedidos/calcular")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(itens))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("150.0"));
    }



    // -------------------------------------------------------------
    // TESTE 6 — Cancelar pedido
    // -------------------------------------------------------------
    @Test
    @DisplayName("Deve cancelar pedido com sucesso")
    void deveCancelarPedido() throws Exception {

        Pedido pedido = pedidoRepository.save(
                Pedido.builder()
                        .cliente(cliente)
                        .restaurante(restaurante)
                        .descricao("Pedido")
                        .total(30.0)
                        .status(StatusPedido.PENDENTE)
                        .ativo(true)
                        .build()
        );

        mockMvc.perform(delete("/api/pedidos/" + pedido.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Pedido cancelado com sucesso"));
    }
}
