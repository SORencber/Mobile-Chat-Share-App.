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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.yavuz.rencber.rencber.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Ayarlar extends AppCompatActivity {

    private static final String TAG = Ayarlar.class.getSimpleName();

    EditText etAyarAdim,etAyarSoyadim;
    Switch arama,takipci,resimlerim,sessizlik;
    Button btnAyarKaydet, btnAyarCikis;
    String aramadurum,resimlerdurum,takipcilerdurum;
    ImageView ivAyarlarKapat;

    String eposta;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String KanKaSessiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        etAyarAdim= (EditText) findViewById(R.id.etAyarAdi);
        etAyarSoyadim= (EditText) findViewById(R.id.etAyarSoyadi);
        arama=(Switch)findViewById(R.id.switchAyarArama);
        sessizlik=(Switch)findViewById(R.id.switchSessiz);

        takipci=(Switch)findViewById(R.id.switchAyarTakipciGoster);
        resimlerim=(Switch)findViewById(R.id.switchAyarResimGoster);
         btnAyarKaydet=(Button)findViewById(R.id.btnAyarlariKaydet);
        ivAyarlarKapat=(ImageView)findViewById(R.id.ivAyarKapat);
        preferences = PreferenceManager.getDefaultSharedPreferences(Ayarlar.this);//preferences objesi
        editor = preferences.edit();
        eposta=preferences.getString("email","boş");
        KanKaSessiz=preferences.getString("KanKaSessiz","0");

        if(KanKaSessiz.equals("1")) { sessizlik.setChecked(true);}else{sessizlik.setChecked(false);}

        aramadurum="1";
        resimlerdurum="1";
        takipcilerdurum="1";
        topKullanicilarGetir();
        sessizlik.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    KanKaSessiz="1";
                    editor.putString("KanKaSessiz","1");
                    editor.commit();

                } else {
                    KanKaSessiz="0";
                    editor.putString("KanKaSessiz","0");
                    editor.commit();
                }
            }
        });
        arama.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    aramadurum="1";
                } else {
                    aramadurum="0";
                }
            }
        });
        resimlerim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    resimlerdurum="1";
                } else {
                    resimlerdurum="0";
                }
            }
        });
        takipci.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    takipcilerdurum="1";
                } else {
                    takipcilerdurum="0";
                }
            }
        });

        btnAyarKaydet.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
         ///  Toast.makeText(getApplicationContext(),aramadurum.toString(), Toast.LENGTH_SHORT).show();
           sendData(etAyarAdim.getText().toString().trim(),etAyarSoyadim.getText().toString().trim(),aramadurum,takipcilerdurum,resimlerdurum);
           Toast.makeText(getApplicationContext(), "Kaydetme İşlemi tamamlandı", Toast.LENGTH_SHORT).show();
       }
   });

        ivAyarlarKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///  Toast.makeText(getApplicationContext(),aramadurum.toString(), Toast.LENGTH_SHORT).show();
                // sendData(etAyarAdim.getText().toString().trim(),etAyarSoyadim.getText().toString().trim(),aramadurum,takipcilerdurum,resimlerdurum);
                finish();
            }
        });

    }



    private void sendData(final String adi, final String soyadi,final String aramam,final String takipcim,final String resimlerimm) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);
        // tvKayitEkrani.setText(Eposta+Sifre+adi+soyadi+cinsiyet);
        String url = Config.SERVER_IS+"ayarlarKaydet.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("")){

                        }
                        else{ /// Giris islemi basarali degilse welcome yazısını değiştiriyoruz
                            //tvKayitEkrani.setText(getResources().getString(R.string.plstryagainlater));
                            // Toast.makeText(KayitEkrani.this, Eposta+Sifre+adi+soyadi+cinsiyet, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Ayarlar.this, getResources().getString(R.string.connecterror), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtAdi", adi);
                params.put("txtSoyadi",soyadi);
                params.put("txtArama", aramam);
                params.put("txtResimler",resimlerimm);
                params.put("txtTakipciler", takipcim);
                params.put("txtEposta", eposta);
               // Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        MySingleton.getInstance(Ayarlar.this.getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private void topKullanicilarGetir() {
        // Toast.makeText(getActivity().getApplicationContext(),eposta,Toast.LENGTH_LONG).show();
        String url=Config.SERVER_IS+"ayarlarGetir.php?eposta="+eposta;
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
                                String Adi=obj.getString("adi");
                                String Soyadi=obj.getString("soyadi");
                                String arama_gorulme=obj.getString("arama_gorulme");
                                String resimler_gorulme=obj.getString("resimler_gorulme");
                                String takipci_gorulme=obj.getString("takipci_gorulme");
                                etAyarSoyadim.setText(Soyadi.trim());
                                etAyarAdim.setText(Adi);
                                 if  (arama_gorulme.equals("0")) {
                                    arama.setChecked(false);

                                }else {
                                     arama.setChecked(true);
                                }
                                if  (resimler_gorulme.equals("0")) {
                                    resimlerim.setChecked(false);

                                }else {
                                    resimlerim.setChecked(true);
                                }
                                if  (resimler_gorulme.equals("0")) {
                                    takipci.setChecked(false);

                                }else {
                                    takipci.setChecked(true);
                                }
                            //    Log.e(TAG, "grup error: " + grup);
                                //toTrendsKllanicilar.add(cr);
                            }  catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                            }
                        }





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
}
