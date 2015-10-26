package com.example.alexander.cratos;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zerokol.views.JoystickView;
import com.zerokol.views.JoystickView.OnJoystickMoveListener;


/**
 * Created by Dylan on 10/2/2015.
 *
 * The fragment for the turret control. One of two possible files.
 */
public class Other_Firing_Fragment extends Fragment implements TextureView.SurfaceTextureListener {

    /**
     * ThreeState variable
     *
     * Positive:
     *  Horizontal: Right
     *  Vertical: Up
     *
     * Negative:
     *  Horizontal: Left
     *  Vertical: Down
     */
    public enum ThreeState {
        POSITIVE,
        NOTHING,
        NEGATIVE
    }
    private final String up = "up";
    private final String down = "down";
    private final String left = "left";
    private final String right = "right";
    private final String noV = "stop_vertical";
    private final String noH = "stop_horizontal";

    private Button fireButton;
    private ToggleButton bluetoothButton;
    private JoystickView joystickView;
    private TextureView textureView;
    private MediaPlayer myVid;

    ThreeState sending_vertical = ThreeState.NOTHING;
    ThreeState sending_horizontal = ThreeState.NOTHING;

    public Other_Firing_Fragment() {}

    private void sendMessage(String message) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("control-click");
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    public void changeToggleButtonOnText(){
        bluetoothButton.setTextOn("Bluetooth Connected");
        bluetoothButton.setChecked(bluetoothButton.isChecked());
    }

    public void toggleToggleButton() {
        bluetoothButton.toggle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_other_fire, container, false);

        textureView = (TextureView) view.findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);

        fireButton = (Button) view.findViewById(R.id.firingButton);
        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("fire");
            }
        });

        bluetoothButton = (ToggleButton) view.findViewById(R.id.toggleBluetooth);

        bluetoothButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()) {
                    //bluetoothConnect();
                    ((Fire_Mode_Activity)getActivity()).bluetoothConnect();
                } else {
                    //stopBluetooth();
                    ((Fire_Mode_Activity)getActivity()).stopBluetooth();
                }
            }
        });
        joystickView = (JoystickView) view.findViewById(R.id.joystickView);
        joystickView.setOnJoystickMoveListener(new OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                if (power != 0) {
                    //double radians = Math.toRadians(angle);
                    //double vertical = Math.cos(radians) * power;
                    //double horizontal = Math.sin(radians) * power;
                    switch (direction) {
                        case JoystickView.FRONT:
                            if(sending_vertical != ThreeState.POSITIVE) {
                                sending_vertical = ThreeState.POSITIVE;
                                sendMessage(up);
                            }
                            if(sending_horizontal != ThreeState.NOTHING) {
                                sending_horizontal = ThreeState.NOTHING;
                                sendMessage(noH);
                            }
                            break;

                        case JoystickView.FRONT_RIGHT:
                            if(sending_vertical != ThreeState.POSITIVE) {
                                sending_vertical = ThreeState.POSITIVE;
                                sendMessage(up);
                            }
                            if(sending_horizontal != ThreeState.POSITIVE) {
                                sending_horizontal = ThreeState.POSITIVE;
                                sendMessage(right);
                            }
                            break;

                        case JoystickView.RIGHT:
                            if(sending_vertical != ThreeState.NOTHING) {
                                sending_vertical = ThreeState.NOTHING;
                                sendMessage(noV);
                            }
                            if(sending_horizontal != ThreeState.POSITIVE) {
                                sending_horizontal = ThreeState.POSITIVE;
                                sendMessage(right);
                            }
                            break;

                        case JoystickView.RIGHT_BOTTOM:
                            if(sending_vertical != ThreeState.NEGATIVE) {
                                sending_vertical = ThreeState.NEGATIVE;
                                sendMessage(down);
                            }
                            if(sending_horizontal != ThreeState.POSITIVE) {
                                sending_horizontal = ThreeState.POSITIVE;
                                sendMessage(right);
                            }
                            break;

                        case JoystickView.BOTTOM:
                            if(sending_vertical != ThreeState.NEGATIVE) {
                                sending_vertical = ThreeState.NEGATIVE;
                                sendMessage(down);
                            }
                            if(sending_horizontal != ThreeState.NOTHING) {
                                sending_horizontal = ThreeState.NOTHING;
                                sendMessage(noH);
                            }
                            break;

                        case JoystickView.BOTTOM_LEFT:
                            if(sending_vertical != ThreeState.NEGATIVE) {
                                sending_vertical = ThreeState.NEGATIVE;
                                sendMessage(down);
                            }
                            if(sending_horizontal != ThreeState.NEGATIVE) {
                                sending_horizontal = ThreeState.NEGATIVE;
                                sendMessage(left);
                            }
                            break;

                        case JoystickView.LEFT:
                            if(sending_vertical != ThreeState.NOTHING) {
                                sending_vertical = ThreeState.NOTHING;
                                sendMessage(noV);
                            }
                            if(sending_horizontal != ThreeState.NEGATIVE) {
                                sending_horizontal = ThreeState.NEGATIVE;
                                sendMessage(left);
                            }
                            break;

                        case JoystickView.LEFT_FRONT:
                            if(sending_vertical != ThreeState.POSITIVE) {
                                sending_vertical = ThreeState.POSITIVE;
                                sendMessage(up);
                            }
                            if(sending_horizontal != ThreeState.NEGATIVE) {
                                sending_horizontal = ThreeState.NEGATIVE;
                                sendMessage(left);
                            }
                            break;

                        default:
                            if(sending_vertical != ThreeState.NOTHING) {
                                sending_vertical = ThreeState.NOTHING;
                                sendMessage(noV);
                            }
                            if(sending_horizontal != ThreeState.NOTHING) {
                                sending_horizontal = ThreeState.NOTHING;
                                sendMessage(noH);
                            }
                    }
                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
        return view;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface mySurface = new Surface(surface);
        myVid = MediaPlayer.create(this.getActivity(), R.raw.test);
        myVid.setSurface(mySurface);
        myVid.setLooping(true);
        myVid.start();

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //nope
    }
}
