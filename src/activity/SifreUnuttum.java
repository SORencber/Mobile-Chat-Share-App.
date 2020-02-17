package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yavuz.rencber.rencber.R;

import java.util.HashMap;
import java.util.Map;

public class SifreUnuttum extends AppCompatActivity {

    final String TAG = this.getClass().getSimpleName();

    TextView tvRegisterme,tvGirisEkrani,tvMesaj,etSignScreen;
    EditText etMailx;
    Button btnforgotpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_unuttum);
        tvRegisterme= (TextView) findViewById(R.id.tvRegistermexx);
        tvGirisEkrani= (TextView) findViewById(R.id.tvGirisEkranı);
        etSignScreen= (TextView) findViewById(R.id.etSignScreen);

        tvMesaj=(TextView)findViewById(R.id.tvMesaj);
        etMailx=(EditText)findViewById(R.id.etMailx);
        btnforgotpassword=(Button)findViewById(R.id.btnForgotPassword);

        btnforgotpassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isValidEmail(etMailx.getText().toString())) {

                    sendData(etMailx.getText().toString());

                }else{
                    etMailx.setError("Lütfen Mail Adresiniz Doğru yazınız");
                }
            }
        });
        tvRegisterme.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),KayitEkrani.class);
                startActivity(i);
                finish();
            }
        });
        etSignScreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),KayitEkrani.class);
                startActivity(i);
                finish();
            }
        });
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
    private void sendData(final String Eposta) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"unuttum.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("success_login")){
                            tvMesaj.setText("Şifreniz E-Posta adresinize gönderildi.Lütfen Kontrol ediniz.");

                        }


                        else{ /// Giris islemi basarali degilse welcome yazısını değiştiriyoruz
                            tvMesaj.setText("Sistemde Böyle bir E-Posta kayıtlı değil.Üye olabilirsiniz");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SifreUnuttum.this, "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtUsername", Eposta.toString());

                return params;
            }
        };

        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
