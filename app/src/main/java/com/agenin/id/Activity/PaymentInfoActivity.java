package com.agenin.id.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.agenin.id.R;

import info.hoang8f.widget.FButton;

public class PaymentInfoActivity extends AppCompatActivity {

    private  int bank;
    private TextView total;
    private TextView norek;
    private TextView cabang;
    private TextView an;
    private Dialog loadingDialog;
    private ImageView bank_img;
    private FButton payment;
    private TextView salin;
    public static Activity paymentInfoActivity;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);



        //loading dialog
        loadingDialog = new Dialog(PaymentInfoActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog

        total = (TextView) findViewById(R.id.total);
        norek = (TextView) findViewById(R.id.norek);
        cabang = (TextView) findViewById(R.id.cabang);
        an = (TextView) findViewById(R.id.an);
        bank_img = (ImageView) findViewById(R.id.img_bank);
        payment =(FButton) findViewById(R.id.upload_btn);
        salin = (TextView) findViewById(R.id.salin);


                Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Info Pembayaran");

        payment.setButtonColor(getResources().getColor(R.color.colorPrimary));
        payment.setTextColor(getResources().getColor(R.color.colorAccent));



        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                bank = 1;
            } else {
                bank= extras.getInt("bank");
            }
        } else {
            bank= (int) savedInstanceState.getSerializable("bank");
        }


        total.setText(DeliveryActivity.totalAmount.getText());
        if (bank==1){
            bank_img.setImageDrawable(getDrawable(R.drawable.bri));
            norek.setText("00000000000000000");
            cabang.setText("Surabaya");
            an.setText("Agenin BRI");
            salin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("No.Rekening tersalin", "00000000000000000");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(PaymentInfoActivity.this,"No.Rekening tersalin", Toast.LENGTH_SHORT).show();

                }
            });
        }else if (bank == 2){
            bank_img.setImageDrawable(getDrawable(R.drawable.bni));
            norek.setText("00000000000000123");
            cabang.setText("Surabaya");
            an.setText("Agenin BNI");
            salin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("No.Rekening tersalin", "00000000000000123");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(PaymentInfoActivity.this,"No.Rekening tersalin", Toast.LENGTH_SHORT).show();

                }
            });
        }else if(bank==3){
            bank_img.setImageDrawable(getDrawable(R.drawable.bca));
            norek.setText("00000000000000345");
            cabang.setText("Surabaya");
            an.setText("Agenin BCA");
            salin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("No.Rekening tersalin", "00000000000000345");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(PaymentInfoActivity.this,"No.Rekening tersalin", Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            bank_img.setImageDrawable(getDrawable(R.drawable.mandiri));
            norek.setText("00000000000000567");
            cabang.setText("Surabaya");
            an.setText("Agenin Mandiri");
            salin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("No.Rekening tersalin", "00000000000000567");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(PaymentInfoActivity.this,"No.Rekening tersalin", Toast.LENGTH_SHORT).show();

                }
            });

        }



        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentInfoActivity = PaymentInfoActivity.this;
                loadingDialog.show();
                loadingDialog.dismiss();
                Intent paymentInfoIntent = new Intent(PaymentInfoActivity.this, PaymentActivity.class);
                paymentInfoIntent.putExtra("bank",bank);
                startActivity(paymentInfoIntent);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            paymentInfoActivity = null;
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        paymentInfoActivity = null;
        super.onBackPressed();
    }
}
