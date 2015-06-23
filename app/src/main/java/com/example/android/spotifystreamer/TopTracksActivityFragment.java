package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTracksActivityFragment extends Fragment {

    private TopTrackAdapter mTracksAdapter;
    private List<Track> tracks;

    public TopTracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);

        mTracksAdapter =
                new TopTrackAdapter(
                        getActivity(), // The current context (this activity)
                        new ArrayList<Track>());

        ListView listView = (ListView) rootView.findViewById(R.id.listview_toptracks);
        listView.setAdapter(mTracksAdapter);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String artistId = intent.getStringExtra(Intent.EXTRA_TEXT);
            FetchTracksTask tracksTask = new FetchTracksTask();
            tracksTask.execute(artistId);
        }

        return rootView;
    }

    public class FetchTracksTask extends AsyncTask<String, Void, List<Track>> {

        private final String LOG_TAG = FetchTracksTask.class.getSimpleName();

        @Override
        protected List<Track> doInBackground(String... params) {
            String tracksSearchText = params[0];

            // No query
            if(TextUtils.isEmpty(tracksSearchText)){
                return null;
            }

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();

            try {
                Map<String, Object> searchTrackParameters = new HashMap<>();
                searchTrackParameters.put("country","BR");
                Tracks topTracks = spotifyService.getArtistTopTrack(tracksSearchText,searchTrackParameters);
                tracks = topTracks.tracks;
                if(!tracks.isEmpty()) {
                    return tracks;
                }
            } catch(RetrofitError e) {
                Log.e(LOG_TAG, e.getMessage().toString());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(List<Track> tracks) {
            if (tracks != null) {
                mTracksAdapter.clear();
                mTracksAdapter.addAll(tracks);
                mTracksAdapter.notifyDataSetChanged();
                // New data is back from the server.  Hooray!
            } else {
                Toast failureTopTracksToast = Toast.makeText(getActivity(),
                        "Não foi encontrada nenhuma música para esse artista", Toast.LENGTH_SHORT);
                failureTopTracksToast.show();
            }
        }
    }

}
