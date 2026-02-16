package sistemaventa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Productos")
public class Producto {



        @Id
        private String id;  // El _id de MongoDB se mapea automáticamente

        @Field("Codigo_producto")
        private int codigoProducto;

        @Field("Nombre")
        private String nombre;

        @Field("Tipo")
        private String tipo;

        @Field("Modelo")
        private String modelo;

        @Field("Precio")
        private double precio;  // o BigDecimal si prefieres

        @Field("Stock")
        private int stock;

        @Field("Stock_Mínimo")
        private int stockMinimo;

        @Field("Stock_Máximo")
        private int stockMaximo;

        @Field("Imagen")
        private byte[] imagen;
    // Constructores
    public Producto() {}

    public Producto( Integer codigoProducto ,String nombre, String tipo, String modelo, double precio, int Stock, int stockMinimo, int stockMaximo, byte[] imagen) {
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = Stock;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
        this.imagen = imagen;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getCantidadStock() { return stock; }
    public void setCantidadStock(int cantidadStock) { this.stock = cantidadStock; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public int getStockMaximo() { return stockMaximo; }
    public void setStockMaximo(int stockMaximo) { this.stockMaximo = stockMaximo; }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Integer codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public byte[] getImagen() { return imagen; }
    public void setImagen(byte[] imagen) { this.imagen = imagen; }
}