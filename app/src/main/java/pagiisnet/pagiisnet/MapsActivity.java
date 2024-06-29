package pagiisnet.pagiisnet;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import pagiisnet.pagiisnet.Utils.FilterMapsProfileCategory;
import pagiisnet.pagiisnet.Utils.PlaceAutocompleteAdapter;
import pagiisnet.pagiisnet.Utils.ViewAllPagiisProfiles;
import pagiisnet.pagiisnet.Utils.ViewMapsProfileCategory;
import pagiisnet.pagiisnet.Utils.ViewProfilePicsAdapter;
import pagiisnet.pagiisnet.Utils.mapSearchedItemAdaptor;
import pagiisnet.pagiisnet.Utils.mapsProfileItemsViewAdaptor;
import pagiisnet.pagiisnet.Utils.mapsViewAdapter;
import pagiisnet.pagiisnet.models.PlaceInfor;

public class MapsActivity extends FragmentActivity implements FilterMapsProfileCategory.OnItemClickListener, ViewMapsProfileCategory.OnItemClickListener, ViewProfilePicsAdapter.OnItemClickListener, ViewAllPagiisProfiles.OnItemClickProfileListener, mapsProfileItemsViewAdaptor.OnItemClickListener, mapSearchedItemAdaptor.OnItemClickListener, mapsViewAdapter.OnItemClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener
{

    GnssStatus.Callback mGnssStatusCallback;
    LocationManager mLocationManager;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int PLACE_PICKER_REQUEST_Two = 2;
    private final long UPDATE_INTERVAL = 8 * 1000;  /* 10 secs */
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    private GoogleMap mMap;
    private Switch radiusSwitch;
    private RecyclerView mRecyclerView;

    private String selectedItem;

    private String shareAppLink;

    private RecyclerView mRecyclerViewProfileCategory;

    int[] usersFoundCountNumber = {0};

    private RecyclerView searchRecyclerView;
    private ViewProfilePicsAdapter viewProfilesAdapter;

    private ViewAllPagiisProfiles viewAllPagiisProfilesAdapter;

    private String profileCategoryValue;
    private mapsProfileItemsViewAdaptor sAdapter;
    private final long FASTEST_INTERVAL = 60000; /*  sec */
    private GoogleApiClient mGoogleApiClient;

    private Fragment oldFragment;
    private DatabaseReference lockedImages;
    private ImageView userImageDp;
    private ImageView hideUserImageDp;
    private ImageView exitMaxCardView;

    private ViewPager viewPager;
    private Adapter adapter;

    private mapsViewAdapter mAdapter;
    private ViewMapsProfileCategory mAdapterProfileCategory;
    private List<ImageUploads> mUploads;

    private List<ImageUploads> mUploadsProfileCategory;
    private List<ImageUploads> kUploads;
    private List<ImageUploads> ViewProfilePostsUploads;
    private List<ImageUploads> viewProfileUploads;
    private mapSearchedItemAdaptor mapsAdapterNew;

    private RecyclerView mapsBViewPager;
    private String onlineUserMarkerName = "Online User";
    private final boolean onlineUserMarkerON = false;
    private final boolean isUserMarkerChecked = false;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private DatabaseReference TagRef_y;
    private Task readDataTask;

    private CardView mapsViewCard;
    private RecyclerView mapsRecyclerView;
    private CardView mapsViewSearcCard;

    private CardView mapsViewSearcCardMax;

    private CardView mapsIconStand;
    private CardView mapsDiscoverLayout;

    private Location mLastLocation;

    private RecyclerView mapSearchRecyclerView;
    LocationRequest mLocationRequest;
    private List<tags> tagedUsers;
    private List<tags> tagedUserString;

    private List<RadiusUserNotification> radiusUserNotifications;
    private LocationCallback mLocationCallbaCk;
    Dialog myDialog;
    //private DatabaseReference geoRef;
    private Button backToMapsbutton;
    private Task getUserPage;

    private static final String TAG = "MapsActivity";
    private AutoCompleteTextView mSearchText;

    private LatLng currentUserLocation;
    private LatLng userLocation;

    private Spinner searchSpinner;
    private ProgressBar progressBarSearc;

    private final String GeofireKey = null;
    private Marker mCuurentUserMaker;
    private Marker userFoundMarker;
    private Task radiusIncrementTask;
    private FirebaseAuth mAuth;


    private ImageView memedialogueImageView;
    private ImageView dialogueImageView;

    private final double radiusMax = 0.5;
    private ImageView radiusIncrement;
    private ImageView mGps, mInfo, mPlacePicker, navigationToTabs, hideSearchRecyclerView, hideUserRecyclerView;
    private String radiusValue;
    private final boolean isMaxRadiusChecked = false;
    //private Location userLocation;


    //These will control the view of the maps CardViews

    private ImageView hidePublicProfilePostsCardview;
    private ImageView hidePublicProfilesCardView;

    private CardView publicProfilePostsCardView;
    private CardView publicProfilesCardview;

    private CardView iconStandView;

    private RecyclerView publicProfilesPostsRecyclerView;
    private RecyclerView publicProfileRecyclerView;


    private String userFoundID;

    private String visitUserFoundId;

    private static final int ACTION_NUM = 0;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private DatabaseReference TagRef;

    private ProgressBar recyclerProgressBar;

    private ProgressBar recyclerProgressBarMax;
    private List<String> OnlineUserList;
    private List<ImageUploads> uploads;

    private List<ProfileViewHolder> ProfileUploads;
    private ValueEventListener mDBlistener;
    private DatabaseReference mDatabaseRef;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef_Y;
    private DatabaseReference mDatabaseRef_Z;
    private DatabaseReference mDatabaseRef_viewProfiles;
    private DatabaseReference TagRef_x;

    private RecyclerView filterProfileCategoryRecyclerView;
    private List<ImageUploads> filterProfilesUpload;
    private FilterMapsProfileCategory filterProfilesAdapter;
    private String filterSelectedItem;
    private DatabaseReference filterDataseServices;
    private DatabaseReference fileterDataBaseProfiles;


    private Geocoder geocoder;


    private LinearLayout textInputLayoutSearc;

    private final String newNotification = null;
    private TextView dialogueTitle, radiusR, radiusView;
    private ImageView goToUploads, logOutButton, userMakerView;
    private ImageView pagiisLogo, getSerachedResults, viewSearchField, siroccoIcon;

    private EditText searchValueField;
    private ImageView pagiisLogoNoti;

    private MaterialSearchBar materialSearchBar;

    private Boolean isFirstTimeRun;

    private String myLastLocationDetails;


    private ImageView artWorld;
    private ImageView businessWorld;
    private ImageView musicWorld;
    private ImageView fashionWorld;
    private ImageView myCart;
    //private PlacesClient mPlacesClient;
    // private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabaseRef_x;

    private DatabaseReference mDatabaseRef_online_products;

    private DatabaseReference mDatabaseRef_shareApp;
    private ImageView SeachLocationImageview;


    private String searcValue;
    private String userStatus;

    private String userProfileServiceTag;

    private List<String> searchString;

    private ImageView searchIndicatoIcon;

    private List<Address> addresses;

    private DatabaseReference getmDatabaseRef_lastLocation;
    private CircularImageView nearbyLocationImagevIew;
    private PlaceInfor mPlace;
    private TextView noSearcFound;

    private TextInputEditText SearchTextField;//this image view is for the search icon serves as a button;

    private String searchedText;
    private ImageView searchBaricon;

    private BottomNavigationView bottomNavigationView;

    private CountDownTimer cTimer = null;

    private BottomSheetDialog bottomSheetDialog;


    public class MyAppConstants {
        public static final String CHANNEL_ID = "YOUR_CHANNEL_ID";
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Places.initialize(getApplicationContext(), "AIzaSyAWyHMDgO9ZvpefFyYxmPoal7J-uljmouk");
        FirebaseApp.initializeApp(getApplicationContext());



        // Assuming you have a FirebaseMessaging instance
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

        // Replace with your actual registration token(s)
        List<String> registrationTokens = Arrays.asList("your_registration_token_here");

        // Subscribe the tokens to the topic
        firebaseMessaging.subscribeToTopic("new_product_forms");

        PlacesClient placesClient = Places.createClient(this);



        mAuth = FirebaseAuth.getInstance();

        getSerachedResults = findViewById(R.id.LogSearchIconGo);

        bottomSheetDialog = new BottomSheetDialog(MapsActivity.this, R.style.BottomSheetDialogueTheme);

        //mProgressCircle = findViewById(R.id.progress_circle);


        bottomNavigationView = findViewById(R.id.maps_bottom_nav);
        //getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new HomeFragment()).commit();


        bottomNavigationView.setSelectedItemId(R.id.home);

        searchSpinner = findViewById(R.id.searchSpinner);

        iconStandView = findViewById(R.id.iconstand);

        mapsDiscoverLayout = findViewById(R.id.discoverLayout);

        //mapsDiscoverLayout.setVisibility(View.INVISIBLE);

        nearbyLocationImagevIew = findViewById(R.id.nearbyLocation);

        nearbyLocationImagevIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                //bottomNavigationView.setVisibility(View.INVISIBLE);


                try {

                    startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e)
                {
                    Log.e(TAG, "onClick: GooglePlayServiceRepairableException" + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "onClick: GooglePlayServicesNotAvailable" + e.getMessage());
                }


            }

        });


        //String shareItemId = getIntent().getExtras().get("visit_user_id").toString();

        //getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.homeFragment)).commit();
        //Fragment fragment = null;
        //switchContent(fragment);

        bottomNavigationView.setSelected(false);

        //oldFragment = getSupportFragmentManager().findFragmentById(R.id.homeFragment);
        //if (oldFragment != null) {
         //   getSupportFragmentManager().beginTransaction()
                   // .remove(oldFragment).commit();
        //}

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                oldFragment = null;
                int itemId = menuItem.getItemId();
                if (itemId == R.id.home) {
                    Bundle bundle = new Bundle();

                    bundle.putString("visited_user_id", mAuth.getCurrentUser().getUid());
                    mapsDiscoverLayout.setVisibility(View.INVISIBLE);

                    oldFragment = new HomeFragment();
                    oldFragment.setArguments(bundle);

                    iconStandView.setVisibility(View.INVISIBLE);
                } else if (itemId == R.id.onlineShops) {
                    mapsDiscoverLayout.setVisibility(View.INVISIBLE);

                    oldFragment = new SiroccoFragment();
                    iconStandView.setVisibility(View.INVISIBLE);
                } else if (itemId == R.id.addCOntent) {

                    Bundle bundle = new Bundle();

                    bundle.putString("visited_user_id", "fromMaps");
                    mapsDiscoverLayout.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(getApplicationContext(), GalleryUploads.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    oldFragment = null;


                    // oldFragment = new GalleryFragment();
                    //iconStandView.setVisibility(View.INVISIBLE);
                } else if (itemId == R.id.Notifications) {
                    mapsDiscoverLayout.setVisibility(View.INVISIBLE);
                    oldFragment = new NotificationFragment();
                    iconStandView.setVisibility(View.INVISIBLE);
                } else if (itemId == R.id.userProfile) {
                    mapsDiscoverLayout.setVisibility(View.INVISIBLE);

                    iconStandView.setVisibility(View.INVISIBLE);
                    oldFragment = new ProfileFragment();
                }

                if (oldFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, oldFragment).commit();
                }
                return true;
            }
        });

        SeachLocationImageview = findViewById(R.id.LogSearchIconGo);

        //SeachLocationImageview.setEnabled(FALSE);

        mSearchText = findViewById(R.id.searchEdittext);

        searchedText = mSearchText.getText().toString();


        // This code if for seaching google places. from touching the search icon an activating it
        SeachLocationImageview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.LogSearchIconGo) {
                    init();

                }
            }
        });

        mapsViewCard = findViewById(R.id.mapsViewOnlineUserCardView);
        mRecyclerView = findViewById(R.id.mapsOnlienUserRecyclerview);

        mRecyclerViewProfileCategory = findViewById(R.id.mapsProfileCategory);
        mRecyclerViewProfileCategory.setHasFixedSize(true);
        //mRecyclerViewProfileCategory.setVisibility(View.INVISIBLE);

        publicProfilesPostsRecyclerView = findViewById(R.id.mapsPublicContentRecyclerView);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerViewProfileCategory.setAdapter(mAdapterProfileCategory);


        mapsViewCard.setVisibility(View.INVISIBLE);

        hidePublicProfilePostsCardview = findViewById(R.id.hideMapsPublicPostsCardView);
        hidePublicProfilesCardView = findViewById(R.id.hidePublicProfilesCardView);

        publicProfilePostsCardView = findViewById(R.id.mapsPublicProfilePostsCardView);
        publicProfilesCardview = findViewById(R.id.mapsViewOnlineUserCardView);


        hidePublicProfilesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                publicProfilesCardview.setVisibility(View.INVISIBLE);

            }
        });

        hidePublicProfilePostsCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                publicProfilePostsCardView.setVisibility(View.INVISIBLE);

            }
        });



        //The following has been disabled for the latest App update
        /*hideUserImageDp= findViewById(R.id.hideUserMaxImage);
        pagiisLogo = findViewById(R.id.PAGiiS_ICON);


        mapsViewSearcCard = findViewById(R.id.mapsSearchViewCardView);
        mapsViewSearcCardMax = findViewById(R.id.mapsSearchViewCardViewMax);


        userImageDp = findViewById(R.id.mapsMaxView);

        siroccoIcon = findViewById(R.id.SIROCCO_ICON);

        searchIndicatoIcon = findViewById(R.id.searchIndicatorIcon);

        pagiisLogoNoti = findViewById(R.id.PAGiiS_ICON_NOTi);
        searchSpinner = findViewById(R.id.searchSpinner);


        mGps = findViewById(R.id.radiusUsersIcon);
        //goToUploads = (ImageView) findViewById(R.id.radiusUserView);
        logOutButton = findViewById(R.id.logOuticon);
        //radiusIncrement = findViewById(R.id.radiusUsersIcon);
        //radiusR = findViewById(R.id.radius);
        radiusView = findViewById(R.id.radiusView);
        //userMakerView = findViewById(R.id.userMarker);
        radiusSwitch = findViewById(R.id.maxRadiusSwitch);
        recyclerProgressBar = findViewById(R.id.progress_circle_maps);
        recyclerProgressBarMax = findViewById(R.id.progress_circle_maps_Max);

        progressBarSearc = findViewById(R.id.progress_circle_mapsSearc);


         hideUserImageDp.setVisibility(View.INVISIBLE);
        userImageDp.setVisibility(View.INVISIBLE);


        lockedImages = FirebaseDatabase.getInstance().getReference("uploads");

        progressBarSearc.setVisibility(View.INVISIBLE);

        hideUserRecyclerView.setEnabled(false);
        hideUserRecyclerView.setVisibility(View.INVISIBLE);

        mapsViewCard.setVisibility(View.INVISIBLE);
        mapsViewSearcCard.setVisibility(View.INVISIBLE);
        mapsViewSearcCardMax.setVisibility(View.INVISIBLE);

        */


        /*artWorld = findViewById(R.id.artworld);


        getSerachedResults = findViewById(R.id.LogSearchIconGo);

        viewSearchField = findViewById(R.id.logSearchIcon);

        textInputLayoutSearc = findViewById(R.id.searchTextInputLayout);
        textInputLayoutSearc.setEnabled(false);
        textInputLayoutSearc.setVisibility(View.INVISIBLE);
        searchValueField = findViewById(R.id.searchEdittext);
        exitMaxCardView = findViewById(R.id.hideUserRecyclerviewMax);

        hideSearchRecyclerView = findViewById(R.id.hideSearchRecyclerView);
        hideSearchRecyclerView.setEnabled(false);
        hideSearchRecyclerView.setVisibility(View.INVISIBLE);
        hideUserRecyclerView = findViewById(R.id.hideUserRecyclerview);
        musicWorld = findViewById(R.id.musicWorld);
        fashionWorld = findViewById(R.id.fashionWorld);
        businessWorld = findViewById(R.id.businessWorld);
         noSearcFound = findViewById(R.id.noSearcFOundText);
        noSearcFound.setVisibility(View.INVISIBLE);
        mapsRecyclerView = findViewById(R.id.mapsRecyclerView);
        mapSearchRecyclerView = findViewById(R.id.mapsSearchRecyclerView);
        mRecyclerView = findViewById(R.id.mapsSearchRecyclerViewMax);
        myCart = findViewById(R.id.myCart);*/


        TagRef_y = FirebaseDatabase.getInstance().getReference("liveEventsAndRipples");

        //mapsRecyclerView.setHasFixedSize(true);
        //mapSearchRecyclerView.setHasFixedSize(true);

        searchIndicatoIcon = findViewById(R.id.nearbyLocation);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManagerR = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager linearLayoutManagerProfileCategory = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        //LinearLayoutManager linearLayoutManagerX = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        //mapsRecyclerView.setLayoutManager(linearLayoutManager);
        //mapSearchRecyclerView.setLayoutManager(linearLayoutManagerR);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerViewProfileCategory.setLayoutManager(linearLayoutManagerProfileCategory);
        publicProfilesPostsRecyclerView.setLayoutManager(linearLayoutManagerR);


        uploads = new ArrayList<>();
        mUploadsProfileCategory = new ArrayList<>();

        ProfileUploads = new ArrayList<>();
        mUploads = new ArrayList<>();




        kUploads = new ArrayList<>();
        ViewProfilePostsUploads = new ArrayList<>();

        searchString = new ArrayList<>();

        searchString.add("nearby places");
        searchString.add("Services");
        searchString.add("live events");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,searchString);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(arrayAdapter);

        //pagiisLogoNoti.setVisibility(View.INVISIBLE);
        mAdapter = new mapsViewAdapter(MapsActivity.this, uploads);

        mAdapterProfileCategory = new ViewMapsProfileCategory(MapsActivity.this, mUploadsProfileCategory);
        viewProfilesAdapter = new ViewProfilePicsAdapter(MapsActivity.this, ViewProfilePostsUploads);
        viewAllPagiisProfilesAdapter = new ViewAllPagiisProfiles(MapsActivity.this,ProfileUploads);
        mapsAdapterNew = new mapSearchedItemAdaptor(MapsActivity.this, mUploads);
        sAdapter = new mapsProfileItemsViewAdaptor(MapsActivity.this, kUploads);
        sAdapter.setOnItemClickListener(MapsActivity.this);

        mAdapter.setOnItemClickListener(MapsActivity.this);

        mAdapterProfileCategory.setOnItemClickListener(MapsActivity.this);
        viewAllPagiisProfilesAdapter.setOnItemProfileClickListener(MapsActivity.this);
        mapsAdapterNew.setOnItemClickListener(MapsActivity.this);
        publicProfilePostsCardView.setVisibility(View.INVISIBLE);


        //mapsRecyclerView.setAdapter(mAdapter);
        // mapSearchRecyclerView.setAdapter(mapsAdapterNew);
        mRecyclerView.setAdapter(mAdapter);
        publicProfilesPostsRecyclerView.setAdapter(viewProfilesAdapter);
        mRecyclerViewProfileCategory.setAdapter(mAdapterProfileCategory);



        if (mAuth.getCurrentUser() == null) {

            Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Check Permission when the location is Switched off.
        myDialog = new Dialog(this);
        // mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);

        //recyclerProgressBarMax.setVisibility(View.INVISIBLE);
        //recyclerProgressBar.setVisibility(View.INVISIBLE);
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");
        mDatabaseRef_Z = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseRef_shareApp = FirebaseDatabase.getInstance().getReference().child("shareAppLink");

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();
            TagRef_x = FirebaseDatabase.getInstance().getReference().child("Tags").child(user_id);

        }

        tagedUsers = new ArrayList<>();
        tagedUserString = new ArrayList<tags>();

        //searchSpinner = findViewById(R.id.searchSpinner);

        if (mAuth.getCurrentUser() == null) {

            startLocationUpdates();

        }


       /* viewSearchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.logSearchIcon);

                textInputLayoutSearc.setVisibility(View.VISIBLE);
                textInputLayoutSearc.setEnabled(true);

            }
        });*/

        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                String selectedItem = searchSpinner.getSelectedItem().toString();

                if(selectedItem.compareTo("live events") == 0)
                {
                    searchIndicatoIcon.setImageResource(R.drawable.time_and_date);
                    mSearchText.setHint("search live events ");
                    searcValue = "Events";



                }else if(selectedItem.compareTo("Services") == 0)
                {
                    searchIndicatoIcon.setImageResource(R.drawable.tag_siroco);
                    mSearchText.setHint("Nearby services. ");
                    searcValue = "Services";

                }else if(selectedItem.compareTo("nearby places") == 0)
                {
                    searchIndicatoIcon.setImageResource(R.drawable.pagiis_onlineusers_finder);
                    mSearchText.setHint("Nearby places.");
                    searcValue = "nearby places";

                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    //bottomNavigationView.setVisibility(View.INVISIBLE);


                    try {

                        startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e)
                    {
                        Log.e(TAG, "onClick: GooglePlayServiceRepairableException" + e.getMessage());
                    } catch (GooglePlayServicesNotAvailableException e) {
                        Log.e(TAG, "onClick: GooglePlayServicesNotAvailable" + e.getMessage());
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

                searchIndicatoIcon = findViewById(R.id.nearbyLocation);
                mSearchText.setText("Pagiis search");

            }
        });


        /*pagiisLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.PAGiiS_ICON);

                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });*/


        /*siroccoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.PAGiiS_ICON);

                Intent intent = new Intent(MapsActivity.this, SiroccoPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });*/


        /*pagiisLogoNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.PAGiiS_ICON_NOTi);
                Intent intent = new Intent(MapsActivity.this, ActivityNotifications.class);
                startActivity(intent);

            }
        });*/

        /*exitMaxCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                v.findViewById(R.id.hideUserRecyclerviewMax);

                mRecyclerView.setVisibility(View.INVISIBLE);
                mapsViewSearcCardMax.setVisibility(View.INVISIBLE);


            }
        });*/

        /*hideUserImageDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                v.findViewById(R.id.hideUserMaxImage);
                userImageDp.setVisibility(View.INVISIBLE);
                exitMaxCardView.setVisibility(View.VISIBLE);
                hideUserImageDp.setVisibility(View.INVISIBLE);



            }
        });*/


        /*hideUserRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.hideUserRecyclerview);
                mapsViewCard.setVisibility(View.INVISIBLE);


            }
        });

        hideSearchRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.hideUserRecyclerview);
                mapsViewSearcCard.setVisibility(View.INVISIBLE);
                hideSearchRecyclerView.setVisibility(View.INVISIBLE);
                mapSearchRecyclerView.setVisibility(View.INVISIBLE);

            }
        });


        //deleteTagRef();

        radiusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked)
                {
                    // Get the custom layout view.
                    //onlineUserMarkerON = true;
                    //isUserMarkerChecked = true;
                    radiusView.setText("max: 1."+"Km");
                    Toast.makeText(MapsActivity.this, "Pagiis Max radius reached   1.Km !", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MapsActivity.this, "Online-user markers enabled.", Toast.LENGTH_SHORT).show();

                }else
                {

                    radiusView.setText("Std: 250"+"m");
                    isUserMarkerChecked = false;
                    onlineUserMarkerON = false;
                }

            }
        });


        getSerachedResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.LogSearchIconGo);

                if(!searchValueField.getText().toString().isEmpty() && searcValue.compareTo("services") ==0 )
                {

                    String searchedText = searchValueField.getText().toString();

                    progressBarSearc.setVisibility(View.VISIBLE);


                    if(!tagedUsers.isEmpty())
                    {

                        mapsViewSearcCard.setVisibility(View.VISIBLE);

                        for (int i = 0; i < tagedUsers.size(); i++)
                        {
                            tags x = tagedUsers.get(i);
                            final String taged_id = x.getUser_tagID();

                            mDatabaseRef_Y = FirebaseDatabase.getInstance().getReference().child("Tags").child(user_id);
                            Query mSearchQuery = mDatabaseRef_Y.orderByChild("name").startAt(searchedText).endAt(searchedText + "\uf8ff");
                            final String userIdRef = mAuth.getCurrentUser().getUid();

                            mDBlistener =mSearchQuery.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists())
                                    {
                                        //mapsViewSearcCardMax.setVisibility(View.VISIBLE);
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        // recyclerProgressBar.setVisibility(View.INVISIBLE);
                                        uploads.clear();
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                        {
                                            ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                                            upload.setKey(postSnapshot.getKey());
                                            uploads.add(upload);

                                        }

                                        mAdapter.notifyDataSetChanged();
                                        //recyclerProgressBar.setVisibility(View.INVISIBLE);
                                        mapsViewCard.setVisibility(View.VISIBLE);
                                        //mapsRecyclerView.setVisibility(View.VISIBLE);
                                    }else
                                    {

                                        whatAreyouLookingFor();

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError)
                                {
                                    Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    //recyclerProgressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }else
                    {

                        Toast.makeText(MapsActivity.this, "search field empty !! ", Toast.LENGTH_SHORT).show();

                    }


                   // getSearchedDataNormally(searchedText);

                }else if(!searchValueField.getText().toString().isEmpty() && searcValue.compareTo("live Events") ==0)
                {
                    String searchedText = searchValueField.getText().toString();

                    progressBarSearc.setVisibility(View.VISIBLE);


                        String user_id = mAuth.getCurrentUser().getUid();
                        TagRef_y = FirebaseDatabase.getInstance().getReference().child("PagiisLiveEvents");




                                Query mSearchQuery = TagRef_y.orderByChild("name").startAt(searchedText).endAt(searchedText + "\uf8ff");
                                final String userIdRef = mAuth.getCurrentUser().getUid();

                                 mDBlistener =mSearchQuery.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists())
                                    {
                                        //mapsViewSearcCardMax.setVisibility(View.VISIBLE);
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        // recyclerProgressBar.setVisibility(View.INVISIBLE);
                                        uploads.clear();
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                        {
                                            ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                                            upload.setKey(postSnapshot.getKey());
                                            uploads.add(upload);

                                        }

                                        mAdapter.notifyDataSetChanged();
                                        //recyclerProgressBar.setVisibility(View.INVISIBLE);
                                        mapsViewCard.setVisibility(View.VISIBLE);
                                        //mapsRecyclerView.setVisibility(View.VISIBLE);
                                    }else
                                    {

                                        whatAreyouLookingFor();

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError)
                                {
                                    Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    //recyclerProgressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                         }

            }
        }); /*


        /*logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.findViewById(R.id.logOuticon);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.logout_dialog, null);

                ImageView logoutImage = mView.findViewById(R.id.SignOutImage);
                ImageView cancelLogOutImage = mView.findViewById(R.id.cancelSignOutImage);


                logoutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.findViewById(R.id.SignOutImage);
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MapsActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                });

                cancelLogOutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });*/


        /*mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.findViewById(R.id.radiusUsersIcon);

                updateArrayListLive();
                recyclerProgressBar.setVisibility(View.VISIBLE);
                recyclerProgressBarMax.setVisibility(View.VISIBLE);


                //onlineUserLocation();
            }
        });*/

        //setupBottomNavigationView();


        //getSearch();


        /*artWorld.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)

            {
                view.findViewById(R.id.artworld);
                Intent intent = new Intent(MapsActivity.this,ViewUsersMemes.class);
                startActivity(intent);
            }
        });*/



        /*businessWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {

                view.findViewById(R.id.businessWorld);
                Intent intent = new Intent(MapsActivity.this, MemePage.class);
                startActivity(intent);


            }
        });*/


        /*musicWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                view.findViewById(R.id.musicWorld);

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = View.inflate(MapsActivity.this,R.layout.activity_user_post,null);

                ImageView logoutImage = mView.findViewById(R.id.cameraOption);
                //ImageView postLink = mView.findViewById(R.id.linkEditImageView);
                ImageView cancelLogOutImage = mView.findViewById(R.id.GalleryOption);
                ImageView homebutton = mView.findViewById(R.id.homeButotn);


                /*postLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        view.findViewById(R.id.linkEditImageView);
                        Intent intent = new Intent(getApplicationContext(),LinkPost.class);
                        intent.putExtra("share_item_id", "null");
                        intent.putExtra("share_item_id_key", "null");
                        MapsActivity.this.startActivity(intent);

                    }
                });

                homebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.findViewById(R.id.homeButotn);
                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        MapsActivity.this.startActivity(intent);
                        finish();


                    }
                });


                logoutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.findViewById(R.id.cameraOption);
                        Intent intent = new Intent(getApplicationContext(), ActivityUploadVideo.class);
                        intent.putExtra("visit_user_id","fromVideoView");
                        MapsActivity.this.startActivity(intent);


                    }
                });

                cancelLogOutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        view.findViewById(R.id.GalleryOption);
                        Intent intent = new Intent(getApplicationContext(),ActivityUploadImage.class);
                        intent.putExtra("share_item_id","null");
                        MapsActivity.this.startActivity(intent);

                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });*/


        /*userImageDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                view.findViewById(R.id.musicWorld);

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = View.inflate(MapsActivity.this,R.layout.activity_user_post,null);

                ImageView logoutImage = mView.findViewById(R.id.cameraOption);
                ImageView postLink = mView.findViewById(R.id.linkEditImageView);
                ImageView cancelLogOutImage = mView.findViewById(R.id.GalleryOption);
                ImageView homebutton = mView.findViewById(R.id.homeButotn);


                postLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        view.findViewById(R.id.linkEditImageView);
                        Intent intent = new Intent(getApplicationContext(),LinkPost.class);
                        intent.putExtra("share_item_id", "null");
                        MapsActivity.this.startActivity(intent);

                    }
                });

                homebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.findViewById(R.id.homeButotn);
                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        MapsActivity.this.startActivity(intent);


                    }
                });


                logoutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.findViewById(R.id.cameraOption);
                        Intent intent = new Intent(getApplicationContext(), ActivityCamera.class);
                        intent.putExtra("idUrl","null");
                        MapsActivity.this.startActivity(intent);


                    }
                });

                cancelLogOutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        view.findViewById(R.id.GalleryOption);
                        Intent intent = new Intent(getApplicationContext(),ActivityUploadImage.class);
                        intent.putExtra("share_item_id","null");
                        intent.putExtra("share_item_id_key","null");

                        MapsActivity.this.startActivity(intent);

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });*/




        /*fashionWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                view.findViewById(R.id.fashionWorld);

                Intent intent = new Intent(MapsActivity.this, ActivityNotifications.class);
                intent.putExtra("share_item_id", "no");
                startActivity(intent);


            }
        });*/


        /*myCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                view.findViewById(R.id.myCart);

                String userId = mAuth.getCurrentUser().getUid();
                Intent intent = new Intent(MapsActivity.this, ActivityOwnProfile.class);
                intent.putExtra("visit_user_id",userId);
                startActivity(intent);
            }
        });*/



        getSerachedResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.LogSearchIconGo);
                if(!mSearchText.getText().toString().isEmpty() && searcValue.compareTo("Services") ==0 )
                {

                    String searchedText = mSearchText.getText().toString();

                   // progressBarSearc.setVisibility(View.VISIBLE);
                    GetMapsOnlineProfileByCategory(searchedText);

                    // getSearchedDataNormally(searchedText);

                }else if(!mSearchText.getText().toString().isEmpty() && searcValue.compareTo("live events") ==0)
                {
                    String searchedText = mSearchText.getText().toString();

                    //progressBarSearc.setVisibility(View.VISIBLE);

                    GetMapsOnlineProfileByCategory(searchedText);
                }

            }
        });




            init();

            //getAutoCompletPlaceSuggestion();

            GetMapsOnlineProfile();
            filterProfileByCategory();
            //checkDataAvalability();
            //getDataNormally();
            getShareAppLink();
            createNotificationChannel();






    } // Oncreate Ends Here

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = MyAppConstants.CHANNEL_ID;
            String channelName = "Pagiis request for admin product verification.";
            String channelDescription = "Admin is requested to handle the verification of Pagiis online shopped products.";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void getShareAppLink()
    {

        mDatabaseRef_shareApp  = FirebaseDatabase.getInstance().getReference().child("shareAppLink");

        mDatabaseRef_shareApp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {

                    shareAppLink = dataSnapshot.getValue().toString();
                    //publicProfilePostsCardView.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                recyclerProgressBar.setVisibility(View.INVISIBLE);
            }
        });



    }

    private void getDataNormally()
    {
        lockedImages  = FirebaseDatabase.getInstance().getReference().child("Uploads");

        final String userIdRef = mAuth.getCurrentUser().getUid();
        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        if(!tagedUserString.isEmpty())
        {
            for (int i = 0; i < tagedUserString.size(); i++)
            {

                tags x = tagedUserString.get(i);

                final String taged_id = x.getUser_tagID();

                Query mSearchQuery = lockedImages.startAt(taged_id).endAt(taged_id + "\uf8ff");

                mSearchQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {

                            viewProfileUploads.clear();
                           // recyclerProgressBar.setVisibility(View.INVISIBLE);

                                ImageUploads upload = dataSnapshot.getValue(ImageUploads.class);
                                upload.setKey(dataSnapshot.getKey());
                                viewProfileUploads.add(upload);


                            Collections.reverse(viewProfileUploads);

                            viewProfilesAdapter.notifyDataSetChanged();
                            //publicProfilePostsCardView.setVisibility(View.VISIBLE);


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        recyclerProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }



    private void checkDataAvalability()

    {

        lockedImages  = FirebaseDatabase.getInstance().getReference().child("Uploads");
        final String userIdRef = mAuth.getCurrentUser().getUid();
        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        if(!tagedUsers.isEmpty())
        {
            for (int i = 0; i < tagedUsers.size(); i++)
            {

                tags x = tagedUsers.get(i);

                final String taged_id = x.getUser_tagID();

                lockedImages.child(taged_id).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("MissingInflatedId")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())

                        {

                            // recyclerProgressBar.setVisibility(View.INVISIBLE);

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                            {

                                String localID = postSnapshot.getKey();

                                tagedUserString.add(new tags(localID));

                                if (tagedUserString.size() == postSnapshot.getChildrenCount())
                                {
                                    getDataNormally();
                                }

                            }

                        }else
                        {

                            if (bottomSheetDialog != null && bottomSheetDialog.isShowing())
                            {

                                bottomSheetDialog.dismiss();

                                View bottomSheetView = LayoutInflater.from(MapsActivity.this).inflate(R.layout.bottom_sheet_expore, findViewById(R.id.mapsBottomSheetCOntainer));
                                bottomSheetView.findViewById(R.id.popUpPostOption).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v)
                                    {

                                        oldFragment = null;
                                        // Create a new instance of your target fragment
                                        //GalleryFragment newFragment = new GalleryFragment();
                                        // Get the FragmentManager
                                        //FragmentManager fragmentManager = getSupportFragmentManager(); // Or use getFragmentManager() if you're not in an AppCompatActivity
                                        // Begin a transaction
                                        //FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        // Replace the current fragment with the new one
                                        //transaction.replace(R.id.mainContainer, newFragment);
                                        // Optionally, add the transaction to the back stack
                                        //transaction.addToBackStack(null);
                                        // Commit the transaction
                                        //transaction.commit();
                                        Intent intent = new Intent(getApplicationContext(),GalleryUploads.class);
                                        startActivity(intent);
                                        finish();
                                        //oldFragment = null;
                                        //publicProfilesCardview.setVisibility(View.INVISIBLE);

                                        bottomSheetDialog.dismiss();
                                    }
                                });


                                bottomSheetView.findViewById(R.id.popUpOnlineShoppingOption).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        oldFragment = null;
                                        // Create a new instance of your target fragment
                                        SiroccoFragment newFragment = new SiroccoFragment();

                                        // Get the FragmentManager
                                        FragmentManager fragmentManager = getSupportFragmentManager(); // Or use getFragmentManager() if you're not in an AppCompatActivity

                                        // Begin a transaction
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                                        // Replace the current fragment with the new one
                                        transaction.replace(R.id.mainContainer, newFragment);

                                        // Optionally, add the transaction to the back stack
                                        transaction.addToBackStack(null);

                                        // Commit the transaction
                                        publicProfilesCardview.setVisibility(View.INVISIBLE);
                                        bottomSheetDialog.dismiss();
                                        transaction.commit();
                                    }

                                });

                                bottomSheetView.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v)
                                    {

                                        bottomSheetDialog.dismiss();

                                    }
                                });


                                bottomSheetView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_SEND);
                                        intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family please care to check out this amazing App called Pagiis:    "+ Uri.parse(shareAppLink));
                                        intent.setType("text/plain");

                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(intent);
                                        }


                                    }
                                });

                                if(bottomSheetDialog == null && !bottomSheetDialog.isShowing()  )

                                {
                                    bottomSheetDialog.setContentView(bottomSheetView);
                                    bottomSheetDialog.show();

                                }




                                // The BottomSheetDialog is currently visible or active
                                // Put your code here for the case when the BottomSheetDialog is active
                            } else {
                                View bottomSheetView = LayoutInflater.from(MapsActivity.this).inflate(R.layout.bottom_sheet_expore, findViewById(R.id.mapsBottomSheetCOntainer));

                                bottomSheetView.findViewById(R.id.popUpPostOption).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        oldFragment = null;
                                        // Create a new instance of your target fragment
                                        GalleryFragment newFragment = new GalleryFragment();

                                        // Get the FragmentManager
                                        FragmentManager fragmentManager = getSupportFragmentManager(); // Or use getFragmentManager() if you're not in an AppCompatActivity

                                        // Begin a transaction
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                                        // Replace the current fragment with the new one
                                        transaction.replace(R.id.mainContainer, newFragment);

                                        // Optionally, add the transaction to the back stack
                                        transaction.addToBackStack(null);

                                        // Commit the transaction
                                        publicProfilesCardview.setVisibility(View.INVISIBLE);
                                        bottomSheetDialog.dismiss();
                                        transaction.commit();
                                    }


                                });


                                bottomSheetView.findViewById(R.id.popUpOnlineShoppingOption).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        oldFragment = null;
                                        // Create a new instance of your target fragment
                                        SiroccoFragment newFragment = new SiroccoFragment();

                                        // Get the FragmentManager
                                        FragmentManager fragmentManager = getSupportFragmentManager(); // Or use getFragmentManager() if you're not in an AppCompatActivity

                                        // Begin a transaction
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                                        // Replace the current fragment with the new one
                                        transaction.replace(R.id.mainContainer, newFragment);

                                        // Optionally, add the transaction to the back stack
                                        transaction.addToBackStack(null);

                                        // Commit the transaction
                                        publicProfilesCardview.setVisibility(View.INVISIBLE);
                                        bottomSheetDialog.dismiss();
                                        transaction.commit();
                                    }

                                });
                                bottomSheetDialog.setContentView(bottomSheetView);
                                bottomSheetDialog.show();

                            }

                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        recyclerProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }else

        {

            publicProfilePostsCardView.setVisibility(View.INVISIBLE);
            //siroccoView();
            Toast.makeText(this, "There are no Post Items", Toast.LENGTH_SHORT).show();

        }

    }


    public void switchContent(Fragment fragment) {
        Fragment oldFragment = getSupportFragmentManager().findFragmentById(R.id.homeFragment);
        if (oldFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(oldFragment).commit();
        }
        //mContent = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContainer, fragment)
                .commit();
        //getSlidingMenu().showContent();
    }


    //HideSoftkey Method

    private void siroccoView() {
        mDatabaseRef_Y = FirebaseDatabase.getInstance().getReference().child("WalkinWall");
        mDatabaseRef_Y.limitToLast(50).addValueEventListener(new ValueEventListener() {
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
                    recyclerProgressBar.setVisibility(View.INVISIBLE);
                    mapsViewSearcCardMax.setVisibility(View.VISIBLE);
                    userImageDp.setVisibility(View.INVISIBLE);
                    hideUserImageDp.setVisibility(View.INVISIBLE);
                    exitMaxCardView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                recyclerProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // super.onActivityResult(requestCode, resultCode, data);
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(MapsActivity.this, data);


                // PendingResult<PlaceBuffer> placeBufferPendingResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, place.getId());

                // PendingResult<PlaceBuffer> placeResult1 = new Places.GeoDataApi.getPlaceById(\)

                //placeBufferPendingResult.setResultCallback(mUpdatePlaceDetailCallback);
                //bottomNavigationView.setVisibility(View.INVISIBLE);

            }

        } else if (requestCode == PLACE_PICKER_REQUEST_Two) {
            if (requestCode == PLACE_PICKER_REQUEST_Two) {
                if (resultCode == RESULT_OK) {
                    Place place = (Place) Autocomplete.getPlaceFromIntent(data);
                    String placeId = place.getId();


                    // PendingResult<PlaceBuffer> placeBufferPendingResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, place.getId());

                    // PendingResult<PlaceBuffer> placeResult1 = new Places.GeoDataApi.getPlaceById(\)

                    //placeBufferPendingResult.setResultCallback(mUpdatePlaceDetailCallback);

                    // Use the place ID to get more details
                    // with a Place Details request


                    PlacesClient placesClient = Places.createClient(getApplicationContext());

                    List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.ADDRESS);

                    FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();


                    //PendingResult<PlaceBuffer> placeResult = new Places.getPlaceById(mGoogleApiClient,placeId);
                    //PendingResult<PlaceBuffer> placeBufferPendingResults = new Places.GeoDataApi.getPlaceById();
                    // Use fetchPlace method to fetch details
                    placesClient.fetchPlace(request).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FetchPlaceResponse response = task.getResult();
                            Place place1 = (Place) response.getPlace();
                            Log.i("Place Details", "Name: " + place1.getName());
                            Log.i("Place Details", "Address: " + place1.getAddress());
                            // Add code to process the place details here
                        } else {
                            Exception exception = task.getException();
                            Log.e("Place Details", "Place not found: " + exception.getMessage());
                            // Handle the exception
                        }
                    });


                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i(TAG, status.getStatusMessage());
                }
            }


        }


    }

    private void getSearchedDataNormally(String searchedtext) {

        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        if (!tagedUsers.isEmpty()) {

            mapsViewSearcCard.setVisibility(View.VISIBLE);

            for (int i = 0; i < tagedUsers.size(); i++) {
                tags x = tagedUsers.get(i);
                final String taged_id = x.getUser_tagID();

                mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(taged_id);
                final String userIdRef = mAuth.getCurrentUser().getUid();

                mDatabaseRef.orderByChild("name").startAt(searchedtext).endAt(searchedtext + "\uf8ff").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {


                            mUploads.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ImageUploads upload = ds.getValue(ImageUploads.class);
                                upload.setKey(ds.getKey());
                                mUploads.add(upload);
                            }

                            mapSearchRecyclerView.setVisibility(View.VISIBLE);
                            mapSearchRecyclerView.setEnabled(true);
                            mapsAdapterNew.notifyDataSetChanged();

                            // sAdapter.notifyDataSetChanged();
                            progressBarSearc.setVisibility(View.INVISIBLE);
                            //mProgressCircle.setVisibility(View.INVISIBLE);
                        } else {
                            noSearcFound.setVisibility(View.VISIBLE);

                            mapSearchRecyclerView.setVisibility(View.VISIBLE);
                            hideSearchRecyclerView.setVisibility(View.VISIBLE);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgressCircle.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    public void GetMapsOnlineProfile()
    {

        //getDataNormally();

        if(mAuth != null)
        {
            mAuth.getCurrentUser().getUid();
            String user_id = mAuth.getCurrentUser().getUid();

            TagRef_x = FirebaseDatabase.getInstance().getReference().child("Tags").child(user_id);

            mDBlistener = TagRef_x.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists())
                    {

                        //mapsViewSearcCardMax.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        // recyclerProgressBar.setVisibility(View.INVISIBLE);
                        uploads.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                            upload.setKey(postSnapshot.getKey());
                            upload.setShare(postSnapshot.getKey());
                            uploads.add(upload);

                        }

                        //Collections.reverse(uploads);
                        Collections.shuffle(mUploads);
                        mAdapter.notifyDataSetChanged();
                        //recyclerProgressBar.setVisibility(View.INVISIBLE);
                        mapsViewCard.setVisibility(View.VISIBLE);
                        //mapsRecyclerView.setVisibility(View.VISIBLE);
                    }else
                    {

                        GetMapsOnlineProfileByCategoryAll(profileCategoryValue);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    //recyclerProgressBar.setVisibility(View.INVISIBLE);
                }
            });

        }

    }


    public void filterProfileByCategory()
    {

        //getDataNormally();

        if(mAuth != null)
        {
            mAuth.getCurrentUser().getUid();
            mDatabaseRef_Y = FirebaseDatabase.getInstance().getReference().child("ProfileServices");


            mDBlistener =mDatabaseRef_Y.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists())
                    {

                        //mapsViewSearcCardMax.setVisibility(View.VISIBLE);
                        //mRecyclerView.setVisibility(View.VISIBLE);
                        // recyclerProgressBar.setVisibility(View.INVISIBLE);
                        mUploadsProfileCategory.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                            upload.setKey(postSnapshot.getKey());
                            mUploadsProfileCategory.add(upload);

                        }

                        //Collections.reverse(mUploadsProfileCategory);
                        Collections.shuffle(mUploadsProfileCategory);
                        mAdapterProfileCategory.notifyDataSetChanged();
                        //publicProfilePostsCardView.setVisibility(View.VISIBLE);

                    }else
                    {

                        whatAreyouLookingFor();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    //recyclerProgressBar.setVisibility(View.INVISIBLE);
                }
            });

        }else

        {
            Toast.makeText(MapsActivity.this, "No data", Toast.LENGTH_SHORT).show();

        }

    }


    public void GetMapsOnlineProfileByCategory(String profileCategory)
    {

        //getDataNormally();

        if(mAuth != null && profileCategory != null && !profileCategory.isEmpty())
        {
            mAuth.getCurrentUser().getUid();
            String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseRef_Y = FirebaseDatabase.getInstance().getReference().child("Tags").child(user_id);
            Query mSearchQuery = mDatabaseRef_Y.orderByChild("exRating").startAt(profileCategory).endAt(profileCategory + "\uf8ff");

            mDBlistener =mSearchQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists())
                    {
                        //mapsViewSearcCardMax.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        // recyclerProgressBar.setVisibility(View.INVISIBLE);
                        uploads.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                            upload.setKey(postSnapshot.getKey());
                            uploads.add(upload);

                        }

                        mAdapter.notifyDataSetChanged();
                        //recyclerProgressBar.setVisibility(View.INVISIBLE);
                        //Collections.reverse(mUploadsProfileCategory);
                        Collections.shuffle(uploads);
                        mapsViewCard.setVisibility(View.VISIBLE);
                        //mapsRecyclerView.setVisibility(View.VISIBLE);
                    }else
                    {

                        whatAreyouLookingFor();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    //recyclerProgressBar.setVisibility(View.INVISIBLE);
                }
            });

        }else

        {
            Toast.makeText(MapsActivity.this, "Profiles with matching services unavailable in your local.", Toast.LENGTH_SHORT).show();

        }

    }



    public void GetMapsOnlineProfileByCategoryAll(String profileCategory)
    {

        //getDataNormally();


            mAuth.getCurrentUser().getUid();
            String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseRef_Z = FirebaseDatabase.getInstance().getReference().child("ProfessionalProfiles");
            Query mSearchQuery = mDatabaseRef_Z.orderByChild("exRating").startAt(profileCategory).endAt(profileCategory + "\uf8ff");

            mDBlistener =mSearchQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists())
                    {
                        //mapsViewSearcCardMax.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        // recyclerProgressBar.setVisibility(View.INVISIBLE);
                        uploads.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                            upload.setKey(postSnapshot.getKey());
                            uploads.add(upload);

                        }

                        mAdapter.notifyDataSetChanged();
                        //recyclerProgressBar.setVisibility(View.INVISIBLE);
                        //Collections.reverse(mUploadsProfileCategory);
                        Collections.shuffle(uploads);
                        mapsViewCard.setVisibility(View.VISIBLE);
                        //mapsRecyclerView.setVisibility(View.VISIBLE);
                    }else
                    {


                            Toast.makeText(MapsActivity.this, "Profiles with matching services unavailable in your local.", Toast.LENGTH_SHORT).show();


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    //recyclerProgressBar.setVisibility(View.INVISIBLE);
                }
            });



    }

    public void GetPagiisProfiles()
    {


        if(mAuth != null)
        {
            mAuth.getCurrentUser().getUid();
            String user_id = mAuth.getCurrentUser().getUid();

            TagRef_x = FirebaseDatabase.getInstance().getReference().child("Users");

            mDBlistener = TagRef_x.limitToLast(50).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists())
                    {

                        //mapsViewSearcCardMax.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        // recyclerProgressBar.setVisibility(View.INVISIBLE);
                        uploads.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                            upload.setKey(postSnapshot.getKey());
                            uploads.add(upload);

                        }

                        mAdapter.notifyDataSetChanged();
                        //recyclerProgressBar.setVisibility(View.INVISIBLE);
                        mapsViewCard.setVisibility(View.VISIBLE);
                        //mapsRecyclerView.setVisibility(View.VISIBLE);
                    }else
                    {

                        whatAreyouLookingFor();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    //recyclerProgressBar.setVisibility(View.INVISIBLE);
                }
            });

        }

    }



    private void whatAreyouLookingFor()

    {

            View bottomSheetView = LayoutInflater.from(MapsActivity.this).inflate(R.layout.bottom_sheet_explore_activities, findViewById(R.id.mapsBottomSheetCOntainer));
            bottomSheetView.findViewById(R.id.popUpPostOption).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    oldFragment = null;
                    // Create a new instance of your target fragment
                    //GalleryFragment newFragment = new GalleryFragment();
                    // Get the FragmentManager
                    //FragmentManager fragmentManager = getSupportFragmentManager(); // Or use getFragmentManager() if you're not in an AppCompatActivity
                    // Begin a transaction
                    //FragmentTransaction transaction = fragmentManager.beginTransaction();
                    // Replace the current fragment with the new one
                    //transaction.replace(R.id.mainContainer, newFragment);
                    // Optionally, add the transaction to the back stack
                    //transaction.addToBackStack(null);
                    // Commit the transaction
                    //transaction.commit();
                    Intent intent = new Intent(getApplicationContext(),GalleryUploads.class);
                    startActivity(intent);
                    finish();
                    //oldFragment = null;
                    //publicProfilesCardview.setVisibility(View.INVISIBLE);

                    bottomSheetDialog.dismiss();
                }
            });


        bottomSheetView.findViewById(R.id.explorePagiis).setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                GetMapsOnlineProfileByCategoryAll(profileCategoryValue);
                bottomSheetDialog.dismiss();

            }
        });


            bottomSheetView.findViewById(R.id.popUpOnlineShoppingOption).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    oldFragment = null;
                    // Create a new instance of your target fragment
                    SiroccoFragment newFragment = new SiroccoFragment();

                    // Get the FragmentManager
                    FragmentManager fragmentManager = getSupportFragmentManager(); // Or use getFragmentManager() if you're not in an AppCompatActivity

                    // Begin a transaction
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    // Replace the current fragment with the new one
                    transaction.replace(R.id.mainContainer, newFragment);

                    // Optionally, add the transaction to the back stack
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    publicProfilesCardview.setVisibility(View.INVISIBLE);
                    bottomSheetDialog.dismiss();
                    transaction.commit();
                }

            });

            bottomSheetView.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    bottomSheetDialog.dismiss();

                }
            });


            bottomSheetView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family please care to check out this amazing App called Pagiis:    "+ Uri.parse(shareAppLink));
                    intent.setType("text/plain");

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }


                }
            });


                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();



            // The BottomSheetDialog is currently visible or active
            // Put your code here for the case when the BottomSheetDialog is active

    }

    private void getSearch(String searchedtext) {



        /*mDBlistener = TagRef_x.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())

                {
                    uploads.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        uploads.add(upload);

                    }
                    mAdapter.notifyDataSetChanged();
                    recyclerProgressBar.setVisibility(View.INVISIBLE);
                    mapsViewCard.setVisibility(View.VISIBLE);
                    mapsRecyclerView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                recyclerProgressBar.setVisibility(View.INVISIBLE);
            }
        });*/
    }

    //start timer function
    void startTimer() {
        cTimer = new CountDownTimer(3000000, 60000) {
            public void onTick(long millisUntilFinished) {
                //onlineUserLocation();
            }

            public void onFinish() {
            }
        };
        cTimer.start();
    }

    //cancel timer
    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    private void updateArrayListLive() {
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");
        final String currentUserId = mAuth.getCurrentUser().getUid();
        mDatabaseRef_x.child(currentUserId).limitToLast(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String localID = ds.getKey();

                        tagedUserString.add(new tags(localID));
                        //mapsViewSearcCardMax.setVisibility(View.VISIBLE);

                        //mDatabaseRef_x.child(localID).child("ArraListUpdated").setValue(true);

                        if (tagedUserString.size() == dataSnapshot.getChildrenCount()) {


                            //GetMapsOnlineProfile();
                            Toast.makeText(MapsActivity.this, "PAGiiS Online users updated !!", Toast.LENGTH_SHORT).show();
                            //getDataNormally();
                        }
                    }

                } else {
                    Toast.makeText(MapsActivity.this, "There are no online users nearby.", Toast.LENGTH_SHORT).show();
                    getLastLocation();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


                Toast.makeText(MapsActivity.this, databaseError.getMessage() + "\n" + "Pagiis failed to update tags.", Toast.LENGTH_SHORT).show();


                // Get the custom layout view.
               /* View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);

                TextView messageGrid = findViewById(R.id.customToastText);

                // Initiate the Toast instance.
                Toast toast = new Toast(getApplicationContext());
                messageGrid.setText("Pagiis online user update failed. Pagiis will restart activity");
                // Set custom view in toast.
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                finish();*/
            }

        });
    }


    private void updateArrayList() {
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");
        final String currentUserId = mAuth.getCurrentUser().getUid();
        mDatabaseRef_x.child(currentUserId).limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String localID = ds.getKey();

                        tagedUsers.add(new tags(localID));

                        //mDatabaseRef_x.child(localID).child("ArraListUpdated").setValue(true);

                        if (tagedUsers.size() == dataSnapshot.getChildrenCount())
                        {

                            getLastLocation();
                            checkDataAvalability();

                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


                Toast.makeText(MapsActivity.this, databaseError.getMessage() + "\n" + "Pagiis failed to update tags.", Toast.LENGTH_SHORT).show();


                // Get the custom layout view.
               /* View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);

                TextView messageGrid = findViewById(R.id.customToastText);

                // Initiate the Toast instance.
                Toast toast = new Toast(getApplicationContext());
                messageGrid.setText("Pagiis online user update failed. Pagiis will restart activity");
                // Set custom view in toast.
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                finish();*/
            }

        });
    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            // int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {

                        if (locationResult != null) {

                            Location location = locationResult.getLastLocation();
                            mLastLocation = location;

                            saveUserLocation();
                        }
                    }

                },
                Looper.myLooper());
    }


    private void saveUserLocation()
    {
        String userID = mAuth.getCurrentUser().getUid();

        getmDatabaseRef_lastLocation = FirebaseDatabase.getInstance().getReference("MyLastLocation").child(userID);

        DatabaseReference geoRef = FirebaseDatabase.getInstance().getReference("UsersLocation");

        GeoFire geoFire = new GeoFire(geoRef);
        geoFire.setLocation(userID, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));


        if (mCuurentUserMaker != null)
        {
            mCuurentUserMaker.remove();
        }

        currentUserLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentUserLocation);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCuurentUserMaker = mMap.addMarker(markerOptions);
        //move map camera
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, 18));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, 16.0f));


        Double latitude;
        Double longitude;

        latitude = mLastLocation.getLatitude();
        longitude = mLastLocation.getLongitude();

        geocoder = new Geocoder(this, Locale.getDefault());


        try {


            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            //String area = addresses.get(0).getLocality();
            String citi = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();


            myLastLocationDetails = address + "," + "," + citi + "," + "," + country;

            getmDatabaseRef_lastLocation.setValue(myLastLocationDetails);

            //bottomNavigationView.setSelectedItemId(R.drawable.add_update);

        } catch (IOException e) {

            e.printStackTrace();

        }

        /*isFirstTimeRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isFirstRun",true);

        if(isFirstTimeRun)
        {
            Intent intent = new Intent(MapsActivity.this,MainActivity.class);
            startActivity(intent);
        }else
        {

        }*/


        onlineUserLocation();


    }


    /*
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not
     * installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     */


    private void getAutoCompletPlaceSuggestion() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (mGoogleApiClient.isConnected()) {
            List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Collections.singletonList(com.google.android.libraries.places.api.model.Place.Field.ID);

            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, placeFields)
                    .build(this);


            startActivityForResult(intent, PLACE_PICKER_REQUEST_Two);


        }


    }

    private void init() {


            /*mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this,this)
                    .build();*/


        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);
        mSearchText.setAdapter(placeAutocompleteAdapter);


        if (!(mSearchText.getText() == null) && !(mSearchText.getText().toString() == "")) {


            mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener()
            {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
                {

                    if (i == EditorInfo.IME_ACTION_SEARCH
                            || i == EditorInfo.IME_ACTION_DONE
                            || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                        geolocate();

                    }

                    return false;
                }
            });

            HideSoftKeyboard();

        }


    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    private void geolocate() {

        searchedText = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);

        List<Address> locationList = new ArrayList<>();

        try {

            locationList = geocoder.getFromLocationName(searchedText, 1);

        } catch (IOException e) {

            Log.e(TAG, "Geolocator: IOexception" + e.getMessage());
        }

        if (locationList.size() > 0) {

            Address locationAddress = locationList.get(0);
            Log.d(TAG, "Gelocation found this location" + locationAddress.toString());

            userLocation = new LatLng(locationAddress.getLatitude(), locationAddress.getLongitude());

            //mMap.moveCamera(new LatLng(locationAddress.getLatitude(),locationAddress.getLongitude()),);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 19.0f));

            HideSoftKeyboard();


        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        startLocationUpdates();
        //onlineUserLocation();
    }

    private void HideSoftKeyboard() {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @SuppressLint({"RestrictedApi", "MissingPermission"})
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        if (checkPermissions()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            buildApiClient();

            mMap.setMyLocationEnabled(false);

            init();


        }
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        //startLocationUpdates();

        //onlineUserLocation();
    }

    private final double radius = 25;
    private boolean userFound = false;
    private String userFoundExited;
    private final int UsersOnline = 0;

    private synchronized void buildApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();
        mGoogleApiClient.connect();
    }


    private String mapsProfileUrl;
    private String mapsUsername;


    private String mapsProfileImageUrl;


    //Here We are going to create functions that will handle location-based searches
    //These functions will feed data onto the current userLocation field as point of reference
    //we get data from searched location picked by a user

    @Override
    public void onConnectionSuspended(int i) {
        stopLocationUpdates();
        mGoogleApiClient.connect();
    }

    //This fuction will be now disabled for now, for this is to make app fuction/behaves better

    private void tagUser() {

        final String user_id = mAuth.getCurrentUser().getUid();

        TagRef = FirebaseDatabase.getInstance().getReference().child("Tags").child(user_id).child(userFoundID);

        TagRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //String myUserId =  mAuth.getCurrentUser().getUid();
                // mLocationDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userFoundID);
                    /*TagRef.child("tagedId").setValue(userFoundID).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                      /`                             //getUserLocation();
                        }
                    });*/



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    //Removing user From Radius if ke is Exited
    private void removeUserFromRadius()

    {
        String user_id = mAuth.getCurrentUser().getUid();

        TagRef = FirebaseDatabase.getInstance().getReference().child("Tags").child(user_id);

        TagRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String  compareId = ds.getKey();

                        if (compareId.compareTo(userFoundExited) == 0) {

                            TagRef.child(userFoundExited).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    getLastLocation();
                                }
                            });
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                Toast.makeText(MapsActivity.this, "Pagiis failed to untag user.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

       /* mLocationDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }


    private void getUserLocation()

    {
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");

        mLocationDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        String currentUserId = mAuth.getCurrentUser().getUid();


        //String onlineUserMarkerName = "Online User";

        //final String[] onRadiusUserName_x = new String[1];

        if(!tagedUsers.isEmpty())

        {

            for(int i = 0; i < tagedUsers.size(); i++)
            {

                tags x = tagedUsers.get(i);

                String taged_id = x.getUser_tagID();

                mLocationDatabaseReference.child(taged_id).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        onlineUserMarkerName = dataSnapshot.child("userNameAsEmail").getValue().toString();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final  DatabaseReference foundUser = FirebaseDatabase.getInstance().getReference().child("UsersLocation").child(taged_id);

                foundUser.child("l").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)

                    {
                        final List<Object> map = (List<Object>) dataSnapshot.getValue();

                        double lati = 0;
                        double longi = 0;


                        if (map != null && map.get(0) != null && map.get(1) != null) {

                            lati = Double.parseDouble(map.get(0).toString());
                            longi = Double.parseDouble(map.get(1).toString());

                            //Toast.makeText(MapsActivity.this, "User Lati and Longi : "+lati + ":" + longi, Toast.LENGTH_LONG).show();
                        }
                               /*if (map.get(1) != null) {
                                    longi = Double.parseDouble(map.get(1).toString());
                                }*/
                        userLocation = new LatLng(lati, longi);
                        if (userFoundMarker != null) {
                            userFoundMarker.remove();
                        }

                        //userLocation = new LatLng(lati, longi);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(userLocation);
                        markerOptions.title(onlineUserMarkerName);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        userFoundMarker = mMap.addMarker(markerOptions);

                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 19));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18.0f));

                        //userFoundMarker = mMap.addMarker(new MarkerOptions().position(userFoundLatlng).title("Online User"));
                        //onlineUserLocation();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                        // Get the custom layout view.
                        View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);

                        TextView messageGrid = findViewById(R.id.customToastText);

                        // Initiate the Toast instance.
                        Toast toast = new Toast(getApplicationContext());

                        messageGrid.setText("Pagiis failed to get user Location"+"\n"+"Please Waite While Pagiis restart activity");
                        // Set custom view in toast.
                        toast.setView(toastView);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();
                        finish();


                        //Getting values failed
                        //Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
                        //...
                    }
                });

            }
        }
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation() {

        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if  (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,`c
            // int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {

                            mLastLocation = location;

                            if (mCuurentUserMaker != null) {
                                mCuurentUserMaker.remove();
                            }
                            currentUserLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(currentUserLocation);
                            markerOptions.title("Current Position");
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                            mCuurentUserMaker = mMap.addMarker(markerOptions);
                            //move map camera
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, 19));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, 18.0f));
                            // Toast.makeText(MapsActivity.this, "Latitude : " + mLastLocation.getLatitude() + "  & Longitude : " + mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();
                            //onLocationChanged(location);
                            //onlineUserLocation();

                        } else {

                            Toast.makeText(MapsActivity.this, "PAGiiS Can Not find Location,please check your location setttings.", Toast.LENGTH_LONG).show();

                        }
                        // onLocationChanged(location);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Get the custom layout view.
                        View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);

                        TextView messageGrid = findViewById(R.id.customToastText);

                        // Initiate the Toast instance.
                        Toast toast = new Toast(getApplicationContext());

                        messageGrid.setText("Pagiis failed to get your last location,"+"\n"+"Please waite while pagiis restart activity.");
                        // Set custom view in toast.
                        toast.setView(toastView);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();
                        finish();

                        //Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        //e.printStackTrace();
                    }
                });
    }




   /* @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.equals(userFoundMarker)) {
            //Create an Alert dialogue that will return the select option for the clicked marker on Every Online-user
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            View view = getLayoutInflater().inflate(R.layout.view_from_maps_dialogue, null);

            dialogueTitle = view.findViewById(R.id.ChoosePageDialogue);
            memedialogueImageView = view.findViewById(R.id.memepage);
            dialogueImageView = view.findViewById(R.id.viewProfile);

            backToMapsbutton = view.findViewById(R.id.navigateBackMaps);

            //Might have to implement This on Success/ complete Listener On the Dialogue View
            backToMapsbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent mapsIntent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(mapsIntent);
                    finish();
                    return;

                }
            });
            dialogueImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    String on_maps_visited_user_id = userFoundID;
                    Intent userProfile = new Intent(getApplicationContext(), UserProfileActivity.class);
                    //This Line of code is used used for retrieving the user Id for profile Activity
                    userProfile.putExtra("visit_user_id", on_maps_visited_user_id);
                    startActivity(userProfile);
                    finish();
                    return;

                }
            });

            memedialogueImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String on_maps_visited_user_id = userFoundID;
                    userFoundID = mAuth.getCurrentUser().getUid();
                    Intent memePage = new Intent(getApplicationContext(), ViewUsersMemes.class);//Start memepage of the Viewed user
                    memePage.putExtra("visit_user_id", on_maps_visited_user_id);
                    startActivity(memePage);
                    finish();
                    return;

                }
            });

            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return true;
    }*/


    private final AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();


            PlacesClient placesClient = Places.createClient(getApplicationContext());

            List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.ADDRESS);

            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();


            //PendingResult<PlaceBuffer> placeResult = new Places.getPlaceById(mGoogleApiClient,placeId);
            //PendingResult<PlaceBuffer> placeBufferPendingResults = new Places.GeoDataApi.getPlaceById();
            // Use fetchPlace method to fetch details
            placesClient.fetchPlace(request).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FetchPlaceResponse response = task.getResult();
                    Place place = (Place) response.getPlace();
                    Log.i("Place Details", "Name: " + place.getName());
                    Log.i("Place Details", "Address: " + place.getAddress());
                    // Add code to process the place details here
                } else {
                    Exception exception = task.getException();
                    Log.e("Place Details", "Place not found: " + exception.getMessage());
                    // Handle the exception
                }
            });


        }
    };




    private DatabaseReference mLocationDatabaseReference;
    private DatabaseReference radiusUsers;
    private final ResultCallback<PlaceBuffer> mUpdatePlaceDetailCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {

            if (!places.getStatus().isSuccess()) {

                Log.d(TAG, "|Results for the places query did not result successfully" + places.getStatus());
                places.release();


            }

            final Place places1 = places.get(0);

            try {

                mPlace = new PlaceInfor();

                mPlace.setName(places1.getName().toString());
                mPlace.setAddress(places1.getAddress().toString());
                //mPlace.setAttribution(places1.getAttributions().toString());
                mPlace.setLatLng(places1.getLatLng());
                mPlace.setPhoneNumber(places1.getName().toString());
                mPlace.setWebsiteUrl(places1.getWebsiteUri());
                mPlace.setRating(places1.getRating());
                Log.d(TAG, "Onresult for place object" + mPlace.toString());

                currentUserLocation = places1.getLatLng();

            } catch (NullPointerException e) {

                Log.d(TAG, "Null Pointer exception for places object" + e.getMessage());

            }

            userLocation = new LatLng(places1.getViewport().getCenter().latitude, places1.getViewport().getCenter().longitude);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 19.0f));


            places.release();

        }
    };


    @Override
    protected void onStart() {
        super.onStart();

        //getLastLocation();
        startLocationUpdates();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        Fragment oldFragment = getSupportFragmentManager().findFragmentById(R.id.homeFragment);
        if (oldFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(oldFragment).commit();
        }



        deleteCache(getApplicationContext());
    }

    private void stopLocationUpdates()
    {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        //startLocationUpdates();

        getLastLocation();

        /*if(tagedUsers.isEmpty())
        {

            startLocationUpdates();

        }else{

            updateArrayList();
        }*/

        //onlineUserLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();

        Fragment oldFragment = getSupportFragmentManager().findFragmentById(R.id.homeFragment);
        if (oldFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(oldFragment).commit();
        }
        deleteCache(getApplicationContext());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopLocationUpdates();

        Fragment oldFragment = getSupportFragmentManager().findFragmentById(R.id.homeFragment);
        if (oldFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(oldFragment).commit();
        }

        deleteCache(getApplicationContext());

    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    @Override
    public void onItemClick(int position)

    {
        final ImageUploads selectedImage = uploads.get(position);

        final String imageUrl = selectedImage.getImageUrl();
        final String  userId = selectedImage.getKey();

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MapsActivity.this,R.style.BottomSheetDialogueTheme);

        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.botttom_sheet_layout,findViewById(R.id.bottomSheeContainer));

        ImageView profilePicture = bottomSheetView.findViewById(R.id.mapsItemProfile);

        TextView profileName = bottomSheetView.findViewById(R.id.popUpDescriptionTextViewTwo);
        TextView profileStatus = bottomSheetView.findViewById(R.id.popUpDescriptionTextViewThreee);
        TextView profileView = bottomSheetView.findViewById(R.id.popLocationTexview);
        SparkButton imageViewLikes = bottomSheetView.findViewById(R.id.likes);


        bottomSheetView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family please care to check out this amazing App called Pagiis:    "+ Uri.parse(shareAppLink));
                intent.setType("text/plain");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }


            }
        });

        bottomSheetView.findViewById(R.id.visitProfile).setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();

                // Put the data you want to send in the Bundle
                bundle.putString("visited_user_id", userId );

                // Create a new Fragment and set the arguments
                ProfileFragment targetFragment = new ProfileFragment();
                targetFragment.setArguments(bundle);

                // Now, you can navigate to the target Fragment
                FragmentManager fragmentManager = getSupportFragmentManager(); // or getFragmentManager() if not using support library

                fragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, targetFragment)
                        .commit();

                mapsDiscoverLayout.setVisibility(View.INVISIBLE);



            }
        });

        bottomSheetView.findViewById(R.id.shareImageView).setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family please care to check out this amazing App called Pagiis:    "+ Uri.parse(shareAppLink));
                intent.setType("text/plain");

                if (MapsActivity.this != null && getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    // There is an activity that can handle the intent

                    startActivity(intent);

                } else
                {
                    // There is no activity that can handle the intent
                }


            }
        });



        imageViewLikes.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(buttonState){


                    Toast.makeText(getApplicationContext(), "add to favourite!", Toast.LENGTH_SHORT).show();
                }else{


                    Toast.makeText(getApplicationContext(), "remove from favourite!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });




        if(!selectedImage.getName().isEmpty())
        {
            profileName.setText(selectedImage.getName());

        }

        if(!selectedImage.getViews().isEmpty() && selectedImage.getViews().length() <=6)
        {
            profileView.setText("Views  "+ selectedImage.getViews().length());
        }else
        {
            profileView.setText("Views");
        }

        if(!selectedImage.getPostLocation().isEmpty())
        {
            profileStatus.setText(selectedImage.getViews());
        }



        RequestOptions options = new RequestOptions();

        Glide.with(MapsActivity.this).load(imageUrl).apply(options.centerCrop()).thumbnail(0.75f).into(profilePicture);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();



    }

    @Override
    public void onNItemClick(int position)
    {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        Intent intent = new Intent(getApplicationContext(),PagiisMaxView.class);
        intent.putExtra("visit_user_id", selectedKey);
        startActivity(intent);

    }



    //The Following is for ViewProfilePic Adaptor for the Content received from public online users
    @SuppressLint("MissingInflatedId")
    @Override
    public void onClick(int position)

    {

        final ProfileViewHolder selectedImage = ProfileUploads.get(position);

        final String imageUrl = selectedImage.getUserImageProfilePic();
        final String  userId = selectedImage.getmKey();


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MapsActivity.this,R.style.BottomSheetDialogueTheme);

        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.botttom_sheet_layout,findViewById(R.id.bottomSheeContainer));

        ImageView profilePicture = bottomSheetView.findViewById(R.id.mapsItemProfile);

        TextView profileName = bottomSheetView.findViewById(R.id.popUpDescriptionTextViewTwo);
        TextView profileStatus = bottomSheetView.findViewById(R.id.popUpDescriptionTextViewThreee);
        TextView profileView = bottomSheetView.findViewById(R.id.popLocationTexview);
        TextView sharePagiis = bottomSheetView.findViewById(R.id.share);

        Button viewProfileButton = bottomSheetView.findViewById(R.id.visitProfile);


        SparkButton imageViewLikes = bottomSheetView.findViewById(R.id.likes);



        bottomSheetView.findViewById(R.id.shareImageView).setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family please care to check out this amazing App called Pagiis:    "+ Uri.parse(shareAppLink));
                intent.setType("text/plain");

                if (MapsActivity.this != null && getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    // There is an activity that can handle the intent

                    startActivity(intent);

                } else
                {
                    // There is no activity that can handle the intent
                }


            }
        });


        bottomSheetView.findViewById(R.id.explorePagiis).setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                GetMapsOnlineProfileByCategoryAll(profileCategoryValue);
                bottomSheetDialog.dismiss();

            }
        });





        sharePagiis.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family please care to check out this amazing App called Pagiis:    "+ Uri.parse(shareAppLink));
                intent.setType("text/plain");

                if (intent.resolveActivity(getPackageManager()) != null)
                {
                    startActivity(intent);
                }


            }
        });


        viewProfileButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();

                // Put the data you want to send in the Bundle
                bundle.putString("visited_user_id", userId);

                // Create a new Fragment and set the arguments
                ProfileFragment targetFragment = new ProfileFragment();
                targetFragment.setArguments(bundle);

                // Now, you can navigate to the target Fragment
                FragmentManager fragmentManager = getSupportFragmentManager(); // or getFragmentManager() if not using support library

                fragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, targetFragment)
                        .addToBackStack(null)
                        .commit();

                mapsDiscoverLayout.setVisibility(View.INVISIBLE);

            }
        });


        imageViewLikes.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(buttonState){


                    Toast.makeText(getApplicationContext(), "add to favourite!", Toast.LENGTH_SHORT).show();
                }else{


                    Toast.makeText(getApplicationContext(), "remove from favourite!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

        if(!(selectedImage.getUserName() == null) &&!selectedImage.getUserName().isEmpty() )
        {
            profileName.setText(selectedImage.getUserName());
        }


        if(!(imageUrl == null) &&!selectedImage.getUserImageProfilePic().isEmpty())

        {

            RequestOptions options = new RequestOptions();

            Glide.with(MapsActivity.this).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).apply(options.centerCrop()).thumbnail(0.75f).into(profilePicture);

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();


        }


    }

    @Override
    public void onWhatEverClickProfile(int position) {

    }

    @Override
    public void shareClickProfile(int position) {

    }

    @Override
    public void chatsClickProfile(int position) {

    }

    @Override
    public void
    onWhatEverClick(int position)
    {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getUserId();

        Intent intent = new Intent(getApplicationContext(),UserProfileActivity.class);
        intent.putExtra("visit_user_id", selectedKey);
        startActivity(intent);


    }

    @Override
    public void shareClick(int position)
    {

    }

    @Override
    public void chatsClick(int position) {

    }


    /*@Override
    public void onWhatEverClick(int position)
    {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getUserId();
        Intent intent = new Intent(getApplicationContext(),ActivityOwnProfile.class);
        intent.putExtra("visit_user_id", selectedKey);
        startActivity(intent);

    }*/


    @Override
    public void onDeleteClick(int position) {

        final ImageUploads selectedImage = uploads.get(position);

        final String selectedKey = selectedImage.getKey();

        String myUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Tags").child(myUserId);

        //StorageReference deletImage = mStorage.getReferenceFromUrl(selectedImage.getImageUrl());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot ds :dataSnapshot.getChildren())
                {
                    if(ds.getKey().compareTo(selectedKey)== 0)
                    {

                        mDatabaseRef.child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(MapsActivity.this, "Item sent to bin..", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MapsActivity.this, (CharSequence) e, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(MapsActivity.this, "item delete failed.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onViewProfile(int position) {
        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String getUserRef = selectedImage.getUserId();

        if(!getUserRef.isEmpty() & !selectedKey.isEmpty())
        {
            Intent intent = new Intent(MapsActivity.this,UserProfileActivity.class);
            intent.putExtra("visit_user_id", getUserRef);
            startActivity(intent);
        }
    }


    @Override
    public void itemClick(int position)
    {

        final ImageUploads selectedImage  =  kUploads.get(position);

        final String imageUrl = selectedImage.getImageUrl();
        final String  userId = selectedImage.getUserId();

        final String taged_id = selectedImage.getKey();

        RequestOptions options = new RequestOptions();
        hideUserImageDp.setVisibility(View.VISIBLE);
        exitMaxCardView.setVisibility(View.INVISIBLE);
        userImageDp.setVisibility(View.VISIBLE);

        Glide.with(MapsActivity.this).load(imageUrl).apply(options.centerCrop()).thumbnail(0.75f).into(userImageDp);


        lockedImages  = FirebaseDatabase.getInstance().getReference().child("Uploads").child(userId);

        lockedImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())

                {
                    kUploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        kUploads.add(upload);
                    }

                    sAdapter.notifyDataSetChanged();
                    recyclerProgressBar.setVisibility(View.INVISIBLE);
                    mapsViewSearcCardMax.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                recyclerProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void WhatEverClick(int position)

    {
        final ImageUploads selectedImage = kUploads.get(position);
        final String selectedKey = selectedImage.getUserId();
        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        intent.putExtra("visit_user_id", selectedKey);
        startActivity(intent);

    }

    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Maps View Places Picker Adapterview>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //stopLocationUpdates();\
        try {

            mGoogleApiClient.connect();

        } catch (NullPointerException exception)
        {
            Log.d(TAG, "The geo failed to locate" + exception.getMessage());
        }

    }

    private void onlineUserLocation()

    {

        final String currentUserId = mAuth.getCurrentUser().getUid();

        final DatabaseReference onlineUserRef = FirebaseDatabase.getInstance().getReference("UsersLocation");

        TagRef_x = FirebaseDatabase.getInstance().getReference().child("Tags").child(currentUserId);

        mDatabaseRef_Z = FirebaseDatabase.getInstance().getReference("Users");

        GeoFire geoFire = new GeoFire(onlineUserRef);

        final GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(currentUserLocation.latitude, currentUserLocation.longitude), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                //final String OnlineKey = mAuth.getCurrentUser().getUid();

                userFound = true;
                userFoundID = key;

                //We must put the |UserFound keys on an ArrayList so that we can be able to manage the number of users to be captured and displayed

                List<String> userTags = new ArrayList<>();

                userTags.add(userFoundID);

                //mapsRecyclerView.setVisibility(View.VISIBLE);

                if (userFoundID.compareTo(currentUserId) != 0) {
                    mDatabaseRef_Z.child(userFoundID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child("userImageDp").exists())
                            {
                                mapsProfileUrl = dataSnapshot.child("userImageDp").getValue().toString();

                            }

                            if (dataSnapshot.child("userNameAsEmail").exists())

                            {

                                mapsUsername = dataSnapshot.child("userNameAsEmail").getValue().toString();
                                userStatus = dataSnapshot.child("userDefaultStatus").getValue().toString();
                                userProfileServiceTag = dataSnapshot.child("proffession").getValue().toString();
                            }

                            int totalUsers = userTags.size(); // Assuming userTags contains all the users you're expecting to find
                            final int[] usersFound = {0};

                            for (String userFoundID : userTags)
                            {
                                ImageUploads upload = new ImageUploads(mapsUsername, mapsProfileUrl,userProfileServiceTag , userFoundID, userFoundID, userFoundID, userFoundID, "", "", userStatus);
                                TagRef_x.child(userFoundID).setValue(upload, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)

                                    {
                                        usersFound[0]++;
                                        usersFoundCountNumber[0]++;

                                        if (usersFound[0] == totalUsers)
                                        {
                                            // This block will execute when the last user is found
                                            // Put your code here for the last user found
                                            // ..

                                            updateArrayList();
                                        }
                                    }
                                });
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }


            @Override
            public void onKeyExited(String key) {
                userFoundExited = key;
                removeUserFromRadius();
                //Toast.makeText(MapsActivity.this, "This User " + userFoundExited + "has Exited", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }

            @Override
            public void onGeoQueryReady() {
                geoQuery.removeAllListeners();
                //getLastLocation();

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                mGoogleApiClient.disconnect();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)

    {
        getMenuInflater().inflate(R.menu.uploads_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if(item.getItemId() == R.id.action_add)
        {
            Intent intent = new Intent(MapsActivity.this, ActivityUploadImage.class);
            intent.putExtra("share_item_id","null");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemClickS(int position)

    {
        ImageUploads selectedImage = mUploadsProfileCategory.get(position);
        String User_id = mAuth.getCurrentUser().getUid();
        profileCategoryValue = selectedImage.getName();

        GetMapsOnlineProfileByCategory(profileCategoryValue);

    }

    @Override
    public void onWhatEverClickS(int position)

    {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)

    {
        super.onPointerCaptureChanged(hasCapture);
    }



    @Override
    public void onItemClickMapsProfileCategory(int position)


    {

        ImageUploads selectedImage = mUploads.get(position);

        selectedItem = selectedImage.getExRating();
        GetMapsOnlineProfileByCategory(profileCategoryValue);


    }

    @Override
    public void onWhatEverClickMapsProfileCategory(int position) {

    }
}