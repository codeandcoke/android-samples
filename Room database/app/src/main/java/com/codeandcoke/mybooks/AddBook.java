package com.codeandcoke.mybooks;

import static com.codeandcoke.mybooks.db.Constants.BOOKS;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.codeandcoke.mybooks.db.AppDatabase;
import com.codeandcoke.mybooks.model.Book;

public class AddBook extends AppCompatActivity implements View.OnClickListener {

    private boolean modify;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        modify = getIntent().getBooleanExtra("modify", false);
        if (modify) {
            Book book = getIntent().getParcelableExtra("book");
            bookId = book.getId();
            fillBookData(book);
        }

        Button btSaveBook = findViewById(R.id.btSaveBook);
        btSaveBook.setOnClickListener(this);
    }

    private void fillBookData(Book book) {
        EditText etTitle = findViewById(R.id.etTitle);
        EditText etDescription = findViewById(R.id.etDescription);
        EditText etPublisher = findViewById(R.id.etPublisher);
        EditText etCategory = findViewById(R.id.etCategory);
        EditText etPageCount = findViewById(R.id.etPageCount);

        etTitle.setText(book.getTitle());
        etDescription.setText(book.getDescription());
        etPublisher.setText(book.getPublisher());
        etCategory.setText(book.getCategory());
        etPageCount.setText(String.valueOf(book.getPageCount()));
    }

    @Override
    public void onClick(View view) {
        EditText etTitle = findViewById(R.id.etTitle);
        EditText etDescription = findViewById(R.id.etDescription);
        EditText etPublisher = findViewById(R.id.etPublisher);
        EditText etCategory = findViewById(R.id.etCategory);
        EditText etPageCount = findViewById(R.id.etPageCount);

        final Book book = new Book();
        book.setId(bookId);
        book.setTitle(etTitle.getText().toString());
        book.setDescription(etDescription.getText().toString());
        book.setPublisher(etPublisher.getText().toString());
        book.setCategory(etCategory.getText().toString());
        book.setPageCount(Integer.parseInt(etPageCount.getText().toString()));

        final AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, BOOKS)
                .allowMainThreadQueries()
                .build();
        if (modify) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.are_you_sure)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        db.bookDao().updateAll(book);
                        Toast.makeText(getApplicationContext(), R.string.book_saved, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    })
                    .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
        }
        else {
            db.bookDao().insertAll(book);

            etTitle.setText("");
            etDescription.setText("");
            etPublisher.setText("");
            etCategory.setText("");
            etPageCount.setText("");
            Toast.makeText(this, R.string.book_saved, Toast.LENGTH_SHORT).show();
        }
    }
}
