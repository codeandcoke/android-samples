package com.codeandcoke.bizistations.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.codeandcoke.bizistations.domain.BiziStation;

import java.util.List;

@Dao
public interface BiziStationDao {

    @Query("SELECT * FROM bizistation")
    List<BiziStation> getAll();

    @Query("SELECT * FROM bizistation WHERE id = :id")
    BiziStation getStation(String id);

    @Insert
    void insert(BiziStation station);

    @Delete
    void delete(BiziStation station);
}
