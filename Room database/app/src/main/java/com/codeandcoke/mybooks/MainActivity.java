package com.codeandcoke.mybooks;

import static com.codeandcoke.mybooks.db.Constants.BOOKS;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.codeandcoke.mybooks.db.AppDatabase;
import com.codeandcoke.mybooks.model.Book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<Book> booksAdapter;
    private List<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvBooks = findViewById(R.id.lvBooks);
        books = new ArrayList<>();
        booksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, books);
        lvBooks.setAdapter(booksAdapter);
        registerForContextMenu(lvBooks);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, BOOKS).allowMainThreadQueries().build();
        books.clear();
        books.addAll(db.bookDao().getAll());
        booksAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getApplicationContext(), "TextChange: " + newText, Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), "TextSbumit: " + query, Toast.LENGTH_LONG).show();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_book) {
            Intent intent = new Intent(this, AddBook.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.search) {
            // FIXME Not finished
            SearchView searchView = (SearchView) item.getActionView();
            Toast.makeText(this, "prueba", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = menuInfo.position;
        final Book book = books.get(position);

        if (item.getItemId() == R.id.recommend_book) {
            // TODO Recommend a book
            return true;
        } else if (item.getItemId() == R.id.modify_book) {
            modifyBook(book);
            return true;
        } else if (item.getItemId() == R.id.delete_book) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.are_you_sure)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        deleteBook(book);
                        // FIXME Refresco rápido
                        books.remove(position);
                        booksAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();

            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void modifyBook(Book book) {
        Intent intent = new Intent(this, AddBook.class);
        intent.putExtra("modify", true);
        intent.putExtra("book", book);
        startActivity(intent);
    }

    private void deleteBook(Book book) {
        final AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, BOOKS)
                .allowMainThreadQueries()
                .build();
        db.bookDao().delete(book);
    }
}
