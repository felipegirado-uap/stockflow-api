package com.felipegirado.stockflow.service;

import com.felipegirado.stockflow.dto.VentaRequestDTO;
import com.felipegirado.stockflow.model.Producto;
import com.felipegirado.stockflow.model.Venta;
import com.felipegirado.stockflow.repository.ProductoRepository;
import com.felipegirado.stockflow.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    @Transactional
    public Venta registrarVenta(VentaRequestDTO dto) {
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("No existe el producto con ID: " + dto.getProductoId()));

        if (producto.getStockActual() < dto.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente para '" + producto.getNombre() + 
                    "'. Disponible: " + producto.getStockActual() + ", Solicitado: " + dto.getCantidad());
        }

        producto.setStockActual(producto.getStockActual() - dto.getCantidad());
        productoRepository.save(producto);

        // Congelo el precio unitario del momento de la venta por si el producto aumenta despues
        Venta nuevaVenta = new Venta();
        nuevaVenta.setProducto(producto);
        nuevaVenta.setCantidad(dto.getCantidad());
        nuevaVenta.setPrecioUnitarioHistorico(producto.getPrecio());
        nuevaVenta.setTotalVenta(producto.getPrecio().multiply(BigDecimal.valueOf(dto.getCantidad())));

        return ventaRepository.save(nuevaVenta);
    }

    public Map<String, Object> obtenerResumenFinanciero() {
        Map<String, Object> reporte = new LinkedHashMap<>();
        reporte.put("recaudacionTotalHistorica", ventaRepository.obtenerRecaudacionTotal());
        reporte.put("cantidadOperaciones", ventaRepository.count());
        reporte.put("rankingProductos", ventaRepository.obtenerRankingVentasPorProducto());
        return reporte;
    }
}