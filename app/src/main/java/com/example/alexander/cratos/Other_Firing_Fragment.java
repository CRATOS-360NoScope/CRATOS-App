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

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Dylan on 10/2/2015.
 *
 * The fragment for the turret control. One of two possible files.
 */
public class Other_Firing_Fragment extends Fragment implements TextureView.SurfaceTextureListener {

    private final String MESSAGE_HORIZONTAL = "Horizontal";
    private final String MESSAGE_VERTICAL = "Vertical";
    private final String MESSAGE_FORMAT_DIRECTION = "Direction";
    private final String MESSAGE_FORMAT_POWER = "Power";
    private final String MESSAGE_FIRE = "Fire";

    JSONObject jsonMessageDirection = new JSONObject();
    JSONObject jsonMessageFire = new JSONObject();

    private int currentHorizontal = 0;
    private int currentVertical = 0;

    private Button fireButton;
    private ToggleButton bluetoothButton;
    private JoystickView joystickView;
    private TextureView textureView;
    private MediaPlayer myVid;

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
        try {
            jsonMessageFire.put(MESSAGE_FIRE, MESSAGE_FIRE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.fragment_other_fire, container, false);

        textureView = (TextureView) view.findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);

        fireButton = (Button) view.findViewById(R.id.firingButton);
        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(jsonMessageFire.toString());
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
                int tempVert = 0;
                int tempHorz = 0;
                if(power >= 20) {
                    power = ((power - 20) * 5)/4;   //scale
                    power = power - (power % 10);          //remove second digit. ex: 11->10,  88->80
                   switch(direction) {
                       case JoystickView.FRONT:
                           tempVert = power;
                           break;

                       case JoystickView.FRONT_RIGHT:
                           tempHorz = power;
                           tempVert = power;
                           break;

                       case JoystickView.RIGHT:
                           tempHorz = power;
                           break;

                       case JoystickView.RIGHT_BOTTOM:
                           tempHorz = power;
                           tempVert = -power;
                           break;

                       case JoystickView.BOTTOM:
                           tempVert = -power;
                           break;

                       case JoystickView.BOTTOM_LEFT:
                           tempHorz = -power;
                           tempVert = -power;
                           break;

                       case JoystickView.LEFT:
                           tempHorz = -power;
                           break;

                       case JoystickView.LEFT_FRONT:
                           tempHorz = -power;
                           tempVert = power;
                           break;
                   }
                }
                try {
                    if (tempHorz != currentHorizontal) {
                        currentHorizontal = tempHorz;
                        jsonMessageDirection.put(MESSAGE_FORMAT_DIRECTION, MESSAGE_HORIZONTAL);
                        jsonMessageDirection.put(MESSAGE_FORMAT_POWER, currentHorizontal);
                        sendMessage(jsonMessageDirection.toString());
                    }

                    if (tempVert != currentVertical) {
                        currentVertical = tempVert;
                        jsonMessageDirection.put(MESSAGE_FORMAT_DIRECTION, MESSAGE_VERTICAL);
                        jsonMessageDirection.put(MESSAGE_FORMAT_POWER, currentVertical);
                        sendMessage(jsonMessageDirection.toString());
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
        myVid.setSurface(mySurface);
        myVid.setLooping(true);
        myVid.setVolume(0,0);
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
