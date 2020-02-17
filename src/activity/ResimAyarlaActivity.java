package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.android.volley.toolbox.StringRequest;
import com.yavuz.rencber.rencber.GPS.Trends.LocationAssistant;
import com.yavuz.rencber.rencber.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ResimAyarlaActivity extends AppCompatActivity   implements LocationAssistant.Listener{
    LocationAssistant assistant;
    TextView tvUlke,tvSehir,tvilcem,tvAdres;
    RadioGroup radioPayslasimlar;
    RadioButton rbtumdiller,rbbenimdilim,rbsadecearkadaslar,rbkonumum;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    Button btnSave;
    String latitude,longitude;

    String eposta, foto, chat_room_id, to_user_id, name, user_id, grup, sahibi, odaIsmi;
String secenek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resim_ayarla);
        tvSehir=(TextView)findViewById(R.id.tvSehir);
        tvUlke=(TextView)findViewById(R.id.tvUlke);
        tvilcem=(TextView)findViewById(R.id.tvilcem);
        tvAdres=(TextView)findViewById(R.id.tvAdres);
   btnSave=(Button)findViewById(R.id.btnSave);
        radioPayslasimlar = (RadioGroup) findViewById(R.id.radioPaylasimlar);
        rbtumdiller = (RadioButton) findViewById(R.id.radioTumDiller);
        rbbenimdilim = (RadioButton) findViewById(R.id.radioKendiDilim);
        rbsadecearkadaslar = (RadioButton) findViewById(R.id.radioSadeceArkadaslarim);
        rbkonumum = (RadioButton) findViewById(R.id.radioKonumumdakiler);
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 5000, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();

        eposta = preferences.getString("email", "boş");
        foto = preferences.getString("foto", "0");
        secenek = preferences.getString("secenek", "0");
        if(secenek=="0")
        {
            rbtumdiller.setChecked(true);

        }else if (secenek=="1") {
            rbbenimdilim.setChecked(true);

        }else if(secenek=="2")
        {
            rbsadecearkadaslar.setChecked(true);

        }else if(secenek=="3") {
            rbkonumum.setChecked(true);

        }
        rbtumdiller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    editor.putString("secenek", "0");
                    editor.commit();
                }
            }
        });
        rbbenimdilim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    editor.putString("secenek", "1");
                    editor.commit();
                }
            }
        });
        rbsadecearkadaslar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    editor.putString("secenek", "2");
                    editor.commit();

                }
            }
        });
        rbkonumum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    editor.putString("secenek", "3");
                    editor.commit();
                    assistant.start();
                   // konumGuncelle();

                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showPopup(v,foto);
                finish();
            }
        });    }

    public void getAddress(final double currentLatitude, final double currentLongitude) {

        String url="http://maps.googleapis.com/maps/api/geocode/json?latlng="+currentLatitude+","+currentLongitude+"&sensor=true";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response.toString(),LENGTH_LONG).show();
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                        JSONArray results = jsonObject.getJSONArray("results");
                        if (results.length() > 0) {
                            for (int i = 0; i < results.length() ; i++) {
                                JSONObject result = results.getJSONObject(i);
                                //Log.e(MyGeocoder.class.getName(), result.toString());
                                Address addr = new Address(Locale.getDefault());
                                // addr.setAddressLine(0, result.getString("formatted_address"));

                                JSONArray components = result.getJSONArray("address_components");
                                String country = "";
                                String route = "";
                                String sehir="";
                                String ilce="";
                                for (int a = 0; a < components.length(); a++) {
                                    JSONObject component = components.getJSONObject(a);
                                    JSONArray types = component.getJSONArray("types");
                                    for (int j = 0; j < types.length(); j++) {
                                        String type = types.getString(j);
                                        if (type.equals("locality")) {
                                            sehir =component.getString("long_name");
                                            if(sehir.equals("")){ }else{ tvSehir.setText(sehir);
                                            editor.putString("sehir",sehir);
                                            editor.commit();}
                                        } else if (type.equals("country")) {
                                            country = component.getString("long_name");
                                            if(country.equals("")){ }else{ tvUlke.setText(country);
                                            }

                                        } else if (type.equals("route")) {
                                            route = component.getString("long_name");
                                            if(route.equals("")){ }else{ tvAdres.setText(route);

                                            }
                                        } else if (type.equals("administrative_area_level_2")) {
                                            ilce = component.getString("long_name");
                                            if(ilce.equals("")){ }else {
                                                tvilcem.setText(ilce);
                                                editor.putString("ilce",ilce);
                                                editor.commit();

                                            }
                                        }
                                    }
                                }
                                // Toast.makeText(getApplicationContext(),route.toString(),LENGTH_LONG).show();


                            }     // JSONArray mtypes = zero2.getJSONArray("types");
                            // String Type = mtypes.getString(0);
                            // Log.e(Type,long_name);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        AppController.getInstance().addToRequestQueue(strReq);

    }
    private void konumGuncelle() {

        String url = Config.SERVER_IS+"konumGuncelle.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")){


                           // Toast.makeText(getApplicationContext().getApplicationContext(), getApplicationContext().getResources().getString(R.string.sentrequest), Toast.LENGTH_SHORT).show();
                        }
                        else{
                           // Toast.makeText(getApplicationContext().getApplicationContext(), getApplicationContext().getResources().getString(R.string.alreadyfriendlist), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtKullanici", eposta);
                params.put("txtUlke", tvUlke.getText().toString());
                params.put("txtSehir", tvSehir.getText().toString());
                params.put("txtIlce", tvilcem.getText().toString());
                params.put("txtadres", tvAdres.getText().toString());
                params.put("txtlatitude", latitude);
                params.put("txtlongitude", longitude);

                // params.put("txtDil",dil);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext().getApplicationContext()).addToRequestQueue(stringRequest);
    }
    @Override

    protected void onResume() {

        super.onResume();

        //assistant.start();

    }



    @Override

    protected void onPause() {

        assistant.stop();

        super.onPause();

    }



    @Override

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (assistant.onPermissionsUpdated(requestCode, grantResults));

        //  tvKonum.setOnClickListener(null);

    }







    @Override

    public void onNeedLocationPermission() {

        tvUlke.setText("Need\nPermission");

        tvUlke.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                assistant.requestLocationPermission();

            }

        });

        assistant.requestAndPossiblyExplainLocationPermission();

    }



    @Override

    public void onExplainLocationPermission() {

        new AlertDialog.Builder(this)

                .setMessage("izinler")

                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        assistant.requestLocationPermission();

                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        tvUlke.setOnClickListener(new View.OnClickListener() {

                            @Override

                            public void onClick(View v) {

                                assistant.requestLocationPermission();

                            }

                        });

                    }

                })

                .show();

    }



    @Override

    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView,

                                                        DialogInterface.OnClickListener fromDialog) {

        new AlertDialog.Builder(this)

                .setMessage("İzin muhabbeti")

                .setPositiveButton(R.string.ok, fromDialog)

                .show();

    }



    @Override

    public void onNeedLocationSettingsChange() {

        new AlertDialog.Builder(this)

                .setMessage(        "Konum izni yok")

                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        assistant.changeLocationSettings();

                    }

                })

                .show();

    }



    @Override

    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

        new AlertDialog.Builder(this)

                .setMessage("burada ne yazar")

                .setPositiveButton(R.string.ok, fromDialog)

                .show();

    }



    @Override

    public void onNewLocationAvailable(Location location) {

        if (location == null) return;

        tvUlke.setOnClickListener(null);

        //tvKonum.setText(location.getLongitude() + "\n" + location.getLatitude());
        //tvKonum.setAlpha(1.0f);

        // getCompleteAddressString( location.getLatitude(),location.getLongitude());
        latitude=String.valueOf(location.getLatitude());
        longitude=String.valueOf(location.getLongitude());
        getAddress( location.getLatitude(),location.getLongitude());
        //tvKonum.setText(getCity());
        //  tvKonum.animate().alpha(0.5f).setDuration(400);


    }

    @Override

    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

        tvUlke.setText("brada mock var");

        tvUlke.setOnClickListener(fromView);

    }



    @Override

    public void onError(LocationAssistant.ErrorType type, String message) {

        tvUlke.setText("hata");

    }
}
