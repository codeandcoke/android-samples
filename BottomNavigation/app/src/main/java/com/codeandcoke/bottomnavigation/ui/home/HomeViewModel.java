package com.codeandcoke.bottomnavigation.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private MutableLiveData<List<String>> categories;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    private void loadCategories() {
        List<String> someCategories = new ArrayList<>();
        someCategories.add("one");
        someCategories.add("two");
        someCategories.add("three");

        categories.setValue(someCategories);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<String>> getCategories() {
        if (categories == null) {
            categories = new MutableLiveData<>();
            loadCategories();
        }

        return categories;
    }
}