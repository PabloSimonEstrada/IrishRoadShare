// Package declaration
package com.example.irishroadshare;

// Importing necessary libraries
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class SearchTripsFragment extends Fragment {

    private ArrayList<DocumentSnapshot> mTripsData = new ArrayList<>();  // To hold the list of trips fetched from Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();  // Firestore database instance
    private String departure;  // To hold the selected departure location
    private String destination; // To hold the selected destination location
    private String date; // To hold the selected date

    // Factory method to create a new instance of this fragment using the provided parameters
    public static SearchTripsFragment newInstance(String departure, String destination, String date) {
        SearchTripsFragment fragment = new SearchTripsFragment();
        Bundle args = new Bundle();
        args.putString("departure", departure);
        args.putString("destination", destination);
        args.putString("date", date);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchTripsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Retrieve the departure, destination, and date from the arguments
            departure = getArguments().getString("departure");
            destination = getArguments().getString("destination");
            date = getArguments().getString("date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_trips, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.trips_recycler_view);
        // Fetch the filtered trips from Firestore and display them in the RecyclerView
        getFilteredTrips(departure, destination, date, recyclerView);

        return root;
    }

    // Method to fetch the filtered trips from Firestore
    private void getFilteredTrips(String departure, String destination, String date, RecyclerView recyclerView) {
        CollectionReference tripsRef = db.collection("trips");
        Query query = tripsRef;

        // Add a filter to the query for the departure location if it's not empty
        if (departure != null && !departure.isEmpty()) {
            query = query.whereEqualTo("departure", departure);
        }

        // Add a filter to the query for the destination location if it's not empty
        if (destination != null && !destination.isEmpty()) {
            query = query.whereEqualTo("destination", destination);
        }

        // Add a filter to the query for the date if it's not empty
        if (date != null && !date.isEmpty()) {
            query = query.whereEqualTo("date", date);
        }

        // Execute the query
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Clear the old data
                mTripsData.clear();
                // Add each document in the query results to the trips list
                for (QueryDocumentSnapshot document : task.getResult()) {
                    mTripsData.add(document);
                }

                // Set the adapter for the RecyclerView with the new data
                TripsViewAdapter recyclerViewAdapter = new TripsViewAdapter(getContext(), mTripsData);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                // Log an error if the query was not successful
                Log.e("Firebase", getString(R.string.error_getting_documents), task.getException());
            }
        });
    }
}