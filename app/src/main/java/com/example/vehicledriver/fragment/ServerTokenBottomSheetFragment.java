package com.example.vehicledriver.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.vehicledriver.R;
import com.example.vehicledriver.utils.GlobalPref;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ServerTokenBottomSheetFragment extends BottomSheetDialogFragment {


    EditText serverKeyEditTxt;
    AppCompatButton serverTokenUpdateBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.bottom_sheet_server, container, false);
        serverKeyEditTxt= view.findViewById(R.id.serverKeyEditTxt);
        serverTokenUpdateBtn= view.findViewById(R.id.serverTokenUpdateBtn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GlobalPref gp=new GlobalPref(getContext());
        serverKeyEditTxt.setText(gp.getServerKey());
        serverTokenUpdateBtn.setOnClickListener(v->{
            gp.setServerKey(serverKeyEditTxt.getText().toString());
            dismiss();
            Toast.makeText(getContext(),"Updated",Toast.LENGTH_SHORT).show();
        });
    }
}
