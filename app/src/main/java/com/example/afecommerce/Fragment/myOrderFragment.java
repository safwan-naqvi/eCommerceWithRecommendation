package com.example.afecommerce.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.afecommerce.Adapter.ProductAdapter;
import com.example.afecommerce.Adapter.TrendingAdapter;
import com.example.afecommerce.Model.Product;

import com.example.afecommerce.databinding.FragmentMyOrderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class myOrderFragment extends Fragment {

    FragmentMyOrderBinding binding;
    TrendingAdapter adapter;
    ArrayList<Product> products;
    FirebaseFirestore firebaseFirestore;

    public myOrderFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMyOrderBinding.inflate(inflater, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        initProduct();
        adapter.notifyDataSetChanged();
        return binding.getRoot();
    }

    private void initProduct() {
        products = new ArrayList<>();
        adapter = new TrendingAdapter(getContext(), products);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        binding.trendingRecycler.setLayoutManager(layoutManager);
        binding.trendingRecycler.setAdapter(adapter);

        //region Categories List
        firebaseFirestore.collection("PRODUCTS").orderBy("DateAdded")
                .orderBy("Five_Star", Query.Direction.DESCENDING)
                .orderBy("Four_Star", Query.Direction.DESCENDING).limit(12)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.e("Home", documentSnapshot.toString());
                                products.add(new Product((String) documentSnapshot.get("Category"),
                                        (String) documentSnapshot.get("Colour"), (String) documentSnapshot.get("Gender"), (String) documentSnapshot.get("ImageURL"),
                                        (String) documentSnapshot.get("ProductTitle"), (String) documentSnapshot.get("ProductType"),
                                        (String) documentSnapshot.get("SubCategory"), (String) documentSnapshot.get("Usage"),
                                        (String) documentSnapshot.get("DateAdded"),
                                        documentSnapshot.getDouble("ProductId"),
                                        documentSnapshot.getDouble("Price"),
                                        (Double) documentSnapshot.getDouble("One_Star"),
                                        (Double) documentSnapshot.getDouble("Two_Star"),
                                        (Double) documentSnapshot.getDouble("Three_Star"),
                                        (Double) documentSnapshot.getDouble("Four_Star"),
                                        (Double) documentSnapshot.getDouble("Five_Star")));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("err","Error " + task.getException().getMessage());
                        }
                    }
                });
        //endregion

    }



}