package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ProdutoRequestDTO;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProdutoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Restaurante restaurante;

    @BeforeEach
    void setup() {
        produtoRepository.deleteAll();
        restauranteRepository.deleteAll();

        restaurante = restauranteRepository.save(
                Restaurante.builder()
                        .nome("Restaurante Teste")
                        .categoria("Lanches")
                        .endereco("Rua X")
                        .telefone("51999999999")
                        .cnpj("1234567890001")
                        .horarioFuncionamento("08:00 - 18:00")
                        .faixaPreco("$$")
                        .descricao("Bom demais")
                        .ativo(true)
                        .build()
        );
    }

    // --------------------------------------------
    @Test
    @DisplayName("Deve criar produto com sucesso")
    void deveCriarProduto() throws Exception {

        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("X-Burger");
        dto.setDescricao("Lanche de teste");
        dto.setPreco(25.0);
        dto.setCategoria("Lanches");
        dto.setRestauranteId(restaurante.getId());

        mockMvc.perform(
                        post("/api/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("X-Burger"))
                .andExpect(jsonPath("$.restauranteId").value(restaurante.getId()));
    }

    // --------------------------------------------
    @Test
    @DisplayName("Não deve criar produto se restaurante não existe")
    void naoCriarRestauranteInexistente() throws Exception {

        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("X-Burger");
        dto.setDescricao("Lanche de teste");
        dto.setPreco(25.0);
        dto.setCategoria("Lanches");
        dto.setRestauranteId(999999L);

        mockMvc.perform(
                        post("/api/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    // --------------------------------------------
    @Test
    @DisplayName("Deve buscar produto por id")
    void deveBuscarPorId() throws Exception {

        Produto produto = produtoRepository.save(
                Produto.builder()
                        .nome("Pizza")
                        .descricao("Calabresa")
                        .preco(49.9)
                        .categoria("Pizza")
                        .restaurante(restaurante)
                        .ativo(true)
                        .build()
        );

        mockMvc.perform(get("/api/produtos/" + produto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Pizza"));
    }

    // --------------------------------------------
    @Test
    @DisplayName("Deve retornar 404 ao buscar produto inexistente")
    void deveRetornar404() throws Exception {
        mockMvc.perform(get("/api/produtos/999999"))
                .andExpect(status().isNotFound());
    }

    // --------------------------------------------
    @Test
    @DisplayName("Deve listar todos produtos ativos")
    void deveListarProdutos() throws Exception {

        produtoRepository.save(
                Produto.builder()
                        .nome("A")
                        .descricao("Desc")
                        .preco(10.0)
                        .categoria("Cat")
                        .restaurante(restaurante)
                        .ativo(true)
                        .build()
        );

        produtoRepository.save(
                Produto.builder()
                        .nome("B")
                        .descricao("Desc")
                        .preco(20.0)
                        .categoria("Cat")
                        .restaurante(restaurante)
                        .ativo(true)
                        .build()
        );

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // --------------------------------------------
    @Test
    @DisplayName("Deve listar produtos por restaurante")
    void listarPorRestaurante() throws Exception {

        produtoRepository.save(
                Produto.builder()
                        .nome("A")
                        .descricao("Desc")
                        .preco(10.0)
                        .categoria("Cat")
                        .restaurante(restaurante)
                        .ativo(true)
                        .build()
        );

        mockMvc.perform(get("/api/produtos/restaurante/" + restaurante.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // --------------------------------------------
    @Test
    @DisplayName("Deve atualizar produto")
    void deveAtualizarProduto() throws Exception {

        Produto produto = produtoRepository.save(
                Produto.builder()
                        .nome("Velho")
                        .descricao("Desc")
                        .preco(10.0)
                        .categoria("Cat")
                        .restaurante(restaurante)
                        .ativo(true)
                        .build()
        );

        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Novo Nome");
        dto.setDescricao("Nova desc");
        dto.setPreco(30.0);
        dto.setCategoria("Massas");
        dto.setRestauranteId(restaurante.getId());

        mockMvc.perform(
                        put("/api/produtos/" + produto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo Nome"));
    }

    // --------------------------------------------
    @Test
    @DisplayName("Deve alternar disponibilidade")
    void deveAlterarDisponibilidade() throws Exception {

        Produto produto = produtoRepository.save(
                Produto.builder()
                        .nome("Teste")
                        .descricao("Desc")
                        .preco(10.0)
                        .categoria("Cat")
                        .restaurante(restaurante)
                        .ativo(true)
                        .build()
        );

        mockMvc.perform(
                        patch("/api/produtos/" + produto.getId() + "/disponibilidade")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));
    }

    // --------------------------------------------
    @Test
    @DisplayName("Deve realizar soft delete")
    void deveSoftDelete() throws Exception {

        Produto produto = produtoRepository.save(
                Produto.builder()
                        .nome("Teste")
                        .descricao("Desc")
                        .preco(10.0)
                        .categoria("Cat")
                        .restaurante(restaurante)
                        .ativo(true)
                        .build()
        );

        mockMvc.perform(delete("/api/produtos/" + produto.getId()))
                .andExpect(status().isNoContent());
    }
}
