package sistemaventa.model;

public class DetalleVentaEmbedded {
    private String productoId;
    private  int codigoProducto ;// referencia al producto
    private String productoNombre;   // copia del nombre (histórico)
    private double precioUnitario;   // precio en el momento de la venta
    private int cantidad;

    public String getProductoNombre() {
        return productoNombre;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}