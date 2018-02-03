package ventasapp.com.ec.ventasapp.GUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ventasapp.com.ec.ventasapp.R;
import ventasapp.com.ec.ventasapp.Servicios.ServicioNotificacion;
import ventasapp.com.ec.ventasapp.data.DataBase;
import ventasapp.com.ec.ventasapp.data.ManagementNotificacion;
import ventasapp.com.ec.ventasapp.model.Notificacion;
import ventasapp.com.ec.ventasapp.modelos.Persona;
import ventasapp.com.ec.ventasapp.utilidades.ComponentsAnimation;
import ventasapp.com.ec.ventasapp.utilidades.DisplayUtils;
import ventasapp.com.ec.ventasapp.utilidades.Hostname;
import ventasapp.com.ec.ventasapp.utilidades.ResizeImage;

public class Login extends AppCompatActivity {

    private ImageView imgLogo;
    private ImageView imgUsuario;
    private ImageView imgContrasena;
    private ImageView imgLogin;
    private ImageView imgRegistre;
    private EditText etxtUsuario;
    private EditText etxtContrasena;
    private String URL_LOGIN = "";
    public static Persona persona;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        final ComponentsAnimation animation = new ComponentsAnimation();
        FirebaseApp.initializeApp(this);

        DataBase dataBase = new DataBase(getApplicationContext());

        if(dataBase.getNumberOrder()==0){
            dataBase.insertUser("1","","","","","","","");
        }

        if(dataBase.getUser("1").getCodigoUser().equals("")){

            createComponents();

            imgLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    animation.componentAnimation(getApplicationContext(),imgLogin);


                    if(etxtContrasena.getText().toString().equals("") || etxtUsuario.getText().toString().equals("")){

                        Toast.makeText(getApplicationContext(),"Datos Incompletos",Toast.LENGTH_SHORT).show();
                    }else{

                        Hostname hostname = new Hostname();
                        URL_LOGIN = hostname.getHost()+"ventapp/ventapp/authentication/query?email="+etxtUsuario.getText().toString()+
                                "&pass="+etxtContrasena.getText().toString();

                        Log.i("nuevo",URL_LOGIN);
                        new GetUsuario().execute(URL_LOGIN);

                    }
                }
            });

            imgRegistre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    animation.componentAnimation(getApplicationContext(),imgRegistre);

                    Intent i = new Intent(getApplicationContext(), RegistrarPersona.class);
                    startActivity(i);


                }
            });

            dataBase.close();
        }else{
            startActivity(new Intent(getApplicationContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

        }





        //j.setVisibility(View.INVISIBLE);

    }

    public void createComponents() {

        imgLogo = new ImageView(this);
        imgUsuario = new ImageView(this);
        imgContrasena = new ImageView(this);
        imgLogin = new ImageView(this);
        imgRegistre = new ImageView(this);
        etxtContrasena = new EditText(this);
        etxtUsuario = new EditText(this);
        etxtContrasena = new EditText(this);

        DisplayUtils displayUtils = new DisplayUtils();
        ResizeImage resizeImage = new ResizeImage();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.imgLogin);


        int height = displayUtils.getHeight(getWindowManager());
        int width = displayUtils.getWidth(getWindowManager());


        int indetifier = getResources().getIdentifier("logo1","drawable",getPackageName());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100*width,30*height);
        layoutParams.setMargins(25*width,8*height,25*width,0);
        imgLogo.setLayoutParams(layoutParams);
        relativeLayout.addView(imgLogo);
        Bitmap bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,90*width,30*height);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        imgLogo.setBackgroundDrawable(bitmapDrawable);


        indetifier = getResources().getIdentifier("editextusuario","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(100*width,8*height);
        layoutParams.setMargins(10*width,45*height,10*width,0);
        imgUsuario.setLayoutParams(layoutParams);
        relativeLayout.addView(imgUsuario);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,60*width,8*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgUsuario.setBackgroundDrawable(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(58*width,7*height);
        etxtUsuario.setSingleLine();
        etxtUsuario.setHint("Usuario");
        etxtUsuario.setTextColor(Color.BLACK);
        etxtUsuario.setGravity(Gravity.CENTER);
        etxtUsuario.setPadding(40,0,40,0);
        etxtUsuario.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(28*width,46*height,0,0);
        etxtUsuario.setLayoutParams(layoutParams);
        relativeLayout.addView(etxtUsuario);

        indetifier = getResources().getIdentifier("imgextcontrasena","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(100*width,8*height);
        layoutParams.setMargins(10*width,60*height,10*width,0);
        imgContrasena.setLayoutParams(layoutParams);
        relativeLayout.addView(imgContrasena);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,60*width,8*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgContrasena.setBackgroundDrawable(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(58*width,7*height);
        etxtContrasena.setSingleLine();
        etxtContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etxtContrasena.setHint("Contraseña");
        etxtContrasena.setTextColor(Color.BLACK);
        etxtContrasena.setGravity(Gravity.CENTER);
        etxtContrasena.setPadding(40,0,40,0);
        etxtContrasena.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(28*width,61*height,0,0);
        etxtContrasena.setLayoutParams(layoutParams);
        relativeLayout.addView(etxtContrasena);


        indetifier = getResources().getIdentifier("btninicionuevo","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(21*width,12*height);
        layoutParams.setMargins(15*width,78*height,10*width,0);
        imgLogin.setLayoutParams(layoutParams);
        relativeLayout.addView(imgLogin);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,21*width,12*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgLogin.setBackgroundDrawable(bitmapDrawable);

        indetifier = getResources().getIdentifier("iconoregistro","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(21*width,12*height);
        layoutParams.setMargins(68*width,78*height,10*width,0);
        imgRegistre.setLayoutParams(layoutParams);
        relativeLayout.addView(imgRegistre);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,21*width,12*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgRegistre.setBackgroundDrawable(bitmapDrawable);


    }


    public class GetUsuario extends AsyncTask<String,String,Boolean> {



        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                JSONObject object = new JSONObject(finalJson);
                persona = new Persona();
                persona.setCodigo(object.getInt("codigo"));
                persona.setNombres(object.getString("nombres").toString());
                persona.setApellido(object.getString("apellido").toString());
                persona.setDireccion(object.getString("direccion").toString());
                persona.setFecha(object.getString("fecha").toString());
                persona.setEmail(object.getString("email").toString());
                persona.setPassword(object.getString("password").toString());





            }catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            return true;

        }


        @Override
        protected void onPostExecute(final Boolean succes){


            Log.i("nuevo", String.valueOf(succes));
            if(succes){

                Log.i("nuevo", String.valueOf(persona));


                DataBase dataBase = new DataBase(getApplicationContext());

                dataBase.updateUser("1",String.valueOf(persona.getCodigo()),persona.getNombres(),persona.getApellido(),persona.getFecha(),persona.getDireccion(),persona.getEmail(),persona.getPassword());

                ManagementNotificacion managementNotificacion = new ManagementNotificacion();
                Notificacion notificacion = new Notificacion();
                notificacion.setId(dataBase.getUser("1").getCodigoUser()); //ide del propietario
                notificacion.setNotifi("notificacion");
                notificacion.setDescripcion("notificacion");
                managementNotificacion.insertar(notificacion);

                startService(new Intent(getBaseContext(), ServicioNotificacion.class));
                dataBase.close();

                startActivity(new Intent(getApplicationContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));


                //Intent i = new Intent(getApplicationContext(), FiltradoPropiedad.class);
                //startActivity(i);

            }else{

                Toast.makeText(getApplicationContext(),"Servidor Desconectado",Toast.LENGTH_SHORT).show();
            }

        }
    }


}
