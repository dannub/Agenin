package com.agenin.id.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agenin.id.Adapter.CartAdapter;
import com.agenin.id.DBQueries;
import com.agenin.id.Model.CartItemModel;
import com.agenin.id.R;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class CartActivity extends AppCompatActivity {

    public static Dialog loadingDialog;
    public static Activity cartActivity;
    public static RecyclerView cartItemsRecyclerView;
    public static Button continueBtn;
    public static CartActivity mycartfragment;


    public  static CartAdapter cartlistAdapter;
    public static TextView totalAmount;
    public static Parcelable recyclerViewState;
    public static LinearLayoutManager linearLayoutManager;


    public static TextView noData;
    public static ConstraintLayout background;
    public static ImageView no_internet;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //loading dialog
        loadingDialog = new Dialog(CartActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog

        noData = findViewById(R.id.pesanan);
        background = findViewById(R.id.bg);
        no_internet = findViewById(R.id.no_internet_connection);
        swipeRefreshLayout = findViewById(R.id.swipe);
        connectivityManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();



        cartItemsRecyclerView = findViewById(R.id.cart_recycleview);
        continueBtn = findViewById(R.id.deliv_continue_btn);
        totalAmount = findViewById(R.id.total_cart_amount);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Keranjang");


        swipeRefreshLayout.setColorSchemeColors(this.getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimary));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadPage();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        reloadPage();





    }

    public void reloadPage(){
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo !=null && networkInfo.isConnected()==true ) {
            DBQueries.updateCartList(this, loadingDialog,true, MainActivity.badgeCount,totalAmount,false,null,true,false);

        }else {
            noData.setVisibility(View.GONE);
            background.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
            no_internet.setVisibility(View.VISIBLE);
            cartItemsRecyclerView.setVisibility(View.GONE);
        }

    }


    @Override
    public void onStart() {

        super.onStart();



        DBQueries.loadCartList(this, loadingDialog,true, MainActivity.badgeCount,totalAmount,false,null);



        if (DBQueries.cartItemModelList.size()==0){
            totalAmount.setText("Rp -");

        }else {
            if (DBQueries.cartItemModelList.get(DBQueries.cartItemModelList.size()-1).getType() == CartItemModel.TOTAL_AMOUNT){
                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
        }


    }
    @Override
    public void onBackPressed() {
        cartActivity = null;
        if (networkInfo !=null && networkInfo.isConnected()==true) {
            DBQueries.updateCartList(this, null,true, MainActivity.badgeCount,totalAmount,false,null,true,true);
        }
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            cartActivity = null;
            if (networkInfo !=null && networkInfo.isConnected()==true) {
                DBQueries.updateCartList(this, null,true, MainActivity.badgeCount,totalAmount,false,null,true,true);
            }
            finish();

//            if (fromCart){
//                linearLayoutManager = new LinearLayoutManager(DeliveryActivity.this);
//                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                MyCartFragment.cartItemsRecyclerView.setLayoutManager(linearLayoutManager);
//
//                linearLayoutManager.scrollToPosition(cartItemModelList.size() - 1);
//                MyCartFragment.cartlistAdapter.notifyDataSetChanged();
//            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}