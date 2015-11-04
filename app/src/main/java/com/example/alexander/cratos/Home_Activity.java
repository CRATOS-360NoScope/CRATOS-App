package com.example.alexander.cratos;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class Home_Activity extends AppCompatActivity {

    BluetoothSPP bt;
    final String TAG = "HOME_ACTIVITY";
    Home_Fragment f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        f = (Home_Fragment) fm.findFragmentById(R.id.fragment);

        bt = ((CratosBaseApplication)getApplication()).getBt();
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(Home_Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Log.d(TAG, "Device Connected: " + name);
                Toast.makeText(Home_Activity.this, "Connected to " + name, Toast.LENGTH_SHORT).show();
                f.changeToggleButtonOnText();
                f.toggleMenuButtons(true);
            }

            public void onDeviceDisconnected() {
                Log.d(TAG, "Device Disconnected");
                Toast.makeText(Home_Activity.this, "Device Disconnected", Toast.LENGTH_SHORT).show();
                f.toggleToggleButton();
                ((CratosBaseApplication) getApplication()).bluetoothKilled();
            }

            public void onDeviceConnectionFailed() {
                Log.d(TAG, "Device Connection Failed");
                Toast.makeText(Home_Activity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                f.toggleToggleButton();

            }
        });

        bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            public void onServiceStateChanged(int state) {
                if (state == BluetoothState.STATE_CONNECTED)
                    Log.d(TAG, "Bluetooth is Connected");
                else if (state == BluetoothState.STATE_CONNECTING)
                    Log.d(TAG, "Bluetooth Connecting...");
                else if (state == BluetoothState.STATE_LISTEN)
                    Log.d(TAG, "Bluetooth State Listening");
                else if (state == BluetoothState.STATE_NONE)
                    Log.d(TAG, "Bluetooth State None");
            }
        });
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] bytes, String s) {
                Log.d(TAG, "Message Recieved");
                Toast.makeText(Home_Activity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        if(bt.getBluetoothAdapter() != null) {
            if (!bt.isBluetoothEnabled()) {
                bt.enable();
            } else {
                if (!bt.isServiceAvailable()) {
                    bt.setupService();
                    bt.startService(BluetoothState.DEVICE_OTHER);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void bluetoothConnect(){
        bt.startService(BluetoothState.DEVICE_OTHER);
        if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
            bt.disconnect();
        } else {
            Intent intent = new Intent(this, DeviceList.class);
            intent.putExtra("bluetooth_devices", "Bluetooth devices");
            intent.putExtra("no_devices_found", "No device");
            intent.putExtra("scanning", "Scanning");
            intent.putExtra("scan_for_devices", "Search");
            intent.putExtra("select_device", "Select");
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
                if (resultCode == Activity.RESULT_OK)
                    Log.d(TAG, "Bluetooth attempt connect...");
                bt.connect(data);
            } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Bluetooth being set up");
                    bt.setupService();
                } else {
                    Toast.makeText(getApplicationContext()
                            , "Bluetooth was not enabled."
                            , Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        } else {
            f.toggleToggleButton();
        }
    }

    public void stopBluetooth(){
        bt.stopService();
    }
}
