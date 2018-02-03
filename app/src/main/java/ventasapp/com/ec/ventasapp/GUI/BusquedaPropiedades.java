package ventasapp.com.ec.ventasapp.GUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import ventasapp.com.ec.ventasapp.adapter.ItemPropiedadAdapter;
import ventasapp.com.ec.ventasapp.adapter.ItemPropiedadBusquedaAdapter;
import ventasapp.com.ec.ventasapp.model.Categoria;
import ventasapp.com.ec.ventasapp.model.ItemPropiedad;
import ventasapp.com.ec.ventasapp.model.ItemPropiedadBusqueda;
import ventasapp.com.ec.ventasapp.model.Propiedad;
import ventasapp.com.ec.ventasapp.model.Sector;
import ventasapp.com.ec.ventasapp.utilidades.DisplayUtils;
import ventasapp.com.ec.ventasapp.utilidades.Hostname;
import ventasapp.com.ec.ventasapp.utilidades.ResizeImage;

public class BusquedaPropiedades extends AppCompatActivity {

    public static List<Propiedad> listadoPropiedadesDirecciones;

    private Spinner spinner;
    private Spinner spinnerSubCategoria;
    private Spinner spinnerSector;
    private TextView txtBusqueda;
    private EditText etxtPrecioMin;
    private EditText etxtPrecioMax;
    private Button btnBuscar;
    private Button btnDirecciones;

    private ListView listPropiedades;
    private TextView txtSep;
    String URL_TO_HIT;

    int tamanoListPrecio = 0;


    ItemPropiedadBusquedaAdapter adapterB;


    String[] parametroBusqueda = {"Precio", "Categoria", "Sector"};
    String[] categoriaItems ;
    String[] sectorItems ;

    String parametro;


    private ArrayList<Categoria> arrayListCategoria = new ArrayList<>();
    private ArrayList<Sector> arrayListSector = new ArrayList<>();


    ArrayList<ItemPropiedadBusqueda> propiedadList = new ArrayList<>();

    ArrayList<Propiedad> auxPropiedad = new ArrayList<>();

    private ArrayList<Propiedad> arrayListPropiedad = new ArrayList<>();
    private ArrayList<Propiedad> auxArrayListPropiedad = new ArrayList<>();


    private ArrayAdapter<ItemPropiedadBusqueda> elementsPropiedadArrayAdapter ;

    private ArrayList<Integer> auxCodigo;

    private Integer codigoSector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_busqueda_propiedades);

        Hostname hostname = new Hostname();
        URL_TO_HIT = hostname.getHost()+"ventapp/ventapp/propiedad/sectores";
        new BusquedaPropiedades.JSONTaskSector().execute(URL_TO_HIT);

        hostname = new Hostname();
        URL_TO_HIT = hostname.getHost()+"ventapp/ventapp/propiedad/categorias";
        new BusquedaPropiedades.JSONTaskCategoria().execute(URL_TO_HIT);

        createComponents();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrayListPropiedad.clear();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etxtPrecioMax.getWindowToken(), 0);
                Log.i("propiedades","_______ "+etxtPrecioMin.getText() + " ___"+etxtPrecioMax.getText());

                if(parametro.equals("Precio")){
                    Hostname hostname = new Hostname();
                    URL_TO_HIT = hostname.getHost()+"ventapp/ventapp/propiedad/propiedadesbyprecio?valmax="
                            + Integer.parseInt(etxtPrecioMax.getText().toString())+"&valmin="
                            + Integer.parseInt(etxtPrecioMin.getText().toString()) ;
                    new BusquedaPropiedades.JSONTaskPropiedadPorPrecio().execute(URL_TO_HIT);
                    listadoPropiedadesDirecciones = new ArrayList<Propiedad>();

                } else if (parametro.equals("Categoria")){

                    Log.i("dato","codigo cat:"+codigoSector);

                    Hostname hostname = new Hostname();
                    URL_TO_HIT = hostname.getHost()+"ventapp/ventapp/propiedad/propiedadesbycategoria?idscategoria="
                            + codigoSector;
                    new BusquedaPropiedades.JSONTaskPropiedadPorCategoria().execute(URL_TO_HIT);
                    listadoPropiedadesDirecciones = new ArrayList<Propiedad>();

                } else if (parametro.equals("Sector")){

                    Log.i("dato","codigo sector:"+codigoSector);

                    Hostname hostname = new Hostname();
                    URL_TO_HIT = hostname.getHost()+"ventapp/ventapp/propiedad/propiedadesbysector?idsector="
                            + codigoSector;
                    new BusquedaPropiedades.JSONTaskPropiedadPorSector().execute(URL_TO_HIT);
                    listadoPropiedadesDirecciones = new ArrayList<Propiedad>();

                }



            }
        });

        btnDirecciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<String> seleccionados = new ArrayList<String>();


                for(int posicion=0;posicion<tamanoListPrecio;posicion++){

                    if(elementsPropiedadArrayAdapter.getItem(posicion).isCheckPropiedad()==true){

                        Log.i("dato","SELECCIONADO "+ "cocli: "+ auxArrayListPropiedad.get(posicion).getCodigoCliente()+
                                "codigo : "+ auxArrayListPropiedad.get(posicion).getCodigo()+" posicio++++"+posicion);
//                        Log.i("dato",elementsPropiedadArrayAdapter.getItem(i).g)
                        Propiedad propiedad = new Propiedad(auxArrayListPropiedad.get(posicion).getCodigo(),auxArrayListPropiedad.get(posicion).getDireccion(),
                                auxArrayListPropiedad.get(posicion).getUrlVideo(),auxArrayListPropiedad.get(posicion).getDescripcion(),
                                auxArrayListPropiedad.get(posicion).getEstado(), auxArrayListPropiedad.get(posicion).getTipo(),
                                auxArrayListPropiedad.get(posicion).getCosto(), auxArrayListPropiedad.get(posicion).getLonguitud(),
                                auxArrayListPropiedad.get(posicion).getLatitud(),auxArrayListPropiedad.get(posicion).getCodigoCliente());

                        listadoPropiedadesDirecciones.add(propiedad);



//                        final int ia = view.getId();
//
//                        seleccionados.add(elementsCategoryArrayAdapter.getItem(i).getTxtElement()+
//                                ":"+elementsCategoryArrayAdapter.getItem(i).getTxtFeature());
//                        dataElement = new DataElement();
//                        int numberRecords = dataBase.getNumberRecordsAV();
//                        for(int j=1; j<numberRecords+1;j++){
//                            String element = dataBase.getArtificialGame(j).getPictograma();
//                            if(element.equals(elementsCategoryArrayAdapter.getItem(i).getTxtElement())){
//                                code = dataBase.getArtificialGame(j).getCode();
//                            }
//                        }
//                        dataElement.setElement(code);
//                        dataElement.setDescription(elementsCategoryArrayAdapter.getItem(i).getTxtFeature());
//                        dataElements.add(dataElement);

                    }

                }


                startActivity(new Intent(getApplicationContext(),RutaMapa.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });

    }


    public void createComponents(){


        spinner = new Spinner(this);
        txtBusqueda = new TextView(this);
        btnBuscar = new Button(this);
        btnDirecciones = new Button(this);
        etxtPrecioMin = new EditText(this);
        etxtPrecioMax = new EditText(this);
        spinnerSubCategoria = new Spinner(this);
        spinnerSector = new Spinner(this);
        listPropiedades = new ListView(this);



        DisplayUtils displayUtils = new DisplayUtils();
        ResizeImage resizeImage = new ResizeImage();
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.activity_busqueda_propiedades);

        int height = displayUtils.getHeight(getWindowManager());
        int width = displayUtils.getWidth(getWindowManager());

        RelativeLayout.LayoutParams layoutParams;

        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,5*height );
        layoutParams.setMargins(6*width,5*height,10*width,0);
        txtBusqueda.setLayoutParams(layoutParams);
        txtBusqueda.setText("Parámeto de busqueda:");
        txtBusqueda.setTextColor(Color.BLACK);
        txtBusqueda.setTextSize(20);
        txtBusqueda.setTypeface(null,Typeface.BOLD);
        relativeLayout.addView(txtBusqueda);

        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2*height+8*width );
        layoutParams.setMargins(10*width,11*height,10*width,0);
        spinner.setLayoutParams(layoutParams);
        relativeLayout.addView(spinner);

        // Get reference of SpinnerView from layout/main_activity.xml

         ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,parametroBusqueda);

        spinner.setAdapter(adapter);
        spinner.setBackgroundColor(getResources().getColor(R.color.fondospinner));
//        spinner.setVisibility(View.INVISIBLE);

        crearSubParametroPrecio();

        crearSubParametroCategoria();
        crearSubParametroSector();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                int sid=spinner.getSelectedItemPosition();
//                Toast.makeText(getBaseContext(), "You have selected City : " + parametroBusqueda[sid],
//                        Toast.LENGTH_SHORT).show();
                parametro = parametroBusqueda[sid];
                if(parametroBusqueda[sid].equals("Precio")){
                    Log.i("propiedades","IF PRECIO "+parametroBusqueda[sid]);
                    vistaSubParametroPrecio(View.VISIBLE);
                    vistaSubParametroCategoria(View.INVISIBLE);
                    vistaSubParametroSector(View.INVISIBLE);

                }
                if(parametroBusqueda[sid].equals("Categoria")){
                    Log.i("propiedades","IF CATEGORIA "+parametroBusqueda[sid]);
                    vistaSubParametroPrecio(View.INVISIBLE);
                    vistaSubParametroCategoria(View.VISIBLE);
                    vistaSubParametroSector(View.INVISIBLE);


                }
                if(parametroBusqueda[sid].equals("Sector")){
                    Log.i("propiedades","IF SECTOR "+parametroBusqueda[sid]);
                    vistaSubParametroPrecio(View.INVISIBLE);
                    vistaSubParametroCategoria(View.INVISIBLE);
                    vistaSubParametroSector(View.VISIBLE);


                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

                int sid=spinner.getSelectedItemPosition();
                parametro = parametroBusqueda[sid];

                if(parametroBusqueda[sid].equals("Precio")){
                    Log.i("propiedades","IF PRECIO "+parametroBusqueda[sid]);


                }
                if(parametroBusqueda[sid].equals("Categoria")){
                    Log.i("propiedades","IF CATEGORIA "+parametroBusqueda[sid]);


                }
                if(parametroBusqueda[sid].equals("Sector")){
                    Log.i("propiedades","IF SECTOR "+parametroBusqueda[sid]);


                }
            }
        });


        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2*height+8*width );
        layoutParams.setMargins(35*width,33*height,35*width,0);
        btnBuscar.setLayoutParams(layoutParams);
        btnBuscar.setText("BUSCAR");
        btnBuscar.setTextColor(Color.WHITE);
        btnBuscar.setBackgroundColor(Color.parseColor("#4e7498"));
        relativeLayout.addView(btnBuscar);

        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT );
        layoutParams.setMargins(10*width,41*height,10*width,15*height);
        listPropiedades.setLayoutParams(layoutParams);
        listPropiedades.setBackgroundColor(Color.parseColor("#edf1f4"));
        listPropiedades.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        relativeLayout.addView(listPropiedades);

//        adapterB = new ItemPropiedadBusquedaAdapter(getApplicationContext(), propiedadList);
//        listPropiedades.setAdapter(adapterB);



//        adapterB.notifyDataSetChanged();

        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2*height+8*width );
        layoutParams.setMargins(35*width,90*height,35*width,0);
        btnDirecciones.setLayoutParams(layoutParams);
        btnDirecciones.setText("IR MAPA");
        btnDirecciones.setTextColor(Color.WHITE);
        btnDirecciones.setBackgroundColor(Color.parseColor("#4e7498"));
        relativeLayout.addView(btnDirecciones);



    }

    private List<ItemPropiedadBusqueda> getModel() {


        List<ItemPropiedadBusqueda> listElements = new ArrayList<ItemPropiedadBusqueda>();

        int imageId = getResources().getIdentifier("btnsearch","drawable",getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
        String editText = "";
        String descrip="";
        listElements.add(get(bitmap,editText, descrip));


//        for (int i=1;i<numberRecords+1;i++){
//            String category = dataBase.getArtificialGame(i).getCategory();
//            if(category.equals(categoryElement)){
//                int imageId = getResources().getIdentifier(dataBase.getArtificialGame(i).getCode(),"drawable",getPackageName());
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
//                String editText = "";
//                listElements.add(get(bitmap,dataBase.getArtificialGame(i).getPictograma().toString(),editText));
//
//            }
//        }

        return listElements;
    }


    private ItemPropiedadBusqueda get(Bitmap bitmap, String precio,String descripcion) {
        return new ItemPropiedadBusqueda(bitmap,precio,descripcion);
    }


    public void vistaSubParametroCategoria(int v){
        spinnerSubCategoria.setVisibility(v);
    }

    public void vistaSubParametroPrecio(int v){
        etxtPrecioMax.setVisibility(v);
        etxtPrecioMin.setVisibility(v);
    }

    public void vistaSubParametroSector(int v){
        spinnerSector.setVisibility(v);
    }

    public void crearSubParametroCategoria() {

        DisplayUtils displayUtils = new DisplayUtils();
        ResizeImage resizeImage = new ResizeImage();
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.activity_busqueda_propiedades);

        int height = displayUtils.getHeight(getWindowManager());
        int width = displayUtils.getWidth(getWindowManager());

        RelativeLayout.LayoutParams layoutParams;

        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2*height+8*width );
        layoutParams.setMargins(10*width,23*height,10*width,0);
        spinnerSubCategoria.setLayoutParams(layoutParams);

        relativeLayout.addView(spinnerSubCategoria);

        // Get reference of SpinnerView from layout/main_activity.xml

//        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
//                R.layout.simple_spinner_dropdown_item ,categoriaItems);
//
//        spinnerSubCategoria.setAdapter(adapter);
        spinnerSubCategoria.setBackgroundColor(getResources().getColor(R.color.fondospinner));

//        spinnerCategoria.setEnabled();

        spinnerSubCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                int sid=spinnerSubCategoria.getSelectedItemPosition();
//                Toast.makeText(getBaseContext(), "You have selected City : " + parametroBusqueda[sid],
//                        Toast.LENGTH_SHORT).show();


                codigoSector = arrayListCategoria.get(position).getCodigo();
                Log.i("dato", String.valueOf(arrayListCategoria.get(sid).getCodigo()));


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void crearSubParametroSector() {

        DisplayUtils displayUtils = new DisplayUtils();
        ResizeImage resizeImage = new ResizeImage();
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.activity_busqueda_propiedades);

        int height = displayUtils.getHeight(getWindowManager());
        int width = displayUtils.getWidth(getWindowManager());

        RelativeLayout.LayoutParams layoutParams;

        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2*height+8*width );
        layoutParams.setMargins(10*width,23*height,10*width,0);
        spinnerSector.setLayoutParams(layoutParams);

        relativeLayout.addView(spinnerSector);


//        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
//                R.layout.simple_spinner_dropdown_item ,sectorItems);
//
//        spinnerSector.setAdapter(adapter);

        spinnerSector.setBackgroundColor(getResources().getColor(R.color.fondospinner));

//        spinnerCategoria.setEnabled();

        spinnerSector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                int sid = spinnerSector.getSelectedItemPosition();
//                Toast.makeText(getBaseContext(), "You have selected: " + parametroBusqueda[sid],
//                        Toast.LENGTH_SHORT).show();

                codigoSector = arrayListSector.get(position).getCodigo();

                Log.i("dato", String.valueOf(arrayListSector.get(sid).getCodigo()));



            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }


    @SuppressLint("ResourceType")
    public void crearSubParametroPrecio() {


        DisplayUtils displayUtils = new DisplayUtils();
        ResizeImage resizeImage = new ResizeImage();
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.activity_busqueda_propiedades);

        int height = displayUtils.getHeight(getWindowManager());
        int width = displayUtils.getWidth(getWindowManager());

        RelativeLayout.LayoutParams layoutParams;

        layoutParams = new RelativeLayout.LayoutParams(22*width,2*height+8*width );
        layoutParams.setMargins(22*width,23*height,0,0);
        etxtPrecioMin.setLayoutParams(layoutParams);
//        etxtPrecioMin.setVisibility(v);
        etxtPrecioMin.setHint("min");
        etxtPrecioMin.setGravity(Gravity.CENTER_HORIZONTAL);
        etxtPrecioMin.setInputType(InputType.TYPE_CLASS_NUMBER);
        etxtPrecioMin.setFilters(new InputFilter[] { new InputFilter.LengthFilter(7) });
        etxtPrecioMin.setBackgroundResource(R.layout.edittext_border);
        relativeLayout.addView(etxtPrecioMin);

        layoutParams = new RelativeLayout.LayoutParams(22*width,2*height+8*width );
        layoutParams.setMargins(62*width,23*height,0,0);
        etxtPrecioMax.setLayoutParams(layoutParams);
//        etxtPrecioMax.setVisibility(v);
        etxtPrecioMax.setHint("max");
        etxtPrecioMax.setGravity(Gravity.CENTER_HORIZONTAL);
        etxtPrecioMax.setInputType(InputType.TYPE_CLASS_NUMBER);
        etxtPrecioMax.setFilters(new InputFilter[] { new InputFilter.LengthFilter(7) });
        etxtPrecioMax.setBackgroundResource(R.layout.edittext_border);
        relativeLayout.addView(etxtPrecioMax);


    }


    // Para cargar las subcategorias de "CATEGORIA"
    public class JSONTaskCategoria extends AsyncTask<String,String, List<Categoria>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();
        }

        @Override
        protected List<Categoria> doInBackground(String... params) {
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
               // categoriaItems = new String[items.length()];

                for(int i =0;i<items.length();i++){//recorremos el obejto jsonArray

                    JSONObject object = items.getJSONObject(i);//se posiciona en el primer objeto

                    Categoria categoria = new Categoria(
                            object.getInt("codigo"),
                            object.getString("descripcion")
                    );

                    arrayListCategoria.add(categoria);
                    Log.i("propiedades","add list");
                   // categoriaItems[i] = arrayListCategoria.get(i).getDescripcion().toString();

                }

//                for(int j =0; j<arrayListCategoria.size();j++){
//                    Log.i("propiedades","-->"+arrayListCategoria.get(j).getDescripcion().toString());
//                }


//
//                for(int i = 0;i<arrayListCategoria.size();i++){
////                    categoriaItems[i] = arrayListCategoria.get(i).getDescripcion().toString();
//                }


                return arrayListCategoria;

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
        protected void onPostExecute(final List<Categoria> result) {
            super.onPostExecute(result);
//            categoriaItems = new String[result.size()] ;

//
//            for(int i = 0;i<result.size();i++){
//                categoriaItems[i] = result.get(i).getDescripcion().toString();
//            }
            //Una vez consultado las subcategorias al servidor creamos el spinner con la lista recuperada
            ArrayList<String> itemsCategorias = new ArrayList<>();
            for(int i = 0;i<result.size();i++){
//                sectorItems[i] = result.get(i).getNombre().toString();
                itemsCategorias.add(result.get(i).getDescripcion().toString());
            }
            //Una vez consultado las subcategorias al servidor creamos el spinner con la lista recuperada

            ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(),android.
                    R.layout.simple_spinner_dropdown_item ,itemsCategorias);

            spinnerSubCategoria.setAdapter(adapter);


        }
    }


    // Para cargar las subcategorias de "SECTOR"
    public class JSONTaskSector extends AsyncTask<String,String, List<Sector>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();
        }

        @Override
        protected List<Sector> doInBackground(String... params) {
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

                    Sector sector = new Sector(
                            object.getInt("codigo"),
                            object.getString("nombre")
                    );

                    arrayListSector.add(sector);
                    Log.i("propiedades","add list tma:" + arrayListSector.size());

                }

//                for(int j =0; j<arrayListCategoria.size();j++){
//                    Log.i("propiedades","-->"+arrayListCategoria.get(j).getDescripcion().toString());
//                }

//                sectorItems = new String[arrayListSector.size()];
//
//                for(int i = 0;i<arrayListSector.size();i++){
//                    sectorItems[i] = arrayListSector.get(i).getNombre().toString();
//                }


                return arrayListSector;

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
        protected void onPostExecute(final List<Sector> result) {
            super.onPostExecute(result);
            ArrayList<String> itemsSector = new ArrayList<>();


            for(int i = 0;i<result.size();i++){
//                sectorItems[i] = result.get(i).getNombre().toString();
                itemsSector.add(result.get(i).getNombre().toString());
            }
            //Una vez consultado las subcategorias al servidor creamos el spinner con la lista recuperada

            ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(),android.
                    R.layout.simple_spinner_dropdown_item ,itemsSector);

            spinnerSector.setAdapter(adapter);



        }
    }

    // Para cargar las propiedades por precio
    public class JSONTaskPropiedadPorPrecio extends AsyncTask<String,String, List<Propiedad>> {

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
                for(int i =0;i<items.length();i++){//recorremos el obejto jsonArray

                    JSONObject object = items.getJSONObject(i);//se posiciona en el primer objeto

                    String persona=object.getString("persona");
                    JSONObject jsonpersona = new JSONObject(persona);
                    String idpersona = jsonpersona.getString("ec.edu.ups.Model.Persona");

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
                    JSONArray ltsImgArr =new JSONArray(finalJson);//
                    ArrayList<String> ltsImagenes=new ArrayList<>();

                    for(int j =0;j<ltsImgArr.length();j++){
                        JSONObject nombreimg = ltsImgArr.getJSONObject(i);//se posiciona en el primer objeto
                        ltsImagenes.add(nombreimg.getString("imagenes"));

                    }
                    propiedad.setImagenes(ltsImagenes);

                    arrayListPropiedad.add(propiedad);
                    Log.i("propiedades","add list");

                }

                for(int j =0; j<arrayListPropiedad.size();j++){
                    Log.i("propiedades","-->"+arrayListPropiedad.get(j).getDireccion().toString());
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


            List<ItemPropiedadBusqueda> listElements = new ArrayList<ItemPropiedadBusqueda>();




            int imageId = getResources().getIdentifier("home","drawable",getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);

            for(int i = 0;i<result.size();i++){

                auxArrayListPropiedad.add(result.get(i));
                listElements.add(new ItemPropiedadBusqueda(bitmap, "$ "+
                        String.valueOf(result.get(i).getCosto()),
                        result.get(i).getDescripcion()));
//                auxArrayListPropiedad.add(new Propiedad(result.get(i).getCodigo(), result.get(i).getDireccion(), result.get(i).getUrlVideo()
//                ,result.get(i).getDescripcion(),result.get(i).getEstado(),result.get(i).getTipo(), result.get(i).getCosto()
//                ,result.get(i).getLonguitud(),result.get(i).getLatitud(), result.get(i).getCodigoCliente()));
            }

            tamanoListPrecio = listElements.size();
//                auxPropiedad.add(result.get(i));
//                propiedadList.add(new ItemPropiedad(1,R.drawable.btnsearch,"$" + result.get(i).getCosto(), result.get(i).getDescripcion()));
//
////            Log.i("propiedades", String.valueOf(result.get(i).getCosto()));
//            }
//
//            adapterB.notifyDataSetChanged();

            elementsPropiedadArrayAdapter = new ItemPropiedadBusquedaAdapter(BusquedaPropiedades.this,listElements);
            listPropiedades.setAdapter(elementsPropiedadArrayAdapter);
            elementsPropiedadArrayAdapter.notifyDataSetChanged();
        }
    }


    // Para cargar las propiedades por categoria
    public class JSONTaskPropiedadPorCategoria extends AsyncTask<String,String, List<Propiedad>> {

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
                for(int i =0;i<items.length();i++){//recorremos el obejto jsonArray

                    JSONObject object = items.getJSONObject(i);//se posiciona en el primer objeto

                    String persona=object.getString("persona");
                    JSONObject jsonpersona = new JSONObject(persona);
                    String idpersona = jsonpersona.getString("ec.edu.ups.Model.Persona");

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
                    JSONArray ltsImgArr =new JSONArray(finalJson);//
                    ArrayList<String> ltsImagenes=new ArrayList<>();

                    for(int j =0;j<ltsImgArr.length();j++){
                        JSONObject nombreimg = ltsImgArr.getJSONObject(i);//se posiciona en el primer objeto
                        ltsImagenes.add(nombreimg.getString("imagenes"));

                    }
                    propiedad.setImagenes(ltsImagenes);

                    arrayListPropiedad.add(propiedad);
                    Log.i("propiedades","add list");

                }

                for(int j =0; j<arrayListPropiedad.size();j++){
                    Log.i("propiedades","-->"+arrayListPropiedad.get(j).getDireccion().toString());
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

            List<ItemPropiedadBusqueda> listElements = new ArrayList<ItemPropiedadBusqueda>();

            int imageId = getResources().getIdentifier("home","drawable",getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);

            for(int i = 0;i<result.size();i++){

                auxArrayListPropiedad.add(result.get(i));
                listElements.add(new ItemPropiedadBusqueda(bitmap, "$ "+
                        String.valueOf(result.get(i).getCosto()),
                        result.get(i).getDescripcion()));
//                auxArrayListPropiedad.add(new Propiedad(result.get(i).getCodigo(), result.get(i).getDireccion(), result.get(i).getUrlVideo()
//                ,result.get(i).getDescripcion(),result.get(i).getEstado(),result.get(i).getTipo(), result.get(i).getCosto()
//                ,result.get(i).getLonguitud(),result.get(i).getLatitud(), result.get(i).getCodigoCliente()));
            }

            tamanoListPrecio = listElements.size();
//                auxPropiedad.add(result.get(i));
//                propiedadList.add(new ItemPropiedad(1,R.drawable.btnsearch,"$" + result.get(i).getCosto(), result.get(i).getDescripcion()));
//
////            Log.i("propiedades", String.valueOf(result.get(i).getCosto()));
//            }
//
//            adapterB.notifyDataSetChanged();

            elementsPropiedadArrayAdapter = new ItemPropiedadBusquedaAdapter(BusquedaPropiedades.this,listElements);
            listPropiedades.setAdapter(elementsPropiedadArrayAdapter);
            elementsPropiedadArrayAdapter.notifyDataSetChanged();
        }
    }

    // Para cargar las propiedades por categoria
    public class JSONTaskPropiedadPorSector extends AsyncTask<String,String, List<Propiedad>> {

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
                for(int i =0;i<items.length();i++){//recorremos el obejto jsonArray

                    JSONObject object = items.getJSONObject(i);//se posiciona en el primer objeto

                    String persona=object.getString("persona");
                    JSONObject jsonpersona = new JSONObject(persona);
                    String idpersona = jsonpersona.getString("ec.edu.ups.Model.Persona");

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
                    JSONArray ltsImgArr =new JSONArray(finalJson);//
                    ArrayList<String> ltsImagenes=new ArrayList<>();

                    for(int j =0;j<ltsImgArr.length();j++){
                        JSONObject nombreimg = ltsImgArr.getJSONObject(i);//se posiciona en el primer objeto
                        ltsImagenes.add(nombreimg.getString("imagenes"));

                    }
                    propiedad.setImagenes(ltsImagenes);

                    arrayListPropiedad.add(propiedad);
                    Log.i("propiedades","add list");

                }

                for(int j =0; j<arrayListPropiedad.size();j++){
                    Log.i("propiedades","-->"+arrayListPropiedad.get(j).getDireccion().toString());
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

            List<ItemPropiedadBusqueda> listElements = new ArrayList<ItemPropiedadBusqueda>();

            int imageId = getResources().getIdentifier("home","drawable",getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);

            for(int i = 0;i<result.size();i++){

                auxArrayListPropiedad.add(result.get(i));
                listElements.add(new ItemPropiedadBusqueda(bitmap, "$ "+
                        String.valueOf(result.get(i).getCosto()),
                        result.get(i).getDescripcion()));
//                auxArrayListPropiedad.add(new Propiedad(result.get(i).getCodigo(), result.get(i).getDireccion(), result.get(i).getUrlVideo()
//                ,result.get(i).getDescripcion(),result.get(i).getEstado(),result.get(i).getTipo(), result.get(i).getCosto()
//                ,result.get(i).getLonguitud(),result.get(i).getLatitud(), result.get(i).getCodigoCliente()));
            }

            tamanoListPrecio = listElements.size();
//                auxPropiedad.add(result.get(i));
//                propiedadList.add(new ItemPropiedad(1,R.drawable.btnsearch,"$" + result.get(i).getCosto(), result.get(i).getDescripcion()));
//
////            Log.i("propiedades", String.valueOf(result.get(i).getCosto()));
//            }
//
//            adapterB.notifyDataSetChanged();

            elementsPropiedadArrayAdapter = new ItemPropiedadBusquedaAdapter(BusquedaPropiedades.this,listElements);
            listPropiedades.setAdapter(elementsPropiedadArrayAdapter);
            elementsPropiedadArrayAdapter.notifyDataSetChanged();
        }
    }

}
