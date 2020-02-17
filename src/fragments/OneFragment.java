package com.yavuz.rencber.rencber.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import com.yavuz.rencber.rencber.activity.Config;
import com.yavuz.rencber.rencber.activity.CustomVolleyRequest;
import com.yavuz.rencber.rencber.activity.KullaniciProfili;
import com.yavuz.rencber.rencber.activity.MainActivity;
import com.yavuz.rencber.rencber.activity.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/**
 * Created by Serhat Ömer RENÇBER on 2018.
 */

public class OneFragment extends Fragment  {

    public OneFragment() {
        // Required empty public constructor
    }
    private static Toast toast;
    Button btnyukari, btnasagi, btngec;
    ImageView ivMesajGonder,ivYukari,ivsendYorum,ivCamera;
    View vi,toasview;
    ImageView begen,begenme,bunugec,ivCevir;
    TextView baslik,pid,tvYukari,tvAsagi,etToplamOy;
    public ProgressDialog loading;
    Point p;

    final String TAG = this.getClass().getSimpleName();
    private NetworkImageView imageView,ivFotoRastgele;
    private ImageLoader imageLoader;
    Spinner spinner;
    String GelenPosta;
    int secilenkategori;
String dil,dbdil;
   // String resimbaslik="";
    String resimyol="";
    String resimno = "";
    String asagi="";
    String yukari = "";
    String notr = "";
    String Fotocu="";
    String epostaFoto="";
    String name="";
    String to_user_id="";
    String to_eposta="";
    EditText yorum;
    String resimbaslik = null;

    //////////////////////////////////
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
///////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       GelenPosta = container.getTag().toString();

        //////////// AndroidLocalize den gelen degerleri alarak dil değiştiriyoruz ////


     //   Toast.makeText(getActivity().getApplicationContext(),GelenPosta,Toast.LENGTH_SHORT).show();
        vi = inflater.inflate(R.layout.fragment_one, container, false);
        ivCevir = (ImageView) vi.findViewById(R.id.ivCevir);
        ivMesajGonder = (ImageView) vi.findViewById(R.id.ivMesajgonder);

        begen = (ImageView) vi.findViewById(R.id.ivYukari);
        begenme = (ImageView) vi.findViewById(R.id.ivAsagi);
        //ivsendYorum=(ImageView)vi.findViewById(R.id.ivsendYorum);
       bunugec = (ImageView) vi.findViewById(R.id.imageView2);
        baslik =(TextView)vi.findViewById(R.id.baslik);
        pid=(TextView)vi.findViewById(R.id.pid);
        imageView = (NetworkImageView) vi.findViewById(R.id.imageView);
        ivFotoRastgele = (NetworkImageView) vi.findViewById(R.id.ivFotoRastgele);
        ivCamera = (ImageView)vi.findViewById(R.id.ivCamera);

      //  spinner=(Spinner)vi.findViewById(R.id.spinner);
         toasview = inflater.inflate(R.layout.toastsonuclar,   (ViewGroup)vi.findViewById(R.id.toastsonuc));
         tvYukari=(TextView) toasview.findViewById(R.id.tvYukari);
         tvAsagi=(TextView) toasview.findViewById(R.id.tvAsagi);
         etToplamOy=(TextView) toasview.findViewById(R.id.etUsername);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());//preferences objesi
        editor = preferences.edit();

        dil= Locale.getDefault().getLanguage();



        // bu.setOnClickListener(this);
        randomData("baslangic",0);

        begen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (toast != null)
                    toast.cancel();

                    sendData("1","");

            }
        });
        begenme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toast != null)
                    toast.cancel();
                sendData("2","");

            }
        });
        ivMesajGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open popup window

                showPopup(v);
            }
        });
     /*   ivsendYorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toast != null)
                    toast.cancel();

                if(yorum.getText().length()>0) {
                sendData("3");}else{ yorum.setError(getResources().getString(R.string.plsyourcomment));}
            }
        });
        */
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("foto","0"); //Galeriye girerken foto ekleme ayari icin 1 konumuna getiriyoruz.
                editor.commit();// degerleri telefona kaydediyoruz

                //  CropImage.startPickImageActivity(getActivity());
                Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
//
                startActivity(i);

                //  selectImage();
            }
        });
        ivCevir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cevir();
            }

        });
      bunugec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toast != null)
                    toast.cancel();
                sendData("0","");
            }
        });
        ivFotoRastgele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i = new Intent(getActivity().getApplication(),KullaniciProfili.class);
               i.putExtra("to_user_id",to_user_id.toString());
                i.putExtra("to_eposta",epostaFoto.toString());

                startActivity(i);

                // when chat is clicked, launch full chat thread activity
             /*   AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                // builder.setCancelable(false);
                // builder.setTitle("Bu uygulamaya Oy vermek istermisiniz?");
                builder.setMessage(getResources().getString(R.string.addfriendlist));
                builder.setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


               /* Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("to_user_id", chatRoom.getTo_user_id());
                startActivity(intent);
                */

         /*               ArkadasEkle(epostaFoto,GelenPosta);
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();

                    }
                });

                AlertDialog alert=builder.create();
                alert.show();
                //super.onBackPressed();

*/

            }


        });

    /*    imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toast != null)
                    toast.cancel();
                sendData("0");
            }
        });


*/



        // The method that displays the popup.


        return vi;

    }

    public void showPopup(View anchorView) {

        final View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup_layout, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        // Example: If you have a TextView inside `popup_layout.xml`
        ImageView  close= (ImageView)popupView.findViewById(R.id.close);
        ImageView  btnyukari= (ImageView)popupView.findViewById(R.id.btnyukari);

        ImageView  btnasagi= (ImageView)popupView.findViewById(R.id.btnasagi);

        yorum= (EditText) popupView.findViewById(R.id.yorum);
        yorum.requestFocus();
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }
        });

        btnyukari.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                sendData("1",yorum.getText().toString());
                yorum.setText("");
                popupWindow.dismiss();

            }
        });
        btnasagi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sendData("2",yorum.getText().toString());
                yorum.setText("");
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


    private void sendData(final String oy,final String yorumum) {

        // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

        String url = Config.SERVER_IS+"Olaylar.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        if(response.contains("success_login")){



                            if(oy=="0") {randomData("baslangic",secilenkategori);}else{
                                //yorum.setText("");
                                randomData("b",secilenkategori);}


                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                params.put("txtpid", pid.getText().toString());
                params.put("txtoy",oy.toString());
                params.put("txtyorum", yorumum.toString());
                params.put("txtkullanici",GelenPosta);
                params.put("txtDil",dil);
                params.put("txt_to_eposta",epostaFoto);
                params.put("txtresimyol",resimyol.toString());
                params.put("txtresimbaslik",resimbaslik.toString());
                return params;
            }
        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (toast != null)
            toast.cancel();
    }
    @Override
    public void onStop(){
        super.onStop();
        if (toast != null)
            toast.cancel();
    }


    public void randomData(final String a,int pos){



        String url=Config.SERVER_IS+"rastgele.php?kategori="+pos;

        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               if (a=="baslangic"){
                showJSON(response,"baslangic");}
            else{showJSON(response,"cikis");}}
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    public void showJSON(String response,String data) {
        try {
            //JSONObject jsonObject = new JSONObject(response);
            JSONArray result = new JSONArray(response);
            //JSONArray result = jsonObject.getJSONArray(response);
            JSONObject collegeData = result.getJSONObject(0);


           resimbaslik = collegeData.getString(Config.RESIM_BASLIK);
            resimyol = collegeData.getString(Config.RESIM_YOL);
            resimno = collegeData.getString(Config.RESIM_NO);
            asagi = collegeData.getString(Config.RESIM_ASAGI);
            yukari = collegeData.getString(Config.RESIM_YUKARI);
            dbdil = collegeData.getString("dil");
            Fotocu = collegeData.getString("foto");
            epostaFoto = collegeData.getString("eposta");
             to_user_id = collegeData.getString("kayit_no");




        } catch (JSONException e) {
            e.printStackTrace();
        }


        /// butona tıkladıktan sonra son oranları gösterir

        if (resimno != "") {
            int x = Integer.valueOf(yukari);
            int y = Integer.valueOf(asagi);
            float sonucasagi = y * 100 / (x + y);
            float sonucyukari = x * 100 / (x + y);
            String asagix = String.valueOf(sonucasagi);
            String yukarix = String.valueOf(sonucyukari);
            int toplam = x + y;

            if (data != "baslangic") {
                if (toast != null)
                    toast.cancel();

                tvAsagi.setText("%" + String.valueOf(asagix));
                tvYukari.setText("%" + String.valueOf(yukarix));
                // etToplamOy.setText("Toplam Oy: "+String.valueOf(toplam));
                toast = new Toast(getActivity());
                toast.getHorizontalMargin();
                toast.setView(toasview);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 900);
                if ((resimbaslik.equals("") || resimbaslik.equals("null"))) {
                    baslik.setVisibility(View.GONE);
                    ivCevir.setVisibility(View.GONE);

                } else
                {         baslik.setText(resimbaslik);
                }
                //toast =  Toast.makeText(getActivity().getApplicationContext(),"text",Toast.LENGTH_SHORT);
                //toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                //toast.setText( "Beğeni:  % "+yukarix+"\t\tBeğenmeyen %:"+asagix+"\n\n\nToplam Oy:"+String.valueOf(toplam));
                //toast.show();
            }

            if ((resimbaslik.equals("") || resimbaslik.equals("null"))) {
                baslik.setVisibility(View.GONE);
               ivCevir.setVisibility(View.GONE);

            } else
            {
                baslik.setVisibility(View.VISIBLE);
                ivCevir.setVisibility(View.VISIBLE);
                baslik.setText(resimbaslik);
            }

           pid.setText(resimno);
           loadImage(Config.SERVER_IS+""+resimyol);
           loadFoto(Config.SERVER_IS+""+Fotocu);

       }
    }

    private void loadImage(String s){



        ImageLoader imageLoader = CustomVolleyRequest.getInstance(getActivity().getApplicationContext())
                .getImageLoader();
        imageLoader.get(s, ImageLoader.getImageListener(imageView,
                R.drawable.image, android.R.drawable
                        .ic_dialog_alert));

        imageView.setImageUrl(s, imageLoader);
    }
    private void loadFoto(String s){



        ImageLoader imageLoader = CustomVolleyRequest.getInstance(getActivity().getApplicationContext())
                .getImageLoader();
        imageLoader.get(s, ImageLoader.getImageListener(ivFotoRastgele,
                R.drawable.image, android.R.drawable
                        .ic_dialog_alert));

        ivFotoRastgele.setImageUrl(s, imageLoader);
    }
public void Cevir(){


    // final ProgressDialog loading = ProgressDialog.show(getActivity().getApplicationContext(), "Lütfen Bekleyin...", "İşlem Yapılıyor...", false, false);

    String url = Config.SERVER_CEVIRI+"&text="+baslik.getText().toString().trim()+"&lang="+dbdil+"-"+dil;
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
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    // builder.setCancelable(false);
                    // builder.setTitle("Bu uygulamaya Oy vermek istermisiniz?");
                    builder.setMessage(getResources().getString(R.string.networkproblem));
                    builder.setPositiveButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            getActivity().finish();
                        }
                    });
                    Toast.makeText(getActivity().getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });

    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
    requestQueue.add(stringRequest);




}

    private void showTranslate(String response) throws JSONException {
        String code = "";
        String lang = "";
        JSONArray  text ;

        try {
            JSONObject jsonObj = new JSONObject(response);
             code = jsonObj.getString("code");
             lang = jsonObj.getString("lang");
             text = jsonObj.getJSONArray("text");

            baslik.setText(text.getString(0));
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Toast.makeText(getActivity().getApplicationContext(), text.getString(0).toString(),Toast.LENGTH_LONG);
        // baslik.setText(text.getString(0));
    }






}





