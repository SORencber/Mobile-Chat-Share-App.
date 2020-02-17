package com.yavuz.rencber.rencber.activity;

////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.yavuz.rencber.rencber.GCM.*;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.adapter.ChatRoomsAdapter;
import com.yavuz.rencber.rencber.adapter.CustomListAdapter4;
import com.yavuz.rencber.rencber.fragments.FiveFragment;

import java.util.ArrayList;
import java.util.logging.Handler;

import static java.security.AccessController.getContext;

public class Bildirimler extends AppCompatActivity {
    private static final String TAG = Bildirimler.class.getSimpleName();
    public static int count;
    public static  String chatRoomId2 ;
    // Movies json url
    String url ;
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
    String eposta,user_email,user_name,user_id;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<ChatRoom> chatRoomArrayList;
    private ArrayList<ChatRoom> chatRoomArrayList_arama;

    private ChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;
    SearchView searchView;
    Context mBase;
    Button btngrupolustur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildirimler);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_bildirimler);
        btngrupolustur=(Button)findViewById(R.id.btngrupolustur);
        editor = preferences.edit();
        user_email=preferences.getString("user_email","boş");
        user_id=preferences.getString("user_id","boş");
        user_name=preferences.getString("user_name","boş");
        eposta=preferences.getString("email","bos");


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

        mAdapter = new ChatRoomsAdapter(getApplicationContext(), chatRoomArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ChatRoomsAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new ChatRoomsAdapter.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                ChatRoom chatRoom = chatRoomArrayList.get(position);

                Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                intent.putExtra("foto", chatRoom.getFoto());
                intent.putExtra("to_eposta", chatRoom.getEmail());

                intent.putExtra("odaIsmi", chatRoom.getName());

                if(chatRoom.getGrup().equals("")|| chatRoom.getGrup().equals("0")|| chatRoom.equals("null"))
                {
                    intent.putExtra("grup", "0");


                }else {
                    intent.putExtra("grup", "1");

                }
                if(chatRoom.getSahibi().equals("")|| chatRoom.getSahibi().equals("0")|| chatRoom.getSahibi().equals("null"))
                {
                    intent.putExtra("sahibi", "0");


                }else {
                    intent.putExtra("sahibi", "1");

                }
                startActivity(intent);
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
        }

    }
    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);
        String grup = intent.getStringExtra("grup");

        Log.e(TAG, "grup");

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (grup.equals("0")) {
            Message message = (Message) intent.getSerializableExtra("message");
            chatRoomId2 = intent.getStringExtra("chat_room_id");

            if (message != null && chatRoomId2 != null) {
                updateRow(chatRoomId2, message);

            }
        } else if (grup.equals("1")) {
            // push belongs to user alone
            // just showing the message in a toast
            Message message = (Message) intent.getSerializableExtra("message");
            Toast.makeText(getApplicationContext(), "New push: " + message.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    /**
     * Updates the chat list unread count and the last message
     */
    public void updateRow(String chatRoomId2, Message message) {
        ChatRoom cr = new ChatRoom();
        cr.setId(chatRoomId2);
        cr.setLastMessage(message.getMessage());
        cr.setTimestamp("");
        cr.setUnreadCount(0);
        cr.setEmail(user_email);
        cr.setName(user_name);
        cr.setCinsiyet("");
        cr.setFoto("");

        chatRoomArrayList.add(cr);

        mAdapter.notifyDataSetChanged();
    }

    // subscribing to global topic
    private void subscribeToGlobalTopic() {
        Intent intent = new Intent(getApplicationContext(), com.yavuz.rencber.rencber.GCM.GcmIntentService.class);
        intent.putExtra(com.yavuz.rencber.rencber.GCM.GcmIntentService.KEY, com.yavuz.rencber.rencber.GCM.GcmIntentService.SUBSCRIBE);
        intent.putExtra(com.yavuz.rencber.rencber.GCM.GcmIntentService.TOPIC, Config.TOPIC_GLOBAL);
        Bildirimler.this.startService(intent);

    }




    // Subscribing to all chat room topics
    // each topic name starts with `topic_` followed by the ID of the chat room
    // Ex: topic_1, topic_2
    private void subscribeToAllTopics() {
        for (ChatRoom cr : chatRoomArrayList) {

            Intent intent = new Intent(getApplicationContext(), com.yavuz.rencber.rencber.GCM.GcmIntentService.class);
            intent.putExtra(com.yavuz.rencber.rencber.GCM.GcmIntentService.KEY, com.yavuz.rencber.rencber.GCM.GcmIntentService.SUBSCRIBE);
            intent.putExtra(com.yavuz.rencber.rencber.GCM.GcmIntentService.TOPIC, "topic_" + cr.getId());
            Bildirimler.this.startService(intent);
        }
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(Bildirimler.this, GirisEkrani.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Bildirimler.this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        NotificationUtils.clearNotifications();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(getApplicationContext(), com.yavuz.rencber.rencber.GCM.GcmIntentService.class);
        intent.putExtra("key", "register");
        getApplicationContext().startService(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(Bildirimler.this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                Bildirimler.this.finish();
            }
            return false;
        }
        return true;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = Bildirimler.this.getMenuInflater();
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



}
