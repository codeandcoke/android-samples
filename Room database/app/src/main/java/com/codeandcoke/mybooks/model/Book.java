package com.codeandcoke.mybooks.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Book implements Parcelable {

    @PrimaryKey @NonNull
    private String title;
    @ColumnInfo
    private String description;
    @ColumnInfo
    private String publisher;
    @ColumnInfo
    private String category;
    @ColumnInfo(name = "page_count")
    private int pageCount;
    @ColumnInfo
    private String image;
    @ColumnInfo
    private boolean read;

    public Book() {
    }

    protected Book(Parcel in) {
        title = in.readString();
        description = in.readString();
        publisher = in.readString();
        category = in.readString();
        pageCount = in.readInt();
        image = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        if (image != null) {
            return title + " (" + publisher + ") [" + image + "]";
        }

        return title + " (" + publisher + ") [no image]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(publisher);
        parcel.writeString(category);
        parcel.writeInt(pageCount);
        parcel.writeString(image);
    }
}
