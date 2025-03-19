package com.example.gim;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;


public class ScanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Initialiser le scanner
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("ScanActivity", "Scan annulé");
            } else {
                Log.d("ScanActivity", "Code-barres scanné: " + result.getContents());
                // Vous pouvez utiliser le résultat ici, par exemple, afficher le code-barres ou le transmettre à une autre activité
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
