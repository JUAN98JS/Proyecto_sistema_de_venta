package sistemaventa.service.impl;

import org.springframework.stereotype.Service;
import sistemaventa.model.Producto;
import sistemaventa.model.ReporteInventarioDTO;
import sistemaventa.repository.ProductoRepository;
import sistemaventa.service.ProductoService;

import java.util.*;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerProductoPorCodigo(Integer codigo) {
        return productoRepository.findByCodigoProducto(codigo).orElse(null);
    }

    @Override
    public Producto actualizarProducto(Producto productoActualizado) {
        Producto existente = productoRepository.findByCodigoProducto(productoActualizado.getCodigoProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con código: " + productoActualizado.getCodigoProducto()));

        existente.setNombre(productoActualizado.getNombre());
        existente.setTipo(productoActualizado.getTipo());
        existente.setModelo(productoActualizado.getModelo());
        existente.setPrecio(productoActualizado.getPrecio());
        existente.setCantidadStock(productoActualizado.getCantidadStock());
        existente.setStockMinimo(productoActualizado.getStockMinimo());
        existente.setStockMaximo(productoActualizado.getStockMaximo());
        existente.setImagen(productoActualizado.getImagen());

        return productoRepository.save(existente);
    }

    @Override
    public void eliminarProducto(Integer codigo) {
        Producto producto = productoRepository.findByCodigoProducto(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productoRepository.deleteById(producto.getId());
    }

    @Override
    public ReporteInventarioDTO generarReporteInventario() {
        List<Producto> productos = obtenerTodosLosProductos();

        long bajoStock = 0;
        long stockAlto = 0;
        double valorTotal = 0;
        Map<String, Long> byTipo = new LinkedHashMap<>();

        for (Producto p : productos) {
            valorTotal += p.getPrecio() * p.getCantidadStock();

            if (p.getCantidadStock() < p.getStockMinimo()) {
                bajoStock++;
            } else if (p.getCantidadStock() > p.getStockMaximo()) {
                stockAlto++;
            }

            String tipo = p.getTipo() != null && !p.getTipo().isBlank() ? p.getTipo() : "Sin Clasificar";
            byTipo.merge(tipo, 1L, Long::sum);
        }

        long totalProductos = productos.size();
        long stockNormal = totalProductos - bajoStock - stockAlto;

        List<String> typeLabels = new ArrayList<>(byTipo.keySet());
        List<Long> typeData = new ArrayList<>(byTipo.values());

        List<String> stateLabels = List.of("Bajo", "Normal", "Alto");
        List<Long> stateData = List.of(bajoStock, stockNormal, stockAlto);

        return new ReporteInventarioDTO(
                productos,
                totalProductos,
                bajoStock,
                stockNormal,
                stockAlto,
                valorTotal,
                byTipo.size(),
                typeLabels,
                typeData,
                stateLabels,
                stateData
        );
    }
}