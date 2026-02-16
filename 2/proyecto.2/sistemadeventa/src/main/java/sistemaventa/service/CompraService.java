package sistemaventa.service;

import sistemaventa.model.Compra;
import sistemaventa.model.Producto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CompraService {

    /**
     * Registra una nueva compra, actualiza el stock del producto y guarda la compra.
     * @param compra objeto compra con los datos (sin ID)
     * @param producto producto al que se le actualizará el stock
     * @param cantidadComprada cantidad comprada (para actualizar stock)
     * @return la compra guardada con ID generado
     */
    Compra registrarCompra(Compra compra, Producto producto, int cantidadComprada);

    /**
     * Busca una compra por su ID (String).
     */
    Optional<Compra> obtenerPorId(String id);

    /**
     * Lista todas las compras.
     */
    List<Compra> obtenerTodas();

    /**
     * Busca compras de un producto específico (por ID del producto).
     */
    List<Compra> obtenerPorProductoId(String productoId);

    /**
     * Busca compras de un proveedor específico (por ID del proveedor).
     */
    List<Compra> obtenerPorProveedorId(String proveedorId);

    /**
     * Busca compras en un rango de fechas (opcional).
     */
    List<Compra> obtenerPorRangoFechas(Date desde, Date hasta);

    /**
     * Elimina una compra (quizás no se usa, pero por si acaso).
     */
    void eliminarCompra(String id);
}