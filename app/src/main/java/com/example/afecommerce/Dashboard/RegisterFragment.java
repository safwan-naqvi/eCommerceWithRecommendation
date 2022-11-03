package com.example.afecommerce.Dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.afecommerce.R;
import com.example.afecommerce.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String _name, _email, _password, _cpassword;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        transitioning();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Start Text Change Listener
        binding.signupEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                callFieldValidations();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.signupPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                callFieldValidations();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.signupCpass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                callFieldValidations();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.signupPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                callFieldValidations();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Will send data to firebase database
                //Firstly We Will validate Fields
                checkEmailAndPassword();
            }
        });


        binding.alreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new LoginFragment());
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //replaceing fragments with transition
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        //Now we are accessing FrameLayout of Parent to be replaced from this fragment
        fragmentTransaction.replace(getActivity().findViewById(R.id.frameLayout).getId(), fragment);
        fragmentTransaction.commit();
    }

    private void callFieldValidations() {
        if (!TextUtils.isEmpty(binding.signupEmail.getEditText().getText().toString())) {
            if (!TextUtils.isEmpty(binding.signupUsername.getEditText().getText().toString())) {
                if (!TextUtils.isEmpty(binding.signupPass.getEditText().getText().toString()) && binding.signupPass.getEditText().getText().toString().length() >= 8) {
                    if (!TextUtils.isEmpty(binding.signupCpass.getEditText().getText().toString())) {
                        binding.signupUsername.setError("");
                        binding.signupEmail.setError("");
                        binding.signupPass.setError("");
                        binding.signupCpass.setError("");
                        binding.signupBtn.setEnabled(true);
                    } else {
                        binding.signupCpass.setError("Field cannot be empty");
                        binding.signupBtn.setEnabled(false);
                    }
                } else {
                    binding.signupPass.setError("Password Length Must be greater than 8");
                    binding.signupBtn.setEnabled(false);
                }
            } else {
                binding.signupUsername.setError("Must be filled");
                binding.signupBtn.setEnabled(false);
            }
        } else {
            binding.signupEmail.setError("Field must be filled");
            binding.signupBtn.setEnabled(false);
        }

    }


    private void checkEmailAndPassword() {

        binding.loading.setVisibility(View.VISIBLE);
        //region Fields Inputs
        _email = binding.signupEmail.getEditText().getText().toString();
        _name = binding.signupUsername.getEditText().getText().toString();
        _password = binding.signupPass.getEditText().getText().toString();
        _cpassword = binding.signupCpass.getEditText().getText().toString();
        //endregion

        if (Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            if (_cpassword.equals(_password)) {
                //Setting Signup Disable False
                binding.signupBtn.setEnabled(false);
                firebaseAuth.createUserWithEmailAndPassword(_email, _password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String,Object> userData = new HashMap<>();
                                    userData.put("fullName", _name);
                                    userData.put("email", _email);
                                    userData.put("password", _password);

                                    firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                                            .set(userData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Map<String, Object> list = new HashMap<>();
                                                        list.put("list_size", (long) 0);
                                                        FirebaseFirestore.getInstance().collection("USERS")
                                                                .document(FirebaseAuth.getInstance().getUid())
                                                                .collection("USER_DATA")
                                                                .document("MY_WISHLIST")
                                                                .set(list).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            startActivity(new Intent(getActivity(), UserDashboardActivity.class));
                                                                            getActivity().finish();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                } else {
                                    binding.signupBtn.setEnabled(true);
                                    binding.loading.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
            } else {
                binding.signupCpass.setError("Password Does not Matches");
                binding.loading.setVisibility(View.INVISIBLE);
            }
        } else {
            binding.signupEmail.setError("Not a valid Email");
            binding.loading.setVisibility(View.INVISIBLE);
        }
    }

    private void transitioning() {
        binding.signupEmail.setTranslationX(800);
        binding.signupCpass.setTranslationX(800);
        binding.signupPass.setTranslationX(800);
        binding.signupBtn.setTranslationX(800);
        binding.signupUsername.setTranslationX(800);


        binding.signupEmail.setAlpha(0);
        binding.signupCpass.setAlpha(0);
        binding.signupPass.setAlpha(0);
        binding.signupBtn.setAlpha(0);
        binding.signupUsername.setAlpha(0);

        binding.signupUsername.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.signupEmail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();
        binding.signupPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.signupCpass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600).start();
        binding.signupBtn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
    }

}