package sistemaventa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sistemaventa.model.Cliente;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {
    // Buscar por identificación numérica
    Optional<Cliente> findByIdentificacion(Integer identificacion);

    // Buscar por email
    Optional<Cliente> findByEmail(String email);

    // Verificar si existe por identificación
    boolean existsByIdentificacion(Integer identificacion);

    // Buscar por administradorId (si es necesario)
    List<Cliente> findByAdministradorId(String administradorId);
}