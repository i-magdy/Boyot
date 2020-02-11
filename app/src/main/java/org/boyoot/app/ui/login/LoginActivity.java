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
    private LinearLayout mSignUpLayout;
    private LinearLayout mLoginLayout;
    private EditText mEmailEditText;
    private EditText mUserNameEditText;
    private EditText mPassEditText;
    private EditText mConfirmPassEditText;
    private EditText mLoginEmailEditText;
    private EditText mLoginPassEditText;
    private TextView mErrorTextView;
    private boolean isSignUpClicked = false;
    private boolean isLoginClicked = false;
    private boolean isSignUpLive = false;
    private boolean isLoginLive = false;
    private static String SIGN_IN_TAG = "SignTag";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private List<UserProfileModel> mUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mSignUpLayout = mBinding.signUpLayout;
        mLoginLayout = mBinding.loginLayout;
        mUserNameEditText = mBinding.userName;
        mEmailEditText = mBinding.email;
        mPassEditText = mBinding.password;
        mConfirmPassEditText = mBinding.confirmPassword;
        mLoginEmailEditText = mBinding.loginEmail;
        mLoginPassEditText = mBinding.loginPassword;
        mErrorTextView = mBinding.loginErrorTextView;

        //check if user exists
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsers = new ArrayList<>();
        checkUsersFromCloud(mUsers);


        mBinding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSignUpClicked && !isSignUpLive){
                    hideLayout(mLoginLayout);
                    showLayout(mSignUpLayout);
                    isSignUpLive = true;
                    isSignUpClicked = true;
                    isLoginClicked = false;
                    isLoginLive = false;
                }else if(isSignUpClicked && isSignUpLive){
                    //Toast.makeText(getApplicationContext(),"hey",Toast.LENGTH_LONG).show();
                attemptSignUp();
                }
            }
        });

        mBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoginClicked&& !isLoginLive){
                    hideLayout(mSignUpLayout);
                    showLayout(mLoginLayout);
                    isSignUpClicked = false;
                    isLoginClicked = true;
                    isSignUpLive = false;
                    isLoginLive = true;

                }else if(isLoginClicked && isLoginLive){
                    attemptLogin();
                    Log.i( "Ytest",mUsers.toString());
                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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

    boolean isPasswordValid(String pass,String confirmed){

       return (pass.length() >= 8 && TextUtils.equals(pass,confirmed));


    }
    boolean isEmailValid(String s){
        return s.contains("@") && s.contains(".com");
    }

    private void attemptSignUp(){
        String userName = mUserNameEditText.getEditableText().toString();
        String email = mEmailEditText.getEditableText().toString();
        String password = mPassEditText.getEditableText().toString();
        String confirmPass = mConfirmPassEditText.getEditableText().toString();

        if(TextUtils.isEmpty(email)){ mEmailEditText.setError(getString(R.string.empty_message)); }

        if (!isEmailValid(email)){
            if(TextUtils.isEmpty(userName)){ mUserNameEditText.setError(getString(R.string.empty_message));
            }else {

                mEmailEditText.setError(getString(R.string.email_message));
            }
        }else{
            mEmailEditText.setError(null);
        }
        if (!isPasswordValid(password,confirmPass)){
            if(TextUtils.isEmpty(password)){ mPassEditText.setError(getString(R.string.empty_message));
            }else if(TextUtils.isEmpty(confirmPass)){ mConfirmPassEditText.setError(getString(R.string.empty_message));
            }else {
                mPassEditText.setError(getString(R.string.not_matching_pass));
            }
        }

        if (isEmailValid(email) && !TextUtils.isEmpty(userName) && isPasswordValid(password , confirmPass)){
            //TODO-SignUp Here

            signUp(userName,email, password);
        }

    }

    private void attemptLogin(){
        String email = mLoginEmailEditText.getEditableText().toString();
        String password = mLoginPassEditText.getEditableText().toString();
        if (TextUtils.isEmpty(email)) mLoginEmailEditText.setError(getString(R.string.empty_message));
        if (TextUtils.isEmpty(password)) mLoginPassEditText.setError(getString(R.string.empty_message));
        //TODO check if user exists

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            if (!isUserExist(mUsers,email) || !isPasswordCorrect(mUsers,password)){
                 if(!isUserExist(mUsers,email)){
                    userDoesNotExistUI(true);
                }else{
                     passwordIncorrectUI(true);
                 }

            }else {
                userDoesNotExistUI(false);
                passwordIncorrectUI(false);
                login(email,password);
            }
        }

    }
    void signUp(final String userName, final String email , final String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(SIGN_IN_TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mDatabase.child("users").push().setValue(new UserProfileModel(userName,email,password,"disabled"));
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(SIGN_IN_TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


    }

    void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(SIGN_IN_TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(SIGN_IN_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }



    boolean isUserExist(List<UserProfileModel> users , String email){
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
    }
    void checkUsersFromCloud(final List<UserProfileModel> users){
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

    }

}
