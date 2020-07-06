package com.agenin.id.Model.Api;

import com.agenin.id.Model.UserModel;
import com.google.gson.annotations.SerializedName;

public class UserAPI {
    @SerializedName("mesagge")
    private String mesagge;
    @SerializedName("user")
    private UserModel user;

    public String getMesagge() {
        return mesagge;
    }

    public void setMesagge(String mesagge) {
        this.mesagge = mesagge;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
