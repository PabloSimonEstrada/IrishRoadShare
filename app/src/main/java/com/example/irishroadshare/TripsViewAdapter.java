// Package declaration
package com.example.irishroadshare;

// Importing necessary libraries
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;


public class TripsViewAdapter extends RecyclerView.Adapter<TripsViewAdapter.ViewHolder> {

    // Array list holding the data for the trips.
    private ArrayList<DocumentSnapshot> mTripsData;

    // Context of the application.
    private Context mContext;

    // Constructor for the TripsViewAdapter.
    public TripsViewAdapter(Context context, ArrayList<DocumentSnapshot> trips) {
        this.mTripsData = trips;
        this.mContext = context;
    }

    // Class representing the ViewHolder for the RecyclerView.
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Variables representing the UI components of each trip item in the RecyclerView.
        public TextView date;
        public TextView departure;
        public TextView destination;
        public TextView priceValue;
        public TextView seatsAvailable;
        public TextView time;
        public TextView username;

        // Constructor for the ViewHolder.
        public ViewHolder(View itemView) {
            super(itemView);

            // Find the UI components within the itemView.
            date = itemView.findViewById(R.id.date);
            departure = itemView.findViewById(R.id.departure);
            destination = itemView.findViewById(R.id.destination);
            priceValue = itemView.findViewById(R.id.priceValue);
            seatsAvailable = itemView.findViewById(R.id.seatsAvailable);
            time = itemView.findViewById(R.id.time);
            username = itemView.findViewById(R.id.username);

            // Set a click listener for the itemView.
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    DocumentSnapshot clickedTrip = mTripsData.get(position);
                    Intent intent = new Intent(mContext, TripDetailsActivity.class);
                    intent.putExtra("TRIP_ID", clickedTrip.getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    // Function called when the RecyclerView needs a new ViewHolder of the given type to represent an item.
    @Override
    public TripsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout for a trip item.
        View contactView = inflater.inflate(R.layout.trip_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Function called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(TripsViewAdapter.ViewHolder viewHolder, int position) {
        DocumentSnapshot trip = mTripsData.get(position);

        // Set the text for the UI components in the ViewHolder.
        viewHolder.date.setText(trip.getString("date"));
        viewHolder.departure.setText(trip.getString("departure"));
        viewHolder.destination.setText(trip.getString("destination"));
        viewHolder.priceValue.setText(String.format("%s€", trip.getString("price")));
        viewHolder.seatsAvailable.setText(trip.getString("seatsAvailable"));
        viewHolder.time.setText(trip.getString("time"));
        viewHolder.username.setText(trip.getString("username"));
    }

    // Function returning the total number of items in the data set held by the adapter.
    @Override
    public int getItemCount() {
        return mTripsData.size();
    }
}