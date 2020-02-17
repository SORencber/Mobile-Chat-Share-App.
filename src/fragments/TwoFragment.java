package com.yavuz.rencber.rencber.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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

import com.yavuz.rencber.rencber.Bildirim.Profile.BildirimYorumlar;
import com.yavuz.rencber.rencber.GCM.NotificationUtils;
import com.yavuz.rencber.rencber.GCM.SimpleDividerItemDecoration;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.IconTextTabsActivity;
import com.yavuz.rencber.rencber.activity.MySingleton;
import com.yavuz.rencber.rencber.adapter.BildirimlerAdapter;
import com.yavuz.rencber.rencber.adapter.BildirimlerResimlerAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Serhat Ömer RENÇBER on 2018.
 */
public class TwoFragment extends Fragment {


    View root;

    private static final String TAG = TwoFragment.class.getSimpleName();
    public static int count;
    public static  String chatRoomId2 ;
    String url ;



    protected Handler handler;

    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,user_email,user_name,user_id;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<BildirimYorumlar> toArkadasEkleList;
    private BildirimlerAdapter mAdapter;
    TabLayout tabLayout;
    private ArrayList<BildirimYorumlar> toBildirimResimlerList;
    private BildirimlerResimlerAdapter mAdapter2;

    private RecyclerView recyclerView1,recyclerView2;
    SearchView searchView;
    Context mBase;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_two, container, false);

        recyclerView1 = (RecyclerView) root.findViewById(R.id.recycler_view_arkadaslik_istekleri);

        recyclerView2 = (RecyclerView) root.findViewById(R.id.recycler_view_resim_yorumlari);
       // recyclerView3 = (RecyclerView) root.findViewById(R.id.recycler_view_3);


        /////////////////////////////////////////////////////////////////////////////////////////////

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());//preferences objesi
        editor = preferences.edit();

        editor.putInt("bildirimler", 0);

        editor.commit();
        ShortcutBadger.removeCount(getActivity()); //for 1.1.4+
        //IconTextTabsActivity.badgebildirimler.setText("0");
        IconTextTabsActivity.badgebildirimler.hide();
        user_email=preferences.getString("user_email","boş");
        user_id=preferences.getString("user_id","boş");
        user_name=preferences.getString("user_name","boş");
        eposta=preferences.getString("email","bos");

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

              if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };


        getirBildirimleri(eposta);
        toArkadasEkleList = new ArrayList<>();
        mAdapter = new BildirimlerAdapter(getActivity(), toArkadasEkleList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setMinimumWidth(200);
        recyclerView1.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()        ));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(mAdapter);

        toBildirimResimlerList = new ArrayList<>();
        mAdapter2 = new BildirimlerResimlerAdapter(getActivity(), toBildirimResimlerList);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setMinimumWidth(200);
        recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()        ));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapter2);

       /* LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setMinimumWidth(200);
        recyclerView3.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()        ));
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setAdapter(mAdapter);
*/
        recyclerView1.addOnItemTouchListener(new BildirimlerAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView1, new BildirimlerAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView2.addOnItemTouchListener(new BildirimlerResimlerAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView2, new BildirimlerResimlerAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return root;

    }

    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);
        String grup = intent.getStringExtra("grup");
       // IconTextTabsActivity.badgebildirimler.setText("0");
        IconTextTabsActivity.badgebildirimler.hide();
      //  IconTextTabsActivity.badgebildirimler.show();
        ShortcutBadger.removeCount(getActivity()); //for 1.1.4+
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());//preferences objesi
        editor = preferences.edit();

        editor.putInt("bildirimler", 0);

        editor.commit();
        //Log.e("grup", grup);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (grup.equals("1") || grup.equals("2")) {

           // Message message = (Message) intent.getSerializableExtra("message");
          //  chatRoomId2 = intent.getStringExtra("chat_room_id");

           // if (message != null && chatRoomId2 != null) {
               // updateRow(userid,message,resimno,resimyol,resimbaslik,gonderenfoto, gonderenisim,gondereneposta);

          //  }
        } else if (grup.equals("3")) {
            String message="Yorum yapıldı";

            String userid=intent.getStringExtra("to_user_id");
            String resimno=intent.getStringExtra("resimno");
            String resimyol=intent.getStringExtra("resimyol");
            String resimbaslik=intent.getStringExtra("resimbaslik");
            String gonderenfoto= intent.getStringExtra("gonderenfoto");
            String gonderenisim=intent.getStringExtra("gonderenisim");
            String gondereneposta=intent.getStringExtra("gondereneposta");
            intent.getStringExtra("silmek");

            updateRow_ResimYorumList(grup,userid,message,resimno,resimyol,resimbaslik,gonderenfoto, gonderenisim,gondereneposta);
        }
     else if (grup.equals("4")) {
        String message=intent.getStringExtra("message");
        String userid=intent.getStringExtra("to_user_id");
        String resimno=intent.getStringExtra("resimno");
        String resimyol=intent.getStringExtra("resimyol");
        String resimbaslik=intent.getStringExtra("resimbaslik");
        String gonderenfoto= intent.getStringExtra("gonderenfoto");
        String gonderenisim=intent.getStringExtra("gonderenisim");
        String gondereneposta=intent.getStringExtra("gondereneposta");

        intent.getStringExtra("silmek");
            updateRow_ArkadasEkle(grup,userid,message,resimno,resimyol,resimbaslik,gonderenfoto, gonderenisim,gondereneposta);


    }
        else if (grup.equals("5")) {
            String message="Arkadaşlık isteğini kabul etti";
            String userid=intent.getStringExtra("to_user_id");
            String resimno=intent.getStringExtra("resimno");
            String resimyol=intent.getStringExtra("resimyol");
            String resimbaslik=intent.getStringExtra("resimbaslik");
            String gonderenfoto= intent.getStringExtra("gonderenfoto");
            String gonderenisim=intent.getStringExtra("gonderenisim");
            String gondereneposta=intent.getStringExtra("gondereneposta");

            intent.getStringExtra("silmek");
            updateRow_ResimYorumList(grup,userid,message,resimno,resimyol,resimbaslik,gonderenfoto, gonderenisim,gondereneposta);


        }   else if (grup.equals("5")) {
            String message="Arkadaşlık isteğini kabul etti";
            String userid=intent.getStringExtra("to_user_id");
            String resimno=intent.getStringExtra("resimno");
            String resimyol=intent.getStringExtra("resimyol");
            String resimbaslik=intent.getStringExtra("resimbaslik");
            String gonderenfoto= intent.getStringExtra("gonderenfoto");
            String gonderenisim=intent.getStringExtra("gonderenisim");
            String gondereneposta=intent.getStringExtra("gondereneposta");

            intent.getStringExtra("silmek");
            updateRow_ResimYorumList(grup,userid,message,resimno,resimyol,resimbaslik,gonderenfoto, gonderenisim,gondereneposta);


        }
    }

    /**
     * Updates the chat list unread count and the last message
     */
    public void updateRow(String userid, String message, String resimno, String resimyol, String resimbaslik, String gonderenfoto, String gonderenisim,String gondereneposta) {
        BildirimYorumlar cr = new BildirimYorumlar();
        cr.setId(chatRoomId2);
        cr.setLastMessage(message);
        cr.setTimestamp("");
        cr.setUnreadCount(0);
        cr.setEmail(user_email);
        cr.setName(user_name);
        cr.setCinsiyet("");
        cr.setFoto("");

        toArkadasEkleList.add(cr);

        mAdapter.notifyDataSetChanged();
    }

    public void updateRow_ResimYorumList(String grup,String userid, String message, String resimno, String resimyol, String resimbaslik, String gonderenfoto, String gonderenisim,String gondereneposta) {
        Log.e("grup", grup);

       BildirimYorumlar cr = new BildirimYorumlar();
        cr.setResim_no(resimno);
        cr.setLastMessage(message);
        cr.setTimestamp("");
        cr.setUnreadCount(0);
       // cr.setEmail(user_email);
        cr.setName(gonderenisim);
        cr.setCinsiyet("");
        cr.setFoto(gonderenfoto);
        cr.setTitle("yorum yapıldı");
        cr.setThumbnailUrl(resimyol);
        cr.setEmail(gondereneposta);

        toBildirimResimlerList.add(cr);
     sendData (grup,userid,  message,  resimno,  resimyol,  "",  gonderenfoto,  gonderenisim,gondereneposta) ;

        mAdapter2.notifyDataSetChanged();
    }

    public void updateRow_ArkadasEkle(String grup,String userid, String message, String resimno, String resimyol, String resimbaslik, String gonderenfoto, String gonderenisim,String gondereneposta) {
        {
            BildirimYorumlar cr = new BildirimYorumlar();
            cr.setTo_user_id(userid);
            //  cr.setLastMessage(message);
            cr.setTimestamp("");
            cr.setUnreadCount(0);
            // cr.setEmail(user_email);
            cr.setName(gonderenisim);
            cr.setEmail(gondereneposta);

            cr.setCinsiyet("");
            cr.setFoto(gonderenfoto);
            cr.setTitle("yorum yapıldı");
            cr.setLastMessage("Yorum Yaptı");
            String message2 = "Yorum Yaptı";
            sendData(grup,userid, "Arkadaşlık isteği", "", "", "", gonderenfoto, gonderenisim, gondereneposta);

            toArkadasEkleList.add(cr);

            mAdapter.notifyDataSetChanged();
        }
    }



    private void      sendData (final String grup,final String userid, final String message, final String resimno, final String resimyol, final String resimbaslik, final String gonderenfoto, final String gonderenisim, final String gondereneposta) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"bildirimler.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if (response.contains("Wrong")) {

                        } else {
                                try {
                                    //JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = new JSONArray(response);
                                    //JSONArray result = jsonObject.getJSONArray(response);
                                    JSONObject obj = result.getJSONObject(0);
                                    BildirimYorumlar cr = new BildirimYorumlar();


                              //  cr.setTo_user_id(obj.getString("kayit_no"));
                                cr.setEmail(obj.getString("gondereneposta"));
                                cr.setFoto(obj.getString("foto"));
                                    cr.setId(obj.getString("b_no"));
                                cr.setName(obj.getString("adi"));
                                cr.setResim_no(obj.getString("resim_no"));
                                cr.setThumbnailUrl(obj.getString("resimyol"));

                              //  Toast.makeText(getActivity(),cr.getGrup(),Toast.LENGTH_SHORT).show();

                                cr.setLastMessage(obj.getString("mesaj"));
                                cr.setUnreadCount(0);
                              //  Log.e(TAG, "grup error: " + grup);
                               if (grup.equals("4")){
                                   toArkadasEkleList.add(cr);
                                }else
                                    {
                                        toBildirimResimlerList.add(cr);


                                }
                            }  catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                            }
                        }


                            mAdapter2.notifyDataSetChanged();


                            mAdapter.notifyDataSetChanged();





                    }




                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("eposta", eposta.toString());
                params.put("message",message.toString());
                    params.put("resimno", resimno.toString());
                    params.put("resimyol", resimyol.toString());
                params.put("gonderenfoto", gonderenfoto.toString());
                params.put("gonderenisim",gonderenisim.toString());
                params.put("gondereneposta",gondereneposta.toString());
                params.put("grup",grup.toString());
                Log.e("params",params.toString());
                return params;
            }
        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }




    private void getirBildirimleri(String eposta) {


        //Toast.makeText(getActivity().getApplicationContext(),eposta,Toast.LENGTH_LONG).show();
        String url= Config.SERVER_IS+"tumbildirimler.php?eposta="+eposta;
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
                                BildirimYorumlar cr = new BildirimYorumlar();
                                //  cr.setTo_user_id(obj.getString("kayit_no"));
                                cr.setEmail(obj.getString("gondereneposta"));
                                cr.setFoto(obj.getString("foto"));
                                cr.setId(obj.getString("b_no"));
                                cr.setLastMessage(obj.getString("mesaj"));

                                cr.setName(obj.getString("adi"));
                                cr.setResim_no(obj.getString("resim_no"));
                                cr.setThumbnailUrl(obj.getString("resimyol"));
                                cr.setGrup(obj.getString("grup"));
                              //  Toast.makeText(getActivity(),cr.getGrup(),Toast.LENGTH_SHORT).show();

                                cr.setUnreadCount(0);
                               // Log.e(TAG, "grup error: " + cr.getGrup());

                                if (cr.getGrup().equals("4")){
                                    toArkadasEkleList.add(cr);
                                }else
                                {
                                    toBildirimResimlerList.add(cr);


                                }
                            }  catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                            }
                        }


                        mAdapter2.notifyDataSetChanged();
                         mAdapter.notifyDataSetChanged();





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





    @Override
    public void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        NotificationUtils.clearNotifications();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


















}


