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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;


/**
 * A placeholder fragment containing a simple view.
 */
public class Firing_Logs_Fragment extends Fragment {

    BluetoothSPP bt;
    JSONObject jsonMessageLog;

    public Firing_Logs_Fragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_firing_logs, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.listView);
        Button loadMoreButton = (Button)rootView.findViewById(R.id.loadMoreButton);

        bt = ((CratosBaseApplication)getActivity().getApplication()).getBt();

        final ArrayAdapter<String> adapter;
        final ArrayList<String> listItems = new ArrayList<>();

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        jsonMessageLog = new JSONObject();
        try {
            jsonMessageLog.put(getString(R.string.command), getString(R.string.log));
            jsonMessageLog.put(getString(R.string.first), getString(R.string.mTrue));
            bt.send(jsonMessageLog.toString(), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] bytes, String s) {

                try {
                    JSONArray array = new JSONArray(s);
                    int size = array.length();
                    DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                    DateFormat newFormat = DateFormat.getDateTimeInstance(
                            DateFormat.MEDIUM,
                            DateFormat.MEDIUM,
                            Locale.ENGLISH);
                    for(int i = 0; i < size; i++) {
                        Date d = oldFormat.parse(array.getJSONObject(i).getString("discharge_timestamp"));
                        String parsedDate = newFormat.format(d);
                        listItems.add("\n" + array.getJSONObject(i).getString("name") + "\n\n" + parsedDate + "\n");
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    Log.d("DATA RECIEVED", e.getMessage());
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }
}
