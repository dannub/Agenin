package com.agenin.id.Fragment.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agenin.id.Activity.MainActivity;
import com.agenin.id.Activity.RegisterActivity;
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.UserClient;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import info.hoang8f.widget.FButton;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 */
public class SignInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    FButton signInBtn;
    private TextView dontHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private TextInputEditText email,password;

    private ImageButton closeBtn;

    private TextView forgotPassword;

    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static Boolean isLogin;

    public  static boolean disableCloseBtn =false;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        signInBtn=(FButton) itemView.findViewById(R.id.btn_signin);
        signInBtn.setButtonColor(getResources().getColor(R.color.colorAccent));
        dontHaveAnAccount = (TextView) itemView.findViewById(R.id.textView3);
        parentFrameLayout = getActivity().findViewById(R.id.frame_layout_register);
        email = itemView.findViewById(R.id.txtemail);
        password = itemView.findViewById(R.id.txtpassword);


        closeBtn = itemView.findViewById(R.id.close_signin);
        signInBtn = itemView.findViewById(R.id.btn_signin);

        forgotPassword = itemView.findViewById(R.id.forgot_pwd);

        progressBar = itemView.findViewById(R.id.progress_signin);

        firebaseAuth = FirebaseAuth.getInstance();

        isLogin = false;

        if (disableCloseBtn){
            closeBtn.setVisibility(View.GONE);
        }else {
            closeBtn.setVisibility(View.VISIBLE);
        }

        signInBtn.setButtonColor(itemView.getResources().getColor(R.color.colorPrimary));
        signInBtn.setTextColor(itemView.getResources().getColor(R.color.colorAccent));




        return itemView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.onResetPasswordFragment = true;
                setFragment(new ResetPasswordFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent(getContext());
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
                checkEmailAndPassword();
            }
        });

    }

    private void checkInputs() {
        Drawable customErrorIcon = getResources().getDrawable(R.mipmap.error);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(password.getText())){


            }else {
                password.setError("Silahkan isi Password");
            }
        }else{
            email.setError("Silahkan isi Email");

        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private  void checkEmailAndPassword(){
        if(email.getText().toString().matches(emailPattern)){
            if(password.length()>=8){

                progressBar.setVisibility(View.VISIBLE);

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user==null){
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        requestLogin(user.getEmail(),user.getUid(),progressBar,getContext(),signInBtn);

//                                    mainIntent();

                                    }else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        signInBtn.setEnabled(true);

                                        String error = task.getException().getMessage();
                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    if(!isLogin){
                        requestLogin(user.getEmail(),user.getUid(),progressBar,getContext(),signInBtn);
                    }
                }


            }else {
                Toast.makeText(getActivity(),"Incorrect email or password", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(),"Incorrect email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void mainIntent(Context context) {

//        DBqueries.clearData();
//        MainActivity.currentFragment = -1;
//        MainActivity.mainActivity = null;
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        MainActivity.mainActivity.finish();
//        MainActivity.showCart=false;
        context.startActivity(mainIntent);
        ((Activity) context).finish();
      //  disableCloseBtn = false;


    }

    public static void requestLogin(String email, String password, ProgressBar progressBar, Context context, FButton button){


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
                    progressBar.setVisibility(View.INVISIBLE);
                    button.setEnabled(true);
                    UserModel userModel = response.body();
                    UserPreference userPreference = new UserPreference(context);
                    userPreference.setUserPreference("user", userModel);
                    SignInFragment.isLogin = true;
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    MainActivity.mainActivity.finish();
                    ((Activity) context).finish();
//                                if (jsonRESULTS.getString("error").equals("false")){
//
//                                    SignInFragment.isLogin = true;
//                                    // Jika login berhasil maka data nama yang ada di response API
//                                    // akan diparsing ke activity selanjutnya.
//
//
//                                } else {
//                                    // Jika login gagal
//                                    String error_message = jsonRESULTS.getString("error_msg");
//                                    Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
//                                }

                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    button.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
            }
        });
    }

}
