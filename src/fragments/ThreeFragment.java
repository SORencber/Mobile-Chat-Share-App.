package com.yavuz.rencber.rencber.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.yavuz.rencber.rencber.GCM.SimpleDividerItemDecoration;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.Trends.TrendsKullanicilar;
import com.yavuz.rencber.rencber.Trends.TrendsResimler;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.KullaniciProfili;
import com.yavuz.rencber.rencber.activity.ShowPopUp;
import com.yavuz.rencber.rencber.adapter.TrendKullanicilarAdapter;
import com.yavuz.rencber.rencber.adapter.TrendsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Handler;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Serhat Ömer RENÇBER on 2018.
 */
public class ThreeFragment extends Fragment   {

    View vi;
    private static final String TAG = ThreeFragment.class.getSimpleName();
    public static int count;
    // Movies json url
    String url ;
    // Movies json url



    protected Handler handler;

    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,user_email,user_name,user_id;
    private ArrayList<TrendsResimler> toResimlerArrayList;
    private TrendsAdapter mAdapter;
    private ArrayList<TrendsKullanicilar> toTrendsKllanicilar;
    private TrendKullanicilarAdapter mAdapter2;
    private ArrayList<TrendsKullanicilar> toPuanKazandiranlar;
    private TrendKullanicilarAdapter mAdapter3;

    private RecyclerView recyclerView1,recyclerView2,recyclerView3;
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_three, container, false);




        recyclerView1 = (RecyclerView) rootView.findViewById(R.id.recycler_view_1);

        recyclerView2 = (RecyclerView) rootView.findViewById(R.id.recycler_view_2);
        recyclerView3 = (RecyclerView) rootView.findViewById(R.id.recycler_view_3);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());//preferences objesi
        editor = preferences.edit();
        user_email=preferences.getString("user_email","boş");
        user_id=preferences.getString("user_id","boş");
        user_name=preferences.getString("user_name","boş");
        eposta=preferences.getString("email","bos");

        topResimlerGetir();
        topKullanicilarGetir();
        puanKazandiranlariGetir();

        toResimlerArrayList = new ArrayList<>();
        mAdapter = new TrendsAdapter(getActivity(), toResimlerArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setMinimumWidth(200);
        recyclerView1.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()        ));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(mAdapter);

        toTrendsKllanicilar = new ArrayList<>();
        mAdapter2 = new TrendKullanicilarAdapter(getActivity(), toTrendsKllanicilar);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setMinimumWidth(200);
        recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()        ));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapter2);


        toPuanKazandiranlar = new ArrayList<>();
        mAdapter3 = new TrendKullanicilarAdapter(getActivity(), toPuanKazandiranlar);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setMinimumWidth(200);
        recyclerView3.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()        ));
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setAdapter(mAdapter3);

        recyclerView1.addOnItemTouchListener(new TrendsAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView1, new TrendsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                TrendsResimler topResimler = toResimlerArrayList.get(position);
                Intent intent = new Intent(getActivity(), ShowPopUp.class);
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
        recyclerView2.addOnItemTouchListener(new TrendKullanicilarAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView2, new TrendKullanicilarAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                TrendsKullanicilar chatRoom = toTrendsKllanicilar.get(position);
                Intent intent = new Intent(getActivity(), KullaniciProfili.class);
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                intent.putExtra("to_eposta", chatRoom.getEmail());

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        recyclerView3.addOnItemTouchListener(new TrendKullanicilarAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView3, new TrendKullanicilarAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                TrendsKullanicilar chatRoom2 = toPuanKazandiranlar.get(position);
                Intent intent = new Intent(getActivity(), KullaniciProfili.class);
                intent.putExtra("to_user_id", chatRoom2.getTo_user_id());
                intent.putExtra("to_eposta", chatRoom2.getEmail());

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




        return rootView;


    }




    private void topResimlerGetir() {
        // Toast.makeText(getActivity().getApplicationContext(),eposta,Toast.LENGTH_LONG).show();
        String url=Config.SERVER_IS+"top10.php";
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

                                TrendsResimler tr = new TrendsResimler();
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
        String url=Config.SERVER_IS+"topusers.php";
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
                                TrendsKullanicilar cr = new TrendsKullanicilar();
                                cr.setTo_user_id(obj.getString("kayit_no"));
                                cr.setEmail(obj.getString("eposta"));
                                cr.setFoto(obj.getString("foto"));
                                cr.setId(obj.getString("kayit_no"));
                                cr.setGrup(obj.getString("kayit_no"));
                                cr.setSahibi(obj.getString("kayit_no"));
                                String grup=obj.getString("kayit_no");
                                if (grup.equals("kayit_no")) {
                                    cr.setName(obj.getString("adi"));

                                }else {
                                    cr.setName(obj.getString("adi"));
                                }


                                cr.setCinsiyet(obj.getString("cinsiyet"));
                                cr.setLastMessage("");
                                cr.setUnreadCount(0);
                                cr.setTimestamp(obj.getString("kayit_no"));

                                toTrendsKllanicilar.add(cr);
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



    private void puanKazandiranlariGetir() {
        // Toast.makeText(getActivity().getApplicationContext(),eposta,Toast.LENGTH_LONG).show();
        String url=Config.SERVER_IS+"puanKazandiranKullanicilar.php";
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
                                TrendsKullanicilar cr = new TrendsKullanicilar();
                                cr.setTo_user_id(obj.getString("kayit_no"));
                                cr.setEmail(obj.getString("eposta"));
                                cr.setFoto(obj.getString("foto"));
                                cr.setId(obj.getString("kayit_no"));
                                cr.setGrup(obj.getString("kayit_no"));
                                cr.setSahibi(obj.getString("kayit_no"));
                                String grup=obj.getString("kayit_no");
                                if (grup.equals("kayit_no")) {
                                    cr.setName(obj.getString("adi"));

                                }else {
                                    cr.setName(obj.getString("adi"));
                                }


                                cr.setCinsiyet(obj.getString("cinsiyet"));
                                cr.setLastMessage("");
                                cr.setUnreadCount(0);
                                cr.setTimestamp(obj.getString("kayit_no"));

                                toPuanKazandiranlar.add(cr);
                            }  catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                            }
                        }




                        mAdapter3.notifyDataSetChanged();

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





    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {

        super.onPause();
    }


















}