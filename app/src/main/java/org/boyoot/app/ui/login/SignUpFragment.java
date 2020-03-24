package org.boyoot.app.ui.login;

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
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.boyoot.app.R;
import org.boyoot.app.databinding.FragmentSignUpBinding;
import org.boyoot.app.model.UserProfileModel;
import org.boyoot.app.ui.user.UserActivity;

import java.util.Objects;

public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private UserProfileViewModel viewModel;
    private FirebaseAuth mAuth;
    private static String SIGN_IN_TAG = "SignTag";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up,container,false);

        binding.signUpFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });


        binding.switchToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).findViewById(R.id.sign_up_fragment).setVisibility(View.GONE);
                Objects.requireNonNull(getActivity()).findViewById(R.id.login_fragment).setVisibility(View.VISIBLE);

            }
        });
        binding.userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hideErrorMessage(binding.nameInputLayout, null);
                }else{
                    binding.userName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hideErrorMessage(binding.emailInputLayout, null);
                }else{
                    binding.email.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hideErrorMessage(binding.phoneInputLayout, null);
                }else{
                    binding.phone.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hideErrorMessage(binding.passwordInputLayout, null);
                }else{
                    binding.password.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hideErrorMessage(binding.confirmPasswordInputLayout, null);
                }else{
                    binding.confirmPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }


    private void attemptSignUp(){
        binding.signUpProgressBar.setVisibility(View.VISIBLE);
        String userName = binding.userName.getEditableText().toString();
        String email = binding.email.getEditableText().toString();
        String phone = binding.phone.getEditableText().toString();
        String password = binding.password.getEditableText().toString();
        String confirmPass = binding.confirmPassword.getEditableText().toString();

        if (userName.isEmpty()){
            binding.signUpProgressBar.setVisibility(View.INVISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showErrorMessage(binding.nameInputLayout,getString(R.string.empty_message));
            }else{
                binding.userName.setError(getString(R.string.empty_message));
            }
        }
        if(TextUtils.isEmpty(email)){
            binding.signUpProgressBar.setVisibility(View.INVISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               showErrorMessage(binding.emailInputLayout,getString(R.string.empty_message));
            }else{
                binding.email.setError(getString(R.string.empty_message));
            }
        }
        if (phone.isEmpty()){
            binding.signUpProgressBar.setVisibility(View.INVISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showErrorMessage(binding.phoneInputLayout,getString(R.string.empty_message));
            }else{
                binding.phone.setError(getString(R.string.empty_message));
            }
        }

        if (!isPasswordValid(password,confirmPass)){
            binding.signUpProgressBar.setVisibility(View.INVISIBLE);
            if(TextUtils.isEmpty(password)){
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showErrorMessage(binding.passwordInputLayout,getString(R.string.empty_message));
                }else{
                    binding.password.setError(getString(R.string.empty_message));
                }
            }else if(TextUtils.isEmpty(confirmPass)){
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showErrorMessage(binding.confirmPasswordInputLayout,getString(R.string.empty_message));
                }else{
                    binding.confirmPassword.setError(getString(R.string.empty_message));
                }
            }else if (password.length() < 8){
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showErrorMessage(binding.passwordInputLayout,getString(R.string.password_message));
                }else{
                    binding.password.setError(getString(R.string.password_message));
                }
            }else{
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showErrorMessage(binding.passwordInputLayout,getString(R.string.not_matching_pass));
                }else{
                    binding.password.setError(getString(R.string.not_matching_pass));
                }

            }
        }

        if (!email.isEmpty()){
            binding.signUpProgressBar.setVisibility(View.INVISIBLE);
            if (!isEmailValid(email)){
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showErrorMessage(binding.emailInputLayout,getString(R.string.email_message));
                }else{
                    binding.email.setError(getString(R.string.email_message));
                }
            }
        }
        if (!phone.isEmpty()){
            binding.signUpProgressBar.setVisibility(View.INVISIBLE);
            if (!isPhoneValid(phone)){
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showErrorMessage(binding.phoneInputLayout,getString(R.string.phone_message));
                }else{
                    binding.phone.setError(getString(R.string.phone_message));
                }
            }
        }

        if (isEmailValid(email) && !TextUtils.isEmpty(userName) && isPhoneValid(phone) && isPasswordValid(password , confirmPass)){
            //TODO-SignUp Here

            signUp(userName,email,phone, password);
            Log.i(SIGN_IN_TAG,"sign up");
        }

    }

    private boolean isEmailValid(String s){
        return s.contains("@") && s.contains(".com");
    }
    private boolean isPasswordValid(String pass,String confirmed){

        return pass.length() >= 8 && TextUtils.equals(pass,confirmed);


    }

    private boolean isPhoneValid(String s){
        return s.length() == 11 || s.length() == 9 || s.length() == 10;
    }

    void signUp(final String userName, final String email , final String phone, final String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                viewModel.pushNewUser(userName, email, phone, user.getUid(), password, "user");
                                startActivity(new Intent(getContext(), UserActivity.class));
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        }else{

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
}
