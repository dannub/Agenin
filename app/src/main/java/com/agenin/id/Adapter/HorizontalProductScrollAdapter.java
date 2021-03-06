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

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    private String categoryFrom;
    private String categorySlug;

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList,String categoryFrom,String categorySlug) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.categoryFrom=categoryFrom;
        this.categorySlug= categorySlug;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder viewHolder, int position) {

        String resource = horizontalProductScrollModelList.get(position).getProductImage();
        String title = horizontalProductScrollModelList.get(position).getProductTitle();
        String price = horizontalProductScrollModelList.get(position).getProducPrice();
        String cuttedprice = horizontalProductScrollModelList.get(position).getProducCuttedPrice();
        String productId = horizontalProductScrollModelList.get(position).getProductID();
        viewHolder.setData(productId,resource,cuttedprice,title,price);

    }

    @Override
    public int getItemCount() {
        if(horizontalProductScrollModelList.size()>8){
            return 8;
        }else {
            return horizontalProductScrollModelList.size();
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView productCuttedPrice;
        private TextView productPrice;
        private View pricecut;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.hs_product_image);
            productTitle = itemView.findViewById(R.id.hs_product_title);
            productCuttedPrice = itemView.findViewById(R.id.cutted_price);
            pricecut = itemView.findViewById(R.id.price_cut);
            productPrice = itemView.findViewById(R.id.hs_product_price);

        }

        private  void  setData(final String productId, String resource,String cuttedPrice, String title, String price){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(productImage);
            productTitle.setText(title);

            if(!cuttedPrice.equals("")) {
                productCuttedPrice.setText("Rp " + ProductDetailActivity.currencyFormatter(cuttedPrice) + "");
                pricecut.setVisibility(View.VISIBLE);
            }else {
                productCuttedPrice.setText("");
                pricecut.setVisibility(View.GONE);
            }
            if(!price.equals("")){
                productPrice.setText("Rp "+ProductDetailActivity.currencyFormatter(price)+"");
            }else {
                productPrice.setText("");
            }

            if(!title.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                        productDetailsIntent.putExtra("productID",productId);
                        productDetailsIntent.putExtra("categoryFrom",categoryFrom);
                        productDetailsIntent.putExtra("categorySlug",categorySlug);
                        itemView.getContext().startActivity(productDetailsIntent);
                    }
                });
            }
        }

    }
}
