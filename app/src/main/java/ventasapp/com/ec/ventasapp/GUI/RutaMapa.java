package ventasapp.com.ec.ventasapp.GUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ventasapp.com.ec.ventasapp.R;
import ventasapp.com.ec.ventasapp.utilidades.DirectionsJSONParser;

public class RutaMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Polyline line;
    private ArrayList   <LatLng> markerPoints = new ArrayList<LatLng>();

    private ArrayList<LatLng> puntos = new ArrayList<LatLng>();
    private List<Integer> distancias = new ArrayList<>();
    private MarkerOptions markerGasOrder = new MarkerOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       // dibujarRuta();


    }

    public ArrayList<LatLng> getUbicaciones(ArrayList<LatLng> point,List<Integer> dis){

        boolean flag = true;
        int temp;
        LatLng templalo;
        while(flag){
            flag = false;
            for(int i = 0;i<dis.size()-1;i++){

                if(dis.get(i)>dis.get(i+1)){
                    temp = dis.get(i);
                    templalo = point.get(i);

                    dis.set(i,dis.get(i+1));
                    point.set(i,point.get(i+1));

                    dis.set(i+1,temp);
                    point.set(i+1,templalo);

                    flag = true;


                }
            }
        }

        Log.i("nuevo","-------------------------------");
        for (int i = 0;i<dis.size();i++){
            Log.i("nuevo", String.valueOf(dis.get(i)));
        }

        return point;

    }



    public void dibujarRuta(){
        puntos.add(new LatLng(MainActivity.orderLocation.getLatitude(),MainActivity.orderLocation.getLongitude()));

        for(int i = 0;i<BusquedaPropiedades.listadoPropiedadesDirecciones.size();i++){

            puntos.add(new LatLng(BusquedaPropiedades.listadoPropiedadesDirecciones.get(i).getLatitud(),BusquedaPropiedades.listadoPropiedadesDirecciones.get(i).getLonguitud()));

        }

        distancias.add(0);
        for (int i = 1;i<puntos.size();i++){
            distancias.add((int) getDistance(puntos.get(0),puntos.get(i)));
        }

        ArrayList<LatLng> rutas = getUbicaciones(puntos,distancias);

        for(int i = 0;i<rutas.size();i++){

            if(i == 0){

                markerGasOrder.position(rutas.get(i));
                markerGasOrder.icon(BitmapDescriptorFactory.fromResource(R.drawable.punteroinicio));
                markerGasOrder.title("Mi ubicación");
                Marker mymarke = mMap.addMarker(markerGasOrder);

            }else{

                markerGasOrder.position(rutas.get(i));
                markerGasOrder.icon(BitmapDescriptorFactory.fromResource(R.drawable.punteroruta));
                markerGasOrder.title(i+"."+" "+BusquedaPropiedades.listadoPropiedadesDirecciones.get(i-1).getDescripcion());
                Marker mymarke = mMap.addMarker(markerGasOrder);

            }



        }

        for(int i = 0;i<rutas.size() - 1;i++){

            String url = getDirectionsUrl(rutas.get(i), rutas.get(i+1));
            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);


        }






    }


    public float getDistance(LatLng start,LatLng end){

        Location locationA = new Location("Start");
        locationA.setLatitude(start.latitude);
        locationA.setLongitude(start.longitude);
        Location locationB = new Location("End");
        locationB.setLatitude(end.latitude);
        locationB.setLongitude(end.longitude);

        return locationA.distanceTo(locationB);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this,R.raw.google_style);
        mMap.setMapStyle(mapStyleOptions);

        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(new LatLng(MainActivity.orderLocation.getLatitude(),MainActivity.orderLocation.getLongitude()), 13);
        mMap.moveCamera(miUbicacion);
        mMap.setMinZoomPreference(12.0f);
        mMap.setMaxZoomPreference(18.0f);


        dibujarRuta();


    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(16);
                lineOptions.color(Color.argb(100,0,128,255));
                //Log.e(LOGTAG, "Color " + Color.CYAN);
            }

            // Drawing polyline in the Google Map for the i-th route

            if(lineOptions!=null){
                if(line!=null) {
                    //line.remove();
                }

                line = mMap.addPolyline(lineOptions);

            }
            //ejecutarRuta=false;
            /*
            try {

            }catch (Exception e){
                ejecutarRuta=false;
                e.printStackTrace();
            }
            */
        }
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
            Log.d("Exception", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        // Waypoints
        String waypoints = "";
        for(int i=2;i<markerPoints.size();i++){
            LatLng point  = markerPoints.get(i);
            if(i==2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }

}
