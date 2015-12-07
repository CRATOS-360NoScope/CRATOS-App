package com.example.alexander.cratos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;


/**
 * A placeholder fragment containing a simple view.
 */
public class Firing_Logs_Fragment extends Fragment {

    BluetoothSPP bt;
    JSONObject jsonMessageLog;
    private boolean sortByTime = false;
    private boolean sortByName = false;

    public Firing_Logs_Fragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_firing_logs, container, false);

        final ListView listView = (ListView)rootView.findViewById(R.id.listView);
        Button loadMoreButton = (Button)rootView.findViewById(R.id.loadMoreButton);
        final Button sortByIDButton = (Button)rootView.findViewById(R.id.button_sort_phone_id);
        final Button sortByTimeButton = (Button)rootView.findViewById(R.id.button_sort_timestamp);

        bt = ((CratosBaseApplication)getActivity().getApplication()).getBt();

        final ArrayAdapter<String> adapter;
        final ArrayList<String> listItems = new ArrayList();
        final ArrayList<String> listItemsTime = new ArrayList();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        jsonMessageLog = new JSONObject();
        try {
            jsonMessageLog.put(getString(R.string.command), getString(R.string.log));
            jsonMessageLog.put(getString(R.string.first), getString(R.string.mTrue));
            bt.send(jsonMessageLog.toString(), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sortByIDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapterID;
                List<String> sublist = listItems.subList(1, listItems.size());
                Collections.sort(sublist);
                adapterID = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sublist);
                listView.setAdapter(adapterID);
                sortByName = true;
                sortByTime = false;
            }
        });

        sortByTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapterTime;
                adapterTime = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItemsTime);
                listView.setAdapter(adapterTime);
                sortByName = false;
                sortByTime = true;
            }
        });

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] bytes, String s) {

                try {
                    JSONArray array = new JSONArray(s);
                    int size = array.length();
                    for (int i = 0; i < size; i++) {
                        listItems.add("\n" + array.getJSONObject(i).getString("name") + "\n\n" + array.getJSONObject(i).getString("discharge_timestamp") + "\n");
                        listItemsTime.add("\n" + array.getJSONObject(i).getString("discharge_timestamp") + "\n\n" + array.getJSONObject(i).getString("name") + "\n");
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    jsonMessageLog.put(getString(R.string.first), getString(R.string.mFalse));
                    bt.send(jsonMessageLog.toString(), false);
                    if (sortByName) {
                        sortByIDButton.callOnClick();
                    }
                    if (sortByTime) {
                        sortByTimeButton.callOnClick();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }
}
