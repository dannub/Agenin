package com.agenin.id.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agenin.id.Activity.ProductDetailActivity;
import com.agenin.id.Model.HorizontalProductScrollModel;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProductLayoutAdapter2 extends RecyclerView.Adapter<GridProductLayoutAdapter2.ViewHolder> {

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public GridProductLayoutAdapter2(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_scroll_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String resource = horizontalProductScrollModelList.get(position).getProductImage();
        String title = horizontalProductScrollModelList.get(position).getProductTitle();
        String price = horizontalProductScrollModelList.get(position).getProducPrice();
        String cuttedprice = horizontalProductScrollModelList.get(position).getProducCuttedPrice();
        String productId = horizontalProductScrollModelList.get(position).getProductID();
        viewHolder.setData(productId,resource,cuttedprice,title,price);
    }

    @Override
    public int getItemCount() {
        return horizontalProductScrollModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productTitle;
        TextView productCuttedPrice;
        TextView productPrice;
        View pricecut;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
             productImage = itemView.findViewById(R.id.hs_product_image);
             productTitle = itemView.findViewById(R.id.hs_product_title);
             productCuttedPrice = itemView.findViewById(R.id.cutted_price);
             productPrice = itemView.findViewById(R.id.hs_product_price);
            pricecut =itemView.findViewById(R.id.price_cut);
        }

        public void setData(String productId, String resource, String cuttedprice, String title, String price) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(productImage);
            productTitle.setText(title);

            if(!cuttedprice.equals("")){
                productCuttedPrice.setText("Rp "+ ProductDetailActivity.currencyFormatter(cuttedprice));
                pricecut.setVisibility(View.VISIBLE);

            }else {
                productCuttedPrice.setText("");
                pricecut.setVisibility(View.GONE);
            }

            if(!price.equals("")){
                productPrice.setText("Rp "+ProductDetailActivity.currencyFormatter(price));

            }else {
                productPrice.setText("");

            }

            if(!title.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                        productDetailsIntent.putExtra("productID",productId);
                        itemView.getContext().startActivity(productDetailsIntent);
                    }
                });
            }
        }
    }
}
