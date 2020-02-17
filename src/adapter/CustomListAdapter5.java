package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Ömer RENÇBER   2018
//
////////////////////////////////////////////////
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.Movie;
import com.yavuz.rencber.rencber.activity.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomListAdapter5 extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,cinsiyet,gelenresimno,resimyol,resimbaslik;
    Movie m;
    TextView mesaj;
    ImageView ivReply,ivYorumCevir2;
    Button btnFollow;
    private static final String TAG = CustomListAdapter5.class.getSimpleName();

    public CustomListAdapter5(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position)
    { return position;}

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;


        preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());//preferences objesi
        editor = preferences.edit();
        // Inflate the layout for this fragment

            eposta = preferences.getString("email", "boş");
            cinsiyet = preferences.getString("cinsiyet", "null");

     final    Movie m = movieItems.get(position);



        if (inflater == null)
            inflater = (LayoutInflater) CustomListAdapter5.this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

              if(eposta.equals(m.getEposta())) {


                  convertView = inflater.inflate(R.layout.list_row_ozel_mesajlar_sol, null);
                  mesaj = (TextView) convertView.findViewById(R.id.tvResimBaslik);
                  mesaj.setText(m.getMesaj());

              }
              else{


                  convertView=inflater.inflate(R.layout.list_row_ozel_mesajlar,null);

                  mesaj = (TextView) convertView.findViewById(R.id.tvResimBaslik);
                  mesaj.setText(m.getMesaj());
                  ImageView ivYorumCevir2= (ImageView) convertView.findViewById(R.id.ivYorumCevir2);

                  ivYorumCevir2.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {

                           Cevir(m.getMesaj(),m.getDiL());
                      }
                  });



              }




        return convertView;
    }
    public void Cevir(String Mesaj,String dbdil){
       String benimdil= Locale.getDefault().getLanguage();


        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_CEVIRI+"&text="+Mesaj.trim()+"&lang="+dbdil+"-"+benimdil;
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    showTranslate(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity.getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);




    }

    private void showTranslate(String response) throws JSONException {
        String code = "";
        String lang = "";
        JSONArray text ;

        try {
            JSONObject jsonObj = new JSONObject(response);
            code = jsonObj.getString("code");
            lang = jsonObj.getString("lang");
            text = jsonObj.getJSONArray("text");
            mesaj.setText(text.getString(0));


        } catch (Exception e) {
            e.printStackTrace();
        }


        // Toast.makeText(getActivity().getApplicationContext(), text.getString(0).toString(),Toast.LENGTH_LONG);
        // baslik.setText(text.getString(0));
    }

    private void Follow(final String friend, final String diL) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"takipet.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("success_login")){
                            Toast.makeText(activity, "OK", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(activity, "Takip listenizde", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtkullanici", (String.valueOf(eposta)) );
                params.put("txtfriend", (String.valueOf(friend)) );
                params.put("txtDil",String.valueOf(diL));
                return params;
            }
        };

        MySingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
