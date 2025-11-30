package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.RelatorioClientesAtivosDTO;
import com.deliverytech.delivery_api.dto.RelatorioPedidosPeriodoDTO;
import com.deliverytech.delivery_api.dto.RelatorioProdutoMaisVendidoDTO;
import com.deliverytech.delivery_api.dto.RelatorioVendasRestauranteDTO;
import com.deliverytech.delivery_api.service.RelatorioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/vendas-por-restaurante")
    public List<RelatorioVendasRestauranteDTO> vendasPorRestaurante() {
        return relatorioService.gerarVendasPorRestaurante();
    }

    @GetMapping("/produtos-mais-vendidos")
    public List<RelatorioProdutoMaisVendidoDTO> produtosMaisVendidos() {
        return relatorioService.gerarProdutosMaisVendidos();
    }

    @GetMapping("/clientes-ativos")
    public List<RelatorioClientesAtivosDTO> clientesAtivos() {
        return relatorioService.gerarClientesAtivos();
    }

    @GetMapping("/pedidos-por-periodo")
    public RelatorioPedidosPeriodoDTO pedidosPorPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim
    ) {
        return relatorioService.gerarPedidosPorPeriodo(dataInicio, dataFim);
    }
}
