package space.harbour.themealdb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meal {

    private String idMeal;
    private String strMeal;

    @Override
    public String toString() {
        return strMeal;
    }
}
