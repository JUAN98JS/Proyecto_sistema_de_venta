package sistemaventa.service;

import sistemaventa.model.Venta;
import java.util.List;
import java.util.Optional;

public interface VentaService {
    Venta guardarVenta(Venta venta);
    List<Venta> obtenerTodas();
    Optional<Venta> obtenerPorId(String id);
}