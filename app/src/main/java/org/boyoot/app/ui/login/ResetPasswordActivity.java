package org.boyoot.app.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityResetPasswordBinding;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    private ActivityResetPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_reset_password);

        binding.resetEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.resetErrorTextView.setVisibility(View.INVISIBLE);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.textInputLayout.setBoxStrokeColor(getColor(R.color.colorAccent));
                    binding.textInputLayout.setDefaultHintTextColor(getColorStateList(R.color.colorAccent));
                    binding.textInputLayout.setHelperText(null);
                    binding.textInputLayout.setHelperTextColor(getColorStateList(R.color.colorBackGround));
                }else{
                    binding.resetEmail.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.confirmResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptToResetPassword();
            }
        });
    }

    private void attemptToResetPassword(){
        binding.signInProgressBar.setVisibility(View.VISIBLE);
        String email = binding.resetEmail.getEditableText().toString();

        if (email.isEmpty()){
            binding.signInProgressBar.setVisibility(View.INVISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.textInputLayout.setBoxStrokeColor(Color.RED);
                binding.textInputLayout.setDefaultHintTextColor(getColorStateList(R.color.colorSecondary));
                binding.textInputLayout.setHelperText(getString(R.string.empty_message));
                binding.textInputLayout.setHelperTextColor(getColorStateList(R.color.colorSecondary));
            }else{
                binding.resetEmail.setError(getString(R.string.empty_message));
            }
        }

        if (!email.isEmpty()){
            if(!email.contains("@") || !email.contains(".")){
                binding.signInProgressBar.setVisibility(View.INVISIBLE);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.textInputLayout.setBoxStrokeColor(Color.RED);
                    binding.textInputLayout.setDefaultHintTextColor(getColorStateList(R.color.colorSecondary));
                    binding.textInputLayout.setHelperText(getString(R.string.email_message));
                    binding.textInputLayout.setHelperTextColor(getColorStateList(R.color.colorSecondary));
                }else{
                    binding.resetEmail.setError(getString(R.string.email_message));
                }
            }
        }

        if (!email.isEmpty() && email.contains("@") && email.contains(".")){
            sendResetEmail(email);
        }


    }

    private void sendResetEmail(String emailAddress){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("reset_password", "Email sent.");
                            showDialog();
                            binding.signInProgressBar.setVisibility(View.INVISIBLE);
                        }else {
                            binding.signInProgressBar.setVisibility(View.INVISIBLE);
                           binding.resetErrorTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void showDialog(){
        Dialog settingsDialog = new Dialog(this);
        Objects.requireNonNull(settingsDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.password_reset_dialog, null));
        settingsDialog.show();
        settingsDialog.getWindow().setBackgroundDrawable(null);
        settingsDialog.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDialog.dismiss();
                finish();
            }
        });
    }
}
