package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyOrderItemModel {

    @SerializedName("_id")
    private String _id;

    @SerializedName("user_id")
    private String user_id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;

    @SerializedName("atas_nama")
    private String atas_nama;
    @SerializedName("bank")
    private String bank;
    @SerializedName("tgl_transfer")
    private String tgl_transfer;

    @SerializedName("total_ongkir")
    private String total_ongkir;
    @SerializedName("total_item_price")
    private String total_item_price;
    @SerializedName("total_amount")
    private String total_amount;
    @SerializedName("save_ongkir")
    private String save_ongkir;



    @SerializedName("full_address")
    private String full_address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("detail_address")
    private String detail_address;
    @SerializedName("kode_pos")
    private String kode_pos;

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



    @SerializedName("ket_kirim")
    private String ket_kirim;
    @SerializedName("metode_kirim")
    private String metode_kirim;

    @SerializedName("items")
    private NotaItemModel items;

    public String get_id() {
        return _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAtas_nama() {
        return atas_nama;
    }

    public String getBank() {
        return bank;
    }

    public String getTgl_transfer() {
        return tgl_transfer;
    }

    public String getTotal_ongkir() {
        return total_ongkir;
    }

    public String getTotal_item_price() {
        return total_item_price;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getSave_ongkir() {
        return save_ongkir;
    }

    public String getFull_address() {
        return full_address;
    }

    public String getPhone() {
        return phone;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public String getKode_pos() {
        return kode_pos;
    }

    public String getOrdered_date() {
        return ordered_date;
    }

    public String getConfirmed_date() {
        return confirmed_date;
    }

    public String getPacked_date() {
        return packed_date;
    }

    public String getShipped_date() {
        return shipped_date;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public String getCanceled_date() {
        return canceled_date;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public Boolean getPacked() {
        return packed;
    }

    public Boolean getShipped() {
        return shipped;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public String getKet_kirim() {
        return ket_kirim;
    }

    public String getMetode_kirim() {
        return metode_kirim;
    }

    public NotaItemModel getItems() {
        return items;
    }
}
