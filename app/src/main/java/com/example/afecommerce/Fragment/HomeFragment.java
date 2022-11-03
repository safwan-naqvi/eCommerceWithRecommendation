package com.example.afecommerce.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.afecommerce.Adapter.CategoryAdapter;
import com.example.afecommerce.Adapter.ProductAdapter;
import com.example.afecommerce.HelperClasses.Constants;
import com.example.afecommerce.Model.Category;
import com.example.afecommerce.Model.Product;
import com.example.afecommerce.Products.SearchActivity;
import com.example.afecommerce.R;

import com.example.afecommerce.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    CategoryAdapter adapter;
    ArrayList<Category> categories;
    FirebaseFirestore firebaseFirestore;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("query",text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        initCategory();
        initProduct();
        getSlider();
        return binding.getRoot();
    }

    private void initProduct() {
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), products);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        binding.recentProductsListRv.setLayoutManager(layoutManager);
        binding.recentProductsListRv.setAdapter(productAdapter);

        //region Categories List
        firebaseFirestore.collection("PRODUCTS").orderBy("DateAdded").limit(6).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.e("Home",documentSnapshot.toString());
                                products.add(new Product((String)documentSnapshot.get("Category"),
                                        (String)documentSnapshot.get("Colour"),(String)documentSnapshot.get("Gender"),(String)documentSnapshot.get("ImageURL"),
                                        (String)documentSnapshot.get("ProductTitle"),(String)documentSnapshot.get("ProductType"),
                                        (String)documentSnapshot.get("SubCategory"),(String)documentSnapshot.get("Usage"),
                                        (String)documentSnapshot.get("DateAdded"),
                                        documentSnapshot.getDouble("ProductId"),
                                        documentSnapshot.getDouble("Price"),
                                        (Double)documentSnapshot.getDouble("One_Star"),
                                        (Double)documentSnapshot.getDouble("Two_Star"),
                                        (Double)documentSnapshot.getDouble("Three_Star"),
                                        (Double) documentSnapshot.getDouble("Four_Star"),
                                        (Double)documentSnapshot.getDouble("Five_Star")));
                            }
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //endregion

    }

    private void getSlider() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject mainObject = new JSONObject(response);
                            if(mainObject.getString("status").equals("success")){
                                JSONArray sliderArray = mainObject.getJSONArray("news_infos");
                                for(int i = 0;i<sliderArray.length();i++){
                                    JSONObject object = sliderArray.getJSONObject(i);
                                    binding.carousel.addData(new CarouselItem(Constants.NEWS_IMAGE_URL+object.get("image"), object.getString("brief_content")));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    private void initCategory() {
        categories = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        binding.categoryListRv.setLayoutManager(layoutManager);

        RequestQueue queueTwo = Volley.newRequestQueue(getContext());
        StringRequest stringRequestTwo = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject mainObject = new JSONObject(response);
                            if(mainObject.getString("status").equals("success")){
                                JSONArray categoryArray = mainObject.getJSONArray("categories");
                                for(int i = 0;i<categoryArray.length();i++){
                                    JSONObject object = categoryArray.getJSONObject(i);
                                    categories.add(new Category(object.getString("name"), Constants.CATEGORIES_IMAGE_URL+object.get("icon"), object.getString("color"), object.getString("brief"), object.getInt("id")));
                                }
                                adapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(getContext(), "UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Home",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queueTwo.add(stringRequestTwo);
        adapter = new CategoryAdapter(getContext(), categories);
        binding.categoryListRv.setAdapter(adapter);

    }

}