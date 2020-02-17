package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.yavuz.rencber.rencber.GCM.Message;
import com.yavuz.rencber.rencber.GCM.NotificationUtils;
import com.yavuz.rencber.rencber.GCM.User;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.yavuz.rencber.rencber.GCM.Yorum;

import org.json.JSONException;
import org.json.JSONObject;

import me.leolin.shortcutbadger.ShortcutBadger;


public class GcmIntentService extends IntentService {
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private static final String TAG = GcmIntentService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    public GcmIntentService() {
        super("GcmIntentService");
    }
   public  String  from,flag,sahibi,grup,mesaj,messageType;
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        //Gelen mesaj tipini alıyoruz
         messageType = gcm.getMessageType(intent);
         mesaj = intent.getExtras().getString("data");
         from = intent.getExtras().getString("from");
         flag = intent.getExtras().getString("flag");
         sahibi = intent.getExtras().getString("sahibi");
         grup = intent.getExtras().getString("grup");

        //String title = intent.getExtras().getString("title");
        //String imageUrl = intent.getExtras().getString("imageUrl");
        String data = intent.getExtras().getString("data");
        Boolean isBackground = intent.getExtras().getBoolean("isBackground");
        String title="Kanka Mesaj:";
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "isBackground: " + isBackground);
        Log.d(TAG, "flag: " + flag);
        Log.d(TAG, "data: " + data);
        Log.d(TAG, "sahibi: " + sahibi);

        Log.d(TAG, "grup: " + grup);

        // Log.d(TAG, "imageUrl: " + imageUrl);

   /*     if (!extras.isEmpty()) {  // has effect of unparcelling Bundle

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {//Herhangi bir sorun yoksa Notification mızı oluşturacak methodu çağırıyoruz
                sendNotification(mesaj);
            }
        }
        */
        GcmBroadcastReceiver.completeWakefulIntent(intent);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        int bildirimler=preferences.getInt("bildirimler",0);
        int toplammesaj = preferences.getInt("mesajsayisi", 0);

        switch (Integer.parseInt(flag)) {
            case Config.PUSH_TYPE_CHATROOM:
                // Chat odasina mesaj gnderiyoruz
                if (grup.equals("0")) {

                    editor.putInt("mesajsayisi",  toplammesaj + 1);
                    // int bildirimler=preferences.getInt("bildirimler",0);
                    // editor.putInt("bildirimler", bildirimler + 1);
                    editor.commit();
                    ShortcutBadger.applyCount(getApplicationContext(), bildirimler+toplammesaj+1); //for 1.1.4+

                    processChatRoomPush(title, isBackground, data);
                }else if(grup.equals("1")) {


                    //editor.putInt("bildirimler", bildirimler + 1);

                    //editor.commit();

                    //ShortcutBadger.applyCount(getApplicationContext(), bildirimler+1);//for 1.1.4+


                    processChatRoomPush("Grup Mesajı", isBackground, data);


                }else if(grup.equals("3")) {

                    editor.putInt("bildirimler", bildirimler + 1);

                    editor.commit();

                    ShortcutBadger.applyCount(getApplicationContext(), bildirimler+1);//for 1.1.4+

                    processCommentPush("Resim  Mesajı", isBackground, data);


                }else if(grup.equals("4")) {
                    editor.putInt("bildirimler", bildirimler + 1);

                    editor.commit();

                    ShortcutBadger.applyCount(getApplicationContext(), bildirimler+1);//for 1.1.4+

                    processFriendAddPush("Arkadaslık İsteği", isBackground, data);

                }else if(grup.equals("5")) {
                    editor.putInt("bildirimler", bildirimler + 1);

                    editor.commit();

                    ShortcutBadger.applyCount(getApplicationContext(), bildirimler+1);//for 1.1.4+

                    processAcceptFriendPush("Arkadaslık Onay Mesajı", isBackground, data);

        } else if(grup.equals("6") || grup.equals("7")  || grup.equals("8")) {



                    processYaziyor("Yazıyor Mesajı", isBackground, data);

        }

                  break;
            case Config.PUSH_TYPE_COMMENT:

                // Yorum yazildiginda kullaniciyi uyarma
            processCommentPush(title, isBackground, data);
                break;
        }


    }


    private void processChatRoomPush(String title, boolean isBackground, String data) {

        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);


                JSONObject mObj = datObj.getJSONObject("message");
                String chatRoomId = mObj.getString("chat_room_id");

                Message message = new Message();
                message.setMessage(mObj.getString("message"));
                message.setId(mObj.getString("message_id"));
                message.setMessage(mObj.getString("message"));
                message.setImage(mObj.getString("imageUrl"));
                String imageUrl= mObj.getString("imageUrl");
                message.setFoto(mObj.getString("foto"));
                message.setOkundu("1");
                message.setCreatedAt(mObj.getString("created_at"));
                Log.d(TAG, "room is: " + chatRoomId);

                JSONObject uObj = datObj.getJSONObject("user");

                // skip the message if the message belongs to same user as
                // the user would be having the same message when he was sending
                // but it might differs in your scenario


                User user = new User();
                user.setId(uObj.getString("user_id"));
                String user_id=uObj.getString("user_id");
                user.setEmail(uObj.getString("email"));
                user.setName(uObj.getString("name"));
                message.setUser(user);
                Log.d(TAG, "room id 2 : " + chatRoomId);

                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
                editor = preferences.edit();
                int chatRoomMesajCount = preferences.getInt(chatRoomId, 0);
                editor.putInt(chatRoomId, chatRoomMesajCount + 1);
                //editor.putInt("mesajsayisi",chatRoomMesajCount + 1);
               // int bildirimler=preferences.getInt("bildirimler",0);
               // editor.putInt("bildirimler", bildirimler + 1);
                editor.commit();



                // verifying whether the app is in background or foreground
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext(),chatRoomId.toString())) {

                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("type", Config.PUSH_TYPE_CHATROOM);
                    pushNotification.putExtra("message", message);
                    pushNotification.putExtra("chat_room_id", chatRoomId);
                    pushNotification.putExtra("to_user_id",user_id);
                    pushNotification.putExtra("imageUrl",imageUrl);
                    pushNotification.putExtra("sahibi",sahibi);
                    pushNotification.putExtra("grup",grup);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils();

                    notificationUtils.playNotificationSound2();
                } else {

                    // app is in background. show the message in notification try
                    Intent resultIntent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                    resultIntent.putExtra("chat_room_id", chatRoomId);
                    resultIntent.putExtra("to_user_id", user_id);
                    resultIntent.putExtra("imageUrl", imageUrl);
                    resultIntent.putExtra("sahibi", sahibi);
                    resultIntent.putExtra("grup", grup);


                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), title, user.getName() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
                    } else {
                        // push notification contains image
                        // show it with the image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message.getMessage(), message.getCreatedAt(), resultIntent, imageUrl);
                    }
                    }

            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }

    private void processCommentPush(String title, boolean isBackground, String data) {


        //IconTextTabsActivity.badge.setText(String.valueOf(sonbildirimler));
        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);


                JSONObject mObj = datObj.getJSONObject("message");

                Yorum message = new Yorum();
                message.setMessage(mObj.getString("message"));

                String resimno=mObj.getString("resimno");
                message.setResimno(mObj.getString("resimno"));
                String resimyol=mObj.getString("resimyol");
                message.setResimyol(mObj.getString("resimno"));

                JSONObject uObj = datObj.getJSONObject("user");

                // skip the message if the message belongs to same user as
                // the user would be having the same message when he was sending
                // but it might differs in your scenario


                User user = new User();
                user.setId(uObj.getString("user_id"));
                String user_id=uObj.getString("user_id");
                user.setEmail(uObj.getString("email"));
                user.setFoto(uObj.getString("foto"));

                user.setName(uObj.getString("name"));
                message.setUser(user);

                // verifying whether the app is in background or foreground
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("type", Config.PUSH_TYPE_COMMENT);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("to_user_id",user_id);
                pushNotification.putExtra("resimno",resimno);
                pushNotification.putExtra("resimyol",resimyol);
                pushNotification.putExtra("resimbaslik","resimbaslik");
                pushNotification.putExtra("gonderenfoto",user.getFoto());
                pushNotification.putExtra("gonderenisim",user.getName());
                pushNotification.putExtra("gonderenfoto",user.getFoto());

                pushNotification.putExtra("gondereneposta",user.getEmail());

                pushNotification.putExtra("grup",grup);
                pushNotification.putExtra("silmek","0");

                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
               // NotificationUtils notificationUtils = new NotificationUtils();
               // notificationUtils.playNotificationSound();

                // app is in background. show the message in notification try
                Intent resultIntent = new Intent(getApplicationContext(),ShowPopUp.class);
                resultIntent.putExtra("to_user_id", user_id);
                resultIntent.putExtra("resimno", resimno);
                resultIntent.putExtra("resimyol", resimyol);
                resultIntent.putExtra("resimbaslik", "resimbaslik");
                resultIntent.putExtra("gonderenfoto",user.getFoto());

                resultIntent.putExtra("sahibi", sahibi);
                resultIntent.putExtra("grup", grup);

                resultIntent.putExtra("silmek", "0");

                showNotificationMessage(getApplicationContext(), title, user.getName() + " : " + message.getMessage(), "", resultIntent);




            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }
    private void processFriendAddPush(String title, boolean isBackground, String data) {



        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);


                JSONObject mObj = datObj.getJSONObject("message");

                Yorum message = new Yorum();
                message.setMessage(mObj.getString("message"));

                String resimno=mObj.getString("resimno");
                message.setResimno(mObj.getString("resimno"));
                String resimyol=mObj.getString("resimyol");
                message.setResimyol(mObj.getString("resimno"));

                JSONObject uObj = datObj.getJSONObject("user");

                // skip the message if the message belongs to same user as
                // the user would be having the same message when he was sending
                // but it might differs in your scenario


                User user = new User();
                user.setId(uObj.getString("user_id"));
                String user_id=uObj.getString("user_id");
                user.setEmail(uObj.getString("email"));
                String gondereneposta=uObj.getString("email");
                user.setFoto(uObj.getString("foto"));

                user.setName(uObj.getString("name"));
                message.setUser(user);

                // verifying whether the app is in background or foreground
                 // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("type", Config.PUSH_TYPE_COMMENT);
                    pushNotification.putExtra("message", message);
                    pushNotification.putExtra("to_user_id",user_id);
                    pushNotification.putExtra("resimno",resimno);
                    pushNotification.putExtra("resimyol",resimyol);
                    pushNotification.putExtra("resimbaslik","resimbaslik");
                    pushNotification.putExtra("gonderenfoto",user.getFoto());
                    pushNotification.putExtra("gonderenisim",user.getName());
                    pushNotification.putExtra("gonderenfoto",user.getFoto());
                    pushNotification.putExtra("gondereneposta",gondereneposta);


                    pushNotification.putExtra("grup",grup);
                    pushNotification.putExtra("silmek","0");

                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    //NotificationUtils notificationUtils = new NotificationUtils();
                   // notificationUtils.playNotificationSound();

                    // app is in background. show the message in notification try
                    Intent resultIntent = new Intent(getApplicationContext(),IconTextTabsActivity.class);
                    resultIntent.putExtra("to_user_id", user_id);
                    resultIntent.putExtra("resimno", resimno);
                    resultIntent.putExtra("resimyol", resimyol);
                    resultIntent.putExtra("resimbaslik", "resimbaslik");
                    resultIntent.putExtra("gonderenfoto",user.getFoto());

                    resultIntent.putExtra("sahibi", sahibi);
                    resultIntent.putExtra("grup", grup);

                    resultIntent.putExtra("silmek", "0");

                    showNotificationMessage(getApplicationContext(), title, user.getName() + " : " + message.getMessage(), "", resultIntent);




            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }

    private void processAcceptFriendPush(String title, boolean isBackground, String data) {


        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);


                JSONObject mObj = datObj.getJSONObject("message");

                Yorum message = new Yorum();
                message.setMessage(mObj.getString("message"));

                String resimno=mObj.getString("resimno");
                message.setResimno(mObj.getString("resimno"));
                String resimyol=mObj.getString("resimyol");
                message.setResimyol(mObj.getString("resimno"));

                JSONObject uObj = datObj.getJSONObject("user");

                // skip the message if the message belongs to same user as
                // the user would be having the same message when he was sending
                // but it might differs in your scenario


                User user = new User();
                user.setId(uObj.getString("user_id"));
                String user_id=uObj.getString("user_id");
                user.setEmail(uObj.getString("email"));
                String gondereneposta=uObj.getString("email");
                user.setFoto(uObj.getString("foto"));

                user.setName(uObj.getString("name"));
                message.setUser(user);

                // verifying whether the app is in background or foreground
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("type", Config.PUSH_TYPE_COMMENT);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("to_user_id",user_id);
                pushNotification.putExtra("resimno",resimno);
                pushNotification.putExtra("resimyol",resimyol);
                pushNotification.putExtra("resimbaslik","resimbaslik");
                pushNotification.putExtra("gonderenfoto",user.getFoto());
                pushNotification.putExtra("gonderenisim",user.getName());
                pushNotification.putExtra("gonderenfoto",user.getFoto());
                pushNotification.putExtra("gondereneposta",gondereneposta);


                pushNotification.putExtra("grup",grup);
                pushNotification.putExtra("silmek","0");

                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
               // NotificationUtils notificationUtils = new NotificationUtils();
              //  notificationUtils.playNotificationSound();

                // app is in background. show the message in notification try
                Intent resultIntent = new Intent(getApplicationContext(),IconTextTabsActivity.class);
                resultIntent.putExtra("to_user_id", user_id);
                resultIntent.putExtra("resimno", resimno);
                resultIntent.putExtra("resimyol", resimyol);
                resultIntent.putExtra("resimbaslik", "resimbaslik");
                resultIntent.putExtra("gonderenfoto",user.getFoto());

                resultIntent.putExtra("sahibi", sahibi);
                resultIntent.putExtra("grup", grup);

                resultIntent.putExtra("silmek", "0");

                showNotificationMessage(getApplicationContext(), title, user.getName() + " : " + message.getMessage(), "", resultIntent);




            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }

    private void processYaziyor(String title, boolean isBackground, String data) {


        try {
            JSONObject datObj = new JSONObject(data);


            JSONObject mObj = datObj.getJSONObject("message");
            String chatRoomId = mObj.getString("chat_room_id");



            Log.d(TAG, "room is: " + chatRoomId);





            // verifying whether the app is in background or foreground
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext(),chatRoomId.toString())) {

                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("type", Config.PUSH_TYPE_CHATROOM);
                pushNotification.putExtra("chat_room_id", chatRoomId);
                pushNotification.putExtra("grup",grup);

                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound


            }

        } catch (JSONException e) {
            Log.e(TAG, "json parsing error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


}

    /**
     * Showing notification with text only
     * */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     * */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}