package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

public class CartItemModel implements Comparable<CartItemModel> {


    //////cart item
    @SerializedName("_id")
    private String _id;
    @SerializedName("product_ID")
    private String productID;
    @SerializedName("image")
    private String productImage;
    @SerializedName("title_product")
    private String productTitle;
    @SerializedName("price")
    private String productPrice;
    @SerializedName("cutted_price")
    private String productOriPrice;
    @SerializedName("berat")
    private Double productWeight;
    @SerializedName("jumlah")
    private int productQuantity;
    @SerializedName("in_stock")
    private Boolean inStock;
    @SerializedName("min_order")
    private int minOrder;
    @SerializedName("average_rating")
    private String ratting;
    @SerializedName("satuan")
    private String satuan;

    private String ongkir;

    public  static final int CART_ITEM = 0;
    public  static final int TOTAL_AMOUNT = 1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public CartItemModel(){

    }

    public CartItemModel(int type, String _id,String productID, String productImage, String productTitle, String productPrice, int productQuantity, Boolean inStock, String ratting, String satuan, Double productWeight,int minOrder,String ongkir) {
        this._id=_id;
        this.ongkir = ongkir;
        this.type = type;
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productWeight = productWeight;
        this.productPrice = productPrice;
        this.minOrder = minOrder;
        this.productQuantity = productQuantity;

        this.inStock = inStock;
        this.ratting = ratting;
        this.satuan = satuan;
    }

    public CartItemModel(int type, String productID, String productImage, String productTitle, String productPrice, int productQuantity, Boolean inStock, String ratting, String satuan, Double productWeight,int minOrder,String ongkir) {

        this.ongkir = ongkir;
        this.type = type;
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productWeight = productWeight;
        this.productPrice = productPrice;
        this.minOrder = minOrder;
        this.productQuantity = productQuantity;

        this.inStock = inStock;
        this.ratting = ratting;
        this.satuan = satuan;
    }

    public int getMinOrder() {
        return minOrder;
    }

    public String getOngkir() {
        return ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public Double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(Double productWeight) {
        this.productWeight = productWeight;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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



    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductOriPrice() {
        return productOriPrice;
    }

    public void setProductOriPrice(String productOriPrice) {
        this.productOriPrice = productOriPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getRatting() {
        return ratting;
    }

    public void setRatting(String ratting) {
        this.ratting = ratting;
    }


    //////cart item

    //////cart total
    private  int totalItems,totalItemsPrice,totalAmount,savedAmount;
    private String deliveryPrice;
    public CartItemModel(int type) {
        this.type = type;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public void setTotalItemsPrice(int totalItemsPrice) {
        this.totalItemsPrice = totalItemsPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    @Override
    public int compareTo(CartItemModel o) {
        if (getProductTitle() == null || o.getProductTitle() == null)
            return 0;
        return getProductTitle().compareTo(o.getProductTitle());
    }


    //////cart total


}
