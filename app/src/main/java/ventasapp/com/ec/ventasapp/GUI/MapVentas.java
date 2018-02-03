package ventasapp.com.ec.ventasapp.GUI;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

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

import ventasapp.com.ec.ventasapp.R;

public class MapVentas extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions markerGasOrder = new MarkerOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_ventas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        markerGasOrder.position(new LatLng(MainActivity.propiedadSeleccionada.getLatitud(),MainActivity.propiedadSeleccionada.getLonguitud()));
        markerGasOrder.icon(BitmapDescriptorFactory.fromResource(R.drawable.puntero));
        markerGasOrder.title(MainActivity.propiedadSeleccionada.getDescripcion());
        Marker mymarke = mMap.addMarker(markerGasOrder);

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this,R.raw.google_style);
        mMap.setMapStyle(mapStyleOptions);


        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(new LatLng(MainActivity.propiedadSeleccionada.getLatitud(),MainActivity.propiedadSeleccionada.getLonguitud()), 15);
        mMap.moveCamera(miUbicacion);
        mMap.setMinZoomPreference(12.0f);
        mMap.setMaxZoomPreference(18.0f);
    }
}
