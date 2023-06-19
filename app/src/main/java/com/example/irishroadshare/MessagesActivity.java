// Package declaration
package com.example.irishroadshare;

// Importing necessary libraries
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    // Declare instance variables for RecyclerView, adapter, data list and user id
    private RecyclerView messagesRecyclerView;
    private ConversationsAdapter conversationsAdapter;
    private List<Conversation> conversationList;
    private String currentUserId;

    @Override
    // onCreate method, which initializes the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the user interface layout for this activity
        setContentView(R.layout.activity_messages);

        // Get Firebase Auth instance and the current user's id
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUserId = auth.getUid();

        // Find the corresponding view for RecyclerView and set its properties
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the conversationList as an ArrayList
        conversationList = new ArrayList<>();

        // Call the method to read conversations
        readConversations();

        // Setup bottom navigation view and its item click listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            // Switch to different activities based on the clicked item
            if (id == R.id.navigation_post_trip) {
                Intent postTripIntent = new Intent(MessagesActivity.this, PostTripActivity.class);
                startActivity(postTripIntent);
                finish();
            } else if (id == R.id.navigation_search) {
                Intent searchIntent = new Intent(MessagesActivity.this, SearchTripsActivity.class);
                startActivity(searchIntent);
                finish();
            } else if (id == R.id.navigation_message) {
                // Stay on the current activity if "message" is clicked
                return true;
            } else {
                return false;
            }
            return true;
        });

        // Set the selected item to "message"
        bottomNavigationView.setSelectedItemId(R.id.navigation_message);
    }

    // Method to read conversations from Firestore
    private void readConversations() {
        FirebaseFirestore.getInstance().collection("conversations").addSnapshotListener((value, error) -> {
            // If there is an error in reading the data, log the error and return
            if (error != null) {
                Log.w("TAG", getString(R.string.listen_failed), error);
                return;
            }
            // If the data is successfully read, update the conversation list
            if (value != null) {
                conversationList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Conversation conversation = doc.toObject(Conversation.class);
                    conversation.setId(doc.getId());
                    // Add the conversation to the list only if it involves the current user
                    if (conversation.getRequesterId().equals(currentUserId) || conversation.getTripOwnerId().equals(currentUserId)) {
                        conversationList.add(conversation);
                    }
                }
                // Set the adapter for the RecyclerView
                conversationsAdapter = new ConversationsAdapter(MessagesActivity.this, conversationList);
                messagesRecyclerView.setAdapter(conversationsAdapter);
            }
        });
    }
}