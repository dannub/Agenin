package com.agenin.id.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.agenin.id.Adapter.NotificationAdapter;
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.NotifClient;
import com.agenin.id.Model.Api.NotifAPI;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationActivity extends AppCompatActivity {

    public static Dialog loadingDialog;
    public static TextView noData;
    public static ConstraintLayout background;
    public static ImageView no_internet;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    public static RecyclerView recyclerView;
    public static NotificationAdapter adapter;
    private boolean runQuery=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Notifikasi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //loading dialog
        loadingDialog = new Dialog(NotificationActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        noData = findViewById(R.id.pesanan);
        background = findViewById(R.id.bg);
        no_internet = findViewById(R.id.no_internet_connection);
        swipeRefreshLayout = findViewById(R.id.swipe);
        connectivityManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();



        recyclerView = findViewById(R.id.recycler_view);

        swipeRefreshLayout.setColorSchemeColors(this.getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimary));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadPage();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NotificationAdapter(DBQueries.notificationModelList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();





        //loading dialog

        toolbar.inflateMenu(R.menu.setting);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.delete)
                {

                    loadingDialog.show();
                    loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });


                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();

                    UserPreference userPreference = new UserPreference(NotificationActivity.this);
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
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .build();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(DBQueries.url)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    NotifClient notifAPI = retrofit.create(NotifClient.class);
                    Call<ResponseBody> call = notifAPI.deleteAllNotif(user.getId());

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){






                                DBQueries.loadNotification(NotificationActivity.this,loadingDialog);

                            } else {
                                if (loadingDialog!=null) {
                                    loadingDialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("debug", "onFailure: ERROR > " + t.toString());
                            if (loadingDialog!=null) {
                                loadingDialog.dismiss();
                            }
                        }
                    });





                }

                return false;
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        reloadPage();
    }

    private void reloadPage() {
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo !=null && networkInfo.isConnected()==true) {
           // DBQueries.loadNotification();

            DBQueries.loadNotification(this,loadingDialog);
        }else {
            noData.setVisibility(View.GONE);
            background.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
            no_internet.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int x=0;x<DBQueries.notificationModelList.size();x++){
            DBQueries.notificationModelList.get(x).setReaded(true);

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if(id==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
