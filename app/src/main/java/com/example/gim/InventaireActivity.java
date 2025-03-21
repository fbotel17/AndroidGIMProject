package com.example.gim;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InventaireActivity extends AppCompatActivity {

    private RecyclerView recyclerViewInventaire;
    private Button buttonRefresh;
    private OkHttpClient client;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private InventaireAdapter inventaireAdapter;
    private static final String TAG = "InventaireActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventaire);

        recyclerViewInventaire = findViewById(R.id.recyclerViewInventaire);
        buttonRefresh = findViewById(R.id.buttonRefresh);

        client = new OkHttpClient();
        gson = new Gson();

        // Initialiser SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Configurer le RecyclerView
        recyclerViewInventaire.setLayoutManager(new LinearLayoutManager(this));

        buttonRefresh.setOnClickListener(v -> fetchInventaire());

        // Fetch inventaire data when the activity is created
        fetchInventaire();
    }

    private void fetchInventaire() {
        String token = sharedPreferences.getString("jwtToken", null);
        if (token == null) {
            Toast.makeText(InventaireActivity.this, "Token non trouvé.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.1.49:99/api/inventaire";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Erreur de connexion: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(InventaireActivity.this, "Erreur de connexion.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d(TAG, "Response Data: " + responseData);
                    Type inventaireType = new TypeToken<List<Inventaire>>(){}.getType();
                    List<Inventaire> inventaireList = gson.fromJson(responseData, inventaireType);

                    runOnUiThread(() -> {
                        inventaireAdapter = new InventaireAdapter(inventaireList);
                        recyclerViewInventaire.setAdapter(inventaireAdapter);
                    });
                } else {
                    Log.e(TAG, "Erreur de réponse: " + response.code() + " " + response.message());
                    runOnUiThread(() -> Toast.makeText(InventaireActivity.this, "Erreur lors de la récupération de l'inventaire.", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
