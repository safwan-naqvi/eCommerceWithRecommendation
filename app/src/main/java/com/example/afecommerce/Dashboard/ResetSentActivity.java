package com.example.afecommerce.Dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.afecommerce.databinding.ActivityResetSentBinding;

public class ResetSentActivity extends AppCompatActivity {

    ActivityResetSentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetSentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ResetSentActivity.this, UserLoginActivity.class));
                finish();
            }
        }, 5000);
    }
}