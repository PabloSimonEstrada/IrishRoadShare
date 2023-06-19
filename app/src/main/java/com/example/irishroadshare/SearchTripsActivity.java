// Package declaration
package com.example.irishroadshare;

// Importing necessary libraries
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.app.DatePickerDialog;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class SearchTripsActivity extends AppCompatActivity {

    private String departure;  // To hold the selected departure location
    private String destination; // To hold the selected destination location
    private Button dateButton; // Button for picking a date
    private Button searchButton; // Button for triggering the search

    private Calendar calendar = Calendar.getInstance(); // Calendar instance to keep track of the selected date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_trips);

        // Initialize Places API
        Places.initialize(getApplicationContext(), getString(R.string.API_key));

        // Setup the Autocomplete fragment for the departure location
        AutocompleteSupportFragment departureAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.departure_autocomplete_fragment);

        if (departureAutocompleteFragment != null) {
            departureAutocompleteFragment.setHint(getString(R.string.search_departure));
            departureAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            departureAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    // Set the selected departure location
                    departure = place.getName();
                }

                @Override
                public void onError(@NonNull Status status) {
                    // Handle the error
                }
            });
        }

        // Setup the Autocomplete fragment for the destination location
        AutocompleteSupportFragment destinationAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.destination_autocomplete_fragment);

        if (destinationAutocompleteFragment != null) {
            destinationAutocompleteFragment.setHint(getString(R.string.search_destination));
            destinationAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            destinationAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    // Set the selected destination location
                    destination = place.getName();
                }

                @Override
                public void onError(@NonNull Status status) {
                    // Handle the error
                }
            });
        }

        // Get the references to the dateButton and searchButton
        dateButton = findViewById(R.id.dateButton);
        searchButton = findViewById(R.id.searchButton);

        // Setup the navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_post_trip) {
                Intent homeIntent = new Intent(SearchTripsActivity.this, PostTripActivity.class);
                startActivity(homeIntent);
            } else if (id == R.id.navigation_search) {
                return true;
            } else if (id == R.id.navigation_message) {
                Intent notificationsIntent = new Intent(SearchTripsActivity.this, MessagesActivity.class);
                startActivity(notificationsIntent);
            } else {
                return false;
            }
            return true;
        });

        // Setup the DatePickerDialog to select a date
        final DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            // Set the selected date in the calendar
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Update the dateButton's text to the selected date
            updateLabel();
        };

        dateButton.setOnClickListener(v -> {
            // Show the DatePickerDialog
            new DatePickerDialog(SearchTripsActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        searchButton.setOnClickListener(v -> {
            // Get the date from the dateButton
            String date = dateButton.getText().toString();

            // Create a new SearchTripsFragment with the selected departure, destination, and date
            SearchTripsFragment searchTripsFragment = SearchTripsFragment.newInstance(departure, destination, date);

            // Replace the current fragment with the new SearchTripsFragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, searchTripsFragment)
                    .commit();
        });
    }

    private void updateLabel() {
        // Format for displaying the date
        String format = "d/M/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        // Update the dateButton's text to the selected date
        dateButton.setText(sdf.format(calendar.getTime()));
    }
}