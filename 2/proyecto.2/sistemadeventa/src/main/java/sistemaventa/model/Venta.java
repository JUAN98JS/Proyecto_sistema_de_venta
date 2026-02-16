package sistemaventa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

@Document(collection = "ventas")
public class Venta {
    @Id
    private String id;

    @NotNull
    private Date fecha;

    @NotBlank
    private String metodoPago;

    @Positive
    private double total;
@Field("Cliente_Identificacion")
    private String clienteId;        // referencia al cliente
    private String administradorId;

    // referencia al administrador

    @Transient
    private Double vuelto;            // no se persiste

    private List<DetalleVentaEmbedded> detalles = new ArrayList<>();

    public void agregarDetalle(DetalleVentaEmbedded detalle) {
        detalles.add(detalle);
    }

    public String getAdministradorId() {
        return administradorId;
    }

    public void setAdministradorId(String administradorId) {
        this.administradorId = administradorId;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<DetalleVentaEmbedded> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaEmbedded> detalles) {
        this.detalles = detalles;
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

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Double getVuelto() {
        return vuelto;
    }

    public void setVuelto(Double vuelto) {
        this.vuelto = vuelto;
    }
}