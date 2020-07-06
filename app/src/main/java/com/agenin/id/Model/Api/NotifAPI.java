package com.agenin.id.Model.Api;

import com.agenin.id.Model.NotificationModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotifAPI {
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("notifications")
    private List<NotificationModel> notifications;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<NotificationModel> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationModel> notifications) {
        this.notifications = notifications;
    }
}
