package com.agenin.id;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agenin.id.Activity.AddAddressActivity;
import com.agenin.id.Activity.CartActivity;
import com.agenin.id.Activity.DeliveryActivity;
import com.agenin.id.Activity.MainActivity;
import com.agenin.id.Activity.NotificationActivity;
import com.agenin.id.Activity.ProductDetailActivity;
import com.agenin.id.Adapter.CartAdapter;
import com.agenin.id.Adapter.CategoryAdapter;
import com.agenin.id.Adapter.HomePageAdapter;
import com.agenin.id.Adapter.MyOrderAdapter;
import com.agenin.id.Adapter.NotificationAdapter;
import com.agenin.id.Adapter.YoutubeVideoAdapter;
import com.agenin.id.Fragment.ui.FavoriteFragment;
import com.agenin.id.Fragment.ui.HomeFragment;
import com.agenin.id.Fragment.ui.MyOrdersFragment;
import com.agenin.id.Fragment.ui.ProfilFragment;
import com.agenin.id.Fragment.ui.TutorialFragment;
import com.agenin.id.Interface.AddressClient;
import com.agenin.id.Interface.CartClient;
import com.agenin.id.Interface.CategoryClient;
import com.agenin.id.Interface.NotaClient;
import com.agenin.id.Interface.NotifClient;
import com.agenin.id.Interface.StarClient;
import com.agenin.id.Interface.UserClient;
import com.agenin.id.Interface.WishlistClient;
import com.agenin.id.Interface.YoutubeClient;
import com.agenin.id.Model.AddressModel;
import com.agenin.id.Model.Api.CartInputAPI;
import com.agenin.id.Model.Api.NotaItemAPI;
import com.agenin.id.Model.Api.NotifAPI;
import com.agenin.id.Model.Api.OrderUserAPI;
import com.agenin.id.Model.Api.StarAPI;
import com.agenin.id.Model.Api.WishlistAPI;
import com.agenin.id.Model.CartInputModel;
import com.agenin.id.Model.CartItemModel;
import com.agenin.id.Model.HomePageModel;
import com.agenin.id.Model.HorizontalProductScrollModel;
import com.agenin.id.Model.NotificationModel;
import com.agenin.id.Model.OrderListModel;
import com.agenin.id.Model.SliderModel;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Model.CategoryModel;
import com.agenin.id.Model.WishlistModel;
import com.agenin.id.Model.YoutubeVideoModel;
import com.agenin.id.Preference.UserPreference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DBQueries {
   public static String url = "https://apk.agenin.id/";

  //public static String url = "http://192.168.100.9:8000/";
    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Integer> myRating = new ArrayList<>();

    public static List<NotificationModel> notificationModelList = new ArrayList<>();



    public static List<CategoryModel> categoryModelList = new ArrayList<>();
    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static  List<String> loadedCategoriesNames = new ArrayList<>();


    public static List<String> wishlist = new ArrayList<>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();

    public static List<YoutubeVideoModel> youtubeVideoModelList = new ArrayList<>();
    public static List<String> cartlist = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();

    public static int selectedAddress = -1;
    public static String selectedAddressId = "";
    public static List<AddressModel> addressModelList = new ArrayList<>();

    public static List<OrderListModel> orderModelList= new ArrayList<>();


    public static void requestLogin(Context context,String email, String password, @Nullable Dialog dialog,Boolean isSplash){

        if (isSplash){
            if (dialog!=null) {
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
            }
        }

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

        Call<UserModel> call =    client.login(email, password);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()){

                    UserModel userModel = response.body();
                    UserPreference userPreference = new UserPreference(context);
                    userPreference.setUserPreference("user", userModel);
                    if(isSplash){
                        Intent mainIntent = new Intent(context, MainActivity.class);
                        // MainActivity.showCart = false;
                        context.startActivity(mainIntent);
                        ((Activity)context).finish();
                    }else {
                        if (dialog!=null) {
                            dialog.dismiss();
                        }
                    }



                } else {

                    UserPreference userPreference = new UserPreference(context);
                    userPreference.setUserPreference("user", null);
                    if(isSplash){
                        Intent mainIntent = new Intent(context, MainActivity.class);
                        // MainActivity.showCart = false;
                        context.startActivity(mainIntent);
                        ((Activity)context).finish();
                    }else {
                        if (dialog!=null) {
                            dialog.dismiss();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                if(isSplash){
                    Intent mainIntent = new Intent(context, MainActivity.class);
                    // MainActivity.showCart = false;
                    context.startActivity(mainIntent);
                    ((Activity)context).finish();
                }else {
                    dialog.dismiss();
                }
            }
        });
    }



    public static void loadAddresses(final Context context, final Dialog loadingDialog, final boolean gotoDeleveryActivity, final int SELECT_ADDRESS,Boolean isMain) {
        addressModelList.clear();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (loadingDialog!=null) {
            loadingDialog.show();
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
        }
        UserPreference userPreference = new UserPreference(context);
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
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        AddressClient addressAPI = retrofit.create(AddressClient.class);
        Call<List<AddressModel>> call = addressAPI.getMyAddress(user.getId());

        call.enqueue(new Callback<List<AddressModel>>() {
            @Override
            public void onResponse(Call<List<AddressModel>> call, Response<List<AddressModel>> response) {
                if (!response.isSuccessful()) {
                    Log.i("response", String.valueOf(response.body()));
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                    if (loadingDialog!=null) {
                        loadingDialog.dismiss();
                    }
                    return;
                }
                Intent deliveryIntent = null;
                if (response.body().size() == 0) {
                    if (!isMain) {
                        deliveryIntent = new Intent(context, AddAddressActivity.class);
                        deliveryIntent.putExtra("INTENT", "deliveryIntent");
                    }
                } else {

                    addressModelList = response.body();

                    for (int x = 0; x < response.body().size(); x++) {

                        if (response.body().get(x).getSelected()) {
                            selectedAddress = x;
                            selectedAddressId = response.body().get(x).get_id();
                        }
                    }
                    if (!isMain) {
                        if (gotoDeleveryActivity) {
                            deliveryIntent = new Intent(context, DeliveryActivity.class);
                        }
                    }
                }
                if (!isMain) {
                    if (SELECT_ADDRESS == 0) {
                        if (DBQueries.addressModelList.size() == 0) {

                            ProfilFragment.addressname.setText("No Address");
                            ProfilFragment.address.setText("-");
                            ProfilFragment.pincode.setText("-");
                        } else {

                            ProfilFragment.setAddress();
                        }
                    }
                }

                loadingDialog.dismiss();
                if (!isMain) {
                    if (gotoDeleveryActivity) {

                        context.startActivity(deliveryIntent);
                    }
                }


            }
            @Override
            public void onFailure(Call<List<AddressModel>> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                if (loadingDialog!=null) {
                    loadingDialog.dismiss();
                }
            }
        });

    }



    public static void loadVideolist(final Context context, final Dialog dialog, final Boolean loadPVideoData, @Nullable final RecyclerView recyclerViewFeed, @Nullable final ImageView no_internet, @Nullable final TextView noData){

        youtubeVideoModelList.clear();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(DBQueries.url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        YoutubeClient client = retrofit.create(YoutubeClient.class);

        Call<List<YoutubeVideoModel>> call = client.getYoutube();
        call.enqueue(new Callback<List<YoutubeVideoModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<List<YoutubeVideoModel>> call, Response<List<YoutubeVideoModel>> response) {
                if (response.isSuccessful()) {

                    youtubeVideoModelList  = response.body();
                    Log.i("youtube",youtubeVideoModelList.get(0).getTitle());
                    if (youtubeVideoModelList.size() != 0) {

                        if (loadPVideoData) {
                            noData.setVisibility(View.GONE);
                            no_internet.setVisibility(View.GONE);
                            recyclerViewFeed.setVisibility(View.VISIBLE);
                            if (youtubeVideoModelList.size() > 1) {
                                Collections.sort(youtubeVideoModelList, new Comparator<YoutubeVideoModel>() {
                                    @Override
                                    public int compare(YoutubeVideoModel o1, YoutubeVideoModel o2) {
                                        if (o1.getTitle() == null || o2.getTitle() == null)
                                            return 0;
                                        return o1.getTitle().compareTo(o2.getTitle());

                                    }
                                });
                            }

                            TutorialFragment.mRecyclerAdapter = new YoutubeVideoAdapter(youtubeVideoModelList);
                            recyclerViewFeed.setAdapter(TutorialFragment.mRecyclerAdapter);
                            TutorialFragment.mRecyclerAdapter.notifyDataSetChanged();


                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                            recyclerViewFeed.setLayoutManager(mLayoutManager);
                            recyclerViewFeed.setItemAnimator(new DefaultItemAnimator());
                            recyclerViewFeed.setAdapter(TutorialFragment.mRecyclerAdapter);

                        }
                    }
                    dialog.dismiss();

                }
            }
            @Override
            public void onFailure(Call<List<YoutubeVideoModel>> call, Throwable t) {

                String error =  t.toString();
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                Log.e("debug", "onFailure: ERROR > " + t.toString());

            }
        });


    }


    public static void removeFromWishlist(final Context context, int index,final String wishlistID){

        final String productID = wishlist.get(index);

        wishlist.remove(index);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        UserPreference userPreference = new UserPreference(context);
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
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        WishlistClient wishlistAPI = retrofit.create(WishlistClient.class);
        Call<ResponseBody> call = wishlistAPI.deleteMyWishlist(user.getId(),wishlistID);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                        if (wishlistModelList.size()!=0){
                            wishlistModelList.remove(index);

                            FavoriteFragment.wishlistAdapter.notifyDataSetChanged();
                            if (wishlistModelList.size()==0){
                                FavoriteFragment.no_internet.setVisibility(View.GONE);
                                FavoriteFragment.noData.setVisibility(View.VISIBLE);
                                FavoriteFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                                FavoriteFragment.wistListRecyclerView.setVisibility(View.GONE);
                            }
                        }
                        ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = false;
                        Toast.makeText(context,"Dihapus dari Daftar Favotit",Toast.LENGTH_SHORT).show();

                } else {

                    wishlist.add(index,productID);
                    String error = response.message();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                ProductDetailActivity.running_wishlist_query =false;

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                wishlist.add(index,productID);
                String error =  t.toString();
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                Log.e("debug", "onFailure: ERROR > " + t.toString());

            }
        });



    }

    public static void removeCartList(final String id,final Context context, @Nullable final Dialog loadingdialog, final boolean loadProductData, final TextView badgeCount, final TextView cartTotalAmount) {



        if (loadingdialog!=null) {
            loadingdialog.show();
            loadingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
        }
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        UserPreference userPreference = new UserPreference(context);
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
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CartClient cartAPI = retrofit.create(CartClient.class);
        Call<ResponseBody> call = cartAPI.deleteMyCart(user.getId(),id);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.i("response", String.valueOf(response.body()));
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                    if (loadingdialog!=null) {
                        loadingdialog.dismiss();
                    }
                    return;
                }


                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());


                        cartlist.clear();
                        cartItemModelList.clear();


                        JSONArray carts= new JSONArray(jsonObject.get("carts").toString());

                        if (carts.length()>0){

                            if (cartItemModelList.size()==0){
                                if (loadProductData) {

                                    CartActivity.noData.setVisibility(View.GONE);
                                    CartActivity.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                                    CartActivity.no_internet.setVisibility(View.GONE);
                                    CartActivity.cartItemsRecyclerView.setVisibility(View.VISIBLE);

                                }

                                for (int x = 0; x < carts.length(); x++) {
                                    JSONObject cart = new JSONObject(carts.getJSONObject(x).toString());
                                    cartlist.add(cart.get("product_ID").toString());
                                    Double berat = Double.parseDouble(cart.get("berat").toString());
                                    int ongkir =  ((int)(8000*berat)*(int) cart.get("jumlah"));
                                    cartItemModelList.add( new CartItemModel(
                                            CartItemModel.CART_ITEM,
                                            cart.get("_id").toString(),
                                            cart.get("product_ID").toString(),
                                            DBQueries.url+cart.get("image").toString()
                                            , cart.get("title_product").toString()
                                            , cart.get("price").toString()
                                            , (int) cart.get("jumlah")
                                            , (Boolean) cart.get("in_stock")
                                            , cart.get("average_rating").toString()
                                            , cart.get("satuan").toString()
                                            , berat
                                            , (int)cart.get("min_order")
                                            ,String.valueOf(ongkir)
                                    ));

                                    if (cartlist.contains(ProductDetailActivity.productID)) {
                                        ProductDetailActivity.ALREADY_ADDED_TO_CART = true;
                                    } else {
                                        ProductDetailActivity.ALREADY_ADDED_TO_CART = false;
                                    }
                                }

                                if (loadProductData) {
                                    int totalItemPrice =0;
                                    int totalDeleveryItemPrice =0;
                                    int totalAmount;
                                    int saveAmount =0;
                                    if (cartItemModelList.size()==carts.length()){


                                        for (int x=0;x< cartItemModelList.size();x++){

                                            if ( cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM &&  cartItemModelList.get(x).getInStock()){
                                                int quantity = Integer.parseInt(String.valueOf( cartItemModelList.get(x).getProductQuantity()));

                                                totalItemPrice = totalItemPrice + (Integer.parseInt( cartItemModelList.get(x).getProductPrice())* quantity);
                                                totalDeleveryItemPrice = totalDeleveryItemPrice +((int)(8000* cartItemModelList.get(x).getProductWeight())*quantity);
                                                if (totalItemPrice >= 200000 ) {
                                                    saveAmount = saveAmount+30000;
                                                }



                                            }
                                        }
                                        if(totalItemPrice!=0){
                                            int totalDeliveryDiskon;
                                            totalDeliveryDiskon = totalDeleveryItemPrice-saveAmount;
                                            totalAmount = totalItemPrice +totalDeliveryDiskon;
                                            CartActivity.totalAmount.setText("Rp "+currencyFormatter(String.valueOf(totalAmount)));
                                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                            CartActivity.continueBtn.setVisibility(View.VISIBLE);
                                        }else {
                                            CartActivity.totalAmount.setText("Rp 0");
                                            CartActivity.continueBtn.setVisibility(View.INVISIBLE);
                                        }



                                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                        parent.setVisibility(View.VISIBLE);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                        CartActivity.cartlistAdapter = new CartAdapter(context, CartActivity.cartItemsRecyclerView, linearLayoutManager, cartItemModelList,  true, loadingdialog,badgeCount,false);

                                        CartActivity.cartlistAdapter.SetAdapter(CartActivity.cartlistAdapter);
                                        CartActivity.cartItemsRecyclerView.setAdapter(CartActivity.cartlistAdapter);

                                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        CartActivity.cartItemsRecyclerView.setLayoutManager(linearLayoutManager);




                                        CartActivity.cartlistAdapter.notifyDataSetChanged();
                                        if (!DBQueries.cartItemModelList.isEmpty()) {
                                            CartActivity.continueBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

//                                                    if (!CartActivity.totalAmount.getText().toString().replace(" ","").equals("Rp-")) {
                                                    CartActivity.cartlistAdapter.notifyDataSetChanged();
                                                    CartActivity.cartActivity = (Activity) context;



                                                    DeliveryActivity.cartItemModelList = new ArrayList<>();
//                         PaymentActivity.fromCart = true;

                                                    for (int x = 0; x < DBQueries.cartItemModelList.size() - 1; x++) {
                                                        CartItemModel cartItemModel = DBQueries.cartItemModelList.get(x);
                                                        if (cartItemModel.getInStock()) {
                                                            DeliveryActivity.cartItemModelList.add(cartItemModel);
                                                        }

                                                    }

                                                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));



                                                    if (DBQueries.addressModelList.size() == 0) {
                                                        DBQueries.loadAddresses(context, loadingdialog,true,0,false);
                                                    } else {
                                                        Intent deliveryIntent = new Intent(context, DeliveryActivity.class);
                                                        context.startActivity(deliveryIntent);
                                                    }
//                                                    }else {
//                                                        CartActivity.mycartfragment = null;
//                                                        Toast.makeText(context,"Stok Habis",Toast.LENGTH_SHORT).show();
//                                                    }
                                                }
                                            });
                                        }else {
                                            CartActivity.continueBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    CartActivity.mycartfragment = null;
                                                    Toast.makeText(context,"Keranjang Kosong",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                    }


                                }


                            }

                                if (carts.length() != 0) {
                                    badgeCount.setVisibility(View.VISIBLE);
                                } else {
                                    badgeCount.setVisibility(View.INVISIBLE);
                                }
                                if (carts.length() < 99) {
                                    badgeCount.setText(String.valueOf(carts.length()));
                                } else {
                                    badgeCount.setText("99+");
                                }




                        }else {
                            if (loadProductData) {
                                CartActivity.totalAmount.setText("Rp -");
                                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                parent.setVisibility(View.GONE);
                                CartActivity.no_internet.setVisibility(View.GONE);
                                CartActivity.noData.setVisibility(View.VISIBLE);
                                CartActivity.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                                CartActivity.cartItemsRecyclerView.setVisibility(View.GONE);
                                cartItemModelList.clear();

                            }

                        }
                        ProductDetailActivity.running_cart_query=false;
                        if (loadingdialog!=null) {
                            loadingdialog.dismiss();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                if (loadingdialog!=null) {
                    loadingdialog.dismiss();
                }
            }
        });

    }

    public static void updateCartList(final Context context, @Nullable final Dialog loadingdialog, final boolean loadProductData, final TextView badgeCount, final TextView cartTotalAmount, final boolean loadProductDetail, final LinearLayout addCart,Boolean isRefresh,Boolean isBack) {

        ArrayList<CartInputModel> cartInputModelList = new ArrayList<>();

        for (int i=0; i<cartlist.size();i++){
            cartInputModelList.add(new CartInputModel(cartItemModelList.get(i).get_id(),cartItemModelList.get(i).getProductID(),cartItemModelList.get(i).getProductQuantity()));
        }

        CartInputAPI cartInputAPI = new CartInputAPI(cartInputModelList);

        if (loadingdialog!=null) {
            loadingdialog.show();
            loadingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
        }
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        UserPreference userPreference = new UserPreference(context);
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
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CartClient cartAPI = retrofit.create(CartClient.class);
        Call<ResponseBody> call = cartAPI.updateMyCart(user.getId(),cartInputAPI);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.i("response", String.valueOf(response.body()));
                    Toast.makeText(context,response.code(),Toast.LENGTH_SHORT).show();
                    if (loadingdialog!=null) {
                        loadingdialog.dismiss();
                    }
                    return;
                }

                if (isRefresh){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());


                        cartlist.clear();
                        cartItemModelList.clear();


                        JSONArray carts= new JSONArray(jsonObject.get("carts").toString());

                        if (carts.length()>0){

                            if (cartItemModelList.size()==0){
                                if (loadProductData) {

                                    CartActivity.noData.setVisibility(View.GONE);
                                    CartActivity.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                                    CartActivity.no_internet.setVisibility(View.GONE);
                                    CartActivity.cartItemsRecyclerView.setVisibility(View.VISIBLE);

                                }

                                for (int x = 0; x < carts.length(); x++) {
                                    JSONObject cart = new JSONObject(carts.getJSONObject(x).toString());
                                    cartlist.add(cart.get("product_ID").toString());
                                    Double berat = Double.parseDouble(cart.get("berat").toString());
                                    int ongkir =  ((int)(8000*berat)*(int) cart.get("jumlah"));
                                    cartItemModelList.add( new CartItemModel(
                                            CartItemModel.CART_ITEM,
                                            cart.get("_id").toString(),
                                            cart.get("product_ID").toString(),
                                            DBQueries.url+cart.get("image").toString()
                                            , cart.get("title_product").toString()
                                            , cart.get("price").toString()
                                            , (int) cart.get("jumlah")
                                            , (Boolean) cart.get("in_stock")
                                            , cart.get("average_rating").toString()
                                            , cart.get("satuan").toString()
                                            , berat
                                            , (int)cart.get("min_order")
                                            ,String.valueOf(ongkir)
                                    ));

                                    if (cartlist.contains(ProductDetailActivity.productID)) {
                                        ProductDetailActivity.ALREADY_ADDED_TO_CART = true;
                                    } else {
                                        ProductDetailActivity.ALREADY_ADDED_TO_CART = false;
                                    }
                                }

                                if (loadProductData) {
                                    int totalItemPrice =0;
                                    int totalDeleveryItemPrice =0;
                                    int totalAmount;
                                    int saveAmount =0;
                                    if (cartItemModelList.size()==carts.length()){


                                        for (int x=0;x< cartItemModelList.size();x++){

                                            if ( cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM &&  cartItemModelList.get(x).getInStock()){
                                                int quantity = Integer.parseInt(String.valueOf( cartItemModelList.get(x).getProductQuantity()));

                                                totalItemPrice = totalItemPrice + (Integer.parseInt( cartItemModelList.get(x).getProductPrice())* quantity);
                                                totalDeleveryItemPrice = totalDeleveryItemPrice +((int)(8000* cartItemModelList.get(x).getProductWeight())*quantity);
                                                if (totalItemPrice >= 200000 ) {
                                                    saveAmount = saveAmount+30000;
                                                }



                                            }
                                        }
                                        if(totalItemPrice!=0){
                                            int totalDeliveryDiskon;
                                            totalDeliveryDiskon = totalDeleveryItemPrice-saveAmount;

                                            totalAmount = totalItemPrice +totalDeliveryDiskon;
                                            CartActivity.totalAmount.setText("Rp "+currencyFormatter(String.valueOf(totalAmount)));
                                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                            CartActivity.continueBtn.setVisibility(View.VISIBLE);
                                        }else {
                                            CartActivity.totalAmount.setText("Rp 0");
                                            CartActivity.continueBtn.setVisibility(View.INVISIBLE);
                                        }
                                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                        parent.setVisibility(View.VISIBLE);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                        CartActivity.cartlistAdapter = new CartAdapter(context, CartActivity.cartItemsRecyclerView, linearLayoutManager, cartItemModelList,  true, loadingdialog,badgeCount,false);


                                        CartActivity.cartlistAdapter.SetAdapter(CartActivity.cartlistAdapter);
                                        CartActivity.cartItemsRecyclerView.setAdapter(CartActivity.cartlistAdapter);

                                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        CartActivity.cartItemsRecyclerView.setLayoutManager(linearLayoutManager);




                                        CartActivity.cartlistAdapter.notifyDataSetChanged();
                                        if (!DBQueries.cartItemModelList.isEmpty()) {
                                            CartActivity.continueBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

//                                                    if (!CartActivity.totalAmount.getText().toString().replace(" ","").equals("Rp-")) {
                                                    CartActivity.cartlistAdapter.notifyDataSetChanged();
                                                    CartActivity.cartActivity = (Activity) context;



                                                    DeliveryActivity.cartItemModelList = new ArrayList<>();
//                         PaymentActivity.fromCart = true;

                                                    for (int x = 0; x < DBQueries.cartItemModelList.size() - 1; x++) {
                                                        CartItemModel cartItemModel = DBQueries.cartItemModelList.get(x);
                                                        if (cartItemModel.getInStock()) {
                                                            DeliveryActivity.cartItemModelList.add(cartItemModel);
                                                        }

                                                    }

                                                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));


                                                    if (DBQueries.addressModelList.size() == 0) {
                                                        DBQueries.loadAddresses(context, loadingdialog,true,0,false);
                                                    } else {
                                                        Intent deliveryIntent = new Intent(context, DeliveryActivity.class);
                                                        context.startActivity(deliveryIntent);
                                                    }
//                                                    }else {
//                                                        CartActivity.mycartfragment = null;
//                                                        Toast.makeText(context,"Stok Habis",Toast.LENGTH_SHORT).show();
//                                                    }
                                                }
                                            });
                                        }else {
                                            CartActivity.continueBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    CartActivity.mycartfragment = null;
                                                    Toast.makeText(context,"Keranjang Kosong",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }


                                }


                            }

                                if (carts.length() != 0) {
                                    badgeCount.setVisibility(View.VISIBLE);
                                } else {
                                    badgeCount.setVisibility(View.INVISIBLE);
                                }
                                if (carts.length() < 99) {
                                    badgeCount.setText(String.valueOf(carts.length()));
                                } else {
                                    badgeCount.setText("99+");
                                }




                        }else {
                            if (loadProductData) {
                                CartActivity.totalAmount.setText("Rp -");
                                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                parent.setVisibility(View.GONE);
                                CartActivity.no_internet.setVisibility(View.GONE);
                                CartActivity.noData.setVisibility(View.VISIBLE);
                                CartActivity.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                                CartActivity.cartItemsRecyclerView.setVisibility(View.GONE);
                                cartItemModelList.clear();

                            }

                        }
                        if (isBack){
                            ((Activity)context).finish();
                        }
                        ProductDetailActivity.running_cart_query=false;
                        if (loadingdialog!=null) {
                            loadingdialog.dismiss();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                if (loadingdialog!=null) {
                    loadingdialog.dismiss();
                }
            }
        });

    }



    public static void loadCartList(final Context context, @Nullable final Dialog loadingdialog, final boolean loadProductData, @Nullable final TextView badgeCount, final TextView cartTotalAmount, final boolean loadProductDetail, final LinearLayout addCart) {
        cartlist.clear();
        cartItemModelList.clear();


        if(loadProductData){
            if (loadingdialog!=null) {
                loadingdialog.show();
                loadingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
            }
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        UserPreference userPreference = new UserPreference(context);
        UserModel user = userPreference.getUserPreference("user");

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request request = chain.request();
                        Headers headers = request.headers().newBuilder().add("Authorization", "Bearer " + user.getToken()).build();
                        request = request.newBuilder().headers(headers).build();
                        return chain.proceed(request);
                    }
                })
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CartClient cartAPI = retrofit.create(CartClient.class);
        Call<ResponseBody> call = cartAPI.getMyCart(user.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context,response.code(),Toast.LENGTH_SHORT).show();
                    if (loadingdialog!=null) {
                        loadingdialog.dismiss();
                    }
                    return;
                }else {
                    try {

                       JSONObject jsonObject = new JSONObject(response.body().string());


                        JSONArray carts= new JSONArray(jsonObject.get("carts").toString());
                        if (carts.length()>0){
                            if (cartItemModelList.size()==0) {
                                if (loadProductData) {

                                    CartActivity.noData.setVisibility(View.GONE);
                                    CartActivity.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                                    CartActivity.no_internet.setVisibility(View.GONE);
                                    CartActivity.cartItemsRecyclerView.setVisibility(View.VISIBLE);

                                }

                                for (int x = 0; x < carts.length(); x++) {
                                    JSONObject cart = new JSONObject(carts.getJSONObject(x).toString());
                                    cartlist.add(cart.get("product_ID").toString());
                                    Double berat = Double.parseDouble(cart.get("berat").toString());
                                    int ongkir =  ((int)(8000*berat)*(int) cart.get("jumlah"));
                                    cartItemModelList.add(new CartItemModel(
                                            CartItemModel.CART_ITEM,
                                            cart.get("_id").toString(),
                                            cart.get("product_ID").toString(),
                                            DBQueries.url + cart.get("image").toString()
                                            , cart.get("title_product").toString()
                                            , cart.get("price").toString()
                                            , (int) cart.get("jumlah")
                                            , (Boolean) cart.get("in_stock")
                                            , cart.get("average_rating").toString()
                                            , cart.get("satuan").toString()
                                            , berat
                                            , (int) cart.get("min_order")
                                            ,String.valueOf(ongkir)
                                    ));

                                    if (cartlist.contains(ProductDetailActivity.productID)) {
                                        ProductDetailActivity.ALREADY_ADDED_TO_CART = true;
                                    } else {
                                        ProductDetailActivity.ALREADY_ADDED_TO_CART = false;
                                    }
                                }
                                if (loadProductData) {

                                    int totalItemPrice =0;
                                    int totalDeleveryItemPrice =0;
                                    int totalAmount;
                                    int saveAmount =0;
                                    if (cartItemModelList.size()==carts.length()){


                                        for (int x=0;x< cartItemModelList.size();x++){

                                            if ( cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM &&  cartItemModelList.get(x).getInStock()){
                                                int quantity = Integer.parseInt(String.valueOf( cartItemModelList.get(x).getProductQuantity()));

                                                totalItemPrice = totalItemPrice + (Integer.parseInt( cartItemModelList.get(x).getProductPrice())* quantity);
                                                totalDeleveryItemPrice = totalDeleveryItemPrice +((int)(8000* cartItemModelList.get(x).getProductWeight())*quantity);
                                                if (totalItemPrice >= 200000 ) {
                                                    saveAmount = saveAmount+30000;
                                                }



                                            }
                                        }
                                        if(totalItemPrice!=0){
                                            int totalDeliveryDiskon;
                                            totalDeliveryDiskon = totalDeleveryItemPrice-saveAmount;
                                            totalAmount = totalItemPrice +totalDeliveryDiskon;
                                            CartActivity.totalAmount.setText("Rp "+currencyFormatter(String.valueOf(totalAmount)));
                                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                                            CartActivity.continueBtn.setVisibility(View.VISIBLE);
                                        }else {
                                            CartActivity.totalAmount.setText("Rp 0");
                                            CartActivity.continueBtn.setVisibility(View.INVISIBLE);
                                        }


                                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                        parent.setVisibility(View.VISIBLE);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                        CartActivity.cartlistAdapter = new CartAdapter(context, CartActivity.cartItemsRecyclerView, linearLayoutManager, cartItemModelList, true, loadingdialog,badgeCount,false);
                                        CartActivity.cartlistAdapter.SetAdapter(CartActivity.cartlistAdapter);
                                        CartActivity.cartItemsRecyclerView.setAdapter(CartActivity.cartlistAdapter);

                                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        CartActivity.cartItemsRecyclerView.setLayoutManager(linearLayoutManager);



                                        CartActivity.cartlistAdapter.notifyDataSetChanged();
                                        if (!DBQueries.cartItemModelList.isEmpty()) {
                                            CartActivity.continueBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

//                                                    if (!CartActivity.totalAmount.getText().toString().replace(" ","").equals("Rp-")) {
                                                        CartActivity.cartlistAdapter.notifyDataSetChanged();
                                                        CartActivity.cartActivity = (Activity) context;



                                                        DeliveryActivity.cartItemModelList = new ArrayList<>();
//                         PaymentActivity.fromCart = true;

                                                        for (int x = 0; x < DBQueries.cartItemModelList.size() - 1; x++) {
                                                            CartItemModel cartItemModel = DBQueries.cartItemModelList.get(x);
                                                            if (cartItemModel.getInStock()) {
                                                                DeliveryActivity.cartItemModelList.add(cartItemModel);
                                                            }

                                                        }

                                                        DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));



                                                        if (DBQueries.addressModelList.size() == 0) {
                                                            DBQueries.loadAddresses(context, loadingdialog,true,0,false);
                                                        } else {
                                                            Intent deliveryIntent = new Intent(context, DeliveryActivity.class);
                                                            context.startActivity(deliveryIntent);
                                                        }
//                                                    }else {
//                                                        CartActivity.mycartfragment = null;
//                                                        Toast.makeText(context,"Stok Habis",Toast.LENGTH_SHORT).show();
//                                                    }
                                                }
                                            });
                                        }else {
                                            CartActivity.continueBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    CartActivity.mycartfragment = null;
                                                    Toast.makeText(context,"Keranjang Kosong",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }


                                    }


                                }


                            }


                                if (carts.length() != 0) {
                                    badgeCount.setVisibility(View.VISIBLE);
                                } else {
                                    badgeCount.setVisibility(View.INVISIBLE);
                                }
                                if (carts.length() < 99) {
                                    badgeCount.setText(String.valueOf(carts.length()));
                                } else {
                                    badgeCount.setText("99+");
                                }



                        }else {
                            if (loadProductData) {
                                CartActivity.totalAmount.setText("Rp -");
                                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                parent.setVisibility(View.GONE);
                                CartActivity.no_internet.setVisibility(View.GONE);
                                CartActivity.noData.setVisibility(View.VISIBLE);
                                CartActivity.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                                CartActivity.cartItemsRecyclerView.setVisibility(View.GONE);
                                cartItemModelList.clear();

                            }

                        }
                        if (loadingdialog != null) {
                            loadingdialog.dismiss();
                        }
                        ProductDetailActivity.running_cart_query=false;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                if (loadingdialog!=null) {
                    loadingdialog.dismiss();
                }
            }
        });

    }

    public static void loadCountNotification(final Context context, final Dialog loadingdialog , @Nullable final TextView notifyCount){


        loadingdialog.show();
        loadingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });



        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        UserPreference userPreference = new UserPreference(context);
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
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NotifClient notifAPI = retrofit.create(NotifClient.class);
        Call<Integer> call = notifAPI.getCountMyNotification(user.getId());

        call.enqueue(new Callback<Integer>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()){

                    if (response.body() != 0) {
                        notifyCount.setVisibility(View.VISIBLE);
                    } else {
                        notifyCount.setVisibility(View.INVISIBLE);
                    }
                    if (response.body() < 99) {
                        notifyCount.setText(String.valueOf(response.body() ));
                    } else {
                        notifyCount.setText("99+");
                    }



                    if (loadingdialog!=null) {
                        loadingdialog.dismiss();
                    }

                } else {
                    if (loadingdialog!=null) {
                        loadingdialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                if (loadingdialog!=null) {
                    loadingdialog.dismiss();
                }
            }
        });



    }

    public static void loadNotification(final Context context, final Dialog loadingdialog ){

        notificationModelList.clear();

            loadingdialog.show();
            loadingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });



        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        UserPreference userPreference = new UserPreference(context);
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
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NotifClient notifAPI = retrofit.create(NotifClient.class);
        Call<NotifAPI> call = notifAPI.getMyNotification(user.getId());

        call.enqueue(new Callback<NotifAPI>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<NotifAPI> call, Response<NotifAPI> response) {
                if (response.isSuccessful()){

                    NotifAPI notifAPI = response.body();
                    if (notifAPI.getNotifications().size()>0) {

                        NotificationActivity.noData.setVisibility(View.GONE);
                        NotificationActivity.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                        NotificationActivity.no_internet.setVisibility(View.GONE);
                        NotificationActivity.recyclerView.setVisibility(View.VISIBLE);

                        notificationModelList= notifAPI.getNotifications();

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        NotificationActivity.recyclerView.setLayoutManager(linearLayoutManager);

                        NotificationActivity.adapter = new NotificationAdapter(notificationModelList);
                        NotificationActivity.recyclerView.setAdapter( NotificationActivity.adapter);
                        NotificationActivity.adapter.notifyDataSetChanged();
                        NotificationActivity.adapter.notifyDataSetChanged();


                        MainActivity.notifyCount.setVisibility(View.INVISIBLE);


                    }else {

                        NotificationActivity.no_internet.setVisibility(View.GONE);
                        NotificationActivity.noData.setVisibility(View.VISIBLE);
                        NotificationActivity.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                        NotificationActivity.recyclerView.setVisibility(View.GONE);
                        if (loadingdialog!=null) {
                            loadingdialog.dismiss();
                        }
                        notificationModelList.clear();

                    }



                    if (loadingdialog!=null) {
                        loadingdialog.dismiss();
                    }

                } else {
                    if (loadingdialog!=null) {
                        loadingdialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotifAPI> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                if (loadingdialog!=null) {
                    loadingdialog.dismiss();
                }
            }
        });



    }

    public static void loadWishlist(final Context context, final Dialog loadingdialog,final Boolean loadProductData ){
        wishlist.clear();
        wishlistModelList.clear();
        loadingdialog.show();
        loadingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        UserPreference userPreference = new UserPreference(context);
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
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        WishlistClient wishlistAPI = retrofit.create(WishlistClient.class);
        Call<WishlistAPI> call = wishlistAPI.getMyWishlist(user.getId());

        call.enqueue(new Callback<WishlistAPI>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<WishlistAPI> call, Response<WishlistAPI> response) {
                if (response.isSuccessful()){

                    WishlistAPI wishlistAPI = response.body();
                    if (wishlistAPI.getWishlist().size()>0) {
                        if (loadProductData) {
                            FavoriteFragment.noData.setVisibility(View.GONE);
                            FavoriteFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                            FavoriteFragment.no_internet.setVisibility(View.GONE);
                            FavoriteFragment.wistListRecyclerView.setVisibility(View.VISIBLE);
                        }
                        for (int x = 0; x < wishlistAPI.getWishlist().size(); x++) {

                            wishlist.add(wishlistAPI.getWishlist().get(x).getProductID());
                            if (DBQueries.wishlist.contains(ProductDetailActivity.productID)) {
                                ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = true;
                                if (ProductDetailActivity.addToWishlistBtn != null) {
                                    ProductDetailActivity.addToWishlistBtn.setImageTintList(context.getResources().getColorStateList(R.color.colorAccent4));
                                }
                            } else {
                                if (ProductDetailActivity.addToWishlistBtn != null) {
                                    ProductDetailActivity.addToWishlistBtn.setImageTintList(context.getResources().getColorStateList(R.color.colorAccent3));
                                }
                                ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = false;
                            }
                            if (loadProductData) {
                                wishlistAPI.getWishlist().get(x).setProductImage(url+wishlistAPI.getWishlist().get(x).getProductImage());
                                wishlistModelList.add(wishlistAPI.getWishlist().get(x));

                            }


                            if (wishlistModelList.size() > 1) {
                                Collections.sort(wishlistModelList, new Comparator<WishlistModel>() {
                                    @Override
                                    public int compare(WishlistModel o1, WishlistModel o2) {
                                        if (o1.getProductTitle() == null || o2.getProductTitle() == null)
                                            return 0;
                                        return o1.getProductTitle().compareTo(o2.getProductTitle());

                                    }

                                });

                            }


                            if (loadProductData){
                                FavoriteFragment.wishlistAdapter.notifyDataSetChanged();
                            }

                        }

                    }else {
                        if (loadProductData){
                            FavoriteFragment.no_internet.setVisibility(View.GONE);
                            FavoriteFragment.noData.setVisibility(View.VISIBLE);
                            FavoriteFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                            FavoriteFragment.wistListRecyclerView.setVisibility(View.GONE);
                            if (loadingdialog!=null) {
                                loadingdialog.dismiss();
                            }
                            wishlistModelList.clear();
                        }
                    }



                    ProductDetailActivity.running_wishlist_query=false;
                    if (loadingdialog!=null) {
                        loadingdialog.dismiss();
                    }

                } else {
                    if (loadingdialog!=null) {
                        loadingdialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<WishlistAPI> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                if (loadingdialog!=null) {
                    loadingdialog.dismiss();
                }
            }
        });



    }





    public static void  loadRatingList(final Context context, @Nullable final Dialog loadingDialog){

        if (loadingDialog!=null){
            loadingDialog.show();
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {


                }
            });
        }


        if (!ProductDetailActivity.running_rating_query) {
            ProductDetailActivity.running_rating_query = true;
            myRatedIds.clear();
            myRating.clear();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            UserPreference userPreference = new UserPreference(context);
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
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            StarClient starApi = retrofit.create(StarClient.class);
            Call<StarAPI> call = starApi.getMyStar(user.getId());

            call.enqueue(new Callback<StarAPI>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(Call<StarAPI> call, Response<StarAPI> response) {
                    if (response.isSuccessful()){



                        StarAPI starAPI = response.body();

                        for (int x = 0; x < starAPI.getCount(); x++) {
                            myRatedIds.add(starAPI.getRatings().get(x).getProduct_ID());
                            myRating.add(starAPI.getRatings().get(x).getRating());

                            if (starAPI.getRatings().get(x).getProduct_ID().equals(ProductDetailActivity.productID) ) {
                                ProductDetailActivity.initialRating = Integer.parseInt(String.valueOf(starAPI.getRatings().get(x).getRating())) - 1;
                                if (ProductDetailActivity.rateNowContainer != null) {
                                    ProductDetailActivity.setRatting(ProductDetailActivity.initialRating);

                                }
                            }


                        }


                        ProductDetailActivity.running_rating_query=false;
                        if (loadingDialog!=null) {
                            loadingDialog.dismiss();
                        }

                    } else {
                        if (loadingDialog!=null) {
                            loadingDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StarAPI> call, Throwable t) {
                    Log.e("debug", "onFailure: ERROR > " + t.toString());
                    if (loadingDialog!=null) {
                        loadingDialog.dismiss();
                    }
                }
            });




        }

    }

    public static void loadOrderList(final Context context, final @Nullable Dialog loadingDialog, @Nullable final LinearLayoutManager layoutManager, @Nullable final RecyclerView myOrderRecycleView) {

        if (loadingDialog!=null && layoutManager != null && myOrderRecycleView != null) {
            loadingDialog.show();
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    loadingDialog.setOnDismissListener(null);
                }
            });
        }
        orderModelList.clear();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        UserPreference userPreference = new UserPreference(context);
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
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NotaClient notaApi = retrofit.create(NotaClient.class);
        Call<OrderUserAPI> call = notaApi.getAllMyOrder(user.getId());
        call.enqueue(new Callback<OrderUserAPI>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<OrderUserAPI> call, Response<OrderUserAPI> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context,response.code(),Toast.LENGTH_SHORT).show();
                    if (loadingDialog!=null) {
                        loadingDialog.dismiss();
                    }
                    return;
                }
                OrderUserAPI orderUserAPI = response.body();

                if (orderUserAPI.getOrderListModels().size()!=0){
                    if (layoutManager!=null&&myOrderRecycleView!=null) {

                        MyOrdersFragment.noData.setVisibility(View.GONE);
                        MyOrdersFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                        MyOrdersFragment.no_internet.setVisibility(View.GONE);
                        MyOrdersFragment.myOrderRecycleView.setVisibility(View.VISIBLE);
                    }

                    DBQueries.orderModelList = orderUserAPI.getOrderListModels();


                    if (layoutManager != null && myOrderRecycleView != null) {
                        loadingDialog.show();
                        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        });

                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        myOrderRecycleView.setLayoutManager(layoutManager);
                        MyOrdersFragment.myOrderAdapter = new MyOrderAdapter(DBQueries.orderModelList, loadingDialog);
                        myOrderRecycleView.setAdapter(MyOrdersFragment.myOrderAdapter);
                        MyOrdersFragment.myOrderAdapter.notifyDataSetChanged();
                        loadingDialog.dismiss();

                    }
                    loadingDialog.dismiss();


                }else {

                    if (layoutManager!=null&&myOrderRecycleView!=null) {

                        MyOrdersFragment.no_internet.setVisibility(View.GONE);
                        MyOrdersFragment.noData.setVisibility(View.VISIBLE);
                        MyOrdersFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                        MyOrdersFragment.myOrderRecycleView.setVisibility(View.GONE);

                    }
                    loadingDialog.dismiss();
                    orderModelList.clear();
                }

            }

            @Override
            public void onFailure(Call<OrderUserAPI> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
            }
        });
    }

    public static void loadFragmentData (final Context context, final int index, final Dialog loadingDialog, final RecyclerView homePageRecyclerView, String categoryName,String label, Boolean isCategory){

        loadingDialog.show();
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        lists.clear();
        DBQueries.lists.add(new ArrayList<HomePageModel>());
        if(!isCategory){
            categoryModelList.clear();
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CategoryClient categoryApi = retrofit.create(CategoryClient.class);

        if(!isCategory) {



                    Call<ResponseBody> call2 = categoryApi.getTopDeal(categoryName);

                    call2.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()){
                                HomeFragment.maintance(true);
                                Toast.makeText(context,response.code(),Toast.LENGTH_SHORT).show();
                                return;


                            }


                                try {

                                    JSONArray jsonArray = new JSONArray(response.body().string());
                                    JSONObject jobj = new JSONObject(jsonArray.get(0).toString());
                                    if (!(Boolean) jobj.get("status")){
                                        HomeFragment.maintance((Boolean) jobj.get("status"));
                                        loadingDialog.dismiss();
                                    }else {
                                        JSONArray arrayTopdeals = new JSONArray(jobj.get("top_deals").toString());
                                        for (int i = 0; i < arrayTopdeals.length(); i++) { // Walk through the Array.
                                            JSONObject topdeal = arrayTopdeals.getJSONObject(i);


                                            if((int)topdeal.get("view_type")==0){
                                                List<SliderModel> sliderModelList = new ArrayList<>();
                                                JSONArray banner = topdeal.getJSONArray("move_banner");
                                                for (int x = 0;x<banner.length();x++){
                                                    JSONObject banneritem = banner.getJSONObject(x);
                                                    sliderModelList.add( new SliderModel(
                                                            banneritem.get("_id").toString(),
                                                            url+banneritem.get("banner").toString(),
                                                            banneritem.get("banner_background").toString().substring(0,7),
                                                            StringtoDate(banneritem.get("date").toString()))
                                                    );

                                                }
                                                lists.get(index).add(new HomePageModel(topdeal.get("_id").toString(),0,sliderModelList));
                                            }else  if((int)topdeal.get("view_type")==1){
                                                lists.get(index).add(new HomePageModel(
                                                        topdeal.get("_id").toString(),
                                                        1,
                                                        url+topdeal.get("strip_ad_banner").toString(),
                                                        topdeal.get("background").toString().substring(0,7)));
                                            } else if ((int)topdeal.get("view_type")==3) {
                                                List<WishlistModel> vieAllProductList = new ArrayList<>();
                                                List<HorizontalProductScrollModel>horizontalProductScrollModelList = new ArrayList<>();
                                                JSONArray horisontalView = topdeal.getJSONArray("horisontal_view");

                                                for (int x = 0;x<horisontalView.length();x++){
                                                    JSONObject horisontalViewItem = horisontalView.getJSONObject(x);
                                                    horizontalProductScrollModelList.add(new HorizontalProductScrollModel(
                                                            horisontalViewItem.get("_id").toString(),
                                                            horisontalViewItem.get("product_ID").toString(),
                                                            url+horisontalViewItem.get("image").toString(),
                                                            horisontalViewItem.get("title_product").toString(),
                                                            horisontalViewItem.get("cutted_price").toString(),
                                                            horisontalViewItem.get("price").toString()
                                                    ));


                                                    vieAllProductList.add(new WishlistModel(
                                                            horisontalViewItem.get("_id").toString(),
                                                            horisontalViewItem.get("product_ID").toString(),
                                                            url+horisontalViewItem.get("image").toString(),
                                                            horisontalViewItem.get("title_product").toString(),
                                                            horisontalViewItem.get("average_rating").toString(),
                                                            (int)horisontalViewItem.get("total_ratings"),
                                                            horisontalViewItem.get("price").toString(),
                                                            horisontalViewItem.get("cutted_price").toString(),
                                                            (Boolean)horisontalViewItem.get("in_stock"),
                                                            horisontalViewItem.get("satuan").toString()

                                                    ));

                                                }
                                                lists.get(index).add(new HomePageModel(
                                                        topdeal.get("_id").toString(),
                                                        2,
                                                        topdeal.get("title_background").toString(),
                                                        topdeal.get("layout_background").toString().substring(0,7),
                                                        horizontalProductScrollModelList,
                                                        vieAllProductList));

                                            }else if ((int)topdeal.get("view_type")==2){
                                                List<HorizontalProductScrollModel>GridLayoutModelList = new ArrayList<>();
                                                JSONArray gridView = topdeal.getJSONArray("grid_view");

                                                for (int x = 0;x<gridView.length();x++){
                                                    JSONObject gridViewItem = gridView.getJSONObject(x);
                                                    GridLayoutModelList.add(new HorizontalProductScrollModel(
                                                            gridViewItem.get("_id").toString(),
                                                            gridViewItem.get("product_ID").toString(),
                                                            url+gridViewItem.get("image").toString(),
                                                            gridViewItem.get("title_product").toString(),
                                                            gridViewItem.get("cutted_price").toString(),
                                                            gridViewItem.get("price").toString()
                                                    ));
                                                }
                                                lists.get(index).add(new HomePageModel(
                                                        topdeal.get("_id").toString(),
                                                        3,
                                                        topdeal.get("title_background").toString(),
                                                        topdeal.get("layout_background").toString().substring(0,7),
                                                        GridLayoutModelList));
                                            }


                                        }


                                        Gson gson2 = new GsonBuilder()
                                                .setLenient()
                                                .create();

                                        final OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                                                .readTimeout(60, TimeUnit.SECONDS)
                                                .connectTimeout(60, TimeUnit.SECONDS)
                                                .build();
                                        Retrofit retrofit2 = new Retrofit.Builder()
                                                .baseUrl(url)
                                                .client(okHttpClient2)
                                                .addConverterFactory(GsonConverterFactory.create(gson2))
                                                .build();

                                        CategoryClient categoryApi2 = retrofit2.create(CategoryClient.class);


                                        Call<List<CategoryModel>> call3 = categoryApi2.getCategories();

                                        call3.enqueue(new Callback<List<CategoryModel>>() {
                                            @Override
                                            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                                                if (!response.isSuccessful()){
                                                    Toast.makeText(context,response.code(),Toast.LENGTH_SHORT).show();
                                                    return;

                                                }




                                                categoryModelList= response.body();

                                                for (int i = 0; i < categoryModelList.size(); i++) {
                                                    categoryModelList.get(i).setCategoryIconLink(url + categoryModelList.get(i).getCategoryIconLink());
                                                }

                                                if (lists.get(index).size()!=0){
                                                    if (lists.get(index).get(1).getType()!=4) {
                                                        lists.get(index).add(1, new HomePageModel(4, categoryModelList));
                                                        HomePageAdapter homePageAdapter = new HomePageAdapter(context, lists.get(index), label);
                                                        homePageRecyclerView.setAdapter(homePageAdapter);
                                                        homePageAdapter.notifyDataSetChanged();
                                                    }
                                                }


                                                loadingDialog.dismiss();
                                            }

                                            @Override
                                            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                                                HomeFragment.maintance(true);
                                                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                                                Log.i("error",t.getMessage());
                                                loadingDialog.dismiss();
                                            }
                                        });
                                    }





                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }




                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            HomeFragment.maintance(true);
                            Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.i("error",t.getMessage());
                            loadingDialog.dismiss();
                        }
                    });





        }else {
            Call<ResponseBody> call2 = categoryApi.getTopDeal(categoryName);

            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()){
                        HomeFragment.maintance(true);
                        Toast.makeText(context,response.code(),Toast.LENGTH_SHORT).show();
                        return;

                    }

                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        JSONObject jobj = new JSONObject(jsonArray.get(0).toString());

                        if (!(Boolean) jobj.get("status")){
                            HomeFragment.maintance((Boolean) jobj.get("status"));
                            loadingDialog.dismiss();
                        }else {
                            JSONArray arrayTopdeals = new JSONArray(jobj.get("top_deals").toString());
                            for (int i = 0; i < arrayTopdeals.length(); i++) { // Walk through the Array.
                                JSONObject topdeal = arrayTopdeals.getJSONObject(i);


                                if ((int) topdeal.get("view_type") == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    JSONArray banner = topdeal.getJSONArray("move_banner");
                                    for (int x = 0; x < banner.length(); x++) {
                                        JSONObject banneritem = banner.getJSONObject(x);
                                        sliderModelList.add(new SliderModel(
                                                banneritem.get("_id").toString(),
                                                url + banneritem.get("banner").toString(),
                                                banneritem.get("banner_background").toString().substring(0, 7),
                                                StringtoDate(banneritem.get("date").toString()))
                                        );

                                    }
                                    lists.get(index).add(new HomePageModel(topdeal.get("_id").toString(), 0, sliderModelList));
                                } else if ((int) topdeal.get("view_type") == 1) {
                                    lists.get(index).add(new HomePageModel(
                                            topdeal.get("_id").toString(),
                                            1,
                                            url + topdeal.get("strip_ad_banner").toString(),
                                            topdeal.get("background").toString().substring(0, 7)));
                                } else if ((int) topdeal.get("view_type") == 3) {
                                    List<WishlistModel> vieAllProductList = new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                    JSONArray horisontalView = topdeal.getJSONArray("horisontal_view");

                                    for (int x = 0; x < horisontalView.length(); x++) {
                                        JSONObject horisontalViewItem = horisontalView.getJSONObject(x);
                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(
                                                horisontalViewItem.get("_id").toString(),
                                                horisontalViewItem.get("product_ID").toString(),
                                                url + horisontalViewItem.get("image").toString(),
                                                horisontalViewItem.get("title_product").toString(),
                                                horisontalViewItem.get("cutted_price").toString(),
                                                horisontalViewItem.get("price").toString()
                                        ));


                                        vieAllProductList.add(new WishlistModel(
                                                horisontalViewItem.get("_id").toString(),
                                                horisontalViewItem.get("product_ID").toString(),
                                                url + horisontalViewItem.get("image").toString(),
                                                horisontalViewItem.get("title_product").toString(),
                                                horisontalViewItem.get("average_rating").toString(),
                                                (int) horisontalViewItem.get("total_ratings"),
                                                horisontalViewItem.get("cutted_price").toString(),
                                                horisontalViewItem.get("price").toString(),
                                                (Boolean) horisontalViewItem.get("in_stock"),
                                                horisontalViewItem.get("satuan").toString()

                                        ));

                                    }
                                    lists.get(index).add(new HomePageModel(
                                            topdeal.get("_id").toString(),
                                            2,
                                            topdeal.get("title_background").toString(),
                                            topdeal.get("layout_background").toString().substring(0, 7),
                                            horizontalProductScrollModelList,
                                            vieAllProductList));

                                } else if ((int) topdeal.get("view_type") == 2) {
                                    List<HorizontalProductScrollModel> GridLayoutModelList = new ArrayList<>();
                                    JSONArray gridView = topdeal.getJSONArray("grid_view");

                                    for (int x = 0; x < gridView.length(); x++) {
                                        JSONObject gridViewItem = gridView.getJSONObject(x);
                                        GridLayoutModelList.add(new HorizontalProductScrollModel(
                                                gridViewItem.get("_id").toString(),
                                                gridViewItem.get("product_ID").toString(),
                                                url + gridViewItem.get("image").toString(),
                                                gridViewItem.get("title_product").toString(),
                                                gridViewItem.get("cutted_price").toString(),
                                                gridViewItem.get("price").toString()
                                        ));
                                    }
                                    lists.get(index).add(new HomePageModel(
                                            topdeal.get("_id").toString(),
                                            3,
                                            topdeal.get("title_background").toString(),
                                            topdeal.get("layout_background").toString().substring(0, 7),
                                            GridLayoutModelList));
                                }


                            }
                            if (lists.get(index).get(1).getType()!=4) {
                                lists.get(index).add(1, new HomePageModel(4, categoryModelList));
                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(context, lists.get(index), label);
                            homePageRecyclerView.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            loadingDialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    HomeFragment.maintance(true);
                    Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.i("error",t.getMessage());
                    loadingDialog.dismiss();
                }
            });
        }


    }

    public static void clearData(Context context){
        categoryModelList.clear();
        if (!MainActivity.showCart) {
            lists.clear();
        }
        orderModelList.clear();
        loadedCategoriesNames.clear();
        wishlist.clear();
        wishlistModelList.clear();
        myRating.clear();
        myRatedIds.clear();
        cartlist.clear();
        cartItemModelList.clear();
        addressModelList.clear();


        UserPreference userPreference = new UserPreference(context);
        userPreference.setUserPreference("user", null);

    }

    public static Date StringtoDate(String dateString){

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" , Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC +7"));
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }


    public static String currencyFormatter(String num) {
        double m = Double.parseDouble(num);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(m);
    }
}
