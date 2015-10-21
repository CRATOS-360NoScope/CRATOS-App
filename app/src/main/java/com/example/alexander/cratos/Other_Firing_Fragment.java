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

import com.zerokol.views.JoystickView;
import com.zerokol.views.JoystickView.OnJoystickMoveListener;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Dylan on 10/2/2015.
 * <p/>
 * The fragment for the turret control. One of two possible files.
 */
public class Other_Firing_Fragment extends Fragment implements TextureView.SurfaceTextureListener {

    /**
     * ThreeState variable
     * <p/>
     * Positive:
     * Horizontal: Right
     * Vertical: Up
     * <p/>
     * Negative:
     * Horizontal: Left
     * Vertical: Down
     */
    public enum ThreeState {
        POSITIVE,
        NOTHING,
        NEGATIVE
    }

    private final String UP = "up";
    private final String DOWN = "down";
    private final String LEFT = "left";
    private final String RIGHT = "right";
    private final String NO_V = "stop_vertical";
    private final String NO_H = "stop_horizontal";
    private final String DIR = "dir";
    private final String PWR = "pwr";

    private Button fireButton;
    private JoystickView joystickView;
    private TextureView textureView;
    private MediaPlayer myVid;
    JSONObject jsonMessage = new JSONObject();

    ThreeState sending_vertical = ThreeState.NOTHING;
    ThreeState sending_horizontal = ThreeState.NOTHING;

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

        textureView = (TextureView) view.findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);

        fireButton = (Button) view.findViewById(R.id.firingButton);
        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("fire");
            }
        });

        joystickView = (JoystickView) view.findViewById(R.id.joystickView);
        joystickView.setOnJoystickMoveListener(new OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                double radians = Math.toRadians(angle);
                double vertical = Math.abs(Math.cos(radians) * power);
                double horizontal = Math.abs(Math.sin(radians) * power);
                try {
                    //rewrite for sensitivity?
                    switch (direction) {
                        case JoystickView.FRONT:
                            if (sending_vertical != ThreeState.POSITIVE) {
                                sending_vertical = ThreeState.POSITIVE;
                                jsonMessage.put(DIR, UP);
                                jsonMessage.put(PWR, vertical);
                                sendMessage(jsonMessage.toString());
                            }
                            if (sending_horizontal != ThreeState.NOTHING) {
                                sending_horizontal = ThreeState.NOTHING;
                                jsonMessage.put(DIR, NO_H);
                                jsonMessage.put(PWR, horizontal);
                                sendMessage(jsonMessage.toString());
                            }
                            break;

                        case JoystickView.FRONT_RIGHT:
                            if (sending_vertical != ThreeState.POSITIVE) {
                                sending_vertical = ThreeState.POSITIVE;
                                jsonMessage.put(DIR, UP);
                                jsonMessage.put(PWR, vertical);
                                sendMessage(jsonMessage.toString());
                            }
                            if (sending_horizontal != ThreeState.POSITIVE) {
                                sending_horizontal = ThreeState.POSITIVE;
                                jsonMessage.put(DIR, RIGHT);
                                jsonMessage.put(PWR, horizontal);
                                sendMessage(jsonMessage.toString());
                            }
                            break;

                        case JoystickView.RIGHT:
                            if (sending_vertical != ThreeState.NOTHING) {
                                sending_vertical = ThreeState.NOTHING;
                                jsonMessage.put(DIR, NO_V);
                                jsonMessage.put(PWR, vertical);
                                sendMessage(jsonMessage.toString());
                            }
                            if (sending_horizontal != ThreeState.POSITIVE) {
                                sending_horizontal = ThreeState.POSITIVE;
                                jsonMessage.put(DIR, RIGHT);
                                jsonMessage.put(PWR, horizontal);
                                sendMessage(jsonMessage.toString());
                            }
                            break;

                        case JoystickView.RIGHT_BOTTOM:
                            if (sending_vertical != ThreeState.NEGATIVE) {
                                sending_vertical = ThreeState.NEGATIVE;
                                jsonMessage.put(DIR, DOWN);
                                jsonMessage.put(PWR, vertical);
                                sendMessage(jsonMessage.toString());
                            }
                            if (sending_horizontal != ThreeState.POSITIVE) {
                                sending_horizontal = ThreeState.POSITIVE;
                                jsonMessage.put(DIR, RIGHT);
                                jsonMessage.put(PWR, horizontal);
                                sendMessage(jsonMessage.toString());
                            }
                            break;

                        case JoystickView.BOTTOM:
                            if (sending_vertical != ThreeState.NEGATIVE) {
                                sending_vertical = ThreeState.NEGATIVE;
                                jsonMessage.put(DIR, DOWN);
                                jsonMessage.put(PWR, vertical);
                                sendMessage(jsonMessage.toString());
                            }
                            if (sending_horizontal != ThreeState.NOTHING) {
                                sending_horizontal = ThreeState.NOTHING;
                                jsonMessage.put(DIR, NO_H);
                                jsonMessage.put(PWR, horizontal);
                                sendMessage(jsonMessage.toString());
                            }
                            break;

                        case JoystickView.BOTTOM_LEFT:
                            if (sending_vertical != ThreeState.NEGATIVE) {
                                sending_vertical = ThreeState.NEGATIVE;
                                jsonMessage.put(DIR, DOWN);
                                jsonMessage.put(PWR, vertical);
                                sendMessage(jsonMessage.toString());
                            }
                            if (sending_horizontal != ThreeState.NEGATIVE) {
                                sending_horizontal = ThreeState.NEGATIVE;
                                jsonMessage.put(DIR, LEFT);
                                jsonMessage.put(PWR, horizontal);
                                sendMessage(jsonMessage.toString());
                            }
                            break;

                        case JoystickView.LEFT:
                            if (sending_vertical != ThreeState.NOTHING) {
                                sending_vertical = ThreeState.NOTHING;
                                jsonMessage.put(DIR, NO_V);
                                jsonMessage.put(PWR, vertical);
                                sendMessage(jsonMessage.toString());
                            }
                            if (sending_horizontal != ThreeState.NEGATIVE) {
                                sending_horizontal = ThreeState.NEGATIVE;
                                jsonMessage.put(DIR, LEFT);
                                jsonMessage.put(PWR, horizontal);
                                sendMessage(jsonMessage.toString());
                            }
                            break;

                        case JoystickView.LEFT_FRONT:
                            if (sending_vertical != ThreeState.POSITIVE) {
                                sending_vertical = ThreeState.POSITIVE;
                                jsonMessage.put(DIR, UP);
                                jsonMessage.put(PWR, vertical);
                                sendMessage(jsonMessage.toString());
                            }
                            if (sending_horizontal != ThreeState.NEGATIVE) {
                                sending_horizontal = ThreeState.NEGATIVE;
                                jsonMessage.put(DIR, LEFT);
                                jsonMessage.put(PWR, horizontal);
                                sendMessage(jsonMessage.toString());
                            }
                            break;

                        default:
                            if (sending_vertical != ThreeState.NOTHING) {
                                sending_vertical = ThreeState.NOTHING;
                                jsonMessage.put(DIR, NO_V);
                                jsonMessage.put(PWR, vertical);
                                sendMessage(jsonMessage.toString());
                            }
                            if (sending_horizontal != ThreeState.NOTHING) {
                                sending_horizontal = ThreeState.NOTHING;
                                jsonMessage.put(DIR, NO_H);
                                jsonMessage.put(PWR, horizontal);
                                sendMessage(jsonMessage.toString());
                            }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
        return view;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface mySurface = new Surface(surface);
        myVid = MediaPlayer.create(this.getActivity(), R.raw.test);
        myVid.setVolume(0, 0);
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
