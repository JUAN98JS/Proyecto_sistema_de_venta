package sistemaventa.repository;

import sistemaventa.model.Compra;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CompraRepository extends MongoRepository<Compra, String> {
    List<Compra> findByProductoId(String productoId);
    List<Compra> findByProveedorId(String proveedorId);
    List<Compra> findByAdministradorId(String administradorId);
}