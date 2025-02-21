package com.example.gim;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class GimActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Charge le layout défini dans res/layout/gim.xml
        setContentView(R.layout.gim);
    }
}
