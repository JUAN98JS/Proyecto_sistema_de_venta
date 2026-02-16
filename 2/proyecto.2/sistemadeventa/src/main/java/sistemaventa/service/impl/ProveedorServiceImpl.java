package sistemaventa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sistemaventa.model.Proveedor;
import sistemaventa.repository.ProveedorRepository;
import sistemaventa.service.ProveedorService;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public Proveedor guardarProveedor(Proveedor proveedor) {
        // Si es un proveedor nuevo y no tiene código, podrías autogenerarlo
        // Pero aquí asumimos que el código viene del formulario
        return proveedorRepository.save(proveedor);
    }

    @Override
    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    @Override
    public Optional<Proveedor> obtenerPorId(String id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public Optional<Proveedor> obtenerPorCodigo(Integer codigoProveedor) {
        return proveedorRepository.findByCodigoProveedor(codigoProveedor);
    }

    @Override
    public void eliminarProveedor(String id) {
        proveedorRepository.deleteById(id);
    }

    @Override
    public void eliminarPorCodigo(Integer codigoProveedor) {
        proveedorRepository.findByCodigoProveedor(codigoProveedor)
                .ifPresent(proveedor -> proveedorRepository.deleteById(proveedor.getId()));
    }
}