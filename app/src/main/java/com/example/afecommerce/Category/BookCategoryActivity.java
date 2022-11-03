package com.example.afecommerce.Category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.afecommerce.Adapter.BookAdapter;
import com.example.afecommerce.Adapter.ProductAdapter;
import com.example.afecommerce.Dashboard.UserDashboardActivity;
import com.example.afecommerce.Model.BookModel;
import com.example.afecommerce.Model.Product;
import com.example.afecommerce.Products.ProductDetailsActivity;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.ActivityBookCategoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookCategoryActivity extends AppCompatActivity {

    ActivityBookCategoryBinding binding;
    BookAdapter productAdapter;
    ArrayList<BookModel> products;
    String url = "http://192.168.100.110:5000/book";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        products = new ArrayList<>();
        buildRecyclerView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(BookCategoryActivity.this, UserDashboardActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }

    private void getData() {
        binding.idPB.setVisibility(View.VISIBLE);
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(BookCategoryActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    JSONArray jsonArray = respObj.getJSONArray("popular_book");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject responseObj = jsonArray.getJSONObject(i);
                        String title = responseObj.getString("Book-Title");
                        String author = responseObj.getString("Book-Author");
                        String image = responseObj.getString("Image-URL-M");
                        String publisher = responseObj.getString("Publisher");
                        String year = responseObj.getString("Year");
                        String isbn = responseObj.getString("ISBN");

                        products.add(new BookModel(title, author, image, publisher, year, isbn));
                        buildRecyclerView();
                    }
                    binding.idPB.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.idPB.setVisibility(View.INVISIBLE);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.idPB.setVisibility(View.INVISIBLE);
                Toast.makeText(BookCategoryActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    private void buildRecyclerView() {

        // initializing our adapter class.
        productAdapter = new BookAdapter(this, products);

        // adding layout manager
        // to our recycler view.
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        binding.bookProducts.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        binding.bookProducts.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        binding.bookProducts.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

}