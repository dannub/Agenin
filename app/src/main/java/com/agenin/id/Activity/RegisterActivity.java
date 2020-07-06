package com.agenin.id.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.agenin.id.Fragment.ui.DaftarFragment;
import com.agenin.id.Fragment.ui.SignInFragment;
import com.agenin.id.Fragment.ui.SignUpFragment;
import com.agenin.id.Fragment.ui.TransferDaftarFragment;
import com.agenin.id.MyFirebaseMessagingService;
import com.agenin.id.Progress.ProgressRequestBody;
import com.agenin.id.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class RegisterActivity extends AppCompatActivity implements  ProgressRequestBody.UploadCallbacks {

    private FrameLayout frameLayout;
    public static  boolean onResetPasswordFragment = false;
    public static  boolean onDaftarFragment = false;
    public static  boolean onTransferDaftarFragment = false;
    public static boolean setSignUpFragment = false;

    public static Context context;

    //Notification
    public   static  final int NOTIFY_ID = 0;
    public   static  final String CHANNEL_ID = "Agenin";
    public   static  final String CHANNEL_NAME = "Agenin";
    private  static  final String CHANNEL_DESC = "Agenin Notification";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        frameLayout = findViewById(R.id.frame_layout_register);


        context = RegisterActivity.this;
        if (setSignUpFragment){
            setSignUpFragment = false;
            setDefaultFragment(new SignUpFragment());
        }else {
            setDefaultFragment(new SignInFragment());
        }

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            SignUpFragment.disableCloseBtn = false;
            SignUpFragment.disableCloseBtn = false;

            if(onResetPasswordFragment){
                onResetPasswordFragment = false;
                setFragment(new SignInFragment());
                return false;
            }
            if(onDaftarFragment){
                onDaftarFragment = false;
                setFragment(new TransferDaftarFragment());
                return false;
            }else {
                if(onTransferDaftarFragment){
                    onTransferDaftarFragment = false;
                    setFragment(new SignUpFragment());
                    return false;
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void setDefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();

    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_left);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onProgressUpdate(int percentage) {
        if(onDaftarFragment) {
            DaftarFragment.progressBar.setProgress(percentage);
        }
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadStart() {

    }
}
