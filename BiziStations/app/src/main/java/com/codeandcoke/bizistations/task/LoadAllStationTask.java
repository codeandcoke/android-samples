package com.codeandcoke.bizistations.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.codeandcoke.bizistations.R;
import com.codeandcoke.bizistations.domain.BiziStation;
import com.codeandcoke.bizistations.util.ServiceUtils;

import java.io.IOException;
import java.util.List;


public class LoadAllStationTask extends AsyncTask<String, Void, List<BiziStation>> {

    private Context context;

    public LoadAllStationTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<BiziStation> doInBackground(String... urls) {
        try {
            return ServiceUtils.getBiziStations(urls[0]);
        } catch (IOException ioe) {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ProgressBar progressBar = ((Activity) context).findViewById(R.id.pbMap);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<BiziStation> biziStations) {
        super.onPostExecute(biziStations);

        ProgressBar progressBar = ((Activity) context).findViewById(R.id.pbMap);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
