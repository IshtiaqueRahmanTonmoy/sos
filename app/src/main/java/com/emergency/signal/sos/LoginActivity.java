package com.emergency.signal.sos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emergency.signal.entity.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private Button loginButton;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private TextView signupText;
    DatabaseReference databaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        setContentView(R.layout.activity_login);

        /*
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, SendMessage1.class));
            finish();
        }
        */

        email = (EditText) findViewById(R.id.emailEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.passwordLoginButton);
        signupText = (TextView) findViewById(R.id.registerTextView);

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SigninActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    if (email.getText().length()<0) {
                        Utils.showToast(LoginActivity.this, "Please input your email");
                    } else if (password.getText().length()<0) {
                        Utils.showToast(LoginActivity.this, "Please input your password");
                    } else {

                        final String emailtext = email.getText().toString();
                        String passwordtext = password.getText().toString();
                        //requesting Firebase server
                        showProcessDialog();
                        auth.signInWithEmailAndPassword(emailtext, passwordtext)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            if (password.length() < 6) {
                                                password.setError("password length must be minimum 6");
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Authentication failed..", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Toast.makeText(LoginActivity.this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, SendMessage1.class);
                                            progressDialog.dismiss();
                                            intent.putExtra("email",emailtext);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    }
                }
        });
    }

    private void showProcessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

    }
}
