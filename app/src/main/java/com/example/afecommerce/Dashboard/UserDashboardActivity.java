package com.example.afecommerce.Dashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.afecommerce.Fragment.HomeFragment;
import com.example.afecommerce.Fragment.MyAccountFragment;
import com.example.afecommerce.Fragment.WishListFragment;
import com.example.afecommerce.Fragment.myOrderFragment;
import com.example.afecommerce.R;
import com.example.afecommerce.databinding.ActivityUserDashboardBinding;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class UserDashboardActivity extends AppCompatActivity {

    ActivityUserDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        View view = binding.getRoot();
        setContentView(view);
        //region Navigation Bar Setup
        binding.bottomNavMenu.setItemSelected(R.id.home, true);
        replaceFragment(new HomeFragment());
        //endregion
        bottomMenu();



    }

    private void bottomMenu() {
        binding.bottomNavMenu.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.trending:
                        fragment = new myOrderFragment();
                        break;
                    case R.id.profile:
                        fragment = new MyAccountFragment();
                        break;
                    case R.id.wish:
                        fragment = new WishListFragment();
                        break;
                }
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                }
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure You Want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserDashboardActivity.super.onBackPressed();
                        finishAndRemoveTask();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}