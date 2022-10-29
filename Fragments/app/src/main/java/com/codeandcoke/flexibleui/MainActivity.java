package com.codeandcoke.flexibleui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            Fragment1 firstFragment = new Fragment1();
            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        if (view.getId() == R.id.button1) {
            fragment = new Fragment1();
        } else if (view.getId() == R.id.button2) {
            fragment = new Fragment2();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
