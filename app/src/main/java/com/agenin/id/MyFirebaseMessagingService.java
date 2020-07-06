package com.agenin.id;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.agenin.id.Activity.MainActivity;
import com.agenin.id.Activity.RegisterActivity;
import com.agenin.id.Helper.NotificationHelper;
import com.agenin.id.Interface.UserClient;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


//    private FirebaseUser user;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        UserPreference userPreference = new UserPreference(getApplicationContext());
//        userPreference.setUserPreference("user",null);
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user!=null){
//
//
//            if(userPreference.getUserPreference("user")==null){
//                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                        .readTimeout(60, TimeUnit.SECONDS)
//                        .connectTimeout(60, TimeUnit.SECONDS)
//                        .build();
//
//                Retrofit.Builder builder = new Retrofit.Builder()
//                        .baseUrl(DBQueries.url)
//                        .client(okHttpClient)
//                        .addConverterFactory(GsonConverterFactory.create());
//
//                Retrofit retrofit = builder.build();
//                UserClient client = retrofit.create(UserClient.class);
//
//                Call<UserModel> call =    client.login(user.getEmail(), user.getUid());
//
//                call.enqueue(new Callback<UserModel>() {
//                    @Override
//                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                        if (response.isSuccessful()){
//
//                            UserModel userModel = response.body();
//                            userPreference.setUserPreference("user", userModel);
//                            Call<ResponseBody> call2 = client.updateToken(userModel.getId(), s);
//                            call2.enqueue(new Callback<ResponseBody>() {
//                                @Override
//                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                    if (!response.isSuccessful()) {
//                                        Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_SHORT).show();
//                                        return;
//                                    }
//                                }
//
//                            @Override
//                            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                Log.e("debug", "onFailure: ERROR > " + t.toString());
//
//
//                            }
//                        });
//
//                        } else {
//
//
//                            UserPreference userPreference = new UserPreference(getApplicationContext());
//                            userPreference.setUserPreference("user", null);
//
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserModel> call, Throwable t) {
//                        Log.e("debug", "onFailure: ERROR > " + t.toString());
//
//
//                    }
//                });
//
//            }
//        }

        Log.i("token",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);






        if (remoteMessage.getNotification()!=null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.i("title",title);
            Log.i("body",body);



            NotificationHelper.displayNotification(getApplicationContext(),title,body);
        }
    }


}
