package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

public class StarModel {
    @SerializedName("_id")
    private String _id;
    @SerializedName("product_ID")
    private String product_ID;
    @SerializedName("rating")
    private Integer rating;

    public StarModel(String _id, String product_ID, Integer rating) {
        this._id = _id;
        this.product_ID = product_ID;
        this.rating = rating;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
