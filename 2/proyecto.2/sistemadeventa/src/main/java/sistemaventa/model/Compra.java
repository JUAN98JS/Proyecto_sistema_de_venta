
package sistemaventa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "compras")
public class Compra {

    @Id
    private String id;                     // ObjectId de MongoDB

    private Date fecha;
    private Integer cantidadComprada;
    private Double precioUnitarioCompra;

    // Referencias (IDs)
    private int productoId;              // ID del producto en MongoDB
    private int proveedorId;              // ID del proveedor
    private String administradorId;          // ID del administrador

    // Campos desnormalizados para evitar joins
    private String productoNombre;
    private String productoTipo;              // opcional
    private String proveedorNombre;
    private String proveedorTelefono;         // opcional

    // Constructores

    public Compra() {
    }

    public Compra(String administradorId, Integer cantidadComprada, Date fecha, String id, Double precioUnitarioCompra, int productoId, String productoNombre, String productoTipo, int proveedorId, String proveedorNombre, String proveedorTelefono) {
        this.administradorId = administradorId;
        this.cantidadComprada = cantidadComprada;
        this.fecha = fecha;
        this.id = id;
        this.precioUnitarioCompra = precioUnitarioCompra;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.productoTipo = productoTipo;
        this.proveedorId = proveedorId;
        this.proveedorNombre = proveedorNombre;
        this.proveedorTelefono = proveedorTelefono;
    }

    public String getAdministradorId() {
        return administradorId;
    }

    public void setAdministradorId(String administradorId) {
        this.administradorId = administradorId;
    }

    public Integer getCantidadComprada() {
        return cantidadComprada;
    }

    public void setCantidadComprada(Integer cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrecioUnitarioCompra() {
        return precioUnitarioCompra;
    }

    public void setPrecioUnitarioCompra(Double precioUnitarioCompra) {
        this.precioUnitarioCompra = precioUnitarioCompra;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getProductoTipo() {
        return productoTipo;
    }

    public void setProductoTipo(String productoTipo) {
        this.productoTipo = productoTipo;
    }

    public int getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(int proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public String getProveedorTelefono() {
        return proveedorTelefono;
    }

    public void setProveedorTelefono(String proveedorTelefono) {
        this.proveedorTelefono = proveedorTelefono;
    }
}