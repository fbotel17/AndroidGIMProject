package com.example.gim;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web); // Assure-toi que le nom correspond bien

        webView = findViewById(R.id.webView);
        // Permettre l'exécution de JavaScript si nécessaire
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Pour que les liens s'ouvrent dans la WebView au lieu du navigateur
        webView.setWebViewClient(new WebViewClient());

        // Charge l'URL de ton application web
        webView.loadUrl("https://faubot.fr/");
    }

    // Pour gérer le retour arrière dans la WebView
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
