package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    // ✅ Corrigido: agora seta ativo=true e dataCadastro
    public Restaurante cadastrar(Restaurante restaurante) {
        restaurante.setAtivo(true);
        restaurante.setDataCadastro(LocalDateTime.now());
        return restauranteRepository.save(restaurante);
    }

    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findByIdAndAtivoTrue(id);
    }

    public List<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
    }

    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaContainingIgnoreCaseAndAtivoTrue(categoria);
    }

    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Restaurante restauranteExistente = restauranteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        restauranteExistente.setNome(restauranteAtualizado.getNome());
        restauranteExistente.setCategoria(restauranteAtualizado.getCategoria());
        restauranteExistente.setEndereco(restauranteAtualizado.getEndereco());
        restauranteExistente.setTelefone(restauranteAtualizado.getTelefone());

        return restauranteRepository.save(restauranteExistente);
    }

    public void inativar(Long id) {
        Restaurante restaurante = restauranteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        restaurante.setAtivo(false);
        restauranteRepository.save(restaurante);
    }
}
