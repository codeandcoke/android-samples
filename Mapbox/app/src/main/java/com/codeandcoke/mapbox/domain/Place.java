package com.codeandcoke.mapbox.domain;

import android.os.Parcel;
import android.os.Parcelable;


public class Place implements Parcelable {

    private String name;
    private String type;
    private float price;
    private double latitude;
    private double longitude;

    public Place(String name, String type, float price, double latitude, double longitude) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Place(Parcel in) {
        name = in.readString();
        type = in.readString();
        price = in.readFloat();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public float getPrice() { return price; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public String toString() {
        return name + " (" + type + ")";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeFloat(price);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
