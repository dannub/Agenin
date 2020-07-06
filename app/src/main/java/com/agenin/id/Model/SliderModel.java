package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class SliderModel {

    @SerializedName("_id")
    private String _id;
    @SerializedName("banner")
    private String banner;
    @SerializedName("banner_background")
    private String backgroundColor;
    @SerializedName("date")
    private Date date;

    public SliderModel(String _id, String banner, String backgroundColor, Date date) {
        this._id = _id;
        this.banner = banner;
        this.backgroundColor = backgroundColor;
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
