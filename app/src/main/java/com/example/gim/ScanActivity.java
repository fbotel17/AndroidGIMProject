package com.example.gim;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import android.widget.EditText;
import android.text.InputType;
import android.app.AlertDialog;
import android.widget.Toast;

public class ScanActivity extends AppCompatActivity {
    private TextView textViewResult;
    private OkHttpClient httpClient;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        textViewResult = findViewById(R.id.textViewResult);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        // Initialiser le scanner pour lire les codes Datamatrix
        IntentIntegrator integrator = new IntentIntegrator(this);
        List<String> desiredBarcodeFormats = Arrays.asList("DATA_MATRIX");
        integrator.setDesiredBarcodeFormats(desiredBarcodeFormats);
        integrator.setPrompt("Scan a Datamatrix code");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("ScanActivity", "Scan annulé");
                textViewResult.setText("Scan annulé");
            } else {
                String qrContent = result.getContents();
                Log.d("ScanActivity", "Datamatrix scanné: " + qrContent);

                // Extraire le code PC qui commence par "034" et a exactement 13 chiffres après le "0"
                String pcCode = extractPCCode(qrContent);
                if (pcCode != null) {
                    textViewResult.setText("Code PC: " + pcCode);

                    // Demander à l'utilisateur la quantité
                    showQuantityDialog(pcCode);
                } else {
                    textViewResult.setText("Code PC non trouvé ou invalide");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showQuantityDialog(String pcCode) {
        final EditText quantityInput = new EditText(this);
        quantityInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(this)
                .setTitle("Entrez la quantité")
                .setMessage("Veuillez entrer le nombre de gélules dans la boîte :")
                .setView(quantityInput)
                .setPositiveButton("OK", (dialog, which) -> {
                    String quantityStr = quantityInput.getText().toString();
                    if (!quantityStr.isEmpty()) {
                        int quantity = Integer.parseInt(quantityStr);
                        sendCodeToAPI(pcCode, quantity);
                    } else {
                        textViewResult.setText("Quantité invalide.");
                    }
                })
                .setNegativeButton("Annuler", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private String extractPCCode(String qrContent) {
        // Utiliser une expression régulière pour trouver le code PC avec exactement 13 chiffres après le "0"
        Pattern pattern = Pattern.compile("034\\d{11}");
        Matcher matcher = pattern.matcher(qrContent);

        if (matcher.find()) {
            String pcCode = matcher.group();
            // Retirer le "0" initial
            return pcCode.substring(1);
        }
        return null;
    }

    private void sendCodeToAPI(String pcCode, int quantity) {
        // Récupérer l'ID de l'utilisateur
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            textViewResult.setText("ID utilisateur non trouvé.");
            return;
        }

        // Construire le JSON à envoyer
        JSONObject json = new JSONObject();
        try {
            json.put("cip13", pcCode);
            json.put("quantity", quantity);
            json.put("userId", userId);  // Ajouter l'ID de l'utilisateur au JSON
        } catch (Exception e) {
            e.printStackTrace();
        }

        // URL de l'API
        String apiUrl = "http://10.3.129.109:99/api/ajouter-medicament";

        // Construire la requête
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10)")
                .build();

        // Envoyer la requête de manière asynchrone
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ScanActivity", "Erreur lors de l'envoi à l'API: " + e.getMessage(), e);
                runOnUiThread(() -> textViewResult.setText("Erreur: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("ScanActivity", "Réponse de l'API: " + responseBody);
                    runOnUiThread(() -> textViewResult.setText("Médicament ajouté avec succès"));
                } else {
                    Log.e("ScanActivity", "Échec de l'ajout du médicament");
                    runOnUiThread(() -> textViewResult.setText("Échec de l'ajout du médicament"));
                }
            }
        });
    }
}
