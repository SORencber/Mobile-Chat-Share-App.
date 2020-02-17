package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.yavuz.rencber.rencber.GCM.User;
import com.yavuz.rencber.rencber.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class GirisEkrani extends AppCompatActivity {
    ///Facebook değişkenleri
    LoginButton loginbutton;
    CallbackManager callbackManager;
    String fbname;
    String fbemail ;
    String fbgender ;
    String fbbirthday ;
    String fbid;
    EditText etMail,etpassword,adi,soyadi;
    Switch rememberme;
    Button btnLogin,btnRegisterMe,btnRememberPassword;
    final String TAG = this.getClass().getSimpleName();
    TextView etwelcome,tvRegisterme,tvforgotpassword,tvMesajGirisEkrani;

  /// SharedPrefences özelligi ile beni hatırlayı etkinleştiriyoruz
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String dil;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    Context ctx;
    String apikey="AIzaSyAHfffxoueqdpbpvh94gesyd5JmFMYdB_mXKieg";
    GoogleCloudMessaging gcm;//Google Cloud referansı
    final String PROJECT_ID = "7572995051 ";//Bu değer Google Apı sayfasında Owerview menüsünde(Giriş sayfası) yukarıda yer alır. Project Number:987... şeklinde
    String regid = null;
    String token;
    private int appVersion;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_ekrani);

        etMail=(EditText)findViewById(R.id.etMail);
        etpassword=(EditText)findViewById(R.id.etPassword);
        rememberme=(Switch)findViewById(R.id.switch1);
        etwelcome=(TextView)findViewById(R.id.etWelcome);

         btnLogin=(Button)findViewById(R.id.btnLogin);
        btnRegisterMe=(Button)findViewById(R.id.btnRegister);
        btnRememberPassword=(Button)findViewById(R.id.btnRememberPassword);


        tvMesajGirisEkrani=(TextView)findViewById(R.id.tvMesajGirisEkrani);

        rememberme.setChecked(true);
        /////////////////////////// Beni hatırla icin Otomatik girisi daimi yapmak icindir.
     preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();

        //logout telefon dosyalarini temizler
      //  editor.clear();
   //   editor.commit();
        String giris=preferences.getString("giris", "0");
        token=preferences.getString("gcm_registration_id", "0");

        if(giris.equals("1")){
            Intent i = new Intent(getApplicationContext(),IconTextTabsActivity.class);
            String epostabilgisi=preferences.getString("email","boş");
            i.putExtra("epostam",epostabilgisi);
            startActivity(i);
            finish();
        }

        /// Facebook girişi

        callbackManager = CallbackManager.Factory.create();



        loginbutton = (LoginButton)findViewById(R.id.login_button);

        List<String> permissionNeeds = Arrays.asList("public_profile", "email", "user_birthday","user_friends" );

        loginbutton.setReadPermissions(permissionNeeds);
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override

            public void onSuccess(LoginResult loginResult) {
                final Profile profile = Profile.getCurrentProfile();


                System.out.println("onSuccess");
                Log.e("onSuccess", "--------" + loginResult.getAccessToken());
                Log.e("Token", "--------" + loginResult.getAccessToken().getToken());
                Log.e("Permision", "--------" + loginResult.getRecentlyGrantedPermissions());
                // Profile profile = Profile.getCurrentProfile();
//                Log.e("ProfileDataNameF", "--" + profile.getFirstName());
                //     Log.e("ProfileDataNameL", "--" + profile.getLastName());

                //   Log.e("Image URI", "--" + profile.getLinkUri());

                Log.e("OnGraph", "------------------------");
                String accessToken = loginResult.getAccessToken()
                        .getToken();
                Log.i("accessToken", accessToken);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {

                                Log.i("GirisEkrani",
                                        response.toString());

                                try {
                                    fbid = object.getString("id");
                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + fbid + "/picture?type=large");
                                        Log.i("profile_pic",
                                                profile_pic + "");

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    fbname = object.getString("name");
                                    //  fbemail = object.getString("email");
                                    fbgender = object.getString("gender");
                                    ///  fbbirthday = object.getString("birthday");
                                    Log.e("GraphResponse", "-------------" + response.toString());
                                    FacebookData(fbid.toString(), fbname.toString(), fbgender.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields",
                        "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                     /*   Intent i = new Intent(getApplicationContext(), IconTextTabsActivity.class);
                        i.putExtra("epostam", etMail.getText().toString());
                        startActivity(i);
                        finish();
                        */
            }

            @Override
            public void onCancel() {
                Toast.makeText(GirisEkrani.this, "vazgeç", Toast.LENGTH_LONG).show();

                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                Toast.makeText(GirisEkrani.this, "hatalı", Toast.LENGTH_LONG).show();
            }


        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Registration success
                     token = intent.getStringExtra("token");
                  // Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();

                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    //Registration error
                    Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                } else {
                    //Tobe define
                }
            }
        };

        //Check status of Google play service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode) {
            //Check type of error
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                //So notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            //Start service
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            this.startService(itent);
        }

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isValidEmail(etMail.getText().toString())) {

                    sendData(etMail.getText().toString(),etpassword.getText().toString());

                }else {
                    etMail.setError(getResources().getString(R.string.plscorrectmail));
                }
                if (etpassword.getText().length()==0){
                    etpassword.setError(getResources().getString(R.string.noemptypassword));
                }
            }
        });


        btnRegisterMe.setOnClickListener(new View.OnClickListener()

        {

            @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), KayitEkrani.class);
            startActivity(i);
                   }
        }
        );

        btnRememberPassword.setOnClickListener(new View.OnClickListener()

                                        {   @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(getApplicationContext(), SifreUnuttum.class);
                                            startActivity(i);
                                            //finish();
                                        }
                                        }
        );

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
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
    private void sendData(final String Eposta, final String Sifre) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"index.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if (response.contains("Wrong")) {
                            tvMesajGirisEkrani.setText(getResources().getString(R.string.wrongmailorpassword));

                        } else if (response.contains("Engelli")) {
                            Intent i = new Intent(getApplicationContext(), Engelli.class);
                           // i.putExtra("epostam", etMail.getText().toString());
                            startActivity(i);
                            finish();
                        } else{
                            try {
                                //JSONObject jsonObject = new JSONObject(response);
                                JSONArray result = new JSONArray(response);
                                //JSONArray result = jsonObject.getJSONArray(response);
                                JSONObject collegeData = result.getJSONObject(0);

                                User user = new User(collegeData.getString("kayit_no"),
                                        collegeData.getString("adi"),
                                        collegeData.getString("eposta"));
                                String bit=collegeData.getString("bit");

                                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences nesnesi oluşturuluyor ve prefernces referansına bağlanıyor
                                editor = preferences.edit();
                                editor.putString("user_id", user.getId());
                                editor.putString("user_email", user.getEmail());
                                editor.putString("bit", bit);
                                editor.putString("user_name", user.getName());
                                editor.putString("email", Eposta.toString());
                                editor.putString("password", Sifre.toString());//email değeri
                                editor.putString("gcm_registration_id", token);

                                editor.commit();

                                // aynı şekil editor nesnesi oluşturuluyor
                                if (rememberme.isChecked()) { /// Remember me On durumunda ise.


                                    editor.putString("giris", "1");
                                    // dogru giris yapmissa bu degeride boolen olarak girdik
                                    editor.commit();// degerleri telefona kaydediyoruz

                                }
                                //   MyApplication.getInstance().getPrefManager().storeUser(user);
                               // Toast.makeText(getApplicationContext(), user.getId() + user.getEmail() + user.getName(), Toast.LENGTH_LONG).show();


                                // storing user in shared preferencesMyApplication.getInstance().getPrefManager().storeUser(user);
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }


                            Intent i = new Intent(getApplicationContext(), IconTextTabsActivity.class);
                            i.putExtra("epostam", etMail.getText().toString());
                            startActivity(i);
                            finish();
                        }
                    }



                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GirisEkrani.this, "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("txtUsername", Eposta.toString());
                params.put("txtPassword",Sifre.toString());

                return params;
            }
        };

        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(stringRequest);
    }


    //facebok
    private void FacebookData(final String fbemail, final String fbadi,final String fbgender) {
        final String[] cinsiyet = new String[1];
        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);
        // tvKayitEkrani.setText(Eposta+Sifre+adi+soyadi+cinsiyet);
        String url = Config.SERVER_IS+"fbkayit.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("dolu")){

                            sendData(fbemail.toString(),"yavuzselim65.123.!!!!,???");


                        } else  if(response.contains("oldu")) {
                            String dil= Locale.getDefault().getLanguage();
                            sendData(fbemail.toString(),"yavuzselim65.123.!!!!,???");
                        }
                     /*   else{ /// Giris islemi basarali degilse welcome yazısını değiştiriyoruz
                            tvKayitEkrani.setText(getResources().getString(R.string.plstryagainlater));
                            // Toast.makeText(KayitEkrani.this, Eposta+Sifre+adi+soyadi+cinsiyet, Toast.LENGTH_SHORT).show();
                        }
                        */
                        //  Toast.makeText(getApplicationContext(), user.getId() + user.getEmail() + user.getName(), Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GirisEkrani.this, getResources().getString(R.string.connecterror), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtUsername", fbemail.toString());
                // params.put("txtPassword",Sifre);
                params.put("txtAdi", fbadi.toString());
                final Profile profile = Profile.getCurrentProfile();

                params.put("txtSoyadi",".");
                if(fbgender.toString()=="female"){
                    cinsiyet[0] ="Bayan";}else{
                    cinsiyet[0] ="Bay";}
                params.put("txtCinsiyet", cinsiyet[0].toString());

                params.put("txtDil", "tr");
                params.put("txtfb", "1");

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        MySingleton.getInstance(GirisEkrani.this.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

}
