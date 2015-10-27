package com.example.alexander.cratos;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A placeholder fragment containing a simple view.
 */
public class Fire_Mode_ActivityFragment extends Fragment {

    Button fireButton;
    ImageButton leftButton, rightButton, upButton, downButton;

    public Fire_Mode_ActivityFragment() {
    }

    private void sendMessage(String message) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("control-click");
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fire__mode, container, false);
        fireButton = (Button) view.findViewById(R.id.fireButton);
        leftButton = (ImageButton) view.findViewById(R.id.leftButton);
        rightButton = (ImageButton) view.findViewById(R.id.rightButton);
        upButton = (ImageButton) view.findViewById(R.id.upButton);
        downButton = (ImageButton) view.findViewById(R.id.downButton);

        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("fire");
            }
        });

        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    sendMessage("left");
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    sendMessage("stop_horizontal");
                    return true;
                }
                return false;
            }
        });

        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    sendMessage("right");
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    sendMessage("stop_horizontal");
                    return true;
                }
                return false;
            }
        });
        upButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    sendMessage("left");
                    return true;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sendMessage("stop_vertical");
                    return true;
                }
                return false;
            }
        });
        downButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    sendMessage("left");
                    return true;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sendMessage("stop_vertical");
                    return true;
                }
                return false;
            }
        });

        return view;
    }
}
