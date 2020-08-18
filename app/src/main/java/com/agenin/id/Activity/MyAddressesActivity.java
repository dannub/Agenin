package com.agenin.id.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.agenin.id.Adapter.AddressesAdapter;
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.AddressClient;
import com.agenin.id.Model.AddressModel;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import info.hoang8f.widget.FButton;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyAddressesActivity extends AppCompatActivity {



    private int previousAddress;
    private FButton deliverHereBtn;
    public static TextView addressesSaved;
    public static RecyclerView myAddressesRecyclerView;
    private Button add_new_address_btn;
    public static AddressesAdapter addressesAdapter;
    private Dialog loadingDialog;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        add_new_address_btn = findViewById(R.id.add_new_address_btn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Alamat");


        //loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                addressesSaved.setText(" "+ DBQueries.addressModelList.size() +" saved addresses ");
            }
        });
        //loading dialog


        previousAddress = DBQueries.selectedAddress;

        addressesSaved = findViewById(R.id.address_saved);
        deliverHereBtn = findViewById(R.id.delivery_here_btn);
        deliverHereBtn.setButtonColor(getResources().getColor(R.color.colorPrimaryDark));
        deliverHereBtn.setTextColor(getResources().getColor(R.color.colorAccent));

        myAddressesRecyclerView = findViewById(R.id.addresses_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(linearLayoutManager);


         mode = getIntent().getIntExtra("MODE",-1);

        if(mode ==DeliveryActivity.SELECT_ADDRESS){
            deliverHereBtn.setVisibility(View.VISIBLE);
        }else {
            deliverHereBtn.setVisibility(View.GONE);
        }
        deliverHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (DBQueries.selectedAddress!=previousAddress) {

                        final int previousAddressIndex = previousAddress;
                        loadingDialog.show();
                        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        });
                        Gson gson = new GsonBuilder()
                                .setLenient()
                                .create();

                        previousAddress = DBQueries.selectedAddress;
                        UserPreference userPreference = new UserPreference(MyAddressesActivity.this);
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
                        Call<List<AddressModel>> call = addressAPI.updateSelectedAddress(user.getId(),DBQueries.addressModelList.get(DBQueries.selectedAddress).get_id());

                        call.enqueue(new Callback<List<AddressModel>>() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onResponse(Call<List<AddressModel>> call, Response<List<AddressModel>> response) {
                                if (!response.isSuccessful()) {
                                    Log.i("response", String.valueOf(response.body()));
                                    Toast.makeText(MyAddressesActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                                    if (loadingDialog!=null) {
                                        previousAddress = previousAddressIndex;
                                        loadingDialog.dismiss();
                                    }
                                    return;
                                }
                                    loadingDialog.dismiss();
                                    finish();



                            }
                            @Override
                            public void onFailure(Call<List<AddressModel>> call, Throwable t) {
                                Log.e("debug", "onFailure: ERROR > " + t.toString());
                                previousAddress = previousAddressIndex;
                                if (loadingDialog!=null) {
                                    loadingDialog.dismiss();
                                }
                            }
                        });


                    }
            }
        });

            addressesAdapter = new AddressesAdapter(MyAddressesActivity.this,DBQueries.addressModelList, mode,loadingDialog);
            myAddressesRecyclerView.setAdapter(addressesAdapter);
            ((SimpleItemAnimator) myAddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            addressesAdapter.notifyDataSetChanged();

        add_new_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAddressIntent = new Intent(MyAddressesActivity.this,AddAddressActivity.class);
                if (mode!=DeliveryActivity.SELECT_ADDRESS){
                    addAddressIntent.putExtra("INTENT","manage");
                }else {
                    addAddressIntent.putExtra("INTENT","null");
                }
                addAddressIntent.putExtra("INTENT","null");
                startActivity(addAddressIntent);
            }
        });

     }

    @Override
    protected void onStart() {
        super.onStart();
        addressesSaved.setText(" "+ DBQueries.addressModelList.size() +" alamat tersimpan");

    }

    public static void refreshItem(int deselect, int select){
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);
        addressesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        if(item.getItemId()==android.R.id.home){
            if (mode==DeliveryActivity.SELECT_ADDRESS) {
                if (DBQueries.selectedAddress != previousAddress) {
                    DBQueries.addressModelList.get(DBQueries.selectedAddress).setSelected(false);
                    DBQueries.addressModelList.get(previousAddress).setSelected(true);
                    DBQueries.selectedAddress = previousAddress;
                }
            }

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mode == OrderDetailsActivity.SELECT_ADDRESS_ORDER){
            finish();
        }else {
            if (DBQueries.selectedAddress != previousAddress) {
                DBQueries.addressModelList.get(DBQueries.selectedAddress).setSelected(false);
                DBQueries.addressModelList.get(previousAddress).setSelected(true);
                DBQueries.selectedAddress = previousAddress;
            }
        }
        super.onBackPressed();
    }
}
