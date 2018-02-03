package ventasapp.com.ec.ventasapp.model;


public class Cometario {

    private int id;
    private String comentario;


    public Cometario(int id, String comentario) {
        this.id = id;
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
