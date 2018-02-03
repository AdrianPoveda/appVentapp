package ventasapp.com.ec.ventasapp.modelos;


public class PersonaBase {

    private String codigo;
    private String codigoUser;
    private String nombres;
    private String apellido;
    private String fecha;
    private String direccion;
    private String email;
    private String password;

    public PersonaBase(String codigo, String codigoUser, String nombres, String apellido, String fecha, String direccion, String email, String password) {
        this.codigo = codigo;
        this.codigoUser = codigoUser;
        this.nombres = nombres;
        this.apellido = apellido;
        this.fecha = fecha;
        this.direccion = direccion;
        this.email = email;
        this.password = password;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoUser() {
        return codigoUser;
    }

    public void setCodigoUser(String codigoUser) {
        this.codigoUser = codigoUser;
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
}
