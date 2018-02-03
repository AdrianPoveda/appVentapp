package ventasapp.com.ec.ventasapp.model;

import android.graphics.Bitmap;


public class ItemPropiedad {

    private int id;
    private Bitmap imgPropiedad;
    private String precio;
    private String descripcion;

    public ItemPropiedad(Bitmap imgPropiedad, String precio, String descripcion) {
        this.id = id;
        this.imgPropiedad = imgPropiedad;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Bitmap getImgPropiedad() {
        return imgPropiedad;
    }

    public void setImgPropiedad(Bitmap imgPropiedad) {
        this.imgPropiedad = imgPropiedad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
