package com.example.vehicledriver.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.vehicledriver.R;
import com.example.vehicledriver.service.GPS_Service;
import com.example.vehicledriver.utils.GlobalPref;


public class MainActivity extends AppCompatActivity {

    String timer[] = {"Select time", "5 sec", "10 sec", "15 sec", "20 sec", "30 sec"};
    String tim;
    AppCompatButton mLocationBtn;
    AppCompatButton stopBtn;
    TextView latitude;
    TextView longitude;
    GPS_Service gps;
    BroadcastReceiver broadcastReceiver;
    ImageView settings;
    LinearLayout latLngLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setKeys();
        try {
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.hide();
        } catch (Exception e) {

        }
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                latLngLayout.setVisibility(View.VISIBLE);
                String lat = intent.getExtras().get("latitude").toString();
                String lng = intent.getExtras().get("longitude").toString();
                latitude.setText(lat);
                longitude.setText(lng);
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("location.intent.vehicle"));
        settings = findViewById(R.id.settingsBtn);
        latitude = findViewById(R.id.latitudeTxt);
        longitude = findViewById(R.id.longitudeTxt);
        Spinner mSpinTime = (Spinner) findViewById(R.id.spinner_time);
        mLocationBtn = findViewById(R.id.location_btn);
        stopBtn = findViewById(R.id.stopBtn);
        latLngLayout = findViewById(R.id.latLngLayout);

        latLngLayout.setVisibility(View.GONE);
//      permission check

        mLocationBtn.setOnClickListener(view -> {
            if (!tim.equals("Select time")) {
                runtime_permission();
            } else {
                Toast.makeText(MainActivity.this, "Select a duration", Toast.LENGTH_SHORT).show();

            }

        });


        stopBtn.setOnClickListener(v -> {
            stopService(new Intent(MainActivity.this, GPS_Service.class));
            latLngLayout.setVisibility(View.GONE);
        });

        settings.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        });

        mSpinTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                tim = adapterView.getItemAtPosition(i).toString();
                if (tim.equals("Select time")) {
                    Toast.makeText(MainActivity.this, "Please Select time!", Toast.LENGTH_SHORT).show();
                }
                if (tim == "5 sec") {
                    tim = String.valueOf(tim.charAt(0));
                    Toast.makeText(MainActivity.this, tim + "", Toast.LENGTH_SHORT).show();
                }
                if (tim == "10 sec") {
                    tim = tim.substring(0, 2);
                    Toast.makeText(MainActivity.this, tim + "", Toast.LENGTH_SHORT).show();
                }
                if (tim == "15 sec") {
                    tim = tim.substring(0, 2);
                    Toast.makeText(MainActivity.this, tim + "", Toast.LENGTH_SHORT).show();
                }
                if (tim == "20 sec") {
                    tim = tim.substring(0, 2);
                    Toast.makeText(MainActivity.this, tim + "", Toast.LENGTH_SHORT).show();
                }
                if (tim == "30 sec") {
                    tim = tim.substring(0, 2);
                    Toast.makeText(MainActivity.this, tim + "", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tim = String.valueOf(0);
            }
        });

        ArrayAdapter arrayAdapterCity = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, timer);
        arrayAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinTime.setAdapter(arrayAdapterCity);
    }

    private void setKeys() {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {

        }

    }



    public void startGpsService(){
        gps = new GPS_Service(MainActivity.this, tim);
        startService(new Intent(MainActivity.this, GPS_Service.class));
    }


    private void runtime_permission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }else{
            startGpsService();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startGpsService();
                } else {
                    Toast.makeText(MainActivity.this, "Grant Permission for location", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
