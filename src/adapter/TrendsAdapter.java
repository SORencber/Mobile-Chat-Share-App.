package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Ömer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.Trends.TrendsResimler;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class TrendsAdapter extends RecyclerView.Adapter<TrendsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<TrendsResimler> trendsResimlerArrayList;
    private static String today;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
   String cinsiyet;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message, timestamp, count;
        NetworkImageView foto;
        public ViewHolder(View view) {
            super(view);
            foto=(NetworkImageView)view.findViewById(R.id.ivresimler);
            name = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            count = (TextView) view.findViewById(R.id.count);
        }
    }


    public TrendsAdapter(Context mContext, ArrayList<TrendsResimler> trendsResimlerArrayList) {
        this.mContext = mContext;
        this.trendsResimlerArrayList = trendsResimlerArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.three_en_iyi_resimler, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrendsResimler trendsResimler = trendsResimlerArrayList.get(position);

        holder.name.setText(trendsResimler.getAdi());
       // holder.message.setText(trendsResimler.getLastMessage());
     /*   if (trendsResimler.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(trendsResimler.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }
*/
      //  holder.timestamp.setText(getTimeStamp(trendsResimler.getTimestamp()));


            // thumbnail image
          holder.foto.setImageUrl(Config.SERVER_IS+trendsResimler.getThumbnailUrl(),imageLoader);
        //Picasso.with(mContext).load(Config.SERVER_IS + "" + trendsResimler.getThumbnailUrl()).resize(80,80).into(holder.foto);

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
        private TrendsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TrendsAdapter.ClickListener clickListener) {
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
