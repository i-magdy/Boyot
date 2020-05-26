package org.boyoot.app.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.boyoot.app.MainActivity;
import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityLoginBinding;
import org.boyoot.app.model.UserProfileModel;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
  /*  private LinearLayout mSignUpLayout;
    private LinearLayout mLoginLayout;
    private EditText mEmailEditText;
    private EditText mUserNameEditText;
    private EditText mPassEditText;
    private EditText mConfirmPassEditText;
    private EditText mLoginEmailEditText;
    private EditText mLoginPassEditText;
    private TextView mErrorTextView;*/
    private boolean isSignUpClicked = false;
    private boolean isLoginClicked = false;
    private boolean isSignUpLive = false;
    private boolean isLoginLive = false;
    private ActivityLoginBinding binding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            finish();
        }
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   // hideLayout(binding.loginLayout);
                    //showLayout(binding.signUpLayout);
                    binding.signUpFragment.setVisibility(View.VISIBLE);
                    binding.loginFragment.setVisibility(View.GONE);
                    binding.signUpButton.setVisibility(View.GONE);
                    binding.loginButton.setVisibility(View.GONE);
                    isSignUpLive = true;
                    isSignUpClicked = true;
                    isLoginClicked = false;
                    isLoginLive = false;

                    //Toast.makeText(getApplicationContext(),"hey",Toast.LENGTH_LONG).show();


            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //hideLayout(binding.signUpLayout);
                    //showLayout(binding.loginLayout);
                    binding.signUpFragment.setVisibility(View.GONE);
                    binding.loginFragment.setVisibility(View.VISIBLE);
                    binding.signUpButton.setVisibility(View.GONE);
                    binding.loginButton.setVisibility(View.GONE);
                    isSignUpClicked = false;
                    isLoginClicked = true;
                    isSignUpLive = false;
                    isLoginLive = true;


            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
       // FirebaseUser currentUser = mAuth.getCurrentUser();
        ///updateUI(currentUser);
    }


    private void updateUI(FirebaseUser user){
        if (user != null){
           Intent i = new Intent(getApplicationContext(), MainActivity.class);
           startActivity(i);
           finish();
        }

    }

    private void showLayout(View v){
        v.setVisibility(View.VISIBLE);
    }
    private void hideLayout(View v){
        v.setVisibility(View.INVISIBLE);
    }









  /*  private boolean isUserExist(List<UserProfileModel> users , String email){
        //user account dose not exist
        int size = users.size();
        if (size == 0) {return false;}
        for (int i =0;i < size; i++){
            String s = users.get(i).getEmail();
            if (TextUtils.equals(email,s)){
                return true;
            }
        }
        return false;
    }
    boolean isPasswordCorrect(List<UserProfileModel> users , String password){
        //user account dose not exist
        int size = users.size();
        if (size == 0) {return false;}
        for (int i =0;i < size; i++){
            String s = users.get(i).getPassword();
            if (TextUtils.equals(password,s)){
                return true;
            }
        }
        return false;
    }
    void userDoesNotExistUI(boolean b){
        if(b) {
            mErrorTextView.setText(getString(R.string.login_email_error_message));
            mErrorTextView.setVisibility(View.VISIBLE);
        }else{
            mErrorTextView.setVisibility(View.INVISIBLE);
        }
    }
    void passwordIncorrectUI(boolean b){
        if(b) {
            mErrorTextView.setText(getString(R.string.login_password_message));
            mErrorTextView.setVisibility(View.VISIBLE);
        }else{
            mErrorTextView.setVisibility(View.INVISIBLE);
        }
    }*/
   /* void checkUsersFromCloud(final List<UserProfileModel> users){
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        DatabaseReference reference = data.getReference().child("users");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                    if (dataSnapshot.exists()) {

                        UserProfileModel user = dataSnapshot.getValue(UserProfileModel.class);
                        users.add(user);
                        Log.i("TEST_DATABASE",  " HOW");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        reference.addChildEventListener(childEventListener);

    }*/

}
