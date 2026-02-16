// VentaRepository.java
package sistemaventa.repository;

import sistemaventa.model.Venta;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface VentaRepository extends MongoRepository<Venta, String> {
    // Puedes agregar métodos personalizados según necesidades futuras
    List<Venta> findByClienteId(String clienteId);
    List<Venta> findByAdministradorId(String administradorId);
}