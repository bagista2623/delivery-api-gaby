package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    public Restaurante cadastrar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }

    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }

    public List<Restaurante> buscarPorNome(String valor) {
        return restauranteRepository.findByNomeContainingIgnoreCase(valor);
    }

    public List<Restaurante> buscarPorCategoria(String valor) {
        return restauranteRepository.findByCategoriaContainingIgnoreCase(valor);
    }

    public List<Restaurante> buscarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Optional<Restaurante> optionalRestaurante = restauranteRepository.findById(id);

        if (optionalRestaurante.isPresent()) {
            Restaurante restaurante = optionalRestaurante.get();
            restaurante.setNome(restauranteAtualizado.getNome());
            restaurante.setCategoria(restauranteAtualizado.getCategoria());
            restaurante.setTelefone(restauranteAtualizado.getTelefone());
            return restauranteRepository.save(restaurante);
        } else {
            throw new RuntimeException("Restaurante não encontrado");
        }
    }

    public void inativar(Long id) {
        Optional<Restaurante> optionalRestaurante = restauranteRepository.findById(id);

        if (optionalRestaurante.isPresent()) {
            Restaurante restaurante = optionalRestaurante.get();
            restaurante.setAtivo(false);
            restauranteRepository.save(restaurante);
        } else {
            throw new RuntimeException("Restaurante não encontrado");
        }
    }
}
