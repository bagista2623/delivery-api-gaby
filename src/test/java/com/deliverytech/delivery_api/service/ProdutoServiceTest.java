package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ProdutoRequestDTO;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private RestauranteService restauranteService;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private Restaurante restaurante;

    @BeforeEach
    void setup() {
        restaurante = new Restaurante();
        restaurante.setId(1L);

        produto = Produto.builder()
                .id(1L)
                .nome("Pizza")
                .descricao("Desc teste")
                .preco(40.0)
                .categoria("Comida")
                .restaurante(restaurante)
                .ativo(true)
                .build();
    }

    // -------------------------------------------------------
    // CRIAR PRODUTO
    // -------------------------------------------------------
    @Test
    void deveCriarProdutoComSucesso() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Pizza Calabresa");
        dto.setDescricao("Descrição");
        dto.setCategoria("Pizza");
        dto.setPreco(50.0);
        dto.setRestauranteId(1L);

        when(restauranteService.buscarEntidadePorId(1L))
                .thenReturn(Optional.of(restaurante));

        when(produtoRepository.save(any())).thenReturn(produto);

        var response = produtoService.criar(dto);

        assertNotNull(response);
        verify(produtoRepository, times(1)).save(any());
    }

    @Test
    void criarProduto_deveLancarErroSeRestauranteNaoEncontrado() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setRestauranteId(99L);

        when(restauranteService.buscarEntidadePorId(99L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> produtoService.criar(dto));
    }

    // -------------------------------------------------------
    // BUSCAR POR ID
    // -------------------------------------------------------
    @Test
    void deveBuscarProdutoPorId() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        var result = produtoService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals("Pizza", result.getNome());
    }

    @Test
    void buscarPorId_deveLancarErroParaProdutoInexistente() {
        when(produtoRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> produtoService.buscarPorId(1L));
    }

    // -------------------------------------------------------
    // LISTAGEM
    // -------------------------------------------------------
    @Test
    void deveListarProdutosAtivos() {
        when(produtoRepository.findByAtivoTrue())
                .thenReturn(List.of(produto));

        var lista = produtoService.listar();

        assertEquals(1, lista.size());
    }

    // -------------------------------------------------------
    // SOFT DELETE
    // -------------------------------------------------------
    @Test
    void deveInativarProduto() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        produtoService.excluir(1L);

        assertFalse(produto.getAtivo());
        verify(produtoRepository, times(1)).save(produto);
    }
}
