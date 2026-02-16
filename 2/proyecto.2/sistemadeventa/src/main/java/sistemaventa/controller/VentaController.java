package sistemaventa.controller;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpSession;
import sistemaventa.model.Administrador;
import sistemaventa.model.Cliente;
import sistemaventa.model.DetalleVentaEmbedded;  // Nuevo: objeto embebido
import sistemaventa.model.Producto;
import sistemaventa.model.Venta;
import sistemaventa.service.ClienteService;
import sistemaventa.service.ProductoService;
import sistemaventa.service.VentaService;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    private static final double IVA = 0.19;

    private static final Logger logger = LoggerFactory.getLogger(VentaController.class);

    @GetMapping("/nueva")
    public String mostrarFormularioVenta(Model model, HttpSession session,
                                         @RequestParam(value = "mensaje", required = false) String mensaje,
                                         @RequestParam(value = "error", required = false) String error) {
        try {
            Administrador adminLogueado = (Administrador) session.getAttribute("adminLogueado");
            if (adminLogueado == null) {
                return "redirect:/admin/login";
            }
            model.addAttribute("venta", new Venta());
            model.addAttribute("admin", adminLogueado);
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("error", error);

            List<Producto> productos;
            try {
                productos = productoService.obtenerTodosLosProductos();
                if (productos == null) productos = new ArrayList<>();
            } catch (Exception e) {
                logger.error("ERROR al obtener productos: {}", e.getMessage(), e);
                productos = new ArrayList<>();
            }
            model.addAttribute("productos", productos);

            // Para filtrar por tipo, usamos el campo "tipo" que sigue existiendo
            List<String> tipos = productos.stream()
                    .map(Producto::getTipo)
                    .filter(t -> t != null && !t.isEmpty())
                    .distinct()
                    .toList();
            model.addAttribute("tipos", tipos);

            return "registrar_venta";
        } catch (Exception e) {
            logger.error("ERROR GENERAL en mostrarFormularioVenta: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
            return "error";
        }
    }


    @GetMapping("/buscar-cliente")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> buscarCliente(@RequestParam("id") Integer identificacion) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Cliente> optionalCliente = clienteService.buscarPorIdentificacion(identificacion);
            if (optionalCliente.isPresent()) {
                Cliente cliente = optionalCliente.get();
                response.put("success", true);
                response.put("nombre", cliente.getNombre() + " " + (cliente.getApellido() != null ? cliente.getApellido() : ""));
                response.put("telefono", cliente.getTelefono() != null ? cliente.getTelefono() : "N/A");
                response.put("email", cliente.getEmail() != null ? cliente.getEmail() : "N/A");
            } else {
                response.put("success", false);
                response.put("mensaje", "Cliente no encontrado");
            }
        } catch (Exception e) {
            logger.error("ERROR en buscarCliente: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("mensaje", "Error al buscar cliente: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar-producto")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> buscarProducto(@RequestParam("id") Integer codigoProducto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto producto = productoService.obtenerProductoPorCodigo(codigoProducto);
            if (producto != null) {
                double precioConIva = producto.getPrecio() * (1 + IVA);
                response.put("success", true);
                response.put("id", producto.getCodigoProducto());
                response.put("nombre", producto.getNombre());
                response.put("precio", producto.getPrecio());
                response.put("precioConIva", precioConIva);
                response.put("stock", producto.getCantidadStock());
                response.put("tipo", producto.getTipo() != null ? producto.getTipo() : "N/A");
                response.put("modelo", producto.getModelo() != null ? producto.getModelo() : "N/A");
            } else {
                response.put("success", false);
                response.put("mensaje", "Producto no encontrado");
            }
        } catch (Exception e) {
            logger.error("ERROR en buscarProducto: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("mensaje", "Error al buscar producto: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/productos-por-tipo")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> productosPorTipo(@RequestParam("tipo") String tipo) {
        List<Map<String, Object>> productosResponse = new ArrayList<>();
        try {
            List<Producto> productos = productoService.obtenerTodosLosProductos().stream()
                    .filter(p -> tipo.equals(p.getTipo()))
                    .toList();
            for (Producto p : productos) {
                Map<String, Object> prod = new HashMap<>();
                prod.put("id", p.getCodigoProducto());
                prod.put("nombre", p.getNombre());
                prod.put("precio", p.getPrecio());
                prod.put("precioConIva", p.getPrecio() * (1 + IVA));
                prod.put("stock", p.getCantidadStock());
                prod.put("modelo", p.getModelo() != null ? p.getModelo() : "N/A");
                productosResponse.add(prod);
            }
        } catch (Exception e) {
            logger.error("ERROR en productosPorTipo: {}", e.getMessage(), e);
        }
        return ResponseEntity.ok(productosResponse);
    }

    @PostMapping("/guardar")
    public String guardarVenta(
            @RequestParam("clienteId") Integer identificacionCliente,
            @RequestParam("productosIds") String productosIds,
            @RequestParam("cantidades") String cantidades,
            @RequestParam("metodoPago") String metodoPago,
            @RequestParam("totalFinal") double totalFinal,
            @RequestParam(value = "montoRecibido", required = false) Double montoRecibido,
            HttpSession session) {

        try {
            Administrador admin = (Administrador) session.getAttribute("adminLogueado");
            if (admin == null) {
                return "redirect:/admin/login";
            }

            // Buscar cliente por identificación (número)
            Optional<Cliente> optionalCliente = clienteService.buscarPorIdentificacion(identificacionCliente);
            if (!optionalCliente.isPresent()) {
                return "redirect:/ventas/nueva?error=Cliente no encontrado";
            }
            Cliente cliente = optionalCliente.get();

            String[] idsArray = productosIds.split(",");
            String[] cantArray = cantidades.split(",");
            if (idsArray.length != cantArray.length || idsArray.length == 0) {
                return "redirect:/ventas/nueva?error=Datos de productos inválidos";
            }

            // Crear venta
            Venta venta = new Venta();
            venta.setFecha(new Date());
            venta.setMetodoPago(metodoPago);
            venta.setTotal(totalFinal);

            // Guardar la IDENTIFICACIÓN del cliente (no el ObjectId)
            venta.setClienteId(String.valueOf(cliente.getIdentificacion())); // ← CORREGIDO

            venta.setAdministradorId(admin.getId());

            Double vuelto = 0.0;
            if ("Efectivo".equalsIgnoreCase(metodoPago)) {
                if (montoRecibido == null || montoRecibido < totalFinal) {
                    return "redirect:/ventas/nueva?error=Monto inválido o inferior al total";
                }
                vuelto = Math.round((montoRecibido - totalFinal) * 100.0) / 100.0;
            }

            List<DetalleVentaEmbedded> detalles = new ArrayList<>();
            for (int i = 0; i < idsArray.length; i++) {
                int codigoProducto = Integer.parseInt(idsArray[i]);
                int cantidad = Integer.parseInt(cantArray[i]);

                Producto producto = productoService.obtenerProductoPorCodigo(codigoProducto);
                if (producto == null) {
                    return "redirect:/ventas/nueva?error=Producto código " + codigoProducto + " no encontrado";
                }
                if (producto.getCantidadStock() < cantidad) {
                    return "redirect:/ventas/nueva?error=Stock insuficiente para " + producto.getNombre();
                }

                DetalleVentaEmbedded detalle = new DetalleVentaEmbedded();
                detalle.setCodigoProducto(producto.getCodigoProducto());
                detalle.setProductoNombre(producto.getNombre());
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setCantidad(cantidad);
                detalles.add(detalle);

                producto.setCantidadStock(producto.getCantidadStock() - cantidad);
                productoService.guardarProducto(producto);
            }
            venta.setDetalles(detalles);

            ventaService.guardarVenta(venta);

            session.setAttribute("ultimaVentaId", venta.getId());
            session.setAttribute("ultimaVentaVuelto", vuelto);

            return "redirect:/ventas/nueva?mensaje=Venta registrada exitosamente. Total: $" + String.format("%.2f", totalFinal);

        } catch (Exception e) {
            logger.error("ERROR en guardarVenta: {}", e.getMessage(), e);
            return "redirect:/ventas/nueva?error=Error al guardar: " + e.getMessage();
        }
    }
    @GetMapping("/listar-productos")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> listarTodosProductos() {
        List<Map<String, Object>> productosResponse = new ArrayList<>();
        try {
            List<Producto> productos = productoService.obtenerTodosLosProductos();
            if (productos != null) {
                for (Producto p : productos) {
                    Map<String, Object> prod = new HashMap<>();
                    prod.put("id", p.getCodigoProducto());
                    prod.put("nombre", p.getNombre());
                    prod.put("tipo", p.getTipo() != null ? p.getTipo() : "N/A");
                    prod.put("modelo", p.getModelo() != null ? p.getModelo() : "N/A");
                    prod.put("precio", p.getPrecio());
                    prod.put("precioConIva", p.getPrecio() * (1 + IVA));
                    prod.put("stock", p.getCantidadStock());

                    if (p.getImagen() != null && p.getImagen().length > 0) {
                        String base64Image = java.util.Base64.getEncoder().encodeToString(p.getImagen());
                        prod.put("imagen", "data:image/jpeg;base64," + base64Image);
                    } else {
                        prod.put("imagen", null);
                    }
                    productosResponse.add(prod);
                }
            }
        } catch (Exception e) {
            logger.error("ERROR en listarTodosProductos: {}", e.getMessage(), e);
        }
        return ResponseEntity.ok(productosResponse);
    }

    // Generar PDF de la última venta
    @GetMapping("/generar-pdf")
    public ResponseEntity<byte[]> generarPDF(HttpSession session) {
        try {
            String ventaId = (String) session.getAttribute("ultimaVentaId");
            if (ventaId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            Venta venta = ventaService.obtenerPorId(ventaId).orElse(null);
            if (venta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }


            Cliente cliente = clienteService.obtenerPorId(venta.getClienteId()).orElse(null);
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                Document document = new Document();
                PdfWriter.getInstance(document, baos);
                document.open();

                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Paragraph title = new Paragraph("FACTURA DE VENTA", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph(" "));

                document.add(new Paragraph("Factura No: " + venta.getId()));
                document.add(new Paragraph("Fecha: " + venta.getFecha()));
                document.add(new Paragraph("Cliente: " + cliente.getNombre() + " " + (cliente.getApellido() != null ? cliente.getApellido() : "")));
                document.add(new Paragraph("ID Cliente: " + cliente.getIdentificacion())); // Número de identificación
                document.add(new Paragraph("Método de Pago: " + venta.getMetodoPago()));
                document.add(new Paragraph(" "));

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{3, 2, 2, 2, 2});

                addTableHeader(table, "Producto");
                addTableHeader(table, "Cantidad");
                addTableHeader(table, "Precio Unit.");
                addTableHeader(table, "IVA 19%");
                addTableHeader(table, "Subtotal");

                java.text.NumberFormat currencyFmt = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es", "CO"));

                for (DetalleVentaEmbedded detalle : venta.getDetalles()) {
                    double precioUnit = detalle.getPrecioUnitario();
                    double iva = precioUnit * IVA;
                    double subtotalLinea = (precioUnit + iva) * detalle.getCantidad();

                    table.addCell(detalle.getProductoNombre());
                    table.addCell(String.valueOf(detalle.getCantidad()));
                    table.addCell(currencyFmt.format(precioUnit));
                    table.addCell(currencyFmt.format(iva));
                    table.addCell(currencyFmt.format(subtotalLinea));
                }

                document.add(table);
                document.add(new Paragraph(" "));

                Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
                Paragraph total = new Paragraph("TOTAL A PAGAR: " + currencyFmt.format(venta.getTotal()), boldFont);
                total.setAlignment(Element.ALIGN_RIGHT);
                document.add(total);

                Object sessionVuelto = session.getAttribute("ultimaVentaVuelto");
                if (sessionVuelto instanceof Double && "Efectivo".equalsIgnoreCase(venta.getMetodoPago())) {
                    Double vuelto = (Double) sessionVuelto;
                    Paragraph vueltoParrafo = new Paragraph("Vuelto: " + currencyFmt.format(vuelto), boldFont);
                    vueltoParrafo.setAlignment(Element.ALIGN_RIGHT);
                    document.add(vueltoParrafo);
                }

                document.close();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", "Factura_" + venta.getId() + ".pdf");

                return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("ERROR en generarPDF: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private void addTableHeader(PdfPTable table, String headerTitle) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        PdfPCell header = new PdfPCell(new Phrase(headerTitle, headerFont));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setBackgroundColor(new Color(200, 200, 200));
        table.addCell(header);
    }
}