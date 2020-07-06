package com.agenin.id.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agenin.id.Activity.FullScreenActivity;
import com.agenin.id.DBQueries;
import com.agenin.id.Fragment.ui.TutorialFragment;
import com.agenin.id.Model.YoutubeVideoModel;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YoutubeVideoAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_NORMAL = 1;
    private List<YoutubeVideoModel> mYoutubeVideos;
    public YoutubeVideoAdapter(List<YoutubeVideoModel> youtubeVideos) {
        mYoutubeVideos = youtubeVideos;
    }
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            holder.onBind(position);
            }
    @Override
    public int getItemViewType(int position) {
            return VIEW_TYPE_NORMAL;
            }
    @Override
    public int getItemCount() {
            if (mYoutubeVideos != null && mYoutubeVideos.size() > 0) {
            return mYoutubeVideos.size();
            } else {
            return 1;
            }
            }
    public void setItems(List<YoutubeVideoModel> youtubeVideos) {
            mYoutubeVideos = youtubeVideos;
            notifyDataSetChanged();
    }
    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.textViewTitle)
        TextView textWaveTitle;
        @BindView(R.id.imageViewItem)
        ImageView tumbnail;
        @BindView(R.id.btnPlay)
        ImageView btnPlay;
//        @BindView(R.id.video_view)
//        WebView webView;

//          @BindView(R.id.video_view)
//        WebView webView;
//        @BindView(R.id.youtube_view)
//        YouTubePlayerView youTubePlayerView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            final YoutubeVideoModel mYoutubeVideo = mYoutubeVideos.get(position);


            if (mYoutubeVideo.getTitle() != null) {
                textWaveTitle.setText(mYoutubeVideo.getTitle());
            }

            Glide.with(itemView.getContext()).load(DBQueries.url+mYoutubeVideo.getImageUrl()).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(tumbnail);

            String link = "https://www.youtube.com/watch?v="+mYoutubeVideo.getVideoId();
            tumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(itemView.getContext(), FullScreenActivity.class);
                    i.putExtra("link",link);
                    itemView.getContext().startActivity(i);
                }
            });
            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), FullScreenActivity.class);
                    i.putExtra("link",link);
                    itemView.getContext().startActivity(i);
                }
            });
//


        }

    }
}
