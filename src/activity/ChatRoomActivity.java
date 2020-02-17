package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;
import com.android.volley.toolbox.ImageLoader;
import com.yavuz.rencber.rencber.GCM.ChatRoom;
import com.yavuz.rencber.rencber.GCM.Message;
import com.yavuz.rencber.rencber.GCM.NotificationUtils;
import com.yavuz.rencber.rencber.GCM.SimpleDividerItemDecoration;
import com.yavuz.rencber.rencber.GCM.User;
import com.yavuz.rencber.rencber.Grup.Trends.AlertDialogHelper;
import com.yavuz.rencber.rencber.Grup.Trends.GrupElemanlari;
import com.yavuz.rencber.rencber.Grup.Trends.RecyclerItemClickListener;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.adapter.ChatRoomThreadAdapter;
import com.yavuz.rencber.rencber.adapter.ChatRoomsAdapter;
import com.yavuz.rencber.rencber.audio.AndroidMultiPartEntity;
import com.yavuz.rencber.rencber.audio.ui.ViewProxy;
import com.yavuz.rencber.rencber.cropper.CropImage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChatRoomActivity extends AppCompatActivity implements AlertDialogHelper.AlertDialogListener {

    private String TAG = ChatRoomActivity.class.getSimpleName();
    static Bitmap mImage;
    String url;
    public static String chatRoomId, gcm_registration_id, kayit_no;
    public static  String to_user_id, gelenfoto, to_eposta, grup, sahibi;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<Message> messageArrayList;
    private ArrayList<Message> multiselect_list;
    boolean isMultiSelect = false;
    AlertDialogHelper alertDialogHelper;
    ActionMode mActionMode;
    Menu context_menu;
    String grup_list[];
    String OdaIsmi,hareket,chat_room_id;
    FloatingActionButton fab;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public EditText inputMessage;
    private Button btnSend;
    NetworkImageView fotogoster;
    NetworkImageView resimlimesaj;
    public static ChatRoomActivity instance2 = null;
    ProgressDialog dialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private TextView isim, tvYaziyor;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    public String eposta, user_email, user_name, user_id, image, sendcalistir, encodedString, sqleposta, odaIsmi;
    public ProgressBar progressBar;
    public TextView txtPercentage;
    Ringtone rbasla,rbitir;

    ImageView resimekle, sohbetkapat, engelle, grubaekle, ivBtnSend;

    ///Voice Recorder icin
    private TextView recordTimeText;
    public static final int RECORD_AUDIO = 0;
    private ImageButton audioSendButton;
    private ImageView imageView9;

    private View recordPanel;

    private View slideText;

    private float startedDraggingX = -1;

    private float distCanMove = dp(80);

    private long startTime = 0L;

    long timeInMilliseconds = 0L;

    long timeSwapBuff = 0L;

    long updatedTime = 0L;

    private Timer timer;

    private MediaRecorder myRecorder;
private MediaPlayer myPlayer;
private String outputFile = null;
    private String dosyaismi = null;

    long totalSize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        alertDialogHelper = new AlertDialogHelper(this);
        instance2 = this; //kapatma icin

        inputMessage = (EditText) findViewById(R.id.message);
        ivBtnSend = (ImageView) findViewById(R.id.ivBtnSend);
        fotogoster = (NetworkImageView) findViewById(R.id.foto2);
        resimekle = (ImageView) findViewById(R.id.resimekle);
        sohbetkapat = (ImageView) findViewById(R.id.ivSohbetKapat);
        grubaekle = (ImageView) findViewById(R.id.grubaekle);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBarchat);
        resimlimesaj = (NetworkImageView) findViewById(R.id.buyukresimlimesaj);

        isim = (TextView) findViewById(R.id.isim2);
        tvYaziyor = (TextView) findViewById(R.id.tvYaziyor);

        recordPanel = findViewById(R.id.record_panel);
        recordTimeText = (TextView) findViewById(R.id.recording_time_text);
        slideText = findViewById(R.id.slideText);
        audioSendButton = (ImageButton) findViewById(R.id.chat_audio_send_button);
        imageView9 = (ImageView) findViewById(R.id.imageview9);

        TextView textView = (TextView) findViewById(R.id.slideToCancelTextView);
        textView.setText(getApplicationContext().getResources().getString(R.string.dragandcancel));

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chat_room_id");
        grup = intent.getStringExtra("grup");
        sahibi = intent.getStringExtra("sahibi");
        odaIsmi = intent.getStringExtra("odaIsmi");

        final String title = intent.getStringExtra("name");
        to_user_id = intent.getStringExtra("to_user_id");
        gelenfoto = intent.getStringExtra("foto");
        sendcalistir = intent.getStringExtra("sendcalistir");
        encodedString = intent.getStringExtra("encodedString");
        to_eposta = intent.getStringExtra("to_eposta");

        //image = intent.getStringExtra("image");
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        user_email = preferences.getString("user_email", "boş");
        user_id = preferences.getString("user_id", "boş");
        user_name = preferences.getString("user_name", "boş");
        eposta = preferences.getString("email", "boş");


        if (sahibi.equals("0")) {
            grubaekle.setVisibility(View.INVISIBLE);
        } else {
        }

// getSupportActionBar().setTitle(title);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getProfile(to_user_id);

        if (sendcalistir != "") {
            //  Toast.makeText(getApplicationContext(), encodedString, Toast.LENGTH_LONG).show();

            sendMessage(1, encodedString);

        }
        if (chatRoomId == null) {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (MainActivity.instance != null) {
            try {
                MainActivity.instance.finish();
            } catch (Exception e) {
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();
        multiselect_list=new ArrayList<>();
        // self user id is to identify the message owner
        String selfUserId = user_id;

        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList, selfUserId,multiselect_list);
        LinearLayoutManager  layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        layoutManager.setStackFromEnd(true);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                //else
                   // Toast.makeText(getApplicationContext(), "Basılı Tutarak Başlayınız", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<Message>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }
        }));




        inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String text = arg0.toString();

                if (text.isEmpty()) {
                    Yaziyor(7);

                    resimekle.setVisibility(View.VISIBLE);
                    ivBtnSend.setVisibility(View.INVISIBLE);
                    audioSendButton.setVisibility(View.VISIBLE);
                    recordPanel.setVisibility(View.INVISIBLE);
                } else {

                    Yaziyor(6);
                    resimekle.setVisibility(View.INVISIBLE);
                    ivBtnSend.setVisibility(View.VISIBLE);
                    audioSendButton.setVisibility(View.INVISIBLE);
                   // recordPanel.setVisibility(View.INVISIBLE);

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
                Log.e(TAG, "Text changed: " + arg0.toString());
            }
        });
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                }
            }
        };

        ivBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage(0, "");
            }
        });
        sohbetkapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }


        });


        audioSendButton.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View view, MotionEvent motionEvent) {

                resimekle.setVisibility(View.GONE);
                ivBtnSend.setVisibility(View.GONE);
                inputMessage.setVisibility(View.GONE);

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    playNotificationSound();

                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText

                            .getLayoutParams();

                    params.leftMargin = dp(30);

                    slideText.setLayoutParams(params);

                    ViewProxy.setAlpha(slideText, 1);

                    startedDraggingX = -1;

                    // startRecording();
                   // Toast.makeText(getApplicationContext(), "Aşağı..",
                     //       Toast.LENGTH_SHORT).show();
                    startrecord();

                    audioSendButton.getParent()

                            .requestDisallowInterceptTouchEvent(true);

                    recordPanel.setVisibility(View.VISIBLE);
                    recordTimeText.setVisibility(View.VISIBLE);
                    imageView9.setVisibility(View.VISIBLE);
                } else if ( motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {

                    startedDraggingX = -1;
                    recordPanel.setVisibility(View.INVISIBLE);
                    recordTimeText.setVisibility(View.INVISIBLE);
                    imageView9.setVisibility(View.INVISIBLE);

                    resimekle.setVisibility(View.VISIBLE);
                    ivBtnSend.setVisibility(View.INVISIBLE);
                    inputMessage.setVisibility(View.VISIBLE);
                    Gonder();
                    //Toast.makeText(getApplicationContext(), "Yukarı veya vazgeç..",
                      //      Toast.LENGTH_SHORT).show();

                    // stopRecording(true);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    Toast.makeText(getApplicationContext(), "OutSide.",
                            Toast.LENGTH_SHORT).show();

                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                   // playNotificationSoundIptal();
                    float x = motionEvent.getX();

                    if (x < -distCanMove) {
                       /* recordPanel.setVisibility(View.INVISIBLE);
                        recordTimeText.setVisibility(View.INVISIBLE);
                        imageView9.setVisibility(View.INVISIBLE);

                        resimekle.setVisibility(View.VISIBLE);
                        ivBtnSend.setVisibility(View.INVISIBLE);
                        inputMessage.setVisibility(View.VISIBLE);*/
                        stoprecord();
                      //  Toast.makeText(getApplicationContext(), "Hareket..",
                        //        Toast.LENGTH_SHORT).show();
                        // stopRecording(false);

                    }

                    x = x + ViewProxy.getX(audioSendButton);

                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText

                            .getLayoutParams();

                    if (startedDraggingX != -1) {

                        float dist = (x - startedDraggingX);

                        params.leftMargin = dp(30) + (int) dist;

                        slideText.setLayoutParams(params);

                        float alpha = 1.0f + dist / distCanMove;

                        if (alpha > 1) {

                            alpha = 1;

                        } else if (alpha < 0) {

                            alpha = 0;

                        }

                        ViewProxy.setAlpha(slideText, alpha);

                    }

                    if (x <= ViewProxy.getX(slideText) + slideText.getWidth()

                            + dp(30)) {

                        if (startedDraggingX == -1) {

                            startedDraggingX = x;

                            distCanMove = (recordPanel.getMeasuredWidth()

                                    - slideText.getMeasuredWidth() - dp(48)) / 2.0f;

                            if (distCanMove <= 0) {

                                distCanMove = dp(80);

                            } else if (distCanMove > dp(80)) {

                                distCanMove = dp(80);

                            }

                        }

                    }

                    if (params.leftMargin > dp(30)) {

                        params.leftMargin = dp(30);

                        slideText.setLayoutParams(params);

                        ViewProxy.setAlpha(slideText, 1);

                        startedDraggingX = -1;

                    }

                }

                view.onTouchEvent(motionEvent);

                return true;

            }

        });

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        resimekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("foto", "2");
                editor.putString("chat_room_id", chatRoomId);
                editor.putString("to_user_id", to_user_id);
                editor.putString("name", title);
                editor.putString("user_id", user_id);
                editor.putString("grup", grup);
                editor.putString("sahibi", sahibi);
                editor.putString("odaIsmi", odaIsmi);

                //  editor.putString("fotox", gelenfoto);

                editor.commit();// degerleri telefona kaydediyoruz

                Intent i = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(i);
            }
        });
        fotogoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("foto", "2");
                editor.putString("chat_room_id", chatRoomId);
                editor.putString("to_user_id", to_user_id);
                editor.putString("name", title);
                editor.putString("user_id", user_id);

                //  editor.putString("fotox", gelenfoto);

                editor.commit();// degerleri telefona kaydediyoruz

                Intent i = new Intent(getApplicationContext(), KullaniciProfili.class);
                i.putExtra("to_user_id", to_user_id);
                i.putExtra("to_eposta", to_eposta);

                startActivity(i);
            }
        });
        grubaekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ChatRoomActivity.this, GrupOlustur.class);
                i.putExtra("hareket", "guncelle");
                i.putExtra("odaIsmi", odaIsmi);
                i.putExtra("chat_room_id", chatRoomId);

                startActivity(i);
            }
        });
        fetchChatThread();


    }
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + AppController.getInstance().getApplicationContext().getPackageName() + "/raw/seslimesaj");
            rbitir = RingtoneManager.getRingtone(AppController.getInstance().getApplicationContext(), alarmSound);
            rbitir.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(messageArrayList.get(position)))
                multiselect_list.remove(messageArrayList.get(position));
            else
                multiselect_list.add(messageArrayList.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }


    public void refreshAdapter() {
        mAdapter.selected_usersList = multiselect_list;
        mAdapter.messageArrayList = messageArrayList;
        mAdapter.notifyDataSetChanged();
    }
    public void playNotificationSoundIptal() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + AppController.getInstance().getApplicationContext().getPackageName() + "/raw/seslimesajiptal");
            rbitir = RingtoneManager.getRingtone(AppController.getInstance().getApplicationContext(), alarmSound);
            rbitir.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void finish() {
        super.finish();
        instance2 = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /// GCM den gelen mesajlarin dogru odaya yazmasini saglamak icin
    ///NotificationUtils.java dosyasina degisken degeri gonderiyoruz.
    public static String isRoomId() {

        return chatRoomId;
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     */
    private void handlePushNotification(Intent intent) {
        String grup = intent.getStringExtra("grup");
        Log.e(TAG, "gelen grup:->>>>>>>" + grup);


        if (grup.equals("0") || grup.equals("1")) {

            Message message = (Message) intent.getSerializableExtra("message");
            String chatRoomId2 = intent.getStringExtra("chat_room_id");


            if (message != null && chatRoomId2 != null) {
                Okundu();
                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
                editor = preferences.edit();
                int mesajsayisi=preferences.getInt("mesajsayisi",0);

                editor.putInt(chatRoomId2, 0);
                editor.putInt(chatRoomId2, mesajsayisi-1);

                editor.commit();
                ShortcutBadger.applyCount(getApplicationContext(), mesajsayisi-1);//for 1.1.4+

                messageArrayList.add(message);
                mAdapter.notifyDataSetChanged();
                if (mAdapter.getItemCount() > 1) {
                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                }
            }
            /// mesaj yazarken karsi taraftan notify aliyoruz
        } else if (grup.equals("6")) {
            tvYaziyor.setVisibility(View.VISIBLE);
            tvYaziyor.setText(isim.getText().toString() + "  " + getApplicationContext().getResources().getString(R.string.typing));
            Toast.makeText(getApplicationContext(), grup.toString(), Toast.LENGTH_SHORT);

        } else if (grup.equals("7")) {
            Toast.makeText(getApplicationContext(), grup.toString(), Toast.LENGTH_SHORT);
            tvYaziyor.setVisibility(View.INVISIBLE);
            tvYaziyor.setText("");

        } else if (grup.equals("8")) {
            /// karsi taraf mesajlari okudugunda alinan notify ile adapteri yeniliyoruz

            //fetchChatThread();
        }


    }

    /**
     * Posting a new message in chat room
     * will make an http call to our server. Our server again sends the message
     * to all the devices as push notification
     */
    private void sendMessage(final int resimgeldi, final String encodedString) {

        //  Toast.makeText(getApplicationContext(),isRoomId(),Toast.LENGTH_LONG).show();
        final String message;
        //Okundu();

        message = this.inputMessage.getText().toString().trim();
        Yaziyor(7);


        if (resimgeldi == 0) {

            if (TextUtils.isEmpty(message)) {
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.writesomething), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String endPoint;
        if (grup.equals("0")) {
            endPoint = Config.SERVER_IS + "odayaMesaj.php";
        } else {
            endPoint = Config.SERVER_IS + "grubaMesaj.php";

        }
        Log.e(TAG, "endpoint: " + endPoint);

        this.inputMessage.setText("");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONObject commentObj = obj.getJSONObject("message");
                        Log.e(TAG, "json parsing error: " + response);

                        String commentId = commentObj.getString("message_id");
                        String commentText = commentObj.getString("message");
                        String createdAt = commentObj.getString("created_at");
                        String gelenresim = commentObj.getString("imageUrl");
                        // String gelenses = commentObj.getString("sesUrl");

                        String foto = commentObj.getString("foto");
                        String okundu = commentObj.getString("okundu");
                        JSONObject userObj = obj.getJSONObject("user");
                        String userId = userObj.getString("user_id");
                        String userName = userObj.getString("name");
                        String userEsposta = userObj.getString("email");

                        User user = new User(userId, userName, userEsposta);

                        Message message = new Message();
                        message.setId(commentId);
                        message.setMessage(commentText);
                        message.setCreatedAt(createdAt);
                        message.setImage(gelenresim);
                        //message.setSes(gelenses);
                        message.setOkundu(okundu);
                        // Toast.makeText(getApplicationContext(), "Send Mesajdaki:"+okundu, Toast.LENGTH_SHORT).show();
                        message.setFoto(foto);
                        message.setUser(user);

                        messageArrayList.add(message);
                        //Toast.makeText(getApplicationContext(), user_id, Toast.LENGTH_LONG).show();
                    }
                    recyclerView.stopScroll();

                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            // scrolling to bottom of the recycler view
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                     else {
                      //  Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                   // Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                if (message.equals("") || message.equals("null")) {
                    params.put("message", "");
                } else {
                    params.put("message", message);
                }


                params.put("room_id", chatRoomId);
                params.put("sahibi", sahibi);
                params.put("grup", grup);
                params.put("nedir", String.valueOf(resimgeldi));

                params.put("to_user_id", to_user_id);
                if (encodedString == "") {
                    params.put("image", "");
                } else {
                    params.put("image", encodedString);


                }

                Log.e(TAG, "Params: " + params.toString());

                return params;
            }


            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };


        // disabling retry policy so that it won't make
        // multiple http calls
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);

        //Adding request to request queue
        // MyApplication.getInstance().addToRequestQueue(strReq);
        AppController.getInstance().addToRequestQueue(strReq);

    }


    /**
     * Fetching all the messages of a single chat room
     */
    private void fetchChatThread() {


        Okundu();

        String endPoint = Config.SERVER_IS + "ozelmesajlar.php?room_id=" + chatRoomId;
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONArray commentsObj = obj.getJSONArray("messages");

                        for (int i = 0; i < commentsObj.length(); i++) {
                            JSONObject commentObj = (JSONObject) commentsObj.get(i);

                            String commentId = commentObj.getString("message_id");
                            String commentText = commentObj.getString("message");
                            String gelenresim = commentObj.getString("image");
                            //String gelenses = commentObj.getString("sesUrl");

                            String okundu = commentObj.getString("okundu");

                            String createdAt = commentObj.getString("created_at");

                            JSONObject userObj = commentObj.getJSONObject("user");
                            String userId = userObj.getString("user_id");
                            String userName = userObj.getString("username");
                            String foto = userObj.getString("foto");

                            String email = userObj.getString("email");

                            User user = new User(userId, userName, email);

                            Message message = new Message();
                            message.setId(commentId);
                            message.setMessage(commentText);
                            message.setCreatedAt(createdAt);
                            message.setUser(user);
                            message.setOkundu(okundu);
                            //Toast.makeText(getApplicationContext(), "Fetchdeki Mesajdaki:"+okundu, Toast.LENGTH_SHORT).show();

                            message.setFoto(foto);
                            message.setImage(gelenresim);
                           // message.setSes(gelenses);

                            messageArrayList.add(message);
                        }
                        recyclerView.stopScroll();

                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    } else {
                       // Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

        //Adding request to request queue
//        MyApplication.getInstance().addToRequestQueue(strReq);
        AppController.getInstance().addToRequestQueue(strReq);

    }

    public void getProfile(String eposta) {


        String url = Config.SERVER_IS + "sohbetprofil.php?eposta=" + eposta + "&istek=1";

        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response2: " + response);
                showJSON2(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    public void showJSON2(String response) {
        String toplamoy = "";

        String foto = "";
        String sqladi = "";
        String sqlsoyadi = "";
        String sqlcinsiyet = "";

        try {
            //JSONObject jsonObject = new JSONObject(response);
            JSONArray result = new JSONArray(response);
            //JSONArray result = jsonObject.getJSONArray(response);
            JSONObject collegeData = result.getJSONObject(0);


            // toplamoy = collegeData.getString("toplamoy");

            foto = collegeData.getString("foto");
            sqleposta = collegeData.getString("eposta");

            sqladi = collegeData.getString("adi");
            isim.setText(sqladi);
            gcm_registration_id = collegeData.getString("gcm_registration_id");
            kayit_no = collegeData.getString("kayit_no");

            sqlsoyadi = collegeData.getString("soyadi");
            sqlcinsiyet = collegeData.getString("cinsiyet");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  tvAdi.setText(sqladi+"   "+sqlsoyadi);
        // tvCinsiyet.setText(sqlcinsiyet);


        //btnToplamOy.setText("Kullanılan Oy  :"+toplamoy);
        if (foto.equals("null")) {


            if (sqlcinsiyet.equals("Bay")) {
                fotogoster.setImageUrl(Config.SERVER_IS + "varsayilan/male.png", imageLoader);
            } else {

                fotogoster.setImageUrl(Config.SERVER_IS + "varsayilan/female.png", imageLoader);
            }

        } else {
            fotogoster.setImageUrl(Config.SERVER_IS + "" + foto, imageLoader);
        }

    }

//
//    private void ArkadasSil(final String eposta,final String sqleposta,final String chatRoomId) {
//
//        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);
//        String url;
//        if (grup.equals("0")) {
//           url = Config.SERVER_IS + "arkadasSil.php";
//        }else {
//             url = Config.SERVER_IS + "odaSil.php";
//
//        }
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("Web ", response);
//                        if(response.contains("success_login")){
//                            Toast.makeText(ChatRoomActivity.this.getApplicationContext(), "İşlem Tamamlandı", Toast.LENGTH_SHORT).show();
//
//                        }
//                        else{
//                            Toast.makeText(ChatRoomActivity.this.getApplicationContext(), "Birşeyler Yanlış Gitti", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ChatRoomActivity.this.getApplicationContext(), "Error while reading data", Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("txteposta", (String.valueOf(eposta)) );
//                params.put("txtsahibi", (String.valueOf(sahibi)) );
//
//                params.put("txtsqleposta", (String.valueOf(sqleposta)) );
//                params.put("txtroomid", (String.valueOf(chatRoomId)) );
//                Log.e("oda", params.toString());
//                return params;
//            }
//        };
//
//        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(stringRequest);
//    }


    @Override
    public void onBackPressed() {
        releaseBitmap();
        super.onBackPressed();
    }

    public void onImageViewClicked(View view) {
        releaseBitmap();
        finish();
    }

    private void releaseBitmap() {
        if (mImage != null) {
            mImage.recycle();
            mImage = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void Yaziyor(final int grupx) {


        String endPoint;

        endPoint = Config.SERVER_IS + "yaziyor.php";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

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

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("room_id", chatRoomId);
                params.put("gcm_registration_id", gcm_registration_id);
                params.put("grup", String.valueOf(grupx));


                Log.e(TAG, "Params: " + params.toString());

                return params;
            }


            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };


        // disabling retry policy so that it won't make
        // multiple http calls
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);

        //Adding request to request queue
        // MyApplication.getInstance().addToRequestQueue(strReq);
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void Okundu() {


        String endPoint;

        endPoint = Config.SERVER_IS + "okundu.php";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

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

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kayit_no", kayit_no);

                params.put("room_id", chatRoomId);
                params.put("gcm_registration_id", gcm_registration_id);


                Log.e(TAG, "Params: " + params.toString());

                return params;
            }


            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };


        // disabling retry policy so that it won't make
        // multiple http calls
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);

        //Adding request to request queue
        // MyApplication.getInstance().addToRequestQueue(strReq);
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void startrecord() {

        // TODO Auto-generated method stub

        startTime = SystemClock.uptimeMillis();

        timer = new Timer();

        MyTimerTask myTimerTask = new MyTimerTask();

       timer.schedule(myTimerTask,1000,1000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},RECORD_AUDIO);

        } else {
            start();
        }
    }
    private void Gonder()
    {
//        Toast.makeText(getApplicationContext(),outputFile.toString(),
 //               Toast.LENGTH_SHORT).show();

             try {
                 myRecorder.stop();
                 myRecorder.release();
                 myRecorder  = null;
                 //sendMessage(1,outputFile);

              //   Toast.makeText(getApplicationContext(), "Stop recording...",
                //         Toast.LENGTH_SHORT).show();
             } catch (IllegalStateException e) {
                 //  it is called before start()
                 e.printStackTrace();
             } catch (RuntimeException e) {
                 // no valid audio/video data has been received
                 e.printStackTrace();
             }
        if((outputFile!="") || (outputFile=="null")) {
           // sendMessage(1,outputFile);
            new UploadFileToServer().execute();
            //File file = new File(outputFile);
          //  if (file.exists()) file.delete();

        } else {
            playNotificationSoundIptal();
        }



    }
    public void start(){

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
         dosyaismi ="Whatsapp"+n+".3gpp";
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/Whatsapp"+n+".3gpp";

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setOutputFile(outputFile);
        try {
            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }



        //Toast.makeText(getApplicationContext(), "Start recording...",
          //      Toast.LENGTH_SHORT).show();
    }
    public void stop(){
        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;
            //sendMessage(1,outputFile);
            File file = new File(outputFile);
            if (file.exists()) file.delete();
            outputFile="";
            //Toast.makeText(getApplicationContext(), "Stop recording...",
              //      Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }
    }







    private void stoprecord() {

        // TODO Auto-generated method stub

        if (timer != null) {

            timer.cancel();

        }

        if (recordTimeText.getText().toString().equals("00:00")) {

            return;

        }
     stop();
        recordTimeText.setText("00:00");
        recordPanel.setVisibility(View.INVISIBLE);

        vibrate();

    }



    private void vibrate() {

        // TODO Auto-generated method stub

        try {

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            v.vibrate(200);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    public static int dp(float value) {

        return (int) Math.ceil(1 * value);

    }
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.main_chat_room, menu);
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
                    alertDialogHelper.showAlertDialog("", getApplicationContext().getResources().getString(R.string.doyouwanttodelete), getApplicationContext().getResources().getString(R.string.deletemessage), getApplicationContext().getResources().getString(R.string.cancelmessage), 1, false);
                    return true;
                case R.id.action_forward:
                    alertDialogHelper.showAlertDialog("", getApplicationContext().getResources().getString(R.string.forwardmessage), getApplicationContext().getResources().getString(R.string.forwardmessageok), getApplicationContext().getResources().getString(R.string.cancelmessage), 2, false);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<Message>();
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
                    messageArrayList.remove(multiselect_list.get(i));
                    grup_list[i] = multiselect_list.get(i).getId();
                }
                sendDataGrup(grup_list,"sil");
                mAdapter.notifyDataSetChanged();

                if (mActionMode != null) {
                    mActionMode.finish();
                }
                //Toast.makeText(getApplicationContext(), "Silmeye Tıkladınız", Toast.LENGTH_SHORT).show();
            }

        } else if (from == 2) {

            if (multiselect_list.size() ==1 ) {

                if(hareket.equals("guncelle"))
                { sendDataGrup(grup_list,"guncelle");}else{
                    sendDataGrup(grup_list,"ekle");}

                mAdapter.notifyDataSetChanged();
                finish();

            }
            if (mActionMode != null) {
                mActionMode.finish();
            }

            // user_list.add(mSample);

        } else {
            alertDialogHelper.showAlertDialog("", "Sadece bir Mesaj Seçniz", "Tamam ", "", 0, false);



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
            url  = Config.SERVER_IS + "messageElemanSil.php";

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


                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.messagesdeleted), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.messagesnotdeleted), Toast.LENGTH_SHORT).show();
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
                    params.put("mesaj["+i+"]", grup_listesi[i]);
                    Log.e("mesaj["+i+"]", grup_listesi[i]);

                }
                params.put("toplam", String.valueOf(grup_listesi.length));
                params.put("user_id", user_id);
                params.put("chat_room_id", chatRoomId);
                params.put("grup", grup);

                Log.e("secilenmesajlar", params.toString());

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    class MyTimerTask extends TimerTask {


        @Override

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            final String hms = String.format("%02d:%02d",

                    TimeUnit.MILLISECONDS.toMinutes(updatedTime)

                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS

                            .toHours(updatedTime)),

                    TimeUnit.MILLISECONDS.toSeconds(updatedTime)

                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS

                            .toMinutes(updatedTime)));

            long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)

                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS

                    .toMinutes(updatedTime));

            System.out.println(lastsec + " hms " + hms);

            runOnUiThread(new Runnable() {


                @Override

                public void run() {

                    try {

                        if (recordTimeText != null)

                            recordTimeText.setText(hms);

                    } catch (Exception e) {

                        // TODO: handle exception

                    }


                }

            });

        }

    }
        private class UploadFileToServer extends AsyncTask<Void, Integer, String> {


            @Override
            protected void onPreExecute() {
                // setting progress bar to zero
                progressBar.setProgress(0);
                super.onPreExecute();
            }

            @Override
            protected void onProgressUpdate(Integer... progress) {
                // Making progress bar visible
                progressBar.setVisibility(View.VISIBLE);
                txtPercentage.setVisibility(View.VISIBLE);

                // updating progress bar value
                progressBar.setProgress(progress[0]);

                // updating percentage value
                 txtPercentage.setText(String.valueOf(progress[0]) + "%");
            }

            @Override
            protected String doInBackground(Void... params) {
                return uploadFile();
            }

            @SuppressWarnings("deprecation")
            private String uploadFile() {
                String responseString = null;
                String room_id = chatRoomId;
                String image = "";

                String nedir = "2";


                String endPoint;

                if (grup.equals("0")) {
                    endPoint = Config.SERVER_IS + "odayaMesaj.php";
                } else {
                    endPoint = Config.SERVER_IS + "grubaMesaj.php";

                }

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(endPoint);

                try {
                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                            new AndroidMultiPartEntity.ProgressListener() {

                                @Override
                                public void transferred(long num) {
                                    publishProgress((int) ((num / (float) totalSize) * 100));
                                }
                            });

                    File sourceFile = new File(outputFile);
                    // Adding file data to http body
                    entity.addPart("ses", new FileBody(sourceFile));
                    entity.addPart("room_id",new StringBody(chatRoomId));
                    entity.addPart("image",new StringBody(image));

                    // Extra parameters if you want to pass to server
                    entity.addPart("room_id",new StringBody(chatRoomId));
                    entity.addPart("user_id", new StringBody(user_id));
                    entity.addPart("message", new StringBody(inputMessage.getText().toString()));
                    entity.addPart("to_user_id", new StringBody(to_user_id));

                    entity.addPart("sahibi", new StringBody(sahibi));
                    entity.addPart("grup", new StringBody(grup));
                    entity.addPart("nedir", new StringBody(nedir));


                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);

                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        File file = new File(outputFile);
                          if (file.exists()) file.delete();

                        responseString = EntityUtils.toString(r_entity);
                       // Toast.makeText(getApplicationContext(),outputFile.toString(),Toast.LENGTH_LONG).show();
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }

                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }

                return responseString;

            }

            @Override
            protected void onPostExecute(String result) {
                Log.e(TAG, "Response from server: " + result);

                // showing the server response in an alert dialog
              //  showAlert(result);
                try {
                    JSONObject obj = new JSONObject(result);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONObject commentObj = obj.getJSONObject("message");
                        Log.e(TAG, "json parsing error: " + result);

                        String commentId = commentObj.getString("message_id");
                        String commentText = commentObj.getString("message");
                        String createdAt = commentObj.getString("created_at");
                        String gelenresim = commentObj.getString("imageUrl");
                        // String gelenses = commentObj.getString("sesUrl");

                        String foto = commentObj.getString("foto");
                        String okundu = commentObj.getString("okundu");
                        JSONObject userObj = obj.getJSONObject("user");
                        String userId = userObj.getString("user_id");
                        String userName = userObj.getString("name");
                        String userEsposta = userObj.getString("email");

                        User user = new User(userId, userName, userEsposta);

                        Message message = new Message();
                        message.setId(commentId);
                        message.setMessage(commentText);
                        message.setCreatedAt(createdAt);
                        message.setImage(gelenresim);
                        //message.setSes(gelenses);
                        message.setOkundu(okundu);
                        // Toast.makeText(getApplicationContext(), "Send Mesajdaki:"+okundu, Toast.LENGTH_SHORT).show();
                        message.setFoto(foto);
                        message.setUser(user);

                        messageArrayList.add(message);
                        //Toast.makeText(getApplicationContext(), user_id, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        txtPercentage.setVisibility(View.INVISIBLE);
                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            // scrolling to bottom of the recycler view
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    } else {
                       // Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                super.onPostExecute(result);
            }

        }

        /**
         * Method to show alert dialog
         */
        private void showAlert(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }



}