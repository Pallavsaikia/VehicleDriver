package com.example.vehicledriver.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;

import com.example.vehicledriver.R;
import com.example.vehicledriver.fragment.ServerTokenBottomSheetFragment;
import com.example.vehicledriver.fragment.TokenBottomSheetFragment;

public class SettingsActivity extends AppCompatActivity {

    AppCompatButton serverTokenBtn;
    AppCompatButton tokenBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        try {
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.hide();
        }catch (Exception e){

        }

        serverTokenBtn=findViewById(R.id.serverTokenBtn);
        tokenBtn=findViewById(R.id.tokenBtn);

        serverTokenBtn.setOnClickListener(v->{
            ServerTokenBottomSheetFragment s= new ServerTokenBottomSheetFragment();
            s.show(getSupportFragmentManager(),"server");
        });

        tokenBtn.setOnClickListener(v->{
            TokenBottomSheetFragment s= new TokenBottomSheetFragment();
            s.show(getSupportFragmentManager(),"token");
        });

    }
}
