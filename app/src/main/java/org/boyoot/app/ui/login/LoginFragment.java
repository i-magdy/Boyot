package org.boyoot.app.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityLoginBinding;
import org.boyoot.app.databinding.FragmentLoginBinding;
import org.boyoot.app.ui.user.UserActivity;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private static String SIGN_IN_TAG = "SignTag";
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,container,false);

       binding.loginEmail.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   hideErrorMessage(binding.textInputLayout, null);
               }else{
                   binding.loginEmail.setError(null);
               }

           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

       binding.loginPassword.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   hideErrorMessage(binding.textInputLayout2, null);
               }else{
                   binding.loginPassword.setError(null);
               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
       binding.switchToSinUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Objects.requireNonNull(getActivity()).findViewById(R.id.login_fragment).setVisibility(View.GONE);
               Objects.requireNonNull(getActivity()).findViewById(R.id.sign_up_fragment).setVisibility(View.VISIBLE);

           }
       });
       binding.loginFragmentButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               attemptLogin();
           }
       });

       binding.resetPasswordTv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getContext(),ResetPasswordActivity.class));
           }
       });
       return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }

    private void attemptLogin(){
        binding.loginErrorTextView.setVisibility(View.INVISIBLE);
        binding.signInProgressBar.setVisibility(View.VISIBLE);
        String email = binding.loginEmail.getEditableText().toString();
        String password = binding.loginPassword.getEditableText().toString();
        if (email.isEmpty()){
            binding.signInProgressBar.setVisibility(View.INVISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showErrorMessage(binding.textInputLayout,getString(R.string.empty_message));
            }else{
                binding.loginEmail.setError(getString(R.string.empty_message));
            }
        }else {
            if (!isEmailValid(email)) {
                binding.signInProgressBar.setVisibility(View.INVISIBLE);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showErrorMessage(binding.textInputLayout, getString(R.string.email_message));
                } else {
                    binding.loginEmail.setError(getString(R.string.email_message));
                }
            }
        }


        if (password.isEmpty()){
            binding.signInProgressBar.setVisibility(View.INVISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showErrorMessage(binding.textInputLayout2,getString(R.string.empty_message));
            }else{
                binding.loginPassword.setError(getString(R.string.empty_message));
            }
        }else if (password.length() < 8){
            binding.signInProgressBar.setVisibility(View.INVISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showErrorMessage(binding.textInputLayout2,getString(R.string.password_message));
            }else{
                binding.loginPassword.setError(getString(R.string.password_message));
            }
        }


        if (!TextUtils.isEmpty(email) && isEmailValid(email)&& password.length() >= 8){
            login(email,password);
        }

    }


    private void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()){
                       Log.i(SIGN_IN_TAG,"signed in");
                       FirebaseUser user = mAuth.getCurrentUser();
                       if (user != null){
                           startActivity(new Intent(getContext(), UserActivity.class));
                           Objects.requireNonNull(getActivity()).finish();
                       }
                   }else{
                       binding.signInProgressBar.setVisibility(View.INVISIBLE);
                       binding.loginErrorTextView.setVisibility(View.VISIBLE);

                   }
               }
           });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hideErrorMessage(TextInputLayout layout, String message){
        layout.setBoxStrokeColor(getActivity().getColor(R.color.colorAccent));
        layout.setDefaultHintTextColor(getActivity().getColorStateList(R.color.colorAccent));
        layout.setHelperTextColor(getActivity().getColorStateList(R.color.colorBackGround));
        layout.setHelperText(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showErrorMessage(TextInputLayout layout, String message){
        layout.setBoxStrokeColor(Color.RED);
        layout.setDefaultHintTextColor(getActivity().getColorStateList(R.color.colorSecondary));
        layout.setHelperText(message);
        layout.setHelperTextColor(getActivity().getColorStateList(R.color.colorSecondary));

    }

    private boolean isEmailValid(String s){
        return s.contains("@") && s.contains(".com");
    }

}
