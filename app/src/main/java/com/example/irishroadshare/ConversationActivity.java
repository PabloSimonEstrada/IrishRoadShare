// Package declaration
package com.example.irishroadshare;
// Importing necessary libraries
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Definition of the ConversationActivity class which extends the AppCompatActivity class
public class ConversationActivity extends AppCompatActivity {

    // Class member declarations
    private RecyclerView conversationRecyclerView;
    private MessagesAdapter messagesAdapter;
    private List<com.example.irishroadshare.Message> messageList;
    private EditText messageInput;
    private TextView conversationTitle;
    private Button acceptButton;
    private Button denyButton;
    private TextView confirmationText;

    // The onCreate() method which is called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // UI element initializations
        conversationRecyclerView = findViewById(R.id.conversationRecyclerView);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageInput = findViewById(R.id.messageInput);
        Button sendMessageButton = findViewById(R.id.sendMessageButton);
        conversationTitle = findViewById(R.id.conversationTitle);

        acceptButton = findViewById(R.id.acceptButton);
        denyButton = findViewById(R.id.denyButton);
        confirmationText = findViewById(R.id.confirmationText);

        // Button click listeners
        sendMessageButton.setOnClickListener(v -> sendMessage());
        acceptButton.setOnClickListener(v -> acceptPassenger());
        denyButton.setOnClickListener(v -> denyPassenger());

        // Initialize message list
        messageList = new ArrayList<>();

        // Fetch and display messages
        readMessages();

        // Bottom navigation view setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setSelectedItemId(R.id.navigation_message);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_post_trip) {
                Intent postTripIntent = new Intent(ConversationActivity.this, PostTripActivity.class);
                startActivity(postTripIntent);
                finish();
            } else if (id == R.id.navigation_search) {
                Intent searchIntent = new Intent(ConversationActivity.this, SearchTripsActivity.class);
                startActivity(searchIntent);
                finish();
            } else if (id == R.id.navigation_message) {
                Intent notificationsIntent = new Intent(ConversationActivity.this, MessagesActivity.class);
                startActivity(notificationsIntent);
                finish();
            } else {
                return false;
            }
            return true;
        });
    }

    // Method to send a message
    private void sendMessage() {
        String messageText = messageInput.getText().toString();
        if (!messageText.isEmpty()) {
            // If the input message is not empty
            String conversationId = getIntent().getStringExtra("CONVERSATION_ID");
            com.example.irishroadshare.Message message = new com.example.irishroadshare.Message();
            message.setContent(messageText);
            message.setTimestamp(new Date());
            message.setSenderId(FirebaseAuth.getInstance().getCurrentUser().getUid());

            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore.getInstance().collection("users").document(currentUserId)
                    .get().addOnSuccessListener(documentSnapshot -> {
                        String senderName = documentSnapshot.getString("username");
                        message.setSenderName(senderName);

                        FirebaseFirestore.getInstance().collection("conversations").document(conversationId)
                                .collection("messages").add(message)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    messageInput.setText("");
                                    // Update the lastMessage and senderName in the conversation
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("lastMessage", messageText);
                                    updates.put("timestamp", new Date());
                                    updates.put("senderName", senderName);
                                    FirebaseFirestore.getInstance().collection("conversations").document(conversationId)
                                            .update(updates);
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error adding document", e);
                                    }
                                });
                    });
        } else {
            // If the input message is empty
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to read messages
    private void readMessages() {
        String conversationId = getIntent().getStringExtra("CONVERSATION_ID");
        Log.d("TAG", "CONVERSATION_ID: " + conversationId);
        FirebaseFirestore.getInstance().collection("conversations").document(conversationId)
                .get().addOnSuccessListener(documentSnapshot -> {
                    Conversation conversation = documentSnapshot.toObject(Conversation.class);
                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String otherUserId = conversation.getOtherUserId(currentUserId);

                    // Get trip request status
                    FirebaseFirestore.getInstance().collection("tripRequests")
                            .whereEqualTo("tripId", conversation.getTripId())
                            .whereEqualTo("requesterId", otherUserId)
                            .addSnapshotListener((value, error) -> {
                                if (error != null) {
                                    Log.w("TAG", getString(R.string.listen_failed), error);
                                    return;
                                }

                                for (QueryDocumentSnapshot doc : value) {
                                    if ("accepted".equals(doc.getString("status"))) {
                                        acceptButton.setVisibility(View.GONE);
                                        denyButton.setVisibility(View.GONE);
                                        confirmationText.setText(R.string.request_accepted);
                                        confirmationText.setTextColor(Color.GREEN);
                                        confirmationText.setVisibility(View.VISIBLE);
                                    } else if ("denied".equals(doc.getString("status"))) {
                                        acceptButton.setVisibility(View.GONE);
                                        denyButton.setVisibility(View.GONE);
                                        confirmationText.setText(R.string.request_denied);
                                        confirmationText.setTextColor(Color.RED);
                                        confirmationText.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                    // Check if current user is the trip owner
                    if (conversation.getTripOwnerId().equals(currentUserId)) {
                        acceptButton.setVisibility(View.VISIBLE);
                        denyButton.setVisibility(View.VISIBLE);
                    } else {
                        acceptButton.setVisibility(View.GONE);
                        denyButton.setVisibility(View.GONE);
                    }

                    FirebaseFirestore.getInstance().collection("users").document(otherUserId)
                            .get().addOnSuccessListener(documentSnapshot1 -> {
                                String otherUserName = documentSnapshot1.getString("username");
                                conversationTitle.setText(otherUserName);
                            });
                    loadMessages(conversationId);
                });
    }

    // Method to load messages
    private void loadMessages(String conversationId) {
        FirebaseFirestore.getInstance().collection("conversations").document(conversationId)
                .collection("messages").orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error);
                        return;
                    }
                    if (value != null) {
                        messageList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            com.example.irishroadshare.Message message = doc.toObject(com.example.irishroadshare.Message.class);
                            messageList.add(message);
                        }
                        messagesAdapter = new MessagesAdapter(ConversationActivity.this, messageList);
                        conversationRecyclerView.setAdapter(messagesAdapter);
                        conversationRecyclerView.scrollToPosition(messageList.size() - 1); // Here is the addition
                        Log.d("TAG", "Messages loaded: " + messageList.size());
                    } else {
                        Log.d("TAG", getString(R.string.no_documents_returned_from_firestore));
                    }
                });
    }

    // Method to accept a passenger for a trip
    private void acceptPassenger() {
        Log.d("TAG", getString(R.string.acceptpassenger_method_called));
        String tripId = getIntent().getStringExtra("TRIP_ID");
        Log.d("TAG", "TRIP_ID: " + tripId);
        FirebaseFirestore.getInstance().collection("trips").document(tripId)
                .get().addOnSuccessListener(documentSnapshot -> {
                    Trip trip = documentSnapshot.toObject(Trip.class);
                    int seatsAvailable = Integer.parseInt(trip.getSeatsAvailable());
                    int seatsToBook = getIntent().getIntExtra("SEATS_TO_BOOK", 0);
                    if (seatsAvailable >= seatsToBook) {
                        seatsAvailable -= seatsToBook;

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("seatsAvailable", String.valueOf(seatsAvailable));
                        FirebaseFirestore.getInstance().collection("trips").document(tripId).update(updates);

                        // Update trip request status
                        String conversationId = getIntent().getStringExtra("CONVERSATION_ID");
                        FirebaseFirestore.getInstance().collection("conversations").document(conversationId)
                                .get().addOnSuccessListener(documentSnapshot1 -> {
                                    Conversation conversation = documentSnapshot1.toObject(Conversation.class);
                                    String otherUserId = conversation.getOtherUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    FirebaseFirestore.getInstance().collection("tripRequests")
                                            .whereEqualTo("tripId", tripId)
                                            .whereEqualTo("requesterId", otherUserId)
                                            .get()
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        document.getReference().update("status", "accepted");
                                                    }
                                                } else {
                                                    Log.w("TAG", getString(R.string.error_updating_trip_request_tatus), task.getException());
                                                }
                                            });
                                });
                    } else {
                        Toast.makeText(ConversationActivity.this, "Not enough seats available.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to deny a passenger for a trip
    private void denyPassenger() {
        Log.d("TAG", getString(R.string.denypassenger_method_called));
        acceptButton.setVisibility(View.GONE);
        denyButton.setVisibility(View.GONE);
        confirmationText.setText(R.string.you_have_denied_this_passenger);
        confirmationText.setVisibility(View.VISIBLE);

        // Update trip request status to "denied"
        String tripId = getIntent().getStringExtra("TRIP_ID");
        String conversationId = getIntent().getStringExtra("CONVERSATION_ID");
        FirebaseFirestore.getInstance().collection("conversations").document(conversationId)
                .get().addOnSuccessListener(documentSnapshot -> {
                    Conversation conversation = documentSnapshot.toObject(Conversation.class);
                    String otherUserId = conversation.getOtherUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    FirebaseFirestore.getInstance().collection("tripRequests")
                            .whereEqualTo("tripId", tripId)
                            .whereEqualTo("requesterId", otherUserId)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().update("status", "denied");
                                    }
                                } else {
                                    Log.w("TAG", getString(R.string.error_updating_trip_request_status), task.getException());
                                }
                            });
                });
    }}