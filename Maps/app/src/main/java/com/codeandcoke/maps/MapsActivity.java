package com.codeandcoke.maps;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private Marker originMarker;
    private Marker destinationMarker;
    private static final int[] COLORS = new int[]{Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.show_map);
        mapFragment.getMapAsync(this);

        FloatingActionButton floatingButton = findViewById(R.id.fabToggleMarkers);
        floatingButton.setOnClickListener(this);

        FloatingActionButton fabDirection = findViewById(R.id.fabDirection);
        fabDirection.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_map));
        fabDirection.setOnClickListener(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        showPlaceMarkers();
    }

    private void showPlaceMarkers() {
        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        if (action == null)
            return;

        switch (action) {
            case "show_place":
                Place place = intent.getParcelableExtra("place");
                if (place == null) {
                    Toast.makeText(this, "Error al carga la información del lugar. Vuelva a intentarlo",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                showMarker(place);
                if (originMarker != null)
                    setAddress(originMarker);
                break;
            case "show_places":
                List<Place> placesList = (List) intent.getStringArrayListExtra("places");
                if (placesList == null) {
                    Toast.makeText(this, "Error al cargar la información de los lugares. Vuelva a intentarlo",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Place aPlace : placesList) {
                    showMarker(aPlace);
                }
                break;
            default:
                // El parámetro action tiene un valor no esperado
                break;
        }
    }

    /*
    Muestra un marker en una posición determinada con el nombre y tipo que se pasa como parámetro
     */
    private void showMarker(Place place) {
        map.addMarker(new MarkerOptions()
                .title(place.getName())
                .snippet(place.getType().concat("\n").concat(String.valueOf(place.getPrice())))
                .position(place.getPosition()));
        map.moveCamera(CameraUpdateFactory.zoomTo(12));
        map.moveCamera(CameraUpdateFactory.newLatLng(place.getPosition()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabToggleMarkers:
                Toast.makeText(this, "Selecciona dos puntos origen y destino", Toast.LENGTH_SHORT).show();
                initializeMarkers();
                break;
            case R.id.fabDirection:
                if ((originMarker == null) || (destinationMarker == null)) {
                    Toast.makeText(this, "Selecciona primero origen y destino", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // Calcula la ruta entre los dos markers seleccionados
                    DirectionsResult result = DirectionsApi.newRequest(getGeoContext())
                            .mode(TravelMode.DRIVING)
                            .origin(DirectionUtils.fromMapsToDirections(originMarker.getPosition()))
                            .destination(DirectionUtils.fromMapsToDirections(destinationMarker.getPosition()))
                            .departureTime(DateTime.now())
                            .alternatives(true)
                            .await();
                    // Dibuja una línea a través de los puntos de la ruta calculada
                    dibujarRuta(result, 0);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (ApiException ae) {
                    ae.printStackTrace();

                }
                break;
        }
    }

    private void dibujarRuta(DirectionsResult result, int i) {
        List<LatLng> routePath = result.routes[i].overviewPolyline.decodePath();
        map.addPolyline(new PolylineOptions()
                .add(DirectionUtils.fromMapsToDirections(routePath)));
        Toast.makeText(this, result.routes[i].legs[0].distance.humanReadable, Toast.LENGTH_LONG).show();
        Toast.makeText(this, result.routes[i].legs[0].endAddress, Toast.LENGTH_LONG).show();
        Toast.makeText(this, result.routes[i].legs[0].duration.humanReadable, Toast.LENGTH_LONG).show();
    }

    private void dibujarRutas(DirectionsResult result) {
        for (int i = 0; i < result.routes.length; i++) {
            List<LatLng> routePath = result.routes[i].overviewPolyline.decodePath();
            map.addPolyline(new PolylineOptions()
                    .add(DirectionUtils.fromMapsToDirections(routePath))
                    .color(COLORS[i]));
        }
    }

    private void initializeMarkers() {
        if (originMarker != null)
            originMarker.setAlpha(1);
        if (destinationMarker != null)
            destinationMarker.setAlpha(1);

        originMarker = null;
        destinationMarker = null;
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.google_maps_key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    private void setAddress(Marker marker) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            Address address = geocoder.getFromLocation(marker.getPosition().latitude,
                    marker.getPosition().longitude, 1).get(0);
            marker.setSnippet(address.toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (originMarker == null) {
            originMarker = marker;
            originMarker.setAlpha(0.5f);
        }
        else {
            if (destinationMarker != null)
                destinationMarker.setAlpha(1);

            destinationMarker = marker;
            destinationMarker.setAlpha(0.5f);
        }

        return false;
    }
}
