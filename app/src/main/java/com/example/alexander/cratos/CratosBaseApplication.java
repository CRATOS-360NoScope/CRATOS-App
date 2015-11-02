package com.example.alexander.cratos;
import android.app.Application;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class CratosBaseApplication extends Application{
    private BluetoothSPP bt;

    @Override
    public void onCreate() {
        super.onCreate();
        bt = new BluetoothSPP(getApplicationContext());
    }

    public BluetoothSPP getBt() {
        return bt;
    }


}
