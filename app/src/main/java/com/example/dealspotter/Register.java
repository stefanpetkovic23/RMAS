package com.example.dealspotter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dealspotter.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Register extends AppCompatActivity {

    EditText edfirstname,edlastname,eddateofbirth,edphonenumber,edemail,edpassword;
    ImageView photo;
    public Uri imageUri;
    public String myUri="";
    private StorageTask uploadtask;

    private static final int REQUEST_GALLERY = 1;


    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edfirstname = findViewById(R.id.first_name);
        edlastname = findViewById(R.id.last_name);
        eddateofbirth = findViewById(R.id.username);
        edphonenumber = findViewById(R.id.phone_number);
        edemail = findViewById(R.id.e_mail);
        edpassword = findViewById(R.id.password);
        photo = findViewById(R.id.registerphoto);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        Button register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String Firstname = edfirstname.getText().toString();
                String Lastname = edlastname.getText().toString();
                String Dateofbirth = eddateofbirth.getText().toString();
                String Phonenumber = edphonenumber.getText().toString();
                String Email = edemail.getText().toString();
                String Password = edpassword.getText().toString();



                if(TextUtils.isEmpty(Firstname)){
                    Toast.makeText(Register.this,"Please enter your firstname!",Toast.LENGTH_LONG).show();
                    edfirstname.setError("Firstname is required!");
                    edfirstname.requestFocus();
                }
                else if(TextUtils.isEmpty(Lastname)){
                    Toast.makeText(Register.this,"Please enter your lastname!",Toast.LENGTH_LONG).show();
                    edlastname.setError("Lastname is required");
                    edlastname.requestFocus();
                }
                else if(TextUtils.isEmpty(Dateofbirth)){
                    Toast.makeText(Register.this,"Please enter you passport number!",Toast.LENGTH_LONG).show();
                    eddateofbirth.setError("Date of birth is required");
                    eddateofbirth.requestFocus();
                }
                else if(TextUtils.isEmpty(Phonenumber)){
                    Toast.makeText(Register.this,"Please enter your phone number!",Toast.LENGTH_LONG).show();
                    edphonenumber.setError("Phone number is required");
                    edphonenumber.requestFocus();
                }
                else if (Phonenumber.length()!=10) {
                    Toast.makeText(Register.this,"Phone number must contain 10 digits!",Toast.LENGTH_LONG).show();
                    edphonenumber.setError("Mobile phone should be 10 digits");
                    edphonenumber.requestFocus();
                }
                else if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(Register.this,"Please enter your email!",Toast.LENGTH_LONG).show();
                    edemail.setError("Email is required");
                    edemail.requestFocus();
                }
                else if(TextUtils.isEmpty(Password)){
                    Toast.makeText(Register.this,"Please enter your password",Toast.LENGTH_LONG).show();
                    edpassword.setError("Password is required");
                    edpassword.requestFocus();
                } else if (Password.length()<6) {
                    Toast.makeText(Register.this,"Password is short!",Toast.LENGTH_LONG).show();
                    edpassword.setError("Password is short");
                    edpassword.requestFocus();
                }
                else{
                    registerUser(Firstname,Lastname,Dateofbirth,Phonenumber,Email,Password);
                }


            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            myUri=imageUri.toString();
            photo.setImageURI(imageUri);
        }
    }


    private void registerUser(String firstname, String lastname, String dateofbirth, String phonenumber, String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                             User user = new User(firstname,lastname,dateofbirth,phonenumber,myUri,0);
                            DatabaseReference reference = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered Users");
                            if (imageUri != null) {
                                storageReference = FirebaseStorage.getInstance().getReference().child("Profile Pic").child(user.username+".jpg");
                                storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                               // Toast.makeText(Register.this, "Picture selected!", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Register.this, "Upload failed!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                            reference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(Register.this, "Account created", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(Register.this,"User registration failed!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    }
                });
    }



               /*
                else{
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        edpassword.setError("Your password is too weak!");
                        edpassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        edpassword.setError("Your email is invalid or already in use!");
                        edpassword.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e){
                        edpassword.setError("User is already registered with this email!");
                        edpassword.requestFocus();
                    } catch (Exception e){
                        Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }*/


}