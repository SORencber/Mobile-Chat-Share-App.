package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Ömer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.yavuz.rencber.rencber.Profile.ProfileResimler;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.MySingleton;
import com.yavuz.rencber.rencber.activity.ShowPopUp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfileResimlerDoldurAdapter extends RecyclerView.Adapter<ProfileResimlerDoldurAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ProfileResimler> trendsResimlerArrayList;
    private static String today;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
   String cinsiyet;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvyukari, tvasagi,timestamp,count;
        NetworkImageView ivresimler;
        ImageView ivResimSil;
        public ViewHolder(View view) {
            super(view);
            ivresimler=(NetworkImageView)view.findViewById(R.id.ivresimler);
            tvasagi = (TextView) view.findViewById(R.id.tvasagi);
            tvyukari = (TextView) view.findViewById(R.id.tvyukari);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            count = (TextView) view.findViewById(R.id.count);
            ivResimSil=(ImageView)view.findViewById(R.id.ivResimSil);
        }
    }


    public ProfileResimlerDoldurAdapter(Context mContext, ArrayList<ProfileResimler> trendsResimlerArrayList) {
        this.mContext = mContext;
        this.trendsResimlerArrayList = trendsResimlerArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_profile_resim_doldur, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ProfileResimler trendsResimler = trendsResimlerArrayList.get(position);
        final int sil=trendsResimler.getSil();
        int toplamoy=trendsResimler.getAsagi()+trendsResimler.getYukari();
        double YukariYuzde=trendsResimler.getYukari()*100/toplamoy;
        double AsagiYuzde=trendsResimler.getAsagi()*100/toplamoy;
        holder.tvyukari.setText("%"+String.valueOf(YukariYuzde));
        holder.tvasagi.setText( "%"+String.valueOf(AsagiYuzde));
        //holder..setText(trendsResimler.getAdi());
       // holder.message.setText(trendsResimler.getLastMessage());
     /*   if (trendsResimler.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(trendsResimler.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }
*/
       // holder.timestamp.setText(getTimeStamp(trendsResimler.getTimestamp()));
        if (sil==0) { holder.ivResimSil.setVisibility(View.INVISIBLE);}


            // thumbnail image
            holder.ivresimler.setImageUrl(Config.SERVER_IS+trendsResimler.getThumbnailUrl(),imageLoader);

        holder.ivresimler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowPopUp.class);
                intent.putExtra("resimno", String.valueOf(trendsResimler.getResim_no()));
                intent.putExtra("resimyol", trendsResimler.getThumbnailUrl());
                intent.putExtra("resimbaslik", trendsResimler.getTitle());
                if (sil==0) { intent.putExtra("silmek", "0");
                    }else {

                    intent.putExtra("silmek", "1");
                }

              Toast.makeText(mContext,String.valueOf(sil),Toast.LENGTH_LONG).show();

                // intent.putExtra("to_user_id", topResimler.getTo_user_id());
                //  intent.putExtra("foto", topResimler.getFoto());
                mContext.startActivity(intent);
            }
        });
        holder.ivResimSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                // builder.setCancelable(false);
                builder.setTitle("Silmek istediğinize emin misiniz?");
             //   builder.setMessage(mContext.getString(R.string.addfriendlist));
                builder.setPositiveButton(mContext.getString(R.string.yes),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


               /* Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                startActivity(intent);
                */

                        deleteResim(trendsResimler.getResim_no(),trendsResimler.getThumbnailUrl());
                        trendsResimlerArrayList.remove(position);
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

                AlertDialog alert=builder.create();
                alert.show();
                //super.onBackPressed();


            }
        });


    }
    private void deleteResim(final int resim_no,final String thumbnail) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"sil.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d(TAG, response);
                        if(response.contains("success_login")){
                            //refresh listview
                        }
                        else{
                            Toast.makeText(mContext, "Birşeyler Yanlış Gitti", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtresim_no", (String.valueOf(resim_no)) );
                params.put("txtthumbnail", (String.valueOf(thumbnail)) );

                return params;
            }
        };

        MySingleton.getInstance(mContext.getApplicationContext()).addToRequestQueue(stringRequest);
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
        private ProfileResimlerDoldurAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ProfileResimlerDoldurAdapter.ClickListener clickListener) {
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
