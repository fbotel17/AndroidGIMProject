package com.example.gim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextIdentifier;
    private EditText editTextPassword;
    private TextView textViewError;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextIdentifier = findViewById(R.id.editTextIdentifier);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewError = findViewById(R.id.textViewError);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String identifier = editTextIdentifier.getText().toString();
                String password = editTextPassword.getText().toString();

                if (identifier.isEmpty() || password.isEmpty()) {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Veuillez remplir tous les champs.");
                } else {
                    login(identifier, password);
                }
            }
        });

        TextView textViewRegister = findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void login(String identifier, String password) {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        try {
            json.put("identifier", identifier);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URL + "/api/login")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText("Erreur de connexion. Veuillez réessayer.");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String token = jsonResponse.getString("token");

                        // Stocker le JWT
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("jwtToken", token);
                        editor.apply();

                        // Rediriger vers GimActivity
                        Intent intent = new Intent(LoginActivity.this, GimActivity.class);
                        startActivity(intent);
                        finish(); // Fermer LoginActivity

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewError.setVisibility(View.VISIBLE);
                            textViewError.setText("Identifiants invalides.");
                        }
                    });
                }
            }
        });
    }

}
