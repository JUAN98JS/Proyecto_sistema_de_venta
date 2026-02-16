// ProductoRepository.java
package sistemaventa.repository;

import sistemaventa.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ProductoRepository extends MongoRepository<Producto, String> {
    // Buscar producto por su código numérico (campo 'codigoProducto')
    Optional<Producto> findByCodigoProducto(Integer codigoProducto);
}