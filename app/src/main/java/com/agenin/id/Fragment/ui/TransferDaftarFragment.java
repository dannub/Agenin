package com.agenin.id.Fragment.ui;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agenin.id.Activity.RegisterActivity;
import com.agenin.id.R;

import info.hoang8f.widget.FButton;


public class TransferDaftarFragment extends Fragment {

    private FrameLayout parentFrameLayout;

    private  String bank;
    private TextView total;
    private TextView norek;
    private TextView cabang;
    private TextView an;
    private Dialog loadingDialog;
    private ImageView bank_img;
    private FButton payment;
    private TextView salin;
    public TransferDaftarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View itemview =  inflater.inflate(R.layout.fragment_transfer_daftar, container, false);

        total = (TextView) itemview.findViewById(R.id.total);
        norek = (TextView) itemview.findViewById(R.id.norek);
        cabang = (TextView) itemview.findViewById(R.id.cabang);
        an = (TextView) itemview.findViewById(R.id.an);
        bank_img = (ImageView) itemview.findViewById(R.id.img_bank);
        payment =(FButton) itemview.findViewById(R.id.upload_btn);
        salin = (TextView) itemview.findViewById(R.id.salin);

        parentFrameLayout = getActivity().findViewById(R.id.frame_layout_register);
        payment.setButtonColor(getResources().getColor(R.color.colorPrimary));
        payment.setTextColor(getResources().getColor(R.color.colorAccent));



        total.setText("Rp. 150.000");
        if ((String)SignUpFragment.userdata.get("bukti_bank")=="BRI"){
            bank_img.setImageDrawable(itemview.getResources().getDrawable(R.drawable.bri));
            norek.setText("00000000000000000");
            cabang.setText("Surabaya");
            an.setText("Agenin BRI");
            salin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("No.Rekening tersalin", "00000000000000000");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getContext(),"No.Rekening tersalin",Toast.LENGTH_SHORT).show();

                }
            });
        }else if ((String)SignUpFragment.userdata.get("bukti_bank")=="BNI"){
            bank_img.setImageDrawable(itemview.getResources().getDrawable(R.drawable.bni));
            norek.setText("482520509");
            cabang.setText("Surabaya");
            an.setText("Moh Rizal Rizki");
            salin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("No.Rekening tersalin", "482520509");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getContext(),"No.Rekening tersalin",Toast.LENGTH_SHORT).show();

                }
            });
        }else if((String)SignUpFragment.userdata.get("bukti_bank")=="BCA"){
            bank_img.setImageDrawable(itemview.getResources().getDrawable(R.drawable.bca));
            norek.setText("00000000000000345");
            cabang.setText("Surabaya");
            an.setText("Agenin BCA");
            salin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("No.Rekening tersalin", "00000000000000345");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getContext(),"No.Rekening tersalin",Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            bank_img.setImageDrawable(itemview.getResources().getDrawable(R.drawable.mandiri));
            norek.setText("1410013917737");
            cabang.setText("Surabaya");
            an.setText("Moh Rizal Rizki");
            salin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("No.Rekening tersalin", "1410013917737");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getContext(),"No.Rekening tersalin",Toast.LENGTH_SHORT).show();

                }
            });

        }



        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.onDaftarFragment = true;
                setFragment(new DaftarFragment());
            }
        });



        return itemview;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
