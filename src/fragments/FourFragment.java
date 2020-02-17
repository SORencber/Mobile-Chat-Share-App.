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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.yavuz.rencber.rencber.GCM.SimpleDividerItemDecoration;
import com.yavuz.rencber.rencber.Profile.ProfileResimler;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Ayarlar;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.MySingleton;
import com.yavuz.rencber.rencber.activity.PuanHarca;
import com.yavuz.rencber.rencber.activity.CustomVolleyRequest;
import com.yavuz.rencber.rencber.activity.MainActivity;
import com.yavuz.rencber.rencber.adapter.ProfileResimlerDoldurAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Handler;
/**
 * Created by Serhat Ömer RENÇBER on 2018.
 */

public class FourFragment extends Fragment {
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String bit,eposta,adi,soyadi,dil,cinsiyet;
    private NetworkImageView imageView;
    private ArrayList<ProfileResimler> toResimlerArrayList;
    private ProfileResimlerDoldurAdapter mAdapter;
    // Movies json url
    private RecyclerView recyclerView;

    TextView tvAdi,tvBegenen,tvTakipci,tvPuan,baslik,pid,tvProfilKonum;
    private static final String TAG = FourFragment.class.getSimpleName();
    public static int count;
    // Movies json url
    String url ;
    ImageView ivsettings,ivLogout,ivstar1,ivstar2,ivstar3,ivstar4,ivstar5,ivHarca,ivResimSil;
    int silmek;

    // Movies json url

    Spinner spinnerSonuc;
    View rootView;

    protected Handler handler;
    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


         rootView = inflater.inflate(R.layout.fragment_four, container, false);

        tvAdi=(TextView)rootView.findViewById(R.id.tvProfilAdıSoyadi);
        tvBegenen=(TextView)rootView.findViewById(R.id.tvBegenen);
        tvTakipci=(TextView)rootView.findViewById(R.id.tvTakipci);
        tvPuan=(TextView)rootView.findViewById(R.id.tvPuan);

        baslik =(TextView)rootView.findViewById(R.id.baslik);
        tvProfilKonum =(TextView)rootView.findViewById(R.id.tvProfilKonum);

        pid=(TextView)rootView.findViewById(R.id.pid);
        imageView = (NetworkImageView) rootView.findViewById(R.id.ivProfile);

        ivLogout=(ImageView)rootView.findViewById(R.id.ivLogout);
        ivsettings=(ImageView)rootView.findViewById(R.id.ivsettings);
        ivstar1=(ImageView)rootView.findViewById(R.id.ivStar_1);
        ivstar2=(ImageView)rootView.findViewById(R.id.ivStar_2);
        ivstar3=(ImageView)rootView.findViewById(R.id.ivStar_3);
        ivstar4=(ImageView)rootView.findViewById(R.id.ivStar_4);
        ivstar5=(ImageView)rootView.findViewById(R.id.ivStar_5);

        ivHarca=(ImageView)rootView.findViewById(R.id.ivHarca);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_profile_doldur);
        ivResimSil=(ImageView)rootView.findViewById(R.id.ivResimSil);

        spinnerSonuc=(Spinner)rootView.findViewById(R.id.spinnerSonuc);


        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());//preferences objesi
        editor = preferences.edit();

        toResimlerArrayList = new ArrayList<>();
        mAdapter = new ProfileResimlerDoldurAdapter(getActivity(), toResimlerArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //url = Config.SERVER_IS + "benimresimlerim.php?eposta="+eposta;
        //fetchMovies(url,silmek);

        spinnerSonuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {


                switch (pos) {

                    case 1:
                        try {

                            url = Config.SERVER_IS + "benimresimlerim.php?eposta="+eposta;
                            silmek=1;
                            fetchMovies(url,silmek);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case 2:

                        try {

                            url = Config.SERVER_IS + "begendiklerim.php?eposta="+eposta;
                            silmek=0;

                            fetchMovies(url,silmek);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:

                        try {

                            url = Config.SERVER_IS + "begenmediklerim.php?eposta="+eposta;
                            silmek=0;

                            fetchMovies(url,silmek);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case 4:
                        try {

                            url = Config.SERVER_IS + "yorumlarim.php?eposta="+eposta;
                            silmek=0;

                            fetchMovies(url,silmek);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });







        ivHarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("foto","1"); //Galeriye girerken foto ekleme ayari icin 1 konumuna getiriyoruz.
                editor.commit();// degerleri telefona kaydediyoruz

                Intent i = new Intent(getActivity().getApplicationContext(), PuanHarca.class);
                startActivity(i);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("foto","1"); //Galeriye girerken foto ekleme ayari icin 1 konumuna getiriyoruz.
                editor.commit();// degerleri telefona kaydediyoruz

                Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivityForResult(i, 10001);
            }
        });
  ivsettings.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Intent i = new Intent(getActivity(), Ayarlar.class);
          startActivity(i);
      }
  });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logOut();
                editor.putString("giris","0");
                editor.commit();
                getActivity().finish();
            }
        });


        // Inflate the layout for this fragment
            eposta=preferences.getString("email","boş");
        bit=preferences.getString("bit","boş");
//Toast.makeText(getActivity().getApplicationContext(),bit,Toast.LENGTH_LONG).show();
        adi=preferences.getString("adi","boş");
            soyadi=preferences.getString("soyadi","boş");
            dil=preferences.getString("dil","boş");
            cinsiyet=preferences.getString("cinsiyet","boş");



        String dil= Locale.getDefault().getDisplayLanguage();

       // tvDil.setText(dil);


        getProfile("1",eposta);
        getProfile("2",eposta);
        return rootView;
    }

    public void getProfile(final String a,String eposta){

        if(a=="1")

        {
            url = Config.SERVER_IS + "profile.php?eposta=" + eposta + "&istek=1";
        }   else{
            url = Config.SERVER_IS + "profile2.php?eposta=" + eposta + "&istek=1";
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
                        Toast.makeText(getActivity().getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    private void showJSON(String response){
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
     /*   int Puan=Integer.parseInt(toplamyukari);
        int Puan2 =Integer.parseInt(toplamasagi);
        int ToplamPuanlar=Puan - Puan2;
        int ToplamPuan= ToplamPuanlar*5;
       */

        tvBegenen.setText( getActivity().getResources().getString(R.string.likes)+": " + toplamyukari);

    }
    private void showJSON2(String response){

        String toplamoy="";

         String foto="";
           String sqladi = "";
        String sqlsoyadi = "";
        String sqlcinsiyet = "";
        String takipci="";


        try {
            //JSONObject jsonObject = new JSONObject(response);
            JSONArray result = new JSONArray(response);
            //JSONArray result = jsonObject.getJSONArray(response);
            JSONObject collegeData = result.getJSONObject(0);




           // toplamoy = collegeData.getString("toplamoy");

            foto = collegeData.getString("foto");
             sqladi = collegeData.getString("adi");
            sqlsoyadi = collegeData.getString("soyadi");
                   sqlcinsiyet=collegeData.getString("cinsiyet");
            takipci=collegeData.getString("takipci");
            String Puan=collegeData.getString("Puan");
            String Ulke=collegeData.getString("Ulke");
            String Sehir=collegeData.getString("Sehir");
            if(tvProfilKonum.equals("")) {
                tvProfilKonum.setVisibility(View.INVISIBLE); }else {
                tvProfilKonum.setText(Ulke+"/"+Sehir);
            }
            int ToplamPuan= Integer.parseInt(Puan);
            tvTakipci.setText(getActivity().getResources().getString(R.string.followers)+" : " + String.valueOf(takipci));
            tvPuan.setText(getActivity().getResources().getString(R.string.point)+": " + String.valueOf(ToplamPuan));

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


            if(sqlcinsiyet.equals("Bay"))
            {
                loadImage(Config.SERVER_IS + "varsayilan/male.png");
            }else{

                loadImage(Config.SERVER_IS + "varsayilan/female.png");
            }

        }else  {
            loadImage(Config.SERVER_IS + "" + foto);
        }

    }


    private void loadImage(String s){



        ImageLoader imageLoader = CustomVolleyRequest.getInstance(getActivity().getApplicationContext())
                .getImageLoader();
        imageLoader.get(s, ImageLoader.getImageListener(imageView,
                R.drawable.image, android.R.drawable
                        .ic_dialog_alert));
        imageView.setImageUrl(s, imageLoader);
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub



    }




    private void deleteResim(final int resim_no,final String thumbnail) {

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





    public  void fetchMovies(final String url,final int silmek){

      // if (silmek==0) { ivResimSil.setVisibility(View.INVISIBLE);}
        toResimlerArrayList.clear();
     //   final Movie m = null;
        // Creating volley request obj
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
                                ProfileResimler movie = new ProfileResimler();
                                movie.setTitle(obj.getString("resim_baslik"));

                                movie.setAsagi(obj.getInt("asagi"));

                                // movie.setYear(obj.getInt("yukari"));
                                movie.setYukari(obj.getInt("yukari"));
                                movie.setThumbnailUrl(obj.getString("resim_adi"));
                                movie.setResim_no(obj.getInt("resim_no"));
                                movie.setYorumsayisi(obj.getInt("yorumsayisi"));
                               // movie.setTimestamp(obj.getString("tarih"));
                                movie.setSil(silmek);

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



    }



}
