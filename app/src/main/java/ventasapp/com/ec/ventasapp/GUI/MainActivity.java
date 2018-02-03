package ventasapp.com.ec.ventasapp.GUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ventasapp.com.ec.ventasapp.R;
import ventasapp.com.ec.ventasapp.adapter.ItemPropiedadAdapter;
import ventasapp.com.ec.ventasapp.adapter.ItemPropiedadBusquedaAdapter;
import ventasapp.com.ec.ventasapp.model.Categoria;
import ventasapp.com.ec.ventasapp.model.Imagen;
import ventasapp.com.ec.ventasapp.model.ItemPropiedad;
import ventasapp.com.ec.ventasapp.model.ItemPropiedadBusqueda;
import ventasapp.com.ec.ventasapp.model.Propiedad;
import ventasapp.com.ec.ventasapp.modelos.Persona;
import ventasapp.com.ec.ventasapp.utilidades.ComponentsAnimation;
import ventasapp.com.ec.ventasapp.utilidades.DisplayUtils;
import ventasapp.com.ec.ventasapp.utilidades.Hostname;
import ventasapp.com.ec.ventasapp.utilidades.ResizeImage;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    public static Propiedad propiedadSeleccionada;
    private Bitmap loadedImage;

    private ImageButton btnBuscar;
    private ImageButton btnMenu;
    private TextView fondoBuscar;
    private ListView listPropiedades;

    private LocationRequest mLocationRequest;
    private GoogleApiClient apiClient;
    public static Location orderLocation;
    static final int LOCATION_PERMISSIONS_REQUEST=0;

    String URL_TO_HIT;

    //Para cargar datos en la lista
    ListView listViewPropiedades;
    ItemPropiedadAdapter adapter;

    List<ItemPropiedad> propiedadList;

    ArrayList<Propiedad> auxPropiedad = new ArrayList<>();

    private ArrayList<Propiedad> arrayListPropiedad = new ArrayList<>();


    private ArrayList<Propiedad> auxArrayListPropiedad ;

    ArrayList<List<String>> auxImg;

    private String imageHttpAddress = "http://54.91.89.143:8080/img/";

    private ArrayList<Bitmap> auxloadImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        final ComponentsAnimation animation = new ComponentsAnimation();
        createComponents();
        Hostname hostname = new Hostname();
        URL_TO_HIT = hostname.getHost()+"ventapp/ventapp/propiedad/propiedades";
        new MainActivity.JSONTask().execute(URL_TO_HIT);


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Intent intent = new Intent(getApplicationContext(), BusquedaPropiedades.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //Intent intent = new Intent(getApplicationContext(), Perfil.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                animation.componentAnimation(getApplicationContext(),btnBuscar);
                Intent intent = new Intent(getApplicationContext(), BusquedaPropiedades.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //Intent intent = new Intent(getApplicationContext(), RutaMapa.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
//                Intent intent = new Intent(getApplicationContext(), BusquedaPropiedades.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                //Intent intent = new Intent(getApplicationContext(), RutaMapa.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);

                //startActivity(new Intent(getApplicationContext(),Publicaciones.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

            }
        });


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Places.GEO_DATA_API)
                .addOnConnectionFailedListener(this)
                .build();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation.componentAnimation(getApplicationContext(),btnMenu);
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

    }

    public void createComponents(){

        btnBuscar = new ImageButton(this);
        btnMenu = new ImageButton(this);
        listPropiedades = new ListView(this);
        fondoBuscar = new TextView(this);


        DisplayUtils displayUtils = new DisplayUtils();
        ResizeImage resizeImage = new ResizeImage();
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.activity_main);

        int height = displayUtils.getHeight(getWindowManager());
        int width = displayUtils.getWidth(getWindowManager());

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(80*width,30*height);


        int icon_size = 10*width;

        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,3*height+10*width );
        layoutParams.setMargins(0,0,0,0);
        fondoBuscar.setLayoutParams(layoutParams);
        relativeLayout.addView(fondoBuscar);
        fondoBuscar.setBackgroundColor(getResources().getColor(R.color.fondobuscar));
        //fondoBuscar.set

        int indetifier = getResources().getIdentifier("btnsearch","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(icon_size,icon_size);
        layoutParams.setMargins(90*width,1*height,1,0);
        layoutParams.setMargins(90*width,2*height,0,0);
        btnBuscar.setLayoutParams(layoutParams);
        relativeLayout.addView(btnBuscar);
        Bitmap bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,100*width,100*width);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        btnBuscar.setBackgroundDrawable(bitmapDrawable);

        indetifier = getResources().getIdentifier("btnmenu","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(icon_size,icon_size);
        layoutParams.setMargins(5*width,1*height,80,0);
        btnMenu.setLayoutParams(layoutParams);
        relativeLayout.addView(btnMenu);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,100*width,100*width);
        bitmapDrawable = new BitmapDrawable(bitmap);
        btnMenu.setBackgroundDrawable(bitmapDrawable);

        int separacionLista = 3*height + icon_size + (icon_size/2);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(5,separacionLista,5,0);
        listPropiedades.setLayoutParams(layoutParams);
        relativeLayout.addView(listPropiedades);

       // listViewPropiedades = (ListView)findViewById(R.id.listDestinations);




        listPropiedades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {

                Log.i("propiedades", String.valueOf(auxPropiedad.get(posicion).getCosto()));

                propiedadSeleccionada = new Propiedad(auxPropiedad.get(posicion).getCodigo(),auxPropiedad.get(posicion).getDireccion(),
                        auxPropiedad.get(posicion).getUrlVideo(),auxPropiedad.get(posicion).getDescripcion(),
                        auxPropiedad.get(posicion).getEstado(), auxPropiedad.get(posicion).getTipo(),
                        auxPropiedad.get(posicion).getCosto(), auxPropiedad.get(posicion).getLonguitud(),
                        auxPropiedad.get(posicion).getLatitud(),auxPropiedad.get(posicion).getCodigoCliente());


                propiedadSeleccionada.setImagenes(auxPropiedad.get(posicion).getImagenes());

                Intent intent = new Intent(getApplicationContext(), DetallePropiedad.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                Log.i("propiedades", "-----"+ propiedadSeleccionada.getLatitud() +" , " +propiedadSeleccionada.getLonguitud());
            }
        });



//        llenarLista();
    }

    public void llenarLista(){

//        propiedadList.add(new ItemPropiedad(1,R.drawable.btnsearch,"$" + "12", "casa"));
//        propiedadList.add(new ItemPropiedad(1,R.drawable.btnsearch,"$" + "12", "asdasdasd"));
//        propiedadList.add(new ItemPropiedad(1,R.drawable.btnsearch,"$" + "12", "fasdffd"));
//        propiedadList.add(new ItemPropiedad(1,R.drawable.btnsearch,"$" + "12", "safdfds"));
//        propiedadList.add(new ItemPropiedad(1,R.drawable.btnsearch,"$" + "12", "asdasdad"));




    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSIONS_REQUEST);

            return;
        }else {
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, mLocationRequest, this);
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            orderLocation = lastLocation;

            Log.i("nuevo",String.valueOf(lastLocation.getLatitude())+" "+ String.valueOf(lastLocation.getLongitude()));

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    public class JSONTask extends AsyncTask<String,String, List<Propiedad>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //dialog.show();
    }

    @Override
    protected List<Propiedad> doInBackground(String... params) {
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

            JSONArray itemsImg =new JSONArray(finalJson);//

            for(int i =0;i<items.length();i++){//recorremos el obejto jsonArray

                JSONObject object = items.getJSONObject(i);//se posiciona en el primer objeto

                String persona=object.getString("persona");
                JSONObject jsonpersona = new JSONObject(persona);
                String idpersona = jsonpersona.getString("ec.edu.ups.Model.Persona");

                Log.i("propiedad","---------------"+idpersona);

                Propiedad propiedad = new Propiedad(
                        object.getInt("codigo"),
                        object.getString("direccion"),
                        object.getString("urlVideo"),
                        object.getString("descripcion"),
                        object.getString("estado"),
                        object.getString("tipo"),
                        object.getDouble("costo"),
                        object.getDouble("longuitud"),
                        object.getDouble("latitud"),
                        idpersona

                );



                String ltsImg=object.getString("imagenes");
                JSONArray ltsImgArr =new JSONArray(ltsImg);//
                ArrayList<String> ltsImagenes=new ArrayList<>();

                for(int j =0;j<ltsImgArr.length();j++){
                    JSONObject nombreimg = ltsImgArr.getJSONObject(j);//se posiciona en el primer objeto

                    ltsImagenes.add(nombreimg.getString("nombreImagen"));
                    //Imagen imagen = new Imagen();
                    Log.i("--->",""+nombreimg.getString("nombreImagen"));



                }
                propiedad.setImagenes(ltsImagenes);

                arrayListPropiedad.add(propiedad);
                Log.i("propiedades","add list");
                //holavgdfgdfgdfgdf vjhvhbkjbfskdjfbskjdfbjsd

            }

            for(int j =0; j<arrayListPropiedad.size();j++){
                Log.i("propiedades","-->"+arrayListPropiedad.get(j).getDireccion().toString());
//
//                if(arrayListPropiedad.get(j).getImagenes().size() == 0){
//                    int imageId = getResources().getIdentifier("btnsearch","drawable",getPackageName());
//                    loadedImage =  BitmapFactory.decodeResource(getResources(), imageId);
//                    auxloadImage.add(loadedImage);
//
//                }else {
//
//                    new MainActivity.JSonImagen().execute(imageHttpAddress+arrayListPropiedad.get(j).getImagenes().get(0));
//
//
//                }
            }



            return arrayListPropiedad;

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
    protected void onPostExecute(final List<Propiedad> result) {
        super.onPostExecute(result);

        int imageId = getResources().getIdentifier("home","drawable",getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);

        auxPropiedad = new ArrayList<>();
        auxImg = new ArrayList<>();
        List<ItemPropiedad> listElements = new ArrayList<ItemPropiedad>();
        Log.i("propiedades", String.valueOf(result.size()));


//
//
        for(int i = 0;i<result.size();i++){
            auxPropiedad.add(result.get(i));
//            auxImg.add(result.get(i).getImagenes().get(i));
//            propiedadList.add(new ItemPropiedad(bitmap,"$" + result.get(i).getCosto(), result.get(i).getDescripcion()));

            listElements.add(new ItemPropiedad(bitmap, "$ "+
                    String.valueOf(result.get(i).getCosto()),
                    result.get(i).getDescripcion()));

//            Log.i("propiedades", String.valueOf(result.get(i).getCosto()));
        }


        adapter = new ItemPropiedadAdapter(getApplicationContext(),listElements);
        listPropiedades.setAdapter(adapter);
        adapter.notifyDataSetChanged();


///////////////////
//        adapter.notifyDataSetChanged();

//        List<ItemPropiedad> listElements = new ArrayList<ItemPropiedad>();
//        auxArrayListPropiedad = new ArrayList<>();
//
//        int imageId = getResources().getIdentifier("btnsearch","drawable",getPackageName());
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
//
//        for(int i = 0;i<result.size();i++){
//            listElements.add(new ItemPropiedad(bitmap,
//                    String.valueOf(result.get(i).getCosto()),
//                    result.get(i).getDescripcion()));
////                auxArrayListPropiedad.add(new Propiedad(result.get(i).getCodigo(), result.get(i).getDireccion(), result.get(i).getUrlVideo()
////                ,result.get(i).getDescripcion(),result.get(i).getEstado(),result.get(i).getTipo(), result.get(i).getCosto()
////                ,result.get(i).getLonguitud(),result.get(i).getLatitud(), result.get(i).getCodigoCliente()));
//        }

//        tamanoListPrecio =listElements.size();
//                auxPropiedad.add(result.get(i));
//                propiedadList.add(new ItemPropiedad(1,R.drawable.btnsearch,"$" + result.get(i).getCosto(), result.get(i).getDescripcion()));
//
////            Log.i("propiedades", String.valueOf(result.get(i).getCosto()));
//            }
//
//            adapterB.notifyDataSetChanged();

//        propiedadList = new ItemPropiedadAdapter()
//
//        propiedadList = new ItemPropiedadAdapter(getApplicationContext(),listElements);
//        listPropiedades.setAdapter(propiedadList);
//        propiedadList.notifyDataSetChanged();


    }
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        //this.finish();
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
            List<ItemPropiedad> listElements = new ArrayList<ItemPropiedad>();
            auxloadImage.add(loadedImage);
            Log.i("dato","aux  ===== "+auxloadImage.size());

           // imgPropiedad.setImageBitmap(loadedImage);
        }
    }


}