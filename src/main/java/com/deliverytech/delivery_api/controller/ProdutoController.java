package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ProdutoRequestDTO;
import com.deliverytech.delivery_api.dto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.service.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Produtos",
        description = "Endpoints para gerenciamento e consulta de produtos."
)
public class ProdutoController {

    private final ProdutoService produtoService;



    // POST – Criar produto

    @PostMapping
    @Operation(
            summary = "Criar um novo produto",
            description = "Cria um produto vinculado a um restaurante existente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Erro de validação")
            }
    )
    public ResponseEntity<ProdutoResponseDTO> criar(
            @Parameter(description = "Dados do produto")
            @RequestBody ProdutoRequestDTO dto
    ) {
        ProdutoResponseDTO produto = produtoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }



    // GET – Listar todos produtos

    @GetMapping
    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna apenas os produtos ativos."
    )
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }



    // GET – Buscar por ID

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar produto por ID",
            description = "Busca produto ativo pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto encontrado"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID do produto")
            @PathVariable Long id
    ) {
        ProdutoResponseDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }


    // ------------------------------------
    // GET – Buscar por nome
    // ------------------------------------
    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar produtos por nome",
            description = "Retorna produtos cujo nome contém o texto informado (case insensitive)."
    )
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorNome(
            @Parameter(description = "Nome para busca")
            @RequestParam String nome
    ) {
        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }


    // ------------------------------------
    // PUT – Atualizar produto
    // ------------------------------------
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar produto por ID",
            description = "Atualiza dados de um produto existente."
    )
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @Parameter(description = "ID do produto")
            @PathVariable Long id,

            @Parameter(description = "Novos dados para atualização")
            @RequestBody ProdutoRequestDTO dto
    ) {
        ProdutoResponseDTO atualizado = produtoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }


    // ------------------------------------
    // DELETE – Excluir
    // ------------------------------------
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir produto (soft delete)",
            description = "Desativa o produto ao invés de remover do banco."
    )
    public ResponseEntity<?> deletar(
            @Parameter(description = "ID do produto")
            @PathVariable Long id
    ) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
