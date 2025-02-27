package pagiisnet.pagiisnet;

import static android.app.Activity.RESULT_OK;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.Manifest;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.net.InternetDomainName;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pagiisnet.pagiisnet.Utils.ViewStoreItemAdapter;
import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements ViewStoreItemAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private WebView webViewLinks;
    private WebSettings webSettings;


    private final static int Gallery_Pick = 1;
    private SparkButton imageviewAnim;
    private CircularImageView userImageDp;
    private ImageView userContactDetails;
    private TextView userName;
    private RecyclerView mRecyclerView;
    private ViewStoreItemAdapter mAdapter;
    private ImageView shareLinkButton;
    private ImageView closeLinkView;
    private CardView userMemelayout;
    private TextView Status;
    private TextView PostTitle;
    private int position;
    private WebView richLinkView;
    private ImageView changeDp;
    private List<ImageUploads> mUploads;
    private DatabaseReference userProfileInfor;
    private ImageView saveinfo;
    private ImageView editStatus;
    private ImageView AddContent;

    private ImageView viewProfileStore;


    private String StringCarrier;
    private String DeleteKeyAlternative;
    private FloatingActionButton videosButton;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_x;
    private DatabaseReference mDatabaseRef_y;
    private DatabaseReference getStoreProductUpdate;
    private ValueEventListener mDBlistener;
    private DatabaseReference mDatabaseUploads;
    private TextView logoutText;

    private TextView numberOfFollowers;
    private TextView followProfileTextView;

    private String userToken;

    private String  myImageDpUrl;

    private Fragment oldFragment;
    private ProgressBar mProgressBar;
    private ProgressBar mProgressBarWebview;
    private StorageTask mUploadTask;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private ImageView profileEMail, profileMobile, profileProffession, profileName;
    private TextView profileNameText, profileEmailText, profileMobileText, profileProffessionText;
    private TextView facebookLinkTextView, twitterLinkTextView, instagramLinkTextView;
    private StorageReference userImageStorageDpRef;
    private DatabaseReference getUserProfileDataRef;

    private TextView profileLikesTextView;

    private DatabaseReference mDatabaseRef_Y;
    private FloatingActionButton facebookIcon, twitterIcon, instagramIcon;
    private DatabaseReference usernameDataRef;
    private FirebaseAuth mAuth;
    private Uri resultUri;


    private DatabaseReference mDatabaseRef_shareApp;

    private String onlineUserId;

    private TextView profileViews;
    private String UrlString;
    private String UrlString2;
    private String UrlString3;

    private TextView profilePosts;

    String numberOfProfileLikes;

    private String shareAppLink;

    private AlertDialog dialog;


    private LinearLayout profileFramelayout;

    private TextView profileTags;

    private String user_profile_view_id;


    private ImageView mProgressCircle;
    private DatabaseReference databaseReferenceLocation;
    private DatabaseReference mDatabaseRef_Tokens;
    private DatabaseReference notificationReference;
    private String myName;
    private String myLastLocationDetails;
    private DatabaseReference mDatabaseRefLikes;

    private DatabaseReference mDatabaseRefFollowers;
    private String numberOfProfileFollowers;

    private ProgressBar progressBarFollow;
    private String notificationTitle;
    private String notificationMessage;
    private String following;


    public ProfileFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        FirebaseApp.initializeApp(getApplicationContext());





        mUploads = new ArrayList<>();

        /*if(haveNetwork()){

            Toast.makeText(this, "Your Device is Not connected to the internet please check mobile data"+"\n"+"and Wifi Connections", Toast.LENGTH_LONG).show();

        }*/
        mAuth = FirebaseAuth.getInstance();

        if( mAuth != null && !mAuth.getCurrentUser().getUid().isEmpty()){

            String User_id = mAuth.getCurrentUser().getUid();

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
            userProfileInfor = FirebaseDatabase.getInstance().getReference("Users").child(User_id);
            getStoreProductUpdate = FirebaseDatabase.getInstance().getReference("SiroccoSite");

        }


        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            // Check if the key you used to put the extra is present
            if (extras.containsKey("visited_user_id")) {
                String value = extras.getString("visited_user_id");
                user_profile_view_id = value;

                onlineUserId = user_profile_view_id;


            }
        }else if (extras == null)
        {
            onlineUserId = mAuth.getCurrentUser().getUid().toString();
        }







        if (mAuth.getCurrentUser() == null) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

        }

        //checkifAdmindTrue();

        getShareAppLink();


    }//End of the Oncreate function. The function scan now be initialted here.


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

            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }



    private void openStatusEditText() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.alert_status_edit, null);
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");

        final EditText newStatus = mView.findViewById(R.id.statusEdit);
        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);

        final String current_userId = mAuth.getCurrentUser().getUid();

        saveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveStatus);
                if (!(newStatus.getText() == null)) {
                    getUserProfileDataRef.child(current_userId).child("userDefaultStatus").setValue(newStatus.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            AlertDialog dialog = mBuilder.create();
                            Toast.makeText(getActivity(), "Status saved successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });


                } else {
                    Toast.makeText(getActivity(), "Please type in your status or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }




//Here we open a webview for the webiste of the pagiis profile which has an online shopping site inisde of pagiis
    @SuppressLint("RestrictedApi")
    private void profileProductWebview() {

        if (UrlString.compareTo("nulll") != 0)
        {



            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();

            customTabsIntent.launchUrl(requireActivity(), Uri.parse(UrlString));

            /*mProgressBarWebview.setVisibility(View.VISIBLE);
            webViewLinks.setVisibility(VISIBLE);

            webViewLinks.getSettings().setJavaScriptEnabled(true);
           // webViewLinks.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            webViewLinks.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            //webViewLinks.getSettings().setSupportZoom(true);
            //webViewLinks.getSettings().setUseWideViewPort(true);
            webViewLinks.getSettings().setLoadsImagesAutomatically(true);
            //webViewLinks.getSettings().setLoadWithOverviewMode(true);
            webViewLinks.setInitialScale(70);

            webViewLinks.getSettings().setSupportZoom(true);
            webViewLinks.getSettings().setBuiltInZoomControls(true);
            webViewLinks.getSettings().setDisplayZoomControls(false); //



            webViewLinks.getSettings().setDomStorageEnabled(true);
            webViewLinks.getSettings().setDatabaseEnabled(true);
            CookieManager.getInstance().setAcceptCookie(true);
            String desktopUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36";
            webViewLinks.getSettings().setUserAgentString(desktopUserAgent);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webViewLinks.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }


            //webViewLinks.getSettings().setDomStorageEnabled(true);
            //webViewLinks.setWebContentsDebuggingEnabled(true);
            //webViewLinks.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            //webViewLinks.setWebViewClient(new CustomWebViewClient());




            // Load the URL
            webViewLinks.loadUrl(UrlString);

            webViewLinks.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    if (view.getProgress() == 100) {
                        mProgressBarWebview.setVisibility(View.INVISIBLE);
                    }
                }
            });*/






            // Handle page loading progress
            /*webViewLinks.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    if (view.getProgress() == 100) {
                        mProgressBarWebview.setVisibility(View.INVISIBLE);
                    }
                }
            });

            // Handle geolocation permissions
            webViewLinks.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // Permission granted, allow geolocation
                        callback.invoke(origin, true, false);
                    } else {
                        // Request location permission
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    }
                }
            });

        }
    }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Reload the WebView to trigger geolocation
                    webViewLinks.reload();
                } else {
                    // Show a message to the user if the location permission is denied
                    Toast.makeText(requireContext(), "Location permission is required to use this feature.", Toast.LENGTH_SHORT).show();
                }
            }
        }

// Handle back button press in the Fragment
        @Override
        public void onResume () {
            super.onResume();
            requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (webViewLinks.canGoBack()) {
                        webViewLinks.goBack();
                    } else {
                        requireActivity().onBackPressed();
                    }
                }
            });*/




        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();

            // Check if the URL should be opened in the default browser

                // Open the URL in the default browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return true; // Return true to indicate that the URL is handled


            // Allow WebView to handle other URLs

        }
    }



    private void showBottomSheetDialog()

    {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogueTheme);
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_expore_profile_product, null);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View rootView = bottomSheetView.findViewById(R.id.bottomSheeContainerProfileProduct); // Replace with your root layout ID


        GifImageView siroccoArtView = bottomSheetView.findViewById(R.id.siroccoArt);
        GifImageView siroccoFashionView = bottomSheetView.findViewById(R.id.siroccoFashion);
        Button homeButton = bottomSheetView.findViewById(R.id.home);
        TextView sharePagiis = bottomSheetView.findViewById(R.id.share);


        SparkButton addToFavourite = bottomSheetView.findViewById(R.id.likes);

        addToFavourite.setEventListener(new SparkEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {


                    Toast.makeText(getApplicationContext(), "add to favourite!", Toast.LENGTH_SHORT).show();
                } else {


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


        siroccoArtView.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v)
            {

                if(UrlString.compareTo("nulll")!=0)
                {

                    Bundle bundle = new Bundle();

                    bundle.putString("visited_user_id", UrlString);

                    @SuppressLint("RestrictedApi") Intent intent = new Intent(getApplicationContext(), GalleryUploads.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    oldFragment = null;
                    bottomSheetDialog.dismiss();


                }else
                {

                    Toast.makeText(getActivity(), "Please make sure to open an online store before adding products", Toast.LENGTH_SHORT).show();
                }



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

                if (getActivity() != null && getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    // There is an activity that can handle the intent

                    startActivity(intent);

                } else
                {
                    // There is no activity that can handle the intent
                }


            }
        });


        siroccoFashionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {



                if(UrlString.compareTo("nulll")!=0)

                {



                    //mProgressCircle.setVisibility(View.VISIBLE);
                    webViewLinks.setVisibility(VISIBLE);
                    mProgressBarWebview.setVisibility(VISIBLE);
                    webViewLinks.loadUrl(UrlString);
                    webViewLinks.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    webViewLinks.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                    webViewLinks.getSettings().setSupportZoom(true);
                    webViewLinks.getSettings().setUseWideViewPort(true);
                    webViewLinks.getSettings().setLoadsImagesAutomatically(true);
                    webViewLinks.getSettings().setLoadWithOverviewMode(true);
                    webViewLinks.getSettings().setLoadWithOverviewMode(true);
                    webViewLinks.setInitialScale(1);

                    webViewLinks.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url)
                        {

                            if(view.getProgress() == 100)
                            {
                                //webViewLinks.setVisibility(INVISIBLE);
                                mProgressBarWebview.setVisibility(INVISIBLE);

                            }
                            // This method will be called when the page finishes loading
                            // You can put your code to check if it's done loading here
                            // For example, you can set a flag or perform some action
                        }
                    });

                    bottomSheetDialog.dismiss();


                }else
                {

                    Toast.makeText(getActivity(), "Please make sure to open an online store before you can access it", Toast.LENGTH_LONG).show();


                }


            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                //Code for the selected option
                bottomSheetDialog.dismiss();

            }
        });


        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }



    private void showBottomSheetDialogVisitStore()


    {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogueTheme);
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_expore_profile_product, null);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View rootView = bottomSheetView.findViewById(R.id.bottomSheeContainerProfileProduct); // Replace with your root layout ID

        GifImageView siroccoArtView = bottomSheetView.findViewById(R.id.siroccoArt);
        GifImageView siroccoFashionView = bottomSheetView.findViewById(R.id.siroccoFashion);
        Button homeButton = bottomSheetView.findViewById(R.id.home);
        TextView sharePagiis = bottomSheetView.findViewById(R.id.share);

        siroccoArtView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                webViewLinks.loadUrl(UrlString2);
                mProgressCircle.setVisibility(VISIBLE);

                webViewLinks.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url)
                    {

                        if(view.getProgress() == 100)
                        {
                            mProgressCircle.setVisibility(INVISIBLE);
                            profileFramelayout.setVisibility(INVISIBLE);

                        }
                        // This method will be called when the page finishes loading
                        // You can put your code to check if it's done loading here
                        // For example, you can set a flag or perform some action
                    }
                });
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

                if (getActivity() != null && getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    // There is an activity that can handle the intent

                    startActivity(intent);

                } else
                {
                    // There is no activity that can handle the intent
                }


            }
        });


        siroccoFashionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                webViewLinks.loadUrl(UrlString);
                mProgressCircle.setVisibility(VISIBLE);


                webViewLinks.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url)
                    {

                        if(view.getProgress() == 100)
                        {
                            //webViewLinks.setVisibility(INVISIBLE);
                            mProgressBarWebview.setVisibility(INVISIBLE);

                        }
                        // This method will be called when the page finishes loading
                        // You can put your code to check if it's done loading here
                        // For example, you can set a flag or perform some action
                    }
                });

                bottomSheetDialog.dismiss();

            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                //Code for the selected option
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void checkifAdmindTrue()
    {



        String visited_user_key = null;

        Bundle extras = getActivity().getIntent().getExtras();

        Bundle arguments = getArguments();

        if(arguments != null)

        {
            String name = getArguments().getString("visited_user_id");



            if (name!= null && !(name.compareTo(mAuth.getCurrentUser().getUid())==0) ) {
                // Check if the key you used to put the extra is present
                if (getArguments().containsKey("visited_user_id")) {
                    String value = getArguments().getString("visited_user_id");
                    user_profile_view_id = value;
                    visited_user_key = getArguments().getString("visited_user_id");

                }
            }else
            {
                Toast.makeText(getActivity(),"Pagiis could not find profile", Toast.LENGTH_SHORT).show();
            }

            upDateFollowers("Follower");
            upDateLikes("Follower");


        }


        final String userId = mAuth.getCurrentUser().getUid();
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        //String onlineUserID = getActivity().getIntent().getExtras().get("visit_user_id").toString();

        if (visited_user_key == null)
        {

            changeDisplay();

            String onlineUserId = mAuth.getCurrentUser().getUid();

            upDateFollowers("Like");
            upDateLikes("Like");

            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
            mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("uploads").child(onlineUserId);
            mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("Tags").child(onlineUserId);
            getUserProfileDataRef.keepSynced(true);


            getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    mProgressBar.setVisibility(INVISIBLE);

                    String name = dataSnapshot.child("userNameAsEmail").getValue().toString();
                    String status = dataSnapshot.child("userDefaultStatus").getValue().toString();
                    String imageProfileDP = dataSnapshot.child("userImageDp").getValue().toString();

                    String thumbImage = dataSnapshot.child("userThumbImageDp").getValue().toString();

                    String facebookLink = dataSnapshot.child("facebookLink").getValue().toString();
                    String twitterLink = dataSnapshot.child("twitterLink").getValue().toString();
                    String instagramLink = dataSnapshot.child("instagramLink").getValue().toString();
                    String profileProfession = dataSnapshot.child("proffession").getValue().toString();
                    UrlString = instagramLink; //This is for StoreUrl
                    UrlString2 = twitterLink;//This is for adding product to the store

                    userName.setText(name);//Returning The Email as userName for now...
                    Status.setText(status);

                    //facebookLinkTextView.setText(facebookLink);

                    //twitterLinkTextView.setText(twitterLink);

                    //instagramLinkTextView.setText(instagramLink);
                    // Check if The Default Frofile is set or Now

                    //String imageUrl = imageProfileDP;

                    RequestOptions options = new RequestOptions();

                    if (imageProfileDP != null )
                    {

                        if (getActivity() != null) {
                            // Access getActivity() here
                            Glide.with(getActivity()).load(imageProfileDP).diskCacheStrategy(DiskCacheStrategy.ALL).apply(options.centerCrop()).thumbnail(0.75f).into(userImageDp);
                        }


                    }

                    if(profileProfession.compareTo("Proffession")!=0)

                    {
                        viewProfileStore.setVisibility(VISIBLE);
                        viewProfileStore.setEnabled(true);
                        //followProfileTextView.setText(following);

                    }else
                    {

                        viewProfileStore.setVisibility(INVISIBLE);
                        viewProfileStore.setEnabled(false);
                        //followProfileTextView.setText(following);


                    }


                    if (name.compareTo("UserName") == 0) {
                        requestNameEdit();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





            //mDatabaseRefFollowers = FirebaseDatabase.getInstance().getReference().child("profileFollowers");


            /*mDatabaseRefFollowers.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)

                {
                    if(dataSnapshot.exists())
                    {



                        mDatabaseRefFollowers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)

                            {
                                if(dataSnapshot.exists())
                                {

                                    following = "followers";
                                    followProfileTextView.setText(following);


                                }else
                                {
                                    following = "follow";
                                    followProfileTextView.setText(following);

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        numberOfProfileFollowers = String.valueOf(dataSnapshot.getChildrenCount());


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/



            databaseReferenceLocation = FirebaseDatabase.getInstance().getReference("myLastLocation");

            databaseReferenceLocation.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)

                {
                    if(dataSnapshot.exists())
                    {

                        myLastLocationDetails = dataSnapshot.getValue().toString();


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            /*mDatabaseRef_y.child(onlineUserId).orderByValue().equalTo(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        String x = String.valueOf(dataSnapshot.getChildrenCount());

                        profileViews.setText(x);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mDatabaseRef_x.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        String x = String.valueOf(dataSnapshot.getChildrenCount());
                        profilePosts.setText(x);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            mDatabaseRef_y.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        String x = String.valueOf(dataSnapshot.getChildrenCount());
                        //profileTags.setText(x);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/




            String user_id = mAuth.getCurrentUser().getUid();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

            mDBlistener = mDatabaseRef.child(user_id).orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        mUploads.add(upload);
                    }
                    Collections.reverse(mUploads);
                    // Notify adapter that data set has changed
                    mAdapter.notifyDataSetChanged();
                    AddContent.setVisibility(INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(INVISIBLE);
                }
            });


        } else  {


            Bundle bundle = getArguments();


            if (bundle != null)
            {
                onlineUserId = bundle.getString("visited_user_id");
                
                // Now you can use receivedData
                getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
                mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("uploads").child(onlineUserId);
                mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("Tags").child(onlineUserId);
                getUserProfileDataRef.keepSynced(true);


                getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        String name = dataSnapshot.child("userNameAsEmail").getValue().toString();
                        String status = dataSnapshot.child("userDefaultStatus").getValue().toString();
                        String imageProfileDP = dataSnapshot.child("userImageDp").getValue().toString();

                        String thumbImage = dataSnapshot.child("userThumbImageDp").getValue().toString();

                        String facebookLink = dataSnapshot.child("facebookLink").getValue().toString();
                        String twitterLink = dataSnapshot.child("twitterLink").getValue().toString();
                        String instagramLink = dataSnapshot.child("instagramLink").getValue().toString();
                        String profileProfession = dataSnapshot.child("proffession").getValue().toString();


                        UrlString3 = facebookLink;//This is for StoreUrl for the customers
                        UrlString = instagramLink; //This is for StoreUrl
                        UrlString2 = twitterLink;//This is for adding product to the store

                        userName.setText(name);//Returning The Email as userName for now...
                        Status.setText(status);

                        //facebookLinkTextView.setText(facebookLink);

                        //twitterLinkTextView.setText(twitterLink);

                        //instagramLinkTextView.setText(instagramLink);
                        // Check if The Default Frofile is set or Now

                        //String imageUrl = imageProfileDP;

                        RequestOptions options = new RequestOptions();

                        Glide.with(getActivity()).load(imageProfileDP).diskCacheStrategy(DiskCacheStrategy.ALL).apply(options.centerCrop()).thumbnail(0.75f).into(userImageDp);


                        if (name.compareTo("UserName") == 0) {
                            requestNameEdit();
                        }

                        if(profileProfession.compareTo("Proffession")!=0)

                        {
                            viewProfileStore.setVisibility(VISIBLE);
                            viewProfileStore.setEnabled(true);

                        }else
                        {

                            viewProfileStore.setVisibility(INVISIBLE);
                            viewProfileStore.setEnabled(false);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });






                /*mDatabaseRefFollowers = FirebaseDatabase.getInstance().getReference().child("profileFollowers");


                mDatabaseRefFollowers.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)

                    {
                        if(dataSnapshot.exists())
                        {

                            mDatabaseRefFollowers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)

                                {
                                    if(dataSnapshot.exists())
                                    {

                                        following = "followers";
                                        followProfileTextView.setText(following);


                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            numberOfProfileFollowers = String.valueOf(dataSnapshot.getChildrenCount());


                        }else
                        {
                            following = "followers";
                            followProfileTextView.setText(following);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/



                databaseReferenceLocation = FirebaseDatabase.getInstance().getReference("myLastLocation");

                databaseReferenceLocation.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)

                    {
                        if(dataSnapshot.exists())
                        {

                            myLastLocationDetails = dataSnapshot.getValue().toString();


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            } else
            {
                // Handle the case where the data is not available
                Toast.makeText(getActivity(),"Pagiis could not find profile", Toast.LENGTH_SHORT).show();
            }


            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            /*mDatabaseRef_y.child(onlineUserId).orderByValue().equalTo(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        String x = String.valueOf(dataSnapshot.getChildrenCount());

                        profileViews.setText(x);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mDatabaseRef_x.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        String x = String.valueOf(dataSnapshot.getChildrenCount());
                        profilePosts.setText(x);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            mDatabaseRef_y.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        String x = String.valueOf(dataSnapshot.getChildrenCount());
                        profileTags.setText(x);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            /*getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.child("userNameAsEmail").getValue().toString();
                    String status = dataSnapshot.child("userDefaultStatus").getValue().toString();
                    String imageProfileDP = dataSnapshot.child("userImageDp").getValue().toString();

                    String thumbImage = dataSnapshot.child("userThumbImageDp").getValue().toString();

                    String facebookLink = dataSnapshot.child("facebookLink").getValue().toString();
                    String twitterLink = dataSnapshot.child("twitterLink").getValue().toString();
                    String instagramLink = dataSnapshot.child("instagramLink").getValue().toString();

                    userName.setText(name);//Returning The Email as userName for now...
                    Status.setText(status);

                    facebookLinkTextView.setText(facebookLink);

                    twitterLinkTextView.setText(twitterLink);

                    instagramLinkTextView.setText(instagramLink);
                    // Check if The Default Frofile is set or Now

                    //String imageUrl = imageProfileDP;

                    RequestOptions options = new RequestOptions();

                    Glide.with(ActivityOwnProfile.this).load(imageProfileDP).apply(options.centerCrop()).thumbnail(0.75f).into(userImageDp);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

            //Call the Onclick Listener On The Change Dp Button


            //String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


            mDBlistener = mDatabaseRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        mUploads.add(upload);
                    }

                    Collections.reverse(mUploads);
                    mAdapter.notifyDataSetChanged();
                    AddContent.setVisibility(INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(INVISIBLE);
                }
            });



        }


    }


    private void requestNameEdit() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.alert_facebook_link_edit, null);
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");

        final EditText newStatus = mView.findViewById(R.id.facebookLinkEdit);
        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);
        final String current_userId = mAuth.getCurrentUser().getUid();
        final ImageView fbIcon = mView.findViewById(R.id.fbIcon);
        final TextView instructionText = mView.findViewById(R.id.instruction);

        fbIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.user));
        instructionText.setText("Edit Profile-Name");

        saveStatus.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveStatus);
                if (!(newStatus.getText() == null) && !TextUtils.isEmpty(newStatus.getText())) {

                    getUserProfileDataRef.child(current_userId).child("userNameAsEmail").setValue(newStatus.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            AlertDialog dialog = mBuilder.create();
                            dialog.dismiss();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.GalleryFragment,new ProfileFragment()).commit();


                        }
                    });


                } else {
                    Toast.makeText(getActivity(), "Please type in your facebookLink or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Gallery_Pick);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null && data.getData() != null) {
            resultUri = data.getData();

            Log.d("Fragment", "Uri: " + resultUri);

            Toast.makeText(getActivity(), "here is the uri: " + resultUri, Toast.LENGTH_SHORT).show();

            if (mUploadTask == null || !mUploadTask.isInProgress()) {
                mProgressBar.setVisibility(VISIBLE);
                Upload(resultUri);
                Log.d("Fragment", "Starting upload");
            } else {
                Toast.makeText(getActivity(), "PAGiiS image upload in progress !!", Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(getActivity(), "no results obtained", Toast.LENGTH_SHORT).show();

        }
    }


    private void saveInfor() {
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
        final String current_userId = mAuth.getCurrentUser().getUid();

        if (StringCarrier != null && !StringCarrier.isEmpty()) {
            getUserProfileDataRef.child(current_userId).child("twitterLink").setValue(StringCarrier).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity(), "linked", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getActivity(), "Sorry link was not captured.", Toast.LENGTH_SHORT).show();
        }

    }


    private void Upload(Uri fileUri) {
        if (resultUri != null)
        {

            String onlineUserId = mAuth.getCurrentUser().getUid();

            getUserProfileDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserId);

            userImageStorageDpRef = FirebaseStorage.getInstance().getReference().child("dp_Images");

            //final String imageUrl = String.valueOf(resultUri);

            final StorageReference storageReference = userImageStorageDpRef.child(onlineUserId);

            mUploadTask = storageReference.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                Uri finalImageUri;

                                String finalImageUrl;

                                @Override
                                public void onSuccess(Uri uri) {
                                    finalImageUrl = String.valueOf(uri);

                                    finalImageUri = uri;


                                    getUserProfileDataRef.child("userImageDp").setValue(finalImageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();

                                        }
                                    });
                                }
                            });

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(VISIBLE);
                            changeDp.setVisibility(INVISIBLE);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBlistener);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
        mDatabaseRef.removeEventListener(mDBlistener);
    }


    @SuppressLint({"MissingInflatedId", "NewApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        webViewLinks = rootView.findViewById(R.id.webview);
        webSettings = webViewLinks.getSettings();
        profileFramelayout = rootView.findViewById(R.id.ProfileLayout);


        userImageDp = rootView.findViewById(R.id.ImageDP);
        profileLikesTextView = rootView.findViewById(R.id.userProfileViews);
        //closeLinkView = rootView.findViewById(R.id.close_link);
        userName = rootView.findViewById(R.id.profileName);
        PostTitle = rootView.findViewById(R.id.post_title);
        Status = rootView.findViewById(R.id.profileStatus);
        logoutText = rootView.findViewById(R.id.LogoutText);

        numberOfFollowers = rootView.findViewById(R.id.numberOfFollowers);
        followProfileTextView = rootView.findViewById(R.id.followProfileButton);

        progressBarFollow = rootView.findViewById(R.id.progress_circle_Follow);



        progressBarFollow.setVisibility(INVISIBLE);

        viewProfileStore = rootView.findViewById(R.id.profileStoreOptionButton);

        mProgressBar = rootView.findViewById(R.id.progress_circle_own_profile);
        mProgressBarWebview = rootView.findViewById(R.id.progress_circle_webview);
        //facebookIcon = rootView.findViewById(R.id.facebookLinkIcon);
        //twitterIcon = rootView.findViewById(R.id.tweeterIcon);
        //instagramIcon = rootView.findViewById(R.id.instagramIcon);
        AddContent = rootView.findViewById(R.id.addContent);

        changeDp = rootView.findViewById(R.id.ChangeOwnDP);

        editStatus = rootView.findViewById(R.id.writeStatus);

        imageviewAnim = rootView.findViewById(R.id.imageViewAnimation);

        profileProffession = rootView.findViewById(R.id.profileSettingsButton);

        //followProfileTextView.setText(following);


        profileProffession.setEnabled(false);
        viewProfileStore.setEnabled(false);
        viewProfileStore.setVisibility(INVISIBLE);

        webViewLinks.setVisibility(INVISIBLE);
        mProgressBarWebview.setVisibility(INVISIBLE);

        //userMemelayout = rootView.findViewById(R.id.userMemeCardView);

        //userMemelayout.setVisibility(View.INVISIBLE);
        // mProgressCircle = findViewById(R.id.addContent);

        //richLinkView = rootView.findViewById(R.id.memeImageView);

        //richLinkView = new WebView(getActivity());

        //WebViewClient myWebViewClient = new WebViewClient();

        //getActivity().setContentView(richLinkView);


        //richLinkView.setWebViewClient(myWebViewClient);

        numberOfFollowers.setText(numberOfProfileFollowers);
        //profileLikesTextView.setText(numberOfProfileLikes);


        AddContent.setEnabled(false);
        AddContent.setVisibility(INVISIBLE);


        changeDp.setEnabled(false);
        changeDp.setVisibility(INVISIBLE);

        logoutText.setEnabled(false);
        logoutText.setVisibility(INVISIBLE);

        editStatus.setEnabled(false);

        profilePosts = rootView.findViewById(R.id.userProfilePosts);
        profileTags = rootView.findViewById(R.id.userProfileConnected);
        profileViews = rootView.findViewById(R.id.userProfileViews);
        userContactDetails = rootView.findViewById(R.id.profile_contact_details);

        //facebookLinkTextView = rootView.findViewById(R.id.faceboolinkTextview);
        //twitterLinkTextView = rootView.findViewById(R.id.tweeterLinkTexview);
        //instagramLinkTextView = rootView.findViewById(R.id.instagramIconTextView);

        //facebookLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        //twitterLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        //instagramLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        //saveinfo = (ImageView) findViewById(R.id.saveInfo);

        //facebookLinkTextView.setVisibility(View.INVISIBLE);
        //twitterLinkTextView.setVisibility(View.INVISIBLE);
        //instagramLinkTextView.setVisibility(View.INVISIBLE);



        mRecyclerView = rootView.findViewById(R.id.memeRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        //changeDp.setEnabled(false);
        //changeDp.setVisibility(View.INVISIBLE);



        mAdapter = new ViewStoreItemAdapter(getActivity(), mUploads);

        mAdapter.setOnItemClickListener(ProfileFragment.this);

        mRecyclerView.setAdapter(mAdapter);

        shareLinkButton = rootView.findViewById(R.id.ripple_button);







        changeDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                // Put the data you want to send in the Bundle

                String User_id = mAuth.getCurrentUser().getUid();
                @SuppressLint("RestrictedApi") Intent intent = new Intent(getApplicationContext(), first_time_profile_config.class);
                intent.putExtra("visited_user_id",User_id);
                startActivity(intent);



            }
        });


        profileProffession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.profileSettingsButton);
                oldFragment = null;

                oldFragment = new profile_service();
                if (oldFragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, oldFragment).commit();
                }
            }
        });


        //Here we open the store of the profile the user has visited
        viewProfileStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.profileStoreOptionButton);
                String visited_user_key = null;
                mProgressBarWebview.setVisibility(VISIBLE);

                Bundle extras = getActivity().getIntent().getExtras();

                Bundle arguments = getArguments();

                if(arguments != null)

                {
                   /* String name = getArguments().getString("visited_user_id");

                    if (name!= null && !(name.compareTo(mAuth.getCurrentUser().getUid())==0) && UrlString.compareTo("nulll")!=0 ) {

                        profileProductWebview();

                    }else
                    {
                        Toast.makeText(getActivity(),"This profile does not have an online store open", Toast.LENGTH_LONG).show();
                    }*/

                    profileProductWebview();


                }else
                {
                    showBottomSheetDialog();
                }
            }
        });





        imageviewAnim.setEventListener(new SparkEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState)
                {



                    mDatabaseRefLikes = FirebaseDatabase.getInstance().getReference().child("postLikes");

                    mDatabaseRefLikes.child(onlineUserId).child(mAuth.getCurrentUser().getUid().toString()).setValue("true");

                    checkLocation("Like");


                    Toast.makeText(getApplicationContext(), "add to favourite!", Toast.LENGTH_SHORT).show();
                } else {


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


        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.LogoutText);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.logout_dialog, null);

                ImageView logoutImage = mView.findViewById(R.id.SignOutImage);
                ImageView cancelLogOutImage = mView.findViewById(R.id.cancelSignOutImage);


                logoutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.findViewById(R.id.SignOutImage);
                        FirebaseAuth.getInstance().signOut();
                    }
                });

                cancelLogOutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        oldFragment = null;
                        AlertDialog dialog = mBuilder.create();
                        Toast.makeText(getActivity(),"Stay tuned.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });


        //profileProductWebview();






        followProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                progressBarFollow.setVisibility(VISIBLE);
                checkLocation("Follower");

            }
        });


        editStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.writeStatus);

                openStatusEditText();
            }
        });






        checkifAdmindTrue();

        return rootView;
    }





    public void checkLocation(String FollowOrLike)

    {


        if(FollowOrLike == "Like")
        {

            mDatabaseRefLikes = FirebaseDatabase.getInstance().getReference().child("profileLikes");


            mDatabaseRefLikes.child(onlineUserId).child(mAuth.getCurrentUser().getUid().toString()).setValue("true")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)

                        {

                            if (task.isSuccessful())
                            {
                                mDatabaseRefLikes.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {

                                            String x = String.valueOf(dataSnapshot.getChildrenCount());

                                            numberOfProfileLikes= x;

                                            profileLikesTextView.setText(numberOfProfileLikes);

                                            postNotification("Like");

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                            }
                        }
                    });






        }else if(FollowOrLike == "Follower")
        {

            mDatabaseRefFollowers = FirebaseDatabase.getInstance().getReference().child("profileFollowers");


            mDatabaseRefFollowers.child(onlineUserId).child(mAuth.getCurrentUser().getUid().toString()).setValue("true")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)

                        {

                            if (task.isSuccessful())
                            {
                                mDatabaseRefFollowers.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {

                                            String x = String.valueOf(dataSnapshot.getChildrenCount());

                                            numberOfProfileFollowers= x;

                                            numberOfFollowers.setText(numberOfProfileFollowers);
                                            followProfileTextView.setText("Following");

                                            progressBarFollow.setVisibility(INVISIBLE);

                                            postNotification("Follower");

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                            }
                        }
                    });



        }




    }


    public void upDateFollowers(String FollowOrLike)

    {

        if(FollowOrLike == "Like")
        {

            mDatabaseRefFollowers = FirebaseDatabase.getInstance().getReference().child("profileFollowers");

            mDatabaseRefFollowers.child(mAuth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener()

            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        String x = String.valueOf(dataSnapshot.getChildrenCount());

                        numberOfProfileFollowers = x;

                        numberOfFollowers.setText(numberOfProfileFollowers);

                        followProfileTextView.setText("Followers");

                        progressBarFollow.setVisibility(INVISIBLE);

                        //postNotification("Follower");
                    }else
                    {


                        numberOfProfileFollowers = "0";

                        numberOfFollowers.setText(numberOfProfileFollowers);

                        followProfileTextView.setText("Followers");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }else if(FollowOrLike == "Follower") {

            mDatabaseRefFollowers = FirebaseDatabase.getInstance().getReference().child("profileFollowers");

            mDatabaseRefFollowers.child(user_profile_view_id).addValueEventListener(new ValueEventListener()

            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        String x = String.valueOf(dataSnapshot.getChildrenCount());

                        numberOfProfileFollowers = x;

                        numberOfFollowers.setText(numberOfProfileFollowers);

                        followProfileTextView.setText("Follow");

                        mDatabaseRefFollowers.child(onlineUserId).child(mAuth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {


                                    followProfileTextView.setText("Following");

                                    progressBarFollow.setVisibility(INVISIBLE);

                                    //postNotification("Follower");
                                }else
                                {
                                    followProfileTextView.setText("Follow");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        progressBarFollow.setVisibility(INVISIBLE);

                        //postNotification("Follower");
                    }else
                    {


                        numberOfProfileFollowers = "0";

                        numberOfFollowers.setText(numberOfProfileFollowers);

                        followProfileTextView.setText("Follow");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }





    }


    public void upDateLikes(String FollowOrLike)

    {

        if(FollowOrLike == "Like")
        {

            mDatabaseRefLikes = FirebaseDatabase.getInstance().getReference().child("profileLikes");


            mDatabaseRefLikes.child(mAuth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener()

            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        String x = String.valueOf(dataSnapshot.getChildrenCount());

                        numberOfProfileLikes = x;

                        profileLikesTextView.setText(numberOfProfileLikes);

                        mDatabaseRefLikes.child(mAuth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    //imageviewAnim.isChecked();
                                    imageviewAnim.setActivated(true);
                                    progressBarFollow.setVisibility(INVISIBLE);

                                    //postNotification("Follower");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        progressBarFollow.setVisibility(INVISIBLE);

                        //postNotification("Follower");
                    }else
                    {

                        mDatabaseRefLikes.child(mAuth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    //imageviewAnim.isChecked();
                                    imageviewAnim.setActivated(true);
                                    progressBarFollow.setVisibility(INVISIBLE);

                                    //postNotification("Follower");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        numberOfProfileLikes = "0";

                        profileLikesTextView.setText(numberOfProfileFollowers);

                        //profileLikesTextView.setText("Followers");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }else if(FollowOrLike == "Follower") {

            mDatabaseRefLikes = FirebaseDatabase.getInstance().getReference().child("profileLikes");


            mDatabaseRefLikes.child(user_profile_view_id).addValueEventListener(new ValueEventListener()

            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        String x = String.valueOf(dataSnapshot.getChildrenCount());

                        numberOfProfileLikes = x;

                        profileLikesTextView.setText(numberOfProfileLikes);


                        mDatabaseRefFollowers.child(user_profile_view_id).child(mAuth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    imageviewAnim.isChecked();
                                    imageviewAnim.setActivated(true);
                                    progressBarFollow.setVisibility(INVISIBLE);

                                    //postNotification("Follower");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        progressBarFollow.setVisibility(INVISIBLE);

                        //postNotification("Follower");
                    }else
                    {


                        numberOfProfileLikes = "0";

                        profileLikesTextView.setText(numberOfProfileFollowers);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }





    }


    private void postNotification(String fromLikeOrFollower)
    {
        String myUserId = mAuth.getCurrentUser().getUid().toString();


        if(fromLikeOrFollower == "Like")
        {

            notificationReference = FirebaseDatabase.getInstance().getReference().child("PagiisNotification");


            ImageUploads upload = new ImageUploads(myName, myImageDpUrl, "", myUserId, "", "", "", "", myLastLocationDetails, "Your Profile has a new like");
            notificationReference.child(onlineUserId)
                    .push()
                    .setValue(upload, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {

                            notificationTitle = "New profile like";

                            notificationMessage = "Someone just liked your profile";

                            sendFCMNotification(requireContext(),userToken, notificationTitle, notificationMessage);

                            //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                            //Toast.makeText(getApplicationContext(), "Notification sent to all users in location.", Toast.LENGTH_SHORT).show();
                            // String uniqueKey = databaseReference.getKey();
                            //Create the function for Clearing/The ImageView Widget.
                        }
                    });



        }else if(fromLikeOrFollower == "Follower")
        {
            notificationReference = FirebaseDatabase.getInstance().getReference().child("PagiisNotification");

            ImageUploads upload = new ImageUploads(myName, myImageDpUrl, "", myUserId, "", "", "", "", myLastLocationDetails, "Your Profile has a new follower");
            notificationReference.child(onlineUserId)
                    .push()
                    .setValue(upload, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {

                            //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                            //Toast.makeText(getApplicationContext(), "Notification sent to all users in location.", Toast.LENGTH_SHORT).show();
                            // String uniqueKey = databaseReference.getKey();
                            //Create the function for Clearing/The ImageView Widget.


                            notificationTitle = "New follower";

                            notificationMessage = "A new firend has followed your profile";
                            sendFCMNotification(requireContext(),userToken, notificationTitle, notificationMessage);
                        }
                    });


        }

    }

    private void sendFCMNotification(Context context, String fcmToken, String title, String message) {
        if (fcmToken == null || fcmToken.isEmpty()) {
            mDatabaseRef_Tokens = FirebaseDatabase.getInstance().getReference().child("userTokens").child(onlineUserId);

            mDatabaseRef_Tokens.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {

                    if(dataSnapshot.exists())
                    {

                        userToken = dataSnapshot.getValue().toString();

                        String FCM_API = "https://fcm.googleapis.com/fcm/send";
                        String serverKey = "AAAA64f0YOg:APA91bEWaRY_bpktQU7HtgIhAVsLjhJCGTwjWVWi1bYutnDkwkmo2QmgKBJf8MO6BJXrpiDEi62-XDWKi8B0ogwQ8PVLoABuRyExDj_kdw4VOGQa-0PzzV_G8toDuzWbcXUqoh6LbBAS";
                        String contentType = "application/json";

                        JSONObject notification = new JSONObject();
                        JSONObject notificationBody = new JSONObject();
                        JSONObject dataPayload = new JSONObject();

                        try {
                            notificationBody.put("title", title);
                            notificationBody.put("body", message);
                            dataPayload.put("key1", "value1"); // Optional data payload
                            dataPayload.put("key2", "value2");

                            notification.put("to", userToken);
                            notification.put("notification", notificationBody);
                            notification.put("data", dataPayload); // Optional data payload
                        } catch (JSONException e) {
                            Log.e("FCM Error", "JSON Exception: " + e.getMessage());
                        }

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_API, notification,
                                response -> Log.d("FCM Response", "Success: " + response.toString()),
                                error -> {
                                    if (error.networkResponse != null) {
                                        Log.e("FCM Error", "Error code: " + error.networkResponse.statusCode);
                                        Log.e("FCM Error", "Error data: " + new String(error.networkResponse.data));
                                    }
                                    Log.e("FCM Error", "Failed: " + error.toString());
                                }) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Authorization", "key=" + serverKey);
                                headers.put("Content-Type", contentType);
                                return headers;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                        requestQueue.add(request);

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






    private void profileProductWebviewInent( String url) {

        if (UrlString.compareTo("nulll") != 0) {


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // Check if there is an application capable of handling the intent
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);

            }

        }else
        {
            Toast.makeText(getActivity(),"This profile does not have an online store open", Toast.LENGTH_LONG).show();
        }
    }


    private void changeDisplay()
    {

        changeDp.setEnabled(true);
        changeDp.setVisibility(VISIBLE);
        editStatus.setEnabled(true);
        profileProffession.setEnabled(true);
        logoutText.setVisibility(VISIBLE);
        logoutText.setEnabled(true);
        followProfileTextView.setEnabled(false);
        followProfileTextView.setText("Followers");

    }

    @Override
    public void onItemClick(int position) {
        ImageUploads selectedImage = mUploads.get(position);

        String selectedKey = selectedImage.getKey();

        String imageUrl = selectedImage.getImageUrl();

        String maxImageUserId = selectedImage.getUserId();


        if (!imageUrl.isEmpty() && !selectedKey.isEmpty())
        {
            Intent intent = new Intent(getActivity(), PagiisMaxView.class);
            intent.putExtra("imageKeyMAx", selectedKey);
            intent.putExtra("imageUrlMax", imageUrl);
            intent.putExtra("imageUserId", maxImageUserId);
            intent.putExtra("From", "Profile");
            intent.putExtra("orderLink",UrlString);
            startActivity(intent);

        } else
        {

            Toast.makeText(getActivity(), "Pagiis failed to open maxview.", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onWhatEverClick(int position) {
        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        DeleteKeyAlternative = selectedKey;

        String myUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(myUserId);

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.logout_dialog, null);
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");


        ImageView logoutImage = mView.findViewById(R.id.SignOutImage);
        TextView signOut = mView.findViewById(R.id.signOutText);
        ImageView cancelLogOutImage = mView.findViewById(R.id.cancelSignOutImage);

        mBuilder.setView(mView);


        signOut.setText("Delete post");

        logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                view.findViewById(R.id.SignOutImage);
                mDatabaseRef.child(selectedKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        //AlertDialog dialog = mBuilder.create();
                        Toast.makeText(getApplicationContext(), "Item sent to bin..", Toast.LENGTH_SHORT).show();

                        //dialog.dismiss();



                    }
                }).addOnFailureListener(new OnFailureListener() {


                    String errorText;
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                        errorText = String.valueOf(e);
                        Toast.makeText(getApplicationContext(), errorText + " Deletion Will restart in few seconds", Toast.LENGTH_LONG).show();


                    }
                });
            }
        });

        cancelLogOutImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view)
            {
               // dialog.dismiss();
                //AlertDialog dialog = mBuilder.create();
                Toast.makeText(getApplicationContext(), "Cancelled.", Toast.LENGTH_SHORT).show();

                //dialog.dismiss();


            }
        });


        AlertDialog dialog = mBuilder.create();

        dialog.show();

        //StorageReference deletImage = mStorage.getReferenceFromUrl(selectedImage.getImageUrl());

    }

    @SuppressLint("RestrictedApi")
    private void dismissDialog_two()
    {



        oldFragment = null;
        Toast.makeText(getApplicationContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
        oldFragment = new ProfileFragment();
    }

    @SuppressLint("RestrictedApi")
    private void dismissDialog()
    {

        oldFragment = null;
        Toast.makeText(getApplicationContext(), "Item sent to bin..", Toast.LENGTH_SHORT).show();
        oldFragment = new ProfileFragment();
    }


    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}