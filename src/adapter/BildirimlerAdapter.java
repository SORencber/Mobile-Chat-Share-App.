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
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.yavuz.rencber.rencber.Bildirim.Profile.BildirimYorumlar;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.KullaniciProfili;
import com.yavuz.rencber.rencber.activity.MySingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.yavuz.rencber.rencber.activity.AppController.TAG;


public class BildirimlerAdapter extends RecyclerView.Adapter<BildirimlerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<BildirimYorumlar> chatRoomArrayList;
    private static String today;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,user_email,user_name,user_id;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String cinsiyet, grup;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message, timestamp, count;
        NetworkImageView foto;
        public Button btn_arkadas_onayla;
        public Button btn_arkadas_reddet;

        public ViewHolder(View view) {
            super(view);
            foto = (NetworkImageView) view.findViewById(R.id.ivresimler);
            name = (TextView) view.findViewById(R.id.name);
            btn_arkadas_onayla = (Button) view.findViewById(R.id.btn_arkadas_onayla);
            btn_arkadas_reddet = (Button) view.findViewById(R.id.btn_arkadas_reddet);

        }
    }


    public BildirimlerAdapter(Context mContext, ArrayList<BildirimYorumlar> chatRoomArrayList) {
        this.mContext = mContext;
        this.chatRoomArrayList = chatRoomArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bildirimler_arkadaslar_list_row, parent, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());//preferences objesi
        editor = preferences.edit();
        user_email=preferences.getString("user_email","boş");
        user_id=preferences.getString("user_id","boş");
        user_name=preferences.getString("user_name","boş");
        eposta=preferences.getString("email","bos");
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BildirimYorumlar chatRoom = chatRoomArrayList.get(position);

        holder.name.setText(chatRoom.getName());



            // thumbnail image
            holder.foto.setImageUrl(Config.SERVER_IS + chatRoom.getFoto(), imageLoader);

        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, KullaniciProfili.class);

                i.putExtra("to_user_id", chatRoom.getTo_user_id());

                i.putExtra("to_eposta", chatRoom.getEmail());

                mContext.startActivity(i);
            }
        });
        holder.btn_arkadas_onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // builder.setCancelable(false);
                // builder.setTitle("Bu uygulamaya Oy vermek istermisiniz?");
                builder.setMessage(mContext.getString(R.string.addfriendlist));
                builder.setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


               /* Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                startActivity(intent);
                */

                        ArkadasEkle(chatRoom.getEmail(), eposta,chatRoom.getId());
                        // chatRoomArrayList.remove(position);

                       holder.btn_arkadas_onayla.setVisibility(View.GONE);
                        holder.btn_arkadas_reddet.setVisibility(View.GONE);
                        holder.itemView.setVisibility(View.GONE);

                    }
                });
                builder.setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                //super.onBackPressed();


            }

        });
        holder.btn_arkadas_reddet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // builder.setCancelable(false);
                // builder.setTitle("Bu uygulamaya Oy vermek istermisiniz?");
                builder.setMessage(mContext.getString(R.string.deletefollowersrequest));
                builder.setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


               /* Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                startActivity(intent);
                */

                        ArkadasIstekSil(chatRoom.getEmail(), eposta,chatRoom.getId());
                        // chatRoomArrayList.remove(position);
                        holder.btn_arkadas_onayla.setVisibility(View.GONE);
                        holder.btn_arkadas_reddet.setVisibility(View.GONE);
                        holder.itemView.setVisibility(View.GONE);
                    }
                });
                builder.setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                //super.onBackPressed();

            }
        });

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
    private void ArkadasEkle(final String email, final String user_email,final String b_no) {

        String url = Config.SERVER_IS+"arkadasOnay.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(mContext.getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                        Log.d("response", response);
                        if(response.contains("1")){


                            Toast.makeText(mContext.getApplicationContext(), mContext.getResources().getString(R.string.friendadded), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mContext.getApplicationContext(), mContext.getResources().getString(R.string.alreadyfriendlist), Toast.LENGTH_SHORT).show();
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
                params.put("txtKullanici", email);
                params.put("txtfriend",user_email);
                params.put("txtb_no",b_no);

                // params.put("txtDil",dil);

                return params;
            }
        };

        MySingleton.getInstance(mContext.getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void ArkadasIstekSil(final String email, final String user_email,final String b_no) {

        String url = Config.SERVER_IS+"arkadasIstekSil.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("1")){


                            Toast.makeText(mContext.getApplicationContext(), mContext.getResources().getString(R.string.deletefriendlist), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mContext.getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
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
                params.put("txtKullanici", email);
                params.put("txtfriend",user_email);
                params.put("txtb_no",b_no);

                // params.put("txtDil",dil);

                return params;
            }
        };

        MySingleton.getInstance(mContext.getApplicationContext()).addToRequestQueue(stringRequest);
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private BildirimlerAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final BildirimlerAdapter.ClickListener clickListener) {
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
