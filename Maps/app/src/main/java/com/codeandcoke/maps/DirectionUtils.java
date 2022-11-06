package com.codeandcoke.maps;

import com.google.maps.model.LatLng;
import java.util.List;

public class DirectionUtils {

    /**
     * Convierte un objeto posición de Google Maps SDK a un objeto posición de Directions API
     * @param position
     * @return
     */
    public static LatLng fromMapsToDirections(com.google.android.gms.maps.model.LatLng position) {
        return new LatLng(position.latitude, position.longitude);
    }

    public static com.google.android.gms.maps.model.LatLng fromDirectionsToMaps(LatLng position) {
        return new com.google.android.gms.maps.model.LatLng(position.lat, position.lng);
    }

    public static com.google.android.gms.maps.model.LatLng[] fromMapsToDirections(List<LatLng> positions) {
        com.google.android.gms.maps.model.LatLng[] convertedPositions = new com.google.android.gms.maps.model.LatLng[positions.size()];

        for (int i = 0; i < positions.size(); i++) {
            convertedPositions[i] = DirectionUtils.fromDirectionsToMaps(positions.get(i));
        }

        return convertedPositions;
    }

}
