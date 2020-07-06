package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

public class NotaItemModel {
    @SerializedName("product_ID")
    private ProductModel product_ID;
    @SerializedName("jumlah")
    private int jumlah;
    @SerializedName("ongkir")
    private String ongkir;

    public ProductModel getProduct_ID() {
        return product_ID;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getOngkir() {
        return ongkir;
    }
}
