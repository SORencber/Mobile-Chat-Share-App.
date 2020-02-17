package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.yavuz.rencber.rencber.GCM.ChatRoom;
import com.yavuz.rencber.rencber.GCM.SimpleDividerItemDecoration;
import com.yavuz.rencber.rencber.GPS.Trends.LocationAssistant;
import com.yavuz.rencber.rencber.R;
import android.hardware.SensorManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.seismic.ShakeDetector;
import com.yavuz.rencber.rencber.adapter.ChatRoomsAdapterAra;
import com.yavuz.rencber.rencber.adapter.SallaAdapter;
import com.yavuz.rencber.rencber.fragments.ArkadasAraFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import java.util.Map;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.Toast.LENGTH_LONG;
import static com.facebook.FacebookSdk.getApplicationContext;

public  class SallaBulActivity extends AppCompatActivity implements ShakeDetector.Listener   , LocationAssistant.Listener {
    LocationAssistant assistant;

    private SallaAdapter adapter1;
    private ProgressDialog pDialog;
    ImageView ivSallaKapat;
    int silmek;
    Spinner spinnerSonuc;
    Button btnara;
    EditText keyword;
    TextView tvSallaMessage;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,user_email,user_name,user_id;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<ChatRoom> chatRoomArrayList;
    private SallaAdapter mAdapter;
    private RecyclerView recyclerView;
    SearchView searchView;
    Context mBase;
    private static final String TAG = SallaBulActivity.class.getSimpleName();
    public static int count;
    Ringtone rbasla,rbitir;
    SensorManager sensorManager;
    ShakeDetector sd;
    Location locationA,locationB;
    public   double latitude,longitude;
    Handler handler = new Handler();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salla_bul);
        ivSallaKapat=(ImageView)findViewById(R.id.ivSallaKapat);
        tvSallaMessage=(TextView)findViewById(R.id.tvSallaMessage);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        user_email=preferences.getString("user_email","boş");
        user_id=preferences.getString("user_id","boş");
        user_name=preferences.getString("user_name","boş");
        eposta=preferences.getString("email","bos");
        chatRoomArrayList = new ArrayList<>();
        mAdapter = new SallaAdapter(this, chatRoomArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 10000, false);
        assistant.start(); //gps calisiyor
        recyclerView.addOnItemTouchListener(new SallaAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new SallaAdapter.ClickListener() {
            @Override
            public void onClick(View view, final int position) {


            }
            @Override
            public void onLongClick(View view, int position) {

            }

        }));


         sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sd = new ShakeDetector(this);
        sd.start(sensorManager);

        // Toast.makeText(getApplicationContext().getApplicationContext(), getApplicationContext().getResources().getString(R.string.sentrequest), Toast.LENGTH_SHORT).show();
        ivSallaKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sd.stop();
                finish();
            }
        });
//        TextView tv = new TextView(this);
//        tv.setGravity(CENTER);
//        tv.setText("Shake me, bro!");
//        setContentView(tv, new LayoutParams(MATCH_PARENT, MATCH_PARENT));

         locationA = new Location("point A");


         locationB = new Location("point B");



    }

    public void hearShake() {
        //playNotificationSoundSalla();
        chatRoomArrayList.clear();
       // sd.start(sensorManager);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // close your dialog
             fetchChatRooms();
            }

        }, 1300);



    }

    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + AppController.getInstance().getApplicationContext().getPackageName() + "/raw/sallabul");
            rbitir = RingtoneManager.getRingtone(AppController.getInstance().getApplicationContext(), alarmSound);
            rbitir.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void playNotificationSoundSalla() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + AppController.getInstance().getApplicationContext().getPackageName() + "/raw/sallarken");
            rbasla = RingtoneManager.getRingtone(AppController.getInstance().getApplicationContext(), alarmSound);
            rbasla.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchChatRooms() {


        //Toast.makeText(getActivity().getApplicationContext(),eposta,Toast.LENGTH_LONG).show();
        String url= Config.SERVER_IS+"sallaBul.php?eposta="+eposta;
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        //  Toast.makeText(getActivity().getApplicationContext(),  eposta.toString(), Toast.LENGTH_SHORT).show();
                        sd.stop();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = null;
                                try {
                                    obj = response.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ChatRoom cr = new ChatRoom();
                                //  cr.setId(obj.getString("no"));
                                cr.setName(obj.getString("adi"));
                                String hedeflatitude=cr.setLatitude(obj.getString("latitude"));
                                String hedeflongitude=cr.setLongitude(obj.getString("longitude"));

                                 if(String.valueOf(latitude).equals("") || String.valueOf(latitude).equals("null"))
                                 {
                                     //finish();
                                     Toast.makeText(getApplicationContext(),"Konum ayarınızı yapmadığınız için bu özelliği kullanamazsınız",LENGTH_LONG);
                                     cr.setMesafe(String.valueOf("boş"));

                                 }else {

                                     if(String.valueOf(hedeflatitude).equals("") || String.valueOf(latitude).equals("null"))
                                     {
                                         //finish();
                                         Toast.makeText(getApplicationContext(),"Konum ayarınızı yapmadığınız için bu özelliği kullanamazsınız",LENGTH_LONG);
                                         cr.setMesafe(String.valueOf("boş"));

                                     }else {
                                         locationA.setLatitude(latitude);
                                         locationA.setLongitude(longitude);
                                         locationB.setLatitude(Double.valueOf(hedeflatitude));
                                         locationB.setLongitude(Double.valueOf(hedeflongitude));
                                         float distance = locationA.distanceTo(locationB) / 1000;
                                         //Toast.makeText(getApplicationContext().getApplicationContext(), String.valueOf(latitude), Toast.LENGTH_SHORT).show();

                                         cr.setMesafe(String.valueOf(distance));
                                     }
                                 }
                                cr.setTo_user_id(obj.getString("kayit_no"));
                                cr.setFoto(obj.getString("foto"));
                                cr.setCinsiyet(obj.getString("cinsiyet"));
                                cr.setLastMessage("");
                                cr.setUnreadCount(0);
                                cr.setTimestamp(obj.getString("tarih"));
                                cr.setEmail(obj.getString("eposta"));
                                chatRoomArrayList.add(cr);
                                sd.start(sensorManager);

                            }  catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                            }
                        }


                        //rbasla.stop();
                        playNotificationSound();
                        mAdapter.notifyDataSetChanged();
                        //rbitir.stop();
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

    private void koordinantGuncelle(final double latitude, final double longitude) {

        String url = Config.SERVER_IS+"koordinantGuncelle.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")){


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
                params.put("txtlatitude", String.valueOf(latitude));
                params.put("txtlongitude", String.valueOf(longitude));
                // params.put("txtDil",dil);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
//
        super.onBackPressed();
        sd.stop();

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

        tvSallaMessage.setText("Need\nPermission");

        tvSallaMessage.setOnClickListener(new View.OnClickListener() {

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

        tvSallaMessage.setOnClickListener(new View.OnClickListener() {

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

        .setMessage("Bu özelliği kullanmanız için konum ayarını açınız")

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

        tvSallaMessage.setOnClickListener(null);

        //tvKonum.setText(location.getLongitude() + "\n" + location.getLatitude());
        //tvKonum.setAlpha(1.0f);
           latitude=location.getLatitude();
           longitude=location.getLongitude();
        // getCompleteAddressString( location.getLatitude(),location.getLongitude());
          koordinantGuncelle(latitude,longitude); // veritabanına yeni konumu gönderiyoruz.

        //getAddress( location.getLatitude(),location.getLongitude());
        //tvKonum.setText(getCity());
        //  tvKonum.animate().alpha(0.5f).setDuration(400);


        }

@Override

public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

        tvSallaMessage.setText("brada mock var");

        tvSallaMessage.setOnClickListener(fromView);

        }



@Override

public void onError(LocationAssistant.ErrorType type, String message) {

        tvSallaMessage.setText("hata");

        }

}