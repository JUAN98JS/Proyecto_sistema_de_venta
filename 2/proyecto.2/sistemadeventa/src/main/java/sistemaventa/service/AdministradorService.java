package sistemaventa.service;

import java.util.List;
import java.util.Optional;

import sistemaventa.model.Administrador;

public interface AdministradorService {

    List<Administrador> findAll();

    Administrador save(Administrador admin);
    Optional<Administrador> findById(String id);


    void deleteById(String id);

    Optional<Administrador> findByUsuarioAndContrasena(String usuario, String contrasena);
}
