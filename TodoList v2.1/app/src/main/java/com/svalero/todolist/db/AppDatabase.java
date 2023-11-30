package com.svalero.todolist.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.svalero.todolist.domain.Task;

@Database(entities = {Task.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
