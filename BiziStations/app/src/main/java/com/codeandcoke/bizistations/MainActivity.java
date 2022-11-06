package com.codeandcoke.bizistations;

import static com.codeandcoke.bizistations.util.Constants.SERVICE_URL;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codeandcoke.bizistations.adapter.BiziStationAdapter;
import com.codeandcoke.bizistations.domain.BiziStation;
import com.codeandcoke.bizistations.util.ServiceUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<BiziStation> stations;
    private BiziStationAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stations = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BiziStationAdapter(this, stations);
        recyclerView.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this::loadStationsData);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadStationsData();
    }

    private void loadStationsData() {
        DataTask dataTask = new DataTask();
        dataTask.execute();
    }

    private class DataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String stringResponse = ServiceUtils.getResponseAsString(SERVICE_URL);

                JsonObject jsonResponse = (JsonObject) JsonParser.parseString(stringResponse);
                JsonArray jsonArrayStations = jsonResponse.getAsJsonArray("result");
                if (jsonArrayStations == null) {
                    Toast.makeText(getApplicationContext(), "No se ha podido conectar con el servicio de datos",
                            Toast.LENGTH_SHORT).show();
                    return null;
                }

                for (JsonElement jsonStation : jsonArrayStations) {
                    BiziStation station = BiziStation.from(jsonStation);
                    addStation(station);
                    Log.d("BiziStations", station.getStreet());
                }

                return null;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            stations.clear();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void addStation(BiziStation station) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getBoolean("no_empty_stations", false)) {
            if (station.getAvailableBikes() > 0)
                stations.add(station);
        } else {
            stations.add(station);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemMap:
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("showAllStations", true);
                startActivity(intent);
                return true;
            case R.id.itemSettings:
                intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
