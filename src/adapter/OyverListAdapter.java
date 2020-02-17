package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Ömer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
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
import com.squareup.picasso.Picasso;
import com.yavuz.rencber.rencber.GCM.ChatRoom;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.KullaniciProfili;
import com.yavuz.rencber.rencber.activity.Movie;
import com.yavuz.rencber.rencber.activity.MySingleton;
import com.yavuz.rencber.rencber.activity.ShowYorum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class OyverListAdapter extends RecyclerView.Adapter<OyverListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Movie> movieArrayList;
    private static String today;
   String grup;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;

    String eposta,cinsiyet,gelenresimno,resimyol,resimbaslik;
    Movie m;
    Movie m2;
    private static final String TAG = OyverListAdapter.class.getSimpleName();


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message, timestamp, count;
        ImageView ivOyverKonum,ivOyverAsagi,ivOyverYukari,ivOyverYorum,ivYorumCevir,ivOyverShare,ivOyverLiked,ivOyverUnLiked,ivbenimyorumYukari,ivbaskayorumsil;
        TextView mesaj,tvOyverKonum,tvAd;
        ImageView thumbNail;
        NetworkImageView foto;
        public ViewHolder(View view) {
            super(view);
             thumbNail = (ImageView) view.findViewById(R.id.ivOyverResim);
            //thumbNail.zoomTo(123,1000);
            foto = (NetworkImageView) view
                    .findViewById(R.id.ivOyverFoto);
            mesaj = (TextView) view.findViewById(R.id.tvResimBaslik);
            tvAd = (TextView) view.findViewById(R.id.tvAd);

            ivOyverAsagi= (ImageView) view.findViewById(R.id.ivOyverAsagi);
            ivOyverYukari= (ImageView) view.findViewById(R.id.ivOyverYukarı);

            ivOyverYorum= (ImageView) view.findViewById(R.id.ivOyverYorum);
            ivOyverShare= (ImageView) view.findViewById(R.id.ivOyverShare);
            ivOyverKonum= (ImageView) view.findViewById(R.id.ivOyverKonum);
            tvOyverKonum=(TextView)view.findViewById(R.id.tvOyverKonum);
            ivOyverLiked= (ImageView) view.findViewById(R.id.ivOyverLiked);
            ivOyverUnLiked= (ImageView) view.findViewById(R.id.ivOyverUnLiked);


        }
    }


    public OyverListAdapter(Context mContext, ArrayList<Movie> movieArrayList) {
        this.mContext = mContext;
        this.movieArrayList = movieArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_oyver, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());//preferences objesi
        editor = preferences.edit();
        final Movie m = movieArrayList.get(position);





            // thumbnail image
        Picasso.with(mContext).load(Config.SERVER_IS + "" + m.getThumbnailUrl()).into(holder.thumbNail);
        //   thumbNail.setImageUrl(Config.SERVER_IS + "" + m.getThumbnailUrl(), imageLoader);
        holder.foto.setImageUrl(Config.SERVER_IS + "" + m.getFoto(), imageLoader);
        holder.mesaj.setText(m.getTitle());
        holder.tvAd.setText(m.getAdi()+":");
        if(m.getAdres().equals("")) {
            holder.tvOyverKonum.setVisibility(View.INVISIBLE);
            holder.ivOyverKonum.setVisibility(View.INVISIBLE);
        }else {
            holder.tvOyverKonum.setVisibility(View.VISIBLE);
            holder.ivOyverKonum.setVisibility(View.VISIBLE);
            holder.tvOyverKonum.setText(m.getIlce() + "/" + m.getSehir());
        }


        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, KullaniciProfili.class);

                i.putExtra("to_user_id",m.getKayit_no());

                i.putExtra("to_eposta",  m.getEposta());

                mContext.startActivity(i);
            }
        });

        holder.ivOyverAsagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendData("2","",String.valueOf(m.getResim_no()),m.getThumbnailUrl(),m.getEposta(),m.getDiL(),m.getTitle());
                //  Movie sonraki=movieItems.get(position-1);

                holder.ivOyverUnLiked.setVisibility(View.VISIBLE);
                holder.ivOyverAsagi.setVisibility(View.INVISIBLE);
                holder.ivOyverYukari.setEnabled(false);
            }
        });


        holder.ivOyverYukari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("1","",String.valueOf(m.getResim_no()),m.getThumbnailUrl(),m.getEposta(),m.getDiL(),m.getTitle());

                holder.ivOyverYukari.setVisibility(View.INVISIBLE);
                holder.ivOyverLiked.setVisibility(View.VISIBLE);
                holder.ivOyverAsagi.setEnabled(false);
            }
        });

        holder.ivOyverYorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowYorum.class);
                intent.putExtra("to_user_id", String.valueOf(m.getEposta()));
                intent.putExtra("resimno", String.valueOf(m.getResim_no()));
                intent.putExtra("resimyol", m.getThumbnailUrl());
                intent.putExtra("resimbaslik", "selam");
                intent.putExtra("sahibi", "");
                intent.putExtra("grup", "");

                intent.putExtra("silmek", "0");

                // intent.putExtra("to_user_id", topResimler.getTo_user_id());
                //  intent.putExtra("foto", topResimler.getFoto());
                mContext.startActivity(intent);
            }
        });
        holder.ivOyverShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareItem(holder.thumbNail);

            }
        });

    }
    public void setItems(ArrayList<Movie> movieArrayList) {
        //get the current items
        int currentSize = movieArrayList.size();
        //remove the current items
        movieArrayList.clear();
        //add all the new items
        movieArrayList.addAll(movieArrayList);
        //tell the recycler view that all the old items are gone
        notifyItemRangeRemoved(0, currentSize);
        //tell the recycler view how many new items we added
        notifyItemRangeInserted(0, movieArrayList.size());
    }
    public void onShareItem(ImageView foto) {
        // Get access to bitmap image from view
       // ImageView ivImage = (ImageView)findViewById(R.id.ivResult);
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(foto);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_REFERRER, "KanKa'ya sor cevabını al");
         //   shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, "<a href=\"https://play.google.com/store/apps/details?id=com.yavuz.rencber.rencber\"> KanKa Uygulamasını İndir</a>");

            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            mContext.startActivity(Intent.createChooser(shareIntent, "KanKa Resim Paylaşımı"));
        } else {
            // ...sharing failed, handle error
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    @Override
    public int getItemCount() {


        return movieArrayList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
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
    private void sendData(final String oy, String yorumum, final  String resim_no, final String resimyol,
                          final String to_eposta, final String dil, final String resimbaslik) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());//preferences objesi
        editor = preferences.edit();
        // Inflate the layout for this fragment

        eposta = preferences.getString("email", "boş");
        cinsiyet = preferences.getString("cinsiyet", "null");
        yorumum="";
        String url = Config.SERVER_IS+"Olaylar.php";
        final String finalYorumum = yorumum;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("success_login")){


                           // Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();



                        }
                        else{
                            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
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
                params.put("txtpid", resim_no);
                params.put("txtoy",oy.toString());
                params.put("txtyorum", finalYorumum.toString());
                params.put("txtkullanici",eposta);
                params.put("txtDil",dil);
                params.put("txt_to_eposta",to_eposta);
                params.put("txtresimyol",resimyol);
                params.put("txtresimbaslik",resimbaslik);
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
        private OyverListAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final OyverListAdapter.ClickListener clickListener) {
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
