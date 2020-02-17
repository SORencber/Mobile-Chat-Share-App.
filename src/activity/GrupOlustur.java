package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
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
import com.yavuz.rencber.rencber.GCM.SimpleDividerItemDecoration;
import com.yavuz.rencber.rencber.Grup.Trends.AlertDialogHelper;
import com.yavuz.rencber.rencber.Grup.Trends.GrupElemanlari;
import com.yavuz.rencber.rencber.Grup.Trends.RecyclerItemClickListener;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.adapter.CustomListAdapter4;
import com.yavuz.rencber.rencber.adapter.GrupOlusturAdapter;
import com.yavuz.rencber.rencber.adapter.ProfileKullanicilarAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GrupOlustur extends AppCompatActivity implements AlertDialogHelper.AlertDialogListener{

    View vi;
    private static final String TAG = GrupOlustur.class.getSimpleName();
    public static int count;
    // Movies json url
    String url;
    private SwipeRefreshLayout swipeRefreshLayout;
    // Movies json url
    public ArrayList<Movie> movieList = new ArrayList<Movie>();
    public ListView listView;
    private CustomListAdapter4 adapter1;
    private ProgressDialog pDialog;
    ImageView ivSil;
    int silmek;
    Spinner spinnerSonuc;

    protected Handler handler;

    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta, user_email, user_name, user_id;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    ArrayList<GrupElemanlari> user_list = new ArrayList<>();
    ArrayList<GrupElemanlari> multiselect_list = new ArrayList<>();
    String grup_list[];
    private GrupOlusturAdapter multiSelectAdapter;
    private GrupOlusturAdapter multiSelectAdapter2;

    private RecyclerView recyclerView,recyclerView_secilenler;
    SearchView searchView;
    Context mBase;
    AlertDialogHelper alertDialogHelper;
    ActionMode mActionMode;
    Menu context_menu;
    String OdaIsmi,hareket,chat_room_id;
    FloatingActionButton fab;
    boolean isMultiSelect = false;
    Button btnGrupOlustur;

    public GrupOlustur() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grup_olustur);
        Bundle extras = getIntent().getExtras();
        OdaIsmi = extras.getString("odaIsmi");
        hareket = extras.getString("hareket");
        chat_room_id = extras.getString("chat_room_id");

        alertDialogHelper = new AlertDialogHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView_secilenler = (RecyclerView) findViewById(R.id.recycler_view_grup_secilenler);

        fab = (FloatingActionButton) findViewById(R.id.fab2);
       // btnGrupOlustur=(Button)findViewById(R.id.btnGrupOlustur);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        user_email = preferences.getString("user_email", "boş");
        user_id = preferences.getString("user_id", "boş");
        user_name = preferences.getString("user_name", "boş");
        eposta = preferences.getString("email", "bos");

        fetchChatRooms();

        multiSelectAdapter = new GrupOlusturAdapter(this, user_list, multiselect_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(GrupOlustur.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(multiSelectAdapter);


        multiSelectAdapter2 = new GrupOlusturAdapter(this,multiselect_list, user_list);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView_secilenler.setLayoutManager(layoutManager2);
        recyclerView_secilenler.setMinimumHeight(200);

        recyclerView_secilenler.setMinimumWidth(200);
        recyclerView_secilenler.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()        ));
        recyclerView_secilenler.setItemAnimator(new DefaultItemAnimator());
        recyclerView_secilenler.setAdapter(multiSelectAdapter2);

        recyclerView.addOnItemTouchListener(new GrupOlusturAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GrupOlusturAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (isMultiSelect)
                    multi_select(position);
                else
                    Toast.makeText(getApplicationContext(), "Basılı Tutarak Başlayınız", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {


                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<GrupElemanlari>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }
        }));
        recyclerView_secilenler.addOnItemTouchListener(new GrupOlusturAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView_secilenler, new GrupOlusturAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (isMultiSelect)
                    multi_select2(position);
                else
                    Toast.makeText(getApplicationContext(), "Basılı Tutarak Başlayınız", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {


                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<GrupElemanlari>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }

                multi_select2(position);

            }
        }));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                alertDialogHelper.showAlertDialog("", "Gruba Ekle", "EKLE", "VAZGEÇ", 2, false);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_common_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings Click", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_exit:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchChatRooms() {
        // Toast.makeText(getActivity().getApplicationContext(),eposta,Toast.LENGTH_LONG).show();

        String url=Config.SERVER_IS+"arkadaslar.php?eposta="+eposta+"&room_id="+chat_room_id;

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
                                GrupElemanlari cr = new GrupElemanlari();
                                cr.setId(obj.getString("no"));
                                cr.setName(obj.getString("adi"));
                                cr.setTo_user_id(obj.getString("kayit_no"));
                                cr.setFoto(obj.getString("foto"));
                                cr.setCinsiyet(obj.getString("cinsiyet"));
                                cr.setEmail(obj.getString("eposta"));

                                cr.setLastMessage("");
                                cr.setUnreadCount(0);
                                cr.setTimestamp(obj.getString("tarih"));

                                user_list.add(cr);
                            }  catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                            }
                        }




                        multiSelectAdapter.notifyDataSetChanged();

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
    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(user_list.get(position))) {
                multiselect_list.remove(user_list.get(position));
            } else {
                multiselect_list.add(user_list.get(position));
            }
            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }

    public void multi_select2(int position) {
        if (mActionMode != null) {
            if (user_list.contains(multiselect_list.get(position))) {
                user_list.remove(multiselect_list.get(position));
            } else {
                user_list.add(user_list.get(position));
            }
            if (user_list.size() > 0)
                mActionMode.setTitle("" + user_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }

    public void refreshAdapter() {
        multiSelectAdapter.selected_usersList = multiselect_list;
        multiSelectAdapter.usersList = user_list;
        multiSelectAdapter2.selected_usersList = multiselect_list;
        multiSelectAdapter2.usersList = user_list;

        multiSelectAdapter.notifyDataSetChanged();
        multiSelectAdapter2.notifyDataSetChanged();

    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    alertDialogHelper.showAlertDialog("", "Listeden Sil", "Sil ", "Vazgeç", 1, false);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<GrupElemanlari>();
            refreshAdapter();
        }
    };

    // AlertDialog Callback Functions

    @Override
    public void onPositiveClick(int from) {
        grup_list=new String[multiselect_list.size()];

        if (from == 1) {
            if (multiselect_list.size() > 0) {
                for (int i = 0; i < multiselect_list.size(); i++) {
                    user_list.remove(multiselect_list.get(i));
                    grup_list[i] = multiselect_list.get(i).getEmail();
                }
                sendDataGrup(grup_list,"sil");
                multiSelectAdapter.notifyDataSetChanged();

                if (mActionMode != null) {
                    mActionMode.finish();
                }
                Toast.makeText(getApplicationContext(), "Silmeye Tıkladınız", Toast.LENGTH_SHORT).show();
            }

        } else if (from == 2) {

            if (multiselect_list.size() > 0) {
                for (int i = 0; i < multiselect_list.size(); i++) {
                    user_list.remove(multiselect_list.get(i));
                    grup_list[i]=multiselect_list.get(i).getEmail();


                  //  Toast.makeText(getApplicationContext(), "Email adresleri", Toast.LENGTH_SHORT).show();

                }
                if(hareket.equals("guncelle"))
                { sendDataGrup(grup_list,"guncelle");}else{
                sendDataGrup(grup_list,"ekle");}

                multiSelectAdapter.notifyDataSetChanged();
                //finish();

            }
                if (mActionMode != null) {
                mActionMode.finish();
            }

           // user_list.add(mSample);

        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }


    public void sendDataGrup (final String grup_listesi[],final String hareket) {

        String url;  // grubu kuran kişinin epostasınıda listeye gekliyoruz
            if(hareket.equals("sil"))
            {
               url  = Config.SERVER_IS + "grupElemanSil.php";

            }else if(hareket.equals("ekle")){
                 url = Config.SERVER_IS + "grupEkle.php";
            }else {
                url = Config.SERVER_IS + "grupGuncelle.php";
            }
        Log.e("url", url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Web", response);
                            if (response.contains("1")) {


                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.friendadded), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alreadyfriendlist), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    for(int i=0; i < grup_listesi.length; i++)
                    {
                        params.put("friend["+i+"]", grup_listesi[i]);
                        Log.e("friend["+i+"]", grup_listesi[i]);

                    }
                    params.put("toplam", String.valueOf(grup_listesi.length));
                    params.put("OdaIsmi", OdaIsmi);
                    params.put("grupbaskani", eposta);
                    params.put("chat_room_id", chat_room_id);

                    Log.e("gruplar", params.toString());

                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }
    }



