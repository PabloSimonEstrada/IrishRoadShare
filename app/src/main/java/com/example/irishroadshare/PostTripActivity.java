// Package declaration
package com.example.irishroadshare;

// Importing necessary libraries
import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostTripActivity extends AppCompatActivity {
    // Declare instance variables
    private String departurePlaceName;
    private String destinationPlaceName;
    private com.google.android.gms.maps.model.LatLng departureLatLng;
    private LatLng destinationLatLng;
    private String selectedDate;
    private String selectedTime;
    private Calendar selectedDateCalendar, selectedTimeCalendar;

    // Declare Firebase Firestore and Authentication instances
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the user interface layout for this Activity
        setContentView(R.layout.activity_post_trip);

        // Instantiate Bottom Navigation View and set up the item selected listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_post_trip) {
                return true;
            } else if (id == R.id.navigation_search) {
                // Transition to SearchTripsActivity if the search option is selected
                Intent homeIntent = new Intent(PostTripActivity.this, SearchTripsActivity.class);
                startActivity(homeIntent);
                finish();
            } else if (id == R.id.navigation_message) {
                // Transition to MessagesActivity if the message option is selected
                Intent notificationsIntent = new Intent(PostTripActivity.this, MessagesActivity.class);
                startActivity(notificationsIntent);
                finish();
            } else {
                return false;
            }
            return true;
        });

        // Set the current item in Bottom Navigation View as post trip
        bottomNavigationView.setSelectedItemId(R.id.navigation_post_trip);

        // Initialize Google Places
        Places.initialize(getApplicationContext(), getString(R.string.API_key));

        // Setup AutocompleteSupportFragment for Departure
        AutocompleteSupportFragment departureAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.departure_autocomplete_fragment);

        // Check if the fragment is not null
        if (departureAutocompleteFragment != null) {
            departureAutocompleteFragment.setHint(getString(R.string.select_departure));
            departureAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            // Set the place selected listener for departure place
            departureAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    // Handle the selected Departure place
                    departurePlaceName = place.getName();
                    departureLatLng = place.getLatLng();
                }

                @Override
                public void onError(@NonNull Status status) {
                }
            });
        }

        // Setup AutocompleteSupportFragment for Destination
        AutocompleteSupportFragment destinationAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.destination_autocomplete_fragment);

        // Check if the fragment is not null
        if (destinationAutocompleteFragment != null) {
            destinationAutocompleteFragment.setHint(getString(R.string.select_destination));
            destinationAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            // Set the place selected listener for destination place
            destinationAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    // Handle the selected Destination place
                    destinationPlaceName = place.getName();
                    destinationLatLng = place.getLatLng();
                }

                @Override
                public void onError(@NonNull Status status) {
                }
            });
        }

        // Get references to the other UI Components in the layout
        Button btnDate = findViewById(R.id.btnDate);
        Button btnTime = findViewById(R.id.btnTime);
        Button btnPostTrip = findViewById(R.id.btnPostTrip);

        EditText etSeatsAvailable = findViewById(R.id.etSeatsAvailable);
        EditText etPrice = findViewById(R.id.etPrice);
        Spinner spLuggageSize = findViewById(R.id.spLuggageSize);
        CheckBox cbSmoking = findViewById(R.id.cbSmoking);
        CheckBox cbPets = findViewById(R.id.cbPets);
        EditText etCarMake = findViewById(R.id.etCarMake);
        EditText etCarModel = findViewById(R.id.etCarModel);
        EditText etLicensePlate = findViewById(R.id.etLicensePlate);
        EditText etAdditionalComments = findViewById(R.id.etAdditionalComments);

        // Setup the date picker
        btnDate.setOnClickListener(v -> {
            // Initialize a new DatePickerDialog instance
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    PostTripActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDateCalendar = Calendar.getInstance();  // Create a new Calendar object for the selected date
                        selectedDateCalendar.set(year, month, dayOfMonth);  // Set the selected date to this Calendar object
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year; // Format the selected date
                        btnDate.setText(selectedDate); // Set the button text to the selected date
                    },
                    Calendar.getInstance().get(Calendar.YEAR), // Get the current year
                    Calendar.getInstance().get(Calendar.MONTH), // Get the current month
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH) // Get the current day of the month
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Set the minimum selectable date to the current date
            datePickerDialog.show(); // Show the date picker dialog
        });

        // Setup the time picker
        btnTime.setOnClickListener(v -> {
            // Initialize a new TimePickerDialog instance
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    PostTripActivity.this,
                    (view, hourOfDay, minute) -> {
                        selectedTimeCalendar = (Calendar) selectedDateCalendar.clone();
                        selectedTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTimeCalendar.set(Calendar.MINUTE, minute);

                        Calendar currentCalendar = Calendar.getInstance();

                        // Check if the selected time is in the past
                        if (selectedTimeCalendar.before(currentCalendar)) {
                            Toast.makeText(PostTripActivity.this, "You can't select a past time", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        selectedTime = hourOfDay + ":" + minute;
                        btnTime.setText(selectedTime); // Set the button text to the selected time
                    },
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY), // Get the current hour
                    Calendar.getInstance().get(Calendar.MINUTE), // Get the current minute
                    true
            );
            timePickerDialog.show(); // Show the time picker dialog
        });

        // Setup the listener for the post trip button
        btnPostTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all the fields are filled in
                if (departurePlaceName == null || destinationPlaceName == null || selectedDate == null || selectedTime == null ||
                        etSeatsAvailable.getText().toString().trim().isEmpty() || etPrice.getText().toString().trim().isEmpty() ||
                        spLuggageSize.getSelectedItem().toString().trim().isEmpty() || etCarMake.getText().toString().trim().isEmpty() ||
                        etCarModel.getText().toString().trim().isEmpty() || etLicensePlate.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PostTripActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the user is logged in
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();

                    // Fetch the user's information from Firestore
                    db.collection("users").document(userId)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        // Fetch the username and user ID from the document
                                        String username = document.getString("username");
                                        String userId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        // Create a map to hold the trip information
                                        Map<String, Object> trip = new HashMap<>();
                                        trip.put("username", username);
                                        trip.put("userId", userId1);
                                        trip.put("departure", departurePlaceName);
                                        trip.put("departureLat", String.valueOf(departureLatLng.latitude));
                                        trip.put("departureLng", String.valueOf(departureLatLng.longitude));
                                        trip.put("destination", destinationPlaceName);
                                        trip.put("destinationLat", String.valueOf(destinationLatLng.latitude));
                                        trip.put("destinationLng", String.valueOf(destinationLatLng.longitude));
                                        trip.put("date", selectedDate);
                                        trip.put("time", selectedTime);
                                        trip.put("seatsAvailable", etSeatsAvailable.getText().toString());
                                        trip.put("price", etPrice.getText().toString());
                                        trip.put("luggageSize", spLuggageSize.getSelectedItem().toString());
                                        trip.put("smokingAllowed", cbSmoking.isChecked());
                                        trip.put("petsAllowed", cbPets.isChecked());
                                        trip.put("carMake", etCarMake.getText().toString());
                                        trip.put("carModel", etCarModel.getText().toString());
                                        trip.put("licensePlate", etLicensePlate.getText().toString());
                                        trip.put("additionalComments", etAdditionalComments.getText().toString());

                                        // Add the trip information to Firestore
                                        db.collection("trips")
                                                .add(trip)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        // Handle the successful addition of the trip
                                                        Toast.makeText(PostTripActivity.this, "Trip posted successfully!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(PostTripActivity.this, MainMenuActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle the failure to add the trip
                                                    Toast.makeText(PostTripActivity.this, "Failed to post trip", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        Log.d(TAG, getString(R.string.no_such_document));
                                    }
                                } else {
                                    Log.d(TAG, getString(R.string.get_failed_with), task.getException());
                                }
                            });
                }
            }
        });
    }
}