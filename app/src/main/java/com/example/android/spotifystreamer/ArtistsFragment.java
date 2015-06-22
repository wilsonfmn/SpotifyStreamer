package com.example.android.spotifystreamer;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistsFragment extends Fragment {

    private List<Artist> artists;
    private ImageAndInfoAdapter mArtistAdapter;

    public ArtistsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mArtistAdapter =
               new ImageAndInfoAdapter(
                       getActivity(), // The current context (this activity)
                       new ArrayList<Artist>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        listView.setAdapter(mArtistAdapter);

        Button buttonSearch = (Button) rootView.findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchView = (EditText) getActivity().findViewById(R.id.textview_search);
                String artistSearchText = searchView.getText().toString();

                FetchArtistsTask artistsTask = new FetchArtistsTask();
                artistsTask.execute(artistSearchText);
            }
        });

        return rootView;
    }

    public class FetchArtistsTask extends AsyncTask<String, Void, List<Artist>> {

        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();

        @Override
        protected List<Artist> doInBackground(String... params) {
            String artistSearchText = params[0];

            // No query
            if(TextUtils.isEmpty(artistSearchText)){
                return null;
            }

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();

            try {
                ArtistsPager artistsPager = spotifyService.searchArtists(artistSearchText);
                artists = artistsPager.artists.items;
                if(!artists.isEmpty()) {
                    return artists;
                }
            } catch(RetrofitError e) {
                Log.e(LOG_TAG, e.getMessage().toString());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(List<Artist> artists) {
            if (artists != null) {
                mArtistAdapter.clear();
                mArtistAdapter.addAll(artists);
                mArtistAdapter.notifyDataSetChanged();
                // New data is back from the server.  Hooray!
            } else {
                Toast failureArtistToast = Toast.makeText(getActivity(),
                        "Não foi encontrado nenhum artista", Toast.LENGTH_SHORT);
                failureArtistToast.show();
            }
        }
    }

}
