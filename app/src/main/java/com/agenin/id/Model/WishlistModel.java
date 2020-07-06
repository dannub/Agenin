package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WishlistModel implements Comparable<WishlistModel> {

    @SerializedName("_id")
    private String _id;
    @SerializedName("image")
    private String productImage;
    @SerializedName("product_ID")
    private String productID;
    @SerializedName("title_product")
    private String productTitle;
    @SerializedName("average_rating")
    private String ratting;
    @SerializedName("total_ratings")
    private Integer totalRattings;
    @SerializedName("price")
    private String productPrice;
    @SerializedName("cutted_price")
    private String oriPrice;
    @SerializedName("in_stock")
    private Boolean inStock;
    @SerializedName("satuan")
    private String satuan;
    @SerializedName("tags")
    private ArrayList<String> tags;

    public WishlistModel(String id,String productID, String image, String title_product, String average_rating, int total_ratings, String price, String cutted_price, Boolean in_stock, String satuan) {
        this._id = id;
        this.productID = productID;
        this.productImage = image;
        this.productTitle = title_product;
        this.ratting = average_rating;
        this.totalRattings = total_ratings;
        this.productPrice = price;
        this.oriPrice = cutted_price;
        this.inStock = in_stock;
        this.satuan = satuan;
    }
    public WishlistModel(String _id, String productImage, String productID, String productTitle, String ratting, Integer totalRattings, String productPrice, String oriPrice, Boolean inStock, String satuan, ArrayList<String> tags) {
        this._id = _id;
        this.productImage = productImage;
        this.productID = productID;
        this.productTitle = productTitle;
        this.ratting = ratting;
        this.totalRattings = totalRattings;
        this.productPrice = productPrice;
        this.oriPrice = oriPrice;
        this.inStock = inStock;
        this.satuan = satuan;
        this.tags = tags;
    }



    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getRatting() {
        return ratting;
    }

    public void setRatting(String ratting) {
        this.ratting = ratting;
    }

    public Integer getTotalRattings() {
        return totalRattings;
    }

    public void setTotalRattings(Integer totalRattings) {
        this.totalRattings = totalRattings;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(String oriPrice) {
        this.oriPrice = oriPrice;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @Override
    public int compareTo(WishlistModel o) {
        if (getProductTitle() == null || o.getProductTitle() == null)
            return 0;
        return getProductTitle().compareTo(o.getProductTitle());
    }
}
