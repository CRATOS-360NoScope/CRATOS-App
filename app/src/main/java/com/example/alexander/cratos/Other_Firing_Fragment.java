package com.example.alexander.cratos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zerokol.views.JoystickView;
import com.zerokol.views.JoystickView.OnJoystickMoveListener;

/**
 * Created by Dylan on 10/2/2015.
 */
public class Other_Firing_Fragment extends Fragment {
    Button fireButton;
    JoystickView joystickView;
    boolean sending_vertical = false;
    boolean sending_horizontal = false;

    public Other_Firing_Fragment() {}

    private void sendMessage(String message) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("control-click");
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_other_fire, container, false);
        fireButton = (Button) view.findViewById(R.id.firingButton);
        joystickView = (JoystickView) view.findViewById(R.id.joystickView);

        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("fire");
            }
        });

        joystickView.setOnJoystickMoveListener(new OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                if (power != 0) {
                    double radians = Math.toRadians(angle);
                    double vertical = Math.cos(radians) * power;
                    double horizontal = Math.sin(radians) * power;
                    switch (direction) {
                        case JoystickView.FRONT:
                            if (!sending_vertical) {
                                if(sending_horizontal) {
                                    sending_horizontal = false;
                                    sendMessage("stop_horizontal");
                                }
                                sendMessage("up");
                                sending_vertical = true;
                            }
                            break;

                        case JoystickView.FRONT_RIGHT:

                            break;

                        case JoystickView.RIGHT:
                            if(!sending_horizontal) {
                                if(sending_vertical) {
                                    sending_vertical = false;
                                    sendMessage("stop_vertical");
                                }
                                sendMessage("right");
                                sending_horizontal = true;
                            }
                            break;

                        case JoystickView.RIGHT_BOTTOM:

                            break;

                        case JoystickView.BOTTOM:
                            if (!sending_vertical) {
                                if(sending_horizontal) {
                                    sending_horizontal = false;
                                    sendMessage("stop_horizontal");
                                }
                                sendMessage("down");
                                sending_vertical = true;
                            }
                            break;

                        case JoystickView.BOTTOM_LEFT:

                            break;

                        case JoystickView.LEFT:
                            if(!sending_horizontal) {
                                if(sending_vertical) {
                                    sending_vertical = false;
                                    sendMessage("stop_vertical");
                                }
                                sendMessage("left");
                                sending_horizontal = true;
                            }
                            break;

                        case JoystickView.LEFT_FRONT:

                            break;
                        default:
                            sending_vertical = false;
                            sending_horizontal = false;
                            sendMessage("stop_horizontal");
                            sendMessage("stop_vertical");
                    }
                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        return view;
    }
}