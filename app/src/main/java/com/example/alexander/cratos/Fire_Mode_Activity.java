package com.example.alexander.cratos;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class Fire_Mode_Activity extends AppCompatActivity{

    BluetoothSPP bt;
    final String TAG = "FIRE_MODE_ACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_fire__mode);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("control-click"));
        bt = new BluetoothSPP(getApplicationContext());
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(Fire_Mode_Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Log.d(TAG, "Device Connected: " + name);
                Toast.makeText(Fire_Mode_Activity.this, "Connected to "+ name, Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                Log.d(TAG, "Device Disconnected");
                Toast.makeText(Fire_Mode_Activity.this, "Device Disconnected", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Log.d(TAG, "Device Connection Failed");
                Toast.makeText(Fire_Mode_Activity.this, "Connection Failed", Toast.LENGTH_SHORT).show();

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
    }

    @Override
    public void onStart(){
        super.onStart();
        if(!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fire__mode, menu);
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
        else if (id == R.id.bluetooth_connect){
            bluetoothConnect();
            return true;
        }
        else if (id == R.id.bluetooth_stop){
            stopBluetooth();
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
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                Log.d(TAG, "Bluetooth attempt connect...");
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "Bluetooth being set up");
                bt.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void stopBluetooth(){
        bt.stopService();
    }

    public boolean sendBTMessage(String message) {
        try {
            bt.send(message, false);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            sendBTMessage(message);
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
