package com.example.alexander.cratos;
import android.app.Activity;
import android.app.Application;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class CratosBaseApplication extends Application{
    private BluetoothSPP bt;
    private Activity firingActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        bt = new BluetoothSPP(getApplicationContext());
    }

    public void bluetoothKilled() {
        if(firingActivity != null)
        firingActivity.finish();
    }

    public void setFiringActivity(Activity activity) {
        firingActivity = activity;
    }

    public BluetoothSPP getBt() {
        return bt;
    }


}
