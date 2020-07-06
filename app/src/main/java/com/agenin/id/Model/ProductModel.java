package com.agenin.id.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModel {

    @SerializedName("_id")
    private String _id;
    @SerializedName("incharge")
    private String incharge;
    @SerializedName("title_product")
    private String title_product;
    @SerializedName("category")
    private String category;
    @SerializedName("price")
    private String price;
    @SerializedName("cutted_price")
    private String cutted_price;
    @SerializedName("satuan")
    private String satuan;
    @SerializedName("berat")
    private Double berat;
    @SerializedName("sent_from")
    private String sent_from;
    @SerializedName("min_order")
    private String min_order;
    @SerializedName("estimation")
    private String estimation;
    @SerializedName("star_1")
    private Integer star_1;
    @SerializedName("star_2")
    private Integer star_2;
    @SerializedName("star_3")
    private Integer star_3;
    @SerializedName("star_4")
    private Integer star_4;
    @SerializedName("star_5")
    private Integer star_5;
    @SerializedName("average_rating")
    private String average_rating;
    @SerializedName("total_ratings")
    private Integer total_ratings;
    @SerializedName("in_stock")
    private Boolean in_stock;
    @SerializedName("description")
    private String decription;
    @SerializedName("no_pedagang")
    private String no_pedagang;
    @SerializedName("image")
    @Expose
    private List<String> image= null;
    @SerializedName("tags")
    @Expose
    private List<String> tags= null;
    @SerializedName("date")
    private String date;

    public ProductModel(String _id, String incharge, String title_product, String category, String price, String cutted_price, String satuan, Double berat, String sent_from, String min_order, String estimation, Integer star_1, Integer star_2, Integer star_3, Integer star_4, Integer star_5, String average_rating, Integer total_ratings, Boolean in_stock, String decription, String no_pedagang, List<String> image, List<String> tags, String date) {
        this._id = _id;
        this.incharge = incharge;
        this.title_product = title_product;
        this.category = category;
        this.price = price;
        this.cutted_price = cutted_price;
        this.satuan = satuan;
        this.berat = berat;
        this.sent_from = sent_from;
        this.min_order = min_order;
        this.estimation = estimation;
        this.star_1 = star_1;
        this.star_2 = star_2;
        this.star_3 = star_3;
        this.star_4 = star_4;
        this.star_5 = star_5;
        this.average_rating = average_rating;
        this.total_ratings = total_ratings;
        this.in_stock = in_stock;
        this.decription = decription;
        this.no_pedagang = no_pedagang;
        this.image = image;
        this.tags = tags;
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIncharge() {
        return incharge;
    }

    public void setIncharge(String incharge) {
        this.incharge = incharge;
    }

    public String getTitle_product() {
        return title_product;
    }

    public void setTitle_product(String title_product) {
        this.title_product = title_product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCutted_price() {
        return cutted_price;
    }

    public void setCutted_price(String cutted_price) {
        this.cutted_price = cutted_price;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public Double getBerat() {
        return berat;
    }

    public void setBerat(Double berat) {
        this.berat = berat;
    }

    public String getSent_from() {
        return sent_from;
    }

    public void setSent_from(String sent_from) {
        this.sent_from = sent_from;
    }

    public String getMin_order() {
        return min_order;
    }

    public void setMin_order(String min_order) {
        this.min_order = min_order;
    }

    public String getEstimation() {
        return estimation;
    }

    public void setEstimation(String estimation) {
        this.estimation = estimation;
    }

    public Integer getStar_1() {
        return star_1;
    }

    public void setStar_1(Integer star_1) {
        this.star_1 = star_1;
    }

    public Integer getStar_2() {
        return star_2;
    }

    public void setStar_2(Integer star_2) {
        this.star_2 = star_2;
    }

    public Integer getStar_3() {
        return star_3;
    }

    public void setStar_3(Integer star_3) {
        this.star_3 = star_3;
    }

    public Integer getStar_4() {
        return star_4;
    }

    public void setStar_4(Integer star_4) {
        this.star_4 = star_4;
    }

    public Integer getStar_5() {
        return star_5;
    }

    public void setStar_5(Integer star_5) {
        this.star_5 = star_5;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public Integer getTotal_ratings() {
        return total_ratings;
    }

    public void setTotal_ratings(Integer total_ratings) {
        this.total_ratings = total_ratings;
    }

    public Boolean getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(Boolean in_stock) {
        this.in_stock = in_stock;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getNo_pedagang() {
        return no_pedagang;
    }

    public void setNo_pedagang(String no_pedagang) {
        this.no_pedagang = no_pedagang;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
