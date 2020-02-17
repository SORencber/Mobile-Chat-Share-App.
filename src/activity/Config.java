package com.yavuz.rencber.rencber.activity;

////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////

public class Config {

    public static final String SERVER_CEVIRI="https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20160519T061006Z.a280502b511e3704.8f69e1d887f567b8a846937b152bb5c781d83463";
   // public static final String SERVER_IS="http://185.86.4.55/tiklapanpa3/";
    public static final String SERVER_IS="http://185.86.4.55/tiklapanpa/";


    public static final String RESIM_NO = "resim_no";
    public static final String RESIM_BASLIK = "resim_baslik";
    public static final String RESIM_YOL = "resim_adi";
    public static final String RESIM_ASAGI = "asagi";

    public static final String RESIM_YUKARI = "yukari";
    public static final String RESIM_NOTR = "notr";

    public static final String JSON_ARRAY = "result";




    //// Chat setting
    public static boolean appendNotificationMessages = true;

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // type of push messages
    public static final int PUSH_TYPE_CHATROOM = 1;
    public static final int PUSH_TYPE_COMMENT = 2;

    // id to handle the notification in the notification try
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
}
