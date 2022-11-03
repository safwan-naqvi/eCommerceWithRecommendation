package com.example.afecommerce.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.afecommerce.Category.BookCategoryActivity;
import com.example.afecommerce.Category.CategoryActivity;
import com.example.afecommerce.Model.Category;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.ItemCategoriesBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<Category> categories;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.binding.categoryText.setText(category.getName());
        Glide.with(context)
                .load(category.getIcon())
                .into(holder.binding.categoryImg);
        holder.binding.categoryImg.setBackgroundColor(Color.parseColor(category.getColor()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoryIntent;
                if(category.getName().equals("Books")){
                    categoryIntent = new Intent(context, BookCategoryActivity.class);
                }else{
                    categoryIntent = new Intent(context, CategoryActivity.class);
                    categoryIntent.putExtra("CategoryName",category.getName());
                }
                context.startActivity(categoryIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ItemCategoriesBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoriesBinding.bind(itemView);
        }
    }

}
