package space.harbour.themealdb;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import space.harbour.themealdb.api.TheMealDbApi;
import space.harbour.themealdb.api.TheMealDbApiInterface;
import space.harbour.themealdb.domain.Category;
import space.harbour.themealdb.domain.CategoryApiList;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<Category> categoryAdapter;
    List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryList = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryList);
        ListView listView = findViewById(R.id.categoryList);
        listView.setAdapter(categoryAdapter);

        loadMeals();
    }

    private void loadMeals() {
        TheMealDbApiInterface api = TheMealDbApi.buildInstance();
        Call<CategoryApiList> callProducts = api.getAllCategories();
        callProducts.enqueue(new Callback<CategoryApiList>() {
            @Override
            public void onResponse(Call<CategoryApiList> call, Response<CategoryApiList> response) {
                CategoryApiList categoryApiList = response.body();
                categoryList.addAll(categoryApiList.getCategories());
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CategoryApiList> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
                categoryList.clear();
                categoryAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}