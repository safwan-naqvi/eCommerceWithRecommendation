package com.example.afecommerce.Adapter;

import static com.example.afecommerce.Products.CheckoutActivity.tax;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.afecommerce.Model.Product;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.MyCartItemLayoutBinding;
import com.example.afecommerce.databinding.QuantityDialogBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> products;
    CollectionReference item;
    TextView subTotal;
    Activity activity;

    public CartAdapter(Context context, Activity activity, ArrayList<Product> products, TextView subtotal) {
        this.context = context;
        this.products = products;
        this.activity = activity;
        item = FirebaseFirestore.getInstance().collection("CART")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("PRODUCTS");
        this.subTotal = subtotal;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.my_cart_item_layout, parent, false));
    }

    public static int qty = 1;

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Product product = products.get(position);
        qty = (product.getQuantity()).intValue();
        Glide.with(context)
                .load(product.getImageURL())
                .into(holder.binding.cartProductImage);
        holder.binding.cartProductTitle.setText(product.getProductTitle());
        holder.binding.cartProductPrice.setText("Rs. " + product.getPrice());
        holder.binding.cartProductQty.setText(qty + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                qty = Integer.parseInt(holder.binding.cartProductQty.getText().toString());
                QuantityDialogBinding quantityDialogBinding = QuantityDialogBinding.inflate(LayoutInflater.from(context));
                AlertDialog dialog = new AlertDialog.Builder(context).
                        setView(quantityDialogBinding.getRoot()).
                        setCancelable(false)
                        .create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
                quantityDialogBinding.productName.setText(product.getProductTitle());
                quantityDialogBinding.productStock.setText(String.valueOf(product.getPrice()));
                quantityDialogBinding.quantity.setText(qty + "");
                quantityDialogBinding.plusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String doc = String.valueOf(product.getProductId().intValue());
                        if (qty > 0) {
                            qty++;
                            item.document(doc).update("Qty", FieldValue.increment(1));
                        } else {
                            qty = 1;
                            item.document(doc).update("Qty", 1);
                        }
                        quantityDialogBinding.quantity.setText(qty + "");
                    }
                });
                quantityDialogBinding.minusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String doc = String.valueOf(product.getProductId().intValue());
                        if (qty > 1) {
                            qty--;
                            item.document(doc).update("Qty", FieldValue.increment(-1));
                        } else {
                            qty = 1;
                            item.document(doc).update("Qty", 1);
                        }
                        quantityDialogBinding.quantity.setText(qty + "");
                    }
                });
                quantityDialogBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.binding.cartProductQty.setText(qty + "");
                        updateCart();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        holder.binding.cartRemoveItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("CART")
                        .document(FirebaseAuth.getInstance().getUid())
                        .collection("PRODUCTS").document(String.valueOf(product.getProductId().intValue())).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                activity.startActivity(activity.getIntent());
                                activity.finish();
                                activity.overridePendingTransition(0, 0);
                            }
                        });
            }
        });
    }

    Double subtotal;

    private void updateCart() {
        subtotal = Double.valueOf(0);
        FirebaseFirestore.getInstance().collection("CART")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("PRODUCTS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getDouble("Price");
                                document.getDouble("Qty");
                                subtotal = subtotal + (document.getDouble("Price") * document.getDouble("Qty"));
                                subTotal.setText("PKR. " + subtotal);
                            }
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MyCartItemLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MyCartItemLayoutBinding.bind(itemView);

        }
    }
}
