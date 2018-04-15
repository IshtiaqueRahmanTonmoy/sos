package com.emergency.signal.sos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emergency.signal.entity.users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScrollingActivity extends AppCompatActivity {

    TextView emailTxt,mobileTxt,addressTxt,contactsTxt;
    ImageView imageheader;
    Button roleBtn;
    Context context;
    String emailval;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        emailval = getIntent().getStringExtra("email");
        imageheader = (ImageView) findViewById(R.id.bgheader);
        emailTxt = (TextView) findViewById(R.id.emailTxt);
        mobileTxt = (TextView) findViewById(R.id.mobileTxtoutput);
        addressTxt = (TextView) findViewById(R.id.addressoutputTxt);
        contactsTxt = (TextView) findViewById(R.id.contactsoutput);

        roleBtn = (Button) findViewById(R.id.chkState1);

        showProcessDialog();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.orderByChild("email").equalTo(emailval).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    String email = childDataSnapshot.child("email").getValue().toString();
                    String mobile = childDataSnapshot.child("phoneNumber").getValue().toString();
                    String addess = childDataSnapshot.child("address").getValue().toString();
                    //String contacts = childDataSnapshot.child("connects").getValue().toString();
                    String role = childDataSnapshot.child("role").getValue().toString();
                    String image = childDataSnapshot.child("photoUrl").getValue().toString();

                    DataSnapshot contactSnapshot = childDataSnapshot.child("connects");
                    Iterable<DataSnapshot> contactChildren = contactSnapshot.getChildren();
                    for (DataSnapshot contact : contactChildren) {
                        users c = contact.getValue(users.class);
                        Toast.makeText(context, ""+c.toString(), Toast.LENGTH_SHORT).show();
                        //Log.d("datashoft",c.getUserId());
                    }

                    Log.d("image",image);
                    //Toast.makeText(context, ""+image, Toast.LENGTH_SHORT).show();
                    Glide.with(ScrollingActivity.this).load(image).into(imageheader);

                   emailTxt.setText(email);
                   mobileTxt.setText(mobile);
                   addressTxt.setText(addess);
                   //contactsTxt.setText(contacts);
                   roleBtn.setText(role);
                   progressDialog.dismiss();
                    //Log.d("key", "PARENT: "+ childDataSnapshot.getKey());
                    //Log.d("value",""+ childDataSnapshot.child("name").getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void showProcessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting profile informations");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }
}
