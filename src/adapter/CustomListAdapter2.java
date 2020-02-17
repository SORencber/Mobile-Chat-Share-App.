package com.yavuz.rencber.rencber.adapter;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.activity.AppController;
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.KullaniciProfili;
import com.yavuz.rencber.rencber.activity.Movie;
import com.yavuz.rencber.rencber.activity.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomListAdapter2 extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    private List<Movie> movieItems2;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta,cinsiyet,gelenresimno,resimyol,resimbaslik;
    Movie m;
    Movie m2;
    TextView mesaj;
    ImageView ivReply,ivYorumCevir,ivyorumsil,ivyorumasagi,ivyorumyukari,ivbenimyorumAsagi,ivbenimyorumYukari,ivbaskayorumsil;
    Button btnFollow;
    private static final String TAG = CustomListAdapter2.class.getSimpleName();

    public CustomListAdapter2(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;



    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position)
    { return position;}

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());//preferences objesi
        editor = preferences.edit();
        // Inflate the layout for this fragment

            eposta = preferences.getString("email", "boş");
            cinsiyet = preferences.getString("cinsiyet", "null");

     final    Movie m = movieItems.get(position);
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);




              if(eposta.equals(m.getEposta()))
              {

                  convertView=inflater.inflate(R.layout.list_row_yorumlarsol,null);

                  if (imageLoader == null)
                      imageLoader = AppController.getInstance().getImageLoader();
                  NetworkImageView thumbNail = (NetworkImageView) convertView
                          .findViewById(R.id.ivOyverFoto);
                  mesaj = (TextView) convertView.findViewById(R.id.tvResimBaslik);


                  TextView username = (TextView) convertView.findViewById(R.id.etUsername);
                  // getting movie data for the row

                  ivbenimyorumAsagi= (ImageView) convertView.findViewById(R.id.ivbenimyorumAsagi);
                  ivbenimyorumYukari= (ImageView) convertView.findViewById(R.id.ivbenimyorumYukari);
                  if (m.getOy()==1) {

                      ivbenimyorumYukari.setVisibility(View.VISIBLE);
                      ivbenimyorumAsagi.setVisibility(View.INVISIBLE);
                  } else if (m.getOy()==2) {
                      ivbenimyorumYukari.setVisibility(View.INVISIBLE);
                      ivbenimyorumAsagi.setVisibility(View.VISIBLE);
                  }else {
                      ivbenimyorumYukari.setVisibility(View.INVISIBLE);
                      ivbenimyorumAsagi.setVisibility(View.INVISIBLE);

                  }
                  ivyorumsil=(ImageView)convertView.findViewById(R.id.ivyorumsil);
                  ivyorumsil.setVisibility(View.VISIBLE);
                   ivyorumsil.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                           // builder.setCancelable(false);
                           builder.setTitle(activity.getResources().getString(R.string.doyouwanttodelete));
                           //   builder.setMessage(mContext.getString(R.string.addfriendlist));
                           builder.setPositiveButton(activity.getString(R.string.yes),new DialogInterface.OnClickListener() {

                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   // TODO Auto-generated method stub


               /* Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                startActivity(intent);
                */

                                   yorumSil(m.getKayit_no());
                                   movieItems.remove(position);
                                   notifyDataSetChanged();

                               }
                           });
                           builder.setNegativeButton(activity.getResources().getString(R.string.no),new DialogInterface.OnClickListener() {

                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   // TODO Auto-generated method stub
                                   dialog.cancel();

                               }
                           });

                           AlertDialog alert=builder.create();
                           alert.show();
                           //super.onBackPressed();


                       }
                   });



                  if (m.getThumbnailUrl().equals("null")) {

                      if (cinsiyet.equals("Bay")) {

                          thumbNail.setImageUrl(Config.SERVER_IS + "varsayilan/male.png", imageLoader);

                      } else {


                          thumbNail.setImageUrl(Config.SERVER_IS +  "varsayilan/female.png", imageLoader);

                      }

                  } else {
                      // thumbnail image
                      thumbNail.setImageUrl(Config.SERVER_IS + "" + m.getThumbnailUrl(), imageLoader);
                  }



                  mesaj.setText(m.getMesaj());

                  username.setText(m.getUsername());


              }

              else{
                convertView = inflater.inflate(R.layout.list_row_yorumlar, null);
                ivyorumasagi= (ImageView) convertView.findViewById(R.id.ivyorumasagi);
                  ivyorumyukari= (ImageView) convertView.findViewById(R.id.ivyorumyukari);
                  ivbaskayorumsil= (ImageView) convertView.findViewById(R.id.ivbaskayorumSil);
                 // Toast.makeText(activity,String.valueOf(m.getSil()),Toast.LENGTH_LONG).show();
                  if(m.getSil()==1)
                  {
                      ivbaskayorumsil.setVisibility(View.VISIBLE);


                  }else {
                      ivbaskayorumsil.setVisibility(View.INVISIBLE);

                  }
                  ivbaskayorumsil.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                          // builder.setCancelable(false);
                          builder.setTitle("Silmek istediğinize emin misiniz?");
                          //   builder.setMessage(mContext.getString(R.string.addfriendlist));
                          builder.setPositiveButton(activity.getString(R.string.yes),new DialogInterface.OnClickListener() {

                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  // TODO Auto-generated method stub


               /* Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                startActivity(intent);
                */

                                  yorumSil(m.getKayit_no());
                                  movieItems.remove(position);
                                  notifyDataSetChanged();

                              }
                          });
                          builder.setNegativeButton(activity.getResources().getString(R.string.no),new DialogInterface.OnClickListener() {

                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  // TODO Auto-generated method stub
                                  dialog.cancel();

                              }
                          });

                          AlertDialog alert=builder.create();
                          alert.show();
                          //super.onBackPressed();



                      }
                  });
                  if (m.getOy()==1) {

                   ivyorumyukari.setVisibility(View.VISIBLE);
                  ivyorumasagi.setVisibility(View.INVISIBLE);
                  } else if (m.getOy()==2) {
                      ivyorumyukari.setVisibility(View.INVISIBLE);
                      ivyorumasagi.setVisibility(View.VISIBLE);
                         }else {
                      ivyorumyukari.setVisibility(View.INVISIBLE);
                      ivyorumasagi.setVisibility(View.INVISIBLE);

                  }
                  //ivReply.setTag(position);
                ivYorumCevir= (ImageView) convertView.findViewById(R.id.ivYorumCevir);
               //   btnFollow=(Button)convertView.findViewById(R.id.btnFollow);

                  ivYorumCevir.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {

                          Cevir(m.getMesaj(),m.getDiL());
                      }
                  });



                  if (imageLoader == null)
                      imageLoader = AppController.getInstance().getImageLoader();
                  NetworkImageView thumbNail = (NetworkImageView) convertView
                          .findViewById(R.id.ivOyverFoto);
                  mesaj = (TextView) convertView.findViewById(R.id.tvResimBaslik);


                  TextView username = (TextView) convertView.findViewById(R.id.etUsername);
                  // getting movie data for the row


                  if (m.getThumbnailUrl().equals("null")) {

                      if (cinsiyet.equals("Bay")) {

                          thumbNail.setImageUrl(Config.SERVER_IS + "varsayilan/male.png", imageLoader);

                      } else {


                          thumbNail.setImageUrl(Config.SERVER_IS +  "varsayilan/female.png", imageLoader);

                      }

                  } else {
                      // thumbnail image
                      thumbNail.setImageUrl(Config.SERVER_IS + "" + m.getThumbnailUrl(), imageLoader);
                  }
  thumbNail.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Intent i = new Intent(activity, KullaniciProfili.class);

          i.putExtra("to_user_id",eposta);

          i.putExtra("to_eposta",  m.getEposta());

          activity.startActivity(i);
      }
  });


                  mesaj.setText(m.getMesaj());

                  username.setText(m.getUsername());
              }










        return convertView;
    }
    public void Cevir(String Mesaj,String dbdil){
       String dil= Locale.getDefault().getLanguage();


        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_CEVIRI+"&text="+Mesaj.trim()+"&lang="+dbdil+"-"+dil;
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    showTranslate(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity.getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);




    }

    private void showTranslate(String response) throws JSONException {
        String code = "";
        String lang = "";
        JSONArray text ;

        try {
            JSONObject jsonObj = new JSONObject(response);
            code = jsonObj.getString("code");
            lang = jsonObj.getString("lang");
            text = jsonObj.getJSONArray("text");
            mesaj.setText(text.getString(0));


        } catch (Exception e) {
            e.printStackTrace();
        }


        // Toast.makeText(getActivity().getApplicationContext(), text.getString(0).toString(),Toast.LENGTH_LONG);
        // baslik.setText(text.getString(0));
    }

    private void Follow(final String friend, final String diL) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"takipet.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("success_login")){
                            Toast.makeText(activity, "OK", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(activity, "Takip listenizde", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtkullanici", (String.valueOf(eposta)) );
                params.put("txtfriend", (String.valueOf(friend)) );
                params.put("txtDil",String.valueOf(diL));
                return params;
            }
        };

        MySingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void yorumSil(final int olayid) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"yorumsil.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("success_login")){
                            Toast.makeText(activity, "Silindi", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(activity, "Silinemedi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Error while reading data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("olayid", (String.valueOf(olayid)) );

                return params;
            }
        };

        MySingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
