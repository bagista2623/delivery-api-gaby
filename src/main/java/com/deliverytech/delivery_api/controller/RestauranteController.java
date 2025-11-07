package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.RestauranteRequestDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;
import com.deliverytech.delivery_api.service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin(origins = "*")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody RestauranteRequestDTO dto) {
        try {
            RestauranteResponseDTO novo = restauranteService.cadastrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    @GetMapping
    public ResponseEntity<List<RestauranteResponseDTO>> listar() {
        return ResponseEntity.ok(restauranteService.listarAtivos());
    }

    @GetMapping("/{id}/taxa-entrega/{cep}")
    public ResponseEntity<Double> calcularTaxaEntrega(@PathVariable Long id, @PathVariable String cep) {
        double taxa = restauranteService.calcularTaxaEntrega(id, cep);
        return ResponseEntity.ok(taxa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody RestauranteRequestDTO dto) {
        try {
            RestauranteResponseDTO atualizado = restauranteService.atualizar(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            restauranteService.inativar(id);
            return ResponseEntity.ok("Restaurante inativado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(restauranteService.buscarPorNome(nome));
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@RequestParam String categoria) {
        return ResponseEntity.ok(restauranteService.buscarPorCategoria(categoria));
    }
}
