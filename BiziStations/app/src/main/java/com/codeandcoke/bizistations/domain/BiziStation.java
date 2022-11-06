package com.codeandcoke.bizistations.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.maps.model.LatLng;

@Entity
public class BiziStation {

    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo
    private String street;
    @ColumnInfo
    private int slots;
    @ColumnInfo(name = "available_bikes")
    private int availableBikes;
    @ColumnInfo(name = "available_slots")
    private int availableSlots;
    @ColumnInfo
    private double latitude;
    @ColumnInfo
    private double longitude;

    public BiziStation() {

    }

    private BiziStation(String id, String street) {
        this.id = id;
        this.street = street;
        slots = 0;
        availableBikes = 0;
        availableSlots = 0;
    }

    public static BiziStation from(String id, String street) {
        return new BiziStation(id, street);
    }

    public static BiziStation from(JsonElement jsonStation) {
        String stationId = ((JsonObject) jsonStation).get("id").toString();
        String stationStreet = ((JsonObject) jsonStation).get("title").toString();
        int availableBikes = Integer.parseInt(((JsonObject) jsonStation).get("bicisDisponibles").toString());
        int availableSlots = Integer.parseInt(((JsonObject) jsonStation).get("anclajesDisponibles").toString());
        JsonArray jsonCoordinates = ((JsonObject) jsonStation).getAsJsonObject("geometry").getAsJsonArray("coordinates");
        double latitude = jsonCoordinates.get(1).getAsDouble();
        double longitude = jsonCoordinates.get(0).getAsDouble();

        BiziStation station =  BiziStation.from(stationId, stationStreet);
        station.setAvailableBikes(availableBikes);
        station.setAvailableSlots(availableSlots);
        station.setSlots(availableSlots + availableBikes);
        station.setLatitude(latitude);
        station.setLongitude(longitude);

        return station;
    }

    public LatLng getLocationDireciontsApi() {
        return new LatLng(latitude, longitude);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getAvailableBikes() {
        return availableBikes;
    }

    public void setAvailableBikes(int availableBikes) {
        this.availableBikes = availableBikes;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
