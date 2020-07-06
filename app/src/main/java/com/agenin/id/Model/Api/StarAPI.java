package com.agenin.id.Model.Api;

import com.agenin.id.Model.StarModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StarAPI {

    @SerializedName("count")
    private Integer count;

    @SerializedName("ratings")
    private List<StarModel> ratings;

    public StarAPI(Integer count, List<StarModel> ratings) {
        this.count = count;
        this.ratings = ratings;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<StarModel> getRatings() {
        return ratings;
    }

    public void setRatings(List<StarModel> ratings) {
        this.ratings = ratings;
    }
}
