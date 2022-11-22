package com.codeandcoke.mapbox;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codeandcoke.mapbox.domain.Place;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements Style.OnStyleLoaded {

    private MapView mapView;
    private String action;
    private Place currentPlace;
    private List<Place> allPlaces;
    private AnnotationConfig annotationConfig;
    private PointAnnotationManager pointAnnotationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if (action.equals("show_place")) {
            currentPlace = intent.getParcelableExtra("place");
        } else if (action.equals("show_places")) {
            allPlaces = intent.getParcelableArrayListExtra("places");
        }

        mapView = findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, this);

        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        annotationConfig = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);

        setMarkerClickListener();
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
        if (action.equals("show_place")) {
            // Shows one place
            addMarker(currentPlace);
            setCameraPosition(currentPlace.getLatitude(), currentPlace.getLongitude());
        } else if (action.equals("show_places")) {
            // Show all places
            for (Place place : allPlaces) {
                addMarker(place);
            }
            Place firstPlace = allPlaces.get(0);
            setCameraPosition(firstPlace.getLatitude(), firstPlace.getLongitude());
        }
    }

    private void addMarker(Place place) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(Point.fromLngLat(place.getLongitude(), place.getLatitude()))
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_marker))
                .withTextField(place.getName());
        pointAnnotationManager.create(pointAnnotationOptions);
        Toast.makeText(this, " " + pointAnnotationManager.getAnnotations().size(), Toast.LENGTH_LONG).show();
    }

    private void setCameraPosition(double latitude, double longitude) {
        CameraOptions cameraPosition = new CameraOptions.Builder()
                .center(Point.fromLngLat(longitude, latitude))
                .pitch(45.0)
                .zoom(15.5)
                .bearing(-17.6)
                .build();
        mapView.getMapboxMap().setCamera(cameraPosition);
    }

    private void removeAllMarkers() {
        pointAnnotationManager.deleteAll();
    }

    private void setMarkerClickListener() {
        pointAnnotationManager.addClickListener((pointAnnotation) -> {
            Toast.makeText(this, "Click", Toast.LENGTH_LONG).show();
            return true;
        });
    }

    public void removeAllMarkers(View view) {
        removeAllMarkers();
        Toast.makeText(this, "Markers eliminados", Toast.LENGTH_LONG).show();
    }
}
