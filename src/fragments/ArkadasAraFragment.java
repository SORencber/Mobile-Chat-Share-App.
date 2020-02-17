package com.yavuz.rencber.rencber.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.yavuz.rencber.rencber.GCM.ChatRoom;
import com.yavuz.rencber.rencber.GCM.SimpleDividerItemDecoration;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.Movie;
import com.yavuz.rencber.rencber.activity.MySingleton;
import com.yavuz.rencber.rencber.adapter.ChatRoomsAdapter;
import com.yavuz.rencber.rencber.adapter.ChatRoomsAdapterAra;

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

public class ArkadasAraFragment extends Fragment {

    View vi;
    private static final String TAG = ArkadasAraFragment.class.getSimpleName();
    public static int count;
    // Movies json url
    String url ;
    private SwipeRefreshLayout swipeRefreshLayout;
    // Movies json url
    public ArrayList<Movie> movieList = new ArrayList<Movie>();
    public ListView listView;
    private ChatRoomsAdapterAra adapter1;
    private ProgressDialog pDialog;
    ImageView ivAddFriend;
    int silmek;



    protected Handler handler;

    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,user_email,user_name,user_id;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<ChatRoom> chatRoomArrayList;
    private  ArrayList<ChatRoom> chatRoomArrayList_arama ;

    private ChatRoomsAdapterAra mAdapter;
    private RecyclerView recyclerView;
    SearchView searchView;
    Context mBase;
    EditText etSearch;

    public ArkadasAraFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_arkadas_ara, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        ivAddFriend=(ImageView)rootView.findViewById(R.id.ivAddFriend);
        etSearch=(EditText)rootView.findViewById(R.id.etArkadasBul);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());//preferences objesi
        editor = preferences.edit();
        user_email=preferences.getString("user_email","boş");
        user_id=preferences.getString("user_id","boş");
        user_name=preferences.getString("user_name","boş");
        eposta=preferences.getString("email","bos");
        /**
         * Broadcast receiver calls in two scenarios
         * 1. gcm registration is completed
         * 2. when new push notification is received
         * */

        chatRoomArrayList = new ArrayList<>();
        chatRoomArrayList_arama = new ArrayList<>();

        mAdapter = new ChatRoomsAdapterAra(getActivity(), chatRoomArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ChatRoomsAdapterAra.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ChatRoomsAdapterAra.ClickListener() {
            @Override
            public void onClick(View view, final int position) {


        }
            @Override
            public void onLongClick(View view, int position) {

            }

        }));




        fetchChatRooms(eposta);

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
                    mAdapter = new ChatRoomsAdapterAra(getActivity(), chatRoomArrayList_arama);

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
                // TODO Auto-generated method stub
                System.out.println("Text changed: " + arg0.toString());
            }
        });
        return rootView;

    }


    // edittext tab menude focuslanmayi calmasin diye


    private void fetchChatRooms(final String eposta) {
        //Toast.makeText(getActivity().getApplicationContext(),eposta,Toast.LENGTH_LONG).show();
        String url= Config.SERVER_IS+"arkadasara.php?keyword=&eposta="+eposta;
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                      //  Toast.makeText(getActivity().getApplicationContext(),  eposta.toString(), Toast.LENGTH_SHORT).show();


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
                                cr.setName(obj.getString("adi")+" "+obj.getString("soyadi") );
                                cr.setTo_user_id(obj.getString("kayit_no"));
                                cr.setFoto(obj.getString("foto"));
                                cr.setCinsiyet(obj.getString("cinsiyet"));
                                cr.setLastMessage("");
                                cr.setUnreadCount(0);
                                cr.setTimestamp(obj.getString("tarih"));
                                cr.setEmail(obj.getString("eposta"));
                                chatRoomArrayList.add(cr);
                            }  catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                            }
                        }




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

    private void ArkadasEkle(final String email, final String user_email) {

        String url = Config.SERVER_IS+"arkadasEkle.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("1")){


                            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.friendadded), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.alreadyfriendlist), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtKullanici", email);
                params.put("txtfriend",user_email);

                // params.put("txtDil",dil);

                return params;
            }
        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }


}
