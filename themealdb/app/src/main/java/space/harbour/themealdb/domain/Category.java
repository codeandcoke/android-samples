package space.harbour.themealdb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private int idCategory;
    private String strCategory;
    private String strCategoryDescription;

    @Override
    public String toString() {
        return strCategory;
    }
}
