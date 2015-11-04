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

import org.json.JSONException;
import org.json.JSONObject;

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

    public void toggleMenuButtons(boolean on) {
        firingLogsButton.setEnabled(on);
        fireModeButton.setEnabled(on);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        bluetoothButton = (ToggleButton) rootView.findViewById(R.id.toggleButton);
        if(((CratosBaseApplication) getActivity().getApplication()).getBt().getBluetoothAdapter() == null) {
            bluetoothButton.setEnabled(false);
        }

        bluetoothButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()) {
                    //bluetoothConnect();
                    ((Home_Activity)getActivity()).bluetoothConnect();
                } else {
                    //stopBluetooth();
                    ((Home_Activity)getActivity()).stopBluetooth();
                    toggleMenuButtons(false);
                }
            }
        });

        fireModeButton = (Button)rootView.findViewById(R.id.btnFireMode);
        fireModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch to other_firing_mode to see/test it.
                Intent intent = new Intent(getActivity(), Fire_Mode_Activity.class);
                startActivity(intent);
            }
        });

        firingLogsButton = (Button)rootView.findViewById(R.id.btnFiringLogs);
        firingLogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonMessageLog;
                try {
                    jsonMessageLog = new JSONObject();
                    jsonMessageLog.put(getString(R.string.command), getString(R.string.log));
                    ((CratosBaseApplication) getActivity().getApplication()).getBt().send(jsonMessageLog.toString(),false);

                    Intent intent = new Intent(getActivity(), Firing_Logs_Activity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }
}
