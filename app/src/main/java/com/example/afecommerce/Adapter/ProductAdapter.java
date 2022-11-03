package com.example.afecommerce.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.afecommerce.HelperClasses.Constants;
import com.example.afecommerce.Model.Product;
import com.example.afecommerce.Products.ProductDetailsActivity;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.ItemProductBinding;


import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        Glide.with(context)
                .load(product.getImageURL())
                .into(holder.binding.productItemImg);
        holder.binding.productItemName.setText(product.getProductTitle());
        holder.binding.productItemPrice.setText("PKR " + product.getPrice());
        holder.binding.productItemCategory.setText(product.getCategory());

        Double rating = (product.getFive_star() * 5) + (product.getFour_star() * 4) + (product.getThree_star() * 3) + (product.getTwo_star() * 2) + (product.getOne_star() * 1);
        Double total_responses = product.getFive_star() + product.getFour_star() + product.getThree_star() + product.getTwo_star() + product.getOne_star();
        Double fiveStarRating = Math.ceil(rating / total_responses);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra(Constants.PRODUCT_ID, product.getProductId());
                intent.putExtra(Constants.PRODUCT_CATEGORY, product.getCategory());
                intent.putExtra(Constants.PRODUCT_SUBCATEGORY, product.getSubCategory());
                intent.putExtra(Constants.PRODUCT_TITLE, product.getProductTitle());
                intent.putExtra(Constants.PRODUCT_TYPE, product.getProductType());
                intent.putExtra("url", product.getImageURL());
                intent.putExtra("price", product.getPrice());
                intent.putExtra(Constants.PRODUCT_COLOR, product.getColor());
                intent.putExtra(Constants.PRODUCT_DATE_ADDED, product.getDateAdded());
                intent.putExtra(Constants.PRODUCT_GENDER, product.getGender());
                intent.putExtra(Constants.PRODUCT_USAGE, product.getUsage());
                intent.putExtra("one_star", product.getOne_star());
                intent.putExtra("two_star", product.getTwo_star());
                intent.putExtra("three_star", product.getThree_star());
                intent.putExtra("four_star", product.getFour_star());
                intent.putExtra("five_star", product.getFive_star());
                intent.putExtra("rating", fiveStarRating.toString());
                intent.putExtra("responses", String.valueOf(total_responses.intValue()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }
}
