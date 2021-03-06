package com.agenin.id.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.agenin.id.Activity.ProductDetailActivity;
import com.agenin.id.Model.HorizontalProductScrollModel;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    String categoryFrom;
    private String categorySlug;

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList,String categoryFrom,String categorySlug) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.categoryFrom = categoryFrom;
        this.categorySlug = categorySlug;
    }

    @Override
    public int getCount() {
        return horizontalProductScrollModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_scroll_item_layout,parent,false);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                view.setElevation(3);
//            }
//            view.setBackgroundColor(Color.parseColor("#ffffff"));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDetailsIntent = new Intent(parent.getContext(), ProductDetailActivity.class);
                productDetailsIntent.putExtra("productID",horizontalProductScrollModelList.get(position).getProductID());
                productDetailsIntent.putExtra("categoryFrom",categoryFrom);
                productDetailsIntent.putExtra("categorySlug",categorySlug);
                parent.getContext().startActivity(productDetailsIntent);
            }
        });

        ImageView productImage = view.findViewById(R.id.hs_product_image);
        TextView productTitle = view.findViewById(R.id.hs_product_title);
        TextView productCuttedPrice = view.findViewById(R.id.cutted_price);
        TextView productPrice = view.findViewById(R.id.hs_product_price);
        View pricecut =view.findViewById(R.id.price_cut);

        Glide.with(parent.getContext()).load(horizontalProductScrollModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(productImage);
        productTitle.setText(horizontalProductScrollModelList.get(position).getProductTitle());

        if(!horizontalProductScrollModelList.get(position).getProducCuttedPrice().equals("")){
            productCuttedPrice.setText("Rp "+ProductDetailActivity.currencyFormatter(horizontalProductScrollModelList.get(position).getProducCuttedPrice()));
            pricecut.setVisibility(View.VISIBLE);

        }else {
            productCuttedPrice.setText("");
            pricecut.setVisibility(View.GONE);
        }

        if(!horizontalProductScrollModelList.get(position).getProducPrice().equals("")){
            productPrice.setText("Rp "+ProductDetailActivity.currencyFormatter(horizontalProductScrollModelList.get(position).getProducPrice()));

        }else {
            productPrice.setText("");

        }



        return view;
    }
}
