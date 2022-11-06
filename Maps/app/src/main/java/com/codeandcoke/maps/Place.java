package com.codeandcoke.maps;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Place implements Parcelable {

    private String name;
    private String type;
    private float price;
    private LatLng position;

    public Place(String name, String type, float price, double latitude, double longitude) {
        this.name = name;
        this.type = type;
        this.price = price;
        position = new LatLng(latitude, longitude);
    }

    protected Place(Parcel in) {
        name = in.readString();
        type = in.readString();
        price = in.readFloat();
        position = in.readParcelable(LatLng.class.getClassLoader());
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

    public LatLng getPosition() {
        return position;
    }

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
        parcel.writeParcelable(position, i);
    }
}
