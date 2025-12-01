package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.LoginRequest;
import com.deliverytech.delivery_api.dto.LoginResponse;
import com.deliverytech.delivery_api.dto.RegisterRequest;
import com.deliverytech.delivery_api.dto.UserResponse;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.security.SecurityUtils;
import com.deliverytech.delivery_api.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Autenticação", description = "Endpoints relacionados à autenticação e usuários")
public class AuthController {

    private final AuthService authService;
    private final SecurityUtils securityUtils;



    // POST - REGISTRO
    @PostMapping("/register")
    @Operation(
            summary = "Registrar um novo usuário",
            description = "Cria um novo usuário no sistema. Pode ser CLIENTE, RESTAURANTE ou ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Erro ao registrar usuário")
            }
    )
    public ResponseEntity<UserResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para registro de usuário",
                    required = true
            )
            @RequestBody RegisterRequest request) {

        UserResponse usuario = authService.registrar(request);
        return ResponseEntity.ok(usuario);
    }



    // POST - LOGIN

    @PostMapping("/login")
    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza login e retorna o token JWT para autenticação nas rotas protegidas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
            }
    )
    public ResponseEntity<LoginResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais para login (email e senha)",
                    required = true
            )
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }



    // GET - USUÁRIO LOGADO

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Obter informações do usuário logado",
            description = "Retorna os dados do usuário autenticado com base no token JWT enviado no header.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do usuário retornados",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Token ausente ou inválido")
            }
    )
    public ResponseEntity<UserResponse> me() {

        Usuario usuario = securityUtils.getCurrentUser();

        UserResponse dto = new UserResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getRestauranteId()
        );

        return ResponseEntity.ok(dto);
    }
}
