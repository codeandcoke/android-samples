package space.harbour.reciclerviewsample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Superhero Adapter to use with a RecyclerView
 */
public class SuperheroAdapter extends RecyclerView.Adapter<SuperheroAdapter.SuperheroHolder> {
    private List<Superhero> dataList;
    private int selectedPosition;

    public SuperheroAdapter(List<Superhero> dataList) {
        this.dataList = dataList;
        selectedPosition = -1;
    }

    @Override
    public SuperheroHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.character_item, parent, false);
        return new SuperheroHolder(view);
    }

    @Override
    public void onBindViewHolder(SuperheroHolder holder, int position) {
        holder.tvFullName.setText(dataList.get(position).getFullName());
        holder.tvSuperheroName.setText(dataList.get(position).getSuperHeroeName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class SuperheroHolder extends RecyclerView.ViewHolder {
        public TextView tvFullName;
        public TextView tvSuperheroName;
        public Button button;
        public View parentView;

        public SuperheroHolder(View view) {
            super(view);
            parentView = view;

            tvFullName = view.findViewById(R.id.tvFullName);
            tvSuperheroName = view.findViewById(R.id.tvSuperheroName);
            button = view.findViewById(R.id.button);

            // Click on superhero (select/unselect)
            view.setOnClickListener(view1 -> selectSuperhero(parentView, view1, getAdapterPosition()));
            // Click on button (remove superhero from the list)
            button.setOnClickListener(view12 -> deleteSuperhero(getAdapterPosition()));
        }
    }

    private void selectSuperhero(View parentView, View view, int position) {
        // Select / Unselect
        if (getSelectedPosition() != position) {
            // Only single selection is allowed
            if (selectedPosition != -1)
                return;

            parentView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.holo_blue_light));
            selectedPosition = position;
            // FIXME eliminar estos Toasts
            Toast.makeText(view.getContext(), "Superhero selected. Position = " + position, Toast.LENGTH_SHORT).show();
        } else {
            parentView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.white));
            selectedPosition = -1;
            // FIXME eliminar estos Toasts
            Toast.makeText(view.getContext(), "Superhero deselected. Position = " + position, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteSuperhero(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public Superhero getSelectedSuperhero() {
        if (getSelectedPosition() == -1)
            return null;

        return dataList.get(getSelectedPosition());
    }
}

