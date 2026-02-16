package sistemaventa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "proveedores")
public class Proveedor {

    @Id
    private String id;                     // ObjectId de MongoDB

    @Indexed(unique = true)
    private int codigoProveedor;        // Código numérico visible para el usuario

    private String nombre;
    private String telefono;
    private String direccion;

    // Constructores
    public Proveedor() {}

    public Proveedor(int codigoProveedor, String nombre, String telefono, String direccion) {
        this.codigoProveedor = codigoProveedor;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}