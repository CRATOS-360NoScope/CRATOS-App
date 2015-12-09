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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

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
public class Other_Firing_Fragment extends Fragment {

    JSONObject jsonMessageDirection = new JSONObject();
    JSONObject jsonMessageFire = new JSONObject();

    private int currentHorizontal = 0;
    private int currentVertical = 0;

    private boolean fireLock = false;
    private boolean aimLock = false;

    private Button fireButton;
    private WebView webView;
    private Switch fireSwitch;
    private Switch aimSwitch;
    private JoystickView joystickView;
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

        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://10.0.0.12:5001/stream_simple.html");
        //webView.loadUrl("http://www.google.com");

        fireSwitch = (Switch) view.findViewById(R.id.fireSwitch);
        fireSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fireLock = isChecked;
            }
        });

        aimSwitch = (Switch) view.findViewById(R.id.aimSwitch);
        aimSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aimLock = isChecked;
            }
        });

        fireButton = (Button) view.findViewById(R.id.firingButton);
        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fireLock) {
                    bt.send(jsonMessageFire.toString(), false);
                }
            }
        });

        joystickView = (JoystickView) view.findViewById(R.id.joystickView);
        joystickView.setOnJoystickMoveListener(new OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                if(!aimLock) {
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
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
        return view;
    }
}
