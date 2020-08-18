package com.agenin.id.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agenin.id.Activity.MainActivity;
import com.agenin.id.DBQueries;
import com.agenin.id.Fragment.ui.HomeFragment;
import com.agenin.id.Model.CategoryModel;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> categoryModelList;
    private  int lastPosition = -1;
    private Context context;

    public CategoryAdapter(Context context,List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_item_layout,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder viewHolder, int position) {

        String icon = categoryModelList.get(position).getCategoryIconLink();
        String name = categoryModelList.get(position).getCategoryName();
        String slug = categoryModelList.get(position).getCategorySlug();
        viewHolder.setCategory(name,position,slug);
        viewHolder.setCategoryIcon(icon);
        if (lastPosition <position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{

        private ImageView categoryIcon;
        private TextView categoryName;
        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.category_icon);
             categoryName = itemView.findViewById(R.id.category_name);
             constraintLayout = itemView.findViewById(R.id.category_container);
        }

        private void  setCategoryIcon(String iconUri){
            if (!iconUri.equals("null")){
                Glide.with(itemView.getContext()).load(iconUri).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(categoryIcon);
            }

        }

        private void setCategory(final String name, final int position,final String slug){

                categoryName.setText(name);

                if (!name.equals("")) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                          //  DBQueries.loadedCategoriesNames.add(slug);

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            HomeFragment.homepagerecyclerView.setLayoutManager(linearLayoutManager);

                            HomeFragment.reloadPage(context,slug,name,false);
                            MainActivity.categoryName=name;
                            MainActivity.categorySlug= slug;

                        }
                    });
                }

        }

    }
}
