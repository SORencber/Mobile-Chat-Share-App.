package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.Trends.TrendsResimler;
import com.yavuz.rencber.rencber.fragments.FourFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PuanHarca extends AppCompatActivity {

    TextView Uyari;
    EditText etPuan;
    ImageView kapat;
    Button btnPuanKaydet;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,adi,soyadi,dil,cinsiyet;
    private static final String TAG = PuanHarca.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puan_harca);

        Uyari= (TextView)findViewById(R.id.tvUyariPuan);
        etPuan=(EditText)findViewById(R.id.etPuan);
        kapat=(ImageView)findViewById(R.id.ivPuanPencereKapat);
        btnPuanKaydet=(Button)findViewById(R.id.btnPuanKaydet);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        eposta=preferences.getString("email","boş");

        PuanGetir(eposta);


        btnPuanKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Puan = Integer.parseInt(etPuan.getText().toString());
                if (Puan >= 0 && Puan <=50) {

                    PuanGuncelle(eposta,Puan);
                }else{
                    Uyari.setText("Lütfen 0 - 50 arası bir puan giriniz.!");
                }
                }

        });

        kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }

        });

    }



    private void PuanGuncelle(final String eposta,final int Puan) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"PuanGuncelle.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("success_login")){



                           Toast.makeText(getApplicationContext(),"Puanınız Kaydedildi.",Toast.LENGTH_LONG).show();



                        }
                        else{
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtPuan", String.valueOf(Puan));

                params.put("txt_to_eposta",eposta);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void PuanGetir(String eposta) {
       String url = Config.SERVER_IS + "PuanGetir.php?eposta=" + eposta + "&istek=1";

        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = new JSONArray(response);
                    //JSONArray result = jsonObject.getJSONArray(response);
                    JSONObject collegeData = result.getJSONObject(0);



                    String GelenPuan = collegeData.getString("Harcama");
                     etPuan.setText(GelenPuan);


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext().getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
}