package ventasapp.com.ec.ventasapp.GUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Date;
import java.util.List;

import ventasapp.com.ec.ventasapp.R;
import ventasapp.com.ec.ventasapp.adapter.ItemPropiedadAdapter;
import ventasapp.com.ec.ventasapp.data.DataBase;
import ventasapp.com.ec.ventasapp.model.ItemPropiedad;
import ventasapp.com.ec.ventasapp.model.Propiedad;
import ventasapp.com.ec.ventasapp.utilidades.DisplayUtils;
import ventasapp.com.ec.ventasapp.utilidades.Hostname;
import ventasapp.com.ec.ventasapp.utilidades.ResizeImage;

public class Publicaciones extends AppCompatActivity {

    ListView listPropiedades;
    private ArrayList<Propiedad> arrayListPropiedad = new ArrayList<>();
    ArrayList<Propiedad> auxPropiedad = new ArrayList<>();
    ArrayList<List<String>> auxImg;
    ItemPropiedadAdapter adapter;
    String URL_TO_HIT;
    Integer codigoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_publicaciones);

        DataBase dataBase = new DataBase(getApplicationContext());
        codigoUser = Integer.valueOf(dataBase.getUser("1").getCodigoUser());
        Log.i("dato","cod: "+codigoUser);
        crearComponentes();
        Hostname hostname = new Hostname();
        URL_TO_HIT = hostname.getHost()+"ventapp/ventapp/propiedad/ltsPropiedadByPerson?idPerson="+codigoUser;
        new Publicaciones.JSONTask().execute(URL_TO_HIT);


    }

    public void crearComponentes(){

        listPropiedades = new ListView(this);


        DisplayUtils displayUtils = new DisplayUtils();
        ResizeImage resizeImage = new ResizeImage();
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.ac_publicaciones);

        int height = displayUtils.getHeight(getWindowManager());
        int width = displayUtils.getWidth(getWindowManager());

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(80*width,30*height);


        int icon_size = 10*width;

        int separacionLista = 3*height + icon_size + (icon_size/2);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(5,10*height,5,0);
        listPropiedades.setLayoutParams(layoutParams);
        relativeLayout.addView(listPropiedades);

        listPropiedades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {

//                Log.i("propiedades", String.valueOf(auxPropiedad.get(posicion).getCosto()));
//
//                propiedadSeleccionada = new Propiedad(auxPropiedad.get(posicion).getCodigo(),auxPropiedad.get(posicion).getDireccion(),
//                        auxPropiedad.get(posicion).getUrlVideo(),auxPropiedad.get(posicion).getDescripcion(),
//                        auxPropiedad.get(posicion).getEstado(), auxPropiedad.get(posicion).getTipo(),
//                        auxPropiedad.get(posicion).getCosto(), auxPropiedad.get(posicion).getLonguitud(),
//                        auxPropiedad.get(posicion).getLatitud(),auxPropiedad.get(posicion).getCodigoCliente());
//
//
//                propiedadSeleccionada.setImagenes(auxPropiedad.get(posicion).getImagenes());
//
//                Intent intent = new Intent(getApplicationContext(), DetallePropiedad.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//
//                Log.i("propiedades", "-----"+ propiedadSeleccionada.getLatitud() +" , " +propiedadSeleccionada.getLonguitud());
            }
        });

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
                    String idpersona = String.valueOf(codigoUser);

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
}
