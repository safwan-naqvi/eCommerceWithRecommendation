package com.example.afecommerce.Fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.parser.ColorParser;
import com.example.afecommerce.Dashboard.UserLoginActivity;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.FragmentMyAccountBinding;
import com.example.afecommerce.databinding.FragmentWishListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MyAccountFragment extends Fragment {

    FragmentMyAccountBinding binding;
    FirebaseFirestore firebaseFirestore;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    String orderNumber, orderPacked, packedShip, delivered;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyAccountBinding.inflate(inflater, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.profileSetting.setRenderEffect(RenderEffect.createBlurEffect(25, 25, Shader.TileMode.CLAMP));
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final DocumentReference docRef = firebaseFirestore.collection("CART").document(firebaseAuth.getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    loadProgress();
                    Log.d("TAG", "Current data: " + snapshot.getData());
                } else {
                    loadProgress();
                    Log.d("TAG", "Current data: null");
                }
            }
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return binding.getRoot();
    }

    private void loadProgress() {
        firebaseFirestore.collection("CART").document(firebaseAuth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            orderNumber = task.getResult().getString("orderNumber");
                            orderPacked = task.getResult().getString("order_packed");
                            packedShip = task.getResult().getString("packed_shipped");
                            delivered = task.getResult().getString("shipped_deliver");
                            binding.currentOrderNumber.setText(orderNumber);
                            if (Integer.parseInt(orderPacked) > 0) {
                                binding.currentOrderStatus.setText("Out For Delivery");
                                binding.orderedIndicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#81C784")));
                                binding.orderedPackedProgress.setProgress(Integer.parseInt(orderPacked));
                            }
                            if (Integer.parseInt(packedShip) > 0) {
                                binding.currentOrderStatus.setText("Package Shipping");
                                binding.orderedIndicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#81C784")));
                                binding.orderedPackedProgress.setProgress(100);
                                binding.packedShippedProgress.setProgress(Integer.parseInt(packedShip));
                                binding.packedIndicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#81C784")));
                            }
                            if (Integer.parseInt(delivered) > 0) {
                                binding.orderedIndicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#81C784")));
                                binding.orderedPackedProgress.setProgress(100);
                                binding.packedIndicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#81C784")));
                                binding.packedShippedProgress.setProgress(100);
                                binding.shippedIndicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#81C784")));
                                binding.shippedDeliveredProgress.setProgress(Integer.parseInt(delivered));
                                if (Integer.parseInt(delivered) >= 100) {
                                    binding.deliveredIndicator.setImageTintList(ColorStateList.valueOf(Color.parseColor("#81C784")));
                                    binding.currentOrderStatus.setText("Delivered");
                                }
                            }
                        }
                    }
                });

        binding.signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UserLoginActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in_short, R.anim.fade_out_short);
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });
    }
}