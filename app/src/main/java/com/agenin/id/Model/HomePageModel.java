package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomePageModel {

    public  static final int BANNER_SLIDER = 0;
    public  static final int STRIP_ADD_BANNER = 1;
    public  static final int HORISONTAL_PRODUCT_VIEW = 2;
    public  static final int GRID_PRODUCT_VIEW = 3;
    public  static final int CATEGORY_VIEW = 4;


    @SerializedName("view_type")
    private int type;

    @SerializedName("_id")
    private String _id;


    ////////////////////Category


    @SerializedName("category")
    private List<CategoryModel> categoryModelList;
    public HomePageModel(int type, List<CategoryModel> categoryModelList) {
        this.type = type;
        this.categoryModelList = categoryModelList;
    }

    public List<CategoryModel> getCategoryModelList() {
        return categoryModelList;
    }

    public void setCategoryModelList(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }
    ////////////////////Category


    ////////////////////////// Banner Slider


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @SerializedName("move_banner")
    private List<SliderModel> sliderModelList;
    public HomePageModel(String _id,int type, List<SliderModel> sliderModelList) {
        this._id= _id;
        this.type = type;
        this.sliderModelList = sliderModelList;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }
    ////////////////////////// Banner Slider


    /////////////////////// Strip Ad

    @SerializedName("background")
    private String backgroundColor;
    @SerializedName("strip_ad_banner")
    private String resource;
    public HomePageModel(String _id, int type, String resource, String backgroundColor) {
        this._id= _id;
        this.type = type;
        this.resource = resource;
        this.backgroundColor = backgroundColor;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    public String getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    /////////////////////// Strip Ad

    @SerializedName("layout_background")
    private String layoutbackgroundColor;
    @SerializedName("title_background")
    private String title;
    @SerializedName("horisontal_view")
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    //////////////////////Horizontal Product Layout

    @SerializedName("horisontal_view")
    private List<WishlistModel> horizontalViewAllProductList;

    public HomePageModel(String _id,int type, String title, String layoutbackgroundColor, List<HorizontalProductScrollModel> horizontalProductScrollModelList, List<WishlistModel> horizontalViewAllProductList) {
        this._id= _id;
        this.type = type;
        this.title = title;
        this.layoutbackgroundColor=layoutbackgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.horizontalViewAllProductList = horizontalViewAllProductList;
    }

    public List<WishlistModel> getHorizontalViewAllProductList() {
        return horizontalViewAllProductList;
    }

    public String getLayoutbackgroundColor() {
        return layoutbackgroundColor;
    }

    public void setLayoutbackgroundColor(String layoutbackgroundColor) {
        this.layoutbackgroundColor = layoutbackgroundColor;
    }

    //////////////////////Horizontal Product Layout



    @SerializedName("grid_view")
    private List<HorizontalProductScrollModel> gridProductScrollModelList;

    //////////////////////GridProduct Layout

    @SerializedName("grid_view")
    private List<WishlistModel> gridViewAllProductList;

    //////////////////////Grid Product Layout
    public HomePageModel(String _id,int type, String title, String layoutbackgroundColor, List<HorizontalProductScrollModel> gridProductScrollModelList) {
        this._id= _id;
        this.type = type;
        this.title = title;
        this.layoutbackgroundColor=layoutbackgroundColor;
        this.gridProductScrollModelList = gridProductScrollModelList;
    }
    public List<WishlistModel> getGridViewAllProductList() {
        return gridViewAllProductList;
    }
    //////////////////////Grid Product Layout

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<HorizontalProductScrollModel> getHorizontalProductScrollModelList() {
        return horizontalProductScrollModelList;
    }
    public void setHorizontalProductScrollModelList(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    public List<HorizontalProductScrollModel> getGridlProductScrollModelList() {
        return gridProductScrollModelList;
    }
    public void setGridProductScrollModelList(List<HorizontalProductScrollModel> gridProductScrollModelList) {
        this.gridProductScrollModelList = gridProductScrollModelList;
    }

    //////////////////////Horizontal Product Layout & Grid Layout






}
