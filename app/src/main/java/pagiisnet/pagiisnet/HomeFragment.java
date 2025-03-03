package pagiisnet.pagiisnet;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.firebase.ui.auth.AuthUI.TAG;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.varunest.sparkbutton.SparkButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pagiisnet.pagiisnet.Utils.ViewProfilePicsAdapter;
import pagiisnet.pagiisnet.Utils.linksViewAdapter;
import pagiisnet.pagiisnet.Utils.mapSearchedItemAdaptor;
/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ViewProfilePicsAdapter.OnItemClickListener, mapSearchedItemAdaptor.OnItemClickListener, linksViewAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    //the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference mDatabaseRef_Y;

    private static final long DEBOUNCE_DELAY = 300;  // Delay in milliseconds
    private Handler handler = new Handler();
    private Runnable debounceRunnable;
    private DatabaseReference mDatabaseRef_Tokens;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private final int raterBarValue = 0;
    String myName;
    String userStatusMessage;
    String myImageDpUrl;
    private RecyclerView mRecyclerView;
    private RecyclerView searchRecyclerView;
    //private DatabaseReference userRef;
    private ViewProfilePicsAdapter mAdapter;
    private ViewProfilePicsAdapter mAdapterLink;
    private mapSearchedItemAdaptor sAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference getUserProfileDataRef;
    private AppCompatEditText locationInput;
    private ImageView searchForResults;
    private ImageView openSearchbar;
    private ImageView uploadItem;
    private CardView searchInputLayout;
    private TextView searchNotice;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private String getKey;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_x;
    private DatabaseReference mDatabaseRef_y;
    private DatabaseReference mDatabaseRef_z;
    private DatabaseReference lockedImages;
    private ValueEventListener mDBlistener;
    private List<ImageUploads> mUploads;
    private List<ImageUploads> kUploads;
    private FloatingActionButton contentRater;
    private FloatingActionButton contentRaterLink;
    private FloatingActionButton viewUserVideos;
    private Iterator<tags> iterator;
    private List<tags> tagedUsers;
    private List<lockedImagesTag> tagedImages;
    private androidx.appcompat.widget.Toolbar mToolbar;

    private FloatingActionButton goToMaps;
    private DatabaseReference notificationReference;
    private DatabaseReference databaseReference;
    private String notificationTitle;

    private String notificationMessage;

    private CircularImageView pagiisIcon;
    private String isLayoutVisisble = "false";

    private CardView discoverLayoutCard;
    private ArrayAdapter<String> locationAdapter;
    private ListView locationSuggestionsList;

    private ArrayList<String> locationSuggestions;
    private DatabaseReference locationDetails;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

        FirebaseApp.initializeApp(getApplicationContext());

        locationSuggestions = new ArrayList<>();


        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
        getUserProfileDataRef.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance();

        if (mAuth.getCurrentUser() == null) {
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

        }

        mUploads = new ArrayList<>();

        tagedUsers = new ArrayList<>();
        tagedImages = new ArrayList<>();
        kUploads = new ArrayList<>();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");
        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("WalkinWall");

        lockedImages = FirebaseDatabase.getInstance().getReference("Likes");

        //mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("LockedImages");
        databaseReference = FirebaseDatabase.getInstance().getReference("myLastLocation");


        String onlineUserId = mAuth.getCurrentUser().getUid();





        getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)

            {
                if(dataSnapshot.exists())
                {

                    String name = dataSnapshot.child("userNameAsEmail").getValue().toString();
                    String userStatus = dataSnapshot.child("userDefaultStatus").getValue().toString();

                    String myDpUrl = dataSnapshot.child("userImageDp").getValue().toString();
                    userStatusMessage = userStatus;
                    myImageDpUrl = myDpUrl;
                    myName = name;

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        databaseReference.child(onlineUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)

            {
                if(dataSnapshot.exists())
                {

                    String name = dataSnapshot.child("userNameAsEmail").getValue().toString();
                    String userStatus = dataSnapshot.child("userDefaultStatus").getValue().toString();

                    String myDpUrl = dataSnapshot.child("userImageDp").getValue().toString();
                    userStatusMessage = userStatus;
                    myImageDpUrl = myDpUrl;
                    myName = name;

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        if (tagedUsers.isEmpty()) {
            updateArrayListPagiis();
            //siroccoView();

        } else {

            //siroccoView();;

            getPagiisData();
        }
    }



    private static final int MAX_MATCHES = 10; // Maximum number of matches to collect

    private void searchLocations(String query) {
        // Remove any existing pending runnable to prevent multiple queries
        if (debounceRunnable != null) {
            handler.removeCallbacks(debounceRunnable);
        }

        // Delay the Firebase query execution to avoid firing it for every keystroke
        debounceRunnable = new Runnable() {
            @Override
            public void run() {
                performSearch(query);
            }
        };

        // Execute the query after DEBOUNCE_DELAY milliseconds
        handler.postDelayed(debounceRunnable, DEBOUNCE_DELAY);
    }

    private void performSearch(String query) {
        DatabaseReference locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation");
        locationDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locationSuggestions.clear();  // Clear previous suggestions
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String locationKey = ds.getKey();
                    if (locationKey != null) {
                        fetchLocationDetails(locationDetails.child(locationKey), query);
                    }

                    // Stop searching if we already have enough matches
                    if (locationSuggestions.size() >= MAX_MATCHES) {
                        break;
                    }
                }
                updateLocationSuggestionsUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleDatabaseError(databaseError);
            }
        });
    }





    private void fetchLocationDetails(DatabaseReference locationRef, String query) {
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String location = dataSnapshot.getValue(String.class);
                    if (location != null && location.contains(query)) {
                        addLocationIfMatchesQuery(location, query);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                handleDatabaseError(databaseError);
            }
        });
    }


    private void addLocationIfMatchesQuery(String location, String query) {
        String[] locationParts = location.split(",");
        for (String part : locationParts) {
            if (part.trim().equalsIgnoreCase(query.trim())) {
                locationSuggestions.add(location);
                break; // No need to check further parts of this location
            }
        }
    }

    private void updateLocationSuggestionsUI() {
        // If the adapter isn't set yet, do it now
        if (locationAdapter == null) {
            locationAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, locationSuggestions);
            locationSuggestionsList.setAdapter(locationAdapter);
        } else {
            // Ensure that UI update happens on the main thread
            if (getActivity() != null) {
                getActivity().runOnUiThread(() ->locationAdapter.notifyDataSetChanged());
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void handleDatabaseError(DatabaseError databaseError) {
        Toast.makeText(getApplicationContext(), databaseError.getMessage() + "\nLocation does not have active users", Toast.LENGTH_SHORT).show();
    }
     
    private void updateArrayListImages() {
        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("LockedImages");

        String currentUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef_y.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String localIDKey = ds.getKey();

                        String localIDKeyx = ds.child("tag_userImage_id").getValue().toString();

                        tagedImages.add(new lockedImagesTag(localIDKey, localIDKeyx));

                        if (dataSnapshot.getChildrenCount() >= 1 && tagedImages.size() >= 1 | dataSnapshot.getChildrenCount() == tagedImages.size()) {
                            Intent catchup = new Intent(getActivity(), CatchUp.class);
                            startActivity(catchup);
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Tags not found.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void updateArrayList() {
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");

        String currentUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef_x.child(currentUserId).limitToFirst(50).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String localID = ds.getKey();

                        tagedUsers.add(new tags(localID));

                        if (dataSnapshot.getChildrenCount() >= 1 && tagedUsers.size() >= 1 | dataSnapshot.getChildrenCount() == tagedUsers.size()) {

                            getDataNormally();

                            //getDataNormally();

                            //0799506310

                        } else {

                            updateArrayListPagiis();

                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "There are no online users nearby.", Toast.LENGTH_SHORT).show();
                    updateArrayListPagiis();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);

                TextView messageGrid = toastView.findViewById(R.id.customToastText);


                // Initiate the Toast instance.
                Toast toast = new Toast(getActivity());

                messageGrid.setText("Pagiis online user update failed, Please waite while pagiis restarts activity");
                // Set custom view in toast.
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                //Toast.makeText(MapsActivity.this, "Button switched on.", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void updateArrayListPagiis() {
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("uploads");

        String currentUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef_x.limitToLast(50).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String localID = ds.getKey();

                        tagedUsers.add(new tags(localID));
                        Collections.shuffle(tagedUsers);

                        if (dataSnapshot.getChildrenCount() == tagedUsers.size()) {

                            getPagiisData();

                            //getDataNormally();

                            //0799506310

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);

                TextView messageGrid = toastView.findViewById(R.id.customToastText);


                // Initiate the Toast instance.
                Toast toast = new Toast(getActivity());

                messageGrid.setText("Pagiis online user update failed, Please waite while pagiis restarts activity");
                // Set custom view in toast.
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                //Toast.makeText(MapsActivity.this, "Button switched on.", Toast.LENGTH_SHORT).show();

            }

        });
    }


    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    private void viewProfile(int position) {
        // Ensure we use an Activity context
        if (!(getApplicationContext() instanceof Activity)) {
            return;  // Prevent crashes
        }

        // Get the selected image from the list using the position
        final ImageUploads selectedImage = mUploads.get(position);

        // Extract the required data from the selected image
        final String imageUrl = selectedImage.getImageUrl(); // Assuming this is the correct getter method
        final String userId = selectedImage.getUserId(); // Assuming this is the correct getter method

        // Initialize BottomSheetDialog with Activity context
        @SuppressLint("RestrictedApi") final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog((Activity)getApplicationContext(), R.style.BottomSheetDialogueTheme);
        @SuppressLint("RestrictedApi") View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.botttom_sheet_layout, null);

        // Initialize views from the bottom sheet layout
        ImageView profilePicture = bottomSheetView.findViewById(R.id.mapsItemProfile);
        TextView profileName = bottomSheetView.findViewById(R.id.popUpDescriptionTextViewTwo);
        TextView profileStatus = bottomSheetView.findViewById(R.id.popUpDescriptionTextViewThreee);
        TextView profileView = bottomSheetView.findViewById(R.id.popLocationTexview);
        TextView sharePagiis = bottomSheetView.findViewById(R.id.share);
        Button viewProfileButton = bottomSheetView.findViewById(R.id.visitProfile);
        SparkButton imageViewLikes = bottomSheetView.findViewById(R.id.likes);

        // Set profile name
        if (selectedImage.getName() != null && !selectedImage.getName().isEmpty()) {
            profileName.setText(selectedImage.getName());
        }

        // Load profile picture using Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            RequestOptions options = new RequestOptions();
            Glide.with(getApplicationContext())
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(options.centerCrop())
                    .thumbnail(0.75f)
                    .into(profilePicture);
        } else {
            // Set a default image if the URL is null or empty
            profilePicture.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pagiis_logo_final));
        }

        // View Profile Button Click Listener
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("visited_user_id", userId); // Pass the userId to the ProfileFragment

                ProfileFragment targetFragment = new ProfileFragment();
                targetFragment.setArguments(bundle);


                if (getApplicationContext() instanceof FragmentActivity) {
                    FragmentManager fragmentManager = ((FragmentActivity) getApplicationContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainContainer, targetFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        // Share App Functionality
        bottomSheetView.findViewById(R.id.shareImageView).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family, please check out this amazing App called Pagiis: " + Uri.parse("https://www.pagiis.co.za/"));
                intent.setType("text/plain");

                if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    startActivity(intent);  // ✅ Works inside Adapter
                }
            }
        });
    }


    private void getDataNormally() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        final String userIdRef = mAuth.getCurrentUser().getUid();
        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        if (!tagedUsers.isEmpty()) {
            for (int i = 0; i < tagedUsers.size(); i++) {

                tags x = tagedUsers.get(i);

                final String taged_id = x.getUser_tagID();

                mDatabaseRef.child(taged_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {


                        if (dataSnapshot.exists()) {
                            mUploads.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ImageUploads upload = ds.getValue(ImageUploads.class);
                                upload.setKey(ds.getKey());
                                mUploads.add(upload);
                                Collections.shuffle(mUploads);
                            }

                            uploadItem.setVisibility(View.INVISIBLE);
                            uploadItem.setEnabled(false);
                            mAdapter.notifyDataSetChanged();
                            mProgressCircle.setVisibility(INVISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        //mProgressCircle.setVisibility(INVISIBLE);
                    }
                });
            }
        }


    }


    private void getPagiisDataByLocation2()
    {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mUploads.clear();
                    String searchLocation = locationInput.getText().toString().trim();
                    if (TextUtils.isEmpty(searchLocation)) {
                        Toast.makeText(getActivity(), "Enter a location", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                            if(ds.hasChild("postLocation"))
                            {

                                String postLocation = ds.child("postLocation").getValue().toString();
                                Toast.makeText(getActivity(),postLocation, Toast.LENGTH_LONG).show();

                                    if (postLocation != null) {
                                        String[] locationParts = postLocation.split(",");
                                        for (String part : locationParts) {
                                            if (part.trim().equalsIgnoreCase(searchLocation.trim()))
                                            {
                                                ImageUploads upload = ds.getValue(ImageUploads.class);
                                                upload.setKey(ds.getKey());
                                                mUploads.add(upload);

                                            }
                                        }
                                    }

                            }




                    }
                    if (!mUploads.isEmpty()) {
                        Collections.shuffle(mUploads);
                        mAdapter.notifyDataSetChanged();
                    } else
                    {
                        //getPagiisDataByLocation2();
                        notifyUsersMatchingLocation(searchLocation);
                        Toast.makeText(getActivity(), "No matching posts found", Toast.LENGTH_LONG).show();
                    }
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void getPagiisDataByLocation() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mUploads.clear(); // Clear previous data
        mProgressCircle.setVisibility(View.VISIBLE);

        // Get search input
        String searchLocation = locationInput.getText().toString().trim();

        // Check if input is empty
        if (TextUtils.isEmpty(searchLocation)) {
            Toast.makeText(getActivity(), "Enter a location", Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.INVISIBLE);
            return; // Stop execution
        }

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashSet<String> uniqueUserIds = new HashSet<>();

                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();

                        // Process each user's uploads only once
                        if (uniqueUserIds.add(userId)) {
                            for (DataSnapshot ds : userSnapshot.getChildren()) {

                                // Check if "postLocation" exists
                                if (ds.hasChild("postLocation")) {
                                    String postLocation = ds.child("postLocation").getValue(String.class);

                                    if (postLocation != null && postLocation.toLowerCase().contains(searchLocation.toLowerCase())) {
                                        ImageUploads upload = ds.getValue(ImageUploads.class);
                                        if (upload != null) {
                                            upload.setKey(ds.getKey());
                                            mUploads.add(upload);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Shuffle to randomize order
                    Collections.shuffle(mUploads);

                    // Update UI
                    uploadItem.setVisibility(View.INVISIBLE);
                    uploadItem.setEnabled(false);
                    mAdapter.notifyDataSetChanged();
                }

                // Hide progress circle after data is loaded
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                mProgressCircle.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void getPagiisData() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mUploads.clear(); // Clear previous data
        mProgressCircle.setVisibility(View.VISIBLE);

        // Retrieve all uploads at once
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashSet<String> uniqueUserIds = new HashSet<>();

                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey(); // Unique user ID

                        // Check if the user ID is already processed
                        if (uniqueUserIds.add(userId)) {
                            for (DataSnapshot ds : userSnapshot.getChildren()) {
                                ImageUploads upload = ds.getValue(ImageUploads.class);
                                if (upload != null) {
                                    upload.setKey(ds.getKey());
                                    mUploads.add(upload);
                                }
                            }
                        }
                    }

                    // Shuffle to randomize order
                    Collections.shuffle(mUploads);

                    // Update UI
                    uploadItem.setVisibility(View.INVISIBLE);
                    uploadItem.setEnabled(false);
                    mAdapter.notifyDataSetChanged();
                }
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void notifyUsersMatchingLocation(final String searchLocation) {
        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("myLastLocation");

        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> matchedUserKeys = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userLocation = userSnapshot.getValue(String.class);

                    if (userLocation != null) {
                        String[] locationParts = userLocation.split(",");
                        for (String part : locationParts) {
                            if (part.trim().equalsIgnoreCase(searchLocation.trim())) {
                                matchedUserKeys.add(userSnapshot.getKey());
                                break;
                            }
                        }
                    }
                }

                if (!matchedUserKeys.isEmpty()) {
                    sendPushNotifications(matchedUserKeys);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("NotifyUsers", "Error fetching location data: " + databaseError.getMessage());
            }
        });
    }


    private void sendPushNotifications(List<String> userKeys) {
        DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference("userTokens");

        tokensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> fcmTokens = new ArrayList<>();

                for (String userKey : userKeys) {
                    String token = dataSnapshot.child(userKey).getValue(String.class);
                    if (token != null) {
                        fcmTokens.add(token);
                    }
                }

                if (!fcmTokens.isEmpty()) {
                    for (String token : fcmTokens)
                    {

                        notificationTitle = "Your location interests";
                        notificationMessage = "People are interested in whats happening where you are .";
                        sendFCMNotification(token,notificationTitle,notificationMessage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SendNotifications", "Error fetching FCM tokens: " + databaseError.getMessage());
            }
        });
    }


    private void sendFCMNotification(String fcmToken, String title, String message) {
        String FCM_API = "https://fcm.googleapis.com/fcm/send";
        String serverKey = "AAAA64f0YOg:APA91bEWaRY_bpktQU7HtgIhAVsLjhJCGTwjWVWi1bYutnDkwkmo2QmgKBJf8MO6BJXrpiDEi62-XDWKi8B0ogwQ8PVLoABuRyExDj_kdw4VOGQa-0PzzV_G8toDuzWbcXUqoh6LbBAS";
        String contentType = "application/json";

        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();

        try {
            notificationBody.put("title", title);
            notificationBody.put("body", message); // Use "body" for notifications
            notification.put("to", fcmToken);
            notification.put("notification", notificationBody); // Correct key
            notification.put("data", notificationBody); // Optional for additional data
        } catch (JSONException e) {
            Log.e("FCM Error", "JSON Exception: " + e.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_API, notification,
                response -> Log.d("FCM Response", "Success: " + response.toString()),
                error -> Log.e("FCM Error", "Failed: " + error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "key=" + serverKey); // Corrected
                headers.put("Content-Type", contentType);
                return headers;
            }
        };

        @SuppressLint("RestrictedApi") RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext()); // Fixed Context
        requestQueue.add(request);
    }




    private void siroccoView() {
        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference().child("WalkinWall");
        mDatabaseRef_y.limitToLast(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    kUploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        kUploads.add(upload);

                    }

                    sAdapter.notifyDataSetChanged();
                    uploadItem.setVisibility(View.INVISIBLE);
                    uploadItem.setEnabled(false);
                    searchRecyclerView.setVisibility(VISIBLE);
                    sAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void getDataNormallyLink() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploadsLink");
        final String userIdRef = mAuth.getCurrentUser().getUid();
        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        if (!tagedUsers.isEmpty()) {
            for (int i = 0; i < tagedUsers.size(); i++) {

                tags x = tagedUsers.get(i);

                final String taged_id = x.getUser_tagID();

                mDatabaseRef.child(taged_id).limitToLast(50).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            mUploads.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ImageUploads upload = ds.getValue(ImageUploads.class);
                                upload.setKey(ds.getKey());
                                mUploads.add(upload);
                            }
                            contentRaterLink.setVisibility(INVISIBLE);
                            contentRaterLink.setEnabled(false);

                            contentRater.setVisibility(VISIBLE);
                            contentRater.setEnabled(true);

                            mAdapterLink.notifyDataSetChanged();
                            mProgressCircle.setVisibility(INVISIBLE);

                        } else {
                            Toast.makeText(getApplicationContext(), "Link posts not found.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgressCircle.setVisibility(INVISIBLE);
                    }
                });
            }
        }
    }

    private void getDataSnapShot() {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(getKey);

        if (!getKey.isEmpty()) {

            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        ImageUploads upload = dataSnapshot.getValue(ImageUploads.class);
                        upload.setKey(String.valueOf(dataSnapshot.getKey()));
                        mUploads.add(upload);

                    } else {
                        Toast.makeText(getActivity(), "Pagiis failed to read database. please retry action", Toast.LENGTH_SHORT).show();

                    }

                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(INVISIBLE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void getRestrictedData() {


        final String on_maps_visited_user_id = getActivity().getIntent().getExtras().get("visit_user_id").toString();
        mDatabaseRef.child(on_maps_visited_user_id).orderByChild("Age").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(INVISIBLE);
            }

            @Override

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(INVISIBLE);
            }
        });
    }


    //Pagiis Lens Location Search.
    @SuppressLint("RestrictedApi")
    private void Geolocate() {


        String searchedString = "";//****get this from searched EditText.
        @SuppressLint("RestrictedApi") Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addressList = new ArrayList<>();

        try {

            addressList = geocoder.getFromLocationName(searchedString, 1);

        } catch (IOException e) {
            Log.e(TAG, "Geolocate: IOException" + e.getMessage());
        }
        if (addressList.size() > 0) {


            Address address1 = addressList.get(0);
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_memes_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //This button is for the explicit content view
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (item.getItemId() == R.id.sirocco_icon) {
            @SuppressLint("RestrictedApi") Intent intent = new Intent(getApplicationContext(), SiroccoPage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(int position)

    {
        ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String own_user_id = mAuth.getCurrentUser().getUid();

        final String imageUrl = selectedImage.getImageUrl();

        final String userKeyId = selectedImage.getUserId();

        // Get the selected image from the list using the position
        // Assuming this is the correct getter method
        final String userId = selectedImage.getUserId(); // Assuming this is the correct getter method

        // Initialize BottomSheetDialog with Activity context
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogueTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.botttom_sheet_layout, null);

        // Initialize views from the bottom sheet layout
        ImageView profilePicture = bottomSheetView.findViewById(R.id.mapsItemProfile);
        TextView profileName = bottomSheetView.findViewById(R.id.popUpDescriptionTextViewTwo);
        TextView profileStatus = bottomSheetView.findViewById(R.id.popUpDescriptionTextViewThreee);
        TextView profileView = bottomSheetView.findViewById(R.id.popLocationTexview);
        TextView sharePagiis = bottomSheetView.findViewById(R.id.share);
        Button viewProfileButton = bottomSheetView.findViewById(R.id.visitProfile);
        SparkButton imageViewLikes = bottomSheetView.findViewById(R.id.likes);

        profileStatus.setText(selectedImage.getPostName());

        // Set profile name
        if (selectedImage.getName() != null && !selectedImage.getName().isEmpty()) {
            profileName.setText(selectedImage.getName());
        }

        // Load profile picture using Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            RequestOptions options = new RequestOptions();
            Glide.with(getApplicationContext())
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(options.centerCrop())
                    .thumbnail(0.75f)
                    .into(profilePicture);
        } else {
            // Set a default image if the URL is null or empty
            profilePicture.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pagiis_logo_final));
        }

        // View Profile Button Click Listener
        bottomSheetView.findViewById(R.id.visitProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("visited_user_id", userId); // Pass the userId to the ProfileFragment

                ProfileFragment targetFragment = new ProfileFragment();
                targetFragment.setArguments(bundle);


                FragmentManager fragmentManager = ((FragmentActivity) requireContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                            .replace(R.id.mainContainer, targetFragment)
                            .addToBackStack(null)
                            .commit();
            }


        });

        // Share App Functionality
        bottomSheetView.findViewById(R.id.shareImageView).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family, please check out this amazing App called Pagiis: " + Uri.parse("https://www.pagiis.co.za/"));
                intent.setType("text/plain");

                if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    startActivity(intent);  // ✅ Works inside Adapter
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


            /*lockedImages.addValueEventListener(new ValueEventListener() {

                String UnitLikes;

                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot)
                {
                    lockedImages.child(own_user_id).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                           UnitLikes = String.valueOf(Objects.requireNonNull(dataSnapshot).getChildrenCount());

                           if(UnitLikes != null)
                           {
                               mDatabaseRef.child("likes").setValue(UnitLikes);
                           }else
                           {
                               mDatabaseRef.child("likes").setValue(String.valueOf(dataSnapshot.getChildrenCount()));
                           }

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });*/


    @Override
    public void onNItemClick(int position) {
        ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String own_user_id = mAuth.getCurrentUser().getUid();

        final String imageUrl = selectedImage.getImageUrl();

        String userKeyId = selectedImage.getUserId();

        /*Intent intent = new Intent(ViewUsersMemes.this ,PagiisMaxView.class);
        intent.putExtra("imageKeyMAx", selectedKey);
        intent.putExtra("imageUrlMax",imageUrl);
        intent.putExtra("imageUserId", userKeyId);
        startActivity(intent);*/
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClickLink(int position) {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String exRater = selectedImage.getExRating();


        final String imageUrl = selectedImage.getImageUrl();


        if (imageUrl != null && !imageUrl.isEmpty() && imageUrl.compareTo("null") != 0 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
            Intent intent = new Intent(getApplicationContext(), LinkView.class);
            intent.putExtra("visit_user_id", imageUrl);
            startActivity(intent);

        } else {
            Toast.makeText(getApplicationContext(), "User not linked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWhatEverClickLink(int position) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void shareClickLink(int position) {


        ImageUploads selectedImage = mUploads.get(position);

        String selectedKey = selectedImage.getKey();

        String getUserRef = selectedImage.getUserId();

        String imageUrl = selectedImage.getImageUrl();

        if (!getUserRef.isEmpty() && !selectedKey.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), LinkPost.class);
            intent.putExtra("share_item_id", getUserRef);
            intent.putExtra("share_item_id_key", selectedKey);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Pagiis failed to delete file. the system will retry deleting file later ", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void chatsClickLink(int position) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onWhatEverClick(int position) {
        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String getUserRef = selectedImage.getUserId();

        if (!getUserRef.isEmpty() & !selectedKey.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), ActivityOwnProfile.class);
            intent.putExtra("visit_user_id", getUserRef);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Pagiis failed to delete file. the system will retry deleting file later ", Toast.LENGTH_SHORT).show();

        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void shareClick(int position) {

        ImageUploads selectedImage = mUploads.get(position);

        String selectedKey = selectedImage.getKey();

        String getUserRef = selectedImage.getUserId();

        String imageUrl = selectedImage.getImageUrl();

        if (!getUserRef.isEmpty() && !selectedKey.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), ActivityUploadImage.class);
            intent.putExtra("share_item_id", imageUrl);
            startActivity(intent);
        } else {

            Toast.makeText(getApplicationContext(), "Pagiis failed to delete file. the system will retry deleting file later ", Toast.LENGTH_SHORT).show();
        }


    }

    /*@Override
    public void onDeleteClick(int position)
    {


        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String getUserRef = selectedImage.getUserId();

        if(!getUserRef.isEmpty() & !selectedKey.isEmpty())
        {
            Intent intent = new Intent(ViewUsersMemes.this,UserProfileActivity.class);
            intent.putExtra("visit_user_id", getUserRef);
            startActivity(intent);
        }else
        {
            Toast.makeText(this, "Pagiis failed to delete file. the system will retry deleting file later ", Toast.LENGTH_SHORT).show();
            finish();
        }

    }*/

    @Override
    public void chatsClick(int position) {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String getUserRef = selectedImage.getUserId();

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.chats_update_infor, null);

        //ImageView logoutImage = mView.findViewById(R.id.SignOutImage);
        ImageView cancelLogOutImage = mView.findViewById(R.id.cancelSignOutImage);


        /*logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.findViewById(R.id.SignOutImage);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ViewUsersMemes.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });*/

        cancelLogOutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("RestrictedApi") Intent intent = new Intent(getApplicationContext(), ViewUsersMemes.class);
                startActivity(intent);

            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    @Override
    public void onStop() {
        super.onStop();

        tagedUsers.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateArrayListPagiis();
    }

    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);




        /*mToolbar = (Toolbar) rootView.findViewById(R.id.appBarLayout);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        setSupportActionBar(mToolbar);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("live"); */


        //contentRater = rootView.findViewById(R.id.contentRating);
        //contentRaterLink = rootView.findViewById(R.id.contentRatingLnk);
        viewUserVideos = rootView.findViewById(R.id.goToVideos);
        viewUserVideos.getRootView().setVisibility(VISIBLE);
        viewUserVideos.setEnabled(true);


        goToMaps = rootView.findViewById(R.id.goToMaps);

        pagiisIcon = rootView.findViewById(R.id.PAGiiS_ICON);


        locationInput = rootView.findViewById(R.id.searchEdittext);
        searchForResults = rootView.findViewById(R.id.LogSearchIconGo);

        uploadItem = rootView.findViewById(R.id.businessId);

        //searchInputLayout = rootView.findViewById(R.id.searchTextInputLayout);

        //searchInputLayout.setVisibility(INVISIBLE);
        //searchInputLayout.setEnabled(true);

        discoverLayoutCard = rootView.findViewById(R.id.discoverLayout);

        discoverLayoutCard.setVisibility(INVISIBLE);
        discoverLayoutCard.setEnabled(true);


        locationSuggestionsList = rootView.findViewById(R.id.lvPlaceSuggestions);

        //searchNotice = rootView.findViewById(R.id.SearcNotice);
        //searchNotice.setVisibility(INVISIBLE);
        //searchNotice.setEnabled(false);

        //contentRater.setVisibility(INVISIBLE);

        //contentRater.setEnabled(false);

        //contentRaterLink.setVisibility(View.VISIBLE);

        //contentRaterLink.setEnabled(true);


        mRecyclerView = rootView.findViewById(R.id.memeRecyclerView);
        searchRecyclerView = rootView.findViewById(R.id.SearchRecyclerViewTop);
        searchRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new ViewProfilePicsAdapter(getApplicationContext(), mUploads);
        mAdapterLink = new ViewProfilePicsAdapter(getApplicationContext(), mUploads);
        sAdapter = new mapSearchedItemAdaptor(getApplicationContext(), kUploads);

        // Initialize the adapter and set it to the ListView
        locationAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, locationSuggestions);
        locationSuggestionsList.setAdapter(locationAdapter);

        // Listen for text input changes
        locationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString();
                if (!query.isEmpty()) {
                    searchLocations(query);
                } else {
                    locationSuggestions.clear();
                    updateLocationSuggestionsUI();  // Clear suggestions when the query is empty
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Handle item selection from suggestions
        locationSuggestionsList.setOnItemClickListener((parent, view, position, id) -> {
            String selectedLocation = locationSuggestions.get(position);
            locationInput.setText(selectedLocation); // Set selected location in EditText
            locationSuggestionsList.setVisibility(View.GONE); // Hide suggestions
        });


        //sAdapter = new mapsProfileItemsViewAdaptor(getApplicationContext(), kUploads);
        //sAdapter.setOnItemClickListener(MapsActivity.this);

        mAdapter.setOnItemClickListener(HomeFragment.this);
        sAdapter.setOnItemClickListener(HomeFragment.this);


        mRecyclerView.setAdapter(mAdapter);

        searchRecyclerView.setAdapter(sAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchRecyclerView.setVisibility(INVISIBLE);
        searchRecyclerView.setEnabled(false);

        //returnToMaps = findViewById(R.id.returnToMaps);

        mProgressCircle = rootView.findViewById(R.id.progress_circle_user_memes);

        mProgressCircle.setVisibility(VISIBLE);

        viewUserVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.goToVideos);
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment,new VideoStreams()).commit();
                Intent intent = new Intent(getActivity(), ViewUsersVideos.class);
                startActivity(intent);
            }
        });

        pagiisIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.PAGiiS_ICON);

                if(isLayoutVisisble.compareTo("false")==0)
                {
                    discoverLayoutCard.setVisibility(VISIBLE);
                    isLayoutVisisble = "true";

                }else if(isLayoutVisisble.compareTo("true")==0)
                {

                    isLayoutVisisble = "false";
                    discoverLayoutCard.setVisibility(INVISIBLE);


                }


            }
        });



        goToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.goToMaps);
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment,new VideoStreams()).commit();
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });




        searchForResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.LogSearchIconGo);



                getPagiisDataByLocation();

               /* if (!tagedUsers.isEmpty()) {

                    for (int i = 0; i < tagedUsers.size(); i++) {
                        tags x = tagedUsers.get(i);
                        final String taged_id = x.getUser_tagID();

                        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(taged_id);

                        Query mSearchQuery = mDatabaseRef.orderByChild("name").startAt(searchedText).endAt(searchedText + "\uf8ff");
                        final String userIdRef = mAuth.getCurrentUser().getUid();

                        mSearchQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    mUploads.clear();
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        ImageUploads upload = ds.getValue(ImageUploads.class);
                                        upload.setKey(ds.getKey());
                                        mUploads.add(upload);
                                    }

                                    searchRecyclerView.setVisibility(View.VISIBLE);
                                    searchRecyclerView.setEnabled(true);
                                    sAdapter.notifyDataSetChanged();
                                    mProgressCircle.setVisibility(INVISIBLE);
                                } else {
                                    searchNotice.setVisibility(View.VISIBLE);
                                    searchNotice.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                mProgressCircle.setVisibility(INVISIBLE);
                            }
                        });
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Pagiis online posts currently unavailable.", Toast.LENGTH_SHORT).show();

                }*/

            }
        });


        return rootView;


    }
}