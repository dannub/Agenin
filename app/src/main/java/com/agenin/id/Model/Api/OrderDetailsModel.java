package com.agenin.id.Model.Api;

import com.agenin.id.Model.MyOrderItemModel;
import com.agenin.id.Model.NotaItemModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailsModel {
    @SerializedName("message")
    private MyOrderItemModel message;
    @SerializedName("nota")
    private List<NotaItemModel> nota;

    public MyOrderItemModel getMessage() {
        return message;
    }

    public List<NotaItemModel> getNota() {
        return nota;
    }
}
