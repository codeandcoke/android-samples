package com.codeandcoke.bizistations;

import static com.codeandcoke.bizistations.util.Constants.SERVICE_URL;
import static com.codeandcoke.bizistations.util.Constants.ZARAGOZA_LOCATION;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.codeandcoke.bizistations.domain.BiziStation;
import com.codeandcoke.bizistations.dto.StationInfoDTO;
import com.codeandcoke.bizistations.task.LoadAllStationTask;
import com.codeandcoke.bizistations.util.ServiceUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.errors.ApiException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private String stationId;
    private double latitude;
    private double longitude;
    private int availableBikes;
    private int availableSlots;
    private StationInfoDTO stationInfo;
    private boolean showAllStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("showAllStations", false)) {
            showAllStations = true;
        } else {
            stationId = intent.getStringExtra("stationId");
            latitude = intent.getDoubleExtra("latitude", 0);
            longitude = intent.getDoubleExtra("longitude", 0);
            availableBikes = intent.getIntExtra("availableBikes", 0);
            availableSlots = intent.getIntExtra("availableSlots", 0);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fabShowInfo = findViewById(R.id.fabShowInfo);
        fabShowInfo.setOnClickListener(this);

        FloatingActionButton fabNearestStation = findViewById(R.id.fabNearestStation);
        fabNearestStation.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setOnMarkerClickListener(this);

        if (showAllStations) {
            LoadAllStationTask loadAllStationTask = new LoadAllStationTask(this);
            loadAllStationTask.execute(SERVICE_URL);
            try {
                List<BiziStation> stations = loadAllStationTask.get();

                map.moveCamera(CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(ZARAGOZA_LOCATION, 12f)));

                drawMarkers(stations);
            } catch (ExecutionException | InterruptedException ee) {
                Toast.makeText(this, "No se han podido obtener los datos", Toast.LENGTH_LONG).show();
            }
        } else {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(stationId)
                    .snippet("Bicis: " + availableBikes + ", Anclajes libres: " + availableSlots);
            map.addMarker(markerOptions);

            map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(latitude, longitude), 14f)));
        }
    }

    private void drawMarkers(List<BiziStation> stations) {
        for (BiziStation station : stations) {
            drawMarker(station);
        }
    }

    private void drawMarker(BiziStation station) {
        String bikeCount = PreferenceManager.getDefaultSharedPreferences(this).getString("show_station_bike_count", "-1");
        if (!bikeCount.isEmpty())
            if (station.getAvailableBikes() < Integer.parseInt(bikeCount))
                return;

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(station.getLatitude(), station.getLongitude()))
                .title(station.getId());
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("station_info", false)) {
            markerOptions.snippet("Bicis: " + station.getAvailableBikes() + ", Anclajes libres: " + station.getAvailableSlots());
        }

        map.addMarker(markerOptions);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabShowInfo:
                loadStationInfo(stationId);
                break;
            case R.id.fabNearestStation:
                try {
                    // TODO ¿Lanzar todo esto en un AsyncTask?
                    // TODO Llevarlo a MainActivity para hacerlo desde alli
                    // TODO Pintar luego ruta en el mapa hacia dicha estación
                    // TODO Conseguir mi ubicación
                    com.google.maps.model.LatLng myLocation = null;
                    List<BiziStation> stations = ServiceUtils.getBiziStations(SERVICE_URL);
                    ServiceUtils.getNearestBiziStation(this, myLocation, stations);
                } catch (ApiException ae) {
                    // TODO Tratar excepciones
                } catch (InterruptedException ie) {
                    // TODO Tratar excepciones
                } catch (IOException ioe) {
                    // TODO Tratar excepciones
                }
            default:
                break;
        }
    }

    private void loadStationInfo(String stationId) {
        String url = SERVICE_URL.replace(".json", "/" + stationId + ".json").replace("\"","");
        DataTask dataTask = new DataTask();
        dataTask.execute(url);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String markerStationId = marker.getTitle();
        loadStationInfo(markerStationId);
        return false;
    }

    private class DataTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            try {
                String stringResponse = ServiceUtils.getResponseAsString(urls[0]);
                JsonObject jsonResponse = (JsonObject) JsonParser.parseString(stringResponse);
                String street = jsonResponse.get("title").getAsString();
                String state = jsonResponse.get("estado").getAsString();
                int availableBikes = Integer.parseInt(jsonResponse.get("bicisDisponibles").getAsString());
                int availableSlots = Integer.parseInt(jsonResponse.get("anclajesDisponibles").getAsString());
                stationInfo = new StationInfoDTO();
                stationInfo.street = street;
                stationInfo.state = state;
                stationInfo.availableBikes = availableBikes;
                stationInfo.availableSlots = availableSlots;

                return null;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressBar progressBar = findViewById(R.id.pbMap);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TextView tvStreet = findViewById(R.id.tvStreet);
            TextView tvState = findViewById(R.id.tvState);
            TextView tvInfo = findViewById(R.id.tvInfo);
            tvStreet.setText(stationInfo.street);
            tvState.setText(stationInfo.state);
            tvInfo.setText("Bicicletas: " + stationInfo.availableBikes + ", Anclajes libres: " + stationInfo.availableSlots);

            ProgressBar progressBar = findViewById(R.id.pbMap);
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
