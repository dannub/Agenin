package com.agenin.id.Model.Api;

import com.agenin.id.Model.WishlistModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WishlistAPI {
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("wishlist")
    private List<WishlistModel> wishlist;

    public WishlistAPI(String user_id, List<WishlistModel> wishlist) {
        this.user_id = user_id;
        this.wishlist = wishlist;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<WishlistModel> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<WishlistModel> wishlist) {
        this.wishlist = wishlist;
    }
}
