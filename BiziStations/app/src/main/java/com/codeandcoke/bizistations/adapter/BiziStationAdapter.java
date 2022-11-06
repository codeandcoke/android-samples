package com.codeandcoke.bizistations.adapter;

import static com.codeandcoke.bizistations.util.Constants.BIZI_STATIONS;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.codeandcoke.bizistations.MapActivity;
import com.codeandcoke.bizistations.R;
import com.codeandcoke.bizistations.db.AppDatabase;
import com.codeandcoke.bizistations.domain.BiziStation;

import java.util.List;

public class BiziStationAdapter extends RecyclerView.Adapter<BiziStationAdapter.MyViewHolder> {

    private Context context;
    private List<BiziStation> dataList;
    private int selectedPosition;

    public BiziStationAdapter(Context context, List<BiziStation> dataList) {
        this.context = context;
        this.dataList = dataList;
        selectedPosition = -1;
    }

    @Override
    public BiziStationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BiziStation station = dataList.get(position);

        holder.tvStreet.setText(station.getStreet());
        int slotCount = station.getSlots();
        int bikeCount = station.getAvailableBikes();
        holder.tvInfo.setText(context.getResources().getString(R.string.info_anclajes, slotCount, bikeCount));

        // Si la estación ya está en base de datos se marca con el icono apagado
        final AppDatabase db = Room.databaseBuilder(holder.parentView.getContext(), AppDatabase.class, BIZI_STATIONS)
                .allowMainThreadQueries().build();
        BiziStation dbStation = db.biziStationDao().getStation(station.getId());
        if (dbStation != null)
            holder.bookmarkButton.setImageResource(android.R.drawable.star_big_on);
        else
            holder.bookmarkButton.setImageResource(android.R.drawable.star_big_off);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvStreet;
        public TextView tvInfo;
        public ImageButton selectButton;
        public ImageButton bookmarkButton;
        public View parentView;

        public MyViewHolder(View view) {
            super(view);
            parentView = view;

            tvStreet = view.findViewById(R.id.tvStreet);
            tvInfo = view.findViewById(R.id.tvState);
            selectButton = view.findViewById(R.id.selectButton);
            bookmarkButton = view.findViewById(R.id.bookmarkButton);

            // Captura evento click sobre cada elemento para seleccionar un item
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Selección / deselección
                    if (getSelectedPosition() != getAdapterPosition()) {
                        // Está otro item seleccionado, no se permite selección múltiple
                        if (selectedPosition != -1)
                            return;

                        parentView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.holo_blue_light));
                        selectedPosition = getAdapterPosition();

                    } else {
                        parentView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.white));
                        selectedPosition = -1;

                    }
                }
            });
            // Captura evento click del botón
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BiziStation station = dataList.get(getAdapterPosition());
                    Intent intent = new Intent(view.getContext(), MapActivity.class);
                    intent.putExtra("stationId", station.getId());
                    intent.putExtra("latitude", station.getLatitude());
                    intent.putExtra("longitude", station.getLongitude());
                    intent.putExtra("availableBikes", station.getAvailableBikes());
                    intent.putExtra("availableSlots", station.getAvailableSlots());
                    view.getContext().startActivity(intent);
                }
            });

            bookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AppDatabase db = Room.databaseBuilder(view.getContext(), AppDatabase.class, BIZI_STATIONS)
                            .allowMainThreadQueries().build();
                    BiziStation station = dataList.get(getAdapterPosition());
                    if (db.biziStationDao().getStation(station.getId()) == null) {
                        db.biziStationDao().insert(station);
                        ((ImageButton) view).setImageResource(android.R.drawable.star_big_on);
                        Toast.makeText(view.getContext(), R.string.station_added_to_bookmarks, Toast.LENGTH_SHORT).show();
                    } else {
                        db.biziStationDao().delete(station);
                        ((ImageButton) view).setImageResource(android.R.drawable.star_big_off);
                        Toast.makeText(view.getContext(), R.string.station_removed_from_bookmarks, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
