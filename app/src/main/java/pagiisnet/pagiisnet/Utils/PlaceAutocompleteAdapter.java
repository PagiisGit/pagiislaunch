package pagiisnet.pagiisnet.Utils;

/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/**
 * Adapter that handles Autocomplete requests from the Places Geo Data Client.
 * {@link AutocompletePrediction} results from the API are frozen and stored directly in this
 * adapter. (See {@link AutocompletePrediction#freeze()}.)
 */
public class PlaceAutocompleteAdapter
        extends ArrayAdapter<AutocompletePrediction> implements Filterable {

    private static final String TAG = "PlaceAutocompleteAdapter";
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    /**
     * Handles autocomplete requests.
     */
    private final GoogleApiClient mGoogleApiClient;
    /**
     * The autocomplete filter used to restrict queries to a specific set of place types.
     */
    private final AutocompleteFilter mPlaceFilter;
    /**
     * Current results returned by this adapter.
     */
    private ArrayList<AutocompletePrediction> mResultList;
    /**
     * The bounds used for Places Geo Data autocomplete API requests.
     */
    private LatLngBounds mBounds;


    /**
     * Initializes with a resource for text rows and autocomplete query bounds.
     *
     * @see android.widget.ArrayAdapter#ArrayAdapter(android.content.Context, int)
     */
    public PlaceAutocompleteAdapter(Context context, GoogleApiClient geoDataClient,
                                    LatLngBounds bounds, AutocompleteFilter filter) {
        super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1);
        mGoogleApiClient = geoDataClient;
        mBounds = bounds;
        mPlaceFilter = filter;


    }


    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }


    /**
     * Returns the number of results received in the last autocomplete query.
     */
    @Override
    public int getCount() {
        return mResultList.size();
    }

    /**
     * Returns an item from the last autocomplete query.
     */
    @Override
    public AutocompletePrediction getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);

        // Sets the primary and secondary text for a row.
        // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
        // styling based on the given CharacterStyle.

        AutocompletePrediction item = getItem(position);

        TextView textView1 = row.findViewById(android.R.id.text1);
        TextView textView2 = row.findViewById(android.R.id.text2);
        textView1.setText(item.getPrimaryText(STYLE_BOLD));
        textView2.setText(item.getSecondaryText(STYLE_BOLD));

        return row;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                // We need a separate list to store the results, since
                // this is run asynchronously.
                ArrayList<AutocompletePrediction> filterData = new ArrayList<>();

                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    filterData = (ArrayList<AutocompletePrediction>) getAutocomplete(constraint);
                }

                results.values = filterData;
                if (filterData != null) {
                    results.count = filterData.size();
                } else {
                    results.count = 0;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    mResultList = (ArrayList<AutocompletePrediction>) results.values;
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                if (resultValue instanceof AutocompletePrediction) {
                    return ((AutocompletePrediction) resultValue).getFullText(null);
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }

    /**
     * Submits an autocomplete query to the Places Geo Data Autocomplete API.
     * Results are returned as frozen AutocompletePrediction objects, ready to be cached.
     * Returns an empty list if no results were found.
     * Returns null if the API client is not available or the query did not complete
     * successfully.
     * This method MUST be called off the main UI thread, as it will block until data is returned
     * from the API, which may include a network request.
     *
     * @param constraint Autocomplete query string
     * @return Results from the autocomplete API or null if the query was not successful.
     * @see GeoDataClient#getAutocompletePredictions(String, LatLngBounds, AutocompleteFilter)
     * @see AutocompletePrediction#freeze()
     */


    @SuppressLint("LongLogTag")
    private DataBuffer<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        Log.i(TAG, "Starting autocomplete query for: " + constraint);

        // Submit the query to the autocomplete API and retrieve a PendingResult that will
        // contain the results when the query completes.

        //laces.initialize(getApplicationContext(), "YOUR_API_KEY");

        Task<AutocompletePredictionBufferResponse> results =
                Places.getGeoDataClient(getContext()).getAutocompletePredictions(constraint.toString(), mBounds,
                        mPlaceFilter);

// This method should have been called off the main UI thread. Block and wait for at most
// 60s for a result from the API.
        try {
            Tasks.await(results, 60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }


        try {

            @SuppressLint("RestrictedApi") PlacesClient placesClient = com.google.android.libraries.places.api.Places.createClient(getApplicationContext());

            // Assuming you have initialized Places before this point.
            //PlacesClient placesClient = PlacesClient.newInstance(getContext());


            // AutocompletePredictionBufferResponse autocompletePredictions = results.getResult();

            //Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount()
            //    + " predictions.");

            // Freeze the results immutable representation that can be stored safely.
            //DataBuffer<AutocompletePrediction> predictions = (DataBuffer<AutocompletePrediction>) DataBufferUtils.freezeAndClose(autocompletePredictions);

            // Assuming the user selected the first prediction, get its place ID
            //String placeId = predictions.get(0).getPlaceId();


            // Assuming you have initialized the Places API earlier in your code.

            // Define the fields you want to retrieve in the predictions
            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);

            // Create the Autocomplete request
            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery("Your search query") // Replace with the user's input
                    .setCountries("US") // Optionally limit results to a specific country
                    .build();

            // Use the PlacesClient to get predictions
            placesClient.findAutocompletePredictions(request).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindAutocompletePredictionsResponse response = task.getResult();
                    if (response != null) {
                        List<com.google.android.libraries.places.api.model.AutocompletePrediction> predictions = response.getAutocompletePredictions();
                        // Process the list of predictions
                        for (com.google.android.libraries.places.api.model.AutocompletePrediction prediction : predictions) {
                            Log.i("Prediction Info", "Place ID: " + prediction.getPlaceId());
                            Log.i("Prediction Info", "Primary Text: " + prediction.getPrimaryText(null));
                            Log.i("Prediction Info", "Secondary Text: " + prediction.getSecondaryText(null));
                        }
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("Autocomplete Error", "Status: " + apiException.getStatusCode());
                        Log.e("Autocomplete Error", "Message: " + apiException.getMessage());
                    }
                }
            });


            // Create a FetchPlaceRequest
            //List<Place.Field> placeFields1 = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS);
            //FetchPlaceRequest request1 = FetchPlaceRequest.builder(placeId, placeFields).build();

           /* placesClient.fetchPlace(request).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FetchPlaceResponse response = task.getResult();
                    Place fetchedPlace = response.getPlace();
                    Log.i("Fetched Place Details", "Name: " + fetchedPlace.getName());
                    Log.i("Fetched Place Details", "Address: " + fetchedPlace.getAddress());

                    // Add code to process the fetched place details here

                } else {
                    Exception exception = task.getException();
                    Toast.makeText(getContext(), "Error fetching place details: " + exception.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error fetching place details: " + exception.getMessage(), exception);
                }
            });*/

            // ;

        } catch (RuntimeExecutionException e) {
            // If the query did not complete successfully return null
            Toast.makeText(getContext(), "Error contacting API: " + e,
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error getting autocomplete prediction API call" + e.getMessage(), e);
            return null;
        }
        return null;
    }
}

