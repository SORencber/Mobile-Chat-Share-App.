package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.Movie;

import java.util.List;

public class CustomListAdapter3 extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,cinsiyet,gelenresimno,resimyol,resimbaslik;

    public CustomListAdapter3(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position)
    { return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());//preferences objesi
        editor = preferences.edit();
        // Inflate the layout for this fragment

            eposta = preferences.getString("email", "boş");
            cinsiyet = preferences.getString("cinsiyet", "null");

        Movie m = movieItems.get(position);
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);



            if (convertView == null)
                convertView = inflater.inflate(R.layout.list_row_ozel_mesajlar, null);


        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.ivOyverFoto);
        TextView mesaj = (TextView) convertView.findViewById(R.id.tvResimBaslik);


        TextView username = (TextView) convertView.findViewById(R.id.etUsername);
        // getting movie data for the row


        if (m.getThumbnailUrl().equals("null")) {

            if (cinsiyet.equals("Bay")) {

                thumbNail.setImageUrl(Config.SERVER_IS + "varsayilan/male.png", imageLoader);

            } else {


                thumbNail.setImageUrl(Config.SERVER_IS +  "varsayilan/female.png", imageLoader);

            }

        } else {
            // thumbnail image
            thumbNail.setImageUrl(Config.SERVER_IS + "" + m.getThumbnailUrl(), imageLoader);
        }



        // title
        mesaj.setText(m.getMesaj());

       username.setText(m.getUsername());




        return convertView;
    }

}
