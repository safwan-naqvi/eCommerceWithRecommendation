package com.example.afecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.afecommerce.Dashboard.UserDashboardActivity;
import com.example.afecommerce.Dashboard.UserLoginActivity;
import com.example.afecommerce.databinding.ActivityIntroBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ncorti.slidetoact.SlideToActView;

public class IntroActivity extends AppCompatActivity {

    ActivityIntroBinding binding;
    FirebaseAuth firebaseAuth;
    boolean flagToGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        firebaseAuth = FirebaseAuth.getInstance();
        binding.sliderMain.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                Intent intent;
                if (flagToGo) {
                    intent = new Intent(IntroActivity.this, UserDashboardActivity.class);
                }else{
                    intent = new Intent(IntroActivity.this, UserLoginActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in_short,R.anim.fade_out_short);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            flagToGo = false;
        } else {
            flagToGo = true;
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}