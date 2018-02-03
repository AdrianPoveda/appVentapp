package ventasapp.com.ec.ventasapp.model;

public class Imagen {
    private int id;
    private String nombreImagen;

    public Imagen(int id, String nombreImagen) {
        this.id = id;
        this.nombreImagen = nombreImagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }
}
