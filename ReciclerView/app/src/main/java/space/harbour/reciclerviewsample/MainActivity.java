package space.harbour.reciclerviewsample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecylerView sample
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Superhero> superheroList;
    private SuperheroAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateSuperheroList();

        RecyclerView recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SuperheroAdapter(superheroList);
        recyclerView.setAdapter(adapter);

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(this);
    }

    private void populateSuperheroList() {
        superheroList = new ArrayList<>();
        superheroList.add(new Superhero("Clark", "Kent", "Superman"));
        superheroList.add(new Superhero("Peter", "Parker", "Spiderman"));
        superheroList.add(new Superhero("Bruce", "Wayne", "Batman"));
    }

    @Override
    public void onClick(View view) {
        // Get selected hero from the list
        Superhero selectedCharacter = adapter.getSelectedSuperhero();
        Toast.makeText(this, selectedCharacter.getFullName(), Toast.LENGTH_SHORT).show();
    }
}
