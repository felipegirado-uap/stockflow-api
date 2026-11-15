package com.felipegirado.stockflow.service;

import com.felipegirado.stockflow.model.Producto;
import com.felipegirado.stockflow.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un producto con el ID: " + id));
    }

    @Transactional
    public Producto crearProducto(Producto nuevoProducto) {
        if (productoRepository.existsBySku(nuevoProducto.getSku())) {
            throw new IllegalArgumentException("Ya existe un producto registrado con el SKU: " + nuevoProducto.getSku());
        }
        return productoRepository.save(nuevoProducto);
    }

    @Transactional
    public Producto actualizarStock(Long id, int cantidadCambio) {
        Producto producto = obtenerPorId(id);
        int nuevoStock = producto.getStockActual() + cantidadCambio;

        if (nuevoStock < 0) {
            throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + producto.getStockActual());
        }

        producto.setStockActual(nuevoStock);
        return productoRepository.save(producto);
    }

    public List<Producto> obtenerAlertasStockBajo() {
        return productoRepository.encontrarProductosConStockBajo();
    }

    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar. No existe el ID: " + id);
        }
        productoRepository.deleteById(id);
    }
}