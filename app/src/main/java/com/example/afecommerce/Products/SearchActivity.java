package com.example.afecommerce.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.afecommerce.Adapter.ProductAdapter;
import com.example.afecommerce.Category.CategoryActivity;
import com.example.afecommerce.Dashboard.UserDashboardActivity;
import com.example.afecommerce.Model.Product;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.ActivitySearchBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);

        String query = getIntent().getStringExtra("query");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getProducts(query);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }

    void getProducts(String query) {
        String str = query;
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        FirebaseFirestore.getInstance().collection("PRODUCTS")
                .whereGreaterThanOrEqualTo("ProductTitle",cap)
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
                            Toast.makeText(SearchActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(SearchActivity.this, UserDashboardActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }
}