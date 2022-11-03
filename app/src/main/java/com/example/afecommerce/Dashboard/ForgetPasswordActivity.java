package com.example.afecommerce.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.afecommerce.R;
import com.example.afecommerce.databinding.ActivityForgetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    ActivityForgetPasswordBinding binding;
    FirebaseAuth firebaseAuth;
    public static boolean onResetPassword = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();

        //region Check If Email is written on note listener
        binding.forgetEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkIfDataIsEntered();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion
        //region Check On Reset Button
        binding.resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Reverting Button to
                binding.resetPassBtn.setEnabled(false);
                checkIfEmailValid();
            }
        });
        //endregion
    }

    private void checkIfEmailValid() {
        String _email = binding.forgetEmail.getEditText().getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            //Clearing Field
            binding.forgetEmail.getEditText().setText("");
            //regionSending Email Using Firebase Function
            firebaseAuth.sendPasswordResetEmail(_email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(ForgetPasswordActivity.this, ResetSentActivity.class));
                                finish();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "Failed Due to " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                            //Enabling Reset button
                            binding.resetPassBtn.setEnabled(true);

                        }
                    });
            //endregion
        } else {
            binding.forgetEmail.setError("Email is not Valid");
        }
    }

    private void checkIfDataIsEntered() {
        if (!TextUtils.isEmpty(binding.forgetEmail.getEditText().getText().toString())) {
            binding.resetPassBtn.setEnabled(true);
        } else {
            binding.resetPassBtn.setEnabled(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(ForgetPasswordActivity.this,UserLoginActivity.class));
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}