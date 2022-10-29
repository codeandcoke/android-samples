package com.codeandcoke.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonPreferences = findViewById(R.id.buttonPreferences);
        buttonPreferences.setOnClickListener(this);
        Button buttonDoSomething = findViewById(R.id.checkPreferencesButton);
        buttonDoSomething.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonPreferences) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.checkPreferencesButton) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if (prefs.getBoolean("notifications", false))
                Toast.makeText(this, "Notifications are enabled", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Notifications are disabled", Toast.LENGTH_LONG).show();
        }
    }
}
