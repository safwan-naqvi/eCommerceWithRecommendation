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
import com.example.afecommerce.Category.CategoryActivity;
import com.example.afecommerce.Model.BookModel;
import com.example.afecommerce.Model.Category;
import com.example.afecommerce.Products.BookDetailActivity;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.BookItemProductBinding;
import com.example.afecommerce.databinding.ItemCategoriesBinding;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    Context context;
    ArrayList<BookModel> model;

    public BookAdapter(Context context, ArrayList<BookModel> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.book_item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        BookModel book = model.get(position);
        holder.binding.bookName.setText(book.getTitle());
        Glide.with(context)
                .load(book.getImage())
                .into(holder.binding.productItemImg);
        holder.binding.author.setText(book.getAuthor());
        holder.binding.publisher.setText(book.getPublisher());
        holder.binding.yearOfPublication.setText(book.getYear());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("book",book.getTitle());
                intent.putExtra("author",book.getAuthor());
                intent.putExtra("image",book.getImage());
                intent.putExtra("year",book.getYear());
                intent.putExtra("isbn",book.getIsbn());
                intent.putExtra("publisher",book.getPublisher());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        BookItemProductBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = BookItemProductBinding.bind(itemView);
        }
    }
}
