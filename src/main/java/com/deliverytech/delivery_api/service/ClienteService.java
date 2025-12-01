package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ClienteRequestDTO;
import com.deliverytech.delivery_api.dto.ClienteResponseDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Cadastrar novo cliente (agora recebe DTO)
     * Invalida todo o cache de clientes
     */
    @CacheEvict(value = "clientesCache", allEntries = true)
    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + dto.getEmail());
        }

        validarDadosCliente(dto);

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());

        Cliente salvo = clienteRepository.save(cliente);

        return new ClienteResponseDTO(salvo);
    }

    /**
     * Buscar cliente por ID
     * Cada cliente tem seu próprio cache
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "clientesCache", key = "#id")
    public Optional<ClienteResponseDTO> buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .map(ClienteResponseDTO::new);
    }

    /**
     * Buscar cliente por email
     * Cache separado por email (chave string)
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "clientesCache", key = "'email_' + #email")
    public Optional<ClienteResponseDTO> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .map(ClienteResponseDTO::new);
    }

    /**
     * Listar todos os clientes ativos
     * Cache para lista completa
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "clientesCache", key = "'ativos'")
    public List<ClienteResponseDTO> listarAtivos() {
        return clienteRepository.findByAtivoTrue()
                .stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Atualizar dados do cliente
     * Sempre limpa todo o cache
     */
    @CacheEvict(value = "clientesCache", allEntries = true)
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));

        if (!cliente.getEmail().equals(dto.getEmail()) &&
                clienteRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + dto.getEmail());
        }

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());

        Cliente atualizado = clienteRepository.save(cliente);
        return new ClienteResponseDTO(atualizado);
    }

    /**
     * Inativar cliente (soft delete)
     * Invalida o cache porque altera dados
     */
    @CacheEvict(value = "clientesCache", allEntries = true)
    public void inativar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));

        cliente.inativar();
        clienteRepository.save(cliente);
    }

    /**
     * Buscar clientes por nome
     * Cache específico para nome
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "clientesCache", key = "'nome_' + #nome")
    public List<ClienteResponseDTO> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Validações de negócio
     */
    private void validarDadosCliente(ClienteRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        if (dto.getNome().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
    }
}
