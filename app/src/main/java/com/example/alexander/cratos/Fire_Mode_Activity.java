package com.example.alexander.cratos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class Fire_Mode_Activity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_fire__mode);
        ((CratosBaseApplication)getApplication()).setFiringActivity(this);
    }
}
