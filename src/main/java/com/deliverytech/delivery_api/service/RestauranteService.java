package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.RestauranteRequestDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.security.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    @Autowired
    private SecurityUtils securityUtils;

    // ---------------------------------------------------
    // NOVO MÉTODO — RETORNA ENTIDADE (necessário para ProdutoService)
    // ---------------------------------------------------
    public Optional<Restaurante> buscarEntidadePorId(Long id) {
        return restauranteRepository.findById(id);
    }

    // ---------------------------------------------------
    // CADASTRAR
    // ---------------------------------------------------
    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setCategoria(dto.getCategoria());
        restaurante.setDataCadastro(LocalDateTime.now());
        restaurante.setAtivo(true);

        Restaurante salvo = restauranteRepository.save(restaurante);
        return new RestauranteResponseDTO(salvo);
    }

    // ---------------------------------------------------
    // LISTAR COM FILTROS + PAGINAÇÃO
    // ---------------------------------------------------
    public Page<RestauranteResponseDTO> listarComFiltros(
            String categoria,
            Boolean ativo,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());

        List<Restaurante> lista = restauranteRepository.findAll();

        if (categoria != null && !categoria.isBlank()) {
            lista = lista.stream()
                    .filter(r -> r.getCategoria() != null &&
                            r.getCategoria().toLowerCase().contains(categoria.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (ativo != null) {
            lista = lista.stream()
                    .filter(r -> r.getAtivo() == ativo)
                    .collect(Collectors.toList());
        }

        List<RestauranteResponseDTO> dtoList = lista.stream()
                .map(RestauranteResponseDTO::new)
                .collect(Collectors.toList());

        int start = Math.min(page * size, dtoList.size());
        int end = Math.min(start + size, dtoList.size());
        List<RestauranteResponseDTO> pageContent = dtoList.subList(start, end);

        return new PageImpl<>(pageContent, pageable, dtoList.size());
    }

    // ---------------------------------------------------
    // LISTAR ATIVOS
    // ---------------------------------------------------
    public List<RestauranteResponseDTO> listarAtivos() {
        return restauranteRepository.findByAtivoTrue()
                .stream()
                .map(RestauranteResponseDTO::new)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------
    // BUSCAR POR ID (RETORNA DTO)
    // ---------------------------------------------------
    public Optional<RestauranteResponseDTO> buscarPorId(Long id) {
        return restauranteRepository.findByIdAndAtivoTrue(id)
                .map(RestauranteResponseDTO::new);
    }

    // ---------------------------------------------------
    // ATUALIZAR
    // ---------------------------------------------------
    public RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO dto) {
        Restaurante restaurante = restauranteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        restaurante.setNome(dto.getNome());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setCategoria(dto.getCategoria());

        Restaurante atualizado = restauranteRepository.save(restaurante);
        return new RestauranteResponseDTO(atualizado);
    }

    // ---------------------------------------------------
    // INATIVAR
    // ---------------------------------------------------
    public void inativar(Long id) {
        Restaurante restaurante = restauranteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        restaurante.setAtivo(false);
        restauranteRepository.save(restaurante);
    }

    // ---------------------------------------------------
    // TOGGLE STATUS
    // ---------------------------------------------------
    public RestauranteResponseDTO toggleStatus(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        restaurante.setAtivo(!restaurante.getAtivo());
        restauranteRepository.save(restaurante);

        return new RestauranteResponseDTO(restaurante);
    }

    // ---------------------------------------------------
    // BUSCAR POR NOME
    // ---------------------------------------------------
    public List<RestauranteResponseDTO> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome)
                .stream()
                .map(RestauranteResponseDTO::new)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------
    // BUSCAR POR CATEGORIA
    // ---------------------------------------------------
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaContainingIgnoreCaseAndAtivoTrue(categoria)
                .stream()
                .map(RestauranteResponseDTO::new)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------
    // CALCULAR TAXA ENTREGA
    // ---------------------------------------------------
    public double calcularTaxaEntrega(Long restauranteId, String cep) {
        if (cep.endsWith("0") || cep.endsWith("5")) {
            return 8.99;
        } else {
            return 5.99;
        }
    }

    // ---------------------------------------------------
    // RESTAURANTES PRÓXIMOS
    // ---------------------------------------------------
    public List<RestauranteResponseDTO> listarProximos(String cep) {
        return restauranteRepository.findByAtivoTrue()
                .stream()
                .filter(r -> r.getNome().length() % 2 == cep.length() % 2)
                .map(RestauranteResponseDTO::new)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------
    // AUTORIZAÇÃO — IMPORTANTE
    // ---------------------------------------------------
    public boolean isOwner(Long restauranteId) {

        Usuario user = securityUtils.getCurrentUser();
        if (user == null) return false;

        if (user.getRole() == Role.ADMIN) return true;

        return user.getRole() == Role.RESTAURANTE &&
                user.getRestauranteId() != null &&
                user.getRestauranteId().equals(restauranteId);
    }
}
