package com.example.gim;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONObject;

import java.io.IOException;

public class SearchMedicamentActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private TextView textViewResult;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_medicament);

        editTextSearch = findViewById(R.id.editTextSearch);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        textViewResult = findViewById(R.id.textViewResult);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = editTextSearch.getText().toString().trim();
                if (!nom.isEmpty()) {
                    searchPokemon(nom);
                } else {
                    textViewResult.setText("Veuillez entrer un nom.");
                }
            }
        });
    }

    private void searchPokemon(String nom) {
        String url = "https://pokebuildapi.fr/api/v1/pokemon/" + nom.toLowerCase();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> textViewResult.setText("Erreur de connexion."));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        String pokemonName = jsonObject.getString("name");

                        runOnUiThread(() -> textViewResult.setText("Nom du Pokémon : " + pokemonName));
                    } catch (Exception e) {
                        runOnUiThread(() -> textViewResult.setText("Aucun résultat trouvé."));
                    }
                } else {
                    runOnUiThread(() -> textViewResult.setText("Aucun Pokémon trouvé."));
                }
            }
        });
    }
}
