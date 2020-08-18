package com.agenin.id.Fragment.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.agenin.id.Model.Api.UserAPI;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.Progress.ProgressRequestBody;
import com.agenin.id.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import info.hoang8f.widget.FButton;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**

 */
public class SignUpFragment extends Fragment {
    FButton signUpBtn,OkButton;
    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private TextInputEditText email,fullname,namerefferal,pwd,confirmpwd,hp;

    private ImageButton closeBtn;



    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn = false;

    public static Map<String,Object> userdata;
    private Dialog paymentMethodDialog;
    private Dialog infoDialog,loadingDialog;
    private ImageButton bri,bca,bni,mandiri;

    private  FirebaseAuth firebaseAuth;

    public SignUpFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment

        View itemView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        signUpBtn=(FButton) itemView.findViewById(R.id.btn_signup);
        signUpBtn.setButtonColor(getResources().getColor(R.color.colorAccent));

        alreadyHaveAnAccount = (TextView) itemView.findViewById(R.id.textView3);

        parentFrameLayout = getActivity().findViewById(R.id.frame_layout_register);
        email = itemView.findViewById(R.id.txtemail);
        namerefferal = itemView.findViewById(R.id.txtnamerefferal);
        fullname = itemView.findViewById(R.id.txtfullname);
        pwd = itemView.findViewById(R.id.txtpassword);
        confirmpwd = itemView.findViewById(R.id.txtconfirmpassword);
        hp= itemView.findViewById(R.id.txthp);
        closeBtn = itemView.findViewById(R.id.close_signup);


        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        }
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog



        //loading dialog
        paymentMethodDialog = new Dialog(getContext());
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(itemView.getResources().getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog

        //info dialog
//        infoDialog = new Dialog(getContext());
//        infoDialog.setContentView(R.layout.info_daftar);
//        infoDialog.setCancelable(true);
//        infoDialog.getWindow().setBackgroundDrawable(itemView.getResources().getDrawable(R.drawable.slider_background));
//        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        //loading dialog
//        OkButton = infoDialog.findViewById(R.id.ok);
//        OkButton.setButtonColor(itemView.getResources().getColor(R.color.colorPrimary));
//        OkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                infoDialog.dismiss();
//            }
//        });
//        infoDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();

     //   progressBar = itemView.findViewById(R.id.progress_signup);

//        firebaseFirestore = FirebaseFirestore.getInstance();

        if (disableCloseBtn){
            closeBtn.setVisibility(View.GONE);
        }else {
            closeBtn.setVisibility(View.VISIBLE);
        }

        signUpBtn.setButtonColor(itemView.getResources().getColor(R.color.colorPrimary));
        signUpBtn.setTextColor(itemView.getResources().getColor(R.color.colorAccent));


        return itemView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputs();
            }
        });
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputs();
            }
        });
        namerefferal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputs();
            }
        });
        hp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputs();
            }
        });
        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputs();
            }
        });
        confirmpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputs();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
                checkEmailAndPassword();
            }
        });
    }



    private void checkEmailAndPassword() {


        Drawable customErrorIcon = getResources().getDrawable(R.mipmap.error);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());



        if (email.getText().toString().matches(emailPattern)) {
            if (pwd.getText().toString().equals(confirmpwd.getText().toString())) {

                userdata = new HashMap<>();

                userdata.put("name", fullname.getText().toString());
                userdata.put("email", email.getText().toString());
                userdata.put("name_refferal", namerefferal.getText().toString());
                userdata.put("password", pwd.getText().toString());
                userdata.put("handphone", hp.getText().toString());
//                paymentMethodDialog.show();
//                bri = paymentMethodDialog.findViewById(R.id.bri);
//                bni = paymentMethodDialog.findViewById(R.id.bni);
//                bca = paymentMethodDialog.findViewById(R.id.bca);
//                mandiri = paymentMethodDialog.findViewById(R.id.mandiri);

//                bri.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        paymentMethodDialog.dismiss();
//                        userdata.put("bukti_bank","BRI");
//                        RegisterActivity.onTransferDaftarFragment = true;
//                        setFragment(new TransferDaftarFragment());
//
//
//
//                    }
//                });

//                bni.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        paymentMethodDialog.dismiss();
//                        userdata.put("bukti_bank","BNI");
//                        RegisterActivity.onTransferDaftarFragment = true;
//                        setFragment(new TransferDaftarFragment());
//
//
//
//                    }
//                });
//
//                bca.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        paymentMethodDialog.dismiss();
//                        userdata.put("bukti_bank","BCA");
//                        RegisterActivity.onTransferDaftarFragment = true;
//                        setFragment(new TransferDaftarFragment());
//                    }
//                });
//
//                mandiri.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        paymentMethodDialog.dismiss();
//                        userdata.put("bukti_bank","Mandiri");
//                        RegisterActivity.onTransferDaftarFragment = true;
//                        setFragment(new TransferDaftarFragment());
//                    }
//                });



                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null){
                    firebaseAuth.createUserWithEmailAndPassword((String) SignUpFragment.userdata.get("email"),(String) SignUpFragment.userdata.get("password"))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                        if(task.isSuccessful()){

                                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                                                @Override
                                                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                                                    String newToken = instanceIdResult.getToken();
                                                                    SignUpFragment.userdata.put("token_fb",newToken);
                                                                    SignUpFragment.userdata.put("uid",user.getUid());
//                                                                    SignUpFragment.userdata.put("bukti_tgl",tgl.getText().toString());
//                                                                    SignUpFragment.userdata.put("bukti_an",an.getText().toString());
                                                                    uploadFile(getContext(),SignUpFragment.userdata,loadingDialog);

                                                                }
                                                            });

                                                        }else {
                                                            Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }else {
                                        loadingDialog.dismiss();
                                        String error = task.getException().getMessage();
                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if(task.isSuccessful()){
                                        String token = task.getResult().getToken();
                                        SignUpFragment.userdata.put("token_fb",token);
                                        SignUpFragment.userdata.put("uid",user.getUid());
//                                        SignUpFragment.userdata.put("bukti_tgl",tgl.getText().toString());
//                                        SignUpFragment.userdata.put("bukti_an",an.getText().toString());
                                        uploadFile(getContext(),SignUpFragment.userdata,loadingDialog);
                                    }else {
                                        Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }




            } else {
                confirmpwd.setError("Password doesn't matched!");
            }
        } else {
            email.setError("Invalid Email!");
        }

    }



    private void checkInputs() {
        Drawable customErrorIcon = getResources().getDrawable(R.mipmap.error);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());

        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(fullname.getText())){
                if(!TextUtils.isEmpty(hp.getText())) {
                    if(!TextUtils.isEmpty(pwd.getText())&&pwd.length()>= 8){
                        if (!TextUtils.isEmpty(confirmpwd.getText())){


                        }else{
                            pwd.setError("Silahkan isi Konfirmasi Password");
                        }
                    }else{
                        pwd.setError("Silahkan isi Password");
                    }
                }else{
                    hp.setError("Silahkan isi No. HP");
                }
            }
            else{
                fullname.setError("Silahkan isi Nama Lengkap");
            }
        }else {
            email.setError("Silahkan isi Email");

        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }


    public static void uploadFile(Context context, Map<String,Object> userdata, final Dialog dialog) {


            String token_fb,passwordori,name,email,name_refferal,password,handphone;
            token_fb = (String)userdata.get("token_fb");
            email =(String)userdata.get("email");
            name =  (String)userdata.get("name");
            passwordori = (String)userdata.get("password");
            name_refferal = (String)userdata.get("name_refferal");
            password =  (String)userdata.get("uid");
            handphone = (String)userdata.get("handphone");





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

            Call<UserAPI> call = client.registerFree(email,name,token_fb,name_refferal,password,handphone);

            call.enqueue(new Callback<UserAPI>() {
                @Override
                public void onResponse(Call<UserAPI> call, Response<UserAPI> response) {
                    if (!response.isSuccessful()) {
                        try {

                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Toast.makeText(context,jsonObject.get("mesagge").toString(),Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


//                     Toast.makeText(context,response.body().getMesagge(),Toast.LENGTH_SHORT).show();
//                        if (dialog!=null) {
//                            dialog.dismiss();
//                        }
//                        return;
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        // Get auth credentials from the user for re-authentication. The example below shows
                        // email and password credentials but there are multiple possible providers,
                        // such as GoogleAuthProvider or FacebookAuthProvider.
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email, passwordori);

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            UserPreference userPreference = new UserPreference(context);
                                                            UserPreference.setUserPreference("user", null);

                                                            FirebaseAuth.getInstance().signOut();

                                                            if (dialog!=null) {
                                                                dialog.dismiss();
                                                            }
                                                            return;
                                                        }
                                                    }
                                                });

                                    }
                                });

                    }else {
                        dialog.dismiss();
                        UserPreference userPreference = new UserPreference(context);
                        UserModel userModel = response.body().getUser();
                        UserPreference.setUserPreference("user", userModel);
                        SignInFragment.isLogin = true;
                        Toast.makeText(context, "Selamat Datang di Agenin", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }


                }

                @Override
                public void onFailure(Call<UserAPI> call, Throwable t) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    // Get auth credentials from the user for re-authentication. The example below shows
                    // email and password credentials but there are multiple possible providers,
                    // such as GoogleAuthProvider or FacebookAuthProvider.
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, passwordori);

                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        dialog.dismiss();
                                                        FirebaseAuth.getInstance().signOut();
                                                        UserPreference userPreference = new UserPreference(context);
                                                        UserPreference.setUserPreference("user", null);
                                                        Log.i("error",call.toString());
                                                        Log.i("trhrowable",t.toString());
                                                        Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }
                            });

                }
            });



    }

        private  void mainIntent(){
        getActivity().finishAffinity();
//        DBqueries.clearData();
//        MainActivity.currentFragment = -1;
        MainActivity.mainActivity.finish();
//        RegisterActivity.onDaftarFragment = true;
//        setFragment(new DaftarFragment());
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
     //   disableCloseBtn = false;

    }


}
