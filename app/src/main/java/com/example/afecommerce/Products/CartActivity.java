package com.example.afecommerce.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.afecommerce.Adapter.CartAdapter;
import com.example.afecommerce.Model.Product;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.ActivityCartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;
    Double subtotal = Double.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        products = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("CART")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("PRODUCTS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                products.add(new Product(
                                        (String) document.get("Category"),
                                        (String) document.get("Colour"),
                                        (String) document.get("Gender"),
                                        (String) document.get("ImageURL"),
                                        (String) document.get("ProductTitle"),
                                        (String) document.get("ProductType"),
                                        (String) document.get("SubCategory"),
                                        (String) document.get("Usage"),
                                        (String) document.get("DateAdded"),
                                        document.getDouble("ProductId"),
                                        document.getDouble("Price"),
                                        document.getDouble("Qty")
                                ));

                                subtotal = subtotal + (document.getDouble("Price") * document.getDouble("Qty"));
                                adapter.notifyDataSetChanged();
                                binding.subtotal.setText("PKR. " + subtotal);

                            }
                        }
                    }
                });

        adapter = new CartAdapter(this, this, products, binding.subtotal);
        binding.cartRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.cartRecycler.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.continueShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}