// Package declaration
package com.example.irishroadshare;

// Importing necessary libraries
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// Class representing the welcome activity of the application.
public class WelcomeActivity extends AppCompatActivity {

    // Function called when the activity is first created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity.
        setContentView(R.layout.activity_welcome);

        // Find the Login button and set a click listener on it.
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            // When clicked, launch the LoginActivity.
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Find the Create Account button and set a click listener on it.
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When clicked, launch the CreateAccountActivity.
                Intent intent = new Intent(WelcomeActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}