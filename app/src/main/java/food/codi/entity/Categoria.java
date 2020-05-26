package food.codi.entity;

/**
 * By: El Bryant
 */

public class Categoria {
    private long id_categoria;
    private String nombre;
    private String imagen;

    public Categoria(long id_categoria, String nombre, String imagen) {
        this.id_categoria = id_categoria;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public long getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(long id_categoria) {
        this.id_categoria = id_categoria;
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
