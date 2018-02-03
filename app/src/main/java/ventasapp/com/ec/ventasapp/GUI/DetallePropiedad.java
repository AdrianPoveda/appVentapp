package ventasapp.com.ec.ventasapp.GUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ventasapp.com.ec.ventasapp.R;
import ventasapp.com.ec.ventasapp.data.DataBase;
import ventasapp.com.ec.ventasapp.data.ManagementNotificacion;
import ventasapp.com.ec.ventasapp.model.Cometario;
import ventasapp.com.ec.ventasapp.model.ItemPropiedad;
import ventasapp.com.ec.ventasapp.model.Notificacion;
import ventasapp.com.ec.ventasapp.model.Propiedad;
import ventasapp.com.ec.ventasapp.utilidades.ComponentsAnimation;
import ventasapp.com.ec.ventasapp.utilidades.DisplayUtils;
import ventasapp.com.ec.ventasapp.utilidades.Hostname;
import ventasapp.com.ec.ventasapp.utilidades.ResizeImage;

public class DetallePropiedad extends AppCompatActivity {

    private ImageView imgPropiedad;
    private TextView txtdireccion;
    private TextView txturlVideo;
    private TextView txtdescripcion;
    private TextView txtcosto;
    private ImageView imgMap;
    private ListView listComentarios;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems=new ArrayList<String>();
    private Bitmap loadedImage;

    private ArrayList<Cometario> arrayListComentario = new ArrayList<>();

    private String URL_LIST_COMENTARIO = "";
    private String URL_ENVIAR_COMENTARIO = "";

    private Button enviar;
    private EditText etxtenviodemesajes;
    private ImageView imgSiguienteI;
    private ImageView imgAnteriorI;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    final long ONE_MEGABYTE = 1024 * 1024;
    List<String> listaimagenes = new ArrayList<>();
    private int number = 0;


    private String imageHttpAddress = "http://54.91.89.143:8080/imagenes/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detalle_propiedad);

        final ComponentsAnimation animation = new ComponentsAnimation();


        for (int i = 0;i<MainActivity.propiedadSeleccionada.getImagenes().size();i++){

            Log.i("nuevo",MainActivity.propiedadSeleccionada.getImagenes().get(i));
            listaimagenes.add(MainActivity.propiedadSeleccionada.getImagenes().get(i));
        }


        createComponents();
        final Hostname hostname = new Hostname();
       URL_LIST_COMENTARIO = hostname.getHost()+"ventapp/ventapp/propiedad/ltsComentarios?id="+MainActivity.propiedadSeleccionada.getCodigo();
        new DetallePropiedad.JSONGetListComentario().execute(URL_LIST_COMENTARIO);

        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation.componentAnimation(getApplicationContext(),imgMap);
                startActivity(new Intent(getApplicationContext(),MapVentas.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etxtenviodemesajes.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(),"Ingrese un comentario ",Toast.LENGTH_SHORT).show();
                }else{


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etxtenviodemesajes.getWindowToken(), 0);

                    DataBase dataBase = new DataBase(getApplicationContext());
                    URL_ENVIAR_COMENTARIO = hostname.getHost()+"ventapp/ventapp/propiedad/comentarioPropiedad?" +
                            "idPropiedad="+MainActivity.propiedadSeleccionada.getCodigo() +
                            "&comentario="+etxtenviodemesajes.getText().toString().replace(" ","%20") +
                            "&idPersona="+dataBase.getUser("1").getCodigoUser();

                    new DetallePropiedad.JSONAgregarComentarios().execute(URL_ENVIAR_COMENTARIO);


                }

            }
        });

        imgSiguienteI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animation.componentAnimation(getApplicationContext(),imgSiguienteI);

                number++;

                if(number > listaimagenes.size()-1){
                    number = 0;
                }


                new JSonImagen().execute(imageHttpAddress+listaimagenes.get(number));


            }
        });

        imgAnteriorI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animation.componentAnimation(getApplicationContext(),imgAnteriorI);
                number --;

                if(number < 0){
                    number = listaimagenes.size()-1;
                }

                new JSonImagen().execute(imageHttpAddress+listaimagenes.get(number));


            }
        });


    }


    public void createComponents() {

        imgPropiedad = new ImageView(this);
        txtdireccion = new TextView(this);
        txturlVideo = new TextView(this);
        txtdescripcion = new TextView(this);
        txtcosto = new TextView(this);
        imgMap = new ImageView(this);
        listComentarios = new ListView(this);
        enviar = new Button(this);
        etxtenviodemesajes = new EditText(this);
        imgSiguienteI = new ImageView(this);
        imgAnteriorI = new ImageView(this);



        DisplayUtils displayUtils = new DisplayUtils();
        ResizeImage resizeImage = new ResizeImage();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.detallepropiedad);


        int height = displayUtils.getHeight(getWindowManager());
        int width = displayUtils.getWidth(getWindowManager());


        int indetifier = getResources().getIdentifier("nodisponible","drawable",getPackageName());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100*width,30*height);
        layoutParams.setMargins(20*width,5*height,20*width,0);
        imgPropiedad.setLayoutParams(layoutParams);
        relativeLayout.addView(imgPropiedad);



        if(listaimagenes.size() == 0){

            Bitmap bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,9*width,5*height);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            imgPropiedad.setBackgroundDrawable(bitmapDrawable);


        }else {
            new JSonImagen().execute(imageHttpAddress+listaimagenes.get(number));

        }



        layoutParams = new RelativeLayout.LayoutParams(100*width,7*height);
        txtdescripcion.setSingleLine();
        txtdescripcion.setText("Descripción : "+MainActivity.propiedadSeleccionada.getDescripcion());
        txtdescripcion.setTypeface(null, Typeface.BOLD);
        txtdescripcion.setTextColor(Color.BLACK);
        txtdescripcion.setGravity(Gravity.CENTER);
        txtdescripcion.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(5*width,35*height,5*width,0);
        txtdescripcion.setLayoutParams(layoutParams);
        relativeLayout.addView(txtdescripcion);

        layoutParams = new RelativeLayout.LayoutParams(100*width,7*height);
        txtcosto.setSingleLine();
        txtcosto.setTypeface(null, Typeface.BOLD);
        txtcosto.setText("Costo : $ "+String.valueOf(MainActivity.propiedadSeleccionada.getCosto()));
        txtcosto.setTextColor(Color.BLACK);
        txtcosto.setGravity(Gravity.CENTER);
        txtcosto.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(5*width,40*height,5*width,0);
        txtcosto.setLayoutParams(layoutParams);
        relativeLayout.addView(txtcosto);

        layoutParams = new RelativeLayout.LayoutParams(100*width,7*height);
        txtdireccion.setSingleLine();
        txtdireccion.setTypeface(null, Typeface.BOLD);
        txtdireccion.setText("Dirección : "+MainActivity.propiedadSeleccionada.getDireccion());
        txtdireccion.setTextColor(Color.BLACK);
        txtdireccion.setGravity(Gravity.CENTER);
        txtdireccion.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(5*width,45*height,5*width,0);
        txtdireccion.setLayoutParams(layoutParams);
        relativeLayout.addView(txtdireccion);

        layoutParams = new RelativeLayout.LayoutParams(100*width,7*height);
        txturlVideo.setSingleLine();
        txturlVideo.setTypeface(null, Typeface.BOLD);
        txturlVideo.setText("URL video : "+MainActivity.propiedadSeleccionada.getUrlVideo());
        txturlVideo.setTextColor(Color.BLACK);
        txturlVideo.setGravity(Gravity.CENTER);
        txturlVideo.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(5*width,50*height,5*width,0);
        txturlVideo.setLayoutParams(layoutParams);
        relativeLayout.addView(txturlVideo);

        indetifier = getResources().getIdentifier("localizar","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(18*width,10*height);
        layoutParams.setMargins(2*width,70*height,10*width,0);
        imgMap.setLayoutParams(layoutParams);
        relativeLayout.addView(imgMap);
        Bitmap bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,18*width,10*height);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        imgMap.setBackgroundDrawable(bitmapDrawable);


        layoutParams = new RelativeLayout.LayoutParams(70*width,25*height);
        layoutParams.setMargins(30*width,60*height,0,0);
        listComentarios.setLayoutParams(layoutParams);
        listComentarios.setBackgroundColor(Color.WHITE);
        relativeLayout.addView(listComentarios);

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        listComentarios.setAdapter(adapter);



        layoutParams = new RelativeLayout.LayoutParams(70*width,7*height);
        etxtenviodemesajes.setSingleLine();
        etxtenviodemesajes.setHint("Ingrese su comentario");
        etxtenviodemesajes.setTextColor(Color.BLACK);
        etxtenviodemesajes.setGravity(Gravity.CENTER);
        etxtenviodemesajes.setBackgroundColor(Color.WHITE);
        etxtenviodemesajes.setPadding(40,0,40,0);
        etxtenviodemesajes.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(30*width,87*height,0,0);
        etxtenviodemesajes.setLayoutParams(layoutParams);
        relativeLayout.addView(etxtenviodemesajes);

        layoutParams = new RelativeLayout.LayoutParams(23*width,8*height);
        layoutParams.setMargins(3*width,87*height,0,0);
        enviar.setText("Enviar");
        enviar.setLayoutParams(layoutParams);
        relativeLayout.addView(enviar);

        indetifier = getResources().getIdentifier("siguiente","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(9*width,5*height);
        layoutParams.setMargins(90*width,20*height,0*width,0);
        imgSiguienteI.setLayoutParams(layoutParams);
        relativeLayout.addView(imgSiguienteI);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,9*width,5*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgSiguienteI.setBackgroundDrawable(bitmapDrawable);


        indetifier = getResources().getIdentifier("anterior","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(9*width,5*height);
        layoutParams.setMargins(5*width,20*height,0*width,0);
        imgAnteriorI.setLayoutParams(layoutParams);
        relativeLayout.addView(imgAnteriorI);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,9*width,5*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgAnteriorI.setBackgroundDrawable(bitmapDrawable);


        if(listaimagenes.size() <= 1){
            imgAnteriorI.setVisibility(View.GONE);
            imgSiguienteI.setVisibility(View.GONE);
        }








    }


    public class JSONGetListComentario extends AsyncTask<String,String, List<Cometario>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();
        }

        @Override
        protected List<Cometario> doInBackground(String... params) {
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


                JSONArray items =new JSONArray(finalJson);//
                for(int i =0;i<items.length();i++){//recorremos el obejto jsonArray

                    JSONObject object = items.getJSONObject(i);//se posiciona en el primer objeto

                    Cometario cometario = new Cometario(
                            object.getInt("id"),
                            object.getString("comentario")
                    );


                    arrayListComentario.add(cometario);
                    Log.i("propiedades","add list");

                }

                for(int j =0; j<arrayListComentario.size();j++){
                    Log.i("propiedades","-->"+arrayListComentario.get(j).getComentario());
                }

                return arrayListComentario;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
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
                }
            }
            return  null;
        }

        @Override
        protected void onPostExecute(final List<Cometario> result) {
            super.onPostExecute(result);

            try {
            for(int i = 0;i<result.size();i++){
                listItems.add(result.get(i).getComentario());
            }


            adapter.notifyDataSetChanged();
        }catch (Exception e){


                Toast.makeText(getApplicationContext(),"Servidor Desconectado",Toast.LENGTH_SHORT).show();
            }
    }

    }

    public class JSONAgregarComentarios extends AsyncTask<String,String, String> {



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
                    Toast.makeText(getApplicationContext(),"Se un comentario con exito",Toast.LENGTH_SHORT).show();
                    listItems.add(etxtenviodemesajes.getText().toString());
                    etxtenviodemesajes.setText("");
                    adapter.notifyDataSetChanged();



                    //if si es el id de dueno de la propieda es igual id de la base si es es igual no mandar notificacion
                    DataBase dataBase = new DataBase(getApplicationContext());
                    if(dataBase.getUser("1").getCodigoUser().equals(MainActivity.propiedadSeleccionada.getCodigoCliente())){

                    }else {
                        ManagementNotificacion managementNotificacion = new ManagementNotificacion();
                        Notificacion notificacion = new Notificacion();
                        notificacion.setId(MainActivity.propiedadSeleccionada.getCodigoCliente()); //ide del propietario
                        notificacion.setNotifi("notificacion");
                        notificacion.setDescripcion(MainActivity.propiedadSeleccionada.getDescripcion());

                        managementNotificacion.insertar(notificacion);

                        //Aquinotificacion
                    }

                }

                if(result.equals("false")){
                    Toast.makeText(getApplicationContext(),"Servidor Desconectado",Toast.LENGTH_SHORT).show();

                }

            }catch (Exception e){


                Toast.makeText(getApplicationContext(),"Servidor Desconectado",Toast.LENGTH_SHORT).show();
            }


        }
    }


    public class JSonImagen extends AsyncTask<String,String,String> {

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
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                loadedImage = BitmapFactory.decodeStream(conn.getInputStream());

            } catch (IOException e) {
                //Toast.makeText(getContext(), "Error cargando la imagen: "+e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            imgPropiedad.setImageBitmap(loadedImage);
        }
    }

}
