// Package declaration
package com.example.irishroadshare;

// Import necessary libraries and classes
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

// ConversationsAdapter class, which is an adapter for a RecyclerView
public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ViewHolder> {

    // Class level variables to hold the Context and List of Conversations
    private Context mContext;
    private List<Conversation> mConversations;

    // Constructor for ConversationsAdapter class
    public ConversationsAdapter(Context context, List<Conversation> conversations) {
        this.mContext = context;
        this.mConversations = conversations;
    }

    // Method to create ViewHolder objects
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout and create a ViewHolder instance
        View view = LayoutInflater.from(mContext).inflate(R.layout.conversation_item, parent, false);
        return new ViewHolder(view);
    }

    // Method to bind ViewHolder objects to their data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the Conversation at the current position
        Conversation conversation = mConversations.get(position);

        // Set the TextViews with the appropriate Conversation data
        holder.lastMessage.setText(conversation.getLastMessage());
        holder.timestamp.setText(conversation.getTimestamp().toString());
        holder.departure.setText(conversation.getDeparture());
        holder.destination.setText(conversation.getDestination());
        holder.price.setText("Price: " + conversation.getPrice() + "€");
        holder.date.setText(conversation.getDate());
        holder.time.setText(conversation.getTime());

        // Get number of seats to book and user IDs
        int seatsToBook = conversation.getSeatsToBook();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String otherUserId = conversation.getOtherUserId(currentUserId);

        // Fetch other user's data from Firebase Firestore and set sender name
        FirebaseFirestore.getInstance().collection("users").document(otherUserId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String otherUserName = documentSnapshot.getString("username");
                        holder.senderName.setText(otherUserName);
                    }
                });

        // Set an onClickListener for the itemView which starts a new ConversationActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ConversationActivity.class);
            intent.putExtra("CONVERSATION_ID", conversation.getId());
            intent.putExtra("TRIP_ID", conversation.getTripId());
            intent.putExtra("SEATS_TO_BOOK", seatsToBook);
            mContext.startActivity(intent);
        });
    }

    // Method to return the size of the Conversations list
    @Override
    public int getItemCount() {
        return mConversations.size();
    }

    // Inner ViewHolder class definition
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Class level TextView variables
        public TextView lastMessage;
        public TextView timestamp;
        public TextView departure;
        public TextView destination;
        public TextView price;
        public TextView date;
        public TextView time;
        public TextView senderName;

        // Constructor for ViewHolder class
        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize TextViews by finding them in the itemView
            lastMessage = itemView.findViewById(R.id.last_message);
            timestamp = itemView.findViewById(R.id.timestamp);
            departure = itemView.findViewById(R.id.departure);
            destination = itemView.findViewById(R.id.destination);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            senderName = itemView.findViewById(R.id.sender_name);
        }
    }
}