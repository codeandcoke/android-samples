package com.svalero.todolist.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey
    private @NonNull String name;
    @ColumnInfo
    private String description;
    @ColumnInfo
    private boolean urgent;
    @ColumnInfo
    private boolean done;
    @ColumnInfo
    private double latitude;
    @ColumnInfo
    private double longitude;

    public Task(String name, String description, boolean urgent, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.urgent = urgent;
        this.done = false;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
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
