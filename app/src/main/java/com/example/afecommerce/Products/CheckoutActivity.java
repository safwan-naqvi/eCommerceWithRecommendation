package com.example.afecommerce.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.afecommerce.Adapter.CartAdapter;
import com.example.afecommerce.HelperClasses.Constants;
import com.example.afecommerce.HelperClasses.PreferenceManager;
import com.example.afecommerce.Model.Product;
import com.example.afecommerce.databinding.ActivityCheckoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;
    public static Double subtotal = Double.valueOf(0);
    Double total = Double.valueOf(0);
    public static final int tax = 11;
    ProgressDialog dialog;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        preferenceManager = new PreferenceManager(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Processing...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                                total = (subtotal * tax / 100) + subtotal;
                                binding.total.setText(String.format("PKR. %.2f", total));
                                preferenceManager.putString("total", total.toString());
                            }

                        }
                    }
                });

        adapter = new CartAdapter(this, this, products, binding.subtotal);
        binding.cartList.setLayoutManager(new LinearLayoutManager(this));
        binding.cartList.setAdapter(adapter);

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkoutItem();
            }
        });
    }

    JSONArray product_order_detail;

    private void checkoutItem() {
        dialog.show();
        total = Double.parseDouble(preferenceManager.getString("total"));

        FirebaseFirestore.getInstance().collection("CART")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("PRODUCTS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        product_order_detail = new JSONArray();
                        if (task.isSuccessful()) {

                            //region Making JSON
                            RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);
                            JSONObject productOrder = new JSONObject();
                            JSONObject dataObject = new JSONObject();
                            try {
                                productOrder.put("address", binding.addressBox.getText().toString());
                                productOrder.put("buyer", binding.nameBox.getText().toString());
                                productOrder.put("comment", binding.commentBox.getText().toString());
                                productOrder.put("created_at", Calendar.getInstance().getTimeInMillis());
                                productOrder.put("last_update", Calendar.getInstance().getTimeInMillis());
                                productOrder.put("date_ship", Calendar.getInstance().getTimeInMillis());
                                productOrder.put("email", binding.emailBox.getText().toString());
                                productOrder.put("phone", binding.phoneBox.getText().toString());
                                productOrder.put("serial", "cab8c1a4e4421a3b");
                                productOrder.put("shipping", "");
                                productOrder.put("shipping_location", "");
                                productOrder.put("shipping_rate", "0.0");
                                productOrder.put("status", "WAITING");
                                productOrder.put("tax", tax);
                                productOrder.put("total_fees", total);
                                dataObject.put("product_order", productOrder);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //endregion
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                JSONObject productObj = new JSONObject();
                                try {
                                    productObj.put("amount", document.getDouble("Qty"));
                                    productObj.put("price_item", document.getDouble("Price"));
                                    productObj.put("product_id", document.getDouble("ProductId"));
                                    productObj.put("product_name", (String) document.get("ProductTitle"));
                                    product_order_detail.put(productObj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                dataObject.put("product_order_detail", product_order_detail);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("err", dataObject.toString());
                            //region JSON Request
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, dataObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("success")) {
                                            Toast.makeText(CheckoutActivity.this, "Success order.", Toast.LENGTH_SHORT).show();
                                            String orderNumber = response.getJSONObject("data").getString("code");
                                            new AlertDialog.Builder(CheckoutActivity.this)
                                                    .setTitle("Order Successful")
                                                    .setCancelable(false)
                                                    .setMessage("Your order number is: " + orderNumber)
                                                    .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                            storeCartInfoInDatabase(orderNumber, total);

                                                            Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
                                                            intent.putExtra("orderCode", orderNumber);
                                                            startActivity(intent);
                                                        }
                                                    }).show();
                                        } else {
                                            new AlertDialog.Builder(CheckoutActivity.this)
                                                    .setTitle("Order Failed")
                                                    .setMessage("Something went wrong, please try again.")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }
                                                    }).show();
                                            Toast.makeText(CheckoutActivity.this, "Failed order.", Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss();
                                        Log.e("res", response.toString());
                                    } catch (Exception e) {
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> headers = new HashMap<>();
                                    headers.put("Security", "secure_code");
                                    return headers;
                                }
                            };
                            //endregion
                            queue.add(request);
                        }
                    }
                });

    }

    private void storeCartInfoInDatabase(String orderNumber, Double total) {
        Map<String, Object> order = new HashMap<>();
        order.put("orderNumber", orderNumber);
        order.put("totalAmount", total.toString());
        order.put("order_packed", "0");
        order.put("packed_shipped", "0");
        order.put("shipped_deliver", "0");
        FirebaseFirestore.getInstance().collection("CART").document(FirebaseAuth.getInstance().getUid())
                .set(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("CheckOut", "Successfully");
                        } else {
                            Log.e("CheckOut", task.getException().getMessage());
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(CheckoutActivity.this, CartActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(CheckoutActivity.this, CartActivity.class));
        finish();
    }
}