package com.example.afecommerce.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.afecommerce.Adapter.ProductAdapter;
import com.example.afecommerce.HelperClasses.Constants;
import com.example.afecommerce.HelperClasses.DBQueries;
import com.example.afecommerce.Model.Product;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.ActivityProductDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProductDetailsActivity extends AppCompatActivity {

    ActivityProductDetailsBinding binding;
    int id;
    Double pricex, one, two, three, four, five;
    String name, image, category, sub_category, type, color, usage, gender, date, responses, rating;
    ArrayList<Product> recommend;
    String url = "http://192.168.100.110:5000/test";
    ProductAdapter productAdapter;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    String temp;
    String old_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        id = (int) getIntent().getDoubleExtra(Constants.PRODUCT_ID, 0);
        name = getIntent().getStringExtra(Constants.PRODUCT_TITLE);
        image = getIntent().getStringExtra("url");
        category = getIntent().getStringExtra(Constants.PRODUCT_CATEGORY);
        sub_category = getIntent().getStringExtra(Constants.PRODUCT_SUBCATEGORY);
        type = getIntent().getStringExtra(Constants.PRODUCT_TYPE);
        color = getIntent().getStringExtra(Constants.PRODUCT_COLOR);
        usage = getIntent().getStringExtra(Constants.PRODUCT_USAGE);
        gender = getIntent().getStringExtra(Constants.PRODUCT_GENDER);
        date = getIntent().getStringExtra(Constants.PRODUCT_DATE_ADDED);
        pricex = getIntent().getDoubleExtra("price", 0);
        one = getIntent().getDoubleExtra("one_star", 0);
        two = getIntent().getDoubleExtra("two_star", 0);
        three = getIntent().getDoubleExtra("three_star", 0);
        four = getIntent().getDoubleExtra("four_star", 0);
        five = getIntent().getDoubleExtra("five_star", 0);
        responses = getIntent().getStringExtra("responses");
        rating = getIntent().getStringExtra("rating");

        //region Utility
        String price = "Rs " + pricex;
        getSupportActionBar().setTitle("AI eCommerce");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.productTitle.setText(name);
        Glide.with(this)
                .load(image)
                .into(binding.productImage);
        binding.productSubcategory.setText(sub_category);
        binding.productDetailsPrice.setText(price);
        binding.productCategory.setText(category);
        binding.productDetailsRatingMiniview.setText(rating);
        binding.responses.setText("(" + responses + ") Ratings");
        binding.buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeItemInFirestoreCart();
                binding.buyNowBtn.setEnabled(false);
                binding.buyNowBtn.setText("Added to Cart");
            }
        });
        recommend = new ArrayList<>();
        productAdapter = new ProductAdapter(this, recommend);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.relatedProductRv.setLayoutManager(layoutManager);
        binding.relatedProductRv.setAdapter(productAdapter);
        getData();


        DBQueries.loadWish(id, binding.addToWishlistBtn);
        binding.addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = new Product(category, color, gender, image, name, type, sub_category, usage, date, (double) id, pricex, one, two, three, four, five);
                if (ALREADY_ADDED_TO_WISHLIST) {
                    DBQueries.removeWish(product, binding.addToWishlistBtn);
                } else {
                    DBQueries.addToWish(product, binding.addToWishlistBtn);
                }

            }
        });

        //region Load User Rating on Particular Item

        binding.rateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("USERS")
                        .document(FirebaseAuth.getInstance().getUid()).
                        collection("MY_RATINGS").document(String.valueOf(id)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    old_rating = "Five_Star";
                                    if (task.getResult() != null) {
                                        old_rating = task.getResult().getString("Your_Rating");
                                    }
                                    Map<String, Object> r = new HashMap<>();
                                    temp = "";
                                    if (binding.ratingTwo.getRating() > 0 && binding.ratingTwo.getRating() <= 1) {
                                        temp = "One_Star";
                                    } else if (binding.ratingTwo.getRating() > 1 && binding.ratingTwo.getRating() <= 2) {
                                        temp = "Two_Star";
                                    } else if (binding.ratingTwo.getRating() > 2 && binding.ratingTwo.getRating() <= 3) {
                                        temp = "Three_Star";
                                    } else if (binding.ratingTwo.getRating() > 3 && binding.ratingTwo.getRating() <= 4) {
                                        temp = "Four_Star";
                                    } else {
                                        temp = "Five_Star";
                                    }
                                    r.put("Your_Rating", temp);

                                    FirebaseFirestore.getInstance().collection("USERS")
                                            .document(FirebaseAuth.getInstance().getUid()).
                                            collection("MY_RATINGS").document(String.valueOf(id)).set(r)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task2) {
                                                    if (task2.isSuccessful()) {
                                                        Map<String, Object> updated_rating = new HashMap<>();
                                                        updated_rating.put(old_rating, FieldValue.increment(-1));
                                                        updated_rating.put(temp, FieldValue.increment(1));
                                                        FirebaseFirestore.getInstance().collection("PRODUCTS")
                                                                .whereEqualTo("ProductId", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                                            FirebaseFirestore.getInstance().collection("PRODUCTS")
                                                                                    .document(snapshot.getId()).set(updated_rating, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            Toast.makeText(ProductDetailsActivity.this, "New Rating Updated", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });
        //endregion

    }

    private void getData() {
//         creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(ProductDetailsActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    JSONArray jsonArray = respObj.getJSONArray("recommend");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject responseObj = jsonArray.getJSONObject(i);
                        String productId = responseObj.getString("id");
                        FirebaseFirestore.getInstance().collection("PRODUCTS")
                                .whereEqualTo("ProductId", Integer.parseInt(productId)).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                recommend.add(new Product((String) documentSnapshot.get("Category"),
                                                        (String) documentSnapshot.get("Colour"), (String) documentSnapshot.get("Gender"), (String) documentSnapshot.get("ImageURL"),
                                                        (String) documentSnapshot.get("ProductTitle"), (String) documentSnapshot.get("ProductType"),
                                                        (String) documentSnapshot.get("SubCategory"), (String) documentSnapshot.get("Usage"),
                                                        (String) documentSnapshot.get("DateAdded"),
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
                                            Toast.makeText(ProductDetailsActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductDetailsActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("value", image);
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void storeItemInFirestoreCart() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Map<String, Object> docData = new HashMap<>();
        docData.put("ProductId", id);
        docData.put("ProductTitle", name);
        docData.put("ProductType", type);
        docData.put("Category", category);
        docData.put("SubCategory", sub_category);
        docData.put("ImageURL", image);
        docData.put("Usage", usage);
        docData.put("Price", pricex);
        docData.put("Colour", color);
        docData.put("Gender", gender);
        docData.put("DateAdded", date);
        docData.put("Qty", 1);
        FirebaseFirestore.getInstance().collection("CART").document(auth.getUid()).collection("PRODUCTS").document(String.valueOf(id)).set(docData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart) {
            startActivity(new Intent(this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}