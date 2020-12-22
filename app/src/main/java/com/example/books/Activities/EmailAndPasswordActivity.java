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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailAndPasswordActivity extends AppCompatActivity {
    EditText email, password, confirmPassword;
    TextInputLayout inputLayout;
    Button signIn, signUp;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_and_password);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        confirmPassword = findViewById(R.id.confirmPassword);
        inputLayout = findViewById(R.id.inputConfirmPassword);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPassword.setVisibility(View.INVISIBLE);
                inputLayout.setVisibility(View.INVISIBLE);
                checkUsersDetailsToLogin();

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputLayout.setVisibility(View.VISIBLE);
                confirmPassword.setVisibility(View.VISIBLE);
                checkUsersDetailsToRegister();
            }
        });
    }

    private void checkUsersDetailsToRegister() {
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String ConfirmedPassword = confirmPassword.getText().toString();

        if (Email.isEmpty()|| Password.isEmpty()|| ConfirmedPassword.isEmpty()){

            Toast.makeText(this, "fields Can't be empty", Toast.LENGTH_SHORT).show();

        }else if (!Password.equals(ConfirmedPassword)){

            Toast.makeText(this, "confirmPassword do not match password please check and try again", Toast.LENGTH_SHORT).show();

        }else {

            progressDialog.setTitle("Creating new Account");
            progressDialog.setMessage("please wait while we create your new Account");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);
            firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                confirmPassword.setVisibility(View.INVISIBLE);
                                sendUserToSetupActivity();
                                progressDialog.dismiss();

                            }else {
                                String message = task.getException().getMessage();
                                Toast.makeText(EmailAndPasswordActivity.this, "error occur"+ message, Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }
                        }
                    });

        }
    }


    private void checkUsersDetailsToLogin() {

        String Useremail = email.getText().toString();
        String Password = password.getText().toString();

        if (Useremail.isEmpty()|| Password.isEmpty()){
            Toast.makeText(this, "None of the fields should be empty.", Toast.LENGTH_LONG).show();

        }else {
            progressDialog.setTitle("Logging in");
            progressDialog.setMessage("wait while we check ur credentials ");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);

            firebaseAuth.signInWithEmailAndPassword(Useremail,Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                sendUserToSetupActivity();
                            }else{
                                String message = task.getException().getMessage();
                                Toast.makeText(EmailAndPasswordActivity.this, "error occurred:"+message, Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
    }
}

    private void sendUserToSetupActivity() {
//        String postKey = getIntent().getExtras().get("postKey").toString();
        Intent ClickIntent = new Intent(EmailAndPasswordActivity.this, UsersSetUpActivity.class);
//        ClickIntent.putExtra("postKey", postKey);
        startActivity(ClickIntent);
    }
    }