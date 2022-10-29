package com.codeandcoke.mybooks.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.codeandcoke.mybooks.model.Book;

@Database(entities = {Book.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BookDao bookDao();
}
