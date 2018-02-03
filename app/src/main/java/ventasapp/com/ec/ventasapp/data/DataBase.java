package ventasapp.com.ec.ventasapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ventasapp.com.ec.ventasapp.modelos.PersonaBase;

/**
 * Created by root on 24/01/18.
 */

public class DataBase extends SQLiteOpenHelper {

    private static final int BD_VERSION = 1;
    private static final String DBNAME = "database.db";

    public static final String user_table = "user_table";


    //Tabla Usuario
    public static final String id_table = "id_table";
    public static final String id_user = "id_user";
    public static final String nombre_user = "nombre_user";
    public static final String apellido_user = "apellido_user";
    public static final String fecha_user = "fecha_user";
    public static final String direccion_user = "direccion_user";
    public static final String email_user = "email_user";
    public static final String password_user = "password_user";

    public static final String CREATE_TABLE_USER = "create table " + user_table + "("
            + id_table + " text primary key,"
            + id_user + " text not null,"
            + nombre_user + " text not null,"
            + apellido_user + " text not null,"
            + fecha_user + " text not null,"
            + direccion_user + " text not null,"
            + email_user + " text not null,"
            + password_user + " text not null)";







    public DataBase(Context context) {
        super(context, DBNAME, null, BD_VERSION);
    }

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS '"+CREATE_TABLE_USER +"'");
        onCreate(db);

    }


    private String codigo;
    private String codigoUser;
    private String nombres;
    private String apellido;
    private String fecha;
    private String direccion;
    private String email;
    private String password;

    public void insertUser(String id, String codigo, String nombres, String apellido, String fecha,String direccion,String email,String password) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put(id_table, id);
            valores.put(id_user, codigo);
            valores.put(nombre_user, nombres);
            valores.put(apellido_user, apellido);
            valores.put(fecha_user,fecha);
            valores.put(direccion_user, direccion);
            valores.put(email_user, email);
            valores.put(password_user, password);
            db.insert(user_table, null, valores);
        }
        db.close();
    }


    public void updateUser(String id, String codigo, String nombres, String apellido, String fecha,String direccion,String email,String password) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put(id_table, id);
            valores.put(id_user, codigo);
            valores.put(nombre_user, nombres);
            valores.put(apellido_user, apellido);
            valores.put(fecha_user,fecha);
            valores.put(direccion_user, direccion);
            valores.put(email_user, email);
            valores.put(password_user, password);
            db.update(user_table, valores,id_table+"="+id,null);
        }
        db.close();
    }

    public PersonaBase getUser(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] registry = {id_table,id_user,nombre_user,apellido_user,fecha_user,direccion_user,email_user,password_user};
        Cursor c = db.query(user_table, registry, id_table+"=" + id,
                null, null, null, null, null);

        if(c != null) {
            c.moveToFirst();
        }
        PersonaBase question = new PersonaBase(c.getString(0),c.getString(1),c.getString(2), c.getString(3),c.getString(4),c.getString(5),c.getString(6),
                c.getString(7));
        db.close();
        c.close();
        return question;
    }

    public int getNumberOrder(){
        int records;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from user_table", null);
        records= c.getCount();
        return records;
    }

}
