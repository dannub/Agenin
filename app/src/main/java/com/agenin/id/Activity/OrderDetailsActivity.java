package com.agenin.id.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agenin.id.Adapter.CartPaymentAdapter;
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.CartClient;
import com.agenin.id.Interface.NotaClient;
import com.agenin.id.Model.Api.OrderDetailsAPI;
import com.agenin.id.Model.Api.OrderUserAPI;
import com.agenin.id.Model.CartInputModel;
import com.agenin.id.Model.CartItemModel;
import com.agenin.id.Model.MyOrderItemModel;
import com.agenin.id.Model.NotaItemModel;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
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

public class OrderDetailsActivity extends AppCompatActivity {

    private String product_id,nota_id;
    private MyOrderItemModel myOrderDetail;
    private List<NotaItemModel> notaItemModels;
    private List<CartItemModel> cartInputModelList;

    //wighet
    private TextView productTitle;
    private TextView productPrice;
    private TextView productQuantity;
    private ImageView productImage;
    private TextView satuan;
    private TextView satuan2;

    private ImageView orderedIndikator;
    private TextView orderedTitle;
    private TextView orderedDate;
    private TextView orderedBody;
    private TextView canceledOrdered;

    private ProgressBar orderedPackedProgressBar;

    private ImageView packedIndikator;
    private TextView packedTitle;
    private TextView packedDate;
    private TextView canceledPacked;

    private ProgressBar packedShippingProgressBar;

    private ImageView shippingIndikator;
    private TextView shippingTitle;
    private TextView shippingDate;
    private TextView shippingBody;
    private TextView detailShipping;

    private Button copyResi;

    private ProgressBar shippingDeliveredProgressBar;

    private ImageView deliveredIndikator;
    private TextView deliveredTitle;
    private TextView deliveredDate;
    private TextView deliveredBody;


    private LinearLayout rateNowContainer;

    public static TextView fullname;
    public static TextView fullAddress;
    public static TextView pincode;

    public static final int SELECT_ADDRESS_ORDER = 1;

    private Button changeOraddNewAddressBtn;
    private Button Call;
    private Button cancel;
    private Button delivered;

    private Dialog loadingDialog;

    private int initialRating = -1;


    private TextView totalItemsText;
    private TextView totalItemPriceText;
    private TextView deliveryPriceText;
    private TextView totalAmountText;
    private TextView saveAmountText;
    private TextView diskonText;
    private TextView notaText;

    private int saveAmount;
    private int totalItemPrice;
    private int totalAmount;
    private Button hapus;

    private static TextView idPayment;
    private static TextView totalBayar;
    private static TextView waktuPesan;
    private static TextView alamat;
    private static TextView bank_tv;
    private static TextView status;

    public static CartPaymentAdapter cartPaymentAdapter;
    public static RecyclerView recyclerViewItem;

    private FirebaseFirestore firebaseFirestore ;
    private Button download;

    private String dirpath;
    private String now;
    private static final int REQUEST = 112;
    private static ConstraintLayout notaPaymentLayout;

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Rincian Pesanan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //loading dialog

        productTitle= findViewById(R.id.product_title);
        productPrice = findViewById(R.id.product_price);
        productQuantity = findViewById(R.id.product_quantity);
        productImage = findViewById(R.id.product_image);

        //Ordered
        orderedIndikator= findViewById(R.id.ordered_indicator);
        orderedTitle= findViewById(R.id.ordered_title);
        orderedDate= findViewById(R.id.ordered_date);
        orderedBody = findViewById(R.id.ordered_body);

        orderedPackedProgressBar = findViewById(R.id.ordered_packed_progressbar);

        canceledOrdered = findViewById(R.id.cancel_konfirmasi);

        firebaseFirestore = FirebaseFirestore.getInstance();

        hapus = findViewById(R.id.hapus);
        satuan = findViewById(R.id.satuan);
        satuan2 = findViewById(R.id.satuan2);

        //Packed
        packedIndikator= findViewById(R.id.packed_indicator);
        packedTitle= findViewById(R.id.packed_title);
        packedDate= findViewById(R.id.packed_date);

        packedShippingProgressBar = findViewById(R.id.packed_shipping_progressbar);

        canceledPacked = findViewById(R.id.cancel_dikemas);

        //Shipping
        shippingIndikator= findViewById(R.id.shipping_indicator);
        shippingTitle= findViewById(R.id.shipping_title);
        shippingDate= findViewById(R.id.shipping_date);
        shippingBody = findViewById(R.id.shipping_body);

        shippingDeliveredProgressBar = findViewById(R.id.shipping_delivered_progressbar);

        detailShipping = findViewById(R.id.detail_shiping);

        copyResi = findViewById(R.id.copy_resi);

        notaText = findViewById(R.id.nota);

        cartInputModelList = new ArrayList<>();
        //Delivered
        deliveredIndikator= findViewById(R.id.delivered_indicator);
        deliveredTitle= findViewById(R.id.delivered_title);
        deliveredDate= findViewById(R.id.delivered_date);
        deliveredBody = findViewById(R.id.delivered_body);

        rateNowContainer = findViewById(R.id.rate_now_container);

        cancel= findViewById(R.id.cancel_order);

        fullname = findViewById(R.id.fullname);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);

        changeOraddNewAddressBtn = findViewById(R.id.change_or_add_address_btn);

        totalItemsText = findViewById(R.id.total_items);
        totalItemPriceText = findViewById(R.id.total_items_price);
        deliveryPriceText = findViewById(R.id.delivery_price);
        totalAmountText = findViewById(R.id.total_price);
        saveAmountText = findViewById(R.id.saved_amount);
        diskonText = findViewById(R.id.diskon);

        Call = findViewById(R.id.call);
        delivered = findViewById(R.id.pesanan_sampai);

        idPayment = findViewById(R.id.id);
        totalBayar = findViewById(R.id.totalbayar);
        waktuPesan = findViewById(R.id.waktupesan);
        alamat = findViewById(R.id.alamat);
        bank_tv = findViewById(R.id.bank);
        status = findViewById(R.id.status);
        recyclerViewItem = findViewById(R.id.item_recycleview);

        notaPaymentLayout = findViewById(R.id.notalayout);

        download = findViewById(R.id.download);

        connectivityManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();



     //   DBqueries.loadOrders(OrderDetailsActivity.this,loadingDialog,MyOrdersFragment.layoutManager,MyOrdersFragment.myOrderRecycleView);



        nota_id = getIntent().getStringExtra("nota_id");
        product_id = getIntent().getStringExtra("product_id");


        reloadPage();



    }

    private String changeDate(Date date){
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
        String day = sdf2.format(date);
        return day;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setProgress(){
        String format = "dd-MM-yyyy HH:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString="";
        initProgress();
        if (myOrderDetail.getConfirmed()){
            orderedDate.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            orderedDate.setText(changeDate(DBQueries.StringtoDate(myOrderDetail.getConfirmed_date())));
            orderedIndikator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));
            orderedBody.setVisibility(View.VISIBLE);




            dateString = sdf.format(DBQueries.StringtoDate(myOrderDetail.getConfirmed_date()));
            waktuPesan.setText(dateString);
            status.setText("Telah Dikonfirmasi");


            if (myOrderDetail.getPacked()){
                orderedPackedProgressBar.setVisibility(View.VISIBLE);
                orderedPackedProgressBar.setProgress(100);
                cancel.setVisibility(View.VISIBLE);
                packedDate.setVisibility(View.VISIBLE);
                packedDate.setText(changeDate(DBQueries.StringtoDate(myOrderDetail.getPacked_date())));
                packedIndikator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));

                 dateString = sdf.format(DBQueries.StringtoDate(myOrderDetail.getPacked_date()));
                waktuPesan.setText(dateString);
                status.setText("Telah Dikemas");

                if (myOrderDetail.getShipped()){
                    packedShippingProgressBar.setVisibility(View.VISIBLE);
                    packedShippingProgressBar.setProgress(100);
                    shippingDate.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.GONE);
                    shippingBody.setVisibility(View.VISIBLE);
                    shippingDate.setText(changeDate(DBQueries.StringtoDate(myOrderDetail.getShipped_date())));
                    shippingIndikator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));

                    delivered.setVisibility(View.VISIBLE);
                    detailShipping.setText(myOrderDetail.getMetode_kirim()+" Resi : "+myOrderDetail.getKet_kirim());
                    detailShipping.setVisibility(View.VISIBLE);
                    copyResi.setVisibility(View.VISIBLE);

                    dateString = sdf.format(DBQueries.StringtoDate(myOrderDetail.getShipped_date()));
                    waktuPesan.setText(dateString);
                    status.setText("Sedang Dikirim");

                    if (myOrderDetail.getDelivered()){
                        shippingDeliveredProgressBar.setVisibility(View.VISIBLE);
                        shippingDeliveredProgressBar.setProgress(100);
                        deliveredBody.setVisibility(View.VISIBLE);
                        deliveredDate.setVisibility(View.VISIBLE);
                        hapus.setVisibility(View.VISIBLE);
                        delivered.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                        deliveredDate.setText(changeDate(DBQueries.StringtoDate(myOrderDetail.getDelivered_date())));
                        deliveredIndikator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));
                        dateString = sdf.format(DBQueries.StringtoDate(myOrderDetail.getDelivered_date()));
                        waktuPesan.setText(dateString);
                        status.setText("Telah Dikirim");
                    }
                }
            }else {
                cancel.setVisibility(View.VISIBLE);
                if (myOrderDetail.getCanceled()){
                    dateString = sdf.format(DBQueries.StringtoDate(myOrderDetail.getCanceled_date()));
                    waktuPesan.setText(dateString);
                    status.setText("Telah Dibatalkan");
                    canceledPacked.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.GONE);
                    hapus.setVisibility(View.VISIBLE);
                    canceledPacked.setText("Cancel Order \n "+changeDate(DBQueries.StringtoDate(myOrderDetail.getConfirmed_date())));
                }
            }

        }else {
            dateString = sdf.format(DBQueries.StringtoDate(myOrderDetail.getOrdered_date()));
            waktuPesan.setText(dateString);
            status.setText("Belum Dikonfirmasi");
            cancel.setVisibility(View.VISIBLE);
            if (myOrderDetail.getCanceled()){
                dateString = sdf.format(DBQueries.StringtoDate(myOrderDetail.getCanceled_date()));
                waktuPesan.setText(dateString);
                status.setText("Telah Dibatalkan");
                canceledPacked.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);
                hapus.setVisibility(View.VISIBLE);
                canceledPacked.setText("Cancel Order \n "+changeDate(DBQueries.StringtoDate(myOrderDetail.getConfirmed_date())));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initProgress(){
        orderedPackedProgressBar.setVisibility(View.INVISIBLE);
        packedShippingProgressBar.setVisibility(View.INVISIBLE);
        shippingDeliveredProgressBar.setVisibility(View.INVISIBLE);

        orderedDate.setVisibility(View.GONE);
        orderedIndikator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
        orderedBody.setVisibility(View.GONE);
        orderedPackedProgressBar.setProgress(0);
        packedDate.setVisibility(View.GONE);
        packedIndikator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
        packedShippingProgressBar.setProgress(0);
        shippingDate.setVisibility(View.GONE);
        shippingIndikator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
        shippingBody.setVisibility(View.GONE);
        shippingDeliveredProgressBar.setProgress(0);
        deliveredDate.setVisibility(View.GONE);
        deliveredIndikator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
        deliveredBody.setVisibility(View.GONE);
        copyResi.setVisibility(View.GONE);
        canceledOrdered.setVisibility(View.GONE);
        detailShipping.setVisibility(View.GONE);
        delivered.setVisibility(View.GONE);
        hapus.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
           // DBqueries.loadOrders(MyOrdersFragment.context,MyOrdersFragment.loadingDialog,MyOrdersFragment.layoutManager, MyOrdersFragment.myOrderRecycleView);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
       // DBqueries.loadOrders(MyOrdersFragment.context,MyOrdersFragment.loadingDialog,MyOrdersFragment.layoutManager, MyOrdersFragment.myOrderRecycleView);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            reloadPage();
           // DBqueries.loadOrders(OrderDetailsActivity.this, loadingDialog, MyOrdersFragment.layoutManager, MyOrdersFragment.myOrderRecycleView);
        }
    }

    public void reloadPage() {
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            if (loadingDialog != null) {
                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
            }



            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            UserPreference userPreference = new UserPreference(OrderDetailsActivity.this);
            UserModel user = userPreference.getUserPreference("user");

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
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

            NotaClient notaApi = retrofit.create(NotaClient.class);
            Call<OrderDetailsAPI> call = notaApi.getMyOrderDetails(user.getId(),nota_id,product_id);
            call.enqueue(new Callback<OrderDetailsAPI>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(Call<OrderDetailsAPI> call, Response<OrderDetailsAPI> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(OrderDetailsActivity.this,response.code(),Toast.LENGTH_SHORT).show();
                        if (loadingDialog!=null) {
                            loadingDialog.dismiss();
                        }
                        return;
                    }



                    myOrderDetail = response.body().getItems().getMessage();
                    notaItemModels= response.body().getItems().getNota();



                    setProgress();

                    productTitle.setText(myOrderDetail.getItems().getProduct_ID().getTitle_product());
                    productPrice.setText("Rp "+DBQueries.currencyFormatter(myOrderDetail.getItems().getProduct_ID().getPrice()));



                    if (!myOrderDetail.getItems().getProduct_ID().getSatuan().equals("")) {
                        satuan.setVisibility(View.VISIBLE);
                        satuan.setText("/" + myOrderDetail.getItems().getProduct_ID().getSatuan());
                        satuan2.setVisibility(View.VISIBLE);
                        satuan2.setText(myOrderDetail.getItems().getProduct_ID().getSatuan());
                    } else {
                        satuan.setVisibility(View.GONE);
                        satuan2.setVisibility(View.GONE);
                    }


                    int totalItem=0;
                    cartInputModelList = new ArrayList<>();
                    for (int i=0;i<notaItemModels.size();i++)
                    {
                        totalItem = totalItem+notaItemModels.get(i).getJumlah();
                        Double berat = Double.parseDouble(String.valueOf(notaItemModels.get(i).getProduct_ID().getBerat()));
                        int ongkir =  ((int)(8000*berat)*(int) notaItemModels.get(i).getJumlah());
                        cartInputModelList.add(new CartItemModel(
                                CartItemModel.CART_ITEM,
                                notaItemModels.get(i).getProduct_ID().get_id(),
                                DBQueries.url +  notaItemModels.get(i).getProduct_ID().getImage().get(0)
                                ,  notaItemModels.get(i).getProduct_ID().getTitle_product()
                                ,  notaItemModels.get(i).getProduct_ID().getPrice()
                                , (int)  notaItemModels.get(i).getJumlah()
                                , true
                                , notaItemModels.get(i).getProduct_ID().getAverage_rating()
                                , notaItemModels.get(i).getProduct_ID().getSatuan()
                                , berat
                                , (int) notaItemModels.get(i).getJumlah()
                                ,String.valueOf(ongkir)
                        ));
                    }
                    cartInputModelList.add(new CartItemModel(
                            CartItemModel.TOTAL_AMOUNT));

                    cartPaymentAdapter = new CartPaymentAdapter(cartInputModelList);


                    recyclerViewItem.setAdapter(cartPaymentAdapter);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderDetailsActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewItem.setLayoutManager(linearLayoutManager);

                    cartPaymentAdapter.notifyDataSetChanged();

                    productQuantity.setText("Jumlah :"+myOrderDetail.getItems().getJumlah());
                    Glide.with(OrderDetailsActivity.this).load(DBQueries.url+myOrderDetail.getItems().getProduct_ID().getImage().get(0)).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(productImage);


                    fullname.setText(myOrderDetail.getName());
                    fullAddress.setText(myOrderDetail.getFull_address());
                    pincode.setText(myOrderDetail.getKode_pos());

                    changeOraddNewAddressBtn.setVisibility(View.GONE);



                    totalItemsText.setText("Harga ("+myOrderDetail.getItems().getJumlah()+" barang)");
                    deliveryPriceText.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(((int)(8000* myOrderDetail.getItems().getProduct_ID().getBerat())*myOrderDetail.getItems().getJumlah()))));

                    totalItemPrice = Integer.parseInt( myOrderDetail.getItems().getProduct_ID().getPrice())* myOrderDetail.getItems().getJumlah();

                    saveAmount =0;
                    if (totalItemPrice >= 200000 ) {
                        saveAmount = 30000;
                    }
                    totalAmount =totalItemPrice+((int)(8000* myOrderDetail.getItems().getProduct_ID().getBerat())*myOrderDetail.getItems().getJumlah())-saveAmount;



                    totalItemPriceText.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(totalItemPrice)));
                    totalAmountText.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(totalAmount)));
                    saveAmountText.setText("Kamu Hemat Rp " + DBQueries.currencyFormatter(String.valueOf(saveAmount)) + " di item pesanan ini");
                    diskonText.setText("- Rp "+DBQueries.currencyFormatter(String.valueOf(saveAmount)));

                    Call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String noWA = myOrderDetail.getItems().getProduct_ID().getNo_pedagang();

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
                                Toast.makeText(OrderDetailsActivity.this, "Anda Belum menginstall WhatsApp", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                        }
                    });

                    delivered.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new SweetAlertDialog(OrderDetailsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Terima Kasih")
                                    .setContentText("Pastikan pesanan benar-benar sampai di lokasi?")
                                    .setConfirmText("Ya")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(final SweetAlertDialog sweetAlertDialog) {

                                            loadingDialog.show();
                                            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {

                                                }
                                            });
                                            Gson gson = new GsonBuilder()
                                                    .setLenient()
                                                    .create();

                                            UserPreference userPreference = new UserPreference(OrderDetailsActivity.this);
                                            UserModel user = userPreference.getUserPreference("user");

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

                                            NotaClient notaAPI = retrofit.create(NotaClient.class);
                                            Call<ResponseBody> call = notaAPI.delivedNota(user.getId(),nota_id);

                                            call.enqueue(new Callback<ResponseBody>() {
                                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (!response.isSuccessful()) {
                                                        Log.i("response", String.valueOf(response.body()));
                                                        Toast.makeText(OrderDetailsActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                                                        if (loadingDialog!=null) {
                                                            loadingDialog.dismiss();
                                                        }
                                                        return;
                                                    }
                                                    if (loadingDialog!=null) {
                                                        loadingDialog.dismiss();
                                                    }
                                                    setProgress();
                                                    reloadPage();
                                                    sweetAlertDialog.dismiss();
                                                    Toast.makeText(OrderDetailsActivity.this, "Pesanan telah Sampai", Toast.LENGTH_SHORT).show();

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
                                    }).show();
                        }
                    });

                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SaveNota(OrderDetailsActivity.this);
                        }
                    });
                    copyResi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("No.Resi tersalin", myOrderDetail.getKet_kirim());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(OrderDetailsActivity.this,"No.Resi tersalin", Toast.LENGTH_SHORT).show();

                        }
                    });

                    hapus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            new SweetAlertDialog(OrderDetailsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Hapus Pesanan")
                                    .setContentText("Apakah benar data ini akan dihapus?")
                                    .setConfirmText("Ya, Hapus!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(final SweetAlertDialog sDialog) {

                                            loadingDialog.show();
                                            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {

                                                }
                                            });
                                            Gson gson = new GsonBuilder()
                                                    .setLenient()
                                                    .create();

                                            UserPreference userPreference = new UserPreference(OrderDetailsActivity.this);
                                            UserModel user = userPreference.getUserPreference("user");

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

                                            NotaClient notaAPI = retrofit.create(NotaClient.class);
                                            Call<ResponseBody> call = notaAPI.deleteNota(user.getId(),nota_id);
                                            call.enqueue(new Callback<ResponseBody>() {
                                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (!response.isSuccessful()) {
                                                        Log.i("response", String.valueOf(response.body()));
                                                        Toast.makeText(OrderDetailsActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                                                        if (loadingDialog!=null) {
                                                            loadingDialog.dismiss();
                                                        }
                                                        return;
                                                    }
                                                    if (loadingDialog!=null) {
                                                        loadingDialog.dismiss();
                                                    }
                                                    sDialog.dismiss();

                                                    finish();
                                                    Toast.makeText(OrderDetailsActivity.this, "Pesanan telah di Hapus", Toast.LENGTH_SHORT).show();

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
                                    })
                                    .show();

                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            new SweetAlertDialog(OrderDetailsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Batalkan Pesanan")
                                    .setContentText("Apakah benar pemesanan ini akan dibatalkan (Ini akan membatalkan semua pesanan anda)?")
                                    .setConfirmText("Ya, Batalkan!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                            loadingDialog.show();
                                            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {

                                                }
                                            });

                                            Gson gson = new GsonBuilder()
                                                    .setLenient()
                                                    .create();

                                            UserPreference userPreference = new UserPreference(OrderDetailsActivity.this);
                                            UserModel user = userPreference.getUserPreference("user");

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

                                            NotaClient notaAPI = retrofit.create(NotaClient.class);
                                            Call<ResponseBody> call = notaAPI.canceledNota(user.getId(),nota_id);
                                            call.enqueue(new Callback<ResponseBody>() {
                                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (!response.isSuccessful()) {
                                                        Log.i("response", String.valueOf(response.body()));
                                                        Toast.makeText(OrderDetailsActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                                                        if (loadingDialog!=null) {
                                                            loadingDialog.dismiss();
                                                        }
                                                        return;
                                                    }
                                                    if (loadingDialog!=null) {
                                                        loadingDialog.dismiss();
                                                    }
                                                    sweetAlertDialog.dismiss();
                                                    setProgress();
                                                    reloadPage();
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(OrderDetailsActivity.this, "Pesanan telah di Batalkan", Toast.LENGTH_SHORT).show();

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
                                    }).show();

                        }
                    });
                    idPayment.setText("Kode.Pesanan: " + myOrderDetail.get_id());
                    totalBayar.setText("Rp "+DBQueries.currencyFormatter(myOrderDetail.getTotal_amount()));


                    alamat.setText(myOrderDetail.getDetail_address());
                    bank_tv.setText("Bank " + myOrderDetail.getBank());

                    notaText.setText("Nota: "+myOrderDetail.get_id());

                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<OrderDetailsAPI> call, Throwable t) {
                    Log.e("debug", "onFailure: ERROR > " + t.toString());
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                }
            });

        }else {

        }
    }


    private void SaveNota(final Context context){
        final ProgressDialog progress = new ProgressDialog(context);
        class SaveThisImage extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setTitle("Download");
                progress.setMessage("Tunggu Sebentar...");
                progress.setCancelable(false);
                progress.show();
            }
            @Override
            protected Void doInBackground(Void... arg0) {
                layoutToImage();
//                try {
//                    imageToPDF();
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if(progress.isShowing()){
                    progress.dismiss();
                }
                Toast.makeText(context, "Nota Telah Tersimpan", Toast.LENGTH_SHORT).show();
            }
        }
        SaveThisImage shareimg = new SaveThisImage();
        shareimg.execute();
    }


    public void layoutToImage() {
        // get view group using reference
        // convert view group to bitmap
        notaPaymentLayout.setDrawingCacheEnabled(true);
        notaPaymentLayout.buildDrawingCache();


        Bitmap bm = Bitmap.createBitmap(notaPaymentLayout.getWidth(), notaPaymentLayout.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bm);
        notaPaymentLayout.layout(0, 0, notaPaymentLayout.getMeasuredWidth(), notaPaymentLayout.getMeasuredHeight());
        notaPaymentLayout.draw(c);

        // Bitmap bm = detail.getDrawingCache(true);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(OrderDetailsActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) OrderDetailsActivity.this, PERMISSIONS, REQUEST );
            } else {
                //do here
                now = Long.toString(System.currentTimeMillis());
                File f = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+now+"nota.jpg");
                try {

                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notaPaymentLayout.setDrawingCacheEnabled(false);
            }
        } else {
            //do here
            now = Long.toString(System.currentTimeMillis());
            File f = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+now+ "nota.jpg");
            try {

                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            notaPaymentLayout.setDrawingCacheEnabled(false);
        }


    }
    public void imageToPDF() throws FileNotFoundException {
        try {
            Document document = new Document(PageSize.A4);
            dirpath = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/"+now+"nota.pdf")); //  Change pdf's name.
            document.open();
            Image img = Image.getInstance(dirpath +"/Download/" + now+"nota.jpg");
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / img.getHeight()) * 100;
            img.scalePercent(scaler);
            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(img);
            document.close();
            Toast.makeText(this, "Silahkan Cek di Download", Toast.LENGTH_SHORT).show();
            // print.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
