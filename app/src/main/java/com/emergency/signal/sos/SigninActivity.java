package com.emergency.signal.sos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SigninActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnSignUp;
    private ProgressDialog progressDialog;
    private EditText email;
    private EditText password;
    private FirebaseAuth auth;
    private TextView logintext;
    DatabaseReference databaseArtists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        databaseArtists = FirebaseDatabase.getInstance().getReference("users");

        logintext = (TextView) findViewById(R.id.loginTextView);
        //btnLogin = findViewById(R.id.login);
        btnSignUp = (Button) findViewById(R.id.createUserButton);
        email = (EditText) findViewById(R.id.emailEditText);
        password = (EditText) findViewById(R.id.passwordEditText);

        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /*
        //go to Login Activity
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        */

        //sign up a new account
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().length()<0) {
                    Utils.showToast(SigninActivity.this, "Please input your email");
                } else if (password.getText().length()<0) {
                    Utils.showToast(SigninActivity.this, "Please input your password");
                } else {

                    String emailtext = email.getText().toString();
                    String passwordtext = password.getText().toString();
                    //requesting Firebase server
                    showProcessDialog();
                    auth.createUserWithEmailAndPassword(emailtext, passwordtext)
                            .addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    Log.d("task",task.getResult().toString());
                                    if (!task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Utils.showToast(SigninActivity.this, "Register failed!");
                                    } else {
                                        Utils.showToast(SigninActivity.this, "Register successful!");
                                        FirebaseUser currentFirebaseUser = auth.getInstance().getCurrentUser() ;
                                        if (currentFirebaseUser != null) {
                                            String userId = currentFirebaseUser.getUid();
                                            addValue(userId);

                                            Log.d("userid",userId);
                                            //Toast.makeText(SigninActivity.this, ""+userId, Toast.LENGTH_SHORT).show();
                                            //String userEmail = currentFirebaseUser.getEmail();
                                        }

                                        startActivity(new Intent(SigninActivity.this, LoginActivity.class));
                                        progressDialog.dismiss();
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void addValue(String userId) {
    }

    private void showProcessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Register a new account...");
        progressDialog.show();
    }
}






















































































