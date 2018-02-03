package ventasapp.com.ec.ventasapp.model;

import android.graphics.Bitmap;

public class ItemPropiedadBusqueda {

    private Bitmap imgPropiedad;
    private String precio;
    private String descripcion;
    private boolean checkPropiedad;

    public ItemPropiedadBusqueda(Bitmap imgPropiedad, String precio, String descripcion) {
        this.imgPropiedad = imgPropiedad;
        this.precio = precio;
        this.descripcion = descripcion;
        checkPropiedad = false;
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

    public boolean isCheckPropiedad() {
        return checkPropiedad;
    }

    public void setCheckPropiedad(boolean checkPropiedad) {
        this.checkPropiedad = checkPropiedad;
    }
}
