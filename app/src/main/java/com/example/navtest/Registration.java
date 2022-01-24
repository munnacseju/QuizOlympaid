package com.example.navtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    String email;
    ProgressBar progressBar;
    EditText emailEditText, passwordEditText;
    Button logInButton, registrationButton;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference, databaseReferenceForUserEmailList;
    Button profileImageSelectorButton;
    ImageView profileImageView;
    private Uri imageUri;
    private static final int IMAGE_REQUEST=1;
    private Boolean imageSelect = false;


    StorageReference storageReference;
    TextView registrationTextView;
    EditText classEditText, phoneNumberEditText, nameEditText, institutionEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        progressBar = findViewById(R.id.progressBarid);
        storageReference = FirebaseStorage.getInstance().getReference("profile");


        registrationTextView = findViewById(R.id.registrationTextViewid);
        classEditText = findViewById(R.id.classEditTextid);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditTextid);
        nameEditText = findViewById(R.id.nameEditTextid);
        institutionEditText = findViewById(R.id.institutionEditTextid);
        profileImageSelectorButton = findViewById(R.id.profileImageSelectorButtonid);
        profileImageView = findViewById(R.id.profileImageid);

        emailEditText = findViewById(R.id.emailEditTextid);
        passwordEditText = findViewById(R.id.passwordEditTextid);
        logInButton = findViewById(R.id.loginButtonid);
        registrationButton = findViewById(R.id.registrationButtonid);
        logInButton.setOnClickListener(this);
        registrationButton.setOnClickListener(this);
        profileImageSelectorButton.setOnClickListener(this);
        setTitle("Registration");

        firebaseAuth = FirebaseAuth.getInstance();




    }


    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.loginButtonid){
            finish();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        else if(view.getId()==R.id.registrationButtonid){
            if(imageSelect==false){
                Toast.makeText(getApplicationContext(), "You must select a profile image", Toast.LENGTH_SHORT).show();
                return;
            }
            register();
        }
        else if(view.getId()==R.id.profileImageSelectorButtonid){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_REQUEST);
        }
    }
    void register(){

        final String password;
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Enter Your Email");
            emailEditText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordEditText.setError("Enter Your Password");
            passwordEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Enter Valid Email");
            emailEditText.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordEditText.setError("Password must at least 6 digit");
            passwordEditText.requestFocus();
            return;
        }

        logInButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        registrationTextView.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Email varification is send", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Sorry, Email varification is not send", Toast.LENGTH_SHORT).show();

                                }
                            });

                            saveImage();
                        }
                        else{

                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(),"You have already acount in this email", Toast.LENGTH_SHORT).show();
                            }
                            else if(task.getException() instanceof FirebaseNetworkException) {
                                Toast.makeText(getApplicationContext(),"Please Cheack your internet connection", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            progressBar.setVisibility(View.GONE);
                            registrationTextView.setVisibility(View.GONE);
                            logInButton.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(getApplicationContext(), "Sorry! Please cheack your internet connection!", Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(profileImageView);
            profileImageView.setVisibility(View.VISIBLE);

        }
        imageSelect = true;
    }

    //Get File Extention
    public String getFileExtention(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    public void saveImage(){

        //Save Image

        StorageReference storageReference1 = storageReference.child(System.currentTimeMillis()+"."+getFileExtention(imageUri));

        storageReference1.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Save Extra Data;

                String reference = email.replace(".", "rm");
                databaseReference = firebaseDatabase.getReference(reference);



                //For User Email List
                databaseReferenceForUserEmailList = firebaseDatabase.getReference("userEmailList");
                String key = databaseReferenceForUserEmailList.push().getKey();
                databaseReferenceForUserEmailList.child(key).setValue(email);




                String name, clas, phone, institution;
                name = nameEditText.getText().toString().trim();
                clas = classEditText.getText().toString().trim();
                phone = phoneNumberEditText.getText().toString().trim();
                institution = institutionEditText.getText().toString().trim();


                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri imageDownloadUri = uriTask.getResult();

                Handle handle = new Handle(name, clas, phone, institution, imageDownloadUri.toString(),0, 100,0,0);
                databaseReference.setValue(handle);


                Toast.makeText(getApplicationContext(), "You Successfully Registerd!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                Intent intent = new Intent(getApplicationContext(), Login.class);
                finish();
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });
    }
}
