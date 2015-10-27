package com.example.alexander.cratos;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;


/**
 * A placeholder fragment containing a simple view.
 */
public class Home_Fragment extends Fragment {

    final static String TAG = "HOME_FRAGMENT";

    Button fireModeButton;
    Button firingLogsButton;

    public Home_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        fireModeButton = (Button)rootView.findViewById(R.id.btnFireMode);
        firingLogsButton = (Button)rootView.findViewById(R.id.btnFiringLogs);

        fireModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch to other_firing_mode to see/test it.
                Intent intent = new Intent(getActivity(), Fire_Mode_Activity.class);
                startActivity(intent);
            }
        });



        return rootView;
    }
}
