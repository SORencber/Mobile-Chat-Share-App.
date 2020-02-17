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
import com.yavuz.rencber.rencber.Profile.ProfileKullanicilar;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;

import java.util.ArrayList;
import java.util.Calendar;


public class ProfileKullanicilarAdapter extends RecyclerView.Adapter<ProfileKullanicilarAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ProfileKullanicilar> chatRoomArrayList;
    private static String today;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
   String cinsiyet;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message, timestamp, count;
        NetworkImageView foto;
        public ViewHolder(View view) {
            super(view);
            foto=(NetworkImageView)view.findViewById(R.id.fotousers);
            name = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            count = (TextView) view.findViewById(R.id.count);
        }
    }


    public ProfileKullanicilarAdapter(Context mContext, ArrayList<ProfileKullanicilar> chatRoomArrayList) {
        this.mContext = mContext;
        this.chatRoomArrayList = chatRoomArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kullanici_profile_kullanicilar, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProfileKullanicilar chatRoom = chatRoomArrayList.get(position);

        holder.name.setText(chatRoom.getName());
        holder.message.setText(chatRoom.getLastMessage());
        if (chatRoom.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(chatRoom.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }


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
    }

    @Override
    public int getItemCount() {
        return chatRoomArrayList.size();
    }

    @Override
    public int getItemViewType(int position)
    { return position;}


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ProfileKullanicilarAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ProfileKullanicilarAdapter.ClickListener clickListener) {
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
