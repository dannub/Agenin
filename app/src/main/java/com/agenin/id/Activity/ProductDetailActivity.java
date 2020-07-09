package com.agenin.id.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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

import com.agenin.id.Adapter.ProductImagesAdapter;
import com.agenin.id.DBQueries;
import com.agenin.id.Fragment.ui.SignInFragment;
import com.agenin.id.Fragment.ui.SignUpFragment;
import com.agenin.id.Interface.CartClient;
import com.agenin.id.Interface.ProductClient;
import com.agenin.id.Interface.StarClient;
import com.agenin.id.Interface.WishlistClient;
import com.agenin.id.Model.CartItemModel;
import com.agenin.id.Model.ProductModel;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class ProductDetailActivity extends AppCompatActivity {


    public static Boolean running_rating_query = false;
    public static Boolean running_wishlist_query = false;
    public static Boolean running_cart_query = false;

    public  static Activity productDetailsActivity;

    private FirebaseUser currentUser;

    public static ProductModel product;
    public static String productID;
    public static List<String> productImages;
    private Dialog loadingDialog;
    private  Dialog signInDialog;

    public static final String PROGRESS_UPDATE = "progress_update";
    private FloatingActionButton download,keranjang;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private ViewPager productImageViewPager;
    private  TextView productTitle;
    private TabLayout viewpagerIndikator;
    private TextView productPrice;
    private String productOriginalPrice;
    private TextView oriPrice;
    private ImageView keranjangImg;

    private TextView satuan_harga_agen;
    private TextView satuan_min_order;
    private TextView min_order;
    private TextView category_product;

    private TextView sent_from;
    private TextView estimation;



    /////ratting layout
    public static int initialRating;
    public static LinearLayout rateNowContainer;
    private  TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private  TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private  TextView averageRatings;
    /////ratting layout


    //Notification
    public   static  final int NOTIFY_ID = 0;
    public   static  final String CHANNEL_ID = "Agenin";
    public   static  final String CHANNEL_NAME = "Agenin";
    private  static  final String CHANNEL_DESC = "Agenin Notification";



    private View divider;


    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;
    public static ImageView addToWishlistBtn;

    private LinearLayout addToCartBtn;
    private Button buyNowBtn;
    private Button salin;

    //Product Description
    private TextView productOnlyDescriptionBody;

    private ProgressDialog progressDialog;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //deklarasi
        productImageViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndikator = findViewById(R.id.viewpager_indicator);
        addToWishlistBtn = findViewById(R.id.add_to_wishlist_btn);

        download = findViewById(R.id.download);
        keranjang = findViewById(R.id.keranjang);



        buyNowBtn = findViewById(R.id.buy_now_btn);
        productTitle = findViewById(R.id.product_title);
        divider = findViewById(R.id.divider3);
        keranjangImg= findViewById(R.id.keranjang_img);


        //basic
        category_product = findViewById(R.id.category);
        productPrice = findViewById(R.id.product_price);
        oriPrice = findViewById(R.id.ori_price);
        satuan_harga_agen = findViewById(R.id.satuan_harga_agen);
        satuan_min_order = findViewById(R.id.satuan_min_order);
        min_order = findViewById(R.id.min_order);

        //info
        sent_from = findViewById(R.id.dikirim_dari);
        estimation = findViewById(R.id.estimasi);

        //productdetail
        salin = findViewById(R.id.salin);
        productOnlyDescriptionBody = findViewById(R.id.product_detail_body);


        //rating
        totalRatings = findViewById(R.id.total_rattings);
        ratingsNoContainer = findViewById(R.id.ratttings_number_container);
        totalRatingsFigure = findViewById(R.id.total_rattings_figure);
        ratingsProgressBarContainer = findViewById(R.id.rattings_progressbar_container);
        averageRatings = findViewById(R.id.average_rattings);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        rateNowContainer = findViewById(R.id.rate_now_container);
        //deklarasi

        //loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        }
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);



        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            // JSON here
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        //loading dialog


        productID = getIntent().getStringExtra("productID").replace(" ","");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        initialRating = -1;
        if (currentUser!=null) {

            DBQueries.loadRatingList(ProductDetailActivity.this,null);


            if (DBQueries.wishlist.size() == 0) {
                DBQueries.loadWishlist(ProductDetailActivity.this, loadingDialog,false);
            } else {
                loadingDialog.dismiss();
            }

//            if (DBqueries.cartlist.size()!=0 && DBqueries.rewardModelList.size()!=0 && DBqueries.wishlist.size() != 0){
//                loadingDialog2.dismiss();
//
//            }
        }else {
            loadingDialog.dismiss();
        }

        if (DBQueries.myRatedIds.contains(productID)){
            int index = DBQueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBQueries.myRating.get(index)))-1;
            setRatting(initialRating);
        }
//
////
////        if (DBQueries.cartlist.contains(productID)){
////            ALREADY_ADDED_TO_CART = true;
////
////        }else {
////            ALREADY_ADDED_TO_CART = false;
////        }
//
        if (DBQueries.wishlist.contains(productID)){
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishlistBtn.setImageTintList(getResources().getColorStateList(R.color.colorAccent4));

        }else {
            addToWishlistBtn.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
            ALREADY_ADDED_TO_WISHLIST = false;
        }
        reloadPage();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setRatting(int starPosition) {

        for (int x = 0; x <rateNowContainer.getChildCount(); x++){
            ImageView starBtn = (ImageView)rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#CCC8C8")));
            if (x <= starPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }

        }
    }

    private  String calculateAverageRating(long currentUserRating,boolean update){
        Double totalStars = Double.valueOf(0);
        for (int x=1;x<6;x++){
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5-x);
            totalStars = totalStars +(Long.parseLong(ratingNo.getText().toString())*x);
        }
        totalStars = totalStars + currentUserRating;

        if (update){
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0,3);
        }else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString())+ 1)).substring(0,3);
        }
    }

    private void reloadPage(){


        ///sign dialog
        signInDialog = new Dialog(ProductDetailActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);

        final Intent registerIntent = new Intent(ProductDetailActivity.this, RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                RegisterActivity.setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                RegisterActivity.setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });
        ///sign dialog

        UserPreference userPreference = new UserPreference(ProductDetailActivity.this);
        userPreference.setUserPreference("user",null);
        if (currentUser!=null){

            if(userPreference.getUserPreference("user")==null){
                DBQueries.requestLogin(ProductDetailActivity.this,currentUser.getEmail(),currentUser.getUid(),loadingDialog,false);
            }
        }
        loadingDialog.show();
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        productImages = new ArrayList<>();

        addToCartBtn.setEnabled(true);

        productID = getIntent().getStringExtra("productID").replace(" ","");

        initialRating = -1;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DBQueries.url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ProductClient productApi = retrofit.create(ProductClient.class);
        Call<ProductModel> call = productApi.getProduct(productID);

        call.enqueue(new Callback<ProductModel>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(ProductDetailActivity.this,response.code(),Toast.LENGTH_SHORT).show();
                    return;

                }


                product = response.body();

                category_product.setText(product.getCategory());

                for (int x = 0;x< product.getImage().size();x++){
                    productImages.add(DBQueries.url+product.getImage().get(x));
                }
                ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                productImageViewPager.setAdapter(productImagesAdapter);
                if (productImages.size()>1){
                    viewpagerIndikator.setupWithViewPager(productImageViewPager,true);
                }


                keranjang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentUser ==null) {
                            signInDialog.show();
                        }else {
                            MainActivity.showCart = true;
                            Intent cartIntent = new Intent(ProductDetailActivity.this,CartActivity.class);
                            startActivity(cartIntent);

                        }
                    }
                });
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  initRetrofit();

                        if (isStoragePermissionGranted()){
                            SaveImage(ProductDetailActivity.this,productImages.get(productImageViewPager.getCurrentItem()),product.getTitle_product());
                        }

                    }
                });
                productTitle.setText(product.getTitle_product());



                if (!product.getCutted_price().equals("")) {
                    oriPrice.setVisibility(View.VISIBLE);
                    divider.setVisibility(View.VISIBLE);
                    productPrice.setText("Rp " +currencyFormatter(product.getPrice()) );
                    if (!product.getSatuan().equals("")) {
                        oriPrice.setText("Rp " + currencyFormatter(product.getCutted_price())+"/"+product.getSatuan());
                    }else {
                        oriPrice.setText("Rp " + currencyFormatter(product.getCutted_price()));
                    }
                }else {
                    oriPrice.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                    productPrice.setText("Rp " + currencyFormatter(product.getPrice()));
                }
                min_order.setText(product.getMin_order());

                if (!product.getSatuan().equals("")){
                    satuan_harga_agen.setVisibility(View.VISIBLE);
                    satuan_harga_agen.setText(" /"+product.getSatuan());
                    satuan_min_order.setVisibility(View.VISIBLE);
                    satuan_min_order.setText(" /"+product.getSatuan());
                }else {
                    satuan_harga_agen.setVisibility(View.GONE);
                    satuan_min_order.setVisibility(View.GONE);
                }

                sent_from.setText(product.getSent_from());
                estimation.setText(product.getEstimation()+" Hari");
                productOnlyDescriptionBody.setText(product.getDecription().replace("\\n", "\n"));
                salin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) ProductDetailActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Rincian Produk Tersalin", product.getDecription().replace("\\n", "\n"));
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(ProductDetailActivity.this,"Rincian Produk Tersalin",Toast.LENGTH_SHORT).show();
                    }
                });


                if (DBQueries.cartlist.contains(productID)){
                    ALREADY_ADDED_TO_CART = true;

                }else {
                    ALREADY_ADDED_TO_CART = false;
                }
                if (product.getIn_stock()){
                    keranjangImg.setVisibility(View.VISIBLE);
                    buyNowBtn.setVisibility(View.VISIBLE);

                    addToCartBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addToCartBtn.setEnabled(false);
                            if (currentUser == null){
                                signInDialog.show();
                            }else {

                                UserPreference userPreference = new UserPreference(ProductDetailActivity.this);
                                UserModel user = userPreference.getUserPreference("user");

                                if (user.getStatus()){
                                    loadingDialog.show();
                                    loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {

                                        }
                                    });
                                    if (!running_cart_query) {

                                        running_cart_query = true;
                                        if (ALREADY_ADDED_TO_CART) {
                                            running_cart_query = false;
                                            Toast.makeText(ProductDetailActivity.this, "Barang Sudah Masuk Ke Keranjang", Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismiss();
                                        } else {
                                            addToCartBtn.setEnabled(true);
                                            final Map<String, Object> addProduct = new HashMap<>();
                                            addProduct.put("product_ID_" + String.valueOf(DBQueries.cartlist.size()), productID);
                                            Gson gson = new GsonBuilder()
                                                    .setLenient()
                                                    .create();


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
                                                    .readTimeout(30, TimeUnit.SECONDS)
                                                    .connectTimeout(30, TimeUnit.SECONDS)
                                                    .build();
                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(DBQueries.url)
                                                    .client(okHttpClient)
                                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                                    .build();

                                            CartClient cartAPI = retrofit.create(CartClient.class);
                                            Call<Integer> call = cartAPI.setMyCart(user.getId(), productID);
                                            call.enqueue(new Callback<Integer>() {
                                                @Override
                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                    if (!response.isSuccessful()) {
                                                        running_cart_query = false;
                                                        loadingDialog.dismiss();
                                                        addToCartBtn.setEnabled(false);
                                                        Toast.makeText(ProductDetailActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                                                        return;

                                                    }


                                                    if (response.body() != 0) {
                                                        MainActivity.badgeCount.setVisibility(View.VISIBLE);
                                                    } else {
                                                        MainActivity.badgeCount.setVisibility(View.INVISIBLE);
                                                    }
                                                    if (response.body() < 99) {
                                                        MainActivity.badgeCount.setText(String.valueOf(response.body()));
                                                    } else {
                                                        MainActivity.badgeCount.setText("99+");
                                                    }
                                                    running_cart_query = false;
                                                    ALREADY_ADDED_TO_CART = true;
//                                                    loadingDialog.dismiss();
                                                    Toast.makeText(ProductDetailActivity.this, "Barang Berhasil Masuk Keranjang", Toast.LENGTH_SHORT).show();
                                                    addToCartBtn.setEnabled(false);
                                                    DBQueries.loadCartList(ProductDetailActivity.this,loadingDialog,false,MainActivity.badgeCount,null,false,null);

                                                }


                                                @Override
                                                public void onFailure(Call<Integer> call, Throwable t) {
                                                    Log.e("debug", "onFailure: ERROR > " + t.toString());
                                                    running_cart_query = false;
                                                    Toast.makeText(ProductDetailActivity.this, "Jaringan Dalam Perbaikan", Toast.LENGTH_SHORT).show();
                                                    if (userPreference.getUserPreference("user") == null) {
                                                        DBQueries.requestLogin(ProductDetailActivity.this, currentUser.getEmail(), currentUser.getUid(), loadingDialog, false);
                                                    }
                                                    loadingDialog.dismiss();
                                                    addToCartBtn.setEnabled(false);


                                                }
                                            });

                                        }
                                    }else {
                                        Toast.makeText(ProductDetailActivity.this,"Tidak Bisa memasukan ke Keranjang! Status Akun Anda menunggu Konfirmasi Admin",Toast.LENGTH_SHORT).show();

                                    }
                                }




                            }
                        }
                    });

                }else {
                    buyNowBtn.setVisibility(View.GONE);
                    TextView outOfStock =(TextView) addToCartBtn.getChildAt(0);
                    outOfStock.setText("Stok Habis");
                    keranjangImg.setVisibility(View.GONE);

                    outOfStock.setTextColor(getResources().getColor(R.color.colorAccent));
                    addToCartBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent4));
                    outOfStock.setCompoundDrawables(null,null,null,null);
                }

                buyNowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentUser ==null) {
                            signInDialog.show();
                        }else {
                            UserPreference userPreference = new UserPreference(ProductDetailActivity.this);
                            UserModel user = userPreference.getUserPreference("user");

                            if (user.getStatus()){
                                MainActivity.showCart = false;
                                //loadingDialog2.show();
                                productDetailsActivity = ProductDetailActivity.this;
                                //DeliveryActivity.cartItemModelList.clear();
                                DeliveryActivity.cartItemModelList = new ArrayList<>();
                                DeliveryActivity.cartItemModelList.clear();
                                Double berat = product.getBerat();
                                int ongkir =  ((int)(8000*berat)*Integer.valueOf(product.getMin_order()));
                                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM,
                                            "",
                                            productID,
                                            DBQueries.url+product.getImage().get(0).toString()
                                            , product.getTitle_product()
                                            , product.getPrice()
                                            , (int) Integer.valueOf(product.getMin_order())
                                            , (Boolean) product.getIn_stock()
                                            , product.getAverage_rating()
                                            , product.getSatuan()
                                            , berat
                                            , (int)Integer.valueOf(product.getMin_order())
                                            ,String.valueOf(ongkir)
                                    ));

                                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                                if (DBQueries.addressModelList.size()==0){
                                    DBQueries.loadAddresses(ProductDetailActivity.this,loadingDialog,true,0,false);
                                }else {
                                    loadingDialog.dismiss();
                                    Intent deliveryIntent = new Intent(ProductDetailActivity.this,DeliveryActivity.class);
                                    startActivity(deliveryIntent);
                                }
                            }else {
                                Toast.makeText(ProductDetailActivity.this,"Tidak Bisa melakukan pembelian! Status Akun Anda menunggu Konfirmasi Admin",Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });

                ///star
                TextView rating = (TextView)ratingsNoContainer.getChildAt(0);
                rating.setText(String.valueOf(product.getStar_5()));
                ProgressBar progressBar = (ProgressBar)ratingsProgressBarContainer.getChildAt(0);
                int maxProgress = Integer.parseInt(String.valueOf(product.getTotal_ratings()));
                progressBar.setMax(maxProgress);
                progressBar.setProgress(Integer.parseInt(String.valueOf(product.getStar_5())));

                rating = (TextView)ratingsNoContainer.getChildAt(1);
                rating.setText(String.valueOf(product.getStar_4()));
                progressBar = (ProgressBar)ratingsProgressBarContainer.getChildAt(1);
                maxProgress = Integer.parseInt(String.valueOf(product.getTotal_ratings()));
                progressBar.setMax(maxProgress);
                progressBar.setProgress(Integer.parseInt(String.valueOf(product.getStar_4())));

                rating = (TextView)ratingsNoContainer.getChildAt(2);
                rating.setText(String.valueOf(product.getStar_3()));
                progressBar = (ProgressBar)ratingsProgressBarContainer.getChildAt(2);
                maxProgress = Integer.parseInt(String.valueOf(product.getTotal_ratings()));
                progressBar.setMax(maxProgress);
                progressBar.setProgress(Integer.parseInt(String.valueOf(product.getStar_3())));

                rating = (TextView)ratingsNoContainer.getChildAt(3);
                rating.setText(String.valueOf(product.getStar_2()));
                progressBar = (ProgressBar)ratingsProgressBarContainer.getChildAt(3);
                maxProgress = Integer.parseInt(String.valueOf(product.getTotal_ratings()));
                progressBar.setMax(maxProgress);
                progressBar.setProgress(Integer.parseInt(String.valueOf(product.getStar_2())));

                rating = (TextView)ratingsNoContainer.getChildAt(4);
                rating.setText(String.valueOf(product.getStar_1()));
                progressBar = (ProgressBar)ratingsProgressBarContainer.getChildAt(4);
                maxProgress = Integer.parseInt(String.valueOf(product.getTotal_ratings()));
                progressBar.setMax(maxProgress);
                progressBar.setProgress(Integer.parseInt(String.valueOf(product.getStar_1())));


                totalRatings.setText("Dari "+product.getTotal_ratings()+" ulasan");

                totalRatingsFigure.setText(String.valueOf(product.getTotal_ratings()));
                averageRatings.setText(product.getAverage_rating());

                if (currentUser!=null) {
                    if (DBQueries.myRating.size()==0){
//                        DBQueries.loadRatingList(ProductDetailActivity.this,loadingDialog);
                    }
//                    if (DBqueries.cartlist.size() == 0) {
//                        DBqueries.loadCartList(ProductDetailActivity.this, loadingDialog2,false,badgeCount,new TextView(ProductDetailActivity.this),false,addToCartBtn);
//                    }
                    if (DBQueries.wishlist.size() == 0) {
//                        DBQueries.loadWishlist(ProductDetailActivity.this, loadingDialog,false);
                    } else {
                        loadingDialog.dismiss();
                    }
//
//                    DBqueries.loadRewards(ProductDetailActivity.this,loadingDialog2,false);
//
//                    if (DBqueries.cartlist.size()!=0 && DBqueries.rewardModelList.size()!=0 && DBqueries.wishlist.size() != 0){
//                        loadingDialog2.dismiss();
//                    }
                }else {
                   // loadingDialog2.dismiss();
                }


                if (DBQueries.myRatedIds.contains(productID)){
                    int index = DBQueries.myRatedIds.indexOf(productID);
                    initialRating = Integer.parseInt(String.valueOf(DBQueries.myRating.get(index))) -1;
                    setRatting(initialRating);
                }


                if (DBQueries.wishlist.contains(productID)){
                    ALREADY_ADDED_TO_WISHLIST = true;
                    addToWishlistBtn.setImageTintList(getResources().getColorStateList(R.color.colorAccent4));

                }else {
                    addToWishlistBtn.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
                    ALREADY_ADDED_TO_WISHLIST = false;
                }

                for (int x = 0; x <rateNowContainer.getChildCount();x++){
                    final int starPosition = x;
                    rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (currentUser ==null) {
                                signInDialog.show();
                            }else {
                                UserPreference userPreference = new UserPreference(ProductDetailActivity.this);
                                UserModel user = userPreference.getUserPreference("user");

                                if (user.getStatus()){
                                    if (starPosition!= initialRating) {
                                        loadingDialog.show();
                                        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {

                                            }
                                        });

                                        Log.i("star", String.valueOf(starPosition));
                                        if (!running_rating_query) {
                                            running_rating_query = true;

                                            setRatting(starPosition);

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
                                                    .readTimeout(60, TimeUnit.SECONDS)
                                                    .connectTimeout(60, TimeUnit.SECONDS)
                                                    .build();
                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(DBQueries.url)
                                                    .client(okHttpClient)
                                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                                    .build();

                                            StarClient starApi = retrofit.create(StarClient.class);
                                            Call<ResponseBody> call = starApi.setMyStar(user.getId(),productID,starPosition+1);

                                            call.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()){


                                                        if (DBQueries.myRatedIds.contains(productID)) {

                                                            DBQueries.myRating.set(DBQueries.myRatedIds.indexOf(productID),  starPosition + 1);

                                                            TextView oldrating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalrating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                            oldrating.setText(String.valueOf(Integer.parseInt(oldrating.getText().toString()) - 1));
                                                            finalrating.setText(String.valueOf(Integer.parseInt(finalrating.getText().toString()) + 1));

                                                        } else {
                                                            DBQueries.myRatedIds.add(productID);
                                                            DBQueries.myRating.add( starPosition + 1);

                                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));


                                                            totalRatings.setText("Dari "+ String.valueOf(product.getTotal_ratings()+1)+" ulasan");

                                                            totalRatingsFigure.setText(String.valueOf(product.getTotal_ratings()+1));


                                                            Toast.makeText(ProductDetailActivity.this, "Terima Kasih Telah Menilai", Toast.LENGTH_SHORT).show();
                                                        }

                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);

                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                            progressBar.setMax(maxProgress);
                                                            progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                        }
                                                        initialRating = starPosition;
                                                        averageRatings.setText(calculateAverageRating(0, true));


                                                        if (DBQueries.wishlist.contains(productID) && DBQueries.wishlistModelList.size() != 0) {
                                                            int index = DBQueries.wishlist.indexOf(productID);
                                                            DBQueries.wishlistModelList.get(index).setRatting(averageRatings.getText().toString());
                                                            DBQueries.wishlistModelList.get(index).setTotalRattings(Integer.parseInt(totalRatingsFigure.getText().toString()));
                                                        }






                                                    } else {

                                                        setRatting(initialRating);
                                                        Log.i("error", String.valueOf((CharSequence) response.errorBody()));
                                                        Toast.makeText(ProductDetailActivity.this, (CharSequence) response.errorBody(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;
                                                    loadingDialog.dismiss();
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    running_rating_query = false;
                                                    setRatting(initialRating);
                                                    Log.e("debug", "onFailure: ERROR > " + t.toString());
                                                    loadingDialog.dismiss();
                                                }
                                            });


                                        }
                                    }
                                }else {
                                    Toast.makeText(ProductDetailActivity.this,"Tidak Bisa melakukan penilaian! Status Akun Anda menunggu Konfirmasi Admin",Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });
                }
                /////ratting layout

                addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //loadingDialog2.show();
                        if (currentUser==null){
                            signInDialog.show();
                        }else {
                            UserPreference userPreference = new UserPreference(ProductDetailActivity.this);
                            UserModel user = userPreference.getUserPreference("user");

                            if (user.getStatus()){
                                if (!running_wishlist_query) {
                                    addToWishlistBtn.setEnabled(false);

                                    loadingDialog.show();
                                    loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {

                                        }
                                    });
                                    running_wishlist_query = true;
                                    if (ALREADY_ADDED_TO_WISHLIST) {
                                        int index = DBQueries.wishlist.indexOf(productID);
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
                                                .readTimeout(60, TimeUnit.SECONDS)
                                                .connectTimeout(60, TimeUnit.SECONDS)
                                                .build();
                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(DBQueries.url)
                                                .client(okHttpClient)
                                                .addConverterFactory(GsonConverterFactory.create(gson))
                                                .build();

                                        WishlistClient wishlistAPI = retrofit.create(WishlistClient.class);
                                        Call<ResponseBody> call = wishlistAPI.setMyWishlist(user.getId(),productID);
                                        call.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful()) {
                                                    if (DBQueries.wishlistModelList.size() != 0) {



                                                        DBQueries.loadRatingList(ProductDetailActivity.this,null);

                                                    }

                                                    ALREADY_ADDED_TO_WISHLIST = false;
                                                    addToWishlistBtn.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));

                                                    DBQueries.wishlist.remove(index);
                                                    Toast.makeText(ProductDetailActivity.this, "Dihapus dari Daftar Favotit", Toast.LENGTH_SHORT).show();
                                                    addToWishlistBtn.setEnabled(true);


                                                }else {
                                                    addToWishlistBtn.setEnabled(true);
                                                    addToWishlistBtn.setImageTintList(getResources().getColorStateList(R.color.colorAccent4));
                                                    String error = response.message();
                                                    Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                running_wishlist_query= false;
                                                loadingDialog.dismiss();
                                            }
                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Log.e("debug", "onFailure: ERROR > " + t.toString());
                                                addToWishlistBtn.setEnabled(true);
                                                addToWishlistBtn.setImageTintList(getResources().getColorStateList(R.color.colorAccent4));

                                                 loadingDialog.dismiss();

                                            }
                                        });

    //                                    DBQueries.removeFromWishlist(index, ProductDetailActivity.this);

                                    } else {



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
                                                .readTimeout(60, TimeUnit.SECONDS)
                                                .connectTimeout(60, TimeUnit.SECONDS)
                                                .build();
                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(DBQueries.url)
                                                .client(okHttpClient)
                                                .addConverterFactory(GsonConverterFactory.create(gson))
                                                .build();

                                        WishlistClient wishlistAPI = retrofit.create(WishlistClient.class);
                                        Call<ResponseBody> call = wishlistAPI.setMyWishlist(user.getId(),productID);
                                        call.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful()) {
                                                        if (DBQueries.wishlistModelList.size() != 0) {



                                                            DBQueries.loadRatingList(ProductDetailActivity.this,null);

                                                        }

                                                        ALREADY_ADDED_TO_WISHLIST = true;
                                                        addToWishlistBtn.setImageTintList(getResources().getColorStateList(R.color.colorAccent4));
                                                        DBQueries.wishlist.add(productID);

                                                        Toast.makeText(ProductDetailActivity.this, "Ditambahkan di Daftar Favorit", Toast.LENGTH_SHORT).show();
                                                        addToWishlistBtn.setEnabled(true);


                                                }else {
                                                    addToWishlistBtn.setEnabled(true);
                                                    addToWishlistBtn.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
                                                    String error = response.message();
                                                    Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                running_wishlist_query= false;
                                                loadingDialog.dismiss();
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
                            }else {
                                Toast.makeText(ProductDetailActivity.this,"Tidak Bisa memasukan ke Favorite! Status Akun Anda menunggu Konfirmasi Admin",Toast.LENGTH_SHORT).show();

                            }
                            }
                        }
                    }
                });

                loadingDialog.dismiss();



            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.i("error",t.getMessage());
                loadingDialog.dismiss();
            }
        });
    }




    private static void SaveImage(final Context context, final String MyUrl,final String filename){
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
                try{

                    File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    @SuppressLint("DefaultLocale") String fileName = String.format("%d", System.currentTimeMillis());
                    File dir = new File(sdCard.getAbsolutePath() );
                    dir.mkdirs();
                    final File myImageFile = new File(dir, fileName+"_"+filename+".jpeg"); // Create image file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(myImageFile);
                        Bitmap bitmap = Picasso.get().load(MyUrl).get();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(myImageFile));
                        context.sendBroadcast(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if(progress.isShowing()){
                    progress.dismiss();
                }
                Toast.makeText(context, "Gambar Telah Tersimpan", Toast.LENGTH_SHORT).show();
            }
        }
        SaveThisImage shareimg = new SaveThisImage();
        shareimg.execute();
    }









    public static String currencyFormatter(String num) {
        double m = Double.parseDouble(num);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(m);
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission","Permission is granted");
                return true;
            } else {

                Log.v("permission","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("permission","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home){
            productDetailsActivity = null;
            MainActivity.showCart = false;
            finish();
            MainActivity.currentFragment = MainActivity.HOME_FRAGMENT;

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //fromSearch = false;
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity = null;
        MainActivity.showCart = false;
        finish();
        MainActivity.currentFragment = MainActivity.HOME_FRAGMENT;
        super.onBackPressed();
    }
}