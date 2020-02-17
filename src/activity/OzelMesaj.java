package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
 import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.adapter.CustomListAdapter5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
public class OzelMesaj extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    PopupWindow popUp;
    LinearLayout layout;
    TextView tv;
    LinearLayout.LayoutParams params;
    LinearLayout mainLayout;
    Button but,btnSendMessage;
    NetworkImageView ivResim;
    EditText etMessage;
    boolean click = true;
    View vi;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private static final String TAG = OzelMesaj.class.getSimpleName();
    public static int count;
    ImageView ivSil,ivOzelResim;
    TextView tvAdiSoyadi;
    // Movies json url
    String url,adi,soyadi,dil;
    Bundle extras;
    private SwipeRefreshLayout swipeRefreshLayout2;
    // Movies json url
    public ArrayList<Movie> movieList = new ArrayList<Movie>();
    public ListView listView;
    private CustomListAdapter5 adapter2;

    protected Handler handler;

    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,gelenresimno,foto,resimbaslik,silmek,kime,token;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ozel_mesaj);



        listView = (ListView) findViewById(R.id.listozelmesaj);
       // ivResim=(NetworkImageView) findViewById(R.id.ivResim);
        btnSendMessage=(Button)findViewById(R.id.btnSendMessage);
        etMessage=(EditText)findViewById(R.id.tvResimBaslik);
        tvAdiSoyadi=(TextView)findViewById(R.id.etAdiSoyadi);
        NetworkImageView ivOzelResim = (NetworkImageView) findViewById(R.id.ivOzelResim);
        swipeRefreshLayout2=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout5);
        swipeRefreshLayout2.setOnRefreshListener(this);
        ivSil=(ImageView)findViewById(R.id.ivSil);
        //ivReply=(ImageView)findViewById(R.id.ivReply);

        swipeRefreshLayout2.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        fetchMovies(eposta, kime,dil);
                    }
                }
        );
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();

         token=preferences.getString("gcm_registration_id", "0");

        extras = getIntent().getExtras();
        foto = extras.getString("resimyol");
        kime=extras.getString("kime");
        adi = extras.getString("adi");
        soyadi=extras.getString("soyadi");
        dil = extras.getString("dil");

        ivOzelResim.setImageUrl(Config.SERVER_IS+""+foto, imageLoader);
        tvAdiSoyadi.setText(adi+"  "+soyadi);

        adapter2 = new CustomListAdapter5(OzelMesaj.this, movieList);
        listView.setAdapter(adapter2);

       // ivSil=(ImageView)findViewById(R.id.ivSil);





        preferences = PreferenceManager.getDefaultSharedPreferences(OzelMesaj.this);//preferences objesi
        editor = preferences.edit();
        // Inflate the layout for this fragment

        eposta=preferences.getString("email","boş");
        fetchMovies(eposta,kime,dil);
        btnSendMessage.setOnClickListener(new View.OnClickListener()

                                          {

                                              @Override
                                              public void onClick(View v) {
                                                  if(etMessage.getText().length()>0){
                                                      sendMessage();}else{
                                                      etMessage.setError("Mesaj girmediniz");
                                                  }

                                              }
                                          }
        );



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
                            Toast.makeText(OzelMesaj.this.getApplicationContext(), "Birşeyler Yanlış Gitti", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OzelMesaj.this.getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder=new AlertDialog.Builder(OzelMesaj.this);
        // builder.setCancelable(false);
        // builder.setTitle("Bu uygulamaya Oy vermek istermisiniz?");
        builder.setMessage(getResources().getString(R.string.delete));
        builder.setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(OzelMesaj.this, getResources().getString(R.string.deleted), Toast.LENGTH_LONG).show();
                deleteResim(Integer.valueOf(gelenresimno),foto);
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

        String url = Config.SERVER_IS+"ozelmesaj.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("")){
                            // Toast.makeText(ShowPopUp.this, response, Toast.LENGTH_SHORT).show();
                            etMessage.setText("");
                            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                            fetchMovies(eposta, kime,dil);
                        }
                        else{
                            Toast.makeText(OzelMesaj.this, "Birşeyler Yanlış Gitti", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                }

                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                }else if(error instanceof NoConnectionError){
                    Log.e("Volley", "NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "ParseError");
                }
                Toast.makeText(OzelMesaj.this, "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtkime",kime );
                params.put("txteposta",eposta.toString()  );
                params.put("txtMessage",etMessage.getText().toString());
                params.put("txtRegistration_ID",token.toString());
                params.put("txtDil",dil.toString());
               // params.put("txtRegistration_ID",token.toString());

                return  params;
            }
        };

        MySingleton.getInstance(OzelMesaj.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onResume() {
        super.onResume();
        //movieList.clear();
        //  movieList = dbHelper.getItems(); // reload the items from database
        adapter2.notifyDataSetChanged();
    }

    private  void fetchMovies(final String eposta, final String kime,final String dil){

        movieList.clear();
        swipeRefreshLayout2.setRefreshing(true);
        //  listView.setSelection(listView.getAdapter().getCount()-1);
        extras = getIntent().getExtras();

        url = Config.SERVER_IS+"ozelmesajlar.php?eposta="+ eposta +"&kime="+kime;
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




                                movie.setEposta(obj.getString("kimden"));
                                movie.setMesaj(obj.getString("icerik"));
                                movie.setDiL(dil);



                                movie.setThumbnailUrl(foto);

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



    @Override
    public void onRefresh() {

    }
}
