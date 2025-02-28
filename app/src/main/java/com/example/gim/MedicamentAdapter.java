package com.example.gim;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MedicamentAdapter extends RecyclerView.Adapter<MedicamentAdapter.MedicamentViewHolder> {

    private List<Medicament> medicamentList;

    // Constructeur
    public MedicamentAdapter(List<Medicament> medicamentList) {
        this.medicamentList = medicamentList;
    }

    @Override
    public MedicamentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Crée une vue pour chaque élément
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicament, parent, false);
        return new MedicamentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MedicamentViewHolder holder, int position) {
        Medicament medicament = medicamentList.get(position);
        holder.nomTextView.setText(medicament.getNom());
        holder.formeTextView.setText(medicament.getFormePharmaceutique());
    }

    @Override
    public int getItemCount() {
        return medicamentList.size();
    }

    // Méthode pour mettre à jour les médicaments dans l'adaptateur
    public void updateMedicaments(List<Medicament> newMedicaments) {
        medicamentList.clear();
        medicamentList.addAll(newMedicaments);
        notifyDataSetChanged();
    }

    public static class MedicamentViewHolder extends RecyclerView.ViewHolder {

        public TextView nomTextView;
        public TextView formeTextView;

        public MedicamentViewHolder(View itemView) {
            super(itemView);
            nomTextView = itemView.findViewById(R.id.medicament_nom);
            formeTextView = itemView.findViewById(R.id.medicament_forme);
        }
    }
}
