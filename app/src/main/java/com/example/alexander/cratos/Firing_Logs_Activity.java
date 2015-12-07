package com.example.alexander.cratos;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class Firing_Logs_Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firing_logs);
        ((CratosBaseApplication)getApplication()).setLogsActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((CratosBaseApplication)getApplication()).setFiringActivity(null);
    }

}
