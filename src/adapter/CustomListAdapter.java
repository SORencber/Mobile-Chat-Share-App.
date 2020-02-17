package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.Movie;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public CustomListAdapter(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;

    }

    @Override
    public int getCount() {
        return movieItems.size();
    }
    @Override
    public int getItemViewType(int position)
    { return position;}

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.ivOyverFoto);
        TextView title = (TextView) convertView.findViewById(R.id.tvResimBaslik);
        TextView Asagi = (TextView) convertView.findViewById(R.id.tvAsagi);
        TextView Mesaj = (TextView) convertView.findViewById(R.id.tvMesaj);
        TextView Yukari=(TextView) convertView.findViewById(R.id.tvYukari);
        TextView ToplamOy=(TextView) convertView.findViewById(R.id.etUsername);
        ImageView ivSil=(ImageView) convertView.findViewById(R.id.ivSil);
        // getting movie data for the row
        Movie m = movieItems.get(position);
       // ivSil.setVisibility(convertView.VISIBLE);

      //  if(m.getSil()==1)
       // {

       //     ivSil.setVisibility(convertView.VISIBLE);
       // }





        // thumbnail image
        thumbNail.setImageUrl(Config.SERVER_IS+""+m.getThumbnailUrl(), imageLoader);

        // title
        title.setText(m.getTitle());

        int toplamoy=m.getAsagi()+m.getYukari();
        double YukariYuzde=m.getYukari()*100/toplamoy;
        double AsagiYuzde=m.getAsagi()*100/toplamoy;
        Yukari.setText("%"+String.valueOf(YukariYuzde));
        Asagi.setText( "%"+String.valueOf(AsagiYuzde));


        ToplamOy.setText(activity.getResources().getString(R.string.vote)+ ":"+String.valueOf(toplamoy));
        Mesaj.setText(activity.getResources().getString(R.string.messages)+"("+String.valueOf(m.getYorumsayisi())+")");
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
