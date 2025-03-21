package com.example.gim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GimActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gim);

        // Récupération du bouton pour rechercher un médicament
        Button buttonToSearchMedic = findViewById(R.id.buttonToSearchMedic);

        // Ajout du listener pour gérer le clic
        buttonToSearchMedic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création de l'intent pour naviguer vers SearchMedicamentActivity
                Intent intent = new Intent(GimActivity.this, SearchMedicamentActivity.class);
                startActivity(intent);
            }
        });

        // Ajout du bouton pour démarrer le scanner
        Button buttonToScan = findViewById(R.id.buttonToScan);

        buttonToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création de l'intent pour naviguer vers ScanActivity
                Intent intent = new Intent(GimActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        // Ajout du bouton pour accéder à l'inventaire
        Button buttonToInventaire = findViewById(R.id.buttonToInventaire);

        buttonToInventaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création de l'intent pour naviguer vers InventaireActivity
                Intent intent = new Intent(GimActivity.this, InventaireActivity.class);
                startActivity(intent);
            }
        });
    }
}
