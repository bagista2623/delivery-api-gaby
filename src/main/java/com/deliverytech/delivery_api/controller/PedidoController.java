package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.PedidoResponseDTO;
import com.deliverytech.delivery_api.service.PedidoService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pedidos", description = "Endereços relacionados ao fluxo de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // -----------------------------------------------------
    // POST – Criar Pedido
    // -----------------------------------------------------
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(
            summary = "Criar novo pedido",
            description = "Cria um novo pedido vinculado a um cliente e restaurante.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Erro ao criar pedido")
            }
    )
    public ResponseEntity<?> criarPedido(
            @Valid @RequestBody PedidoRequestDTO dto) {

        try {
            PedidoResponseDTO criado = pedidoService.criarPedido(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar pedido: " + e.getMessage());
        }
    }


    // -----------------------------------------------------
    // GET – Buscar por ID
    // -----------------------------------------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @pedidoService.canAccess(#id)")
    @Operation(
            summary = "Buscar pedido por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
            }
    )
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID do pedido")
            @PathVariable Long id) {

        Optional<PedidoResponseDTO> pedido = pedidoService.buscarPorId(id);

        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
    }


    // -----------------------------------------------------
    // GET – Listar com filtros (ADMIN)
    // -----------------------------------------------------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Listar pedidos com filtros",
            description = "Permite filtrar pedidos por status e intervalo de datas"
    )
    public ResponseEntity<Page<PedidoResponseDTO>> listar(
            @Parameter(description = "Status do pedido") @RequestParam(required = false) String status,
            @Parameter(description = "Data inicial (YYYY-MM-DD)") @RequestParam(required = false) String dataInicio,
            @Parameter(description = "Data final (YYYY-MM-DD)") @RequestParam(required = false) String dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PedidoResponseDTO> pagina = pedidoService.listarComFiltros(
                status,
                dataInicio != null ? LocalDate.parse(dataInicio) : null,
                dataFim != null ? LocalDate.parse(dataFim) : null,
                page,
                size
        );
        return ResponseEntity.ok(pagina);
    }


    // -----------------------------------------------------
    // GET – Pedidos de um Cliente
    // -----------------------------------------------------
    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('ADMIN') or @pedidoService.canAccess(#clienteId)")
    @Operation(summary = "Listar pedidos de um cliente")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorCliente(
            @Parameter(description = "ID do cliente")
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(pedidoService.buscarPedidosPorCliente(clienteId));
    }


    // -----------------------------------------------------
    // GET – Pedidos de um Restaurante
    // -----------------------------------------------------
    @GetMapping("/restaurante/{restauranteId}")
    @PreAuthorize("hasRole('ADMIN') or @pedidoService.canAccess(#restauranteId)")
    @Operation(summary = "Listar pedidos de um restaurante")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorRestaurante(
            @Parameter(description = "ID do restaurante")
            @PathVariable Long restauranteId) {

        return ResponseEntity.ok(pedidoService.buscarPedidosPorRestaurante(restauranteId));
    }


    // -----------------------------------------------------
    // PATCH – Atualizar status
    // -----------------------------------------------------
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or @pedidoService.canAccess(#id)")
    @Operation(
            summary = "Atualizar status do pedido",
            description = "Permite atualizar o status do pedido (ex: RECEBIDO → PREPARANDO → ENTREGUE)."
    )
    public ResponseEntity<?> atualizarStatus(
            @Parameter(description = "ID do pedido") @PathVariable Long id,
            @Parameter(description = "Novo status") @RequestParam String status) {

        try {
            pedidoService.atualizarStatusPedido(id, status);
            return ResponseEntity.ok("Status atualizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar status: " + e.getMessage());
        }
    }


    // -----------------------------------------------------
    // POST – Calcular total
    // -----------------------------------------------------
    @PostMapping("/calcular")
    @Operation(
            summary = "Calcular total do pedido",
            description = "Calcula o valor total do pedido com base nos itens enviados."
    )
    public ResponseEntity<Double> calcularTotal(
            @Valid @RequestBody List<ItemPedidoDTO> itens) {

        return ResponseEntity.ok(pedidoService.calcularTotalPedido(itens));
    }


    // -----------------------------------------------------
    // DELETE – Cancelar pedido
    // -----------------------------------------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @pedidoService.canAccess(#id)")
    @Operation(summary = "Cancelar pedido")
    public ResponseEntity<?> cancelar(
            @Parameter(description = "ID do pedido")
            @PathVariable Long id) {

        try {
            pedidoService.cancelarPedido(id);
            return ResponseEntity.ok("Pedido cancelado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cancelar pedido: " + e.getMessage());
        }
    }
}
