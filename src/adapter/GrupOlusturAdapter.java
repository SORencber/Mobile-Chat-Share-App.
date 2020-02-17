package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Ömer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.Grup.Trends.GrupElemanlari;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class GrupOlusturAdapter extends RecyclerView.Adapter<GrupOlusturAdapter.ViewHolder> {
    private Context mContext;
    public  ArrayList<GrupElemanlari> usersList;
    public ArrayList<GrupElemanlari> selected_usersList;

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

    @Override
    public int getItemViewType(int position)
    { return position;}

    public GrupOlusturAdapter(Context mContext, ArrayList<GrupElemanlari> userList,ArrayList<GrupElemanlari> selectedList ) {
        this.mContext = mContext;
        this.usersList = userList;
        this.selected_usersList = selectedList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_group_elemanlari, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GrupElemanlari chatRoom = usersList.get(position);
   //     GrupElemanlari chatRoom2 = selected_usersList.get(position);

        holder.name.setText(chatRoom.getName());
        holder.message.setText(chatRoom.getLastMessage());
        if (chatRoom.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(chatRoom.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }

        holder.timestamp.setText(getTimeStamp(chatRoom.getTimestamp()));

        if (chatRoom.getFoto().equals("null")) {

            if (chatRoom.getCinsiyet().equals("Bay")) {

                holder.foto.setImageUrl(Config.SERVER_IS + "varsayilan/male.png", imageLoader);

            } else {


                holder.foto.setImageUrl(Config.SERVER_IS +  "varsayilan/female.png", imageLoader);

            }

        } else {
            // thumbnail image
            holder.foto.setImageUrl(Config.SERVER_IS+""+chatRoom.getFoto(), imageLoader);
        }

        if(selected_usersList.contains(usersList.get(position)))
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));





    }


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
        private GrupOlusturAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GrupOlusturAdapter.ClickListener clickListener) {
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

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
