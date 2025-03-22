package com.example.gim;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InventaireAdapter extends RecyclerView.Adapter<InventaireAdapter.InventaireViewHolder> {

    private List<Inventaire> inventaireList;
    private OnQuantityChangeListener listener;

    public InventaireAdapter(List<Inventaire> inventaireList, OnQuantityChangeListener listener) {
        this.inventaireList = inventaireList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InventaireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventaire, parent, false);
        return new InventaireViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventaireViewHolder holder, int position) {
        Inventaire item = inventaireList.get(position);
        holder.textViewNomMedicament.setText("Nom: " + item.getMedicament().getNom());
        holder.textViewNombreBoite.setText("Nombre de boîtes: " + item.getNbBoite());
        holder.textViewQuantite.setText("Quantité: " + item.getQuantite());

        holder.buttonPlus.setOnClickListener(v -> listener.onQuantityChange(item.getId(), 1));
        holder.buttonMinus.setOnClickListener(v -> listener.onQuantityChange(item.getId(), -1));
    }

    @Override
    public int getItemCount() {
        return inventaireList.size();
    }

    public static class InventaireViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNomMedicament;
        TextView textViewNombreBoite;
        TextView textViewQuantite;
        Button buttonPlus;
        Button buttonMinus;

        public InventaireViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomMedicament = itemView.findViewById(R.id.textViewNomMedicament);
            textViewNombreBoite = itemView.findViewById(R.id.textViewNombreBoite);
            textViewQuantite = itemView.findViewById(R.id.textViewQuantite);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange(int inventaireId, int change);
    }
}
