package com.agenin.id.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.agenin.id.Adapter.WishlistAdapter;
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.ProductClient;
import com.agenin.id.Model.WishlistModel;
import com.agenin.id.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private androidx.appcompat.widget.SearchView searchView;
    private TextView textView;
    private RecyclerView recyclerView;
    private WishlistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view);
        textView = findViewById(R.id.textview);
        recyclerView = findViewById(R.id.cart_recycleview);

        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        final List<WishlistModel> wishlistModelList = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        adapter = new WishlistAdapter(wishlistModelList,false);
        adapter.setFromSearch(true);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()  {
            @Override
            public boolean onQueryTextSubmit(String query) {
                wishlistModelList.clear();
                ids.clear();
                final String [] tags = query.toLowerCase().split(" ");
                for (final String tag : tags) {
                    tag.trim();
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();

                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .build();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(DBQueries.url)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    ProductClient productApi = retrofit.create(ProductClient.class);
                    Call<ResponseBody> call = productApi.searchProduct(tag);
                    call.enqueue(new Callback<ResponseBody>() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()){
                                Toast.makeText(SearchActivity.this,response.code(),Toast.LENGTH_SHORT).show();
                                return;

                            }


                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());

                                JSONArray jsonArray = new JSONArray(jsonObject.get("products").toString());
                                for (int i=0;i<jsonArray.length();i++){

                                    JSONObject product = new JSONObject(jsonArray.get(i).toString());

                                    WishlistModel wishlistModel;
                                    JSONArray image =new JSONArray(product.get("image").toString());

                                    wishlistModel = new WishlistModel(
                                            product.get("_id").toString()
                                            ,product.get("_id").toString()
                                            ,DBQueries.url+image.get(0).toString()
                                            ,  product.get("title_product").toString()
                                            ,product.get("average_rating").toString()
                                            ,(int)product.get("total_ratings")
                                            ,product.get("price").toString()
                                            ,product.get("cutted_price").toString()
                                            ,(Boolean)product.get("in_stock")
                                            ,product.get("satuan").toString()
                                    );

                                    JSONArray tagsarray = new JSONArray(product.get("tags").toString());
                                    ArrayList<String> tags = new ArrayList<String>();
                                    for (int j=0; j<tagsarray.length();j++){
                                        tags.add(j,tagsarray.get(j).toString().toLowerCase());
                                    }
                                    wishlistModel.setTags(tags);
                                    Log.i("title", wishlistModel.getProductImage());


                                        wishlistModelList.add(wishlistModel);
                                        ids.add(wishlistModel.getProductID());



                                }

                                adapter.notifyDataSetChanged();

                                if (tag.equals(tags[tags.length-1])){
                                    if (wishlistModelList.size()==0){
                                        textView.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }else {
                                        textView.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                       // adapter.getFilter().filter(query);
                                    }
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(SearchActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.i("error",t.getMessage());
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                wishlistModelList.clear();
                ids.clear();
                final String [] tags = newText.toLowerCase().split(" ");
                for (final String tag : tags) {
                    tag.trim();
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();

                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .build();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(DBQueries.url)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    ProductClient productApi = retrofit.create(ProductClient.class);
                    Call<ResponseBody> call = productApi.searchProduct(tag);
                    call.enqueue(new Callback<ResponseBody>() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()){
                                Toast.makeText(SearchActivity.this,response.code(),Toast.LENGTH_SHORT).show();
                                return;

                            }


                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());

                                JSONArray jsonArray = new JSONArray(jsonObject.get("products").toString());
                                for (int i=0;i<jsonArray.length();i++){

                                    JSONObject product = new JSONObject(jsonArray.get(i).toString());

                                    WishlistModel wishlistModel;
                                    JSONArray image =new JSONArray(product.get("image").toString());

                                    wishlistModel = new WishlistModel(
                                            product.get("_id").toString()
                                            ,product.get("_id").toString()
                                            ,DBQueries.url+image.get(0).toString()
                                            ,  product.get("title_product").toString()
                                            ,product.get("average_rating").toString()
                                            ,(int)product.get("total_ratings")
                                            ,product.get("price").toString()
                                            ,product.get("cutted_price").toString()
                                            ,(Boolean)product.get("in_stock")
                                            ,product.get("satuan").toString()
                                    );

                                    JSONArray tagsarray = new JSONArray(product.get("tags").toString());
                                    ArrayList<String> tags = new ArrayList<String>();
                                    for (int j=0; j<tagsarray.length();j++){
                                        tags.add(j,tagsarray.get(j).toString().toLowerCase());
                                    }
                                    wishlistModel.setTags(tags);
                                    Log.i("title", wishlistModel.getProductImage());


                                    wishlistModelList.add(wishlistModel);
                                    ids.add(wishlistModel.getProductID());



                                }

                                adapter.notifyDataSetChanged();

                                if (tag.equals(tags[tags.length-1])){
                                    if (wishlistModelList.size()==0){
                                        textView.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }else {
                                        textView.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        // adapter.getFilter().filter(query);
                                    }
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(SearchActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.i("error",t.getMessage());
                        }
                    });
                }
                return false;
            }
        });
    }

    class Adapter extends WishlistAdapter implements Filterable {



        private List<WishlistModel> originalList;

        public Adapter(List<WishlistModel> wishlistModelList, Boolean wishlist) {
            super(wishlistModelList, wishlist);
            originalList = wishlistModelList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    List<WishlistModel> filteredList = new ArrayList<>();

                    final String [] tags = constraint.toString().toLowerCase().split(" ");

                    for (WishlistModel model : originalList){
                        ArrayList<String> presentTags = new ArrayList<>();
                        for (String tag : tags){
                            if (model.getTags().contains(tag.toLowerCase())){
                                presentTags.add(tag.toLowerCase());
                            }
                        }
                        model.setTags(presentTags);
                    }

                    for (int i = tags.length;i>0;i--){
                        for (WishlistModel model:originalList){
                            if (model.getTags().size()==i){
                                filteredList.add(model);
                            }
                        }
                    }

                    results.values = filteredList;
                    results.count = filteredList.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results.count>0){
                        setWishlistModelList((List<WishlistModel>) results.values);
                    }
                    notifyDataSetChanged();
                }
            };
        }
    }
}