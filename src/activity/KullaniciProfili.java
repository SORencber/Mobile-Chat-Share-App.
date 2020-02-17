package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;
import com.android.volley.toolbox.ImageLoader;
import com.yavuz.rencber.rencber.GCM.*;
import com.yavuz.rencber.rencber.Profile.ProfileKullanicilar;
import com.yavuz.rencber.rencber.Profile.ProfileResimler;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.adapter.CustomListAdapter;
import com.yavuz.rencber.rencber.adapter.ProfileKullanicilarAdapter;
import com.yavuz.rencber.rencber.adapter.ProfileResimlerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Handler;

import static com.facebook.FacebookSdk.getApplicationContext;


public class KullaniciProfili extends Activity implements SwipeRefreshLayout.OnRefreshListener {


    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,adi,soyadi,dil,cinsiyet,bit;
    private NetworkImageView imageView;
    public String foto="";
    // Movies json url
    public ArrayList<Movie> movieList = new ArrayList<Movie>();
    private CustomListAdapter adapter1;
    TextView tvAdi,tvBegenen,tvTakipci,tvVerilenOy,tvPuan,baslik,pid,tvKullaniciProfilKonum;
    private static final String TAG = KullaniciProfili.class.getSimpleName();
    public static int count;
    // Movies json url
    String url,usereposta;
    ImageView ivstar1,ivstar2,ivstar3,ivstar4,ivstar5,ivSendMessage,ivArkadasEkle,ivZilCal,ivZilKes,ivcloseprofile,ivKullaniciSil,ivKullaniciEngelle;


    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private String to_user_id,to_eposta;

    protected Handler handler;



    ////////////////////ryclerview
    private ArrayList<ProfileResimler> toResimlerArrayList;
    private ProfileResimlerAdapter mAdapter;

    private ArrayList<ProfileKullanicilar> toTrendsKllanicilar;
    private ProfileKullanicilarAdapter mAdapter2;

    private RecyclerView recyclerView1,recyclerView2;





    public KullaniciProfili() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kullanici_profili);



        tvAdi=(TextView)findViewById(R.id.tvProfilAdıSoyadi);
        tvBegenen=(TextView)findViewById(R.id.tvBegenen);
        tvTakipci=(TextView)findViewById(R.id.tvTakipci);
        tvPuan=(TextView)findViewById(R.id.tvPuan);
        tvKullaniciProfilKonum=(TextView)findViewById(R.id.tvKullaniciProfilKonum);
        baslik =(TextView)findViewById(R.id.baslik);
        pid=(TextView)findViewById(R.id.pid);
        imageView = (NetworkImageView) findViewById(R.id.ivProfile);
        ivstar1=(ImageView)findViewById(R.id.ivStar_1);
        ivstar2=(ImageView)findViewById(R.id.ivStar_2);
        ivstar3=(ImageView)findViewById(R.id.ivStar_3);
        ivstar4=(ImageView)findViewById(R.id.ivStar_4);
        ivstar5=(ImageView)findViewById(R.id.ivStar_5);
        ivZilCal=(ImageView)findViewById(R.id.ivZilCal);
        ivZilKes=(ImageView)findViewById(R.id.ivZilKes);
        ivKullaniciSil=(ImageView)findViewById(R.id.ivKullaniciSil);
        ivKullaniciEngelle=(ImageView)findViewById(R.id.ivEngelle);

        ivcloseprofile=(ImageView)findViewById(R.id.ivcloseProfile);

        ivArkadasEkle=(ImageView)findViewById(R.id.ivArkadasEkle);
        ivSendMessage=(ImageView)findViewById(R.id.ivSendMessage);
        recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view__profile_resimler);

        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view__profile_kullanicilar);
      //  btnsendmessage=(ImageButton)findViewById(R.id.btnsendmessage);
        //ivLogout=(ImageView)findViewById(R.id.ivLogout);
        //  btnToplamOy=(Button) rootView.findViewById(R.id.btntoplamoy);


        preferences = PreferenceManager.getDefaultSharedPreferences(KullaniciProfili.this);//preferences objesi
        editor = preferences.edit();
        eposta=preferences.getString("email","boş");
        adi=preferences.getString("adi","boş");
        soyadi=preferences.getString("soyadi","boş");
        dil=preferences.getString("dil","boş");
        cinsiyet=preferences.getString("cinsiyet","boş");
       // to_user_id=preferences.getString("to_user_id","boş");
        bit=preferences.getString("bit","boş");
        if(bit.toString().equals("1"))
        {
            ivKullaniciSil.setVisibility(View.VISIBLE);
            ivKullaniciEngelle.setVisibility(View.VISIBLE);
        }else{ ivKullaniciSil.setVisibility(View.INVISIBLE);
        ivKullaniciEngelle.setVisibility(View.INVISIBLE);}


        Intent extras = getIntent();
         to_user_id = extras.getStringExtra("to_user_id");
         to_eposta = extras.getStringExtra("to_eposta");

        getProfile("1", to_eposta);
        getProfile("2", to_eposta);
        //Toast.makeText(KullaniciProfili.this,to_eposta , Toast.LENGTH_SHORT).show();
        ArkadaslikDurum(to_eposta,eposta);

        //Profil resimlerini doldur
        topResimlerGetir();

        //Profil arkadaslarini doldur
        topKullanicilarGetir();


        toResimlerArrayList = new ArrayList<>();
        mAdapter = new ProfileResimlerAdapter(KullaniciProfili.this, toResimlerArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setMinimumHeight(200);

        recyclerView1.setMinimumWidth(200);
        recyclerView1.addItemDecoration(new SimpleDividerItemDecoration(
               getApplicationContext()        ));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(mAdapter);

        toTrendsKllanicilar = new ArrayList<>();
        mAdapter2 = new ProfileKullanicilarAdapter(KullaniciProfili.this, toTrendsKllanicilar);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setMinimumHeight(200);

        recyclerView2.setMinimumWidth(200);
        recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()        ));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapter2);



        recyclerView1.addOnItemTouchListener(new ProfileResimlerAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView1, new ProfileResimlerAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                ProfileResimler topResimler = toResimlerArrayList.get(position);
                Intent intent = new Intent(getApplication(), ShowPopUp.class);
                intent.putExtra("resimno", String.valueOf(topResimler.getResim_no()));
                intent.putExtra("resimyol", topResimler.getThumbnailUrl());
                intent.putExtra("resimbaslik", topResimler.getTitle());

                intent.putExtra("silmek", "0");

                // intent.putExtra("to_user_id", topResimler.getTo_user_id());
                //  intent.putExtra("foto", topResimler.getFoto());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView2.addOnItemTouchListener(new ProfileKullanicilarAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView2, new ProfileKullanicilarAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                ProfileKullanicilar chatRoom = toTrendsKllanicilar.get(position);
                Intent intent = new Intent(getApplicationContext(), KullaniciProfili.class);
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                intent.putExtra("to_eposta", chatRoom.getEmail());

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        ivKullaniciEngelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showPopup(v,foto);

                AlertDialog.Builder builder=new AlertDialog.Builder(KullaniciProfili.this);
                // new android.support.v7.app.AlertDialog.Builder(this)

                builder.setTitle("Bu kullaniciyi süresiz engellemek istermisiniz?");
                //builder.setMessage(getApplicationContext().getString(R.string.addfriendlist));
                builder.setPositiveButton(getApplicationContext().getString(R.string.yes),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


               /* Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                startActivity(intent);
                */

                        KullaniciEngelle(to_eposta,eposta);
                        // ivArkadasEkle.setVisibility(View.INVISIBLE);
                        //  ivcheck.setVisibility(View.VISIBLE);
                        //  ivSendMessage.setVisibility(View.INVISIBLE);


                    }
                });
                builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.no),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                        //  ivArkadasEkle.setVisibility(View.VISIBLE);
                        // ivcheck.setVisibility(View.INVISIBLE);
                        // ivSendMessage.setVisibility(View.INVISIBLE);

                    }
                });

                AlertDialog alert=builder.create();
                alert.show();
                //super.onBackPressed();

            }
        });
        ivKullaniciSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showPopup(v,foto);

                AlertDialog.Builder builder=new AlertDialog.Builder(KullaniciProfili.this);
                // new android.support.v7.app.AlertDialog.Builder(this)

                builder.setTitle("Bu kullaniciyi kalici olarak silmek istermisiniz?");
                //builder.setMessage(getApplicationContext().getString(R.string.addfriendlist));
                builder.setPositiveButton(getApplicationContext().getString(R.string.yes),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


               /* Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                startActivity(intent);
                */

                        KullaniciSil(to_eposta,eposta);
                        // ivArkadasEkle.setVisibility(View.INVISIBLE);
                        //  ivcheck.setVisibility(View.VISIBLE);
                        //  ivSendMessage.setVisibility(View.INVISIBLE);


                    }
                });
                builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.no),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                        //  ivArkadasEkle.setVisibility(View.VISIBLE);
                        // ivcheck.setVisibility(View.INVISIBLE);
                        // ivSendMessage.setVisibility(View.INVISIBLE);

                    }
                });

                AlertDialog alert=builder.create();
                alert.show();
                //super.onBackPressed();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showPopup(v,foto);
                Intent intent = new Intent(getApplicationContext(), FullscreenActivity.class);
                intent.putExtra("resimyol", foto);



                startActivity(intent);
            }
        });
        ivcloseprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showPopup(v,foto);
               finish();
            }
        });
        ivArkadasEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(KullaniciProfili.this);
               // new android.support.v7.app.AlertDialog.Builder(this)
                // builder.setCancelable(false);
                // builder.setTitle("Bu uygulamaya Oy vermek istermisiniz?");
                builder.setMessage(getApplicationContext().getString(R.string.addfriendlist));
                builder.setPositiveButton(getApplicationContext().getString(R.string.yes),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


               /* Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                startActivity(intent);
                */

                        ArkadasEkle(to_eposta,eposta);
                       // ivArkadasEkle.setVisibility(View.INVISIBLE);
                      //  ivcheck.setVisibility(View.VISIBLE);
                      //  ivSendMessage.setVisibility(View.INVISIBLE);


                    }
                });
                builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.no),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                      //  ivArkadasEkle.setVisibility(View.VISIBLE);
                       // ivcheck.setVisibility(View.INVISIBLE);
                       // ivSendMessage.setVisibility(View.INVISIBLE);

                    }
                });

                AlertDialog alert=builder.create();
                alert.show();
                //super.onBackPressed();

            }
        });


        ivSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                        sendMessage(to_eposta,eposta);
                    }

        });

    // Inflate the layout for this fragment
        ivZilCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder=new AlertDialog.Builder(KullaniciProfili.this);
                // new android.support.v7.app.AlertDialog.Builder(this)
                // builder.setCancelable(false);
                builder.setTitle("Bildirimleri Açmak istiyormusunuz?");
               // builder.setMessage(getApplicationContext().getString(R.string.addfriendlist));
                builder.setPositiveButton(getApplicationContext().getString(R.string.yes),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


               /* Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                startActivity(intent);
                */

                        BildirimDuzenle(to_eposta,eposta,"0");
                        ivZilKes.setVisibility(View.VISIBLE);
                        ivZilCal.setVisibility(View.INVISIBLE);


                    }
                });
                builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.no),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                        //  ivArkadasEkle.setVisibility(View.VISIBLE);
                        // ivcheck.setVisibility(View.INVISIBLE);
                        // ivSendMessage.setVisibility(View.INVISIBLE);

                    }
                });

                AlertDialog alert=builder.create();
                alert.show();
                //super.onBackPressed();

            }
        });

        ivZilKes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder=new AlertDialog.Builder(KullaniciProfili.this);
                // new android.support.v7.app.AlertDialog.Builder(this)
                // builder.setCancelable(false);

                builder.setTitle("Bildirimleri Kapatmak istiyormusunuz?");
                // builder.setMessage(getApplicationContext().getString(R.string.addfriendlist));
                builder.setPositiveButton(getApplicationContext().getString(R.string.yes),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


               /* Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                startActivity(intent);
                */

                        BildirimDuzenle(to_eposta,eposta,"1");
                        ivZilCal.setVisibility(View.VISIBLE);
                        ivZilKes.setVisibility(View.INVISIBLE);


                    }
                });
                builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.no),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                        //  ivArkadasEkle.setVisibility(View.VISIBLE);
                        // ivcheck.setVisibility(View.INVISIBLE);
                        // ivSendMessage.setVisibility(View.INVISIBLE);

                    }
                });

                AlertDialog alert=builder.create();
                alert.show();
                //super.onBackPressed();

            }
        });

        String dil= Locale.getDefault().getDisplayLanguage();

        // tvDil.setText(dil);


    }
    private void BildirimDuzenle(final String email, final String user_email, final String sessiz) {

        String url = Config.SERVER_IS+"bildirimDuzenle.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("1")){


                          //  Toast.makeText(getApplicationContext().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        }
                        else{
                          //  Toast.makeText(getApplicationContext().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtKullanici", email);
                params.put("txtfriend",user_email);
                params.put("sessiz",sessiz);

                // params.put("txtDil",dil);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext().getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void ArkadasEkle(final String email, final String user_email) {

        String url = Config.SERVER_IS+"arkadasEkle.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("1")){


                            Toast.makeText(getApplicationContext().getApplicationContext(), getApplicationContext().getResources().getString(R.string.sentrequest), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext().getApplicationContext(), getApplicationContext().getResources().getString(R.string.alreadyfriendlist), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtKullanici", email);
                params.put("txtfriend",user_email);

                // params.put("txtDil",dil);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext().getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void KullaniciEngelle(final String email, final String user_email) {

        String url = Config.SERVER_IS+"kullaniciEngelle.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("1")){


                            Toast.makeText(getApplicationContext().getApplicationContext(), "Tamamdır", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext().getApplicationContext(), "Olmadı", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtKullanici", email);
                params.put("txtfriend",user_email);

                // params.put("txtDil",dil);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext().getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void KullaniciSil(final String email, final String user_email) {

        String url = Config.SERVER_IS+"kullaniciSil.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("1")){


                            Toast.makeText(getApplicationContext().getApplicationContext(), getApplicationContext().getResources().getString(R.string.sentrequest), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext().getApplicationContext(), getApplicationContext().getResources().getString(R.string.alreadyfriendlist), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtKullanici", email);
                params.put("txtSilen",user_email);

                // params.put("txtDil",dil);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext().getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void ArkadaslikDurum(final String email, final String user_email) {

        String url = Config.SERVER_IS+"arkadaslikDurum.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("1")){


                            ivArkadasEkle.setVisibility(View.INVISIBLE);
                            ivZilCal.setVisibility(View.VISIBLE);
                            ivZilKes.setVisibility(View.INVISIBLE);
                            ivSendMessage.setVisibility(View.VISIBLE);
                        }  if(response.contains("2")){


                            ivArkadasEkle.setVisibility(View.INVISIBLE);
                            ivZilCal.setVisibility(View.INVISIBLE);
                            ivZilKes.setVisibility(View.VISIBLE);
                            ivSendMessage.setVisibility(View.VISIBLE);
                        }
                        else if(response.contains("0")){

                            toResimlerArrayList.clear();
                            ivArkadasEkle.setVisibility(View.VISIBLE);
                            ivZilCal.setVisibility(View.INVISIBLE);

                            ivZilKes.setVisibility(View.INVISIBLE);
                            ivSendMessage.setVisibility(View.INVISIBLE);                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtKullanici", email);
                params.put("txtfriend",user_email);

                // params.put("txtDil",dil);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void topResimlerGetir() {
        // Toast.makeText(getActivity().getApplicationContext(),eposta,Toast.LENGTH_LONG).show();
        String url=Config.SERVER_IS+"benimresimlerim.php?eposta=" + to_eposta;
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = null;
                                try {
                                    obj = response.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                ProfileResimler tr = new ProfileResimler();
                                tr.setTitle(obj.getString("resim_baslik"));

                                tr.setAsagi(obj.getInt("asagi"));

                                // movie.setYear(obj.getInt("yukari"));
                                tr.setYukari(obj.getInt("yukari"));
                                tr.setThumbnailUrl(obj.getString("resim_adi"));
                                tr.setResim_no(obj.getInt("resim_no"));
                                tr.setYorumsayisi(obj.getInt("yorumsayisi"));

                                toResimlerArrayList.add(tr);
                            }  catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                            }
                        }




                        mAdapter.notifyDataSetChanged();

                        // subscribing to all chat room topics
                        // subscribeToAllTopics();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "Communication Error!", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Authentication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Server Side Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "Parse Error!", Toast.LENGTH_SHORT).show();
                }
            }

        });
        AppController.getInstance().addToRequestQueue(movieReq);

        //Adding request to request queue
        //  MyApplication.getInstance().addToRequestQueue(strReq);
        // MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(movieReq);
    }

    private void topKullanicilarGetir() {
        // Toast.makeText(getActivity().getApplicationContext(),eposta,Toast.LENGTH_LONG).show();
        String url=Config.SERVER_IS+"arkadaslar.php?eposta="+to_eposta;
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = null;
                                try {
                                    obj = response.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ProfileKullanicilar cr = new ProfileKullanicilar();
                                cr.setTo_user_id(obj.getString("kayit_no"));
                                cr.setEmail(obj.getString("eposta"));
                                cr.setFoto(obj.getString("foto"));
                                cr.setId(obj.getString("no"));
                                cr.setGrup(obj.getString("grup"));
                                cr.setSahibi(obj.getString("sahibi"));
                                String grup=obj.getString("grup");
                                if (grup.equals("0")) {
                                    cr.setName(obj.getString("adi"));

                                }else {
                                    cr.setName(obj.getString("kullanici"));
                                }
                                cr.setTo_user_id(obj.getString("kayit_no"));

                                cr.setFoto(obj.getString("foto"));
                                cr.setCinsiyet(obj.getString("cinsiyet"));
                                cr.setLastMessage("");
                                cr.setUnreadCount(0);
                                cr.setTimestamp(obj.getString("tarih"));
                                Log.e(TAG, "grup error: " + grup);
                                if (grup.equals("0")) {
                                    toTrendsKllanicilar.add(cr);

                                }
                            }  catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                            }
                        }




                        mAdapter2.notifyDataSetChanged();

                        // subscribing to all chat room topics
                        // subscribeToAllTopics();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "Communication Error!", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Authentication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Server Side Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "Parse Error!", Toast.LENGTH_SHORT).show();
                }
            }

        });
        AppController.getInstance().addToRequestQueue(movieReq);

        //Adding request to request queue
        //  MyApplication.getInstance().addToRequestQueue(strReq);
        // MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(movieReq);
    }

    // subscribing to global topic



    public void showPopup(View anchorView,final String resim) {
//        LayoutInflater.from(mContext)
        //              .inflate(R.layout.popup_layout_resim_goster, null);

        final View popupView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.popup_layout_resim_goster, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        // Example: If you have a TextView inside `popup_layout.xml`
        ImageView close= (ImageView)popupView.findViewById(R.id.close2);
        NetworkImageView buyukresimlimesaj=(NetworkImageView)popupView.findViewById(R.id.buyukresimlimesaj);
        buyukresimlimesaj.setImageUrl(Config.SERVER_IS  + resim, imageLoader);

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }
        });

      /*  Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // close your dialog
                popupWindow.dismiss();
            }

        }, 10000);
*/
        //  tv.setText(....);

        // Initialize more widgets from `popup_layout.xml`


        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

    }
















    public void getProfile(final String a,String to_user_id){


            if (a == "1")

            {
                url = Config.SERVER_IS + "profile.php?eposta=" + to_user_id + "&istek=1";
            } else {
                url = Config.SERVER_IS + "profile2.php?eposta=" + to_user_id + "&istek=1";
            }




        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (a=="1"){
                    showJSON(response);}
                else{showJSON2(response);}}
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(KullaniciProfili.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(KullaniciProfili.this);
        requestQueue.add(stringRequest);


    }

    public void showJSON(String response){
        String toplamyukari="";
        String toplamasagi = "";

        try {
            //JSONObject jsonObject = new JSONObject(response);
            JSONArray result = new JSONArray(response);
            //JSONArray result = jsonObject.getJSONArray(response);
            JSONObject collegeData = result.getJSONObject(0);



            toplamyukari = collegeData.getString("toplamyukari");
            toplamasagi = collegeData.getString("toplamasagi");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (toplamyukari.equals(""))
        {
            toplamyukari="0";
        }

        if (toplamasagi.equals(""))
        {
            toplamasagi="0";

        }

        tvBegenen.setText(getApplicationContext().getResources().getString(R.string.likes)+": " + toplamyukari);


    }
    public void showJSON2(String response){

        String toplamoy="";

        String sqladi = "";
        String sqlsoyadi = "";
        String sqlcinsiyet = "";
        String takipci="";
        //String foto="";

        try {
            //JSONObject jsonObject = new JSONObject(response);
            JSONArray result = new JSONArray(response);
            //JSONArray result = jsonObject.getJSONArray(response);
            JSONObject collegeData = result.getJSONObject(0);




            // toplamoy = collegeData.getString("toplamoy");
            usereposta=collegeData.getString("eposta");

            foto = collegeData.getString("foto");
            sqladi = collegeData.getString("adi");
            sqlsoyadi = collegeData.getString("soyadi");
            sqlcinsiyet=collegeData.getString("cinsiyet");
            takipci=collegeData.getString("takipci");
            String Puan=collegeData.getString("Puan");
            String Ulke=collegeData.getString("Ulke");
            String Sehir=collegeData.getString("Sehir");

            if (Puan.equals("0") || Puan.equals("null") || Puan.equals("NULL"))
            {
                Puan="0";
            }
            int ToplamPuan= Integer.parseInt(Puan);
            tvTakipci.setText(getApplicationContext().getResources().getString(R.string.followers)+": "+ String.valueOf(takipci));
            tvPuan.setText(getApplicationContext().getResources().getString(R.string.point)+": "+ String.valueOf(ToplamPuan));
            if(Sehir.equals("")) {
                tvKullaniciProfilKonum.setVisibility(View.INVISIBLE); }else {
                tvKullaniciProfilKonum.setText(Ulke+"/"+Sehir);
            }
            if (ToplamPuan >=0 && ToplamPuan <=2000)
            {
                ivstar1.setVisibility(View.VISIBLE);
                ivstar2.setVisibility(View.INVISIBLE);
                ivstar3.setVisibility(View.INVISIBLE);
                ivstar4.setVisibility(View.INVISIBLE);
                ivstar5.setVisibility(View.INVISIBLE);

            }
            else if (ToplamPuan>2000 && ToplamPuan<3000)
            {
                ivstar1.setVisibility(View.VISIBLE);
                ivstar2.setVisibility(View.VISIBLE);

                ivstar3.setVisibility(View.INVISIBLE);
                ivstar4.setVisibility(View.INVISIBLE);
                ivstar5.setVisibility(View.INVISIBLE);

            } else if (ToplamPuan>3000 && ToplamPuan<4000)
            {

                ivstar1.setVisibility(View.VISIBLE);
                ivstar2.setVisibility(View.VISIBLE);
                ivstar3.setVisibility(View.VISIBLE);
                ivstar4.setVisibility(View.INVISIBLE);
                ivstar5.setVisibility(View.INVISIBLE);
            } else if (ToplamPuan >=4000 && ToplamPuan<30000)
            {

                ivstar1.setVisibility(View.VISIBLE);
                ivstar2.setVisibility(View.VISIBLE);
                ivstar3.setVisibility(View.VISIBLE);
                ivstar4.setVisibility(View.VISIBLE);
                ivstar5.setVisibility(View.INVISIBLE);
            }else if (ToplamPuan>30000) {

                ivstar1.setVisibility(View.VISIBLE);
                ivstar2.setVisibility(View.VISIBLE);
                ivstar3.setVisibility(View.VISIBLE);
                ivstar4.setVisibility(View.VISIBLE);
                ivstar5.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvAdi.setText(sqladi+"   "+sqlsoyadi);


        //btnToplamOy.setText("Kullanılan Oy  :"+toplamoy);
        if(foto.equals("null")) {


            if(sqlcinsiyet.toString().equals("Bay"))
            {
                imageView.setImageUrl(Config.SERVER_IS + "varsayilan/male.png",imageLoader);

            }else{

                imageView.setImageUrl(Config.SERVER_IS + "varsayilan/female.png",imageLoader);
            }

        }else  {
            imageView.setImageUrl(Config.SERVER_IS + "" + foto,imageLoader);
        }

     /*   Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(context)
                .load(url)
                .fit()
                .transform(transformation)
                .into(imageView);
*/
    }




    @Override
    public void onRefresh() {

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void sendMessage(final String email, final String user_email) {

        //  Toast.makeText(getApplicationContext(),isRoomId(),Toast.LENGTH_LONG).show();
        String endPoint;

        endPoint = Config.SERVER_IS + "profildenarkadaslar.php";

        Log.e(TAG, "endpoint: " + endPoint);


        StringRequest strReq = new StringRequest(Request.Method.POST,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONObject commentObj = obj.getJSONObject("odabilgileri");
                        Log.e(TAG, "json parsing error: " + response);
                        String dbname = "";


                        String dbgrup = commentObj.getString("grup");
                        if (dbgrup.equals("0")) {
                            dbname = commentObj.getString("adi");

                        } else {
                            dbname = commentObj.getString("kullanici");
                        }
                        String dbsahibi = commentObj.getString("sahibi");
                        String dbroom = commentObj.getString("no");
                        String dbfoto = commentObj.getString("foto");

                        Intent intent = new Intent(KullaniciProfili.this, ChatRoomActivity.class);
                        intent.putExtra("chat_room_id", dbroom);
                        intent.putExtra("name", dbname);
                        intent.putExtra("to_user_id", to_user_id);
                        intent.putExtra("to_eposta", to_eposta);

                        intent.putExtra("foto", dbfoto);
                        intent.putExtra("odaIsmi", dbname);

                        if (dbgrup.equals("") || dbgrup.equals("0") || dbgrup.equals("null")) {
                            intent.putExtra("grup", "0");


                        } else {
                            intent.putExtra("grup", "1");

                        }
                        if (dbsahibi.equals("") || dbsahibi.equals("0") || dbsahibi.equals("null")) {
                            intent.putExtra("sahibi", "0");


                        } else {
                            intent.putExtra("sahibi", "1");

                        }
                        startActivity(intent);


                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "Communication Error!", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Authentication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Server Side Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "Parse Error!", Toast.LENGTH_SHORT).show();
                }
            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("txtKullanici", email);
                params.put("txtfriend", user_email);




                Log.e(TAG, "Params: " + params.toString());

                return params;
            }

            ;

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };


        // disabling retry policy so that it won't make
        // multiple http calls
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);

        //Adding request to request queue
        // MyApplication.getInstance().addToRequestQueue(strReq);
        AppController.getInstance().addToRequestQueue(strReq);

    }
}
