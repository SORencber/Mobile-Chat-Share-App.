// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.yavuz.rencber.rencber.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;

import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import com.yavuz.rencber.rencber.GPS.Trends.LocationAssistant;
import com.yavuz.rencber.rencber.R;
import com.yavuz.rencber.rencber.audio.AndroidMultiPartEntity;
import com.yavuz.rencber.rencber.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_LONG;

public final class CropResultActivity  extends AppCompatActivity
        implements LocationAssistant.Listener  {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    ProgressDialog dialog = null;
    private LocationManager locationManager;
    Button btnYukle;
    private String TAG = CropResultActivity.class.getSimpleName();
  TextView tvKonumSehir,tvKonumUlke,tvKonumAdres,tvilce,tvKonumEkle;
    Button btnKonum;
    EditText etBaslik;
    Spinner spKategori, spYas;
    String gizlilik;
    ImageView ivKonum;
    public ProgressBar progressBar;
    public TextView txtPercentage;
    /**
     * The image to show in the activity.
     */
    static Bitmap mImage;
    RadioGroup radioGizlilik;
    RadioButton rbarkadaslarimGorsun, rbherkesGorsun;
    String url, dil;
    private ImageView imageView;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    String eposta, foto, chat_room_id, to_user_id, name, user_id, grup, sahibi, odaIsmi;
     LocationAssistant assistant;
    public   double latitude,longitude;
    long totalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crop_result);


        // Create the LocationRequest object

        btnYukle = (Button) findViewById(R.id.btnUpload);
        //spKategori=(Spinner)findViewById(R.id.spKategori);
        // spYas   =(Spinner)findViewById(R.id.spYas);
        etBaslik = (EditText) findViewById(R.id.etbaslik);
        radioGizlilik = (RadioGroup) findViewById(R.id.radioGizlilik);
        rbarkadaslarimGorsun = (RadioButton) findViewById(R.id.radioArkadaslarimGorsun);
        rbherkesGorsun = (RadioButton) findViewById(R.id.radioHerkesGorsun);
        ivKonum = (ImageView) findViewById(R.id.ivKonum);
        tvKonumUlke = (TextView) findViewById(R.id.tvKonumUlke);
        tvKonumSehir = (TextView) findViewById(R.id.tvKonumSehir);
        tvKonumAdres = (TextView) findViewById(R.id.tvKonumAdres);
        tvilce = (TextView) findViewById(R.id.tvilce);
        tvKonumEkle = (TextView) findViewById(R.id.tvKonumEkle);
        txtPercentage = (TextView) findViewById(R.id.txtYuzde);
        progressBar = (ProgressBar) findViewById(R.id.progressBarUploadImage);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit();
        //   editor.clear();
        //  editor.commit();
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 5000, false);
        ivKonum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assistant.start();
                // tvKonum.setText(getCity());
            }
        });
        tvKonumEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assistant.start();
                // tvKonum.setText(getCity());
            }
        });
        if (MainActivity.instance != null) {
            try {
                MainActivity.instance.finish();
            } catch (Exception e) {
            }
        }
        if (ChatRoomActivity.instance2 != null) {
            try {
                ChatRoomActivity.instance2.finish();
            } catch (Exception e) {
            }
        }

        dil = Locale.getDefault().getLanguage();


        eposta = preferences.getString("email", "boş");
        foto = preferences.getString("foto", "0");
        chat_room_id = preferences.getString("chat_room_id", "0");
        user_id = preferences.getString("user_id", "0");
        to_user_id = preferences.getString("to_user_id", "0");
        name = preferences.getString("name", "0");
        grup = preferences.getString("grup", "0");
        sahibi = preferences.getString("sahibi", "0");
        odaIsmi = preferences.getString("odaIsmi", "0");


        if (foto.equals("1")) {
            etBaslik.setText("foto ekleniyor");
            etBaslik.setVisibility(View.GONE);
            // spKategori.setVisibility(View.GONE);
            // spYas.setVisibility(View.GONE);
            radioGizlilik.setVisibility(View.GONE);
        }
        if (foto.equals("2")) {
            etBaslik.setText("foto ekleniyor");
            etBaslik.setVisibility(View.GONE);
            // spKategori.setVisibility(View.GONE);
            // spYas.setVisibility(View.GONE);
            radioGizlilik.setVisibility(View.GONE);
        }
        if (mImage != null) {
            imageView = ((ImageView) findViewById(R.id.resultImageView));
            imageView.setBackgroundResource(R.drawable.backdrop);
            imageView.setImageBitmap(mImage);
            double ratio = ((int) (10 * mImage.getWidth() / (double) mImage.getHeight())) / 10d;
            int byteCount = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
                byteCount = mImage.getByteCount() / 1024;
            }
           /* String desc = "(" + mImage.getWidth() + ", " + mImage.getHeight() + "), Ratio: " + ratio + ", Bytes: " + byteCount + "K";
            ((TextView) findViewById(R.id.resultImageText)).setText(desc);
            */
        } else {
            Toast.makeText(this, "No image is set to show", LENGTH_LONG).show();
        }

        // final MyCommand myCommand = new MyCommand(getApplicationContext());

        btnYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String strBaslik = etBaslik.getText().toString();

              /*  if (etBaslik.getText().toString().length() == 0) {
                    etBaslik.setError(getResources().getString(R.string.question));
                    return;
                }
*/ btnYukle.setEnabled(false);
                if (rbherkesGorsun.isChecked()) {
                    gizlilik = "0";
                } else {
                    gizlilik = "1";
                }
                if (rbarkadaslarimGorsun.isChecked()) {
                    gizlilik = "1";
                } else {
                    gizlilik = "0";
                }
              //  dialog = ProgressDialog.show(CropResultActivity.this, "", "Dosya yükleniyor...", true);

                new UploadFileToServer().execute();


            }
        });
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
            return Upload();
        }

        @SuppressWarnings("deprecation")
        private String Upload() {

            final String[] responseString = {null};
            // Toast.makeText(getApplicationContext(),gizlilik.toString(),Toast.LENGTH_LONG).show();

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                //myDir.delete();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = Environment.getExternalStorageDirectory().
                        getAbsolutePath() + "/Image" + n + ".jpg";

                final File file = new File(fname);
                if (file.exists()) file.delete();
                try {
                    //  finalBitmap = rotate(bmp,50);
                    FileOutputStream out = new FileOutputStream(file);
                    mImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    btnYukle.setEnabled(true);
                }
                String picturePath2 = fname;
                Bitmap bitmap;
                 String encodedString = null;
                //  Toast.makeText(getApplicationContext(), picturePath2, Toast.LENGTH_SHORT).show();
                // Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(picturePath2));
                //  BitmapFactory.Options o = new BitmapFactory.Options();
                // o.inSampleSize = 2;
                //  Bitmap b = BitmapFactory.decodeFile(picturePath2, o);

                // Toast.makeText(getApplicationContext(), dil, Toast.LENGTH_SHORT).show();

                if (foto.equals("1")) {
                     bitmap = PhotoLoader.init().from(picturePath2).requestSize(512, 512).getBitmap();
                     encodedString = ImageBase64.encode(bitmap);

                    url = Config.SERVER_IS + "uploadfoto.php";

                } else if (foto.equals("2")) {
                     bitmap = PhotoLoader.init().from(picturePath2).requestSize(300, 300).getBitmap();
                     encodedString = ImageBase64.encode(bitmap);

                    Intent i = new Intent(getApplicationContext(), ChatRoomActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    i.putExtra("chat_room_id", chat_room_id);
                    i.putExtra("name", name);
                    i.putExtra("to_user_id", to_user_id);
                    i.putExtra("sendcalistir", "calis");
                    i.putExtra("encodedString", encodedString);
                    i.putExtra("user_id", user_id);
                    i.putExtra("grup", grup);
                    i.putExtra("sahibi", sahibi);
                    i.putExtra("odaIsmi", odaIsmi);

                    startActivity(i);
                    file.delete();

                    finish();
                } else if (foto.equals("0")) {
                     bitmap = PhotoLoader.init().from(picturePath2).requestSize(512, 512).getBitmap();
                     encodedString = ImageBase64.encode(bitmap);

                    url = Config.SERVER_IS + "upload.php";

                }
                final String finalEncodedString = encodedString;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                         responseString[0] = response;
                        Log.e(TAG, "response: " + response);
                        if (response.contains("uploaded_success")) {
                            file.delete();

                            Toast.makeText(getApplicationContext(), "RESİM YÜKLENDİ", LENGTH_LONG).show();
                            setResult(201);
                            finish();
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
                        params.put("image", finalEncodedString);
                        params.put("txteposta", eposta);
                        //params.put("txtkategori",String.valueOf(radioGizlilik.getCheckedRadioButtonId()));
                        if (foto.equals("0")) {
                            params.put("txtbaslik", etBaslik.getText().toString());
                            params.put("user_id", user_id);
                            params.put("chatRoomId", chat_room_id);

                            params.put("txtGizlilik", gizlilik.toString());

                            params.put("txtUlke", tvKonumUlke.getText().toString());
                            params.put("txtSehir", tvKonumSehir.getText().toString());
                            params.put("txtIlce", tvilce.getText().toString());
                            params.put("txtadres", tvKonumAdres.getText().toString());

                            if (String.valueOf(latitude).equals("") || String.valueOf(latitude).equals("null")) {
                                params.put("txtlatitude", "");
                                params.put("txtlongitude", "");


                            } else {
                                params.put("txtlatitude", String.valueOf(latitude));
                                params.put("txtlongitude", String.valueOf(longitude));

                            }
                            params.put("txtDil", dil);
                        }
                        return params;
                    }


                };

                int socketTimeout = 0;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                stringRequest.setRetryPolicy(policy);

                //Adding request to request queue
                // MyApplication.getInstance().addToRequestQueue(strReq);
                AppController.getInstance().addToRequestQueue(stringRequest);
            } catch (FileNotFoundException e) {
                btnYukle.setEnabled(true); //  Toast.makeText(getApplicationContext(), "Error while uploading image", Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(), "Error  while loading image", Toast.LENGTH_SHORT).show();
            }
            // myCommand.execute();
            return responseString[0];

        }


    }


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
        assistant.onActivityResult(requestCode, resultCode);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }




    public void getAddress(final double currentLatitude, final double currentLongitude) {

        String url="http://maps.googleapis.com/maps/api/geocode/json?latlng="+currentLatitude+","+currentLongitude+"&sensor=true";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "koordinant"+String.valueOf(currentLatitude)+"------"+String.valueOf(currentLongitude));
                        //Toast.makeText(getApplicationContext(),response.toString(),LENGTH_LONG).show();
        try {

            JSONObject jsonObject = new JSONObject(response);

            if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                JSONArray results = jsonObject.getJSONArray("results");
                if (results.length() > 0) {
                    for (int i = 0; i < results.length() ; i++) {
                        JSONObject result = results.getJSONObject(i);
                        //Log.e(MyGeocoder.class.getName(), result.toString());
                        Address addr = new Address(Locale.getDefault());
                        // addr.setAddressLine(0, result.getString("formatted_address"));

                        JSONArray components = result.getJSONArray("address_components");
                        String country = "";
                        String route = "";
                        String adres="";
                        String ilce="";
                        for (int a = 0; a < components.length(); a++) {
                            JSONObject component = components.getJSONObject(a);
                            JSONArray types = component.getJSONArray("types");
                            for (int j = 0; j < types.length(); j++) {
                                String type = types.getString(j);
                                if (type.equals("administrative_area_level_1")) {
                                    adres =component.getString("long_name");
                                    if(adres.equals("")){ }else{ tvKonumSehir.setText(adres);}
                                } else if (type.equals("country")) {
                                    country = component.getString("long_name");
                                    if(country.equals("")){ }else{ tvKonumUlke.setText(country);
                                    }

                                } else if (type.equals("administrative_area_level_4")) {
                                    route = component.getString("long_name");
                                    if(route.equals("")){ }else{ tvKonumAdres.setText(route);

                                    }
                                } else if (type.equals("administrative_area_level_2")) {
                                ilce = component.getString("long_name");
                                if(ilce.equals("")){ }else {
                                    tvilce.setText(ilce);

                                }
                            }
                            }
                        }
                       // Toast.makeText(getApplicationContext(),route.toString(),LENGTH_LONG).show();


                    }     // JSONArray mtypes = zero2.getJSONArray("types");
                    // String Type = mtypes.getString(0);
                    // Log.e(Type,long_name);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        AppController.getInstance().addToRequestQueue(strReq);

    }


    @Override

    protected void onResume() {

        super.onResume();

        //assistant.start();

    }



    @Override

    protected void onPause() {

        assistant.stop();

        super.onPause();

    }



    @Override

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (assistant.onPermissionsUpdated(requestCode, grantResults));

          //  tvKonum.setOnClickListener(null);

    }







    @Override

    public void onNeedLocationPermission() {

        tvKonumSehir.setText("Need\nPermission");

        tvKonumSehir.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                assistant.requestLocationPermission();

            }

        });

        assistant.requestAndPossiblyExplainLocationPermission();

    }



    @Override

    public void onExplainLocationPermission() {

        new AlertDialog.Builder(this)

                .setMessage("izinler")

                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        assistant.requestLocationPermission();

                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        tvKonumSehir.setOnClickListener(new View.OnClickListener() {

                            @Override

                            public void onClick(View v) {

                                assistant.requestLocationPermission();

                            }

                        });

                    }

                })

                .show();

    }



    @Override

    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView,

                                                        DialogInterface.OnClickListener fromDialog) {

        new AlertDialog.Builder(this)

                .setMessage("İzin muhabbeti")

                .setPositiveButton(R.string.ok, fromDialog)

                .show();

    }



    @Override

    public void onNeedLocationSettingsChange() {

        new AlertDialog.Builder(this)

                .setMessage(        "Konum izni yok")

                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        assistant.changeLocationSettings();

                    }

                })

                .show();

    }



    @Override

    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

        new AlertDialog.Builder(this)

                    .setMessage("burada ne yazar")

                .setPositiveButton(R.string.ok, fromDialog)

                .show();

    }



    @Override

    public void onNewLocationAvailable(Location location) {

            if (location == null) return;

            tvKonumSehir.setOnClickListener(null);

            //tvKonum.setText(location.getLongitude() + "\n" + location.getLatitude());
            //tvKonum.setAlpha(1.0f);

       // getCompleteAddressString( location.getLatitude(),location.getLongitude());
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        getAddress( location.getLatitude(),location.getLongitude());
            //tvKonum.setText(getCity());
          //  tvKonum.animate().alpha(0.5f).setDuration(400);


    }

        @Override

    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

        tvKonumSehir.setText("brada mock var");

        tvKonumSehir.setOnClickListener(fromView);

    }



    @Override

    public void onError(LocationAssistant.ErrorType type, String message) {

        tvKonumSehir.setText("hata");

    }
}


