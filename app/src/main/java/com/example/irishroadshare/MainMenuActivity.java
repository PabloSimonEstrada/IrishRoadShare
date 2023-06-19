// Package declaration
package com.example.irishroadshare;

// Importing necessary libraries
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.bumptech.glide.Glide;

// This is the main menu activity where the application's functionality starts from
public class MainMenuActivity extends AppCompatActivity {

    // Declare the welcome TextView
    private TextView tvWelcome;

    @Override
    // This method is executed when the activity is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for the activity
        setContentView(R.layout.activity_main_menu);

        // Get the ImageView that will hold the background gif
        ImageView backgroundGif = findViewById(R.id.background_gif);

        // Load the gif into the ImageView using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.road)
                .into(backgroundGif);

        // Get the TextView that will display the welcome message
        tvWelcome = findViewById(R.id.tvWelcome);

        // Get the ImageButtons that will navigate to different parts of the app
        ImageButton btnSearchTrip = findViewById(R.id.btnSearchTrip);
        ImageButton btnPostTrip = findViewById(R.id.btnPostTrip);
        ImageButton btnMessages = findViewById(R.id.btnMessages);

        // Get the current Firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Check if the user is logged in
        if (user != null) {
            // Get the user's unique ID
            String uid = user.getUid();

            // Get a Firestore instance and fetch the user document
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        // If the request is successful, get the username and display it in the welcome message
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String username = document.getString("username");
                                tvWelcome.setText(String.format(getString(R.string.hello_s), username.toUpperCase()));
                            }
                        }
                    });
        }

        // Set click listeners for the ImageButtons that start the corresponding activities
        btnSearchTrip.setOnClickListener(view -> startActivity(new Intent(MainMenuActivity.this, SearchTripsActivity.class)));

        btnPostTrip.setOnClickListener(view -> startActivity(new Intent(MainMenuActivity.this, PostTripActivity.class)));

        btnMessages.setOnClickListener(view -> startActivity(new Intent(MainMenuActivity.this, MessagesActivity.class)));
    }
}