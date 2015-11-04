package com.example.alexander.cratos;
import android.app.Activity;
import android.app.Application;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class CratosBaseApplication extends Application{
    private BluetoothSPP bt;
    private Activity firingActivity;
    private Activity logsActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        bt = new BluetoothSPP(getApplicationContext());
    }

    public void bluetoothKilled() {
        if(firingActivity != null) {
            firingActivity.finish();
        }
        if(logsActivity != null) {
            logsActivity.finish();
        }
    }

    public void setFiringActivity(Activity activity) {
        firingActivity = activity;
    }

    public void setLogsActivity(Activity activity) {logsActivity = activity; }

    public BluetoothSPP getBt() {
        return bt;
    }


}
