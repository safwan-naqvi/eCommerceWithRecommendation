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
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.afecommerce.R;
import com.example.afecommerce.databinding.FragmentLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    public static final String TAG = "LoginFragment";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 786;
    String _email, _password;

    public LoginFragment() {
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
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        transitioning();

        createRequestForLogin();

        //region Start Text Change Listener
        binding.loginEmail.getEditText().addTextChangedListener(new TextWatcher() {
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
        binding.loginPass.getEditText().addTextChangedListener(new TextWatcher() {
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

        binding.googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Clicked On Button");
                signIn();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.notUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new RegisterFragment());
            }
        });
        binding.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();
            }
        });

        //region Click on Forget Button
        binding.forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
                getActivity().finish();
            }
        });
        //endregion


    }

    private void createRequestForLogin() {
        // Configure Google Sign In and give a popup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("638804327358-adcpl8l0eki0qo8dck78bq9ndlu01mkm.apps.googleusercontent.com")
                .requestEmail()
                .build();
        //Building SignInClient with options specified by geo this will help us to
        //create request from app to google to access gmails.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //replaceing fragments with transition
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        //Now we are accessing FrameLayout of Parent to be replaced from this fragment
        fragmentTransaction.replace(getActivity().findViewById(R.id.frameLayout).getId(), fragment);
        fragmentTransaction.commit();
    }

    private void callFieldValidations() {
        if (!TextUtils.isEmpty(binding.loginEmail.getEditText().getText().toString())) {
            if (!TextUtils.isEmpty(binding.loginPass.getEditText().getText().toString())) {
                binding.loginEmail.setError("");
                binding.loginPass.setError("");
                binding.signinBtn.setEnabled(true);
            } else {
                binding.loginPass.setError("Field cannot be empty");
                binding.signinBtn.setEnabled(false);
            }
        } else {
            binding.loginEmail.setError("Field must be filled");
            binding.signinBtn.setEnabled(false);
        }
    }

    private void checkEmailAndPassword() {
        binding.loading.setVisibility(View.VISIBLE);

        //region Fields Inputs
        _email = binding.loginEmail.getEditText().getText().toString();
        _password = binding.loginPass.getEditText().getText().toString();
        //endregion

        if (Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            if (_password.length() >= 8) {
                //Setting Signup Disable False
                binding.signinBtn.setEnabled(false);
                firebaseAuth.signInWithEmailAndPassword(_email, _password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getActivity(), UserDashboardActivity.class));
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), "Account Details Not Correct!", Toast.LENGTH_SHORT).show();
                                    binding.signinBtn.setEnabled(true);
                                    binding.loading.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
            } else {
                binding.loginPass.setError("Issue in Email or Password");
                binding.loading.setVisibility(View.INVISIBLE);
            }
        } else {
            binding.loginEmail.setError("Issue in Email or Password");
            binding.loading.setVisibility(View.INVISIBLE);
        }
    }

    private void transitioning() {
        binding.loginEmail.setTranslationX(800);
        binding.loginPass.setTranslationX(800);
        binding.forgetPass.setTranslationX(800);
        binding.signinBtn.setTranslationX(800);
        binding.googleLogin.setTranslationX(800);

        binding.loginEmail.setAlpha(0);
        binding.loginPass.setAlpha(0);
        binding.forgetPass.setAlpha(0);
        binding.signinBtn.setAlpha(0);
        binding.googleLogin.setAlpha(0);

        binding.loginEmail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();
        binding.loginPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.forgetPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600).start();
        binding.signinBtn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        binding.googleLogin.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(800).start();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Logic
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(getActivity(), UserDashboardActivity.class);
                            getContext().startActivity(intent);
                            getActivity().finish();
                        }else{
                            Toast.makeText(getActivity(), "Issue", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}