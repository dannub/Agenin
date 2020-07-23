package com.agenin.id.Adapter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.agenin.id.Activity.OrderDetailsActivity;
import com.agenin.id.Activity.ProductDetailActivity;
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.StarClient;
import com.agenin.id.Model.OrderListModel;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Viewholder> {

    private List<OrderListModel> myOrderItemModelList;



    private Dialog loadingDialog;

    public MyOrderAdapter(List<OrderListModel> myOrderItemModelList, Dialog loadingDialog) {
        this.myOrderItemModelList = myOrderItemModelList;
        this.loadingDialog = loadingDialog;
    }

    @NonNull
    @Override
    public MyOrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.Viewholder viewholder, int position) {
        String nota_id = myOrderItemModelList.get(position).getNota_id();
        String resource = myOrderItemModelList.get(position).getProduct_ID().getImage().get(0);
        int ratting = myOrderItemModelList.get(position).getRating();
        String title = myOrderItemModelList.get(position).getProduct_ID().getTitle_product();
        String productID = myOrderItemModelList.get(position).getProduct_ID().get_id();
       boolean ordered = myOrderItemModelList.get(position).getConfirmed();
       boolean packed= myOrderItemModelList.get(position).getPacked();
       boolean shipped= myOrderItemModelList.get(position).getShipped();
       boolean delivered= myOrderItemModelList.get(position).getDelivered();
       boolean canceled= myOrderItemModelList.get(position).getCanceled();

       Date ordered_date= DBQueries.StringtoDate(myOrderItemModelList.get(position).getConfirmed_date());
       Date packed_date=DBQueries.StringtoDate(myOrderItemModelList.get(position).getPacked_date());
        Date shipped_date=DBQueries.StringtoDate(myOrderItemModelList.get(position).getShipped_date());
        Date delivered_date=DBQueries.StringtoDate(myOrderItemModelList.get(position).getDelivered_date());
        Date canceled_date=DBQueries.StringtoDate(myOrderItemModelList.get(position).getCanceled_date());

        Date tgl_pesan=DBQueries.StringtoDate(myOrderItemModelList.get(position).getOrdered_date());


        String ket_kirim=myOrderItemModelList.get(position).getKet_kirim();
        String metode_kirim=myOrderItemModelList.get(position).getMetode_kirim();

        viewholder.setData(nota_id,resource,title,productID,ratting,ordered,packed,shipped,delivered,canceled,ordered_date,packed_date,shipped_date,delivered_date,canceled_date,tgl_pesan,ket_kirim,metode_kirim,position);

    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private ImageView orderIndicator;
        private TextView productTitle;
        private TextView notaId;
        private TextView deliveryStatus;
        private LinearLayout rateNowContainer;
        private int initialRating = -1;


        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            notaId = itemView.findViewById(R.id.nota);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            orderIndicator = itemView.findViewById(R.id.order_status_indicator);
            deliveryStatus = itemView.findViewById(R.id.order_delivered_day);
            rateNowContainer = itemView.findViewById(R.id.rate_now_container);


        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void setData(String notaid,String resource, String title, final String productID, final int ratting, boolean ordered, boolean packed, boolean shipped, boolean delivered, boolean canceled, Date ordered_date, Date packed_date, Date shipped_date, Date delivered_date, Date canceled_date, Date tgl_Pesan, String ket_kirim, String metode_kirim, final int position) {

            Glide.with(itemView.getContext()).load(DBQueries.url+resource).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(productImage);
            productTitle.setText(title);

            notaId.setText("Nota: "+notaid);






            if(canceled) {
                deliveryStatus.setText("Pesanan Dibatalkan \n"+changeDate(canceled_date));
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorAccent4)));
            }else {
                if (delivered){
                    orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.delivered)));
                    deliveryStatus.setText("Pesanan telah sampai \n"+changeDate(delivered_date));

                }else {
                    if (shipped){
                        orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.shipping)));
                        deliveryStatus.setText("Pesanan sedang dikirim \n"+changeDate(shipped_date));
                    }else {
                        if (packed){
                            orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.packed)));
                            deliveryStatus.setText("Pesanan telah dikemas \n"+changeDate(packed_date));
                        }else {
                            if (ordered){
                                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.ordered)));
                                deliveryStatus.setText("Telah dikonfirmasi \n" + changeDate(ordered_date));
                            }else {
                                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.pesanan)));
                                deliveryStatus.setText("Telah memesan menunggu konfirmasi \n" + changeDate(tgl_Pesan));
                            }
                        }
                    }
                }

            }



            /////ratting layout

            if (DBQueries.myRatedIds.contains(productID)){
                int index = DBQueries.myRatedIds.indexOf(productID);
                initialRating = Integer.parseInt(String.valueOf(DBQueries.myRating.get(index)))-1;
                setRatting(initialRating);
            }


            for (int x = 0; x <rateNowContainer.getChildCount();x++){
                final int starPosition = x;
                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                            if (starPosition!= initialRating) {
                                loadingDialog.show();
                                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {

                                    }
                                });
                                if (!ProductDetailActivity.running_rating_query) {
                                    ProductDetailActivity.running_rating_query = true;

                                    setRatting(starPosition);

                                    Gson gson = new GsonBuilder()
                                            .setLenient()
                                            .create();

                                    UserPreference userPreference = new UserPreference(itemView.getContext());
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

                                    StarClient starApi = retrofit.create(StarClient.class);
                                    Call<ResponseBody> call = starApi.setMyStar(user.getId(),productID,starPosition+1);

                                    call.enqueue(new Callback<ResponseBody>() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()){


                                                if (DBQueries.myRatedIds.contains(productID)) {

                                                    DBQueries.myRating.set(DBQueries.myRatedIds.indexOf(productID),  starPosition + 1);

//                                                    TextView oldrating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
//                                                    TextView finalrating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
//                                                    oldrating.setText(String.valueOf(Integer.parseInt(oldrating.getText().toString()) - 1));
//                                                    finalrating.setText(String.valueOf(Integer.parseInt(finalrating.getText().toString()) + 1));

                                                } else {
                                                    DBQueries.myRatedIds.add(productID);
                                                    DBQueries.myRating.add( starPosition + 1);





                                                    Toast.makeText(itemView.getContext(), "Terima Kasih Telah Menilai", Toast.LENGTH_SHORT).show();
                                                }


                                                initialRating = starPosition;


//
//                                                if (DBQueries.wishlist.contains(productID) && DBQueries.wishlistModelList.size() != 0) {
//                                                    int index = DBQueries.wishlist.indexOf(productID);
////                                                    DBQueries.wishlistModelList.get(index).setRatting(averageRatings.getText().toString());
////                                                    DBQueries.wishlistModelList.get(index).setTotalRattings(Integer.parseInt(totalRatingsFigure.getText().toString()));
//                                                }






                                            } else {

                                                setRatting(initialRating);
                                                Toast.makeText(itemView.getContext(), response.message(), Toast.LENGTH_SHORT).show();
                                            }
                                            ProductDetailActivity.running_rating_query = false;
                                            loadingDialog.dismiss();
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            ProductDetailActivity.running_rating_query = false;
                                            setRatting(initialRating);
                                            Log.e("debug", "onFailure: ERROR > " + t.toString());
                                            loadingDialog.dismiss();
                                        }
                                    });


                                }

                        }
                    }
                });
            }
            /////ratting layout






            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailIntent = new Intent(itemView.getContext(), OrderDetailsActivity.class);

                    orderDetailIntent.putExtra("nota_id",notaid);
                    orderDetailIntent.putExtra("product_id",productID);
                    itemView.getContext().startActivity(orderDetailIntent);
                }
            });



        }

        private String changeDate(Date date){

            SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
            String day = sdf2.format(date);
            return day;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setRatting(int starPosition) {

                for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                    ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
                    starBtn.setImageTintList(itemView.getContext().getResources().getColorStateList(R.color.colorAccent3));
                    if (x <= starPosition) {
                        starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                    }

                }

        }





    }


}
