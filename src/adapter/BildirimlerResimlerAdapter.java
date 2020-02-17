package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yavuz.rencber.rencber.Bildirim.Profile.BildirimYorumlar;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.KullaniciProfili;
import com.yavuz.rencber.rencber.activity.ShowPopUp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class BildirimlerResimlerAdapter extends RecyclerView.Adapter<BildirimlerResimlerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<BildirimYorumlar> trendsResimlerArrayList;
    private static String today;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
   String cinsiyet;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message, timestamp, count;
        NetworkImageView foto,ivYorumResim;
        public ViewHolder(View view) {
            super(view);
            foto=(NetworkImageView)view.findViewById(R.id.ivresimler);
            ivYorumResim=(NetworkImageView) view.findViewById(R.id.ivYorumResim);

            name = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            count = (TextView) view.findViewById(R.id.count);
        }
    }


    public BildirimlerResimlerAdapter(Context mContext, ArrayList<BildirimYorumlar> trendsResimlerArrayList) {
        this.mContext = mContext;
        this.trendsResimlerArrayList = trendsResimlerArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bildirimler_resim_yorumlari, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BildirimYorumlar bildirimYorumlar = trendsResimlerArrayList.get(position);

        holder.name.setText(bildirimYorumlar.getName());
        holder.message.setText(bildirimYorumlar.getLastMessage());
     /*   if (trendsResimler.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(trendsResimler.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }
*/
      //  holder.timestamp.setText(getTimeStamp(trendsResimler.getTimestamp()));


            // thumbnail image
            holder.foto.setImageUrl(Config.SERVER_IS+bildirimYorumlar.getFoto(),imageLoader);
        holder.ivYorumResim.setImageUrl(Config.SERVER_IS+bildirimYorumlar.getThumbnailUrl(),imageLoader);

        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, KullaniciProfili.class);

                i.putExtra("to_user_id", bildirimYorumlar.getTo_user_id());

                i.putExtra("to_eposta", bildirimYorumlar.getEmail());

                mContext.startActivity(i);
            }
        });
        holder.ivYorumResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowPopUp.class);
                intent.putExtra("to_user_id", String.valueOf(bildirimYorumlar.getTo_user_id()));
                intent.putExtra("resimno", String.valueOf(bildirimYorumlar.getResim_no()));
                intent.putExtra("resimyol", bildirimYorumlar.getThumbnailUrl());
                intent.putExtra("resimbaslik", "selam");
                intent.putExtra("sahibi", bildirimYorumlar.getSahibi());
                intent.putExtra("grup", bildirimYorumlar.getGrup());

                intent.putExtra("silmek", "0");

                // intent.putExtra("to_user_id", topResimler.getTo_user_id());
                //  intent.putExtra("foto", topResimler.getFoto());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trendsResimlerArrayList.size();
    }
    @Override
    public int getItemViewType(int position)
    { return position;}

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private BildirimlerResimlerAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final BildirimlerResimlerAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
