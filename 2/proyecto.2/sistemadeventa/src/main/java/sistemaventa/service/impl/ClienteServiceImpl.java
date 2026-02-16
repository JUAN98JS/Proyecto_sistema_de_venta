package sistemaventa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sistemaventa.model.Administrador;
import sistemaventa.model.Cliente;
import sistemaventa.repository.AdministradorRepository;
import sistemaventa.repository.ClienteRepository;
import sistemaventa.service.ClienteService;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Override
    public Cliente guardarCliente(Cliente nuevoCliente, String adminId) {
        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        nuevoCliente.setAdministradorId(admin.getId());
        return clienteRepository.save(nuevoCliente);
    }

    @Override
    public Optional<Cliente> obtenerPorId(String id) {
        return clienteRepository.findById(id); // Implementación correcta
    }

    @Override
    public Cliente actualizarCliente(Cliente clienteActualizado, String adminId) {
        Cliente existente = clienteRepository.findByIdentificacion(clienteActualizado.getIdentificacion())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con identificación: " + clienteActualizado.getIdentificacion()));

        existente.setNombre(clienteActualizado.getNombre());
        existente.setApellido(clienteActualizado.getApellido());
        existente.setTelefono(clienteActualizado.getTelefono());
        existente.setEmail(clienteActualizado.getEmail());
        existente.setDireccion(clienteActualizado.getDireccion());

        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        existente.setAdministradorId(admin.getId());

        return clienteRepository.save(existente);
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public void eliminarCliente(Integer identificacion) {
        Cliente cliente = clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        clienteRepository.deleteById(cliente.getId());
    }

    @Override
    public Optional<Cliente> buscarPorIdentificacion(Integer identificacion) {
        return clienteRepository.findByIdentificacion(identificacion);
    }
}