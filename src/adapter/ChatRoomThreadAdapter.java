package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;

import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;

import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yavuz.rencber.rencber.CircleImageView;
import com.yavuz.rencber.rencber.GCM.Message;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.FullscreenActivity;
import com.yavuz.rencber.rencber.activity.KullaniciProfili;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;



public class ChatRoomThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static String TAG = ChatRoomThreadAdapter.class.getSimpleName();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private View.OnClickListener clickListener;
    private String userId;
    private int SELF = 100;
    private int OTHER = 200;
    View itemView=null;

    private static String today;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;

    public static int oneTimeOnly = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private Context mContext;
    public ArrayList<Message> messageArrayList;
    public ArrayList<Message> selected_usersList;

    public class ViewHolder extends RecyclerView.ViewHolder  {
         TextView message, timestamp,startTimeField,endTimeField;
        NetworkImageView networkresim;
        CircleImageView ivOdafoto;
        ImageView ivGoruldu,playButton,pauseButton;

         SeekBar seekbar;
View muzikKutusu;

        MediaPlayer mediaPlayer;
        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            networkresim =(NetworkImageView)itemView.findViewById(R.id.buyukresimlimesaj);
            ivOdafoto =(CircleImageView)itemView.findViewById(R.id.ivOdafoto);
            ivGoruldu =(ImageView) itemView.findViewById(R.id.ivGoruldu);
            seekbar = (SeekBar)itemView.findViewById(R.id.seekBar1);
            playButton = (ImageView) itemView.findViewById(R.id.ivPlayButton);
            pauseButton = (ImageView)itemView.findViewById(R.id.ivPauseButton);
            startTimeField =(TextView)itemView.findViewById(R.id.textView1);
            endTimeField =(TextView)itemView.findViewById(R.id.textView2);
            muzikKutusu=itemView.findViewById(R.id.muzikkutusu);
            seekbar.setClickable(false);
            pauseButton.setEnabled(false);
           mediaPlayer = new MediaPlayer();
        }


    }


    public ChatRoomThreadAdapter(Context mContext, ArrayList<Message> messageArrayList, String userId,ArrayList<Message> selectedList) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
        this.userId = userId;
        this.selected_usersList = selectedList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else if(viewType == OTHER) {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
        }


        return new ViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (message.getUser().getId().equals(userId)) {
            return SELF;
        } else
        {

            return OTHER;
        }}

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        final Message message = messageArrayList.get(position);
        if (message.getImage().length() > 0) {
            String new_word = message.getImage().substring(message.getImage().length() - 4);

            if (new_word.equals("3gpp")) {
                //mediaPlayer = MediaPlayer.create(mContext, Config.SERVER_IS  + message.getImage(), imageLoader);
                // Toast.makeText(mContext,"ses gelecek",Toast.LENGTH_LONG).show();

                ((ViewHolder) holder).networkresim.setVisibility(View.INVISIBLE);
                ((ViewHolder) holder).message.setVisibility(View.GONE);
                ((ViewHolder) holder).playButton.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).pauseButton.setVisibility(View.INVISIBLE);
                ((ViewHolder) holder).message.setVisibility(View.INVISIBLE);
                ((ViewHolder) holder).seekbar.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).endTimeField.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).startTimeField.setVisibility(View.VISIBLE);
                 ((ViewHolder) holder).muzikKutusu.setVisibility(View.VISIBLE);
            } else if (new_word.equals("jpeg")) {
                // Toast.makeText(mContext, "resim gelecek", Toast.LENGTH_LONG).show();
                ((ViewHolder) holder).networkresim.setImageUrl(Config.SERVER_IS + message.getImage(), imageLoader);
                ((ViewHolder) holder).networkresim.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).message.setVisibility(View.INVISIBLE);
                ((ViewHolder) holder).playButton.setVisibility(View.INVISIBLE);
                ((ViewHolder) holder).pauseButton.setVisibility(View.INVISIBLE);
                ((ViewHolder) holder).seekbar.setVisibility(View.INVISIBLE);
                ((ViewHolder) holder).endTimeField.setVisibility(View.INVISIBLE);
                ((ViewHolder) holder).startTimeField.setVisibility(View.INVISIBLE);
                ((ViewHolder) holder).muzikKutusu.setVisibility(View.INVISIBLE);

            }
        } else if (message.getImage().equals("")) {
            ((ViewHolder) holder).message.setText(message.getMessage());

            Linkify.addLinks(((ViewHolder) holder).message, Linkify.ALL); //link varmı tel var mi e mail varmi

           // Toast.makeText(mContext, "mesaj gelecek", Toast.LENGTH_LONG).show();
            ((ViewHolder) holder).message.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).networkresim.setVisibility(View.INVISIBLE);
            ((ViewHolder) holder).playButton.setVisibility(View.INVISIBLE);
            ((ViewHolder) holder).pauseButton.setVisibility(View.INVISIBLE);
            ((ViewHolder) holder).seekbar.setVisibility(View.INVISIBLE);
            ((ViewHolder) holder).endTimeField.setVisibility(View.INVISIBLE);
            ((ViewHolder) holder).startTimeField.setVisibility(View.INVISIBLE);
            ((ViewHolder) holder).muzikKutusu.setVisibility(View.INVISIBLE);

        }


        String timestamp = getTimeStamp(message.getCreatedAt());


        // if (message.getUser().getName() != null)
        //timestamp = message.getUser().getName() + ", " + timestamp;
        ((ViewHolder) holder).timestamp.setText(timestamp);
        // Toast.makeText(mContext, "Threaddeki " + message.getOkundu(), Toast.LENGTH_SHORT).show();
        if (message.getOkundu().equals("1")) {
            ((ViewHolder) holder).ivGoruldu.setVisibility(View.VISIBLE);
        } else if (message.getOkundu().equals("null") || message.getOkundu().equals("") || message.getOkundu().equals("0")) {
            ((ViewHolder) holder).ivGoruldu.setVisibility(View.INVISIBLE);

        }


        ((ViewHolder) holder).ivOdafoto.setImageUrl(Config.SERVER_IS + message.getFoto(), imageLoader);
        ((ViewHolder) holder).ivOdafoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, KullaniciProfili.class);

                i.putExtra("to_user_id", message.getUser().getId());

                i.putExtra("to_eposta", message.getUser().getEmail());

                mContext.startActivity(i);
            }
        });

        ((ViewHolder) holder).networkresim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext, FullscreenActivity.class);
                intent.putExtra("resimyol", message.getImage());


                mContext.startActivity(intent);
                //showPopup(v,message.getImage());
            }
        });

        ((ViewHolder) holder).playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  ((ViewHolder) holder).mediaPlayer.reset();

               // ((ViewHolder) holder).mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {
                    ((ViewHolder) holder).mediaPlayer.reset();
                    ((ViewHolder) holder).mediaPlayer.setDataSource(Config.SERVER_IS + message.getImage());

                    ((ViewHolder) holder).mediaPlayer.prepare();
                    ((ViewHolder) holder).mediaPlayer.start();
                    // Displaying Song title
                    finalTime =   ((ViewHolder) holder).mediaPlayer.getDuration();
                    startTime =   ((ViewHolder) holder).mediaPlayer.getCurrentPosition();


                    //Muziğin toplamda ne kadar süre oldugunu  endTimeField controller ına yazdırıyoruz...
                    ((ViewHolder) holder).endTimeField.setText(String.format("%d dk, %d sn",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) finalTime)))
                    );
                    //Muziğin başladıgı andan itibaren gecen süreyi ,startTimeField controller ına yazdırıyoruz...
                    ((ViewHolder) holder).startTimeField.setText(String.format("%d dk, %d sn",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
                    );
                    if(oneTimeOnly == 0){
                        ((ViewHolder) holder).seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }

                    //Muziğin hangi sürede oldugunu gostermek icin, seekbar kullarak gosteriyoruz...
                    ((ViewHolder) holder).seekbar.setProgress((int) startTime);
                    myHandler.postDelayed(UpdateSongTime, 100);
                    // Changing Button Image to pause image
                    ((ViewHolder) holder).pauseButton.setVisibility(View.VISIBLE);
                    ((ViewHolder) holder).playButton.setVisibility(View.INVISIBLE);

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }

            private Runnable UpdateSongTime = new Runnable() {
                public void run() {
                    startTime =   ((ViewHolder) holder).mediaPlayer.getCurrentPosition();
                    ((ViewHolder) holder).startTimeField.setText(String.format("%d dk, %d sn",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
                    );
                    //Muziğin hangi sürede oldugunu gostermek icin, seekbar kullarak gosteriyoruz...
                    ((ViewHolder) holder).seekbar.setProgress((int)startTime);
                    myHandler.postDelayed(this, 100);
                }
            };

        });


        ((ViewHolder) holder).pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Muziğin duraklatıldıgına  dair Toast ile uyarı yazdırıyoruz..
                //Toast.makeText(mContext, "Müzik durduruldu",Toast.LENGTH_SHORT).show();
                // MediaPlayer durduyoruz
//                mediaPlayer.reset();


                ((ViewHolder) holder).mediaPlayer.pause();
                ((ViewHolder) holder).playButton.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).pauseButton.setVisibility(View.INVISIBLE);
                //ve butonlarımızı pasif ,aktif durumunu düzenliyoruz..


            }
        });

        ((ViewHolder) holder).mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                ((ViewHolder) holder).playButton.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).pauseButton.setVisibility(View.INVISIBLE);
            }


        });
        if(selected_usersList.contains(messageArrayList.get(position)))
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
          }



    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }


    //İleri butonuna bastgınızda,muzigin çalış süresini 5 saniye artırarak muzigi  ilerletir

    public void showPopup(View anchorView,final String resim) {
//        LayoutInflater.from(mContext)
  //              .inflate(R.layout.popup_layout_resim_goster, null);

        final View popupView = LayoutInflater.from(mContext)
                .inflate(R.layout.popup_layout_resim_goster, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        // Example: If you have a TextView inside `popup_layout.xml`
        ImageView close= (ImageView)popupView.findViewById(R.id.close2);
  NetworkImageView buyukresimlimesaj=(NetworkImageView)popupView.findViewById(R.id.buyukresimlimesaj);
        buyukresimlimesaj.setImageUrl(Config.SERVER_IS  + resim, imageLoader);

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }
        });

      /*  Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // close your dialog
                popupWindow.dismiss();
            }

        }, 10000);
*/
        //  tv.setText(....);

        // Initialize more widgets from `popup_layout.xml`


        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private OyverListAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final OyverListAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}

