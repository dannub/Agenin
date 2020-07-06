package com.agenin.id.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agenin.id.DBQueries;
import com.agenin.id.Model.NotificationModel;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private List<NotificationModel> notifiationModelList;

    public NotificationAdapter(List<NotificationModel> notifiationModelList) {
        this.notifiationModelList = notifiationModelList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        String image = notifiationModelList.get(position).getImage();
        String title = notifiationModelList.get(position).getTitle();
        String body = notifiationModelList.get(position).getBody();
        boolean readed = notifiationModelList.get(position).isReaded();

        holder.setData(image,body,title,readed);

    }

    @Override
    public int getItemCount() {
        return notifiationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
        }

        private void setData (String image, String body, String titleText, boolean readed){

            Glide.with(itemView.getContext()).load(DBQueries.url+image).apply(new RequestOptions().placeholder(R.drawable.icon)).into(imageView);

//            if (image.equals("")) {
//                Glide.with(itemView.getContext()).load(DBQueries.url+image).apply(new RequestOptions().placeholder(R.drawable.icon)).into(imageView);
//            }else {
//                imageView.setImageDrawable(itemView.getResources().getDrawable(R.drawable.icon));
//            }
            title.setText(titleText);
            if (readed){
                textView.setAlpha(0.5f);
            }else {
                textView.setAlpha(1f);
            }
            textView.setText(body);
        }
    }
}
