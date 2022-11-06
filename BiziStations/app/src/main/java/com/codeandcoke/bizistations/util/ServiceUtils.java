package com.codeandcoke.bizistations.util;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.codeandcoke.bizistations.R;
import com.codeandcoke.bizistations.domain.BiziStation;
import com.codeandcoke.bizistations.dto.StationInfoDTO;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServiceUtils {

    public static String getResponseAsString(String url) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = httpClient.newCall(request);
        Response response = call.execute();
        String stringResponse = response.body().string();

        return stringResponse;
    }

    public static List<BiziStation> getBiziStations(String url) throws IOException {
        List<BiziStation> stations = new ArrayList<>();

        String stringResponse = ServiceUtils.getResponseAsString(url);
        JsonObject jsonResponse = (JsonObject) JsonParser.parseString(stringResponse);
        JsonArray jsonArrayStations = jsonResponse.getAsJsonArray("result");
        for (JsonElement jsonStation : jsonArrayStations) {
            BiziStation station = BiziStation.from(jsonStation);
            stations.add(station);
        }

        return stations;
    }

    public static GeoApiContext getGeoContext(Context context) {
        GeoApiContext geoApiContext = new GeoApiContext(); 
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(context.getResources().getString(R.string.google_maps_key))
                .setConnectTimeout(1, TimeUnit.SECONDS) 
                .setReadTimeout(1, TimeUnit.SECONDS) 
                .setWriteTimeout(1, TimeUnit.SECONDS);   
    }

    public static StationInfoDTO getNearestBiziStation(Context context, LatLng myLocation, List<BiziStation> stations)
                                                                throws ApiException, InterruptedException, IOException {
        List<StationInfoDTO> directions = new ArrayList<>();
        for (BiziStation station : stations) {
            DirectionsResult result = DirectionsApi.newRequest(getGeoContext(context))
                    .mode(TravelMode.WALKING)
                    .origin(myLocation)
                    .destination(station.getLocationDireciontsApi())
                    .departureTime(DateTime.now())
                    .await();

            StationInfoDTO stationDTO = StationInfoDTO.from(station);
            stationDTO.distance = result.routes[0].legs[0].distance.inMeters;
            directions.add(stationDTO);
        }

        directions.sort((station1, station2) -> (int) (station2.distance - station1.distance));
        return directions.get(0);
    }
}
