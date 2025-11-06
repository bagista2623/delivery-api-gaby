package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.RestauranteRequestDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setEspecialidade(dto.getEspecialidade());
        restaurante.setDataCadastro(LocalDateTime.now());
        restaurante.setAtivo(true);
        Restaurante salvo = restauranteRepository.save(restaurante);
        return new RestauranteResponseDTO(salvo);
    }

    public List<RestauranteResponseDTO> listarAtivos() {
        return restauranteRepository.findByAtivoTrue()
                .stream()
                .map(RestauranteResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<RestauranteResponseDTO> buscarPorId(Long id) {
        return restauranteRepository.findByIdAndAtivoTrue(id)
                .map(RestauranteResponseDTO::new);
    }

    public RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO dto) {
        Restaurante restaurante = restauranteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        restaurante.setNome(dto.getNome());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setEspecialidade(dto.getEspecialidade());

        Restaurante atualizado = restauranteRepository.save(restaurante);
        return new RestauranteResponseDTO(atualizado);
    }

    public void inativar(Long id) {
        Restaurante restaurante = restauranteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        restaurante.setAtivo(false);
        restauranteRepository.save(restaurante);
    }

    public List<RestauranteResponseDTO> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome)
                .stream()
                .map(RestauranteResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaContainingIgnoreCaseAndAtivoTrue(categoria)
                .stream()
                .map(RestauranteResponseDTO::new)
                .collect(Collectors.toList());
    }
}
