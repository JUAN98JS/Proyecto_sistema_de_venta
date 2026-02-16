package sistemaventa.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import sistemaventa.model.Administrador;
import sistemaventa.model.Cliente;
import sistemaventa.repository.ClienteRepository;
import sistemaventa.service.ClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("")
    public String inicioModuloClientes(HttpSession session, Model model) {
        Administrador admin = (Administrador) session.getAttribute("adminLogueado");
        if (admin == null) return "redirect:/admin/login";
        model.addAttribute("adminUsuario", admin.getUsuario());
        return "Menu_clientes";
    }

    @GetMapping("/agregar")
    public String mostrarFormularioAgregar(HttpSession session, Model model) {
        Administrador admin = (Administrador) session.getAttribute("adminLogueado");
        if (admin == null) return "redirect:/admin/login";
        model.addAttribute("adminUsuario", admin.getUsuario());
        model.addAttribute("cliente", new Cliente());
        return "Agregar_cliente";
    }

    @PostMapping("/nuevo")
    public String registrarCliente(@ModelAttribute Cliente cliente, HttpSession session, Model model) {
        Administrador admin = (Administrador) session.getAttribute("adminLogueado");
        if (admin == null) return "redirect:/admin/login";

        try {
            clienteService.guardarCliente(cliente, admin.getId()); // admin.getId() es String
            return "redirect:/clientes/ver";
        } catch (Exception e) {
            model.addAttribute("error", " Error al registrar cliente: " + e.getMessage());
            model.addAttribute("cliente", cliente);
            model.addAttribute("adminUsuario", admin.getUsuario());
            return "Agregar_cliente";
        }
    }

    @GetMapping("/ver")
    public String verClientes(HttpSession session, Model model) {
        Administrador admin = (Administrador) session.getAttribute("adminLogueado");
        if (admin == null) return "redirect:/admin/login";

        List<Cliente> clientes = clienteService.listarClientes();
        model.addAttribute("clientes", clientes);
        model.addAttribute("adminUsuario", admin.getUsuario());
        return "Ver_clientes";
    }

    @GetMapping("/buscar")
    public String buscarCliente(@RequestParam(value = "identificacion", required = false) Integer identificacion,
                                HttpSession session, Model model) {
        Administrador admin = (Administrador) session.getAttribute("adminLogueado");
        if (admin == null) return "redirect:/admin/login";

        if (identificacion == null) return "Editar_cliente";

        try {
            // 1. El servicio devuelve Optional<Cliente>
            Optional<Cliente> optionalCliente = clienteService.buscarPorIdentificacion(identificacion);

            if (!optionalCliente.isPresent()) {
                model.addAttribute("error", "ID incorrecto. El cliente con identificación " + identificacion + " no existe.");
                model.addAttribute("adminUsuario", admin.getUsuario());
                return "Editar_cliente";
            }

            // 2. Extraemos el cliente del Optional
            Cliente cliente = optionalCliente.get();
            model.addAttribute("cliente", cliente);
            model.addAttribute("adminUsuario", admin.getUsuario());
            return "Editar_cliente";

        } catch (Exception e) {
            model.addAttribute("error", "Error al buscar cliente: " + e.getMessage());
            model.addAttribute("adminUsuario", admin.getUsuario());
            return "Editar_cliente";
        }
    }

    @GetMapping("/eliminar")
    public String eliminarCliente(@RequestParam("identificacion") Integer identificacion,
                                  HttpSession session, Model model) {
        Administrador admin = (Administrador) session.getAttribute("adminLogueado");
        if (admin == null) return "redirect:/admin/login";

        try {
            clienteService.eliminarCliente(identificacion);
            return "redirect:/clientes/ver";
        } catch (Exception e) {
            List<Cliente> clientes = clienteService.listarClientes();
            model.addAttribute("clientes", clientes);
            model.addAttribute("adminUsuario", admin.getUsuario());
            model.addAttribute("error", "  Error al eliminar el cliente: " + e.getMessage());
            return "Ver_clientes";
        }
    }


    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/test-clientes")
    @ResponseBody
    public String testClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("Total de clientes: ").append(clientes.size()).append("<br>");
        for (Cliente c : clientes) {
            sb.append("ID: ").append(c.getId())
                    .append(", Nombre: ").append(c.getNombre())
                    .append(", Apellido: ").append(c.getApellido())
                    .append(", Email: ").append(c.getEmail())
                    .append(", Admin ID: ").append(c.getAdministradorId())
                    .append("<br>");
        }
        return sb.toString();
    }

    @PostMapping("/editar")
    public String editarCliente(@ModelAttribute Cliente cliente, HttpSession session, Model model) {
        Administrador admin = (Administrador) session.getAttribute("adminLogueado");
        if (admin == null) return "redirect:/admin/login";

        try {
            clienteService.actualizarCliente(cliente, admin.getId());
            return "redirect:/clientes/ver";
        } catch (Exception e) {
            model.addAttribute("error", " Error al actualizar: " + e.getMessage());
            model.addAttribute("cliente", cliente);
            model.addAttribute("adminUsuario", admin.getUsuario());
            return "Editar_cliente";
        }
    }
}