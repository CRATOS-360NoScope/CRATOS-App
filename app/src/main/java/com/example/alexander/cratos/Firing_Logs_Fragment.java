package com.example.alexander.cratos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;


/**
 * A placeholder fragment containing a simple view.
 */
public class Firing_Logs_Fragment extends Fragment {

    BluetoothSPP bt;

    public Firing_Logs_Fragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_firing_logs, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.listView);

        bt = ((CratosBaseApplication)getActivity().getApplication()).getBt();

        final ArrayAdapter<String> adapter;
        final ArrayList<String> listItems = new ArrayList();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);


        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] bytes, String s) {

                try {
                    JSONArray array = new JSONArray(s);
                    Toast.makeText(getActivity(), array.getJSONObject(0).toString(), Toast.LENGTH_LONG).show();
                    int size = array.length();
                    for(int i = 0; i < size; i++) {
                        listItems.add("\n" + array.getJSONObject(i).getString("device_id") + "\n\n" + array.getJSONObject(i).getString("discharge_timestamp") + "\n");
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }
}
