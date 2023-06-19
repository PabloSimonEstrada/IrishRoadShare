// Package declaration
package com.example.irishroadshare;

// Importing necessary libraries
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// Declare LoginActivity, an extension of AppCompatActivity
public class LoginActivity extends AppCompatActivity {

    // Declare Firebase authentication and EditText fields
    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;

    @Override
    // onCreate method, which sets up the initial state of the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the user interface layout for this activity
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();

        // Find the corresponding views for EditText fields
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        // Find the corresponding view for the button
        Button btnLogin = findViewById(R.id.btnLogin);

        // Set a click listener for the login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            // onClick method, which is executed when the button is clicked
            public void onClick(View v) {
                // Get user inputs from EditText fields
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Check if either field is empty
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your email and password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If all checks pass, proceed to login the user
                loginUser(email, password);
            }
        });
    }

    // Method to log in a user in Firebase
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    // onComplete method, which is executed once the task is completed
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If sign in is successful, navigate to MainMenuActivity
                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}