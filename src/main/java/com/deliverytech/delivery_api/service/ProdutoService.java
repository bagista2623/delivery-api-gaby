package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ProdutoRequestDTO;
import com.deliverytech.delivery_api.dto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {
        validarDados(dto);
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setAtivo(true);

        Produto salvo = produtoRepository.save(produto);
        return new ProdutoResponseDTO(salvo);
    }

    public Optional<ProdutoResponseDTO> buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .filter(Produto::getAtivo)
                .map(ProdutoResponseDTO::new);
    }

    public List<ProdutoResponseDTO> listarAtivos() {
        return produtoRepository.findByAtivoTrue()
                .stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));

        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());

        Produto atualizado = produtoRepository.save(produto);
        return new ProdutoResponseDTO(atualizado);
    }

    public void inativar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));

        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    private void validarDados(ProdutoRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (dto.getPreco() == null || dto.getPreco() <= 0) {
            throw new IllegalArgumentException("Preço do produto deve ser maior que zero");
        }
    }
}
