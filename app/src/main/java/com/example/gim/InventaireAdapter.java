package com.example.gim;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InventaireAdapter extends RecyclerView.Adapter<InventaireAdapter.InventaireViewHolder> {

    private List<Inventaire> inventaireList;

    public InventaireAdapter(List<Inventaire> inventaireList) {
        this.inventaireList = inventaireList;
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
    }

    @Override
    public int getItemCount() {
        return inventaireList.size();
    }

    public static class InventaireViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNomMedicament;
        TextView textViewNombreBoite;
        TextView textViewQuantite;

        public InventaireViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomMedicament = itemView.findViewById(R.id.textViewNomMedicament);
            textViewNombreBoite = itemView.findViewById(R.id.textViewNombreBoite);
            textViewQuantite = itemView.findViewById(R.id.textViewQuantite);
        }
    }
}
