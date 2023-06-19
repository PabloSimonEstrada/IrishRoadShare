// Package declaration
package com.example.irishroadshare;

// Import necessary libraries and classes
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.HashMap;
import java.util.Map;

// Declare CreateAccountActivity, an extension of AppCompatActivity
public class CreateAccountActivity extends AppCompatActivity {

    // Declare Firebase authentication and EditText fields
    private FirebaseAuth mAuth;
    private EditText etUsername, etEmail, etPhone, etPassword, etConfirmPassword;

    @Override
    // onCreate method, which sets up the initial state of the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the user interface layout for this activity
        setContentView(R.layout.activity_create_account);

        // Initialize Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();

        // Find the corresponding views for EditText fields
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        // Find the corresponding view for the button
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);

        // Set a click listener for the create account button
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            // onClick method, which is executed when the button is clicked
            public void onClick(View v) {
                // Get user inputs from EditText fields
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String username = etUsername.getText().toString().trim();

                // Check if any field is empty
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the entered passwords match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(CreateAccountActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the phone number is valid
                if (!phone.matches("\\+?\\d+")) {
                    Toast.makeText(CreateAccountActivity.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If all checks pass, proceed to create a new account
                createAccount(email, password, username, phone);
            }
        });
    }

    // Method to create a new account in Firebase
    private void createAccount(String email, String password, final String username, final String phone) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    // onComplete method, which is executed once the task is completed
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get the current user
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Check if the user is not null
                            if (user != null) {
                                // Create a HashMap to store user details
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("username", username);
                                userMap.put("phone", phone);

                                // Get an instance of FirebaseFirestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                // Create a new user document in the Firestore "users" collection
                                db.collection("users").document(user.getUid())
                                        .set(userMap)
                                        .addOnSuccessListener(aVoid -> {
                                            // If successful, start the MainMenuActivity
                                            Intent intent = new Intent(CreateAccountActivity.this, MainMenuActivity.class);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(CreateAccountActivity.this, "Error saving user details.",
                                                Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            // If sign up fails, check if the email is already in use
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(CreateAccountActivity.this, "Error, this email is already registered.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // If the sign up fails for another reason, display a generic error message
                                Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}