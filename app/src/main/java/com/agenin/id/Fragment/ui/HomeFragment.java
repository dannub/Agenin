package com.agenin.id.Fragment.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.agenin.id.Activity.MainActivity;
import com.agenin.id.Adapter.CategoryAdapter;
import com.agenin.id.Adapter.HomePageAdapter;
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.UserClient;
import com.agenin.id.Model.CategoryModel;
import com.agenin.id.Model.HomePageModel;
import com.agenin.id.Model.HorizontalProductScrollModel;
import com.agenin.id.Model.SliderModel;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Model.WishlistModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {


    public static ConnectivityManager connectivityManager;
    public static NetworkInfo networkInfo;

    public static SwipeRefreshLayout swipeRefreshLayout;
    public static Dialog loadingDialog;
    public static RecyclerView homepagerecyclerView;
    private List<CategoryModel> categoryModelFakeList = new ArrayList<>();
    public static List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private static HomePageAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private static ImageView noInternetConnection;
    private static ImageView maintanance;
    private static TextView maintanance_text;
    private static Button retryBtn;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View itemView = inflater.inflate(R.layout.fragment_home, container, false);
       // final TextView textView = root.findViewById(R.id.text_home);
        noInternetConnection = itemView.findViewById(R.id.no_internet_connection);
        swipeRefreshLayout = itemView.findViewById(R.id.refresh_layout);
        retryBtn = itemView.findViewById(R.id.retry_btn);
        maintanance =itemView.findViewById(R.id.maintainance);
        maintanance_text =itemView.findViewById(R.id.maintainance_text);



        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary));

        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog

        homepagerecyclerView= (RecyclerView) itemView.findViewById(R.id.home_page_recycle);


        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homepagerecyclerView.setLayoutManager(linearLayoutManager);


        //categories fake list
        categoryModelFakeList.add(new CategoryModel("null","","","",""));
        categoryModelFakeList.add(new CategoryModel("","","","",""));
        categoryModelFakeList.add(new CategoryModel("","","","",""));
        categoryModelFakeList.add(new CategoryModel("","","","",""));
        categoryModelFakeList.add(new CategoryModel("","","","",""));
        categoryModelFakeList.add(new CategoryModel("","","","",""));
        categoryModelFakeList.add(new CategoryModel("","","","",""));
        categoryModelFakeList.add(new CategoryModel("","","","",""));
        categoryModelFakeList.add(new CategoryModel("","","","",""));
        categoryModelFakeList.add(new CategoryModel("","","","",""));

        //categories fake list

        ///home page fake list
        List<SliderModel> sliderModeFakelList = new ArrayList<>();
        sliderModeFakelList.add(new SliderModel("","null","#dfdfdf",null));
        sliderModeFakelList.add(new SliderModel("","null","#dfdfdf",null));
        sliderModeFakelList.add(new SliderModel("","null","#dfdfdf",null));
        sliderModeFakelList.add(new SliderModel("","null","#dfdfdf",null));
        sliderModeFakelList.add(new SliderModel("","null","#dfdfdf",null));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",",",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",",",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",",",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",",",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",",",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",",",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",",",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",",",","","","",""));

        homePageModelFakeList.add(new HomePageModel("",0,sliderModeFakelList));
        homePageModelFakeList.add(new HomePageModel(4,categoryModelFakeList));
        homePageModelFakeList.add(new HomePageModel("",3,"","#dfdfdf",horizontalProductScrollModelFakeList));
        homePageModelFakeList.add(new HomePageModel("",1,"","#dfdfdf"));
        homePageModelFakeList.add(new HomePageModel("",2,"","#dfdfdf",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));



        adapter = new HomePageAdapter(getContext(),homePageModelFakeList,"");
        adapter.notifyDataSetChanged();
        homepagerecyclerView.setAdapter(adapter);

        ///home page fake list






        connectivityManager =(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        if (networkInfo !=null && networkInfo.isConnected()==true && isOnline()) {
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            homepagerecyclerView.setVisibility(View.VISIBLE);
            maintanance.setVisibility(View.GONE);
            maintanance_text.setVisibility(View.GONE);
//
//            if (loadingDialog!=null){
//                loadingDialog.dismiss();
//            }
//            loadingDialog.show();
//            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//
//                }
//            });


            UserPreference userPreference = new UserPreference(itemView.getContext());
            userPreference.setUserPreference("user",null);
            if (FirebaseAuth.getInstance().getCurrentUser() != null){


                if(userPreference.getUserPreference("user")==null){
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .build();

                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl(DBQueries.url)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create());

                    Retrofit retrofit = builder.build();
                    UserClient client = retrofit.create(UserClient.class);

                    Call<UserModel> call =    client.login(FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getUid());

                    call.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.isSuccessful()){

                                UserModel userModel = response.body();
                                userPreference.setUserPreference("user", userModel);
                                DBQueries.loadCartList(itemView.getContext(),loadingDialog,false,MainActivity.badgeCount,new TextView(itemView.getContext()),false,null);

//                                if (DBQueries.lists.size() == 0) {
//                                    DBQueries.loadedCategoriesNames.add("HOME");
//
//
//                                    DBQueries.loadFragmentData(getContext(), 0, loadingDialog, homepagerecyclerView, "home", "Semua Kategori", false);
//
//
//                                } else {
//                                    DBQueries.loadFragmentData(getContext(), 0, loadingDialog, homepagerecyclerView, "home", "Semua Kategori", false);
//
//                                }



                            } else {

                                HomeFragment.maintance(true);
                                UserPreference userPreference = new UserPreference(itemView.getContext());
                                userPreference.setUserPreference("user", null);

                                loadingDialog.dismiss();

                            }

                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            HomeFragment.maintance(true);
                            Log.e("debug", "onFailure: ERROR > " + t.toString());

                            loadingDialog.dismiss();

                        }
                    });

                }
            }else {
//                if (DBQueries.lists.size() == 0) {
//                    DBQueries.loadedCategoriesNames.add("HOME");
//
//
//                    DBQueries.loadFragmentData(getContext(), 0, loadingDialog, homepagerecyclerView, "home", "Semua Kategori", false);
//
//
//                } else {
//                    DBQueries.loadFragmentData(getContext(), 0, loadingDialog, homepagerecyclerView, "home", "Semua Kategori", false);
//
//                }

                loadingDialog.dismiss();
            }

        }else {
            homepagerecyclerView.setVisibility(View.GONE);
           // Glide.with(this).load(R.drawable.no_internet).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            maintanance.setVisibility(View.GONE);
            maintanance_text.setVisibility(View.GONE);
        }

        ////refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadPage(getContext(),"home","Semua Kategori",true);
            }
        });
        ////refresh layout

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage(getContext(),"home","Semua Kategori",true);
            }
        });



        return itemView;
    }

    public static void  reloadPage(Context context,String slug, String label,Boolean load){
        networkInfo = connectivityManager.getActiveNetworkInfo();

//        categoryModelList.clear();
          DBQueries.lists.clear();
        DBQueries.loadedCategoriesNames.clear();
//        DBqueries.clearData();

        if (networkInfo !=null && networkInfo.isConnected()==true && isOnline()) {

            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            homepagerecyclerView.setVisibility(View.VISIBLE);
            maintanance.setVisibility(View.GONE);
            maintanance_text.setVisibility(View.GONE);

            if (load){
                adapter = new HomePageAdapter(context,homePageModelFakeList,"");
                adapter.notifyDataSetChanged();
                homepagerecyclerView.setAdapter(adapter);
            }

            if (DBQueries.lists.size() == 0) {
                DBQueries.loadedCategoriesNames.add(label);


                if (load){
                    DBQueries.loadFragmentData(context, 0, loadingDialog, homepagerecyclerView, slug, label, false);
                }else {
                    DBQueries.loadFragmentData(context, 0, loadingDialog, homepagerecyclerView, slug, label, true);
                }



            } else {

                if (load){
                    DBQueries.loadFragmentData(context, 0, loadingDialog, homepagerecyclerView, slug, label, false);
                }else {
                    DBQueries.loadFragmentData(context, 0, loadingDialog, homepagerecyclerView, slug, label, true);
                }
            }


            UserPreference userPreference = new UserPreference(context);
            userPreference.setUserPreference("user",null);
            if (FirebaseAuth.getInstance().getCurrentUser() != null){


                if(userPreference.getUserPreference("user")==null){
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .build();

                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl(DBQueries.url)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create());

                    Retrofit retrofit = builder.build();
                    UserClient client = retrofit.create(UserClient.class);

                    Call<UserModel> call =    client.login(FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getUid());

                    call.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.isSuccessful()){

                                MainActivity.navView.setVisibility(View.VISIBLE);
                                UserModel userModel = response.body();
                                userPreference.setUserPreference("user", userModel);
                                DBQueries.loadCartList(context,loadingDialog,false,MainActivity.badgeCount,new TextView(context),false,null);
                                DBQueries.loadAddresses(context,loadingDialog,false,0,true);
                                DBQueries.loadRatingList(context,loadingDialog);
                                DBQueries.loadCountNotification(context,loadingDialog,MainActivity.notifyCount);


                            } else {
                                MainActivity.navView.setVisibility(View.GONE);
                                HomeFragment.maintance(false);

                                UserPreference userPreference = new UserPreference(context);
                                userPreference.setUserPreference("user", null);

                                loadingDialog.dismiss();

                            }

                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            MainActivity.navView.setVisibility(View.GONE);
                            HomeFragment.maintance(false);
                            Log.e("debug", "onFailure: ERROR > " + t.toString());

                            loadingDialog.dismiss();

                        }
                    });

                }
            }else {
                loadingDialog.dismiss();
            }

            swipeRefreshLayout.setRefreshing(false);


        }else {
            maintanance.setVisibility(View.GONE);
            maintanance_text.setVisibility(View.GONE);

            Toast.makeText(context,"Tidak Ada Sambungan Internet!",Toast.LENGTH_SHORT).show();
            homepagerecyclerView.setVisibility(View.GONE);
         //   Glide.with(getContext()).load(R.drawable.no_internet).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            MainActivity.navView.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            loadingDialog.dismiss();
        }
    }

    public static void  maintance(Boolean status){
        if (!status) {
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.VISIBLE);
            homepagerecyclerView.setVisibility(View.GONE);
            maintanance.setVisibility(View.VISIBLE);
            maintanance_text.setVisibility(View.VISIBLE);
        }
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 185.201.8.241");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadPage(getContext(),"home","Semua Kategori",true);

    }
}
