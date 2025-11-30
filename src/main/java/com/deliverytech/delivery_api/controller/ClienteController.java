package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ClienteRequestDTO;
import com.deliverytech.delivery_api.dto.ClienteResponseDTO;
import com.deliverytech.delivery_api.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Clientes", description = "Operações relacionadas aos clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Cadastrar cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<?> cadastrar(@Valid @RequestBody ClienteRequestDTO cliente) {
        ClienteResponseDTO novo = clienteService.cadastrar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @GetMapping
    @Operation(summary = "Listar clientes ativos")
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        return ResponseEntity.ok(clienteService.listarAtivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        Optional<ClienteResponseDTO> cliente = clienteService.buscarPorId(id);

        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente não encontrado");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO clienteAtualizado) {

        ClienteResponseDTO atualizado = clienteService.atualizar(id, clienteAtualizado);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inativar cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente inativado com sucesso")
    })
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        clienteService.inativar(id);
        return ResponseEntity.ok("Cliente inativado com sucesso");
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes por nome")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(clienteService.buscarPorNome(nome));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar cliente por e-mail")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        Optional<ClienteResponseDTO> cliente = clienteService.buscarPorEmail(email);

        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente não encontrado");
        }
    }
}
