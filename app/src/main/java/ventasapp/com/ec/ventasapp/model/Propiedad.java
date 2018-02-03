package ventasapp.com.ec.ventasapp.model;

import java.util.List;

public class Propiedad {

    private int codigo;
    private String direccion;
    private String urlVideo;
    private String descripcion;
    private String estado;
    private String tipo;
    private double costo;
    private double longuitud;
    private double latitud;
    private String codigoCliente;
    private List<String> imagenes;

    public Propiedad(int codigo, String direccion, String urlVideo, String descripcion, String estado, String tipo, double costo, double longuitud, double latitud, String codigoCliente) {
        this.codigo = codigo;
        this.direccion = direccion;
        this.urlVideo = urlVideo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.tipo = tipo;
        this.costo = costo;
        this.longuitud = longuitud;
        this.latitud = latitud;
        this.codigoCliente = codigoCliente;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public double getLonguitud() {
        return longuitud;
    }

    public void setLonguitud(double longuitud) {
        this.longuitud = longuitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }
}
