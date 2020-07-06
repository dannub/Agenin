package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

public class AddressModel {
    @SerializedName("_id")
    private String _id;
    @SerializedName("selected")
    private Boolean selected;
    @SerializedName("negara")
    private String negara;
    @SerializedName("provinsi")
    private String provinsi;
    @SerializedName("kabupaten")
    private String kabupaten;
    @SerializedName("kecamatan")
    private String kecamatan;
    @SerializedName("detail")
    private String alamat;
    @SerializedName("kodepos")
    private String kodepos;
    @SerializedName("nama")
    private String nama;
    @SerializedName("no_telepon")
    private String no_telepon;
    @SerializedName("no_alternatif")
    private String no_alternatif;
    public AddressModel(String negara, String provinsi, String kabupaten, String kecamatan, String alamat, String kodepos, String nama, String no_telepon, String no_alternatif) {

        this.negara = negara;
        this.provinsi = provinsi;
        this.kabupaten = kabupaten;
        this.kecamatan = kecamatan;
        this.alamat = alamat;
        this.kodepos = kodepos;
        this.nama = nama;
        this.no_telepon = no_telepon;
        this.no_alternatif = no_alternatif;
    }
    public AddressModel( Boolean selected, String negara, String provinsi, String kabupaten, String kecamatan, String alamat, String kodepos, String nama, String no_telepon, String no_alternatif) {
        this._id = _id;
        this.selected = selected;
        this.negara = negara;
        this.provinsi = provinsi;
        this.kabupaten = kabupaten;
        this.kecamatan = kecamatan;
        this.alamat = alamat;
        this.kodepos = kodepos;
        this.nama = nama;
        this.no_telepon = no_telepon;
        this.no_alternatif = no_alternatif;
    }
    public AddressModel(String _id, Boolean selected, String negara, String provinsi, String kabupaten, String kecamatan, String alamat, String kodepos, String nama, String no_telepon, String no_alternatif) {
        this._id = _id;
        this.selected = selected;
        this.negara = negara;
        this.provinsi = provinsi;
        this.kabupaten = kabupaten;
        this.kecamatan = kecamatan;
        this.alamat = alamat;
        this.kodepos = kodepos;
        this.nama = nama;
        this.no_telepon = no_telepon;
        this.no_alternatif = no_alternatif;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getNegara() {
        return negara;
    }

    public void setNegara(String negara) {
        this.negara = negara;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKodepos() {
        return kodepos;
    }

    public void setKodepos(String kodepos) {
        this.kodepos = kodepos;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNo_telepon() {
        return no_telepon;
    }

    public void setNo_telepon(String no_telepon) {
        this.no_telepon = no_telepon;
    }

    public String getNo_alternatif() {
        return no_alternatif;
    }

    public void setNo_alternatif(String no_alternatif) {
        this.no_alternatif = no_alternatif;
    }
//
//    public AddressModel(Boolean selected, String city, String locality, String flatNo, String pincode, String landmark, String name, String mobileNo, String alternativeMobileNo, String state) {
//        this.selected = selected;
//        this.city = city;
//        this.locality = locality;
//        this.flatNo = flatNo;
//        this.pincode = pincode;
//        this.state = state;
//        this.landmark = landmark;
//        this.name = name;
//        this.mobileNo = mobileNo;
//        this.alternativeMobileNo = alternativeMobileNo;
//    }


}
