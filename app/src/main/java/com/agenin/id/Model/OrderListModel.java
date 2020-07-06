package com.agenin.id.Model;

import com.agenin.id.Model.ProductModel;
import com.google.gson.annotations.SerializedName;

public class OrderListModel {
    @SerializedName("ordered_date")
    private String ordered_date;
    @SerializedName("confirmed_date")
    private String confirmed_date;
    @SerializedName("packed_date")
    private String packed_date;
    @SerializedName("shipped_date")
    private String shipped_date;
    @SerializedName("delivered_date")
    private String delivered_date;
    @SerializedName("canceled_date")
    private String canceled_date;
    @SerializedName("confirmed")
    private Boolean confirmed;
    @SerializedName("packed")
    private Boolean packed;
    @SerializedName("shipped")
    private Boolean shipped;
    @SerializedName("delivered")
    private Boolean delivered;
    @SerializedName("canceled")
    private Boolean canceled;
    @SerializedName("nota_id")
    private String nota_id;
    @SerializedName("jumlah")
    private int jumlah;
    @SerializedName("ongkir")
    private String ongkir;
    @SerializedName("rating")
    private int rating;
    @SerializedName("product_ID")
    private ProductModel product_ID;
    @SerializedName("ket_kirim")
    private String ket_kirim;
    @SerializedName("metode_kirim")
    private String metode_kirim;

    public String getOrdered_date() {
        return ordered_date;
    }

    public void setOrdered_date(String ordered_date) {
        this.ordered_date = ordered_date;
    }

    public String getConfirmed_date() {
        return confirmed_date;
    }

    public void setConfirmed_date(String confirmed_date) {
        this.confirmed_date = confirmed_date;
    }

    public String getPacked_date() {
        return packed_date;
    }

    public void setPacked_date(String packed_date) {
        this.packed_date = packed_date;
    }

    public String getShipped_date() {
        return shipped_date;
    }

    public void setShipped_date(String shipped_date) {
        this.shipped_date = shipped_date;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(String delivered_date) {
        this.delivered_date = delivered_date;
    }

    public String getCanceled_date() {
        return canceled_date;
    }

    public void setCanceled_date(String canceled_date) {
        this.canceled_date = canceled_date;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Boolean getPacked() {
        return packed;
    }

    public void setPacked(Boolean packed) {
        this.packed = packed;
    }

    public Boolean getShipped() {
        return shipped;
    }

    public void setShipped(Boolean shipped) {
        this.shipped = shipped;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public String getNota_id() {
        return nota_id;
    }

    public void setNota_id(String nota_id) {
        this.nota_id = nota_id;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getOngkir() {
        return ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public ProductModel getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(ProductModel product_ID) {
        this.product_ID = product_ID;
    }

    public String getKet_kirim() {
        return ket_kirim;
    }

    public void setKet_kirim(String ket_kirim) {
        this.ket_kirim = ket_kirim;
    }

    public String getMetode_kirim() {
        return metode_kirim;
    }

    public void setMetode_kirim(String metode_kirim) {
        this.metode_kirim = metode_kirim;
    }
}
