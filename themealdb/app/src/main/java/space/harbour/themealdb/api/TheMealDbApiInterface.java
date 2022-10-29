package space.harbour.themealdb.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import space.harbour.themealdb.domain.CategoryApiList;
import space.harbour.themealdb.domain.MealApiList;

public interface TheMealDbApiInterface {

    @GET("search.php")
    Call<MealApiList> getMealsByName(@Query("s") String name);

    @GET("categories.php")
    Call<CategoryApiList> getAllCategories();
}
