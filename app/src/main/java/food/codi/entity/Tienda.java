package food.codi.entity;

/**
 * By: El Bryant
 */

public class Tienda {
    private long id_tienda;
    private String nombre;
    private String imagen;

    public Tienda(long id_tienda, String nombre, String imagen) {
        this.id_tienda = id_tienda;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public long getId_tienda() {
        return id_tienda;
    }

    public void setId_tienda(long id_tienda) {
        this.id_tienda = id_tienda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
