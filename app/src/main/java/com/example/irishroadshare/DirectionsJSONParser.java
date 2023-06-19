// Package declaration
package com.example.irishroadshare;

// Importing necessary libraries
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Declare DirectionsJSONParser class
public class DirectionsJSONParser {

    // Method to parse the JSON Object for routes
    public List<List<HashMap<String,String>>> parse(JSONObject jObject) {

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>();
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {
            // Get the array of routes from the JSON object
            jRoutes = jObject.getJSONArray("routes");

            // Loop through all routes
            for(int i=0;i<jRoutes.length();i++) {
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                // Loop through all legs of the route
                for(int j=0;j<jLegs.length();j++) {
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    // Loop through all steps of the leg
                    for(int k=0;k<jSteps.length();k++) {
                        String polyline = "";
                        // Get the polyline for the step
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        // Decode the polyline into a list of LatLng
                        List<LatLng> list = decodePoly(polyline);

                        // Loop through all points in the polyline
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            // Add the latitude and longitude of the point to the HashMap
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            // Add the HashMap to the path
                            path.add(hm);
                        }
                    }
                    // Add the path to the routes
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            // Handle any other exceptions
        }

        // Return the list of routes
        return routes;
    }

    // Method to decode a polyline
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        // Decoding the polyline
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            // Create a LatLng from the decoded polyline
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            // Add the LatLng to the list
            poly.add(p);
        }

        // Return the list of LatLng
        return poly;
    }
}