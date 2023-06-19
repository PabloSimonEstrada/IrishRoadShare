// Package declaration
package com.example.irishroadshare;

// Importing necessary libraries
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.Http;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Client for interacting with the fused location provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // Firebase Firestore and Auth instances.
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private GoogleMap mMap;

    // UI elements.
    private Button requestButton;
    private TextView seatsToBookTextView, seatsAvailableTextView, carBrandTextView, carModelTextView, priceTextView, additionalComments;
    private ImageView luggageImageView, smokingImageView, petsImageView, carImageView, calendarImageView;
    private String tripOwnerId;

    private ImageButton minusButton;
    private ImageButton plusButton;
    private int seatsToBook = 1, seatsAvailable;

    private DocumentSnapshot tripDocument;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final float DEFAULT_ZOOM = 15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        // Initialize Firestore and FirebaseAuth instances.
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Retrieve the trip ID from the intent.
        String tripId = getIntent().getStringExtra("TRIP_ID");

        // Initialize UI elements.
        minusButton = findViewById(R.id.minusButton);
        plusButton = findViewById(R.id.plusButton);
        requestButton = findViewById(R.id.requestButton);
        seatsToBookTextView = findViewById(R.id.seatsToBook);
        seatsAvailableTextView = findViewById(R.id.seatsAvailable);
        luggageImageView = findViewById(R.id.luggageImageView);
        smokingImageView = findViewById(R.id.smokingImageView);
        petsImageView = findViewById(R.id.petsImageView);
        carImageView = findViewById(R.id.carImageView);
        calendarImageView = findViewById(R.id.calendarImageView);
        carBrandTextView = findViewById(R.id.carBrand);
        carModelTextView = findViewById(R.id.carModel);
        priceTextView = findViewById(R.id.priceTextView);
        additionalComments = findViewById(R.id.additionalComments);

        TextView routeTextView = findViewById(R.id.route);
        TextView dateTimeTextView = findViewById(R.id.dateTime);
        TextView driverTextView = findViewById(R.id.driver);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);

        // Setting up Bottom Navigation View Listener.
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_post_trip) {
                Intent homeIntent = new Intent(TripDetailsActivity.this, PostTripActivity.class);
                startActivity(homeIntent);
            } else if (id == R.id.navigation_search) {
                Intent dashboardIntent = new Intent(TripDetailsActivity.this, SearchTripsActivity.class);
                startActivity(dashboardIntent);
            } else if (id == R.id.navigation_message) {
                Intent notificationsIntent = new Intent(TripDetailsActivity.this, MessagesActivity.class);
                startActivity(notificationsIntent);
            } else {
                return false;
            }
            return true;
        });

        // Initialize Map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Fetch the trip details from Firestore.
        db.collection("trips").document(tripId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                tripDocument = task.getResult();

                seatsAvailable = Integer.parseInt(tripDocument.getString("seatsAvailable"));
                tripOwnerId = tripDocument.getString("userId");

                // Setting up the UI with fetched data.
                routeTextView.setText(String.format("%s%s%s", tripDocument.getString("departure"), getString(R.string.arrow), tripDocument.getString("destination")));
                dateTimeTextView.setText(String.format("%s at %s", tripDocument.getString("date"), tripDocument.getString("time")));
                driverTextView.setText(String.format("Driver: %s", tripDocument.getString("username")));
                seatsAvailableTextView.setText(tripDocument.getString("seatsAvailable"));
                carBrandTextView.setText(tripDocument.getString("carMake"));
                carModelTextView.setText(tripDocument.getString("carModel"));
                additionalComments.setText(tripDocument.getString("additionalComments"));

                // Get the coordinates
                String departureLat = tripDocument.getString("departureLat");
                String departureLng = tripDocument.getString("departureLng");
                String destinationLat = tripDocument.getString("destinationLat");
                String destinationLng = tripDocument.getString("destinationLng");

                new DownloadTask().execute(getDirectionsUrl(departureLat, departureLng, destinationLat, destinationLng));
                String price = tripDocument.getString("price");
                priceTextView.setText(String.format("Price: %s€", price));

                String luggageSize = tripDocument.getString("luggageSize");
                boolean smokingAllowed = tripDocument.getBoolean("smokingAllowed");
                boolean petsAllowed = tripDocument.getBoolean("petsAllowed");

                // UI logic to manage luggage, smoking, and pet permissions.
                if (luggageSize.equals(getString(R.string.small_luggage))) {
                    luggageImageView.setImageResource(R.drawable.luggage_small);
                } else if (luggageSize.equals(getString(R.string.medium_luggage))) {
                    luggageImageView.setImageResource(R.drawable.luggage_medium);
                } else {
                    luggageImageView.setImageResource(R.drawable.luggage_large);
                }

                if (smokingAllowed) {
                    smokingImageView.setImageResource(R.drawable.smoking_allowed);
                } else {
                    smokingImageView.setImageResource(R.drawable.smoking_not_allowed);
                }

                if (petsAllowed) {
                    petsImageView.setImageResource(R.drawable.pets_allowed);
                } else {
                    petsImageView.setImageResource(R.drawable.pets_not_allowed);
                }

                // Set a static car image.
                carImageView.setImageResource(R.drawable.car_icon);
                calendarImageView.setImageResource(R.drawable.calendar_icon);
            }
        });

        // Set onClick listeners for the buttons.
        minusButton.setOnClickListener(v -> {
            seatsToBook = Math.max(1, seatsToBook - 1);
            seatsToBookTextView.setText(String.valueOf(seatsToBook));
        });

        plusButton.setOnClickListener(v -> {
            seatsToBook = Math.min(seatsAvailable, seatsToBook + 1);
            seatsToBookTextView.setText(String.valueOf(seatsToBook));
        });

        requestButton.setOnClickListener(v -> {
            // Validate the number of seats available before proceeding.
            if (seatsToBook > seatsAvailable) {
                Toast.makeText(TripDetailsActivity.this, "Not enough seats available", Toast.LENGTH_SHORT).show();
                return;
            }

            // Prepare the trip request data.
            Map<String, Object> tripRequest = new HashMap<>();
            tripRequest.put("requesterId", auth.getUid());
            tripRequest.put("tripId", tripId);
            tripRequest.put("status", "pending");
            tripRequest.put("seatsToBook", seatsToBook);

            // Send a trip request and start a conversation with the trip owner on success.
            db.collection("tripRequests").add(tripRequest)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(TripDetailsActivity.this, "Trip request successful", Toast.LENGTH_SHORT).show();

                        Map<String, Object> conversation = new HashMap<>();
                        conversation.put("tripId", tripId);
                        conversation.put("requesterId", auth.getUid());
                        conversation.put("tripOwnerId", tripOwnerId);
                        conversation.put("lastMessage", "You have requested a trip.");
                        conversation.put("timestamp", new Date());

                        // Use the tripDocument to fill the conversation data.
                        conversation.put("departure", tripDocument.getString("departure"));
                        conversation.put("destination", tripDocument.getString("destination"));
                        conversation.put("price", tripDocument.getString("price"));
                        conversation.put("date", tripDocument.getString("date"));
                        conversation.put("time", tripDocument.getString("time"));

                        conversation.put("seatsToBook", seatsToBook);

                        db.collection("conversations").add(conversation)
                                .addOnSuccessListener(conversationDocument -> {
                                    Intent intent = new Intent(TripDetailsActivity.this, ConversationActivity.class);
                                    intent.putExtra("CONVERSATION_ID", conversationDocument.getId());
                                    intent.putExtra("TRIP_ID", tripId);
                                    intent.putExtra("SEATS_TO_BOOK", seatsToBook);
                                    startActivity(intent);
                                });
                    })
                    .addOnFailureListener(e -> Toast.makeText(TripDetailsActivity.this, "Trip request failed", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permission before enabling user location on the map.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            requestLocationUpdates();
        } else {
            // Request location permission if not granted.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Function to request location updates.
    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }

                    // Fetch the last known location and convert it to a LatLng object.
                    Location location = locationResult.getLastLocation();
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }, Looper.getMainLooper());
        }
    }

    // Callback for the result from requesting permissions.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    requestLocationUpdates();
                }
            } else {
                // Notify user that location permission is needed for the location functionality to work.
                Toast.makeText(this, "The location function needs location permissions to work", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Function to get the Google Directions URL for the given origin and destination coordinates.
    private String getDirectionsUrl(String depLat, String depLng, String destLat, String destLng) {
        // Convert the coordinates to LatLng
        LatLng originLatLng = new LatLng(Double.parseDouble(depLat), Double.parseDouble(depLng));
        LatLng destLatLng = new LatLng(Double.parseDouble(destLat), Double.parseDouble(destLng));

        String str_origin = "origin=" + originLatLng.latitude + "," + originLatLng.longitude;
        String str_dest = "destination=" + destLatLng.latitude + "," + destLatLng.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = getString(R.string.https_maps_googleapis_com_maps_api_directions) + output + "?" + parameters +  "&key=AIzaSyDagxyAch91-wbJbpvLWcjhJIQY-6rZ6Fw";
        return url;
    }

    // AsyncTask to download data from the Google Directions API.
    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                URL myUrl = new URL(url[0]);
                HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
                conn.connect();

                InputStream stream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                data = buffer.toString();
                reader.close();

            } catch (Exception e) {
                Log.d(getString(R.string.exception_downloading), e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Execute ParserTask on the downloaded data.
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    // AsyncTask to parse the data from the Google Directions API.
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Start parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            // Traverse through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetch i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetch all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                    builder.include(position);
                }

                // Add all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Draw polyline in the Google Map
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }

            // The following will zoom the map to the route
            int padding = 100;
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cu);
        }
    }
}