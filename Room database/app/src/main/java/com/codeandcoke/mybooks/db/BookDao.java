package com.codeandcoke.mybooks.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.codeandcoke.mybooks.model.Book;

import java.util.List;

@Dao
public interface BookDao {

    @Query("SELECT * FROM book")
    List<Book> getAll();

    @Insert
    void insertAll(Book... books);

    @Delete
    void delete(Book book);

    @Update
    void update(Book book);

    @Update
    void updateAll(Book... books);
}
