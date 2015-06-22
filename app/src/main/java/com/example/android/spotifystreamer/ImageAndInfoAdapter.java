package com.example.android.spotifystreamer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by wilson.neto on 19/06/2015.
 */
public class ImageAndInfoAdapter extends BaseAdapter {

    List<Artist> artists;
    LayoutInflater inflater;
    Context context;

    public ImageAndInfoAdapter(Context context, List<Artist> artists) {
        this.artists = artists;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.artists.size();
    }

    @Override
    public Object getItem(int position) {
        return this.artists.get(position);
    }

    /**
     * Use getArtistId
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void clear() {
        this.artists = new ArrayList<>();
    }

    public void add(Artist artist) {
        this.artists.add(artist);
    }

    public void addAll(List<Artist> artists) {
        this.artists.addAll(artists);
    }

    public String getArtistId(int position) {
        return this.artists.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_artists,
                    parent, false);

            holder.nameArtist = (TextView) convertView
                    .findViewById(R.id.list_item_artist_textview);
            holder.imgArtist = (ImageView) convertView.findViewById(R.id.list_item_artist_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Artist artist = artists.get(position);
        holder.nameArtist.setText(artist.name);

        if(artist.images != null && !artist.images.isEmpty()) {
            Picasso.with(context).load(artist.images.get(0).url).error(R.drawable.artist_placeholder2).into(holder.imgArtist);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView nameArtist;
        ImageView imgArtist;
    }
}
