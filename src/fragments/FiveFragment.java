package com.yavuz.rencber.rencber.fragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.yavuz.rencber.rencber.GCM.ChatRoom;
import com.yavuz.rencber.rencber.GCM.GcmIntentService;
import com.yavuz.rencber.rencber.GCM.Message;
import com.yavuz.rencber.rencber.GCM.MyApplication;
import com.yavuz.rencber.rencber.GCM.NotificationUtils;
import com.yavuz.rencber.rencber.GCM.SimpleDividerItemDecoration;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.ChatRoomActivity;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.GirisEkrani;
import com.yavuz.rencber.rencber.activity.IconTextTabsActivity;
import com.yavuz.rencber.rencber.activity.Movie;
import com.yavuz.rencber.rencber.activity.MySingleton;
import com.yavuz.rencber.rencber.activity.OdaIsmi;
import com.yavuz.rencber.rencber.adapter.ChatRoomsAdapter;
import com.yavuz.rencber.rencber.adapter.CustomListAdapter4;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.yavuz.rencber.rencber.adapter.TrendsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import static com.facebook.FacebookSdk.getApplicationContext;
/**
 * Created by Serhat Ömer RENÇBER on 2018.
 */

public class FiveFragment extends Fragment implements android.support.v7.widget.SearchView.OnQueryTextListener {

    View vi;
    private static final String TAG = FiveFragment.class.getSimpleName();
    public static int count;
    public static String chatRoomId2;
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
    private ArrayList<ChatRoom> chatRoomArrayList;
    private ArrayList<ChatRoom> chatRoomArrayList_arama;

    private ChatRoomsAdapter mAdapter;

    private RecyclerView recyclerView;
    private android.support.v7.widget.SearchView mSearchView;
    Context mBase;
    Button btngrupolustur;
   EditText etSearch;
    public FiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public static String isRoomId() {

        return chatRoomId2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_five, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        btngrupolustur = (Button) rootView.findViewById(R.id.btngrupolustur);
        etSearch=(EditText)rootView.findViewById(R.id.etSearch);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());//preferences objesi



        editor = preferences.edit();
        user_email = preferences.getString("user_email", "boş");
        user_id = preferences.getString("user_id", "boş");
        user_name = preferences.getString("user_name", "boş");
        eposta = preferences.getString("email", "bos");
        /**
         * Broadcast receiver calls in two scenarios
         * 1. gcm registration is completed
         * 2. when new push notification is received
         * */
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                chatRoomArrayList_arama.clear();
                String text = arg0.toString();
                if (text.isEmpty()) {
                    chatRoomArrayList_arama.addAll(chatRoomArrayList);
                } else {
                    text = text.toLowerCase();
                    for (ChatRoom item : chatRoomArrayList) {
                        if (item.getName().toLowerCase().contains(text)) {
                            chatRoomArrayList_arama.add(item);
                        }
                    }
                    mAdapter = new ChatRoomsAdapter(getActivity(), chatRoomArrayList_arama);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                            getActivity().getApplicationContext()
                    ));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                chatRoomArrayList_arama.clear();
                String text = arg0.toString();
                if (text.isEmpty()) {
                    chatRoomArrayList_arama.addAll(chatRoomArrayList);
                } else {
                    text = text.toLowerCase();
                    for (ChatRoom item : chatRoomArrayList) {
                        if (item.getName().toLowerCase().contains(text)) {
                            chatRoomArrayList_arama.add(item);
                        }
                    }
                    mAdapter = new ChatRoomsAdapter(getActivity(), chatRoomArrayList_arama);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                            getActivity().getApplicationContext()
                    ));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    subscribeToGlobalTopic();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                    Log.e(TAG, "GCM registration id is sent to our server");

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };

        chatRoomArrayList = new ArrayList<>();
        chatRoomArrayList_arama = new ArrayList<>();

        mAdapter = new ChatRoomsAdapter(getActivity(), chatRoomArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ChatRoomsAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ChatRoomsAdapter.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity

                // Odaya girerken mesaj sayisiniz sifirliyoruz
//                ChatRoom chatRoom = chatRoomArrayList.get(position);
//                chatRoom.setUnreadCount(0);
//                int cikacak = chatRoom.getUnreadCount();
//                int chatRoomMesajCount = preferences.getInt("mesajsayisi", 0);
//                int sonuc = chatRoomMesajCount - cikacak;
//                editor.putInt(chatRoom.getId(), 0);
//                editor.putInt("mesajsayisi", 0);
//
//                editor.commit();
//
//                IconTextTabsActivity.badgesohbet.setText("0");
//
//                IconTextTabsActivity.badgesohbet.hide();
//                mAdapter.notifyDataSetChanged();
//
//
//                Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
//                intent.putExtra("chat_room_id", chatRoom.getId());
//                intent.putExtra("name", chatRoom.getName());
//                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
//                intent.putExtra("foto", chatRoom.getFoto());
//
//
//                intent.putExtra("odaIsmi", chatRoom.getName());
//
//                if (chatRoom.getGrup().equals("") || chatRoom.getGrup().equals("0") || chatRoom.equals("null")) {
//                    intent.putExtra("grup", "0");
//                    intent.putExtra("to_eposta", chatRoom.getEmail());
//
//
//                } else {
//                    intent.putExtra("grup", "1");
//                    intent.putExtra("to_eposta", eposta);
//
//                }
//                if (chatRoom.getSahibi().equals("") || chatRoom.getSahibi().equals("0") || chatRoom.getSahibi().equals("null")) {
//                    intent.putExtra("sahibi", "0");
//
//
//                } else {
//                    intent.putExtra("sahibi", "1");
//
//                }
//                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        /**
         * Always check for google play services availability before
         * proceeding further with GCM
         * //*/
        if (checkPlayServices()) {
            registerGCM();
            fetchChatRooms();
        }

        btngrupolustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), OdaIsmi.class), 1);


                //  startActivity(intent);
            }
        });


        return rootView;

    }

    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);
        String grup = intent.getStringExtra("grup");

        Log.e("grup", grup);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == Config.PUSH_TYPE_CHATROOM) {
            Message message = (Message) intent.getSerializableExtra("message");
            chatRoomId2 = intent.getStringExtra("chat_room_id");

            if (message != null && chatRoomId2 != null) {
                updateRow(chatRoomId2, message);

            }
        }


    }

    /**
     * Updates the chat list unread count and the last message
     */
    public void updateRow(String chatRoomId2, Message message) {
        int chatRoomMesajCount = 0;
        for (ChatRoom cr : chatRoomArrayList)
            if (cr.getId().equals(chatRoomId2)) {
                int index = chatRoomArrayList.indexOf(cr);
                cr.setLastMessage(message.getMessage());
                chatRoomMesajCount = preferences.getInt(chatRoomId2, 0);
                editor.putInt(chatRoomId2, chatRoomMesajCount + 1);
                editor.commit();
                cr.setUnreadCount(chatRoomMesajCount + 1);

                chatRoomArrayList.remove(index);
                chatRoomArrayList.add(index, cr);
                break;
            }
        mAdapter.notifyDataSetChanged();
        //fetchChatRooms();
    }


    /**
     * fetching the chat rooms by making http call
     */
    private void fetchChatRooms() {
        // Toast.makeText(getActivity().getApplicationContext(),eposta,Toast.LENGTH_LONG).show();
        String url = Config.SERVER_IS + "arkadaslar.php?eposta=" + eposta;
        final int[] chatRoomMesajCount = new int[1];

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
                                ChatRoom cr = new ChatRoom();
                                cr.setId(obj.getString("no"));
                                String OdaNo = obj.getString("no");
                                chatRoomMesajCount[0] = preferences.getInt(OdaNo, 0);

                                cr.setUnreadCount(chatRoomMesajCount[0]);

                                cr.setGrup(obj.getString("grup"));
                                cr.setSahibi(obj.getString("sahibi"));
                                String grup = obj.getString("grup");

                                if (grup.equals("0")) {
                                    cr.setName(obj.getString("adi")+" "+obj.getString("soyadi") );

                                } else {
                                    cr.setName(obj.getString("kullanici"));
                                }
                                cr.setTo_user_id(obj.getString("kayit_no"));

                                cr.setFoto(obj.getString("foto"));
                                cr.setEmail(obj.getString("eposta"));

                                cr.setCinsiyet(obj.getString("cinsiyet"));
                                cr.setLastMessage("");
                                //cr.setUnreadCount(0);
                                cr.setTimestamp(obj.getString("tarih"));
                                //Log.e(TAG, "grup error: " + grup);
                                chatRoomArrayList.add(cr);
                            } catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                            }
                        }


                      /*  Collections.sort(chatRoomArrayList, new Comparator<ChatRoom>() {
                            public int compare(ChatRoom o1, ChatRoom o2) {
                                return o1.getTimestamp().compareTo(o2.getTimestamp());
                            }
                        });
*/
                        mAdapter.notifyDataSetChanged();

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

    // subscribing to global topic
    private void subscribeToGlobalTopic() {
        Intent intent = new Intent(getActivity(), GcmIntentService.class);
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
        intent.putExtra(GcmIntentService.TOPIC, Config.TOPIC_GLOBAL);
        getContext().startService(intent);

    }


    // Subscribing to all chat room topics
    // each topic name starts with `topic_` followed by the ID of the chat room
    // Ex: topic_1, topic_2
    private void subscribeToAllTopics() {
        for (ChatRoom cr : chatRoomArrayList) {

            Intent intent = new Intent(getActivity(), GcmIntentService.class);
            intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
            intent.putExtra(GcmIntentService.TOPIC, "topic_" + cr.getId());
            getContext().startService(intent);
        }
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(getActivity(), GirisEkrani.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        NotificationUtils.clearNotifications();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(getActivity(), GcmIntentService.class);
        intent.putExtra("key", "register");
        getContext().startService(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getActivity().getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
            return false;
        }
        return true;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.off:
                MyApplication.getInstance().logout();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            mAdapter.notifyDataSetChanged();

        }
    }


    private void deleteResim(final int resim_no, final String thumbnail) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS + "sil.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if (response.contains("success_login")) {
                            //refresh listview
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Birşeyler Yanlış Gitti", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtresim_no", (String.valueOf(resim_no)));
                params.put("txtthumbnail", (String.valueOf(thumbnail)));

                return params;
            }
        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub


    }


    private void fetchMovies() {
        movieList.clear();
        swipeRefreshLayout.setRefreshing(true);
        final Movie m = null;
        String url = Config.SERVER_IS + "arkadaslar.php?eposta=" + eposta;
// Creating volley request obj
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
                                Movie movie = new Movie();
                                movie.setAdi(obj.getString("adi"));
                                movie.setSoyadi(obj.getString("soyadi"));
                                movie.setEposta(obj.getString("eposta"));
                                movie.setDiL(obj.getString("dil"));


                                // movie.setYear(obj.getInt("yukari"));
                                movie.setYukari(obj.getInt("online"));
                                movie.setThumbnailUrl(obj.getString("foto"));

                                //  movie.setYorumsayisi(obj.getInt("yorumsayisi"));
                                // Genre is json array
                               /* JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);
*/
                                // adding movie to movies array
                                movieList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter1.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG);
        return true;
    }
}