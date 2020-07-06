package com.agenin.id.Model;

import com.google.gson.annotations.SerializedName;

public class NotificationModel {
    @SerializedName("_id")
    private String _id;
    @SerializedName("title")
    private String title;
    @SerializedName("icon")
    private String image;
    @SerializedName("body")
    private String body;
    @SerializedName("readed")
    private  boolean readed;

    public NotificationModel(String image, String body, boolean readed) {
        this.image = image;
        this.body = body;
        this.readed = readed;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }
}

