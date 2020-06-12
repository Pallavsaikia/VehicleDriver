package com.example.vehicledriver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.vehicledriver.R;
import com.example.vehicledriver.fragment.ServerTokenBottomSheetFragment;
import com.example.vehicledriver.fragment.TokenBottomSheetFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class SettingsActivity extends AppCompatActivity {

    AppCompatButton serverTokenBtn;
    AppCompatButton tokenBtn;
    AppCompatButton qrScannerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        try {
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.hide();
        } catch (Exception e) {

        }

        serverTokenBtn = findViewById(R.id.serverTokenBtn);
        tokenBtn = findViewById(R.id.tokenBtn);
        qrScannerBtn = findViewById(R.id.qrScannerBtn);

        serverTokenBtn.setOnClickListener(v -> {
            ServerTokenBottomSheetFragment s = new ServerTokenBottomSheetFragment();
            s.show(getSupportFragmentManager(), "server");
        });

        tokenBtn.setOnClickListener(v -> {
            TokenBottomSheetFragment s = new TokenBottomSheetFragment();
            s.show(getSupportFragmentManager(), "token");
        });

        qrScannerBtn.setOnClickListener(v -> {
            new IntentIntegrator(this).initiateScan();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
            } else {
                Log.d("Asd", result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
