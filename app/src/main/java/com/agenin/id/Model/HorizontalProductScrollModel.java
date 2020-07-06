package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

public class HorizontalProductScrollModel {

    @SerializedName("_id")
    private String _id;
    @SerializedName("product_ID")
    private String productID;
    @SerializedName("image")
    private String productImage;
    @SerializedName("title_product")
    private String productTitle;
    @SerializedName("cutted_price")
    private String producCuttedPrice;
    @SerializedName("price")
    private String producPrice;

    public HorizontalProductScrollModel(String _id,String productID, String productImage, String productTitle, String producCuttedPrice,  String producPrice) {
        this._id = _id;
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.producCuttedPrice = producCuttedPrice;
        this.producPrice = producPrice;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProducCuttedPrice() {
        return producCuttedPrice;
    }

    public void setProducCuttedPrice(String producCuttedPrice) {
        this.producCuttedPrice = producCuttedPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }



    public String getProducPrice() {
        return producPrice;
    }

    public void setProducPrice(String producPrice) {
        this.producPrice = producPrice;
    }
}
