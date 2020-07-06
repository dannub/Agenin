package com.agenin.id.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agenin.id.Activity.CartActivity;
import com.agenin.id.Activity.DeliveryActivity;
import com.agenin.id.Activity.MainActivity;
import com.agenin.id.Activity.ProductDetailActivity;
import com.agenin.id.DBQueries;
import com.agenin.id.Model.CartItemModel;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private  int lastPosition = -1;
    private TextView badgeCount;
    private Context context;
    private boolean showDeleteBtn;
    private CartAdapter cartAdapter;
    private RecyclerView cartItemsRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Dialog loadingDialog;
    private Boolean isDelivery;
    private TextView totalAmount;

    public CartAdapter(Context context, RecyclerView cartItemsRecyclerView, LinearLayoutManager linearLayoutManager, List<CartItemModel> cartItemModelList,  boolean showDeleteBtn, Dialog loadingDialog,TextView badgeCount,boolean isDelivery) {
        this.cartItemModelList = cartItemModelList;
        this.context=context;
        this.isDelivery = isDelivery;
        this.badgeCount = badgeCount;
        this.cartItemsRecyclerView = cartItemsRecyclerView;
        this.linearLayoutManager = linearLayoutManager;
        this.showDeleteBtn = showDeleteBtn;
        this.loadingDialog = loadingDialog;
    }
    public void SetAdapter(CartAdapter cartAdapter){
        this.cartAdapter=cartAdapter;
    }

    public void SetTotalAmount(TextView totalAmount){
        this.totalAmount=totalAmount;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
                return  CartItemModel.CART_ITEM;
            case 1:
                return  CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType){
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout,viewGroup,false);
                return new CartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amaount_layout,viewGroup,false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String _id = cartItemModelList.get(position).get_id();
                Double productWeight = cartItemModelList.get(position).getProductWeight();
                Boolean inStock = cartItemModelList.get(position).getInStock();
                int quantity = cartItemModelList.get(position).getProductQuantity();
                String satuan = cartItemModelList.get(position).getSatuan();
                int minOrder = cartItemModelList.get(position).getMinOrder();

                ((CartItemViewholder)viewHolder).setItemDetails(context,resource,_id,productID,title,quantity,productPrice,position,inStock,satuan,productWeight,minOrder);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice =0;
                int totalDeleveryItemPrice =0;
                String deliveryPrice = "";
                int totalAmount ;
                int saveAmount = 0;


                for (int x=0;x<cartItemModelList.size();x++){

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).getInStock()){
                        int qty = Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProductQuantity()));
                        totalItems= totalItems + qty;
                        totalItemPrice = totalItemPrice + (Integer.parseInt(cartItemModelList.get(x).getProductPrice())* qty);
                        totalDeleveryItemPrice = totalDeleveryItemPrice +((int)(8000*cartItemModelList.get(x).getProductWeight())*qty);

                        if (totalItemPrice >= 200000 ) {
                            saveAmount = saveAmount+30000;
                        }

                    }
                }


                int totalDeliveryDiskon;
                totalDeliveryDiskon = totalDeleveryItemPrice-saveAmount;
//                if (totalItemPrice >= 200000 ){
//                  //  PaymentActivity.isfree = true;
//                   saveAmount = 30000;
//                    totalDeliveryDiskon = totalDeleveryItemPrice-30000;
//                }else {
//                    saveAmount = 0;
//                    totalDeliveryDiskon = totalDeleveryItemPrice;
//                }

                totalAmount = totalItemPrice +totalDeliveryDiskon;
                deliveryPrice = String.valueOf(totalDeleveryItemPrice);

                cartItemModelList.get(position).setTotalItems(totalItems);
                cartItemModelList.get(position).setTotalItemsPrice(totalItemPrice);
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(saveAmount);




                ((CartTotalAmountViewholder) viewHolder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, saveAmount, position);

                break;
            default:
                return;
        }

        if (lastPosition <position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productTitle;
        private TextView productPrice;
        private TextView productQuantity;

        private LinearLayout deleteBtn;


        ////coupondialog

        private TextView decrease;
        private TextView increase;
        private TextView satuan,satuan2;
        private TextView sendPrice;
        ////coupondialog

        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            decrease = itemView.findViewById(R.id.decrease);
            increase = itemView.findViewById(R.id.increase);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
            satuan = itemView.findViewById(R.id.satuan);
            satuan2 = itemView.findViewById(R.id.satuan2);
            sendPrice = itemView.findViewById(R.id.sendprice);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void  setItemDetails(final Context context, String resource,String _id, String productID, String title, int quantity, final String productPriceText,final int position, Boolean inStock, String satuanText,Double weight,int minOrder) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(productImage);
            productTitle.setText(title);


//            final Dialog checkCouponPriceDialog = new Dialog(itemView.getContext());
//          checkCouponPriceDialog.setCancelable(true);
//            checkCouponPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            int sendprice =  ((int)(8000*weight)*quantity);
            sendPrice.setText("Ongkir Rp "+currencyFormatter(String.valueOf(sendprice)));

            if (!satuanText.equals("")){
                satuan.setText(satuanText);
                satuan2.setText("/"+satuanText);
                satuan.setVisibility(View.VISIBLE);
                satuan2.setVisibility(View.VISIBLE);
            }else {
                satuan.setVisibility(View.GONE);
                satuan2.setVisibility(View.GONE);
            }
            if (inStock) {
                productQuantity.setVisibility(View.VISIBLE);

                sendPrice.setVisibility(View.VISIBLE);
                decrease.setVisibility(View.VISIBLE);
                increase.setVisibility(View.VISIBLE);


                productPrice.setText("Rp " + currencyFormatter(productPriceText) );
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));


               ///for  coupon dialog

                ///for  coupon dialog

                if (quantity<=minOrder){
                    decrease.setBackgroundTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.colorAccent)));
                    decrease.setTextColor(ColorStateList.valueOf(itemView.getResources().getColor(R.color.decrease_on)));
                    decrease.setEnabled(false);
                }else {
                    decrease.setEnabled(true);
                    decrease.setBackgroundTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.decrease_on)));
                    decrease.setTextColor(ColorStateList.valueOf(itemView.getResources().getColor(R.color.colorAccent)));
                }


                if (isDelivery){
                    increase.setVisibility(View.GONE);
                    decrease.setVisibility(View.GONE);
                    productQuantity.setText("Jumlah "+Long.toString(quantity));
                }else {
                    increase.setVisibility(View.VISIBLE);
                    decrease.setVisibility(View.VISIBLE);
                    productQuantity.setText(Long.toString(quantity));
                }
                 increase.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         int qty = Integer.parseInt((String) productQuantity.getText());


                             productQuantity.setText(String.valueOf(qty+1));
                             int sendprice =  ((int)(8000*weight)*qty+1);
                             cartItemModelList.get(position).setProductQuantity(qty+1);
                         int totalItemPrice =0;
                         int totalDeleveryItemPrice =0;
                         int totalAmountText;
                         int saveAmount =0;

                         for (int x=0;x< cartItemModelList.size();x++){

                             if ( cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM &&  cartItemModelList.get(x).getInStock()){
                                 int quantity = Integer.parseInt(String.valueOf( cartItemModelList.get(x).getProductQuantity()));

                                 totalItemPrice = totalItemPrice + (Integer.parseInt( cartItemModelList.get(x).getProductPrice())* quantity);
                                 totalDeleveryItemPrice = totalDeleveryItemPrice +((int)(8000* cartItemModelList.get(x).getProductWeight())*quantity);
                                 if (totalItemPrice >= 200000 ) {
                                     saveAmount = saveAmount+30000;
                                 }



                             }
                         }
                         if(totalItemPrice!=0){
                             int totalDeliveryDiskon;
                             totalDeliveryDiskon = totalDeleveryItemPrice-saveAmount;


                             totalAmountText = totalItemPrice +totalDeliveryDiskon;
                             if (MainActivity.showCart){
                                 if (CartActivity.totalAmount!=null){
                                     CartActivity.totalAmount.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(totalAmountText)));
                                 }
                             }else {
                                 if (totalAmount!=null){
                                     totalAmount.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(totalAmountText)));
                                 }
                             }

                         }else {
                             if (MainActivity.showCart){
                                 if (CartActivity.totalAmount!=null){
                                     CartActivity.totalAmount.setText("Rp 0");
                                 }
                             }else {
                                 if (totalAmount!=null){
                                     totalAmount.setText("Rp 0");
                                 }
                             }
                         }

                             sendPrice.setText("Ongkir Rp "+currencyFormatter(String.valueOf(sendprice)));


                             notifyItemChanged(cartItemModelList.size()-1);
//
                             loadingDialog.show();

                             cartItemModelList.get(position).setProductQuantity(qty+1);

                             linearLayoutManager = new LinearLayoutManager(context);
                             linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                             cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

                             linearLayoutManager.scrollToPosition(position);
                             cartAdapter.notifyDataSetChanged();
                             loadingDialog.dismiss();


                     }
                 });
                decrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int qty = Integer.parseInt((String) productQuantity.getText());
                        if (qty<=minOrder){
                            decrease.setBackgroundTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.colorAccent)));
                            decrease.setTextColor(ColorStateList.valueOf(itemView.getResources().getColor(R.color.decrease_on)));
                            decrease.setEnabled(false);
                            Toast.makeText(context,"Minimum pemesanan "+minOrder, Toast.LENGTH_SHORT).show();
                        }else {
                            decrease.setEnabled(true);
                            decrease.setBackgroundTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.decrease_on)));
                            decrease.setTextColor(ColorStateList.valueOf(itemView.getResources().getColor(R.color.colorAccent)));
                            productQuantity.setText(String.valueOf(qty-1));
                            int sendprice =  ((int)(8000*weight)*qty-1);
                            sendPrice.setText("Ongkir Rp "+currencyFormatter(String.valueOf(sendprice)));

                            cartItemModelList.get(position).setProductQuantity(qty-1);
                            int totalItemPrice =0;
                            int totalDeleveryItemPrice =0;
                            int totalAmountText;
                            int saveAmount =0;

                            for (int x=0;x< cartItemModelList.size();x++){

                                if ( cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM &&  cartItemModelList.get(x).getInStock()){
                                    int quantity = Integer.parseInt(String.valueOf( cartItemModelList.get(x).getProductQuantity()));

                                    totalItemPrice = totalItemPrice + (Integer.parseInt( cartItemModelList.get(x).getProductPrice())* quantity);
                                    totalDeleveryItemPrice = totalDeleveryItemPrice +((int)(8000* cartItemModelList.get(x).getProductWeight())*quantity);
                                    if (totalItemPrice >= 200000 ) {
                                        saveAmount = saveAmount+30000;
                                    }



                                }
                            }
                            if(totalItemPrice!=0){
                                int totalDeliveryDiskon;
                                totalDeliveryDiskon = totalDeleveryItemPrice-saveAmount;
                                totalAmountText = totalItemPrice +totalDeliveryDiskon;
                                if (MainActivity.showCart){
                                    if (CartActivity.totalAmount!=null){
                                        CartActivity.totalAmount.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(totalAmountText)));
                                    }
                                }else {
                                    if (totalAmount!=null){
                                        totalAmount.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(totalAmountText)));
                                    }
                                }

                            }else {
                                if (MainActivity.showCart){
                                    if (CartActivity.totalAmount!=null){
                                        CartActivity.totalAmount.setText("Rp 0");
                                    }
                                }else {
                                    if (totalAmount!=null){
                                        totalAmount.setText("Rp 0");
                                    }
                                }


                            }

                            loadingDialog.show();

                            cartItemModelList.get(position).setProductQuantity(qty-1);

                            linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

                            linearLayoutManager.scrollToPosition(position);
                            cartAdapter.notifyDataSetChanged();
                            loadingDialog.dismiss();
                        }

                    }
                });
//                productQuantity.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final Dialog quantityDialog = new Dialog(itemView.getContext());
//                        quantityDialog.setContentView(R.layout.quantity_dialog);
//                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        quantityDialog.setCancelable(false);
//                        final EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
//                        Button cancelbtn =  quantityDialog.findViewById(R.id.cancel_btn);
//                        Button okbtn =   quantityDialog.findViewById(R.id.ok_btn);
//
//                        cancelbtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                quantityDialog.dismiss();
//                            }
//                        });
//
//                        okbtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (!quantityNo.getText().toString().isEmpty()) {
//                                    if (Integer.parseInt(quantityNo.getText().toString())<=5) {
//                                        productQuantity.setText("Qty: " + quantityNo.getText());
//                                        notifyItemChanged(cartItemModelList.size()-1);
//
//                                        loadingDialog.show();
//
//                                        cartItemModelList.get(position).setProductQuantity(Long.parseLong(quantityNo.getText().toString()));
//
//                                        linearLayoutManager = new LinearLayoutManager(context);
//                                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                                        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);
//
//                                        linearLayoutManager.scrollToPosition(cartItemModelList.size() - 1);
//                                        cartAdapter.notifyDataSetChanged();
//                                        loadingDialog.dismiss();
//                                        quantityDialog.dismiss();
//                                    }else {
//                                        Toast.makeText(context,"Maximal pesan 5", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        });
//
//
//                        quantityDialog.show();
//
//                    }
//                });


            }else {
                productPrice.setText("Stok Habis");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent4));

                sendPrice.setVisibility(View.INVISIBLE);
                decrease.setVisibility(View.INVISIBLE);
                increase.setVisibility(View.INVISIBLE);

               productQuantity.setVisibility(View.INVISIBLE);


            }


            if (showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }






            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
//                        for (RewardModel rewardModel: DBqueries.rewardModelList){
//                            if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())){
//                                rewardModel.setAlreadyUsed(false);
//                            }
//                        }
                    //}
                    if (Integer.parseInt(badgeCount.getText().toString())-1 != 0) {
                        badgeCount.setVisibility(View.VISIBLE);
                    } else {
                        badgeCount.setVisibility(View.INVISIBLE);
                    }
                    if (Integer.parseInt(badgeCount.getText().toString())-1 < 99) {
                        badgeCount.setText(String.valueOf(Integer.parseInt(badgeCount.getText().toString())-1));
                    } else {
                        badgeCount.setText("99+");
                    }
                    DBQueries.removeCartList(_id, itemView.getContext(),loadingDialog,true, badgeCount,CartActivity.totalAmount);

                    if (!ProductDetailActivity.running_cart_query){
                        ProductDetailActivity.running_cart_query = true;

                    }
                }
            });


        }


    }
    class  CartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private  TextView totalItems;
        private  TextView totalItemPrice;
        private  TextView deliveryPrice;
        private  TextView totalAmount;
        private  TextView saveAmount;
        private TextView diskonText;
        private TextView diskon;



        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            saveAmount = itemView.findViewById(R.id.saved_amount);
            diskonText = itemView.findViewById(R.id.diskon_text);
            diskon = itemView.findViewById(R.id.diskon);
        }

        private  void setTotalAmount(int totalItemText, int totalItemPriceText,String deliveryPriceText,int totalAmountText, int saveAmountText,int position){



            totalItems.setText("Harga("+totalItemText+" barang)");
            totalItemPrice.setText("Rp "+currencyFormatter(String.valueOf(totalItemPriceText)));
            deliveryPrice.setText("+ Rp "+currencyFormatter(deliveryPriceText));

            diskon.setVisibility(View.VISIBLE);
            diskonText.setVisibility(View.VISIBLE);
            diskon.setText( "- Rp "+DBQueries.currencyFormatter(String.valueOf(saveAmountText)));
            if (!(totalAmountText==0)) {
                totalAmount.setText("Rp "+currencyFormatter(String.valueOf(totalAmountText)) );

                saveAmount.setText("Kamu Hemat Rp "+currencyFormatter(String.valueOf(saveAmountText)) +" pada pesanan ini");
            }
            if (totalItemPriceText == 0){
                cartItemModelList.remove(cartItemModelList.size()-1);
            }



        }
    }

    public static String currencyFormatter(String num) {
        double m = Double.parseDouble(num);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(m);
    }

}
