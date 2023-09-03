package com.example.dealspotter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText edtloginemail,edtloginpassword;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.login);
        edtloginemail = findViewById(R.id.E_mail);
        edtloginpassword = findViewById(R.id.Password);

        auth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textEmail = edtloginemail.getText().toString();
                String textPassword = edtloginpassword.getText().toString();

                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(Login.this,"Please enter your email!",Toast.LENGTH_LONG).show();
                    edtloginemail.setError("Email is required");
                    edtloginemail.requestFocus();
                } else if(TextUtils.isEmpty(textPassword)){
                    Toast.makeText(Login.this,"Please enter your password!",Toast.LENGTH_LONG).show();
                    edtloginpassword.setError("Password is required");
                    edtloginpassword.requestFocus();
                } else{
                    loginuser(textEmail,textPassword);
                }
            }
        });

        Button signup = findViewById(R.id.register);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private void loginuser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(Login.this,"Login failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}