package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.yavuz.rencber.rencber.GCM.ChatRoom;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.ChatRoomActivity;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.IconTextTabsActivity;
import com.yavuz.rencber.rencber.activity.KullaniciProfili;
import com.yavuz.rencber.rencber.activity.MySingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ViewHolder> {
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    private Context mContext;
    private ArrayList<ChatRoom> chatRoomArrayList;
    private ArrayList<ChatRoom> chatRoomArrayList_arama;
    String eposta, user_email, user_name, user_id;

    private static String today;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
   String cinsiyet,grup;















    public class ViewHolder extends RecyclerView.ViewHolder {
        public  SeekBar seekbar;
        public TextView name, timestamp, count;
        NetworkImageView foto;
        ImageView ivArkadasSil;

        public ViewHolder(View view) {
            super(view);
            foto=(NetworkImageView)view.findViewById(R.id.ivresimler);
            name = (TextView) view.findViewById(R.id.name);
          //  message = (TextView) view.findViewById(R.id.message);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            count = (TextView) view.findViewById(R.id.count);
            ivArkadasSil=(ImageView)view.findViewById(R.id.ivArkadasSilsene);
        }
    }


    public ChatRoomsAdapter(Context mContext, ArrayList<ChatRoom> chatRoomArrayList) {
        this.mContext = mContext;
        this.chatRoomArrayList = chatRoomArrayList;
        this.chatRoomArrayList_arama = chatRoomArrayList_arama;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_rooms_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);//preferences objesi



        editor = preferences.edit();
        user_email = preferences.getString("user_email", "boş");
        user_id = preferences.getString("user_id", "boş");
        user_name = preferences.getString("user_name", "boş");
        eposta = preferences.getString("email", "bos");
        final ChatRoom chatRoom = chatRoomArrayList.get(position);

        holder.name.setText(chatRoom.getName());
       // holder.message.setText(chatRoom.getLastMessage());
        if (chatRoom.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(chatRoom.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }

        holder.timestamp.setText(getTimeStamp(chatRoom.getTimestamp()));

        if (chatRoom.getFoto().equals("null")) {

            if (chatRoom.getCinsiyet().equals("Bayan")) {

                holder.foto.setImageUrl(Config.SERVER_IS + "varsayilan/female.png", imageLoader);

            } else {


                holder.foto.setImageUrl(Config.SERVER_IS +  "varsayilan/male.png", imageLoader);

            }

        } else {
            // thumbnail image
            holder.foto.setImageUrl(Config.SERVER_IS+chatRoom.getFoto(), imageLoader);
        }
        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, KullaniciProfili.class);

                i.putExtra("to_user_id", chatRoom.getTo_user_id());

                i.putExtra("to_eposta", chatRoom.getEmail());

                mContext.startActivity(i);
            }
        });
        holder.ivArkadasSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(mContext);
                builder.setCancelable(false);
                if (chatRoom.getGrup().equals("0"))
                {
                    //   builder.setTitle("Bu ?");
                    builder.setMessage(mContext.getResources().getString(R.string.deletefriendlist));

                }else{
                    if(chatRoom.getSahibi().equals("0"))
                    {                    builder.setMessage(mContext.getResources().getString(R.string.deletegrouplist));
                    }else {builder.setMessage(mContext.getResources().getString(R.string.deletegroup));}
                    // builder.setTitle("Bu ?");
                }
                // builder.setTitle("Bu ?");
                builder.setPositiveButton(mContext.getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub



                        ArkadasSil(chatRoom.getGrup(),chatRoom.getSahibi(),eposta,chatRoom.getEmail(),chatRoom.getId());
                        chatRoomArrayList.remove(position);

                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(mContext.getResources().getString(R.string.no),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();

                    }
                });

                android.app.AlertDialog alert=builder.create();
                alert.show();
                //super.onBackPressed();
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatRoom.setUnreadCount(0);
                int cikacak = chatRoom.getUnreadCount();
                int chatRoomMesajCount = preferences.getInt("mesajsayisi", 0);
                int sonuc = chatRoomMesajCount - cikacak;
                editor.putInt(chatRoom.getId(), 0);
                editor.putInt("mesajsayisi", 0);

                editor.commit();

                IconTextTabsActivity.badgesohbet.setText("0");

                IconTextTabsActivity.badgesohbet.hide();
                notifyDataSetChanged();


                Intent intent = new Intent(mContext, ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                intent.putExtra("foto", chatRoom.getFoto());


                intent.putExtra("odaIsmi", chatRoom.getName());

                if (chatRoom.getGrup().equals("") || chatRoom.getGrup().equals("0") || chatRoom.equals("null")) {
                    intent.putExtra("grup", "0");
                    intent.putExtra("to_eposta", chatRoom.getEmail());


                } else {
                    intent.putExtra("grup", "1");
                    intent.putExtra("to_eposta", eposta);

                }
                if (chatRoom.getSahibi().equals("") || chatRoom.getSahibi().equals("0") || chatRoom.getSahibi().equals("null")) {
                    intent.putExtra("sahibi", "0");


                } else {
                    intent.putExtra("sahibi", "1");

                }
                mContext.startActivity(intent);
            }
        });

    }
    public void filter(String text) {
        chatRoomArrayList_arama.clear();
        if(text.isEmpty()){
            chatRoomArrayList_arama.addAll(chatRoomArrayList);
        } else{
            text = text.toLowerCase();
            for(ChatRoom item: chatRoomArrayList){
                if(item.getName().toLowerCase().contains(text)){
                    chatRoomArrayList_arama.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return chatRoomArrayList.size();
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
        private ChatRoomsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ChatRoomsAdapter.ClickListener clickListener) {
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


    private void ArkadasSil(final String grup,final String sahibi,final String eposta,final String sqleposta,final String chatRoomId) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);
        String url;
        if (grup.equals("0")) {
            url = Config.SERVER_IS + "arkadasSil.php";
        }else {
            url = Config.SERVER_IS + "odaSil.php";

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Web ", response);
                        if(response.contains("success_login")){
                            Toast.makeText(mContext.getApplicationContext(), mContext.getResources().getString(R.string.forwardmessageok), Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(mContext.getApplicationContext(),  mContext.getResources().getString(R.string.somethingwrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext.getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txteposta", (String.valueOf(eposta)) );
                params.put("txtsahibi", (String.valueOf(sahibi)) );

                params.put("txtsqleposta", (String.valueOf(sqleposta)) );
                params.put("txtroomid", (String.valueOf(chatRoomId)) );
                Log.e("oda", params.toString());
                return params;
            }
        };

        MySingleton.getInstance(mContext.getApplicationContext()).addToRequestQueue(stringRequest);
    }


}
