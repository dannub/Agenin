package com.agenin.id.Fragment.ui;

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
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agenin.id.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import info.hoang8f.widget.FButton;



public class ResetPasswordFragment extends Fragment {
    private TextInputEditText registeredEmail;
    private FButton btn_reset;
    private TextView goBack;

    private FrameLayout parentFrameLayout;

    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        registeredEmail= view.findViewById(R.id.txtemail);
        btn_reset= view.findViewById(R.id.btn_reset);
        goBack = view.findViewById(R.id.goback);

        emailIconContainer = view.findViewById(R.id.layoutemail);
        emailIcon = view.findViewById(R.id.gmb_mail);
        emailIconText = view.findViewById(R.id.text_mail);
        progressBar = view.findViewById(R.id.progress_forget);

        parentFrameLayout= getActivity().findViewById(R.id.frame_layout_register);

        btn_reset.setButtonColor(getResources().getColor(R.color.colorAccent));

        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setButtonColor(view.getResources().getColor(R.color.colorPrimary));
        btn_reset.setTextColor(view.getResources().getColor(R.color.colorAccent));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registeredEmail.addTextChangedListener(new TextWatcher() {
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

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                checkInputs();
                if(!TextUtils.isEmpty(registeredEmail.getText())) {

                    TransitionManager.beginDelayedTransition(emailIconContainer);
                    emailIconText.setVisibility(View.GONE);

                    TransitionManager.beginDelayedTransition(emailIconContainer);
                    emailIcon.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);


                    firebaseAuth.sendPasswordResetEmail(registeredEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, emailIcon.getWidth() / 2, emailIcon.getHeight() / 2);
                                        scaleAnimation.setDuration(100);
                                        scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                        scaleAnimation.setRepeatMode(Animation.REVERSE);
                                        scaleAnimation.setRepeatCount(1);

                                        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {
                                                emailIconText.setText("Email untuk reset password telah dikirim! Silahkan cek inboxmu");

                                                TransitionManager.beginDelayedTransition(emailIconContainer);
                                                emailIconText.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                                emailIcon.setImageResource(R.drawable.mail_success);
                                            }

                                            @Override
                                            public void onAnimationRepeat(Animation animation) {

                                            }
                                        });
                                        emailIconContainer.startAnimation(scaleAnimation);

                                    } else {
                                        String error = task.getException().getMessage();

                                        emailIconText.setText(error);
                                        TransitionManager.beginDelayedTransition(emailIconContainer);
                                        emailIconText.setVisibility(View.VISIBLE);

                                    }
                                    progressBar.setVisibility(View.GONE);
                                    btn_reset.setEnabled(true);
                                }
                            });
                }

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

    }


    private void checkInputs() {
        Drawable customErrorIcon = getResources().getDrawable(R.mipmap.error);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());

        if(!TextUtils.isEmpty(registeredEmail.getText())){

        }else{
            registeredEmail.setError("Silahkan isi Email");
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
