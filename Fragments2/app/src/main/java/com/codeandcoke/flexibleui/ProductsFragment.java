package com.codeandcoke.flexibleui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class ProductsFragment extends Fragment implements MyFragment {

    private List<String> productsList;
    private ArrayAdapter<String> adapter;

    public ProductsFragment() {
        productsList = new ArrayList<>();
        productsList.add("Patatas");
        productsList.add("Colacao");
        productsList.add("Cereales");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_products, container, false);

        ListView listView = view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productsList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void addItem(String item) {
        productsList.add(item);
        adapter.notifyDataSetChanged();
    }
}
