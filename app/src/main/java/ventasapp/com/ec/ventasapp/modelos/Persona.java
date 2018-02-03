package ventasapp.com.ec.ventasapp.modelos;

import java.io.Serializable;
import java.util.Date;

public class Persona implements Serializable{

    private int codigo;
    private String nombres;
    private String apellido;
    private String fecha;
    private String direccion;
    private String email;
    private String password;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "codigo=" + codigo +
                ", nombres='" + nombres + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fecha='" + fecha + '\'' +
                ", direccion='" + direccion + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
