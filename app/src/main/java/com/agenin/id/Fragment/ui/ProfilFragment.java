package com.agenin.id.Fragment.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.agenin.id.Activity.MainActivity;
import com.agenin.id.Activity.MyAddressesActivity;
import com.agenin.id.Activity.UpdateUserInfoActivity;
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.UserClient;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfilFragment extends Fragment {

    private FloatingActionButton settingBtn;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Button viewAllAddressBtn;
    public static final int MANAGE_ADDRESS =1;
    public static CircleImageView profileview;
    private TextView name,email;
    private LinearLayout layoutContainer,recentOrdersContainer;
    private Dialog loadingDialog;
    public static TextView addressname,address,pincode;
    private  Button signoutBtn;
    private ConnectivityManager connectivityManager;
    private TextView user_date;
    private NetworkInfo networkInfo;
    private FirebaseUser user;
    private ImageView dot;
    private TextView status;
    private UserModel userModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //loading dialog

        swipeRefreshLayout = view.findViewById(R.id.swipe);
        layoutContainer = view.findViewById(R.id.layout_container);
        profileview = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.fullname);
        email = view.findViewById(R.id.user_email);

        recentOrdersContainer = view.findViewById(R.id.recent_order_container);
        addressname = view.findViewById(R.id.address_fullname);
        address = view.findViewById(R.id.address);
        pincode = view.findViewById(R.id.address_pincode);
        signoutBtn = view.findViewById(R.id.sign_out_btn);
        settingBtn = view.findViewById(R.id.setting_btn);
        user_date =view.findViewById(R.id.user_date);
        dot= view.findViewById(R.id.dot);
        status= view.findViewById(R.id.status);

        user = FirebaseAuth.getInstance().getCurrentUser();


        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary));



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadPage();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
                FirebaseAuth.getInstance().signOut();
                DBQueries.clearData(getContext());
                MainActivity.mainActivity = null;
                MainActivity.currentFragment = -1;
                Intent mainActivityIntent = new Intent(getContext(), MainActivity.class);
                MainActivity.showCart = false;
                startActivity(mainActivityIntent);
                getActivity().finish();
                if (loadingDialog!=null) {
                    loadingDialog.dismiss();
                }

            }
        });

        layoutContainer.setVisibility(View.GONE);
        layoutContainer.getChildAt(1).setVisibility(View.GONE);

        viewAllAddressBtn = view.findViewById(R.id.view_all_addresses_btn);
        viewAllAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressIntent = new Intent(getContext(), MyAddressesActivity.class);
                myAddressIntent.putExtra("MODE", MANAGE_ADDRESS);
                startActivity(myAddressIntent);
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateUserInfo = new Intent(getContext(), UpdateUserInfoActivity.class);
                updateUserInfo.putExtra("Name",name.getText());
                updateUserInfo.putExtra("Email",email.getText());
                if (!userModel.getProfil().isEmpty()){
                    updateUserInfo.putExtra("Photo",DBQueries.url+userModel.getProfil());
                }else {
                    updateUserInfo.putExtra("Photo",userModel.getProfil());
                }
                UpdateUserInfoActivity.profilFragment= ProfilFragment.this;

                startActivity(updateUserInfo);
            }
        });


        reloadPage();
        return view;
    }

    private void reloadPage(){


        loadingDialog.show();
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        //DBqueries.loadOrders(getContext(), loadingDialog, null, null);
        connectivityManager =(ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo !=null && networkInfo.isConnected()==true ) {

            UserPreference userPreference = new UserPreference(getContext());
            UserPreference.setUserPreference("user", null);
            if (user != null) {


                if (UserPreference.getUserPreference("user") == null) {

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

                    Call<UserModel> call = client.login(user.getEmail(), user.getUid());

                    call.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.isSuccessful()) {

                                userModel = response.body();
                                UserPreference.setUserPreference("user", userModel);

                                DBQueries.loadAddresses(getContext(), loadingDialog, false, 0, false);

                                name.setText(userModel.getName());
                                email.setText(userModel.getEmail());
                                Date userDate = DBQueries.StringtoDate(userModel.getDate());

                                user_date.setText("Bergabung sejak : " + datetoString(userDate));
                                if (userModel.getStatus()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        dot.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorSuccess)));
                                    } else {
                                        dot.setVisibility(View.GONE);
                                    }
                                    status.setText(" Aktif");
                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        dot.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorPrimary)));
                                    } else {
                                        dot.setVisibility(View.GONE);
                                    }
                                    status.setText(" Menunggu Konfirmasi");
                                }
                                if (!userModel.getProfil().equals("")) {
                                    Glide.with(getContext()).load(DBQueries.url + userModel.getProfil()).apply(new RequestOptions().placeholder(R.drawable.profil2)).into(profileview);

                                } else {
                                    profileview.setImageDrawable(getContext().getResources().getDrawable(R.drawable.profil2));

                                }

                                if (!loadingDialog.isShowing()) {
                                    if (DBQueries.addressModelList.size() == 0) {
                                        addressname.setText("No Address");
                                        address.setText("-");
                                        pincode.setText("-");
                                    } else {
                                        setAddress();
                                    }

                                }

                                layoutContainer.setVisibility(View.VISIBLE);
                                layoutContainer.getChildAt(1).setVisibility(View.VISIBLE);

                            } else {

                                UserPreference userPreference = new UserPreference(getContext());
                                UserPreference.setUserPreference("user", null);

                                loadingDialog.dismiss();

                            }

                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            Log.e("debug", "onFailure: ERROR > " + t.toString());

                            loadingDialog.dismiss();

                        }
                    });

                }
            } else {
                loadingDialog.dismiss();
            }

            layoutContainer.setVisibility(View.VISIBLE);


        } else {
            loadingDialog.dismiss();
        }

    }

    public static void setAddress() {
        String name,mobileNo;
        name = DBQueries.addressModelList.get(DBQueries.selectedAddress).getNama();
        mobileNo = DBQueries.addressModelList.get(DBQueries.selectedAddress).getNo_telepon();
        if (DBQueries.addressModelList.get(DBQueries.selectedAddress).getNo_alternatif().equals("")){
            addressname.setText(name + " | "+mobileNo);
        }else {
            addressname.setText(name + " | "+mobileNo+" atau "+DBQueries.addressModelList.get(DBQueries.selectedAddress).getNo_alternatif());
        }
        String flatNo = DBQueries.addressModelList.get(DBQueries.selectedAddress).getProvinsi();
        String locality = DBQueries.addressModelList.get(DBQueries.selectedAddress).getKabupaten();
        String landmark = DBQueries.addressModelList.get(DBQueries.selectedAddress).getAlamat();
        String city = DBQueries.addressModelList.get(DBQueries.selectedAddress).getKecamatan();
        String state = DBQueries.addressModelList.get(DBQueries.selectedAddress).getNegara();
        if (landmark.equals("")) {
            address.setText(city+" "+locality + " "+flatNo+" "+" "+state);
        }else {
            address.setText(landmark+" "+city+" "+locality + " "+flatNo+" "+state);
        }
        pincode.setText(DBQueries.addressModelList.get(DBQueries.selectedAddress).getKodepos());

    }

    @Override
    public void onStart() {
        super.onStart();

        reloadPage();
//        loadingDialog.show();
//        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//
//            }
//        });
//        connectivityManager =(ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        networkInfo = connectivityManager.getActiveNetworkInfo();
//
//        if (networkInfo !=null && networkInfo.isConnected()==true ) {
//
//            UserPreference userPreference = new UserPreference(getContext());
//            userPreference.setUserPreference("user", null);
//            if (user != null) {
//
//
//                if (userPreference.getUserPreference("user") == null) {
//
//                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                            .readTimeout(60, TimeUnit.SECONDS)
//                            .connectTimeout(60, TimeUnit.SECONDS)
//                            .build();
//
//                    Retrofit.Builder builder = new Retrofit.Builder()
//                            .baseUrl(DBQueries.url)
//                            .client(okHttpClient)
//                            .addConverterFactory(GsonConverterFactory.create());
//
//                    Retrofit retrofit = builder.build();
//                    UserClient client = retrofit.create(UserClient.class);
//
//                    Call<UserModel> call = client.login(user.getEmail(), user.getUid());
//
//                    call.enqueue(new Callback<UserModel>() {
//                        @Override
//                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                            if (response.isSuccessful()) {
//
//                                userModel = response.body();
//                                userPreference.setUserPreference("user", userModel);
//
//                                DBQueries.loadAddresses(getContext(), loadingDialog, false, 0, false);
//
//                                name.setText(userModel.getName());
//                                email.setText(userModel.getEmail());
//                                Date userDate = DBQueries.StringtoDate(userModel.getDate());
//
//                                user_date.setText("Bergabung sejak : " + datetoString(userDate));
//                                if (userModel.getStatus()) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                        dot.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorSuccess)));
//                                    } else {
//                                        dot.setVisibility(View.GONE);
//                                    }
//                                    status.setText(" Aktif");
//                                } else {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                        dot.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorPrimary)));
//                                    } else {
//                                        dot.setVisibility(View.GONE);
//                                    }
//                                    status.setText(" Menunggu Konfirmasi");
//                                }
//                                if (!userModel.getProfil().equals("")) {
//                                    Glide.with(getContext()).load(DBQueries.url + userModel.getProfil()).apply(new RequestOptions().placeholder(R.drawable.profil2)).into(profileview);
//
//                                } else {
//                                    profileview.setImageDrawable(getContext().getResources().getDrawable(R.drawable.profil2));
//
//                                }
//
//                                if (!loadingDialog.isShowing()) {
//                                    if (DBQueries.addressModelList.size() == 0) {
//                                        addressname.setText("No Address");
//                                        address.setText("-");
//                                        pincode.setText("-");
//                                    } else {
//                                        setAddress();
//                                    }
//
//                                }
//
//                                layoutContainer.setVisibility(View.VISIBLE);
//                                layoutContainer.getChildAt(1).setVisibility(View.VISIBLE);
//
//                                reloadPage();
//
//                            } else {
//
//                                UserPreference userPreference = new UserPreference(getContext());
//                                userPreference.setUserPreference("user", null);
//
//                                loadingDialog.dismiss();
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserModel> call, Throwable t) {
//                            Log.e("debug", "onFailure: ERROR > " + t.toString());
//
//                            loadingDialog.dismiss();
//
//                        }
//                    });
//
//                }
//            } else {
//                loadingDialog.dismiss();
//            }
//        }else {
//            loadingDialog.dismiss();
//        }
    }

    private String datetoString (Date date){


        String format = "MMMyyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(format);

        String hasil = sdf.format(date);

        String bulan=hasil.substring(0,3);
        if(bulan.equals("Jan")){
            bulan = "Januari";
        }else if(bulan.equals("Feb")){
            bulan = "Februari";
        }else if(bulan.equals("Mar")){
            bulan = "Maret";
        }else if(bulan.equals("Apr")){
            bulan = "April";
        }else if(bulan.equals("May")){
            bulan = "Mei";
        }else if(bulan.equals("Jun")){
            bulan = "Juni";
        }else if(bulan.equals("Jul")){
            bulan = "Juli";
        }else if(bulan.equals("Agt")){
            bulan = "Agustus";
        }else if(bulan.equals("Sep")){
            bulan = "September";
        }else if(bulan.equals("Okt")){
            bulan = "Oktober";
        }else if(bulan.equals("Nov")){
            bulan = "November";
        }else if(bulan.equals("Des")){
            bulan = "Desember";
        }
        hasil = bulan+" "+hasil.substring(3);
        return hasil;
    }

//    public static boolean isOnline() {
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 185.201.8.241");
//            int     exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        }
//        catch (IOException e)          { e.printStackTrace(); }
//        catch (InterruptedException e) { e.printStackTrace(); }
//
//        return false;
//    }


}
