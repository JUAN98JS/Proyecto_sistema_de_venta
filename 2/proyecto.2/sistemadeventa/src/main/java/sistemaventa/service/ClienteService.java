package sistemaventa.service;

import sistemaventa.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    Cliente guardarCliente(Cliente nuevoCliente, String adminId);

    Optional<Cliente> obtenerPorId(String id);
    Optional<Cliente> buscarPorIdentificacion(Integer identificacion) ;




    Cliente actualizarCliente(Cliente clienteActualizado, String adminId);
    List<Cliente> listarClientes();
    void eliminarCliente(Integer identificacion);


}