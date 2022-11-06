package com.codeandcoke.flexibleui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btProducts = findViewById(R.id.btProducts);
        btProducts.setOnClickListener(this);
        Button btSupermarkets = findViewById(R.id.btSupermarkets);
        btSupermarkets.setOnClickListener(this);
        Button btAdd = findViewById(R.id.btAdd);
        btAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.btProducts:
                fragment = new ProductsFragment();
                break;
            case R.id.btSupermarkets:
                fragment = new SupermarketsFragment();
                break;
            case R.id.btAdd:
                TextView etItem = findViewById(R.id.etItem);
                String item = etItem.getText().toString();
                MyFragment myFragment = (MyFragment) this.fragment;
                myFragment.addItem(item);
                return;
        }

        this.fragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
