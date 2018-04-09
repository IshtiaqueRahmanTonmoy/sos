package com.emergency.signal.sos;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emergency.signal.entity.users;
import com.emergency.signal.entity.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class SigninActivity extends AppCompatActivity {

    private ImageView imageview;
    private Button btnLogin;
    private Button btnSignUp;
    private ProgressDialog progressDialog;
    private EditText email;
    private EditText password,name,address,gender,phone,role;
    private FirebaseAuth auth;
    private TextView logintext;
    DatabaseReference databaseUsers;
    int Image_Request_Code = 7;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    String Storage_Path = "All_Image_Uploads/";
    public static final String Database_Path = "All_Image_Uploads_Database";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        imageview = (ImageView) findViewById(R.id.user_profile_photo);
        logintext = (TextView) findViewById(R.id.loginTextView);
        //btnLogin = findViewById(R.id.login);
        btnSignUp = (Button) findViewById(R.id.createUserButton);
        email = (EditText) findViewById(R.id.emailEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        name = (EditText) findViewById(R.id.nameEditText);
        address = (EditText) findViewById(R.id.addressEditText);
        gender = (EditText) findViewById(R.id.genderEditText);
        phone = (EditText) findViewById(R.id.phoneEditText);
        role = (EditText) findViewById(R.id.roleEditText);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

            }
        });
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

                    final String emailtext = email.getText().toString();
                    String passwordtext = password.getText().toString();
                    final String nametext = name.getText().toString();
                    final String addresstext = address.getText().toString();
                    final String gendertext = gender.getText().toString();
                    final String phoneNumber = phone.getText().toString();
                    final String roletext = role.getText().toString();
                    Long tsLong = System.currentTimeMillis()/1000;
                    final String createdtext = tsLong.toString();
                    //String url = "https://firebasestorage.googleapis.com/v0/b/i-help-e7082.appspot.com/o/users%2F";
                    //final String photoUrl = "?alt=media&token=6d7f7e63-3478-4af5-b0d1-cfdf49ba9a61";

                    final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

                    //final String photoUrl = "https://firebasestorage.googleapis.com/v0/b/i-help-e7082.appspot.com/o/users%2FDkBWJIrwmzRjS3i30xWjF5Lsd1h1..png?alt=media&token=6d7f7e63-3478-4af5-b0d1-cfdf49ba9a61";
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
                                            final String uid = currentFirebaseUser.getUid();
                                            storageReference2nd.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    String photoUrl = taskSnapshot.getDownloadUrl().toString();
                                                    addValue(uid,addresstext,createdtext,emailtext,gendertext,nametext,phoneNumber,photoUrl,roletext);
                                                }
                                            });


                                            Log.d("userid",uid);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                imageview.setImageBitmap(bitmap);


            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    private void addValue(String uid, String addresstext, String createdtext, String emailtext, String gendertext, String nametext, String phoneNumber, String photoUrl, String role) {
        String id = databaseUsers.push().getKey();

        users user = new users(addresstext,createdtext,emailtext,gendertext,nametext,phoneNumber,photoUrl,role,uid);
        databaseUsers.child(uid).setValue(user);
        name.setText("");
        email.setText("");
        address.setText("");
        gender.setText("");
        phone.setText("");
        password.setText("");
    }

    private String GetFileExtension(Uri filePathUri) {
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(filePathUri)) ;

    }

    private void showProcessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Register a new account...");
        progressDialog.show();
    }
}






















































































