package com.example.afecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.afecommerce.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(view);

        Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        Animation fade_in_long = AnimationUtils.loadAnimation(this,R.anim.fade_in_long);

        binding.splashTitle.setAnimation(fade_in);
        binding.splashDesc.setAnimation(fade_in_long);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, IntroActivity.class));
                finish();
            }
        }, 3500);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}