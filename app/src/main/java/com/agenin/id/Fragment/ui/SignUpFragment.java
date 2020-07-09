package com.agenin.id.Fragment.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.agenin.id.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.hoang8f.widget.FButton;

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
    private Dialog infoDialog;
    private ImageButton bri,bca,bni,mandiri;

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
        paymentMethodDialog = new Dialog(getContext());
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(itemView.getResources().getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog

        //loading dialog
        infoDialog = new Dialog(getContext());
        infoDialog.setContentView(R.layout.info_daftar);
        infoDialog.setCancelable(true);
        infoDialog.getWindow().setBackgroundDrawable(itemView.getResources().getDrawable(R.drawable.slider_background));
        infoDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog
        OkButton = infoDialog.findViewById(R.id.ok);
        OkButton.setButtonColor(itemView.getResources().getColor(R.color.colorPrimary));
        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.dismiss();
            }
        });
        infoDialog.show();


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
                paymentMethodDialog.show();
                bri = paymentMethodDialog.findViewById(R.id.bri);
                bni = paymentMethodDialog.findViewById(R.id.bni);
                bca = paymentMethodDialog.findViewById(R.id.bca);
                mandiri = paymentMethodDialog.findViewById(R.id.mandiri);
//
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

                bni.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paymentMethodDialog.dismiss();
                        userdata.put("bukti_bank","BNI");
                        RegisterActivity.onTransferDaftarFragment = true;
                        setFragment(new TransferDaftarFragment());



                    }
                });

//                bca.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        paymentMethodDialog.dismiss();
//                        userdata.put("bukti_bank","BCA");
//                        RegisterActivity.onTransferDaftarFragment = true;
//                        setFragment(new TransferDaftarFragment());
//                    }
//                });

                mandiri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paymentMethodDialog.dismiss();
                        userdata.put("bukti_bank","Mandiri");
                        RegisterActivity.onTransferDaftarFragment = true;
                        setFragment(new TransferDaftarFragment());
                    }
                });





//                                    mainIntent();
////                                    Map<String,Object> userdata = new HashMap<>();
////
////                                    userdata.put("fullname",fullname.getText().toString());
////                                    userdata.put("email",email.getText().toString());
////                                    userdata.put("hp",hp.getText().toString());
////                                    userdata.put("profile","");
////                                    userdata.put("Lastseen", FieldValue.serverTimestamp());
////
////
////
////                                    firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
////                                            .set(userdata)
////                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
////                                                @Override
////                                                public void onComplete(@NonNull Task<Void> task) {
////                                                    if(task.isSuccessful()){
////
////                                                        CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");
////
////                                                        ///MAPS
////                                                        Map<String ,Object> wishlistMap = new HashMap<>();
////                                                        wishlistMap.put("list_size",(long)0);
////
////                                                        Map<String ,Object> ratingMap = new HashMap<>();
////                                                        ratingMap.put("list_size",(long)0);
////
////                                                        Map<String ,Object> cartMap = new HashMap<>();
////                                                        cartMap.put("list_size",(long)0);
////
////                                                        Map<String ,Object> myAddressesMap = new HashMap<>();
////                                                        myAddressesMap.put("list_size",(long)0);
////
////                                                        Map<String ,Object> notificationMap = new HashMap<>();
////                                                        notificationMap.put("list_size",(long)0);
////                                                        ///MAPS
////
////                                                        final List<String> documentNames = new ArrayList<>();
////                                                        documentNames.add("MY_WISHLIST");
////                                                        documentNames.add("MY_RATINGS");
////                                                        documentNames.add("MY_CART");
////                                                        documentNames.add("MY_ADDRESSES");
////                                                        documentNames.add("MY_NOTIFICATIONS");
////
////
////                                                        List<Map<String,Object>> documentFields = new ArrayList<>();
////                                                        documentFields.add(wishlistMap);
////                                                        documentFields.add(ratingMap);
////                                                        documentFields.add(cartMap);
////                                                        documentFields.add(myAddressesMap);
////                                                        documentFields.add(notificationMap);
////
////                                                        for (int x = 0;x<documentNames.size();x++){
////
////                                                            final int finalX = x;
////                                                            userDataReference.document(documentNames.get(x))
////                                                                    .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
////                                                                @Override
////                                                                public void onComplete(@NonNull Task<Void> task) {
////                                                                    if(task.isSuccessful()){
////                                                                        if (finalX == documentNames.size()-1) {
////
////                                                                            mainIntent();
////                                                                        }
////                                                                    }else {
////                                                                        progressBar.setVisibility(View.INVISIBLE);
////                                                                        signUpBtn.setEnabled(true);
////                                                                        String error = task.getException().getMessage();
////                                                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
////                                                                    }
////                                                                }
////                                                            });
////                                                        }
////
////
////                                                    }else {
////                                                        String error = task.getException().getMessage();
////                                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
////                                                    }
////                                                }
////                                            });
//


//

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
