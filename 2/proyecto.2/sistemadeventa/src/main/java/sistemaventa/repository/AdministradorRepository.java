// AdministradorRepository.java
package sistemaventa.repository;

import sistemaventa.model.Administrador;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface AdministradorRepository extends MongoRepository<Administrador, String> {
     Optional<Administrador> findByUsuarioAndContrasena(String usuario, String contrasena);
}