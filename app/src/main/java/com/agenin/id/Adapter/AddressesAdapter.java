package com.agenin.id.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.agenin.id.Activity.AddAddressActivity;
import com.agenin.id.Activity.DeliveryActivity;
import com.agenin.id.Activity.MyAddressesActivity;
import com.agenin.id.DBQueries;
import com.agenin.id.Fragment.ui.ProfilFragment;
import com.agenin.id.Interface.AddressClient;
import com.agenin.id.Model.AddressModel;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
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


public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private List<AddressModel> addressModelList;
    private int MODE;
    private int preSelectedPosition;
    private boolean refreesh =false;
    private Context context;
    private Dialog loadingDialog;

    public AddressesAdapter(Context context,List<AddressModel> addressModelList, int MODE, Dialog loadingDialog) {
        this.context=context;
        this.addressModelList = addressModelList;
        this.MODE = MODE;
        preSelectedPosition = DBQueries.selectedAddress;

        this.loadingDialog = loadingDialog;


    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addresses_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {

        String _id = addressModelList.get(position).get_id();
        String city= addressModelList.get(position).getProvinsi();
        String locality=addressModelList.get(position).getKabupaten();
        String flatNo=addressModelList.get(position).getKecamatan();
        String pincode= addressModelList.get(position).getKodepos();

        String landmark= addressModelList.get(position).getAlamat();
        String name= addressModelList.get(position).getNama();
        String mobileNo= addressModelList.get(position).getNo_telepon();
        String alternativeMobileNo=addressModelList.get(position).getNo_alternatif();
        String state=addressModelList.get(position).getNegara();
        Boolean selected = addressModelList.get(position).getSelected();
        viewholder.setData(_id,city,locality,flatNo,pincode,landmark,name,mobileNo,alternativeMobileNo,state,selected,position);
    }

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView fullname;
        private TextView address;
        private TextView pincode;
        private ImageView icon;
        private LinearLayout manage_address_container;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pincode = itemView.findViewById(R.id.pincode);
            icon = itemView.findViewById(R.id.icon_view);
            manage_address_container = itemView.findViewById(R.id.manage_address_container);
            manage_address_container.setVisibility(View.GONE);
        }

        private  void setData(String _id,String city, String locality, String flatNo, String pincode, String landmark, String name, String mobileNo, String alternativeMobileNo, String state, Boolean selected, final int position){
            if (alternativeMobileNo.equals("")){
                fullname.setText(name + " | "+mobileNo);
            }else {
                fullname.setText(name + " | "+mobileNo+" atau "+alternativeMobileNo);
            }
            if (landmark.equals("")) {
                address.setText(city+" "+locality +" "+flatNo+" "+state);
            }else {
                address.setText(landmark+" "+city+" "+locality +" "+flatNo+" "+state);
            }

            this.pincode.setText(pincode);

            if(MODE == DeliveryActivity.SELECT_ADDRESS){
                icon.setImageResource(R.drawable.check);
                if(selected){
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                }else {
                    icon.setVisibility(View.INVISIBLE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(preSelectedPosition != position){
                            addressModelList.get(position).setSelected(true);
                            addressModelList.get(preSelectedPosition).setSelected(false);
                            MyAddressesActivity.refreshItem(preSelectedPosition,position);
                            preSelectedPosition =position;
                            DBQueries.selectedAddress = position;
                        }

                    }
                });

            }
            else if(MODE == ProfilFragment.MANAGE_ADDRESS){
                manage_address_container.setVisibility(View.GONE);
                manage_address_container.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//edit address
                        Intent addAddressIntent = new Intent(itemView.getContext(), AddAddressActivity.class);
                        addAddressIntent.putExtra("INTENT","update_address");
                        addAddressIntent.putExtra("addressid",_id);
                        addAddressIntent.putExtra("index",position);
                        itemView.getContext().startActivity(addAddressIntent);
                        refreesh = false;
                    }
                });

                manage_address_container.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//remove address

                        loadingDialog.show();
                        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        });
                        int x = 0;
                        int selected = -1;

                        for (int i =0;i<addressModelList.size();i++){
                            if (i!=position){
                                x++;

                                if (addressModelList.get(position).getSelected()){
                                    if (position - 1 >= 0){
                                        if (x==position){

                                            selected = x;
                                        }
                                    }else {
                                        if (x==1){
                                            selected = x;
                                        }
                                    }
                                }else {

                                    if (addressModelList.get(i).getSelected()){
                                        selected=x;
                                    }
                                }
                            }
                        }

                        final int finalSelected = selected;


                        Gson gson = new GsonBuilder()
                                .setLenient()
                                .create();

                        UserPreference userPreference = new UserPreference(context);
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
                                .readTimeout(2, TimeUnit.MINUTES)
                                .connectTimeout(2, TimeUnit.MINUTES)
                                .build();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(DBQueries.url)
                                .client(okHttpClient)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();

                        AddressClient addressAPI = retrofit.create(AddressClient.class);
                        Call<ResponseBody> call = addressAPI.deleteSelectedAddress(user.getId(),_id);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (!response.isSuccessful()) {
                                    Log.i("response", String.valueOf(response.body()));
                                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                                    if (loadingDialog != null) {
                                        loadingDialog.dismiss();
                                    }
                                    return;
                                }
                                ///didalem
                                DBQueries.addressModelList.remove(position);
                                if (finalSelected!= -1) {
                                    DBQueries.selectedAddress = finalSelected - 1;
                                    DBQueries.addressModelList.get(finalSelected - 1).setSelected(true);
                                }
                                MyAddressesActivity.addressesSaved.setText(" "+ DBQueries.addressModelList.size() +" saved addresses ");
                                notifyDataSetChanged();
                                loadingDialog.dismiss();
                                ///didalem
                            }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("debug", "onFailure: ERROR > " + t.toString());
                            if (loadingDialog!=null) {
                                loadingDialog.dismiss();
                            }
                        }
                    });
                        loadingDialog.dismiss();
                        refreesh = false;
                    }
                });

                icon.setImageResource(R.drawable.vertical_date);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manage_address_container.setVisibility(View.VISIBLE);
                        if (refreesh){
                            MyAddressesActivity.refreshItem(preSelectedPosition,preSelectedPosition);
                        }else {
                            refreesh = true;
                        }
                        preSelectedPosition = position;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manage_address_container.setVisibility(View.GONE);
                        MyAddressesActivity.refreshItem(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition = -1;
                    }
                });
            }
        }
    }
}
