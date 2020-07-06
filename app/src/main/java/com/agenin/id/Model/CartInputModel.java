package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

public class CartInputModel {
    @SerializedName("_id")
    private String _id;
    @SerializedName("product_ID")
    private String product_ID;
    @SerializedName("jumlah")
    private int jumlah;

    public CartInputModel(String _id, String product_ID, int jumlah) {
        this._id = _id;
        this.product_ID = product_ID;
        this.jumlah = jumlah;
    }

}
