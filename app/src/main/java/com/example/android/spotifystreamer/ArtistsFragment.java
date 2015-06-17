package com.example.android.spotifystreamer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistsFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter;

    public ArtistsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String[] artistsTest = {"Angra", "Shaman", "Andre Mattos", "Alguem Mais"};
        List<String> artistsList = Arrays.asList(artistsTest);

        mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_artists,
                R.id.list_item_artist_textview, artistsList);

        //mForecastAdapter =
         //       new ArrayAdapter<String>(
          //              getActivity(), // The current context (this activity)
          //              R.layout.list_item_artists, // The name of the layout ID.
           //             R.id.list_item_artist_textview, // The ID of the textview to populate.
            //            new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }

}
