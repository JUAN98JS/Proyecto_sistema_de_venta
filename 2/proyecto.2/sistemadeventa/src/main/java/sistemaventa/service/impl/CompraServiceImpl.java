package sistemaventa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistemaventa.model.Compra;
import sistemaventa.model.Producto;
import sistemaventa.repository.CompraRepository;
import sistemaventa.service.CompraService;
import sistemaventa.service.ProductoService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private MongoTemplate mongoTemplate; // Para consultas más complejas

    @Override
    @Transactional
    public Compra registrarCompra(Compra compra, Producto producto, int cantidadComprada) {
        // Actualizar stock del producto
        int nuevoStock = producto.getCantidadStock() + cantidadComprada;
        producto.setCantidadStock(nuevoStock);
        productoService.guardarProducto(producto); // Guarda el producto actualizado

        // Desnormalizar datos del producto en la compra
        compra.setProductoId(producto.getCodigoProducto());
        compra.setProductoNombre(producto.getNombre());
        compra.setProductoTipo(producto.getTipo());

        // Si el proveedor tiene datos adicionales, podríamos desnormalizarlos aquí
        // (pero asumimos que ya vienen en el objeto compra desde el controlador,
        // donde se obtuvieron del servicio de proveedor)

        // Guardar la compra
        return compraRepository.save(compra);
    }

    @Override
    public Optional<Compra> obtenerPorId(String id) {
        return compraRepository.findById(id);
    }

    @Override
    public List<Compra> obtenerTodas() {
        return compraRepository.findAll();
    }

    @Override
    public List<Compra> obtenerPorProductoId(String productoId) {
        return compraRepository.findByProductoId(productoId);
    }

    @Override
    public List<Compra> obtenerPorProveedorId(String proveedorId) {
        return compraRepository.findByProveedorId(proveedorId);
    }

    @Override
    public List<Compra> obtenerPorRangoFechas(Date desde, Date hasta) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fecha").gte(desde).lte(hasta));
        return mongoTemplate.find(query, Compra.class);
    }

    @Override
    public void eliminarCompra(String id) {
        compraRepository.deleteById(id);
    }
}