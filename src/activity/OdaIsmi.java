package com.yavuz.rencber.rencber.activity;
////////////////////////////////////////////////
//
//   Created by Serhat Omer RENÇBER   2018
//
////////////////////////////////////////////////
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yavuz.rencber.rencber.R;

public class OdaIsmi extends AppCompatActivity {

    Button btnGrupDevam;
    EditText etGrupIsmi;
    TextView tvUyariGrup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oda_ismi);
        btnGrupDevam=(Button) findViewById(R.id.btnPuanKaydet);
        etGrupIsmi=(EditText)findViewById(R.id.etGrupIsmi);
        tvUyariGrup=(TextView)findViewById(R.id.tvUyariPuan);
        btnGrupDevam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (etGrupIsmi.getText().toString().equals("") || etGrupIsmi.getText().toString().equals("NULL"))
                {

                    tvUyariGrup.setText("Lütfen bir Grup ismi veriniz.");
                }else {
                    Toast.makeText(OdaIsmi.this,etGrupIsmi.getText().toString(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), GrupOlustur.class);
                intent.putExtra("odaIsmi", etGrupIsmi.getText().toString());
                    intent.putExtra("hareket", "boş");
                    intent.putExtra("chat_room_id", "boş");

                startActivity(intent);
                finish();
            } }
        });

    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        setResult(1);
        super.finish();
    }
}
