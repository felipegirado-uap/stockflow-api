package com.felipegirado.stockflow.dto;

import java.math.BigDecimal;

public class RankingProductoDTO {

    private String sku;
    private String producto;
    private Long unidadesVendidas;
    private BigDecimal totalRecaudado;

    public RankingProductoDTO(String sku, String producto, Long unidadesVendidas, BigDecimal totalRecaudado) {
        this.sku = sku;
        this.producto = producto;
        this.unidadesVendidas = unidadesVendidas;
        this.totalRecaudado = totalRecaudado;
    }

    public String getSku() { return sku; }
    public String getProducto() { return producto; }
    public Long getUnidadesVendidas() { return unidadesVendidas; }
    public BigDecimal getTotalRecaudado() { return totalRecaudado; }
}