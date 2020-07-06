package com.agenin.id.Fragment.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agenin.id.Adapter.WishlistAdapter;
import com.agenin.id.DBQueries;
import com.agenin.id.R;

import static com.agenin.id.DBQueries.loadWishlist;

public class FavoriteFragment extends Fragment {


    public static RecyclerView wistListRecyclerView;
    private Dialog loadingDialog;
    public  static WishlistAdapter wishlistAdapter;

    public static TextView noData;
    public static ConstraintLayout background;
    public static ImageView no_internet;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favorite, container, false);

        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //loading dialog

        wistListRecyclerView = root.findViewById(R.id.my_wishlist_recyclerview);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wistListRecyclerView.setLayoutManager(linearLayoutManager);

        noData = root.findViewById(R.id.pesanan);
        background =  root.findViewById(R.id.bg);
        no_internet = root.findViewById(R.id.no_internet_connection);
        swipeRefreshLayout = root.findViewById(R.id.swipe);
        connectivityManager =(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();






        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadPage();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        wishlistAdapter = new WishlistAdapter(DBQueries.wishlistModelList,true);
        wistListRecyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();
        return root;
    }
    public void reloadPage(){
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo !=null && networkInfo.isConnected()==true) {
            loadWishlist(getContext(),loadingDialog,true);

        }else {
            noData.setVisibility(View.GONE);
            background.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
            no_internet.setVisibility(View.VISIBLE);
            wistListRecyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        reloadPage();
    }
}
