package com.agenin.id.Model.Api;

import com.agenin.id.Model.CartInputModel;
import com.agenin.id.Model.CartItemModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartInputAPI {
    @SerializedName("carts")
    private List<CartInputModel> carts;

    public CartInputAPI( List<CartInputModel> carts) {

        this.carts = carts;
    }


    public List<CartInputModel> getCarts() {
        return carts;
    }

    public void setCarts(List<CartInputModel> carts) {
        this.carts = carts;
    }
}
