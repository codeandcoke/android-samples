package com.codeandcoke.bizistations.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.codeandcoke.bizistations.domain.BiziStation;

@Database(entities = {BiziStation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BiziStationDao biziStationDao();
}
