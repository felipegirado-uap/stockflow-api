# StockFlow API

API REST desarrollada con **Java 21** y **Spring Boot** para gestionar el inventario de productos, registrar ventas descontando stock de forma automática y consultar reportes de recaudación.

Armé este proyecto para llevar a la práctica el diseño en capas con Spring Data JPA conectado a una base de datos relacional en **SQL Server**.

## Tecnologías utilizadas
* **Java 21** + **Spring Boot**
* **Spring Data JPA (Hibernate)**
* **Microsoft SQL Server**
* **Jakarta Validation**
* **Maven**

## Estructura del proyecto
El código está organizado dentro del paquete `com.felipegirado.stockflow`:
* `model`: Entidades JPA (`Producto`, `Venta`) relacionadas mediante `@ManyToOne`.
* `dto`: Objetos de transferencia (`VentaRequestDTO`, `RankingProductoDTO`) para manejar las peticiones de entrada y las respuestas agrupadas.
* `repository`: Interfaces de acceso a datos con consultas derivadas y queries en JPQL (`GROUP BY`, `SUM`, control de stock mínimo).
* `service`: Lógica de negocio y manejo transaccional (`@Transactional`) para asegurar que el descuento de stock y el registro de la venta se ejecuten como una operación atómica.
* `controller`: Endpoints REST expuestos bajo `/api/productos` y `/api/ventas`.

## Endpoints principales

### Productos (`/api/productos`)
* `GET /api/productos` - Lista todos los productos cargados.
* `GET /api/productos/{id}` - Busca un producto por su ID.
* `GET /api/productos/alertas-stock` - Devuelve los productos donde `stockActual <= stockMinimo`.
* `POST /api/productos` - Da de alta un producto nuevo (valida que el código SKU no esté repetido).
* `PATCH /api/productos/{id}/stock?cantidad={n}` - Suma o resta unidades validando que el stock no quede en negativo.
* `DELETE /api/productos/{id}` - Elimina un producto por ID.

### Ventas (`/api/ventas`)
* `GET /api/ventas` - Historial de ventas registradas.
* `POST /api/ventas` - Registra una venta nueva, descuenta el stock del producto y guarda el precio unitario histórico del momento de la operación.
* `GET /api/ventas/reporte-financiero` - Devuelve el total recaudado histórico, la cantidad de operaciones y el ranking de productos más vendidos.

## Ejemplos de prueba (JSON)

**Alta de producto (`POST /api/productos`):**
```json
{
  "sku": "TEC-MEC-001",
  "nombre": "Teclado Mecánico Redragon Kumara",
  "precio": 65000.50,
  "stockActual": 15,
  "stockMinimo": 5
}
```

**Registro de venta (`POST /api/ventas`):**
```json
{
  "productoId": 1,
  "cantidad": 3
}
```

## Ejecución local
1. Crear una base de datos llamada `StockFlowDB` en SQL Server.
2. Configurar el usuario y contraseña en `src/main/resources/application.properties`.
3. Ejecutar la clase `StockflowApiApplication.java` (Hibernate crea y actualiza las tablas automáticamente mediante `ddl-auto=update`).