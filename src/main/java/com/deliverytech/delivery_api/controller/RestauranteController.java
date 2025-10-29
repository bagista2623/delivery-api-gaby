package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes") // define o caminho base da API
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    // GET - Listar todos
    @GetMapping
    public List<Restaurante> listarTodos() {
        return restauranteService.listarTodos();
    }

    // GET - Buscar por nome
    @GetMapping("/nome")
    public List<Restaurante> buscarPorNome(@RequestParam String valor) {
        return restauranteService.buscarPorNome(valor);
    }

    // GET - Buscar por categoria
    @GetMapping("/categoria")
    public List<Restaurante> buscarPorCategoria(@RequestParam String valor) {
        return restauranteService.buscarPorCategoria(valor);
    }

    // GET - Listar apenas ativos
    @GetMapping("/ativos")
    public List<Restaurante> listarAtivos() {
        return restauranteService.buscarAtivos();
    }

    // PUT - Atualizar restaurante
    @PutMapping("/{id}")
    public Restaurante atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
        return restauranteService.atualizar(id, restaurante);
    }

    // DELETE - Inativar restaurante
    @DeleteMapping("/{id}")
    public String inativar(@PathVariable Long id) {
        restauranteService.inativar(id);
        return "Restaurante inativado com sucesso";
    }
}