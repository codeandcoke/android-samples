package com.codeandcoke.gps;


import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RouteUtils {

    public static LatLng[] toLatLng(List<Location> locationRoute) {
        LatLng route[] = new LatLng[locationRoute.size()];
        for (int i = 0; i < locationRoute.size(); i++)
            route[i] = toLatLng(locationRoute.get(i));

        return route;
    }

    public static LatLng toLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
