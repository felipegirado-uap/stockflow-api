package com.felipegirado.stockflow.controller;

import com.felipegirado.stockflow.dto.VentaRequestDTO;
import com.felipegirado.stockflow.model.Venta;
import com.felipegirado.stockflow.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    @GetMapping("/reporte-financiero")
    public ResponseEntity<?> obtenerReporteFinanciero() {
        return ResponseEntity.ok(ventaService.obtenerResumenFinanciero());
    }

    @PostMapping
    public ResponseEntity<?> registrarVenta(@Valid @RequestBody VentaRequestDTO dto) {
        try {
            Venta ventaRegistrada = ventaService.registrarVenta(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaRegistrada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}