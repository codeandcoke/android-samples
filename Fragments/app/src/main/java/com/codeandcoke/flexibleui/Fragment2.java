package com.codeandcoke.flexibleui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class Fragment2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment2, container, false);

        // Get intent arguments (from the parent Activity)
        Bundle arguments = getArguments();

        TextView textView = view.findViewById(R.id.textView2);
        // Check device orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            textView.setText("Landscape");
        }
        else {
            textView.setText("Portrait");
        }

        return view;
    }

}