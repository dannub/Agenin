package com.agenin.id.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.agenin.id.Model.SliderModel;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private List<SliderModel> sliderModelsList;

    public SliderAdapter(List<SliderModel> sliderModelsList) {
        this.sliderModelsList = sliderModelsList;
    }

    @Override
    public int getCount() {
        return sliderModelsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
       View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider_layout,container,false );
        ConstraintLayout bannercontainer= view.findViewById(R.id.banner_container);
        bannercontainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sliderModelsList.get(position).getBackgroundColor())));


        ImageView banner = view.findViewById(R.id.banner_slide);
        Glide.with(container.getContext()).load(sliderModelsList.get(position).getBanner()).apply(new RequestOptions().placeholder(R.drawable.banner_slider)).into(banner);
        container.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
