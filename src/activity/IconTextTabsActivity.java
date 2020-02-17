package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.readystatesoftware.viewbadger.BadgeView;
import com.yavuz.rencber.rencber.GCM.NotificationUtils;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.fragments.BlankFragment;
import com.yavuz.rencber.rencber.fragments.FourFragment;
import com.yavuz.rencber.rencber.fragments.OneFragment;
import com.yavuz.rencber.rencber.fragments.OyverFragment;
import com.yavuz.rencber.rencber.fragments.ThreeFragment;
import com.yavuz.rencber.rencber.fragments.TwoFragment;

import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;


public class IconTextTabsActivity extends AppCompatActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = IconTextTabsActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    public static BadgeView badgesohbet,badgebildirimler;
    public static String RoomId;
    private Toolbar toolbar;
    public String GelenPosta;
    public static TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_tab_favourite,
            R.drawable.friends,
            R.drawable.statistics,

            R.drawable.profile,
            R.drawable.ask
    };
    TextView tab1,tab2,tab3,tab4,tab5;
    int bildirimler;
    int chatRoomMesajCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_text_tabs);
        Bundle extras = getIntent().getExtras();
        String GelenPosta = extras.getString("epostam");
         RoomId=ChatRoomActivity.isRoomId();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // Toast.makeText(this,GelenPosta,Toast.LENGTH_SHORT).show();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setTag(GelenPosta);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //tabThree.setText("THREE");

        ShortcutBadger.removeCount(this); //for 1.1.4+


        setupTabIcons();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        bildirimler=preferences.getInt("bildirimler",0);
         chatRoomMesajCount = preferences.getInt("mesajsayisi", 0);

        badgesohbet = new BadgeView(IconTextTabsActivity.this,tab2 );
                badgebildirimler = new BadgeView(IconTextTabsActivity.this,tab5 );

        if(bildirimler==0) { badgebildirimler.hide(); }else{
            badgebildirimler.setText(String.valueOf(bildirimler));
            badgebildirimler.show(); }

        if(chatRoomMesajCount==0) {badgesohbet.hide();}else{
            badgesohbet.setText(String.valueOf(chatRoomMesajCount));
            badgesohbet.show();}
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);

                }
            }
        };

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
    private void handlePushNotification(Intent intent) {
        String grup = intent.getStringExtra("grup");
        if (grup.equals("0")) {

            badgesohbet.setText(String.valueOf(chatRoomMesajCount + 1));
            badgesohbet.show();

        }else {


            badgebildirimler.setText(String.valueOf(bildirimler + 1));
            badgebildirimler.show();
        }

    }
    private void setupTabIcons() {

        tab1 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
       // tab1.setText(R.string.oyver);
        tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_favourite, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tab1);

        tab2 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        //tab2.setText(R.string.friends);
        tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sohbet, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tab2);



        tab3 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        //tab3.setText(R.string.friends);
        tab3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.istatistik, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tab3);

        tab4 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
       // tab4.setText(R.string.sonuc);
        tab4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_contacts, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tab4);

        tab5 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ///tab5.setText("TWO");
        tab5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.duyuru, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tab5);



//        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//
//        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
//        tabLayout.getTabAt(4).setIcon(tabIcons[4]);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OyverFragment(), this.getString(R.string.oyver));
        adapter.addFrag(new BlankFragment(), this.getString(R.string.friends));
        adapter.addFrag(new ThreeFragment(), this.getString(R.string.sonuc));
        adapter.addFrag(new FourFragment(), this.getString(R.string.profile));
        adapter.addFrag(new TwoFragment(), this.getString(R.string.getopion));


        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public void onBackPressed() {
//
        super.onBackPressed();
    }




}
