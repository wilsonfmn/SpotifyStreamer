package com.example.android.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;


/**
 * Created by wilson.neto on 23/06/2015.
 */
public class TopTrackAdapter extends BaseAdapter {

    List<Track> tracks;
    LayoutInflater inflater;
    Context context;

    public TopTrackAdapter(Context context, List<Track> tracks) {
        this.tracks = tracks;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
        public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void clear() {
        this.tracks = new ArrayList<>();
    }

    public void add(Track track) {
        this.tracks.add(track);
    }

    public void addAll(List<Track> tracks) {
        this.tracks.addAll(tracks);
    }

    public String getTrackId(int position) {
        return this.tracks.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_toptracks,
                    parent, false);

            holder.nameTrack = (TextView) convertView
                    .findViewById(R.id.list_item_track_textview);
            holder.imgAlbum = (ImageView) convertView.findViewById(R.id.list_item_album_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Track track = tracks.get(position);
        holder.nameTrack.setText(track.name);

        if(track.album != null && track.album.images != null && !track.album.images.isEmpty()) {
            Picasso.with(context).load(track.album.images.get(0).url).error(R.drawable.artist_placeholder2).into(holder.imgAlbum);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView nameTrack;
        ImageView imgAlbum;
    }
}
