package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {


    @SerializedName("_id")
    private String categoryId;

    @SerializedName("status")
    private String categoryStatus;

    @SerializedName("slug")
    private String categorySlug;

    @SerializedName("icon")
    private String categoryIconLink;

    @SerializedName("category_name")
    private String categoryName;

    public CategoryModel(String categoryId, String categoryStatus, String categorySlug, String categoryIconLink, String categoryName) {
        this.categoryId = categoryId;
        this.categoryStatus = categoryStatus;
        this.categorySlug = categorySlug;
        this.categoryIconLink = categoryIconLink;
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(String categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public String getCategoryIconLink() {
        return categoryIconLink;
    }

    public void setCategoryIconLink(String categoryIconLink) {
        this.categoryIconLink = categoryIconLink;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    //    public CategoryModel(String categoryIconLink, String categoryName) {
//        this.categoryIconLink = categoryIconLink;
//        this.categoryName = categoryName;
//    }
//
//    public String getCategoryIconLink() {
//        return categoryIconLink;
//    }
//
//    public void setCategoryIconLink(String categoryIconLink) {
//        this.categoryIconLink = categoryIconLink;
//    }
//
//    public String getCategoryName() {
//        return categoryName;
//    }
//
//    public void setCategoryName(String categoryName) {
//        this.categoryName = categoryName;
//    }
}
