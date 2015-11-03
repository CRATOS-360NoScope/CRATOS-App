package com.example.alexander.cratos;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class Home_Fragment extends Fragment {

    private ToggleButton bluetoothButton;
    Button fireModeButton;
    Button firingLogsButton;

    public void changeToggleButtonOnText(){
        bluetoothButton.setTextOn("Bluetooth Connected");
        bluetoothButton.setChecked(bluetoothButton.isChecked());
    }

    public void toggleToggleButton() {
        bluetoothButton.toggle();
    }

    public Home_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        bluetoothButton = (ToggleButton) rootView.findViewById(R.id.toggleButton);
        if(!((Home_Activity)getActivity()).isBluetoothGood()) {
            bluetoothButton.setEnabled(false);
        }

        bluetoothButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()) {
                    //bluetoothConnect();
                    ((Home_Activity)getActivity()).bluetoothConnect();
                    firingLogsButton.setEnabled(true);
                    fireModeButton.setEnabled(true);
                } else {
                    //stopBluetooth();
                    ((Home_Activity)getActivity()).stopBluetooth();
                    firingLogsButton.setEnabled(false);
                    fireModeButton.setEnabled(false);
                }
            }
        });

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
