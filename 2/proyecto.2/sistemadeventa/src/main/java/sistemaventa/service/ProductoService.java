package sistemaventa.service;

import sistemaventa.model.Cliente;
import sistemaventa.model.Producto;
import sistemaventa.model.ReporteInventarioDTO;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    Producto guardarProducto(Producto producto);
    List<Producto> obtenerTodosLosProductos();
    Producto obtenerProductoPorCodigo(Integer codigo);
    Producto actualizarProducto(Producto productoActualizado);
    void eliminarProducto(Integer codigo);
    ReporteInventarioDTO generarReporteInventario();



}