package com.example.alexander.cratos;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
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

import app.akexorcist.bluetotohspp.library.BluetoothSPP;


/**
 * Created by Dylan on 10/2/2015.
 *
 * The fragment for the turret control. One of two possible files.
 */
public class Other_Firing_Fragment extends Fragment implements TextureView.SurfaceTextureListener {

    JSONObject jsonMessageDirection = new JSONObject();
    JSONObject jsonMessageFire = new JSONObject();

    private int currentHorizontal = 0;
    private int currentVertical = 0;

    private Button fireButton;
    private JoystickView joystickView;
    private TextureView textureView;
    private MediaPlayer myVid;

    public Other_Firing_Fragment() {}

    BluetoothSPP bt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bt = ((CratosBaseApplication) getActivity().getApplication()).getBt();
        String id = Settings.Secure.getString(this.getActivity().getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
        id = id == null ? "bad_id" : id;
        try {
            jsonMessageFire.put(getString(R.string.command), getString(R.string.fire));
            jsonMessageFire.put(getString(R.string.id), id);
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
                bt.send(jsonMessageFire.toString(), false);
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
                        jsonMessageDirection.put(getString(R.string.command), getString(R.string.horizontal));
                        jsonMessageDirection.put(getString(R.string.power), currentHorizontal);
                        bt.send(jsonMessageDirection.toString(), false);
                    }

                    if (tempVert != currentVertical) {
                        currentVertical = tempVert;
                        jsonMessageDirection.put(getString(R.string.command), getString(R.string.vertical));
                        jsonMessageDirection.put(getString(R.string.power), currentVertical);
                        bt.send(jsonMessageDirection.toString(), false);
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
