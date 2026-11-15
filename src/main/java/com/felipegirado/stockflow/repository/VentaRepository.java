package com.felipegirado.stockflow.repository;

import com.felipegirado.stockflow.dto.RankingProductoDTO;
import com.felipegirado.stockflow.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByProductoId(Long productoId);

    @Query("SELECT COALESCE(SUM(v.totalVenta), 0) FROM Venta v")
    BigDecimal obtenerRecaudacionTotal();

    @Query("SELECT new com.felipegirado.stockflow.dto.RankingProductoDTO(" +
           "v.producto.sku, v.producto.nombre, SUM(v.cantidad), SUM(v.totalVenta)) " +
           "FROM Venta v GROUP BY v.producto.sku, v.producto.nombre " +
           "ORDER BY SUM(v.totalVenta) DESC")
    List<RankingProductoDTO> obtenerRankingVentasPorProducto();
}