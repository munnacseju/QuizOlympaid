package com.example.navtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navtest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText emailEditText, passwordEditText;
    Button loginButton, registerButton;
    TextView forgetPasswordTextView;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log In");






        forgetPasswordTextView = findViewById(R.id.forgetPasswordButtonid);
        progressBar = findViewById(R.id.loginLoadingProgressBarid);
        emailEditText = findViewById(R.id.emailEditTextid);
        passwordEditText = findViewById(R.id.passwordEditTextid);
        loginButton = findViewById(R.id.loginButtonid);
        registerButton = findViewById(R.id.registrationButtonid);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgetPasswordTextView.setOnClickListener(this);




        firebaseAuth = FirebaseAuth.getInstance();



    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.registrationButtonid){
            finish();
            Intent intent = new Intent(this, Registration.class);
            startActivity(intent);
        }

        else if(view.getId()==R.id.loginButtonid){

            final String email, password;
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

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                                SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.commit();

                            }
                            else {
                                if(task.getException() instanceof FirebaseNetworkException) {
                                    Toast.makeText(getApplicationContext(),"Please Cheack your internet connection", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                                forgetPasswordTextView.setVisibility(View.VISIBLE);

                            }
                        }
                    });
        }
        else if(view.getId()==R.id.forgetPasswordButtonid){
            final EditText resetEmailTextView = new EditText(view.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
            passwordResetDialog.setTitle("Reset Password ?");
            passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link.");

            passwordResetDialog.setView(resetEmailTextView);

            passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    String email = resetEmailTextView.getText().toString().trim();

                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        Toast.makeText(getApplicationContext(), "Your mail is not valid", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Reset Link Sent, Please Cheack Your Mail", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error! Reset Link Not Sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            passwordResetDialog.show();
        }
    }

}
