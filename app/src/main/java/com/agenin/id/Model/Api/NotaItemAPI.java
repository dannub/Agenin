package com.agenin.id.Model.Api;

import com.google.gson.annotations.SerializedName;

public class NotaItemAPI {
    @SerializedName("product_ID")
    private String product_ID;
    @SerializedName("jumlah")
    private int jumlah;
    @SerializedName("ongkir")
    private String ongkir;

    public NotaItemAPI(String product_ID, int jumlah, String ongkir) {
        this.product_ID = product_ID;
        this.jumlah = jumlah;
        this.ongkir = ongkir;
    }

}
