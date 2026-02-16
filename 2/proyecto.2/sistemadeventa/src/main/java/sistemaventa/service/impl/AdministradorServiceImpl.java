package sistemaventa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sistemaventa.model.Administrador;
import sistemaventa.repository.AdministradorRepository;
import sistemaventa.service.AdministradorService;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Override
    public List<Administrador> findAll() {
        return administradorRepository.findAll();
    }

    @Override
    public Optional<Administrador> findById(String id) {
        return administradorRepository.findById(id);
    }

    @Override
    public Administrador save(Administrador admin) {
        return administradorRepository.save(admin);
    }

    @Override
    public void deleteById(String id) {
        administradorRepository.deleteById(id);
    }

    @Override
    public Optional<Administrador> findByUsuarioAndContrasena(String usuario, String contrasena) {
        return administradorRepository.findByUsuarioAndContrasena(usuario, contrasena);
    }
}