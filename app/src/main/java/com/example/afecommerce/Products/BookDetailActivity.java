package com.example.afecommerce.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.afecommerce.Adapter.BookAdapter;
import com.example.afecommerce.Adapter.ProductAdapter;
import com.example.afecommerce.Model.BookModel;
import com.example.afecommerce.Model.Product;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.ActivityBookCategoryBinding;
import com.example.afecommerce.databinding.ActivityBookDetailBinding;
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

public class BookDetailActivity extends AppCompatActivity {

    ActivityBookDetailBinding binding;
    ArrayList<BookModel> recommend;
    String url = "http://192.168.100.110:5000/recommend_books";
    BookAdapter bookAdapter;

    String book,image,author,publisher,year,isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        book = getIntent().getStringExtra("book");
        image = getIntent().getStringExtra("image");
        author = getIntent().getStringExtra("author");
        publisher = getIntent().getStringExtra("publisher");
        year = getIntent().getStringExtra("year");
        isbn = getIntent().getStringExtra("isbn");


        binding.productCategory.setText("ISBN: "+isbn);
        binding.productTitle.setText(book + " - " + year);
        Glide.with(this)
                .load(image).into(binding.productImage);
        binding.productSubcategory.setText(author +" ("+publisher+")");
        binding.textView2.setText(isbn);

        getSupportActionBar().setTitle("Book Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recommend = new ArrayList<>();
        bookAdapter = new BookAdapter(this, recommend);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.relatedProductRv.setLayoutManager(layoutManager);
        binding.relatedProductRv.setAdapter(bookAdapter);

        getData();

    }

    private void getData() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(BookDetailActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    JSONArray jsonArray = respObj.getJSONArray("recommend_book");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject responseObj = jsonArray.getJSONObject(i);
                        String title = responseObj.getString("Book-Title");
                        String author = responseObj.getString("Book-Author");
                        String image = responseObj.getString("Image-URL-M");
                        String publisher = responseObj.getString("Publisher");
                        String year = responseObj.getString("Year");
                        String isbn = responseObj.getString("ISBN");

                        recommend.add(new BookModel(title, author, image, publisher, year, isbn));
                        buildRecyclerView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookDetailActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_input", book);
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void buildRecyclerView() {

        // initializing our adapter class.
        bookAdapter = new BookAdapter(this, recommend);

        // adding layout manager
        // to our recycler view.
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        binding.relatedProductRv.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        binding.relatedProductRv.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        binding.relatedProductRv.setAdapter(bookAdapter);
        bookAdapter.notifyDataSetChanged();
    }

}