package com.yavuz.rencber.rencber.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.yavuz.rencber.rencber.GCM.Message;
import com.yavuz.rencber.rencber.GCM.SimpleDividerItemDecoration;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Ayarlar;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.CustomVolleyRequest;
import com.yavuz.rencber.rencber.activity.LinearLayoutManagerWrapper;
import com.yavuz.rencber.rencber.activity.MainActivity;
import com.yavuz.rencber.rencber.activity.Movie;
import com.yavuz.rencber.rencber.activity.ResimAyarlaActivity;
import com.yavuz.rencber.rencber.activity.SallaBulActivity;
import com.yavuz.rencber.rencber.adapter.ChatRoomThreadAdapter;
import com.yavuz.rencber.rencber.adapter.OyverListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;
import static com.facebook.FacebookSdk.getApplicationContext;
/**
 * Created by Serhat Ömer RENÇBER on 2018.
 */

public class OyverFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    ImageView Kamera,Ayarlar,Salla;
    private static Toast toast;

    View vi,toasview;
    TextView baslik,pid,tvYukari,tvAsagi,etToplamOy;
    public ProgressDialog loading;

    final String TAG = this.getClass().getSimpleName();
    private NetworkImageView imageView,ivFotoRastgele;
    private ImageLoader imageLoader;

    // String resimbaslik="";
    String resimyol="";
    String resimno = "";
    String asagi="";
    String yukari = "";
    String notr = "";
    String Fotocu="";
    String epostaFoto="";
    String name="";
    String to_user_id="";
    String to_eposta="";
    EditText yorum;
    String resimbaslik = null;
    String url,sehir,ilce;
    Bundle extras;
    private SwipeRefreshLayout swipeRefreshLayout2;
    // Movies json url
    public ArrayList<Movie> movieList;
    public ListView listView;
    private OyverListAdapter adapter2;
    String dil,dbdil,eposta,secenek;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<Message> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //////////////////////////////////
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    ///////////////////////////////////
    private AdView mAdView;
    public OyverFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          vi= inflater.inflate(R.layout.fragment_oyver,container,false);
        Kamera=(ImageView)vi.findViewById(R.id.ivOyverKamera);
        Salla=(ImageView)vi.findViewById(R.id.ivSalla);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6206182306880992~9183316463");

       // mAdView = (AdView) vi.findViewById(R.id.adView);
       // AdRequest adRequest = new AdRequest.Builder().build();
      //  mAdView.loadAd(adRequest);



        Ayarlar=(ImageView)vi.findViewById(R.id.ivOyverAyarlar);
        //listView=(ListView)vi.findViewById(R.id.list_oyver_akis);


        //  spinner=(Spinner)vi.findViewById(R.id.spinner);
        toasview = inflater.inflate(R.layout.toastsonuclar,   (ViewGroup)vi.findViewById(R.id.toastsonuc));
        tvYukari=(TextView) toasview.findViewById(R.id.tvYukari);
        tvAsagi=(TextView) toasview.findViewById(R.id.tvAsagi);
        etToplamOy=(TextView) toasview.findViewById(R.id.etUsername);
        // Inflate the layout for this fragment
        swipeRefreshLayout2=(SwipeRefreshLayout)vi.findViewById(R.id.swipe_refresh_layout_oyver);
        swipeRefreshLayout2.setOnRefreshListener(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());//preferences objesi
        editor = preferences.edit();
        eposta=preferences.getString("email","bos");
        secenek=preferences.getString("secenek","0");
        sehir=preferences.getString("sehir","bos");
        ilce=preferences.getString("ilce","bos");
       // Toast.makeText(getContext(),eposta+"--"+secenek+"---"+sehir+"---"+ilce +""+url,LENGTH_LONG).show();




        recyclerView = (RecyclerView)vi.findViewById(R.id.recycler_view_oy_ver);

        movieList = new ArrayList<>();
        adapter2 = new OyverListAdapter(getActivity(), movieList);

        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManagerWrapper(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter2);
        //layoutManager.setReverseLayout(true) ; // sohbeti tersten baslatir
        //layoutManager.setStackFromEnd(true);

        Kamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("foto","0"); //Galeriye girerken foto ekleme ayari icin 1 konumuna getiriyoruz.
                editor.commit();// degerleri telefona kaydediyoruz

                //  CropImage.startPickImageActivity(getActivity());
                Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
//
                startActivity(i);

                //  selectImage();
            }
        });
        Ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  CropImage.startPickImageActivity(getActivity());
                Intent i = new Intent(getActivity().getApplicationContext(), ResimAyarlaActivity.class);
//
                startActivity(i);

                //  selectImage();
            }
        });

        Salla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  CropImage.startPickImageActivity(getActivity());
                Intent i = new Intent(getActivity().getApplicationContext(), SallaBulActivity.class);
//
                startActivity(i);

                //  selectImage();
            }
        });
        fetchMovies();
        swipeRefreshLayout2.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                        secenek=preferences.getString("secenek","0");
                        sehir=preferences.getString("sehir","bos");
                        ilce=preferences.getString("ilce","bos");
                        fetchMovies();
                    }
                }
        );
        return vi;
    }

    @Override
    public void onRefresh() {

    }



    private  void fetchMovies(){
        //swipeRefreshLayout2.setRefreshing(true);
        //  listView.setSelection(listView.getAdapter().getCount()-1);

        url = Config.SERVER_IS+"Oyverresimler.php?eposta="+eposta+"&secenek="+secenek+"&Sehir="+sehir+"&ilce="+ilce;
        Log.e(TAG,eposta+"--"+secenek+"---"+sehir+"---"+ilce +""+url);
         //Toast.makeText(getContext(),eposta+"--"+secenek+"---"+sehir+"---"+ilce +""+url,LENGTH_LONG).show();
        final Movie m = null;
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        movieList.clear();


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = null;
                                try {
                                    obj = response.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Movie movie = new Movie();
                                movie.setAdi(obj.getString("adi"));
                                movie.setEposta(obj.getString("eposta"));
                                movie.setResim_no(obj.getInt("resim_no"));
                                movie.setThumbnailUrl(obj.getString("resim_adi"));
                                movie.setKayit_no(obj.getInt("kayit_no"));
                                movie.setTitle(obj.getString("resim_baslik"));
                                movie.setUlke(obj.getString("Ulke"));
                                movie.setSehir(obj.getString("Sehir"));
                                movie.setIlce(obj.getString("ilce"));
                                movie.setAdres(obj.getString("adres"));

                                //movie.setKayit_no(obj.getInt("olayid"));
                                //movie.setOy(obj.getInt("oy"));
                                //  Toast.makeText(getApplicationContext(),silmek,Toast.LENGTH_LONG).show();
                                //movie.setSil(Integer.parseInt(silmek));
                                ///movie.setMesaj(obj.getString("yorum"));
                                movie.setDiL(obj.getString("dil"));
                                movie.setFoto(obj.getString("foto"));

                                // Genre is json array
                               /* JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);
*/
                                // adding movie to movies array

                                movieList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        recyclerView.stopScroll();
                        adapter2.notifyDataSetChanged();

                        swipeRefreshLayout2.setRefreshing(false);
                     // listView.setSelection(listView.getAdapter().getCount()-1);

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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);



    }

    public void randomData(final String a,int pos){



        String url= Config.SERVER_IS+"rastgele.php?kategori="+pos;

        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (a=="baslangic"){
                    showJSON(response,"baslangic");}
                else{showJSON(response,"cikis");}}
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(),error.getMessage().toString(), LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    public void showJSON(String response,String data) {
        try {
            //JSONObject jsonObject = new JSONObject(response);
            JSONArray result = new JSONArray(response);
            //JSONArray result = jsonObject.getJSONArray(response);
            JSONObject collegeData = result.getJSONObject(0);


            resimbaslik = collegeData.getString(Config.RESIM_BASLIK);
            resimyol = collegeData.getString(Config.RESIM_YOL);
            resimno = collegeData.getString(Config.RESIM_NO);
            asagi = collegeData.getString(Config.RESIM_ASAGI);
            yukari = collegeData.getString(Config.RESIM_YUKARI);
            dbdil = collegeData.getString("dil");
            Fotocu = collegeData.getString("foto");
            epostaFoto = collegeData.getString("eposta");
            to_user_id = collegeData.getString("kayit_no");




        } catch (JSONException e) {
            e.printStackTrace();
        }


        /// butona tıkladıktan sonra son oranları gösterir

        if (resimno != "") {
            int x = Integer.valueOf(yukari);
            int y = Integer.valueOf(asagi);
            float sonucasagi = y * 100 / (x + y);
            float sonucyukari = x * 100 / (x + y);
            String asagix = String.valueOf(sonucasagi);
            String yukarix = String.valueOf(sonucyukari);
            int toplam = x + y;

            if (data != "baslangic") {
                if (toast != null)
                    toast.cancel();

                tvAsagi.setText("%" + String.valueOf(asagix));
                tvYukari.setText("%" + String.valueOf(yukarix));
                // etToplamOy.setText("Toplam Oy: "+String.valueOf(toplam));
                toast = new Toast(getActivity());
                toast.getHorizontalMargin();
                toast.setView(toasview);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 900);
                if ((resimbaslik.equals("") || resimbaslik.equals("null"))) {
                    baslik.setVisibility(View.GONE);
                  //  ivCevir.setVisibility(View.GONE);

                } else
                {         baslik.setText(resimbaslik);
                }
                //toast =  Toast.makeText(getActivity().getApplicationContext(),"text",Toast.LENGTH_SHORT);
                //toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                //toast.setText( "Beğeni:  % "+yukarix+"\t\tBeğenmeyen %:"+asagix+"\n\n\nToplam Oy:"+String.valueOf(toplam));
                //toast.show();
            }

            if ((resimbaslik.equals("") || resimbaslik.equals("null"))) {
                baslik.setVisibility(View.GONE);
              //  ivCevir.setVisibility(View.GONE);

            } else
            {
                baslik.setVisibility(View.VISIBLE);
                //ivCevir.setVisibility(View.VISIBLE);
                baslik.setText(resimbaslik);
            }

            pid.setText(resimno);
            loadImage(Config.SERVER_IS+""+resimyol);
            loadFoto(Config.SERVER_IS+""+Fotocu);

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
    private void loadFoto(String s){



        ImageLoader imageLoader = CustomVolleyRequest.getInstance(getActivity().getApplicationContext())
                .getImageLoader();
        imageLoader.get(s, ImageLoader.getImageListener(ivFotoRastgele,
                R.drawable.image, android.R.drawable
                        .ic_dialog_alert));

        ivFotoRastgele.setImageUrl(s, imageLoader);
    }

}
