package com.example.gim;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Calendar;
import android.content.SharedPreferences;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNom, editTextPrenom, editTextUsername, editTextEmail, editTextPassword, editTextDateNaissance;
    private TextView textViewError;
    private Calendar calendar;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialisation des vues
        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextDateNaissance = findViewById(R.id.editTextDateNaissance);
        textViewError = findViewById(R.id.textViewError);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        calendar = Calendar.getInstance();

        // Affichage du DatePicker lorsqu'on clique sur le champ de date
        editTextDateNaissance.setOnClickListener(v -> showDatePicker());

        // Gestion du clic sur le bouton d'inscription
        buttonRegister.setOnClickListener(v -> {
            String nom = editTextNom.getText().toString().trim();
            String prenom = editTextPrenom.getText().toString().trim();
            String username = editTextUsername.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String dateNaissance = editTextDateNaissance.getText().toString().trim();

            // Validation des champs
            if (nom.isEmpty() || prenom.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || dateNaissance.isEmpty()) {
                Log.d(TAG, "Champs vides détectés");
                showError("Veuillez remplir tous les champs.");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Log.d(TAG, "Email invalide détecté");
                showError("L'email fourni est invalide.");
            } else {
                Log.d(TAG, "Tentative d'inscription");
                register(nom, prenom, username, email, password, dateNaissance);
            }
        });
    }

    // Méthode pour afficher le DatePicker
    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                    editTextDateNaissance.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Méthode pour afficher les messages d'erreur
    private void showError(String message) {
        Log.d(TAG, "Affichage de l'erreur: " + message);
        textViewError.setVisibility(View.VISIBLE);
        textViewError.setText(message);
    }

    // Méthode pour enregistrer l'utilisateur
    private void register(String nom, String prenom, String username, String email, String password, String dateNaissance) {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        try {
            json.put("nom", nom);
            json.put("prenom", prenom);
            json.put("username", username);
            json.put("email", email);
            json.put("password", password);
            json.put("dateNaissance", dateNaissance);
        } catch (JSONException e) {
            e.printStackTrace();
            showError("Erreur de traitement des données.");
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("http://192.168.1.49:99/api/register")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> showError("Erreur de connexion. Veuillez réessayer."));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();

                try {
                    JSONObject jsonResponse = new JSONObject(responseData);

                    if (response.isSuccessful()) {
                        if (jsonResponse.has("token")) {
                            String token = jsonResponse.getString("token");

                            // Stocker le JWT dans SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("jwtToken", token);
                            editor.apply();

                            runOnUiThread(() -> {
                                Toast.makeText(RegisterActivity.this, "Inscription réussie !", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, GimActivity.class);
                                startActivity(intent);
                                finish();
                            });
                        }
                    } else {
                        String errorMessage = jsonResponse.has("error") ? jsonResponse.getString("error") : "Erreur inconnue.";
                        runOnUiThread(() -> showError(errorMessage));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> showError("Erreur de traitement des données."));
                }
            }
        });
    }
}
