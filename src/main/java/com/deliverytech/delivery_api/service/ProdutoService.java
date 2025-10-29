package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Cadastrar novo produto
     */
    public Produto cadastrar(Produto produto) {
        validarDadosProduto(produto);
        produto.setAtivo(true);
        return produtoRepository.save(produto);
    }

    /**
     * Buscar produto por ID
     */
    @Transactional
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    /**
     * Listar produtos ativos
     */
    @Transactional
    public List<Produto> listarAtivos() {
        return produtoRepository.findByAtivoTrue();
    }

    /**
     * Buscar produtos por nome
     */
    @Transactional
    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Atualizar produto
     */
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());

        return produtoRepository.save(produto);
    }

    /**
     * Inativar produto (soft delete)
     */
    public void inativar(Long id) {
        Produto produto = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));

        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    /**
     * Validação de dados
     */
    private void validarDadosProduto(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (produto.getPreco() == null || produto.getPreco().doubleValue() <= 0) {
            throw new IllegalArgumentException("Preço do produto deve ser maior que zero");
        }
    }
}
