package com.yavuz.rencber.rencber.fragments;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.yavuz.rencber.rencber.GCM.SimpleDividerItemDecoration;
import com.yavuz.rencber.rencber.Profile.ProfileResimler;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.MySingleton;
import com.yavuz.rencber.rencber.adapter.ProfileResimlerDoldurAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Serhat Ömer RENÇBER on 2018.
 */

public class ResimlerimFragment  extends Fragment {

    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta, adi, soyadi, dil, cinsiyet;
    private NetworkImageView imageView;
    private ArrayList<ProfileResimler> toResimlerArrayList;
    private ProfileResimlerDoldurAdapter mAdapter;
    // Movies json url
    private RecyclerView recyclerView;

    TextView tvAdi, tvBegenen, tvTakipci, tvPuan, baslik, pid;
    private static final String TAG = FourFragment.class.getSimpleName();
    public static int count;
    // Movies json url
    String url;
    ImageView ivLogout, ivstar1, ivstar2, ivstar3, ivstar4, ivstar5, ivHarca, ivResimSil;
    int silmek;
    View rootView1;

    public ResimlerimFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView1 = inflater.inflate(R.layout.fragment_resimlerim, container, false);
        recyclerView = (RecyclerView) rootView1.findViewById(R.id.recycler_view_profile_doldur);
        ivResimSil=(ImageView)rootView1.findViewById(R.id.ivResimSil);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());//preferences objesi
        editor = preferences.edit();
        // Inflate the layout for this fragment
        eposta=preferences.getString("email","boş");
        adi=preferences.getString("adi","boş");
        soyadi=preferences.getString("soyadi","boş");
        dil=preferences.getString("dil","boş");
        cinsiyet=preferences.getString("cinsiyet","boş");

        toResimlerArrayList = new ArrayList<>();
        mAdapter = new ProfileResimlerDoldurAdapter(getActivity(), toResimlerArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        fetchMovies();
        return rootView1;
    }

    private  void fetchMovies(){


        //    Toast.makeText(getActivity(),String.valueOf(url),Toast.LENGTH_LONG).show();
         //ivResimSil.setVisibility(View.VISIBLE);
             toResimlerArrayList.clear();
            //   final Movie m = null;
            // Creating volley request obj
            JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("donus", response.toString());

                            Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = null;
                                    try {
                                        obj = response.getJSONObject(i);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    ProfileResimler movie = new ProfileResimler();
                                    movie.setTitle(obj.getString("resim_baslik"));

                                    movie.setAsagi(obj.getInt("asagi"));

                                    // movie.setYear(obj.getInt("yukari"));
                                    movie.setYukari(obj.getInt("yukari"));
                                    movie.setThumbnailUrl(obj.getString("resim_adi"));
                                    movie.setResim_no(obj.getInt("resim_no"));
                                    movie.setYorumsayisi(obj.getInt("yorumsayisi"));
                                  //  Toast.makeText(getActivity(),movie.getResim_no(),Toast.LENGTH_LONG).show();
                                    // Genre is json array
                               /* JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);
*/
                                    // adding movie to movies array
                                    toResimlerArrayList.add(movie);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            mAdapter.notifyDataSetChanged();

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(movieReq);



        }  private void deleteResim(final int resim_no,final String thumbnail) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"sil.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("success_login")){
                            //refresh listview
                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "Birşeyler Yanlış Gitti", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
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

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

}