package com.agenin.id.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.agenin.id.DBQueries;
import com.agenin.id.Interface.AddressClient;
import com.agenin.id.Model.AddressModel;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import info.hoang8f.widget.FButton;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddAddressActivity extends AppCompatActivity {

    private EditText provinsi;
    private EditText kabupaten;
    private EditText kecamatan;
    private EditText kodepos;

    private EditText alamat;
    private EditText nama;
    private EditText nohandphone;
    private EditText alternatifMobileNo;
    private Spinner stateSpinner;

    private String address_id;

    private Dialog loadingDialog;

    private String[] stateList;
    private String selectedState;

    private FButton saveBtn;

    private boolean updateAddress = false;
    private AddressModel addressModel;
    private int position;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Tambah Alamat Baru");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //loading dialog
        loadingDialog = new Dialog(AddAddressActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        }
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //loading dialog

        stateList  = getResources().getStringArray(R.array.countries_array);


        saveBtn = findViewById(R.id.save_btn);
        provinsi = findViewById(R.id.provinsi);
        kabupaten = findViewById(R.id.kabupaten);
        kecamatan = findViewById(R.id.kecamatan);
        kodepos = findViewById(R.id.kodepos);
        stateSpinner = findViewById(R.id.state_spinner);
        alamat = findViewById(R.id.alamat);
        nama = findViewById(R.id.nama);
        nohandphone = findViewById(R.id.nohandphone);
        alternatifMobileNo = findViewById(R.id.alternatif_mobile_no);




        ArrayAdapter spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,stateList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(spinnerAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for (int i = 0;i<stateList.length;i++){
            if (stateList[i].equals("Indonesia")){
                stateSpinner.setSelection(i);
            }
        }


        if (getIntent().getStringExtra("INTENT").equals("update_address")){
            updateAddress = true;
            position = getIntent().getIntExtra("index",-1);
            address_id = getIntent().getStringExtra("addressid");
            addressModel = DBQueries.addressModelList.get(position);

            getSupportActionBar().setTitle("Perbarui Alamat");
            provinsi.setText(addressModel.getProvinsi());
            kabupaten.setText(addressModel.getKabupaten());
            kecamatan.setText(addressModel.getKecamatan());
            alamat.setText(addressModel.getAlamat());
            nama.setText(addressModel.getNama());
            nohandphone.setText(addressModel.getNo_telepon());
            alternatifMobileNo.setText(addressModel.getNo_alternatif());
            kodepos.setText(addressModel.getKodepos());
            for (int i = 0;i<stateList.length;i++){
                if (stateList[i].equals(addressModel.getNegara())){
                    stateSpinner.setSelection(i);
                }
            }
            saveBtn.setText("Perbarui");

        }else {

            position = DBQueries.addressModelList.size();
        }


        saveBtn.setButtonColor(getResources().getColor(R.color.colorPrimary));
        saveBtn.setTextColor(getResources().getColor(R.color.colorAccent));
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(provinsi.getText())){
                    if (!TextUtils.isEmpty(kabupaten.getText())){
                        if (!TextUtils.isEmpty(kecamatan.getText())){
                            if (!TextUtils.isEmpty(kodepos.getText()) && kodepos.getText().length() >= 5){
                                 if (!TextUtils.isEmpty(nama.getText())){
                                        if (!TextUtils.isEmpty(nohandphone.getText())&& nohandphone.getText().length() <= 14){

                                            loadingDialog.show();

                                            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    
                                                }
                                            });
                                            
//                                            AddressInputModel addressInputModel = new AddressInputModel(
//                                                    stateSpinner.getSelectedItem().toString(),
//                                                    provinsi.getText().toString(),
//                                                    kabupaten.getText().toString(),
//                                                    kecamatan.getText().toString(),
//                                                    alamat.getText().toString(),
//                                                    kodepos.getText().toString(),
//                                                    nama.getText().toString(),
//                                                    nohandphone.getText().toString(),
//                                                    alternatifMobileNo.getText().toString()
//                                                    );

                                            Gson gson = new GsonBuilder()
                                                    .setLenient()
                                                    .create();

                                            UserPreference userPreference = new UserPreference(AddAddressActivity.this);
                                            UserModel user = UserPreference.getUserPreference("user");

                                            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                                    .addInterceptor(new Interceptor() {
                                                        @Override
                                                        public okhttp3.Response intercept(Chain chain) throws IOException {
                                                            Request newRequest  = chain.request().newBuilder()
                                                                    .addHeader("Authorization", "Bearer " + user.getToken())
                                                                    .build();
                                                            return chain.proceed(newRequest);
                                                        }
                                                    })
                                                    .readTimeout(2, TimeUnit.MINUTES)
                                                    .connectTimeout(2, TimeUnit.MINUTES)
                                                    .build();
                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(DBQueries.url)
                                                    .client(okHttpClient)
                                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                                    .build();

                                            AddressClient addressAPI = retrofit.create(AddressClient.class);

                                            if (updateAddress){
                                                Call<AddressModel> call = addressAPI.updateAddress(
                                                        user.getId(),
                                                        address_id,
                                                        nama.getText().toString(),
                                                        nohandphone.getText().toString(),
                                                        alternatifMobileNo.getText().toString(),
                                                        stateSpinner.getSelectedItem().toString(),
                                                        provinsi.getText().toString(),
                                                        kabupaten.getText().toString(),
                                                        kecamatan.getText().toString(),
                                                        kodepos.getText().toString(),
                                                        alamat.getText().toString());
                                                call.enqueue(new Callback<AddressModel>() {
                                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                    @Override
                                                    public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                                                        if (!response.isSuccessful()) {
                                                            Log.i("response", String.valueOf(response.body()));
                                                            Toast.makeText(AddAddressActivity.this,response.code(),Toast.LENGTH_SHORT).show();
                                                            if (loadingDialog!=null) {
                                                                loadingDialog.dismiss();
                                                            }
                                                            return;

                                                        }

                                                            DBQueries.addressModelList.set(position,new AddressModel(true,
                                                                    stateSpinner.getSelectedItem().toString(),
                                                                    provinsi.getText().toString(),
                                                                    kabupaten.getText().toString(),
                                                                    kecamatan.getText().toString(),
                                                                    alamat.getText().toString(),
                                                                    kodepos.getText().toString(),
                                                                    nama.getText().toString(),
                                                                    nohandphone.getText().toString(),
                                                                    alternatifMobileNo.getText().toString()
                                                            ));
                                                        loadingDialog.dismiss();


                                                        if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                            Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                            startActivity(deliveryIntent);
                                                        }else {
                                                            MyAddressesActivity.refreshItem(DBQueries.selectedAddress,DBQueries.addressModelList.size()-1);

                                                        }
                                                        finish();

                                                    }
                                                    @Override
                                                    public void onFailure(Call<AddressModel> call, Throwable t) {
                                                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                                                        if (loadingDialog!=null) {
                                                            loadingDialog.dismiss();
                                                        }
                                                    }
                                                });
                                            }else {
                                                Call<AddressModel> call = addressAPI.addNewAddress(user.getId(),
                                                        nama.getText().toString(),
                                                        nohandphone.getText().toString(),
                                                        alternatifMobileNo.getText().toString(),
                                                        stateSpinner.getSelectedItem().toString(),
                                                        provinsi.getText().toString(),
                                                        kabupaten.getText().toString(),
                                                        kecamatan.getText().toString(),
                                                        kodepos.getText().toString(),
                                                        alamat.getText().toString());

                                                call.enqueue(new Callback<AddressModel>() {
                                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                    @Override
                                                    public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                                                        if (!response.isSuccessful()) {
                                                            Log.i("response", String.valueOf(response.body()));
                                                            Toast.makeText(AddAddressActivity.this,response.code(),Toast.LENGTH_SHORT).show();
                                                            if (loadingDialog!=null) {
                                                                loadingDialog.dismiss();
                                                            }
                                                            return;

                                                        }

                                                            if (DBQueries.addressModelList.size() > 0) {
                                                                DBQueries.addressModelList.get(DBQueries.selectedAddress).setSelected(false);

                                                            }
                                                            DBQueries.addressModelList.add(response.body());

                                                            if (getIntent().getStringExtra("INTENT").equals("manage")) {
                                                                if (DBQueries.addressModelList.size()==0) {
                                                                    DBQueries.selectedAddress = DBQueries.addressModelList.size() - 1;
                                                                    DBQueries.selectedAddressId = response.body().get_id();
                                                                }
                                                            }else {

                                                                DBQueries.selectedAddress = DBQueries.addressModelList.size() - 1;
                                                                DBQueries.selectedAddressId = response.body().get_id();
                                                            }



                                                        if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                            Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                            startActivity(deliveryIntent);
                                                        }else {
                                                            MyAddressesActivity.refreshItem(DBQueries.selectedAddress,DBQueries.addressModelList.size()-1);

                                                        }
                                                        finish();

                                                    }
                                                    @Override
                                                    public void onFailure(Call<AddressModel> call, Throwable t) {
                                                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                                                        if (loadingDialog!=null) {
                                                            loadingDialog.dismiss();
                                                        }
                                                    }
                                                });
                                            }







                                        }else {
                                            nohandphone.requestFocus();
                                            Toast.makeText(AddAddressActivity.this,"Tolong masukan nomer handphone yang benar", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        nohandphone.requestFocus();
                                    }

                            }else {
                                kodepos.requestFocus();
                                Toast.makeText(AddAddressActivity.this,"Tolong Masukan Kode Pos yang benar", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            kecamatan.requestFocus();
                        }
                    }else {
                        kabupaten.requestFocus();
                    }

                }else {
                    provinsi.requestFocus();
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
