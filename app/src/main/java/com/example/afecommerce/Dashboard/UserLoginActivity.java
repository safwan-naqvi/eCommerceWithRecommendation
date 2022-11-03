package com.example.afecommerce.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.afecommerce.R;
import com.example.afecommerce.databinding.ActivityUserLoginBinding;

public class UserLoginActivity extends AppCompatActivity {

    ActivityUserLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityUserLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setFragment(new LoginFragment());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}