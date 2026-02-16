package sistemaventa.service;

import sistemaventa.model.Proveedor;
import java.util.List;
import java.util.Optional;

public interface ProveedorService {

    Proveedor guardarProveedor(Proveedor proveedor);

    List<Proveedor> listarProveedores();

    Optional<Proveedor> obtenerPorId(String id);

    Optional<Proveedor> obtenerPorCodigo(Integer codigoProveedor);

    void eliminarProveedor(String id);

    void eliminarPorCodigo(Integer codigoProveedor);
}