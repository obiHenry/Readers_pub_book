package com.example.books.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.books.R;
import com.example.books.fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UsersSetUpActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    String currentUserId;

    private EditText userName,phoneNumber;
    private Button fullSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_set_up);
        userName = findViewById(R.id.userName);
        phoneNumber = findViewById(R.id.mobile);
        fullSignUp = findViewById(R.id.setUp);
        progressDialog = new ProgressDialog(this);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        fullSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });
    }

    private void saveUser() {
        String Username = userName.getText().toString();
        String phoneNo = phoneNumber.getText().toString();

        if (Username.isEmpty()||phoneNo.isEmpty()){
            Toast.makeText( UsersSetUpActivity.this,"None of the fields can be empty",Toast.LENGTH_LONG).show();
        }else{
            progressDialog.setTitle("setting up details");
            progressDialog.setMessage("wait while we validate");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();

            userMap.put("phoneNo", phoneNo );
            userMap.put("userName", Username );

            reference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(UsersSetUpActivity.this, "Account setup successfully", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }else {
                        String message = task.getException().getMessage();
                        Toast.makeText(UsersSetUpActivity.this, "error ocurred: "+message, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }



    private void SendUserToMainActivity() {
        Intent intent = new Intent(UsersSetUpActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}