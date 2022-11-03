package com.example.afecommerce.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.example.afecommerce.Adapter.ProductAdapter;
import com.example.afecommerce.HelperClasses.Constants;
import com.example.afecommerce.Model.Product;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.FragmentHomeBinding;
import com.example.afecommerce.databinding.FragmentWishListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WishListFragment extends Fragment {
    FragmentWishListBinding binding;
    ProductAdapter adapter;
    ArrayList<Product> products;
    FirebaseFirestore firebaseFirestore;

    public WishListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWishListBinding.inflate(inflater, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();

        initProduct();
        getSlider();
        adapter.notifyDataSetChanged();
        return binding.getRoot();
    }

    private void initProduct() {
        products = new ArrayList<>();
        adapter = new ProductAdapter(getContext(), products);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        binding.wishList.setLayoutManager(layoutManager);
        binding.wishList.setAdapter(adapter);

        //region Categories List
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("MY_WISHLIST").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                products.add(new Product((String)documentSnapshot.get("category"),
                                        (String)documentSnapshot.get("colour"),(String)documentSnapshot.get("gender"),(String)documentSnapshot.get("imageURL"),
                                        (String)documentSnapshot.get("productTitle"),(String)documentSnapshot.get("productType"),
                                        (String)documentSnapshot.get("subCategory"),(String)documentSnapshot.get("usage"),
                                        (String)documentSnapshot.get("dateAdded"),
                                        documentSnapshot.getDouble("productId"),
                                        documentSnapshot.getDouble("price"),
                                        documentSnapshot.getDouble("one_star"),
                                        documentSnapshot.getDouble("two_star"),
                                        documentSnapshot.getDouble("three_star"),
                                        documentSnapshot.getDouble("four_star"),
                                        documentSnapshot.getDouble("five_star")));
                            }
                            adapter.notifyDataSetChanged();
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

}