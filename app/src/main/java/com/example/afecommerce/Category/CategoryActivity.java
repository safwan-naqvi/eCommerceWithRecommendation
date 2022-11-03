package com.example.afecommerce.Category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.afecommerce.Adapter.ProductAdapter;
import com.example.afecommerce.Dashboard.UserDashboardActivity;
import com.example.afecommerce.Model.Product;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.ActivityCategoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);

        String categoryName = getIntent().getStringExtra("CategoryName").trim();
        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.categoryProducts.setAdapter(productAdapter);
        binding.categoryProducts.setLayoutManager(layoutManager);

        FirebaseFirestore.getInstance().collection("PRODUCTS").whereEqualTo("SubCategory",categoryName)
                .limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                products.add(new Product((String)documentSnapshot.get("Category"),
                                        (String)documentSnapshot.get("Colour"),(String)documentSnapshot.get("Gender"),(String)documentSnapshot.get("ImageURL"),
                                        (String)documentSnapshot.get("ProductTitle"),(String)documentSnapshot.get("ProductType"),
                                        (String)documentSnapshot.get("SubCategory"),(String)documentSnapshot.get("Usage"),
                                        (String)documentSnapshot.get("DateAdded"),
                                        documentSnapshot.getDouble("ProductId"),
                                        documentSnapshot.getDouble("Price"),
                                        documentSnapshot.getDouble("One_Star"),
                                        documentSnapshot.getDouble("Two_Star"),
                                        documentSnapshot.getDouble("Three_Star"),
                                        documentSnapshot.getDouble("Four_Star"),
                                        documentSnapshot.getDouble("Five_Star")));
                            }
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CategoryActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(CategoryActivity.this, UserDashboardActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }
}