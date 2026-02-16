package sistemaventa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Document(collection = "administradores")
public class Administrador {

    @Id
    private String id;

    private int codigoadmin;

    @NotBlank(message = "El usuario no puede estar vacío")
    @Size(max = 45, message = "El usuario no debe superar los 45 caracteres")
    private String usuario;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(max = 45, message = "La contraseña no debe superar los 45 caracteres")
    private String contrasena;

    @Email(message = "Debe ingresar un correo electrónico válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    private String email;

    // Constructores
    public Administrador() {}

    public Administrador(int codigoadmin, String contrasena, String email, String id, String usuario) {
        this.codigoadmin = codigoadmin;
        this.contrasena = contrasena;
        this.email = email;
        this.id = id;
        this.usuario = usuario;
    }

    public int getCodigoadmin() {
        return codigoadmin;
    }

    public void setCodigoadmin(int codigoadmin) {
        this.codigoadmin = codigoadmin;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}