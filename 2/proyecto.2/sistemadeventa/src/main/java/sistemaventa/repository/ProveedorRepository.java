package sistemaventa.repository;

import sistemaventa.model.Proveedor;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ProveedorRepository extends MongoRepository<Proveedor, String> {

    // Buscar proveedor por su código numérico (campo de negocio)
    Optional<Proveedor> findByCodigoProveedor(Integer codigoProveedor);

    // Verificar si existe un proveedor con ese código
    boolean existsByCodigoProveedor(Integer codigoProveedor);
}