package sistemaventa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import jakarta.validation.constraints.*;

@Document(collection = "CLIENTES")




    public class Cliente {


    @Id
    private String id;


    private Integer identificacion;

    @Field("nombre")
    private String nombre;

    @Field("apellido")
    private String apellido;

    @Field("Telefono")
    private String telefono;

    @Field("Direccion")
    private String direccion;

    @Field("email")
    private String email;

    @Field("Administrador_idAdministrador")
    private String administradorId;

    // Constructor, getters y setters...


    @DBRef
    private Administrador administrador;

    // Constructores
    public Cliente() {}

    public Cliente(Administrador administrador, String administradorId, String apellido, String direccion, String email, String id, Integer identificacion, String nombre, String telefono) {
        this.administrador = administrador;
        this.administradorId = administradorId;
        this.apellido = apellido;
        this.direccion = direccion;
        this.email = email;
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getIdentificacion() { return identificacion; }
    public void setIdentificacion(Integer identificacion) { this.identificacion = identificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAdministradorId() { return administradorId; }
    public void setAdministradorId(String administradorId) { this.administradorId = administradorId; }
}