package com.agenin.id.Fragment.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.agenin.id.Adapter.YoutubeVideoAdapter;
import com.agenin.id.DBQueries;
import com.agenin.id.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TutorialFragment extends Fragment {


    @BindView(R.id.recycler_view_video)
    RecyclerView recyclerViewFeed;

    @BindView(R.id.no_internet_connection)
    ImageView no_internet;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.pesanan)
    TextView noData;




    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    public static YoutubeVideoAdapter mRecyclerAdapter;
    // public static YouTubePlayer youTubePlayerku ;
    public static String video;

    private Dialog loadingDialog;

    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tutorial, container, false);
        video = "";
        ButterKnife.bind(this, root);


        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        }
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //loading dialog

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        //  extractYoutubeUrl();


        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadPage();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        loadingDialog.show();
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loadingDialog.setOnDismissListener(null);
            }
        });
        reloadPage();
        return root;
    }

    @SuppressLint("MissingPermission")
    private void reloadPage() {
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true && isOnline()) {
               DBQueries.loadVideolist(getContext(),loadingDialog,true,recyclerViewFeed,no_internet,noData);

        } else {
            noData.setVisibility(View.GONE);
            no_internet.setVisibility(View.VISIBLE);
                recyclerViewFeed.setVisibility(View.GONE);
                loadingDialog.dismiss();
        }
    }


//    private List<YoutubeVideoModel> prepareList() {
//        ArrayList<YoutubeVideoModel> videoArrayList=new ArrayList<>();
//        // add first item
//        YoutubeVideoModel video1 = new YoutubeVideoModel();
//        video1.setId(1l);
//        video1.setImageUrl("https://i.ytimg.com/vi/zI-Pux4uaqM/maxresdefault.jpg");
//        video1.setTitle(
//                "Thugs Of Hindostan - Official Trailer | Amitabh Bachchan | Aamir Khan");
//        video1.setVideoId("zI-Pux4uaqM");
//        videoArrayList.add(video1);
//        // add second item
//        YoutubeVideoModel video2 = new YoutubeVideoModel();
//        video2.setId(2l);
//        video2.setImageUrl("https://i.ytimg.com/vi/8ZK_S-46KwE/maxresdefault.jpg");
//        video2.setTitle(
//                "Colors for Children to Learning with Baby Fun Play with Color Balls Dolphin...");
//        video2.setVideoId("8ZK_S-46KwE");
//        // add third item
//        YoutubeVideoModel video3 = new YoutubeVideoModel();
//        video3.setId(3l);
//        video3.setImageUrl("https://i.ytimg.com/vi/8czMWUH7vW4/hqdefault.jpg");
//        video3.setTitle("Air Hostess Accepts Marriage Proposal Mid-Air, Airline Fires her.");
//        video3.setVideoId("8czMWUH7vW4");
//        // add four item
//        YoutubeVideoModel video4 = new YoutubeVideoModel();
//        video4.setId(4l);
//        video4.setImageUrl("https://i.ytimg.com/vi/YrQVYEb6hcc/maxresdefault.jpg");
//        video4.setTitle("EXPERIMENT Glowing 1000 degree METAL BALL vs Gunpowder (100 grams)");
//        video4.setVideoId("YrQVYEb6hcc");
//        // add four item
//        YoutubeVideoModel video5 = new YoutubeVideoModel();
//        video5.setId(5l);
//        video5.setImageUrl("https://i.ytimg.com/vi/S84Fuo2rGoY/maxresdefault.jpg");
//        video5.setTitle("What happened after Jauhar of Padmavati");
//        video5.setVideoId("S84Fuo2rGoY");
//        videoArrayList.add(video1);
//        videoArrayList.add(video2);
//        videoArrayList.add(video3);
//        videoArrayList.add(video4);
//        return videoArrayList;
//    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 https://apk.agenin.id");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}