package com.example.afecommerce.HelperClasses;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.afecommerce.Model.Product;
import com.example.afecommerce.Products.ProductDetailsActivity;
import com.example.afecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBQueries {

    public static List<String> wishlist = new ArrayList<>();
    public static List<String> wish = new ArrayList<>();

    public static void loadWish(int id, ImageView addToWishlistBtn) {
        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("MY_WISHLIST").whereEqualTo("productId", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().size() > 0) {
                            for (long x = 0; x < task.getResult().size(); x++) {
                                wish.add(task.getResult().toString());
                            }
                            addToWishlistBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ecb365")));
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                            Log.e("DB", "Product Found " + wish);
                        } else {
                            addToWishlistBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#eeeeee")));
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                            Log.e("DB", "Product Not Found " + id);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("DB", e.getMessage());
                    }
                });
    }

    public static void addToWish(Product product, ImageView addToWishlistBtn) {
        int id = product.getProductId().intValue();
        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("MY_WISHLIST").document(String.valueOf(id))
                .set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addToWishlistBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ecb365")));
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                        } else {
                            addToWishlistBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#eeeeee")));
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                        }
                    }
                });

    }

    public static void removeWish(Product product, ImageView addToWishlistBtn) {
        int id = product.getProductId().intValue();
        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("MY_WISHLIST").document(String.valueOf(id))
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addToWishlistBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#eeeeee")));
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                        } else {
                            addToWishlistBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ecb365")));
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                        }
                    }
                });
    }

}
