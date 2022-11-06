package com.codeandcoke.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<Place> places;
    private ArrayAdapter<Place> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillPlaces();

        ListView lvPlaces = findViewById(R.id.lvPlaces);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);
        lvPlaces.setAdapter(adapter);
        lvPlaces.setOnItemClickListener(this);
    }

    private void fillPlaces() {
        places = new ArrayList<>();
        places.add(new Place("Hotel 1", "Hotel", 100, 41.653574, -0.880474));
        places.add(new Place("Hotel 2", "Hotel", 50, 41.653574, -0.850474));
        places.add(new Place("Restaurante 1", "Restaurante", 10, 41.653574, -0.980474));
        places.add(new Place("Restaurante 2", "Restaurante", 12, 41.653574, -0.8780474));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_map:
                // Mostrar todas las ubicaciones en el mapa
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("action", "show_places");
                intent.putParcelableArrayListExtra("places", (ArrayList) places);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Place place = places.get(position);

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("action", "show_place");
        intent.putExtra("place", place);
        startActivity(intent);
    }
}
