package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.RestauranteRequestDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;
import com.deliverytech.delivery_api.service.RestauranteService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth") // ✅ ADICIONADO
@Tag(
        name = "Restaurantes",
        description = "Endpoints para gerenciamento e consulta de restaurantes."
)
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;


    // ------------------------------------
    // POST – Criar restaurante (ADMIN)
    // ------------------------------------
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Criar um novo restaurante",
            description = "Disponível apenas para ADMIN. Cria um restaurante ativo no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Restaurante criado",
                            content = @Content(schema = @Schema(implementation = RestauranteResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Erro de validação",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno")
            }
    )
    public ResponseEntity<?> cadastrar(
            @Valid @RequestBody RestauranteRequestDTO dto
    ) {
        try {
            RestauranteResponseDTO novo = restauranteService.cadastrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }


    // ------------------------------------
    // GET – Listagem com filtros + paginação (PÚBLICO)
    // ------------------------------------
    @GetMapping
    @Operation(
            summary = "Listar restaurantes com filtros e paginação",
            description = "Endpoint público para consultar restaurantes por categoria, status e paginação."
    )
    public ResponseEntity<Page<RestauranteResponseDTO>> listarComFiltros(
            @Parameter(description = "Categoria do restaurante") @RequestParam(required = false) String categoria,
            @Parameter(description = "Status (true = ativo)") @RequestParam(required = false) Boolean ativo,
            @Parameter(description = "Página atual") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        Page<RestauranteResponseDTO> lista = restauranteService.listarComFiltros(
                categoria, ativo, page, size);
        return ResponseEntity.ok(lista);
    }


    // ------------------------------------
    // GET – Buscar por ID (PÚBLICO)
    // ------------------------------------
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar restaurante por ID",
            description = "Endpoint público para buscar restaurante ativo pelo ID."
    )
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID do restaurante") @PathVariable Long id
    ) {
        var restaurante = restauranteService.buscarPorId(id);

        if (restaurante.isPresent()) {
            return ResponseEntity.ok(restaurante.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Restaurante não encontrado");
    }


    // ------------------------------------
    // GET – Calcular taxa de entrega (PÚBLICO)
    // ------------------------------------
    @GetMapping("/{id}/taxa-entrega/{cep}")
    @Operation(
            summary = "Calcular taxa de entrega",
            description = "Simula cálculo de taxa de entrega com base no CEP informado."
    )
    public ResponseEntity<Double> calcularTaxaEntrega(
            @Parameter(description = "ID do restaurante") @PathVariable Long id,
            @Parameter(description = "CEP do cliente") @PathVariable String cep
    ) {
        double taxa = restauranteService.calcularTaxaEntrega(id, cep);
        return ResponseEntity.ok(taxa);
    }


    // ------------------------------------
    // GET – Restaurantes próximos (PÚBLICO)
    // ------------------------------------
    @GetMapping("/proximos/{cep}")
    @Operation(
            summary = "Listar restaurantes próximos",
            description = "Retorna restaurantes próximos de acordo com lógica simplificada."
    )
    public ResponseEntity<List<RestauranteResponseDTO>> listarProximos(
            @Parameter(description = "CEP do cliente") @PathVariable String cep
    ) {
        return ResponseEntity.ok(restauranteService.listarProximos(cep));
    }


    // ------------------------------------
    // PUT – Atualizar restaurante
    // ADMIN OU DONO DO RESTAURANTE
    // ------------------------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('RESTAURANTE') and @restauranteService.isOwner(#id))")
    @Operation(
            summary = "Atualizar restaurante",
            description = "Disponível para ADMIN ou dono do restaurante."
    )
    public ResponseEntity<?> atualizar(
            @Parameter(description = "ID do restaurante") @PathVariable Long id,
            @Valid @RequestBody RestauranteRequestDTO dto
    ) {
        try {
            RestauranteResponseDTO atualizado = restauranteService.atualizar(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }


    // ------------------------------------
    // PATCH – Ativar / Desativar (ADMIN)
    // ------------------------------------
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Alterar status do restaurante",
            description = "ADMIN pode ativar ou desativar um restaurante."
    )
    public ResponseEntity<?> alterarStatus(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(restauranteService.toggleStatus(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }


    // ------------------------------------
    // DELETE – Inativar restaurante (ADMIN)
    // ------------------------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Inativar restaurante",
            description = "Inativa um restaurante sem removê-lo do banco."
    )
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            restauranteService.inativar(id);
            return ResponseEntity.ok("Restaurante inativado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }


    // ------------------------------------
    // GET – Buscar por nome (PÚBLICO)
    // ------------------------------------
    @GetMapping("/buscar")
    @Operation(summary = "Buscar restaurantes por nome")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorNome(
            @Parameter(description = "Nome do restaurante") @RequestParam String nome
    ) {
        return ResponseEntity.ok(restauranteService.buscarPorNome(nome));
    }


    // ------------------------------------
    // GET – Buscar por categoria (PÚBLICO)
    // ------------------------------------
    @GetMapping("/categoria")
    @Operation(summary = "Buscar restaurantes por categoria")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(
            @Parameter(description = "Categoria do restaurante") @RequestParam String categoria
    ) {
        return ResponseEntity.ok(restauranteService.buscarPorCategoria(categoria));
    }
}
