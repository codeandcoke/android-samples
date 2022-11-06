package com.codeandcoke.gps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeandcoke.gps.MapsActivity.Status.ROUTE;
import static com.codeandcoke.gps.MapsActivity.Status.STOP;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        LocationListener, View.OnClickListener {

    private GoogleMap map;
    private LocationManager locationManager;
    private LocationProvider locationProvider;

    private List<Location> liveRoute;
    private Map<String, List<Location>> routes;
    public enum Status {
        ROUTE, STOP
    }
    private Status status;
    private boolean traceRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        FloatingActionButton btStartStopRoute = findViewById(R.id.btStartStopRoute);
        btStartStopRoute.setOnClickListener(this);
        FloatingActionButton btShowRoute = findViewById(R.id.btShowRoute);
        btShowRoute.setOnClickListener(this);
        FloatingActionButton btTraceRoute = findViewById(R.id.btTraceRoute);
        btTraceRoute.setOnClickListener(this);

        initializeActivity();
    }

    private void initializeActivity() {
        liveRoute = new ArrayList<>();
        routes = new HashMap<>();
        traceRoute = false;
        status = STOP;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationClickListener(this);

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        }
    }

    private void drawRoute(List<Location> route, boolean clear) {
        if (route == null)
            return;
        if (route.isEmpty())
            return;

        if (clear)
            map.clear();

        map.addPolyline(new PolylineOptions()
                .add(RouteUtils.toLatLng(route)));
    }

    private void drawRoute(List<Location> route) {
        drawRoute(route, true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    @Override
    public void onLocationChanged(Location location) {
        liveRoute.add(location);
        if (traceRoute)
            drawRoute(liveRoute, false);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Se ha restablecido la señal GPS", Toast.LENGTH_SHORT).show();
            if (status == ROUTE)
                resumeRoute();
        }

        Toast.makeText(this, "Se activa el " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Se ha perdido la señal GPS", Toast.LENGTH_SHORT).show();
            if (status == ROUTE)
                pauseRoute();
        }
    }

    /**
     * Inicia la ruta
     */
    private void startRoute() {
        resumeRoute();
        map.animateCamera(CameraUpdateFactory.zoomTo(12));
        Toast.makeText(this, "Iniciando ruta", Toast.LENGTH_SHORT).show();
    }

    /**
     * Continua la ruta donde lo dejó
     */
    private void resumeRoute() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(locationProvider.getName(), 2000, 5, this);
        status = ROUTE;
    }

    /**
     * Detiene la ruta
     */
    private void stopRoute() {
        pauseRoute();
        map.moveCamera(CameraUpdateFactory.zoomTo(9));
        Toast.makeText(this, "Terminando ruta", Toast.LENGTH_SHORT).show();

        routes.put("Nombre de la ruta", liveRoute);
        // TODO Guardar la ruta en base de datos, Internet, . . .
        liveRoute.clear();
    }

    /**
     * Pausa la ruta, para poder continuar más adelante
     */
    private void pauseRoute() {
        locationManager.removeUpdates(this);
        status = STOP;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btStartStopRoute:
                if (status == STOP) {
                    startRoute();
                    ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                } else {
                    stopRoute();
                    ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                }
                break;
            case R.id.btShowRoute:
                drawRoute(liveRoute);
                break;
            case R.id.btTraceRoute:
                traceRoute = !traceRoute;
                if (traceRoute)
                    Toast.makeText(this, "Trazado de ruta activado", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Trazado de ruta desactivado", Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
