package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ProdutoRequestDTO;
import com.deliverytech.delivery_api.dto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.security.SecurityUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final RestauranteService restauranteService;
    private final SecurityUtils securityUtils;

    // ------------------------------------
    // CRIAR PRODUTO
    // Invalida todo o cache ao criar
    // ------------------------------------
    @CacheEvict(value = "produtosCache", allEntries = true)
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {

        Restaurante restaurante = restauranteService
                .buscarEntidadePorId(dto.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(dto.getCategoria());
        produto.setRestaurante(restaurante);
        produto.setAtivo(true);

        return new ProdutoResponseDTO(produtoRepository.save(produto));
    }

    // ------------------------------------
    // LISTAR TODOS (ATIVOS)
    // Usa cache da lista de produtos
    // ------------------------------------
    @Cacheable(value = "produtosCache", key = "'listaTodos'")
    public List<ProdutoResponseDTO> listar() {
        return produtoRepository.findByAtivoTrue()
                .stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    // ------------------------------------
    // BUSCAR POR ID
    // Cada produto tem seu próprio cache
    // ------------------------------------
    @Cacheable(value = "produtosCache", key = "#id")
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .filter(Produto::getAtivo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        return new ProdutoResponseDTO(produto);
    }

    // ------------------------------------
    // BUSCAR POR NOME
    // Cache mais específico por nome
    // ------------------------------------
    @Cacheable(value = "produtosCache", key = "'nome_' + #nome")
    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome)
                .stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    // ------------------------------------
    // BUSCAR POR RESTAURANTE
    // Cache por restaurante
    // ------------------------------------
    @Cacheable(value = "produtosCache", key = "'rest_' + #restauranteId")
    public List<ProdutoResponseDTO> listarPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteIdAndAtivoTrue(restauranteId)
                .stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    // ------------------------------------
    // ATUALIZAR PRODUTO
    // Atualização sempre invalida o cache
    // ------------------------------------
    @CacheEvict(value = "produtosCache", allEntries = true)
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(dto.getCategoria());

        Restaurante restaurante = restauranteService
                .buscarEntidadePorId(dto.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        produto.setRestaurante(restaurante);

        return new ProdutoResponseDTO(produtoRepository.save(produto));
    }

    // ------------------------------------
    // SOFT DELETE
    // Limpa todo o cache ao excluir
    // ------------------------------------
    @CacheEvict(value = "produtosCache", allEntries = true)
    public void excluir(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    // ------------------------------------
    // ALTERAR DISPONIBILIDADE
    // Também invalida cache
    // ------------------------------------
    @CacheEvict(value = "produtosCache", allEntries = true)
    public ProdutoResponseDTO alterarDisponibilidade(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        produto.setAtivo(!produto.getAtivo());
        return new ProdutoResponseDTO(produtoRepository.save(produto));
    }

    // ------------------------------------
    // VERIFICAR SE RESTAURANTE É DONO
    // Não precisa de cache (é dinâmico por segurança)
    // ------------------------------------
    public boolean isOwner(Long produtoId) {

        Usuario user = securityUtils.getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.ADMIN) return true;

        Produto produto = produtoRepository.findById(produtoId).orElse(null);
        if (produto == null) return false;

        Long restauranteDoProduto = produto.getRestaurante().getId();

        return user.getRole() == Role.RESTAURANTE &&
                user.getRestauranteId() != null &&
                user.getRestauranteId().equals(restauranteDoProduto);
    }
}
