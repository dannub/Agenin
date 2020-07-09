package com.agenin.id.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.agenin.id.Adapter.CartAdapter;
import com.agenin.id.Adapter.CartPaymentAdapter;
import com.agenin.id.DBQueries;
import com.agenin.id.Model.CartItemModel;
import com.agenin.id.R;

import java.util.ArrayList;
import java.util.List;


public class DeliveryActivity extends AppCompatActivity {

    public  static List<CartItemModel> cartItemModelList;
    public static RecyclerView deliveryRecycleView;
    private Button changeOraddNewAddressBtn;
    public static final int SELECT_ADDRESS = 0;
    public static TextView totalAmount;
    public static Activity deliveryActivity;
    private TextView fullname;
    private TextView fullAddress;
    private TextView pincode;
    public  static CartAdapter cartlistAdapter;
    public static Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private Button continueBtn;
    private Parcelable recyclerViewState;
    public  static LinearLayoutManager linearLayoutManager;
    private ImageButton bri,bca,bni,mandiri;
    private String name,mobileNo;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //loading dialog
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //loading dialog

        //loading dialog
        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Pengiriman");

        deliveryRecycleView = findViewById(R.id.delivery_recycleview);
        changeOraddNewAddressBtn = findViewById(R.id.change_or_add_address_btn);
        totalAmount = findViewById(R.id.total_cart_amount);
        fullname = findViewById(R.id.fullname);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        continueBtn = findViewById(R.id.deliv_continue_btn);






// Restore state

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecycleView.setLayoutManager(linearLayoutManager);

        recyclerViewState = deliveryRecycleView.getLayoutManager().onSaveInstanceState();
        deliveryRecycleView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        if (cartItemModelList.size()!=0) {
            linearLayoutManager.scrollToPosition(cartItemModelList.size() - 1);
        }
//        deliveryRecycleView.smoothScrollToPosition(0);
//        linearLayoutManager.scrollToPositionWithOffset(0, 0);

//        cartlistAdapter.notifyDataSetChanged();



        if (MainActivity.showCart){
            cartlistAdapter = new CartAdapter(DeliveryActivity.this,deliveryRecycleView,linearLayoutManager,cartItemModelList,false,loadingDialog,totalAmount,true);
            int totalItemPrice =0;
            int totalDeleveryItemPrice =0;
            int totalAmount;

            for (int x=0;x<DBQueries.cartItemModelList.size();x++){

                if (DBQueries.cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && DBQueries.cartItemModelList.get(x).getInStock()){
                    int qty = Integer.parseInt(String.valueOf(DBQueries.cartItemModelList.get(x).getProductQuantity()));

                    totalItemPrice = totalItemPrice + (Integer.parseInt(DBQueries.cartItemModelList.get(x).getProductPrice())* qty);
                    totalDeleveryItemPrice = totalDeleveryItemPrice +((int)(8000*DBQueries.cartItemModelList.get(x).getProductWeight())*qty);


                }
            }
            int totalDeliveryDiskon;
            if (totalItemPrice >= 200000 ){
                totalDeliveryDiskon = totalDeleveryItemPrice-30000;
            }else {
                totalDeliveryDiskon = totalDeleveryItemPrice;
            }

            totalAmount = totalItemPrice +totalDeliveryDiskon;
            DeliveryActivity.totalAmount.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(totalAmount)));

        }else {
            cartlistAdapter = new CartAdapter(DeliveryActivity.this,deliveryRecycleView,linearLayoutManager,cartItemModelList,false,loadingDialog,totalAmount,false);

            int totalItemPrice =0;
            int totalDeleveryItemPrice =0;
            int totalAmountText;
            for (int x=0;x< cartItemModelList.size();x++){

                if ( cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM &&  cartItemModelList.get(x).getInStock()){
                    int qty = Integer.parseInt(String.valueOf( cartItemModelList.get(x).getProductQuantity()));

                    totalItemPrice = totalItemPrice + (Integer.parseInt( cartItemModelList.get(x).getProductPrice())* qty);
                    totalDeleveryItemPrice = totalDeleveryItemPrice +((int)(8000* cartItemModelList.get(x).getProductWeight())*qty);


                }
            }
            if(totalItemPrice!=0){
                int totalDeliveryDiskon;
                if (totalItemPrice >= 200000 ){
                    totalDeliveryDiskon = totalDeleveryItemPrice-30000;
                }else {
                    totalDeliveryDiskon = totalDeleveryItemPrice;
                }

                totalAmountText = totalItemPrice +totalDeliveryDiskon;
                totalAmount.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(totalAmountText)));

            }else {
                totalAmount.setText("Rp 0");

            }
        }


        DeliveryActivity.cartlistAdapter.SetAdapter(DeliveryActivity.cartlistAdapter);
        DeliveryActivity.cartlistAdapter.SetTotalAmount(totalAmount);
        DeliveryActivity.deliveryRecycleView.setAdapter(DeliveryActivity.cartlistAdapter);
        DeliveryActivity.cartlistAdapter.notifyDataSetChanged();




        changeOraddNewAddressBtn.setVisibility(View.VISIBLE);

        changeOraddNewAddressBtn.setVisibility(View.VISIBLE);
        changeOraddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressIntent = new Intent(DeliveryActivity.this,MyAddressesActivity.class);
                myAddressIntent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(myAddressIntent);

            }
        });
        loadingDialog.dismiss();
        if (!cartItemModelList.isEmpty()) {

            continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deliveryActivity = DeliveryActivity.this;


                    if (DBQueries.addressModelList.size() == 0) {
                        DBQueries.loadAddresses(DeliveryActivity.this, loadingDialog,true,SELECT_ADDRESS,false);
                    } else {

                        paymentMethodDialog.show();
                        bri = paymentMethodDialog.findViewById(R.id.bri);
                        bni = paymentMethodDialog.findViewById(R.id.bni);
                        bca = paymentMethodDialog.findViewById(R.id.bca);
                        mandiri = paymentMethodDialog.findViewById(R.id.mandiri);

//                        bri.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                PaymentActivity.cartItemModelList = new ArrayList<>();
//                                PaymentActivity.cartItemModelList.clear();
//
//                                for (int x = 0; x < cartItemModelList.size(); x++) {
//                                    CartItemModel cartItemModel = cartItemModelList.get(x);
//                                    if (x!=cartItemModelList.size()-1){
//                                        if (cartItemModel.getInStock()) {
//                                            PaymentActivity.cartItemModelList.add(cartItemModel);
//                                        }
//                                    }else {
//                                        PaymentActivity.cartItemModelList.add(cartItemModel);
//                                    }
//
//                                }
//
//
//                                paymentMethodDialog.dismiss();
//                                Intent deliveryIntent = new Intent(DeliveryActivity.this, PaymentInfoActivity.class);
//                                deliveryIntent.putExtra("bank",1);
//                                startActivity(deliveryIntent);
//
//
//                            }
//                        });

                        bni.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PaymentActivity.cartItemModelList = new ArrayList<>();
                                PaymentActivity.cartItemModelList.clear();

                                for (int x = 0; x < cartItemModelList.size(); x++) {
                                    CartItemModel cartItemModel = cartItemModelList.get(x);
                                    if (x!=cartItemModelList.size()-1){
                                        if (cartItemModel.getInStock()) {
                                            PaymentActivity.cartItemModelList.add(cartItemModel);
                                        }
                                    }else {
                                        PaymentActivity.cartItemModelList.add(cartItemModel);
                                    }

                                }
//                                PaymentActivity.cartPaymentAdapter = new CartPaymentAdapter(PaymentActivity.cartItemModelList);


                                paymentMethodDialog.dismiss();
                                Intent deliveryIntent = new Intent(DeliveryActivity.this, PaymentInfoActivity.class);
                                deliveryIntent.putExtra("bank",2);
                                startActivity(deliveryIntent);
                            }
                        });
//
//                        bca.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                PaymentActivity.cartItemModelList = new ArrayList<>();
//                                PaymentActivity.cartItemModelList.clear();
//
//                                for (int x = 0; x < cartItemModelList.size(); x++) {
//                                    CartItemModel cartItemModel = cartItemModelList.get(x);
//                                    if (x!=cartItemModelList.size()-1){
//                                        if (cartItemModel.getInStock()) {
//                                            PaymentActivity.cartItemModelList.add(cartItemModel);
//                                        }
//                                    }else {
//                                        PaymentActivity.cartItemModelList.add(cartItemModel);
//                                    }
//
//                                }
//
////                                PaymentActivity.cartPaymentAdapter = new CartPaymentAdapter(PaymentActivity.cartItemModelList);
//
//
//                                paymentMethodDialog.dismiss();
//                                Intent deliveryIntent = new Intent(DeliveryActivity.this, PaymentInfoActivity.class);
//                                deliveryIntent.putExtra("bank",3);
//                                startActivity(deliveryIntent);
//                            }
//                        });

                        mandiri.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PaymentActivity.cartItemModelList = new ArrayList<>();
                                PaymentActivity.cartItemModelList.clear();

                                for (int x = 0; x < cartItemModelList.size(); x++) {
                                    CartItemModel cartItemModel = cartItemModelList.get(x);
                                    if (x!=cartItemModelList.size()-1){
                                        if (cartItemModel.getInStock()) {
                                            PaymentActivity.cartItemModelList.add(cartItemModel);
                                        }
                                    }else {
                                        PaymentActivity.cartItemModelList.add(cartItemModel);
                                    }

                                }

                          //      PaymentActivity.cartPaymentAdapter = new CartPaymentAdapter(PaymentActivity.cartItemModelList);


                                paymentMethodDialog.dismiss();
                                Intent deliveryIntent = new Intent(DeliveryActivity.this, PaymentInfoActivity.class);
                                deliveryIntent.putExtra("bank",4);
                                startActivity(deliveryIntent);
                            }
                        });


                    }

                }


            });



        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        name = DBQueries.addressModelList.get(DBQueries.selectedAddress).getNama();
        mobileNo = DBQueries.addressModelList.get(DBQueries.selectedAddress).getNo_telepon();
        if (DBQueries.addressModelList.get(DBQueries.selectedAddress).getNo_alternatif().equals("")){
            fullname.setText(name + " | "+mobileNo);
        }else {
            fullname.setText(name + " | "+mobileNo+" atau "+DBQueries.addressModelList.get(DBQueries.selectedAddress).getNo_alternatif());
        }
        String flatNo = DBQueries.addressModelList.get(DBQueries.selectedAddress).getProvinsi();
        String locality = DBQueries.addressModelList.get(DBQueries.selectedAddress).getKabupaten();
        String landmark = DBQueries.addressModelList.get(DBQueries.selectedAddress).getAlamat();
        String city = DBQueries.addressModelList.get(DBQueries.selectedAddress).getKecamatan();
        String state = DBQueries.addressModelList.get(DBQueries.selectedAddress).getNegara();
        if (landmark.equals("")) {
            fullAddress.setText(city+" "+locality +" "+flatNo+" "+state);
        }else {
            fullAddress.setText(landmark+" "+city+" "+locality+" "+flatNo+" "+state);
        }
        pincode.setText(DBQueries.addressModelList.get(DBQueries.selectedAddress).getKodepos());

    }

    @Override
    public void onBackPressed() {
        if (PaymentActivity.fromCart) {
            linearLayoutManager = new LinearLayoutManager(DeliveryActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            CartActivity.cartItemsRecyclerView.setLayoutManager(linearLayoutManager);
            linearLayoutManager.scrollToPosition(cartItemModelList.size() - 1);
            CartActivity.cartlistAdapter.notifyDataSetChanged();
            deliveryActivity = null;
        }
        ProductDetailActivity.productDetailsActivity = null;
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            deliveryActivity = null;
            finish();

            if (PaymentActivity.fromCart){
            linearLayoutManager = new LinearLayoutManager(DeliveryActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            CartActivity.cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

            linearLayoutManager.scrollToPosition(cartItemModelList.size() - 1);
            CartActivity.cartlistAdapter.notifyDataSetChanged();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
