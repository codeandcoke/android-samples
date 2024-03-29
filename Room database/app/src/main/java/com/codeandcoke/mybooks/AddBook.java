package com.codeandcoke.mybooks;

import static com.codeandcoke.mybooks.db.Constants.BOOKS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.codeandcoke.mybooks.db.AppDatabase;
import com.codeandcoke.mybooks.model.Book;

public class AddBook extends AppCompatActivity implements View.OnClickListener {

    private boolean modify;
    private String bookTitle;
    private ImageView bookImage;
    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        modify = getIntent().getBooleanExtra("modify", false);
        if (modify) {
            Book book = getIntent().getParcelableExtra("book");
            bookTitle = book.getTitle();
            fillBookData(book);
        }

        Button btSaveBook = findViewById(R.id.save_book_button);
        btSaveBook.setOnClickListener(this);
        bookImage = findViewById(R.id.book_image);
    }

    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri uri = result.getData().getData();
                    bookImage.setImageURI(uri);
                    imageUri = uri.toString();
                }
            }
    );

    public void setImage(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(galleryIntent);
    }

    private void fillBookData(Book book) {
        EditText etTitle = findViewById(R.id.book_title);
        EditText etDescription = findViewById(R.id.book_description);
        EditText etPublisher = findViewById(R.id.book_publisher);
        EditText etCategory = findViewById(R.id.book_category);
        EditText etPageCount = findViewById(R.id.book_page_count);

        etTitle.setText(book.getTitle());
        etDescription.setText(book.getDescription());
        etPublisher.setText(book.getPublisher());
        etCategory.setText(book.getCategory());
        etPageCount.setText(String.valueOf(book.getPageCount()));
    }

    @Override
    public void onClick(View view) {
        EditText etTitle = findViewById(R.id.book_title);
        EditText etDescription = findViewById(R.id.book_description);
        EditText etPublisher = findViewById(R.id.book_publisher);
        EditText etCategory = findViewById(R.id.book_category);
        EditText etPageCount = findViewById(R.id.book_page_count);
        ImageView bookImage = findViewById(R.id.book_image);

        final Book book = new Book();
        book.setTitle(etTitle.getText().toString());
        book.setDescription(etDescription.getText().toString());
        book.setPublisher(etPublisher.getText().toString());
        book.setCategory(etCategory.getText().toString());
        book.setPageCount(Integer.parseInt(etPageCount.getText().toString()));
        book.setImage(imageUri);

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
