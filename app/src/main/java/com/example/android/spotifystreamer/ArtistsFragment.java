package com.example.android.spotifystreamer;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

    private ArrayAdapter<String> mArtistAdapter;

    public ArtistsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] artistsTest = {"Angra", "Shaman", "Andre Mattos", "Alguem Mais"};
        List<String> artistsList = Arrays.asList(artistsTest);

        mArtistAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_artists,
                R.id.list_item_artist_textview, artistsList);

        mArtistAdapter =
               new ArrayAdapter<String>(
                       getActivity(), // The current context (this activity)
                       R.layout.list_item_artists, // The name of the layout ID.
                       R.id.list_item_artist_textview, // The ID of the textview to populate.
                       new ArrayList<String>());

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

    public class FetchArtistsTask extends AsyncTask<String, Void, List<String>> {

        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();

        @Override
        protected List<String> doInBackground(String... params) {
            String artistSearchText = params[0];

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();

            ArtistsPager artistsPager = spotifyService.searchArtists(artistSearchText);
            Log.d(LOG_TAG, "It works!");
            List<Artist> artists = artistsPager.artists.items;
            //Couldn't find anything
            if(!artists.isEmpty()) {
                List<String> artistsNames = new ArrayList<>();
                for(Artist artist : artists) {
                    Log.d(LOG_TAG, artist.name);
                    artistsNames.add(artist.name);
                }
                return artistsNames;
            }
            Toast failureArtistToast = Toast.makeText(getActivity(),
                "Não foi encontrado nenhum artista", Toast.LENGTH_SHORT);
            failureArtistToast.show();

            return null;
        }

        @Override
        public void onPostExecute(List<String> artistsNames) {
            if (artistsNames != null) {
                mArtistAdapter.clear();
                for(String artistName : artistsNames) {
                    mArtistAdapter.add(artistName);
                }
                // New data is back from the server.  Hooray!
            }
        }
    }

}
