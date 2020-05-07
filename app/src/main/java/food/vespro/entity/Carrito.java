package food.vespro.entity;

public class Carrito {
    private String id_detalle_pedido;
    private String id_producto;
    private String nombre;
    private Double precio;
    private String proveedor;
    private String imagen;

    public Carrito(String id_detalle_pedido, String id_producto, String nombre, Double precio, String proveedor, String imagen) {
        this.id_detalle_pedido = id_detalle_pedido;
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.precio = precio;
        this.proveedor = proveedor;
        this.imagen = imagen;
    }

    public String getId_detalle_pedido() {
        return id_detalle_pedido;
    }

    public void setId_detalle_pedido(String id_detalle_pedido) {
        this.id_detalle_pedido = id_detalle_pedido;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
