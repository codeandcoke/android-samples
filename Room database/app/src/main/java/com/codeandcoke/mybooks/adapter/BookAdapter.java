package com.codeandcoke.mybooks.adapter;

import static com.codeandcoke.mybooks.db.Constants.BOOKS;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.codeandcoke.mybooks.BookDetailActivity;
import com.codeandcoke.mybooks.R;
import com.codeandcoke.mybooks.db.AppDatabase;
import com.codeandcoke.mybooks.model.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.TaskHolder> {

    private List<Book> books;

    public BookAdapter(List<Book> tasks) {
        this.books = tasks;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.TaskHolder holder, int position) {
        Book book = books.get(position);

        if (book.getImage() != null) {
            holder.ivImage.setImageURI(Uri.parse(book.getImage()));
        }
        holder.tvTitle.setText(book.getTitle());
        holder.tvDescription.setText(book.getDescription());
        if (book.isRead()) {
            holder.doButton.setText("Leído");
            holder.parentView.setBackgroundColor(holder.parentView.getContext().getResources().getColor(android.R.color.holo_red_light));
        }
        else
            holder.doButton.setText("Sin leer");
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        public ImageView ivImage;
        public TextView tvTitle;
        public TextView tvDescription;
        public Button doButton;
        public Button detailsButton;
        public View parentView;

        public TaskHolder(@NonNull View view) {
            super(view);
            parentView = view;

            ivImage = view.findViewById(R.id.book_item_image);
            tvTitle = view.findViewById(R.id.book_item_title);
            tvDescription = view.findViewById(R.id.book_item_description);
            doButton = view.findViewById(R.id.read_item_button);
            detailsButton = view.findViewById(R.id.details_item_button);

            doButton.setOnClickListener(v -> doTask(view));
            detailsButton.setOnClickListener(v -> goToTaskDetails(view));
        }

        private void doTask(View itemView) {
            int currentPosition = getAdapterPosition();
            Book book = books.get(currentPosition);
            book.setRead(!book.isRead());
            AppDatabase db = Room.databaseBuilder(itemView.getContext(), AppDatabase.class, BOOKS).allowMainThreadQueries().build();
            db.bookDao().update(book);
            if (book.isRead()) {
                doButton.setText("Reset");
                parentView.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_light));
            }
            else {
                doButton.setText("Leer");
                parentView.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.white));
            }
        }

        private void goToTaskDetails(View itemView) {
            Intent intent = new Intent(itemView.getContext(), BookDetailActivity.class);
            Book task = books.get(getBindingAdapterPosition());
            intent.putExtra("book_title", task.getTitle());
            itemView.getContext().startActivity(intent);
        }
    }
}
