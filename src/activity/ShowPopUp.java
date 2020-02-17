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
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.adapter.CustomListAdapter2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Handler;


public class ShowPopUp extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    PopupWindow popUp;
    LinearLayout layout;
    TextView tvResimBaslikMesaji,tvYukariBilgi,tvAsagiBilgi;
    LinearLayout.LayoutParams params;
    LinearLayout mainLayout;
    Button but,btnSendMessage;
    NetworkImageView ivResimSahibiFoto;
    ImageView ivResim;
    EditText etMessage;
    boolean click = true;
    View vi;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private static final String TAG = ShowPopUp.class.getSimpleName();
    public static int count;
    ImageView ivSil,ivResimYukari,ivResimAsagi,ivKapatResim,ivZilCal,ivZilKes;
    // Movies json url
    String url;
    Bundle extras;
    private SwipeRefreshLayout swipeRefreshLayout2;
    // Movies json url
    public ArrayList<Movie> movieList = new ArrayList<Movie>();
    public ListView listView;
    private CustomListAdapter2 adapter2;
    String dil,dbdil;
    protected Handler handler;

    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,bit,gelenresimno,resimyol,resimbaslik,resimBaslik,resimAdi,resimSahibiFotosu,resimSahibiId,resimSahibiAdi,resimSahibiSoyadi;
    public  String silmek,resimSahibi,fotonunepostasi,fotonunId;
    public int Asagi,Yukari;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_pop_up);



        listView = (ListView) findViewById(R.id.listyorum);
        ivResim=(ImageView) findViewById(R.id.ivResim);
        btnSendMessage=(Button)findViewById(R.id.btnSendMessage);
        etMessage=(EditText)findViewById(R.id.tvResimBaslik);
        swipeRefreshLayout2=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout2);
        swipeRefreshLayout2.setOnRefreshListener(this);
        ivSil=(ImageView)findViewById(R.id.ivSil);
        ivResimAsagi=(ImageView)findViewById(R.id.ivResimAsagi);
        ivResimYukari=(ImageView)findViewById(R.id.ivResimYukari);
        ivKapatResim=(ImageView)findViewById(R.id.ivKapatResim);
        ivZilCal=(ImageView)findViewById(R.id.ivZilCal);
        ivZilKes=(ImageView)findViewById(R.id.ivZilKes);


        tvAsagiBilgi=(TextView)findViewById(R.id.tvAsagiBilgi);
        tvYukariBilgi=(TextView)findViewById(R.id.tvYukariBilgi);
        tvResimBaslikMesaji=(TextView)findViewById(R.id.tvResimBaslikMesaji);
        ivResimSahibiFoto=(NetworkImageView)findViewById(R.id.ivOyverFoto);
       // ivReply=(ImageView)findViewById(R.id.ivReply);
        dil= Locale.getDefault().getLanguage();
        preferences = PreferenceManager.getDefaultSharedPreferences(ShowPopUp.this);//preferences objesi
        editor = preferences.edit();
        // Inflate the layout for this fragment

        eposta=preferences.getString("email","boş");
        bit=preferences.getString("bit","boş");

        swipeRefreshLayout2.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        fetchMovies();
                    }
                }
        );
        extras = getIntent().getExtras();
        gelenresimno=extras.getString("resimno");
        resimyol = extras.getString("resimyol");
        resimbaslik = extras.getString("resimbaslik");
        silmek = extras.getString("silmek");
     // Toast.makeText(getApplicationContext(),silmek.toString(),Toast.LENGTH_LONG).show();

       // ivResim.setImageUrl(Config.SERVER_IS+""+resimyol, imageLoader);
        Picasso.with(this).load(Config.SERVER_IS + "" +resimyol).into(ivResim);

        adapter2 = new CustomListAdapter2(ShowPopUp.this, movieList);
        listView.setAdapter(adapter2);

        ivSil=(ImageView)findViewById(R.id.ivSil);



        if(bit.toString().equals("1"))
            silmek="1";

        if (Integer.valueOf(silmek)==1)
        {

            ivSil.setVisibility(View.VISIBLE);
            //silmek="0";
        }

        fetchMovies();

        fetchResimBilgileriGetir(gelenresimno);

ivKapatResim.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});


        btnSendMessage.setOnClickListener(new View.OnClickListener()

                                          {

                                              @Override
                                              public void onClick(View v) {
                                                  if(etMessage.getText().length()>0){
                                                      sendMessage();}else{
                                                      etMessage.setError(getApplicationContext().getResources().getString(R.string.plsyourcomment));
                                                  }

                                              }
                                          }
        );
        ivSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kapat();


            }
        });
        ivZilCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(ShowPopUp.this);
                // new android.support.v7.app.AlertDialog.Builder(this)
                // builder.setCancelable(false);
                builder.setTitle(getApplicationContext().getResources().getString(R.string.doyouwanttoopennotification));

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
                        ivZilKes.setVisibility(View.VISIBLE);
                        ivZilCal.setVisibility(View.INVISIBLE);
                        resimBildirimAcKapat("0");



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

                AlertDialog.Builder builder=new AlertDialog.Builder(ShowPopUp.this);
                // new android.support.v7.app.AlertDialog.Builder(this)
                // builder.setCancelable(false);
                builder.setTitle(getApplicationContext().getResources().getString(R.string.doyouwanttoclosenotification));

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

                        ivZilKes.setVisibility(View.INVISIBLE);
                        ivZilCal.setVisibility(View.VISIBLE);
                        resimBildirimAcKapat("1");




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
        ivResimSahibiFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), KullaniciProfili.class);

                i.putExtra("to_user_id", fotonunId);

                i.putExtra("to_eposta",fotonunepostasi );

                startActivity(i);

            }
        });
        ivResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FullscreenActivity.class);
                intent.putExtra("resimyol", resimyol);



                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {


                //  deleteResim(movieList.get(i).getResim_no(),movieList.get(i).getThumbnailUrl());
               /* Intent intent = new Intent(ShowPopUp.this, OzelMesaj.class);
                intent.putExtra("resimno",gelenresimno);
                intent.putExtra("resimyol",resimyol);
                intent.putExtra("resimbaslik",resimbaslik);
               // intent.putExtra("silmek",String.valueOf(silmek));
                intent.putExtra("gelenEposta",movieList.get(i).getEposta());
                startActivity(intent);

                // movieList.remove(movieList.get(i));
                // adapter1.notifyDataSetChanged();

                //  swipeRefreshLayout.setRefreshing(true);
*/

                // Toast.makeText(getActivity().getApplicationContext(),(String.valueOf(movieList.get(i).getResim_no())) , Toast.LENGTH_SHORT).show();
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
                        Log.d(TAG, response);
                        if(response.contains("success_login")){

                        }
                        else{
                            Toast.makeText(ShowPopUp.this.getApplicationContext(), "Birşeyler Yanlış Gitti", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowPopUp.this.getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
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

        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public void Kapat() {
// TODO Auto-generated method stub
        AlertDialog.Builder builder=new AlertDialog.Builder(ShowPopUp.this);
        // builder.setCancelable(false);
        // builder.setTitle("Bu uygulamaya Oy vermek istermisiniz?");
        builder.setMessage(getApplicationContext().getResources().getString(R.string.delete));
        builder.setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(ShowPopUp.this, getResources().getString(R.string.deleted), Toast.LENGTH_LONG).show();
                deleteResim(Integer.valueOf(gelenresimno),resimyol);
                finish();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                //Toast.makeText(ShowPopUp.this, "Devam ediyoruz", Toast.LENGTH_LONG).show();
                dialog.cancel();

            }
        });


        AlertDialog alert=builder.create();
        alert.show();
        //super.onBackPressed();
    }

    private void sendMessage() {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"sendmesaj.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("success_login")){
                            // Toast.makeText(ShowPopUp.this, response, Toast.LENGTH_SHORT).show();
                            etMessage.setText("");

                            ///mesajdan sonra uyari
                           // displayNotificationOne();

                            ///klavyeyi gizler
                            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                            fetchMovies();
                        }
                        else{
                            Toast.makeText(ShowPopUp.this, "Birşeyler Yanlış Gitti", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowPopUp.this, "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtMessage", etMessage.getText().toString() );
                params.put("txtresim_no",gelenresimno.toString()  );
                params.put("txteposta",eposta.toString());
                params.put("txtresimyol",resimyol.toString());
                params.put("txtresimbaslik",resimbaslik.toString());


                params.put("txtDil",dil);

                return  params;
            }
        };

        MySingleton.getInstance(ShowPopUp.this).addToRequestQueue(stringRequest);
    }

    private void resimBildirimAcKapat(final String sessiz) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"resimBildirimAcKapat.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("success_login")){
                            // Toast.makeText(ShowPopUp.this, response, Toast.LENGTH_SHORT).show();
                            etMessage.setText("");

                            ///mesajdan sonra uyari
                            // displayNotificationOne();

                            ///klavyeyi gizler
                            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                            fetchMovies();
                        }
                        else{
                            Toast.makeText(ShowPopUp.this, "Birşeyler Yanlış Gitti", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowPopUp.this, "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtsessiz", sessiz.toString() );
                params.put("txtresim_no",gelenresimno.toString()  );
                params.put("txteposta",eposta.toString());



                params.put("txtDil",dil);

                return  params;
            }
        };

        MySingleton.getInstance(ShowPopUp.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        //movieList.clear();
        //  movieList = dbHelper.getItems(); // reload the items from database
        adapter2.notifyDataSetChanged();
    }

    private  void fetchMovies(){
        movieList.clear();
        swipeRefreshLayout2.setRefreshing(true);
      //  listView.setSelection(listView.getAdapter().getCount()-1);


        url = Config.SERVER_IS+"resimyorumlari.php?resim_no="+gelenresimno;
        final Movie m = null;
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
                                Movie movie = new Movie();
                                movie.setUsername(obj.getString("adi"));
                                movie.setEposta(obj.getString("kullanici"));
                                movie.setKayit_no(obj.getInt("olayid"));
                                movie.setOy(obj.getInt("oy"));
                              //  Toast.makeText(getApplicationContext(),silmek,Toast.LENGTH_LONG).show();
                                movie.setSil(Integer.parseInt(silmek));
                                movie.setMesaj(obj.getString("yorum"));
                                movie.setDiL(obj.getString("dil"));
                                movie.setThumbnailUrl(obj.getString("foto"));

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
                        adapter2.notifyDataSetChanged();
                        swipeRefreshLayout2.setRefreshing(false);
                        listView.setSelection(listView.getAdapter().getCount()-1);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeRefreshLayout2.setRefreshing(false);

                listView.setSelection(listView.getAdapter().getCount()-1);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);



    }

    public  void fetchResimBilgileriGetir(final String gelenresimno){

        // if (silmek==0) { ivResimSil.setVisibility(View.INVISIBLE);}
        //toResimlerArrayList.clear();
        //   final Movie m = null;
        url = Config.SERVER_IS + "resimbilgileri.php?resimno="+gelenresimno;
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
                               // Movie movie = new Movie();
                                resimBaslik=obj.getString("resim_baslik");
                                tvResimBaslikMesaji.setText(resimBaslik);
                                tvAsagiBilgi.setText(String.valueOf(obj.getInt("asagi")));
                                tvYukariBilgi.setText(String.valueOf(obj.getInt("yukari")));
                                resimAdi=obj.getString("resim_adi");
                                resimSahibi=obj.getString("eposta");
                                fotonunepostasi=resimSahibi;
                                fotonunId=obj.getString("kayit_no");
                                if(resimSahibi.equals(eposta))
                                {silmek="1";}
                                resimSahibiFotosu=obj.getString("foto");
                                ivResimSahibiFoto.setImageUrl(Config.SERVER_IS + "" + resimSahibiFotosu, imageLoader);

                                resimSahibiId=obj.getString("kayit_no");
                                resimSahibiAdi=obj.getString("adi");
                                resimSahibiSoyadi=obj.getString("soyadi");
                              // obj.getInt("yorumsayisi");

                                // movie.setTimestamp(obj.getString("tarih"));
                                //movie.setSil(silmek);

                                // Genre is json array
                               /* JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);
*/
                                // adding movie to movies array
                                //toResimlerArrayList.add(movie);
                             //   movieList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                       // mAdapter.notifyDataSetChanged();

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

    @Override
    public void onRefresh() {

    }
}
