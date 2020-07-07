package com.agenin.id.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agenin.id.DBQueries;
import com.agenin.id.Fragment.ui.FavoriteFragment;
import com.agenin.id.Fragment.ui.HomeFragment;
import com.agenin.id.Fragment.ui.MyOrdersFragment;
import com.agenin.id.Fragment.ui.ProfilFragment;
import com.agenin.id.Fragment.ui.SignInFragment;
import com.agenin.id.Fragment.ui.SignUpFragment;
import com.agenin.id.Fragment.ui.TutorialFragment;
import com.agenin.id.Interface.UserClient;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;
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


public class MainActivity extends AppCompatActivity
{


    private int mMenuId;
    public static Boolean showCart=false;
    public static int currentFragment= -1;
    private static Window window;
    public static Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout frameLayout;

    public   static final  int HOME_FRAGMENT = 0;
    private  static final  int ORDER_FRAGMENT = 1;
    private  static final  int WISHLIST_FRAGMENT = 2;
    private  static final  int MYACCOUNT_FRAGMENT = 3;
    private  static final  int VIDEO_FRAGMENT = 4;

    private AppBarLayout.LayoutParams params;


    private ImageView actionBarLogo;
    public static MainActivity mainActivity;


    private Dialog loadingDialog;
    private FirebaseAuth firebaseAuth;
    public  static   BottomNavigationView navView;
    public static TextView badgeCount;
    public static TextView notifyCount;
    private ImageView whatsapp;

//    private AppBarLayout.LayoutParams params;

    private FirebaseUser user;
    private Dialog signInDialog;
    private  int scrollFlags;
    //Notification
    public   static  final int NOTIFY_ID = 0;
    public   static  final String CHANNEL_ID = "Agenin";
    public   static  final String CHANNEL_NAME = "Agenin";
    private  static  final String CHANNEL_DESC = "Agenin Notification";

    private Boolean isHome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       navView = (BottomNavigationView) findViewById(R.id.nav_view);
        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        actionBarLogo = findViewById(R.id.actionbar_logo);
        setSupportActionBar(toolbar);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();
//
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar = findViewById(R.id.toolbar);
//        actionBarLogo = findViewById(R.id.actionbar_logo);
//        setSupportActionBar(toolbar);
//
//        window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();

        //loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        }
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        whatsapp = findViewById(R.id.whatsapp);
        //loading dialog
         user = firebaseAuth.getCurrentUser();

//        UserPreference userPreference = new UserPreference(this);
//        userPreference.setUserPreference("user",null);
//        if (user!=null){
//
//            if(userPreference.getUserPreference("user")==null){
//                DBQueries.requestLogin(this,user.getEmail(),user.getUid(),loadingDialog,false);
//            }
//        }else {
//            loadingDialog.dismiss();
//        }



        signInDialog = new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);

        final Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity = MainActivity.this;
                SignInFragment.disableCloseBtn = false;
                SignUpFragment.disableCloseBtn = false;
                signInDialog.dismiss();
                RegisterActivity.setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity = MainActivity.this;
                SignInFragment.disableCloseBtn = false;
                SignUpFragment.disableCloseBtn = false;
                signInDialog.dismiss();
                RegisterActivity.setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noWA = "085746134241";

                if (noWA.substring(0,1).equals("0")){
                    noWA="+62"+noWA.substring(1,noWA.length());
                }
                String url = "https://api.whatsapp.com/send?phone=" + noWA;
                try {
                    PackageManager pm = getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Anda Belum menginstall WhatsApp", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        navView.setOnNavigationItemSelectedListener(navListener);
        navView.setSelectedItemId(R.id.navigation_home);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_tutorial, R.id.navigation_favorite,R.id.navigation_myorders,R.id.navigation_profil)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//
////      NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("Permission","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener=
           new BottomNavigationView.OnNavigationItemSelectedListener(){

               @Override
               public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                   Fragment setectedFragment= null;

                   switch (item.getItemId()) {
                       case R.id.navigation_home:
                           invalidateOptionsMenu();

                           isHome =true;
                           params.setScrollFlags(scrollFlags);
                            currentFragment = HOME_FRAGMENT;
                           setectedFragment = new HomeFragment();
                           getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.left_to_right).replace(R.id.nav_host_fragment,setectedFragment).commit();
                           whatsapp.setVisibility(View.VISIBLE);
                           break;
                       case R.id.navigation_tutorial:
                           invalidateOptionsMenu();
                           isHome =false;

                           params.setScrollFlags(scrollFlags);
                           currentFragment = VIDEO_FRAGMENT;
                           setectedFragment = new TutorialFragment();
                           getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.left_to_right).replace(R.id.nav_host_fragment,setectedFragment).commit();
                           whatsapp.setVisibility(View.VISIBLE);
                           break;
                       case R.id.navigation_favorite:

                           isHome =false;
                           params.setScrollFlags(scrollFlags);

                           currentFragment = WISHLIST_FRAGMENT;
                           if (user !=null) {
                               invalidateOptionsMenu();
                               MenuItem menuItem = navView.getMenu().findItem(item.getItemId());
                               menuItem.setCheckable(true);
                               setectedFragment = new FavoriteFragment();
                               getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.left_to_right).replace(R.id.nav_host_fragment,setectedFragment).commit();

                           }else {
                               MenuItem menuItem = navView.getMenu().findItem(item.getItemId());
                               menuItem.setCheckable(false);
                               signInDialog.show();


                           }
                           whatsapp.setVisibility(View.VISIBLE);
                           break;
                       case R.id.navigation_myorders:
                           isHome =false;
                           params.setScrollFlags(scrollFlags);
                           currentFragment = ORDER_FRAGMENT;
                           if (user !=null) {
                               invalidateOptionsMenu();
                               MenuItem menuItem = navView.getMenu().findItem(item.getItemId());
                               menuItem.setCheckable(true);
                               setectedFragment = new MyOrdersFragment();
                               getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.left_to_right).replace(R.id.nav_host_fragment,setectedFragment).commit();

                           }else {
                               MenuItem menuItem = navView.getMenu().findItem(item.getItemId());
                               menuItem.setCheckable(false);
                               signInDialog.show();

                           }
                           whatsapp.setVisibility(View.GONE);
                           break;
                       case R.id.navigation_profil:
                           isHome =false;

                           params.setScrollFlags(scrollFlags);
                           currentFragment = MYACCOUNT_FRAGMENT;
                           if (user !=null) {
                               invalidateOptionsMenu();
                               MenuItem menuItem = navView.getMenu().findItem(item.getItemId());
                               menuItem.setCheckable(true);
                               setectedFragment = new ProfilFragment();
                               getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.left_to_right).replace(R.id.nav_host_fragment,setectedFragment).commit();

                           }else {
                               MenuItem menuItem = navView.getMenu().findItem(item.getItemId());
                               menuItem.setCheckable(false);
                               signInDialog.show();

                           }
                           whatsapp.setVisibility(View.VISIBLE);
                           break;
                   }
                  return true;
               }
           };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFragment == HOME_FRAGMENT){

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getMenuInflater().inflate(R.menu.main,menu);

            MenuItem cartItem = menu.findItem(R.id.main_chart_icon);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.shop);


            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user ==null) {
                        signInDialog.show();
                    }else {
                        showCart = true;
                        mainActivity = MainActivity.this;
                        Intent cartIntent = new Intent(MainActivity.this,CartActivity.class);
                        startActivity(cartIntent);

                    } }
            });

            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

            loadingDialog.show();
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
            UserPreference userPreference = new UserPreference(this);
            userPreference.setUserPreference("user",null);
            if (user!=null){


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

                    Call<UserModel> call =    client.login(user.getEmail(), user.getUid());

                    call.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.isSuccessful()){

                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                    @Override
                                    public void onSuccess(InstanceIdResult instanceIdResult) {
                                        String newToken = instanceIdResult.getToken();

                                        getPreferences(Context.MODE_PRIVATE).edit().putString("fb", newToken).apply();
                                        UserPreference userPreference = new UserPreference(MainActivity.this);
                                        UserModel user = userPreference.getUserPreference("user");

                                        Gson gson = new GsonBuilder()
                                                .setLenient()
                                                .create();
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

                                        UserClient client = retrofit.create(UserClient.class);
                                        Call<ResponseBody> call2 = client.updateToken(response.body().getId(), newToken);

                                        call2.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (!response.isSuccessful()) {
                                                    HomeFragment.maintance(true);
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_SHORT).show();
                                                    return;
                                                }


                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                HomeFragment.maintance(true);
                                                loadingDialog.dismiss();
                                                Log.e("debug", "onFailure: ERROR > " + t.toString());


                                            }
                                        });


                                    }
                                });

                                UserModel userModel = response.body();

                                userPreference.setUserPreference("user", userModel);
//                                DBQueries.loadAddresses(MainActivity.this,loadingDialog,false,0,true);
//                                DBQueries.loadCartList(MainActivity.this,loadingDialog,false,badgeCount,new TextView(MainActivity.this),false,null);
//                                DBQueries.loadRatingList(MainActivity.this,loadingDialog);
                                DBQueries.loadCountNotification(MainActivity.this,loadingDialog,notifyCount);
                                //    DBqueries.checkNotifications(MainActivity.this,false,notifyCount);

                            } else {


                                UserPreference userPreference = new UserPreference(MainActivity.this);
                                userPreference.setUserPreference("user", null);
                                HomeFragment.maintance(false);
                                loadingDialog.dismiss();

                            }

                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            HomeFragment.maintance(false);
                            loadingDialog.dismiss();
                            Log.e("debug", "onFailure: ERROR > " + t.toString());


                        }
                    });

                }
            }else {
                loadingDialog.dismiss();
            }


            MenuItem notificationItem = menu.findItem(R.id.main_notification_icon);
            notificationItem.setActionView(R.layout.badge_layout);
            ImageView notifyIcon = notificationItem.getActionView().findViewById(R.id.badge_icon);
            notifyIcon.setImageResource(R.drawable.notification);
            notifyCount = notificationItem.getActionView().findViewById(R.id.badge_count);


            notificationItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user ==null) {
                        signInDialog.show();
                    }else {
                        Intent notificationIntent = new Intent(MainActivity.this,NotificationActivity.class);
                        startActivity(notificationIntent);
                    } }
            });




        }
        return true;
    }

    @Override
    public void onBackPressed() {

      if (!isHome||currentFragment!=HOME_FRAGMENT){
          params.setScrollFlags(scrollFlags);
          currentFragment = HOME_FRAGMENT;
          Fragment setectedFragment = new HomeFragment();
          getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.left_to_right).replace(R.id.nav_host_fragment,setectedFragment).commit();

      }
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.search_icon){
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);

            return true;
        }
        else if(id ==R.id.main_notification_icon) {
            Intent notificationIntent = new Intent(this, NotificationActivity.class);
            startActivity(notificationIntent);
            return true;
        }
        else if(id ==R.id.main_chart_icon){
            if (user ==null) {
                signInDialog.show();
            }else {
                mainActivity = MainActivity.this;
                Intent cartIntent = new Intent(this,CartActivity.class);
                startActivity(cartIntent);

            }
            return true;
        }
//        else  if (id == android.R.id.home){
//            MyCartFragment.mycartfragment = null;
//
//
//            if(drawer.isDrawerOpen(GravityCompat.START)){
//                drawer.closeDrawer(GravityCompat.START);
//            }else {
//                if (currentFragment==HOME_FRAGMENT){
//                    getSupportActionBar().setDisplayShowTitleEnabled(false);
//                    currentFragment = -1;
//                    super.onBackPressed();
//
//                }else if (currentFragment==CART_FRAGMENT){
//
//                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_framelayout);
//
//                    if ((fragment instanceof IOnBackPressed) || ((IOnBackPressed) fragment).onBackPressed()) {
//                        if (showCart){
//                            mainActivity = null;
//                            showCart = false;
//                            MyCartFragment.mycartfragment = null;
//                            getSupportActionBar().setDisplayShowTitleEnabled(true);
//                            currentFragment = -1;
//                            finish();
//                        }else {
//                            actionBarLogo.setVisibility(View.VISIBLE);
//                            invalidateOptionsMenu();
//                            setFragment(new HomeFragment(), HOME_FRAGMENT);
//                            getSupportActionBar().setDisplayShowTitleEnabled(false);
//                            navigationView.getMenu().getItem(0).setChecked(true);
//                        }
//                    }
//                } else {
//                    if (showCart){
//                        MyCartFragment.mycartfragment = null;
//                        mainActivity = null;
//                        showCart = false;
//                        getSupportActionBar().setDisplayShowTitleEnabled(true);
//                        currentFragment = -1;
//                        finish();
//                        super.onBackPressed();
//                    }
//
//                    else {
//                        actionBarLogo.setVisibility(View.VISIBLE);
//                        invalidateOptionsMenu();
//                        setFragment(new HomeFragment(), HOME_FRAGMENT);
//                        getSupportActionBar().setDisplayShowTitleEnabled(false);
//                        navigationView.getMenu().getItem(0).setChecked(true);
//                    }
//                }
//
//            }

            return super.onOptionsItemSelected(item);
    }

    public void loadCart(){
        invalidateOptionsMenu();
    }
}
