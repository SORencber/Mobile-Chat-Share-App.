package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yavuz.rencber.rencber.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KayitEkrani extends AppCompatActivity {
    EditText etMail,etpassword,adi,soyadi;
    Switch rememberme;
    Button btnKayit;
    RadioButton cinsiyet, bay,bayan;
    Locale MakineDili;
    String dil;
    final String TAG = this.getClass().getSimpleName();
    TextView etwelcome,tvRegisterme,tvforgotpassword,tvKayitEkrani;

    /// SharedPrefences özelligi ile beni hatırlayı etkinleştiriyoruz
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ekrani);
        btnKayit=(Button)findViewById(R.id.btnKayit);
        adi =(EditText)findViewById(R.id.etAd);
        soyadi=(EditText)findViewById(R.id.etSoyadi);
        etMail=(EditText)findViewById(R.id.ketMail);
        etpassword=(EditText)findViewById(R.id.ketPassword);
        bay=(RadioButton)findViewById(R.id.radioBay) ;
        bayan=(RadioButton)findViewById(R.id.radioBayan) ;
        tvKayitEkrani=(TextView)findViewById(R.id.tvKayitMesaj);
        TextView tvGirisEkrani=(TextView)findViewById(R.id.tvGirisEkranı);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        /////////////////////////// bir kez kayit olmussa cikis yapana kadar direk girsin//////////////////
        dil= Locale.getDefault().getLanguage();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        //editor.clear();
        //editor.commit();


        String giris=preferences.getString("giris", "0");

        if(giris.equals("1")){
            Intent i = new Intent(getApplicationContext(),GirisEkrani.class);
            String epostabilgisi=preferences.getString("email","boş");
            i.putExtra("epostam",epostabilgisi);
            startActivity(i);
            finish();
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////

        tvGirisEkrani.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),GirisEkrani.class);

                startActivity(i);
                finish();
            }
        });
        btnKayit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                cinsiyet = (RadioButton) findViewById(selectedId);



                if(cinsiyet.getText().toString()=="") {


                  bay.setChecked(true);                }
                else if(!isValidEmail(etMail.getText().toString())) {


                 etMail.setError(getResources().getString(R.string.plscorrectmail));
                }
                else if (etpassword.getText().length()==0){
                    etpassword.setError(getResources().getString(R.string.noemptymail));
                }

               else  if((soyadi.getText().length()==0))
                {

                    soyadi.setError(getResources().getString(R.string.noemptysurname));
                }
               else  if((adi.getText().length()==0) )
                {

                    adi.setError(getResources().getString(R.string.noemptyname));
                }
                else  if((etpassword.getText().length()==0) )
                {

                    etpassword.setError(getResources().getString(R.string.noemptypassword));
                }
                else {


                    sendData(etMail.getText().toString(), etpassword.getText().toString(), adi.getText().toString(), soyadi.getText().toString(), cinsiyet.getText().toString());

                }
            }

        });


    }


    /// Mail adres kontrol methodu
    public boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            // e-posta formatı kontrol
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    //// Form verileri sunucuya gönderiliyor
    private void sendData(final String Eposta, final String Sifre,final String adi,final String soyadi,final String cinsiyet) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);
       // tvKayitEkrani.setText(Eposta+Sifre+adi+soyadi+cinsiyet);
        String url = Config.SERVER_IS+"userkayit.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("dolu")){
                            tvKayitEkrani.setText(getResources().getString(R.string.thismailregistered));

                        } else  if(response.contains("oldu")) {
                            String dil= Locale.getDefault().getLanguage();
                            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences nesnesi oluşturuluyor ve prefernces referansına bağlanıyor
                            editor = preferences.edit(); //aynı şekil editor nesnesi oluşturuluyor
                            editor.putString("email", etMail.toString());
                            editor.putString("adi", adi.toString()); //Eposta ve sifreyi sharedprefencese kaydediyoruz
                            editor.putString("soyadi", soyadi.toString());//email değeri
                            editor.putString("dil",dil.toString());//Dil degeri
                            editor.putString("cinsiyet",cinsiyet.toString());//Dil degeri
                            editor.putString("dil",dil.toString());//Dil degeri

                            //editor.putBoolean("login", true);// dogru giris yapmissa bu degeride boolen olarak girdik
                            editor.commit();// degerleri telefona kaydediyoruz

                            Intent i = new Intent(getApplicationContext(),GirisEkrani.class);
                            i.putExtra("Eposta",Eposta);
                            i.putExtra("sifre",Sifre);
                            startActivity(i);
                            finish();
                        }
                        else{ /// Giris islemi basarali degilse welcome yazısını değiştiriyoruz
                            tvKayitEkrani.setText(getResources().getString(R.string.plstryagainlater));
                           // Toast.makeText(KayitEkrani.this, Eposta+Sifre+adi+soyadi+cinsiyet, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KayitEkrani.this, getResources().getString(R.string.connecterror), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtUsername", Eposta);
                params.put("txtPassword",Sifre);
                params.put("txtAdi", adi);
                params.put("txtSoyadi",soyadi);
                params.put("txtCinsiyet", cinsiyet);
                params.put("txtDil", dil);
                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        MySingleton.getInstance(KayitEkrani.this.getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
