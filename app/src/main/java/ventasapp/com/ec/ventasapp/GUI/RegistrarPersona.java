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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ventasapp.com.ec.ventasapp.R;
import ventasapp.com.ec.ventasapp.utilidades.ComponentsAnimation;
import ventasapp.com.ec.ventasapp.utilidades.DisplayUtils;
import ventasapp.com.ec.ventasapp.utilidades.ResizeImage;

public class RegistrarPersona extends AppCompatActivity {

    private ImageView imgNombre;
    private ImageView imgApellido;
    private ImageView imgfechana;
    private ImageView imgDireccion;
    private ImageView imgEmail;
    private ImageView imgPassword;
    private ImageView imgTelefono;

    private EditText etxtNombre;
    private EditText etxtApellido;
    private EditText etxtfechana;
    private EditText etxtDireccion;
    private EditText etxtEmail;
    private EditText etxtPassword;
    private EditText etxtTelefono;


    private ImageView imgRegistrat;
    private ImageView imgReturn;

    private String URL_SET_PERSON = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registrar_persona);
        final ComponentsAnimation animation = new ComponentsAnimation();
        createComponents();

        imgRegistrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animation.componentAnimation(getApplicationContext(),imgRegistrat);
                if(etxtApellido.getText().toString().equals("") || etxtDireccion.getText().toString().equals("") || etxtEmail.getText().toString().equals("") ||
                        etxtfechana.getText().toString().equals("") || etxtNombre.getText().toString().equals("") || etxtPassword.getText().toString().equals("") ||
                        etxtTelefono.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(),"Datos Incompletos",Toast.LENGTH_SHORT).show();
                }else{

                    URL_SET_PERSON="http://54.91.89.143:8080/ventapp/ventapp/user/registroPropiedad?" +
                            "apellido="+etxtApellido.getText().toString().replace(" ","%20") +
                            "&direccion="+etxtDireccion.getText().toString().replace(" ","%20") +
                            "&email="+etxtEmail.getText().toString().replace(" ","%20") +
                            "&fecha="+etxtfechana.getText().toString().replace(" ","%20") +
                            "&nombres="+etxtNombre.getText().toString().replace(" ","%20") +
                            "&clave="+etxtPassword.getText().toString().replace(" ","%20") +
                            "&telefono="+etxtTelefono.getText().toString().replace(" ","%20");


                    Log.i("nuevo",URL_SET_PERSON);

                    new RegistrarPersona.JSONAddPerson().execute(URL_SET_PERSON);

                }
            }
        });


        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation.componentAnimation(getApplicationContext(),imgReturn);
                startActivity(new Intent(getApplicationContext(),Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });
    }


    public void createComponents() {

        imgNombre = new ImageView(this);
        imgApellido = new ImageView(this);
        imgfechana = new ImageView(this);
        imgDireccion = new ImageView(this);
        imgEmail = new ImageView(this);
        imgPassword = new ImageView(this);
        imgTelefono= new ImageView(this);

        etxtTelefono = new EditText(this);
        etxtPassword = new EditText(this);
        etxtEmail = new EditText(this);
        etxtDireccion = new EditText(this);
        etxtfechana = new EditText(this);
        etxtApellido = new EditText(this);
        etxtNombre = new EditText(this);

        imgRegistrat = new ImageView(this);
        imgReturn = new ImageView(this);


        DisplayUtils displayUtils = new DisplayUtils();
        ResizeImage resizeImage = new ResizeImage();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.registrar);


        int height = displayUtils.getHeight(getWindowManager());
        int width = displayUtils.getWidth(getWindowManager());


        int indetifier = getResources().getIdentifier("personanombre","drawable",getPackageName());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100*width,8*height);
        layoutParams.setMargins(10*width,10*height,10*width,0);
        imgNombre.setLayoutParams(layoutParams);
        relativeLayout.addView(imgNombre);
        Bitmap bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,60*width,8*height);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        imgNombre.setBackgroundDrawable(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(58*width,7*height);
        etxtNombre.setSingleLine();
        etxtNombre.setHint("Nombre");
        etxtNombre.setTextColor(Color.BLACK);
        etxtNombre.setGravity(Gravity.CENTER);
        etxtNombre.setPadding(40,0,40,0);
        etxtNombre.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(28*width,11*height,0,0);
        etxtNombre.setLayoutParams(layoutParams);
        relativeLayout.addView(etxtNombre);

        indetifier = getResources().getIdentifier("personanombre","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(100*width,8*height);
        layoutParams.setMargins(10*width,20*height,10*width,0);
        imgApellido.setLayoutParams(layoutParams);
        relativeLayout.addView(imgApellido);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,60*width,8*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgApellido.setBackgroundDrawable(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(58*width,7*height);
        etxtApellido.setSingleLine();
        etxtApellido.setHint("Apellido");
        etxtApellido.setTextColor(Color.BLACK);
        etxtApellido.setGravity(Gravity.CENTER);
        etxtApellido.setPadding(40,0,40,0);
        etxtApellido.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(28*width,21*height,0,0);
        etxtApellido.setLayoutParams(layoutParams);
        relativeLayout.addView(etxtApellido);


        indetifier = getResources().getIdentifier("fechanacimiento","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(100*width,8*height);
        layoutParams.setMargins(10*width,30*height,10*width,0);
        imgfechana.setLayoutParams(layoutParams);
        relativeLayout.addView(imgfechana);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,60*width,8*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgfechana.setBackgroundDrawable(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(58*width,7*height);
        etxtfechana.setSingleLine();
        etxtfechana.setHint("1991/12/10");
        etxtfechana.setTextColor(Color.BLACK);
        etxtfechana.setGravity(Gravity.CENTER);
        etxtfechana.setPadding(40,0,40,0);
        etxtfechana.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(28*width,31*height,0,0);
        etxtfechana.setLayoutParams(layoutParams);
        relativeLayout.addView(etxtfechana);



        indetifier = getResources().getIdentifier("direccion","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(100*width,8*height);
        layoutParams.setMargins(10*width,40*height,10*width,0);
        imgDireccion.setLayoutParams(layoutParams);
        relativeLayout.addView(imgDireccion);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,60*width,8*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgDireccion.setBackgroundDrawable(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(58*width,7*height);
        etxtDireccion.setSingleLine();
        etxtDireccion.setHint("Dirección");
        etxtDireccion.setTextColor(Color.BLACK);
        etxtDireccion.setGravity(Gravity.CENTER);
        etxtDireccion.setPadding(40,0,40,0);
        etxtDireccion.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(28*width,41*height,0,0);
        etxtDireccion.setLayoutParams(layoutParams);
        relativeLayout.addView(etxtDireccion);


        indetifier = getResources().getIdentifier("emailre","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(100*width,8*height);
        layoutParams.setMargins(10*width,50*height,10*width,0);
        imgEmail.setLayoutParams(layoutParams);
        relativeLayout.addView(imgEmail);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,60*width,8*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgEmail.setBackgroundDrawable(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(58*width,7*height);
        etxtEmail.setSingleLine();
        etxtEmail.setHint("Email");
        etxtEmail.setTextColor(Color.BLACK);
        etxtEmail.setGravity(Gravity.CENTER);
        etxtEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etxtEmail.setPadding(40,0,40,0);
        etxtEmail.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(28*width,51*height,0,0);
        etxtEmail.setLayoutParams(layoutParams);
        relativeLayout.addView(etxtEmail);

        indetifier = getResources().getIdentifier("imgpassword","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(100*width,8*height);
        layoutParams.setMargins(10*width,60*height,10*width,0);
        imgPassword.setLayoutParams(layoutParams);
        relativeLayout.addView(imgPassword);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,60*width,8*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgPassword.setBackgroundDrawable(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(58*width,7*height);
        etxtPassword.setSingleLine();
        etxtPassword.setHint("Contraseña");
        etxtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etxtPassword.setTextColor(Color.BLACK);
        etxtPassword.setGravity(Gravity.CENTER);
        etxtPassword.setPadding(40,0,40,0);
        etxtPassword.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(28*width,61*height,0,0);
        etxtPassword.setLayoutParams(layoutParams);
        relativeLayout.addView(etxtPassword);

        indetifier = getResources().getIdentifier("telefono","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(100*width,8*height);
        layoutParams.setMargins(10*width,70*height,10*width,0);
        imgTelefono.setLayoutParams(layoutParams);
        relativeLayout.addView(imgTelefono);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,60*width,8*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgTelefono.setBackgroundDrawable(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(58*width,7*height);
        etxtTelefono.setSingleLine();
        etxtTelefono.setHint("Teléfono");
        etxtTelefono.setTextColor(Color.BLACK);
        etxtTelefono.setInputType(InputType.TYPE_CLASS_NUMBER);
        etxtTelefono.setGravity(Gravity.CENTER);
        etxtTelefono.setPadding(40,0,40,0);
        etxtTelefono.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(28*width,71*height,0,0);
        etxtTelefono.setLayoutParams(layoutParams);
        relativeLayout.addView(etxtTelefono);


        indetifier = getResources().getIdentifier("iconoregistro","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(17*width,10*height);
        layoutParams.setMargins(15*width,82*height,0,0);
        imgRegistrat.setLayoutParams(layoutParams);
        relativeLayout.addView(imgRegistrat);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,17*width,10*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgRegistrat.setBackgroundDrawable(bitmapDrawable);

        indetifier = getResources().getIdentifier("replay","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(17*width,10*height);
        layoutParams.setMargins(72*width,82*height,0,0);
        imgReturn.setLayoutParams(layoutParams);
        relativeLayout.addView(imgReturn);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,17*width,10*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgReturn.setBackgroundDrawable(bitmapDrawable);






    }


    public class JSONAddPerson extends AsyncTask<String,String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
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


                return finalJson;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (java.net.SocketTimeoutException e){

            }
            catch (IOException e) {
                e.printStackTrace();
            }


            finally {

                if(connection != null) {
                    connection.disconnect();

                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);


            //POner si el valor es null el servidor esta desactivado

            try {

                Log.i("nuevo",result);

                if (result.equals("true")){
                    Toast.makeText(getApplicationContext(),"Se a ingresado con exito una persona",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

                }

                if(result.equals("false")){
                    Toast.makeText(getApplicationContext(),"Servidor Desconectado",Toast.LENGTH_SHORT).show();

                }

            }catch (Exception e){


                Toast.makeText(getApplicationContext(),"Servidor Desconectado",Toast.LENGTH_SHORT).show();
            }


        }
    }


}
