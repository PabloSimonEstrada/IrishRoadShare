// Package declaration
package com.example.irishroadshare;

// Importing necessary libraries
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.List;

// Declare MessagesAdapter, an extension of RecyclerView.Adapter
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    // Constants for message types
    private static final int MESSAGE_TYPE_SENT = 0;
    private static final int MESSAGE_TYPE_RECEIVED = 1;

    // Declare context, list of messages and user ID
    private Context mContext;
    private List<Message> mMessages;
    private String currentUserId;

    // Constructor for MessagesAdapter
    public MessagesAdapter(Context context, List<Message> messages) {
        this.mContext = context;
        this.mMessages = messages;
        // Get the user ID of the current user
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    // Method to create a new ViewHolder
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        // Inflate the appropriate layout based on the message type
        if (viewType == MESSAGE_TYPE_SENT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.message_item_sent, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.message_item_received, parent, false);
        }

        return new MessageViewHolder(view);
    }

    @Override
    // Method to bind the ViewHolder with data
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // Get the message at the given position
        Message message = mMessages.get(position);

        // Set the message content and sender
        holder.messageContent.setText(message.getContent());
        holder.messageSender.setText(message.getSenderName());

        // Convert the timestamp to a readable format and set it
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formattedTime = sdf.format(message.getTimestamp());
        holder.messageTimestamp.setText(formattedTime);
    }

    @Override
    // Method to get the size of the messages list
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    // Method to get the type of the view at the given position
    public int getItemViewType(int position) {
        // Get the message at the given position
        Message message = mMessages.get(position);

        // Check if the sender is the current user
        if (message.getSenderId().equals(currentUserId)) {
            return MESSAGE_TYPE_SENT;
        } else {
            return MESSAGE_TYPE_RECEIVED;
        }
    }

    // ViewHolder class declaration
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        // Declare TextViews for message content, sender and timestamp
        public TextView messageContent;
        public TextView messageSender;
        public TextView messageTimestamp;

        // Constructor for MessageViewHolder
        public MessageViewHolder(View itemView) {
            super(itemView);

            // Find the corresponding views for TextViews
            messageContent = itemView.findViewById(R.id.message_content);
            messageSender = itemView.findViewById(R.id.message_sender);
            messageTimestamp = itemView.findViewById(R.id.message_timestamp);
        }
    }
}