package com.example.alexander.cratos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class Firing_Logs_Fragment extends Fragment {

    public Firing_Logs_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_firing_logs, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.listView);

        ArrayAdapter<String> adapter;
        ArrayList<String> listItems = new ArrayList<String>();

        listItems.add("Item 1");
        listItems.add("Item 2");
        listItems.add("Item 3");
        listItems.add("Item 4");
        listItems.add("Item 5");
        listItems.add("Item 6");
        listItems.add("Item 7");
        listItems.add("Item 8");
        listItems.add("Item 9");
        listItems.add("Item 10");
        listItems.add("Item 11");
        listItems.add("Item 12");

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        return rootView;
    }
}
