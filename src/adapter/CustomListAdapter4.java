package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Ömer RENÇBER   2018
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

public class CustomListAdapter4 extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,cinsiyet,gelenresimno,resimyol,resimbaslik;

    public CustomListAdapter4(Activity activity, List<Movie> movieItems) {
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
                convertView = inflater.inflate(R.layout.list_row_arkadaslar, null);


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
       // mesaj.setText(m.getAdi());

        username.setText(m.getAdi()+" "+m.getSoyadi());

        // genre
      /*  String genreStr = "";
        for (String str : m.getGenre()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
        genre.setText(genreStr);
*/
        // release year


        return convertView;
    }

}
