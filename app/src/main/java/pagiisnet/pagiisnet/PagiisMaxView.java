package pagiisnet.pagiisnet;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PagiisMaxView extends AppCompatActivity

{

    private FirebaseAuth mAuth;
    private androidx.appcompat.widget.Toolbar mToolbar;

    private String UrlString;


    private ProgressBar mProgressBar;
    private ImageView maxView, blackLock, blueLock;
    private DatabaseReference mDataRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_x;
    private DatabaseReference lockedImages;
    private DatabaseReference myDetailsRef;
    private String from;
    private DatabaseReference myLocatinLastRef;



    private WebView webViewLinks;
    private WebSettings webSettings;


    private String ImageUrl;
    private String imageKey;
    private String imageUserId;
    private TextView maxViewLink;

    private Button RippleButton;
    private String itemName;
    private List<tags> tagedUsers;
    private Long tagedUsersSizeValue;
    private Long tagedUsersDabaseCount;

    private ProgressBar mProgressBarWebview;


    private String MyName;
    private String MyLastLocation;

    private String MyProfileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagiis_max_view);
        FirebaseApp.initializeApp(getApplicationContext());


        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        tagedUsers = new ArrayList<>();

        webViewLinks = findViewById(R.id.webview);
        webSettings = webViewLinks.getSettings();
        mProgressBarWebview = findViewById(R.id.progress_circle_webview);
        webViewLinks.setVisibility(INVISIBLE);
        mProgressBarWebview.setVisibility(INVISIBLE);


        myLocatinLastRef = FirebaseDatabase.getInstance().getReference().child("MyLastLocation");
        myDetailsRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("PagiisNotifications");


        if (mAuth.getCurrentUser() == null) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Pagiis");

        maxView = findViewById(R.id.pagiis_max_view);

        ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();
        imageKey = getIntent().getExtras().get("imageKeyMAx").toString();
        imageUserId = getIntent().getExtras().get("imageUserId").toString();
        UrlString = getIntent().getExtras().get("orderLink").toString();

        RippleButton = findViewById(R.id.ripplePost);

        maxViewLink = findViewById(R.id.linkTextView);


        maxViewLink.setMovementMethod(LinkMovementMethod.getInstance());

        mProgressBar = findViewById(R.id.PagiisMaxProgressBar);

        if(UrlString.compareTo("nulll")==0)
        {
            RippleButton.setVisibility(INVISIBLE);
            RippleButton.setEnabled(false);

        }else
        {

            RippleButton.setVisibility(INVISIBLE);
            RippleButton.setEnabled(false);


        }

       /* maxView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.pagiis_max_view);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(PagiisMaxView.this);
                View mView = getLayoutInflater().inflate(R.layout.pin_image_dialog, null);
                ImageView logoutImage = mView.findViewById(R.id.SignOutImage);
                ImageView cancelLogOutImage = mView.findViewById(R.id.cancelSignOutImage);
                final String current_userID = mAuth.getCurrentUser().getUid();

                lockedImages = FirebaseDatabase.getInstance().getReference().child("LockedImages").child(current_userID).child(imageKey);


                logoutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        lockedImages.child("tag_userImage_id").setValue(imageUserId);
                        lockedImages.child("tag_userImage_url").setValue(ImageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(PagiisMaxView.this, "Image pinned successfully", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                });

                cancelLogOutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), PagiisMaxView.class);
                        startActivity(intent);
                        finish();

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });*/



        RippleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)

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


                }else
                {

                    Toast.makeText(PagiisMaxView.this, "Store not available", Toast.LENGTH_LONG).show();


                }

            }
        });

        from = getIntent().getExtras().get("From").toString();
        if(from.compareTo("Profile")==0)
        {
            getPagiisMaxView();

        }else{

            getPagiisMaxViewNoti();
        }



        getPagiisMaxView();
        myLocationDetails();
        myDetails();
    }



    private void updateArrayList()

    {
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");

        String currentUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef_x.child(currentUserId).limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String localID = ds.getKey();

                        tagedUsers.add(new tags(localID));

                        if (dataSnapshot.getChildrenCount() >= 1 && tagedUsers.size() >= 1 | dataSnapshot.getChildrenCount() == tagedUsers.size()) {

                            if (dataSnapshot.getChildrenCount() == tagedUsers.size() | dataSnapshot.getChildrenCount() >= 1 && tagedUsers.size() >= 1)
                            {
                                tagedUsersDabaseCount = dataSnapshot.getChildrenCount();
                                getDataNormally();
                            }


                            //getDataNormally();

                            //0799506310

                        } else {

                            Toast.makeText(PagiisMaxView.this, "Online posts currently unavailable.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                } else {
                    Toast.makeText(PagiisMaxView.this, "Pagiis online users currently unavailable.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);

                TextView messageGrid = findViewById(R.id.customToastText);


                // Initiate the Toast instance.
                Toast toast = new Toast(getApplicationContext());

                messageGrid.setText("Pagiis online user update failed, Please waite while pagiis restarts activity");
                // Set custom view in toast.
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                //Toast.makeText(MapsActivity.this, "Button switched on.", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }

    private void getDataNormally() {

        if (!tagedUsers.isEmpty()) {

            for (int i = 0; i < tagedUsers.size(); i++)
            {
                tags x = tagedUsers.get(i);
                final String taged_id = x.getUser_tagID();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("LiveEventsAndRipples").child(taged_id);
                //Query mSearchQuery = mDatabaseRef.orderByChild("name").startAt(searchedtext).endAt(searchedtext + "\uf8ff");
                final String userIdRef = mAuth.getCurrentUser().getUid();


                if(tagedUsersSizeValue <= tagedUsersDabaseCount)
                {

                    createEventFile upload = new createEventFile(itemName, ImageUrl, "just rippled to you", imageUserId, "emailAddress", "storecell", "StoreBankinDetails ", "just rippled to you", "", "", "", "", "", MyLastLocation, userIdRef, MyName,"live");
                    final int finalI = i;
                    mDatabaseRef.child(userIdRef)
                            .push()
                            .setValue(upload, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError,
                                                       DatabaseReference databaseReference)

                                {

                                    tagedUsersSizeValue++;

                                }
                            });

                }else if(tagedUsersSizeValue >= tagedUsersDabaseCount)
                {Toast.makeText(this, "Rippled successfully !", Toast.LENGTH_SHORT).show();
                 Intent intent  = new Intent(PagiisMaxView.this,ViewUsersMemes.class);
                 startActivity(intent);
                }

            }

        }else

        {
            Toast.makeText(PagiisMaxView.this, "No nearby online users found ", Toast.LENGTH_SHORT).show();
        }

    }

   private void myDetails()
    {
        String user_id = mAuth.getCurrentUser().getUid();

        myDetailsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        myDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String myName =  dataSnapshot.child("userNameAsEmail").getValue().toString();

                String myProfileImae = dataSnapshot.child("userImageDp").getValue().toString();


                MyName = myName;
                MyProfileUrl = myProfileImae;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void myLocationDetails()

    {

        String user_id = mAuth.getCurrentUser().getUid();

        myLocatinLastRef = FirebaseDatabase.getInstance().getReference().child("MyLastLocation").child(user_id);

        myDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    MyLastLocation = dataSnapshot.getValue().toString();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                String e = databaseError.getMessage();
                Toast.makeText(PagiisMaxView.this, "last location not found"+"Results"+":"+e, Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }

    private void checkImageLocked()
    {
        imageUserId = getIntent().getExtras().get("imageUserId").toString();
        String CurrentUserId = mAuth.getCurrentUser().getUid();

        lockedImages.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if (ds.getKey().compareTo(imageUserId) == 0)
                        {
                            getPagiisMaxView();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void tryIntentMaxView()

    {
        Glide.with(PagiisMaxView.this).load(ImageUrl).into(maxView);
        mProgressBar.setVisibility(View.INVISIBLE);
    }


    private void getPagiisMaxView() {

        if(!imageKey.isEmpty() && !ImageUrl.isEmpty())
        {
            ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();

            imageKey = getIntent().getExtras().get("imageKeyMAx").toString();

            imageUserId = getIntent().getExtras().get("imageUserId").toString();

            mDataRef = FirebaseDatabase.getInstance().getReference("uploads");

            mDataRef.child(imageUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        if (postSnapshot.getKey().compareTo(imageKey) == 0)
                        {

                            //final String linkView = postSnapshot.child("name").getValue().toString();

                            //itemName = linkView;

                            maxViewLink.setText("Pagiis Notification");

                            Glide.with(PagiisMaxView.this).load(ImageUrl).thumbnail(0.65f).into(maxView);
                            mProgressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });

        }

    }





    private void getPagiisMaxViewNoti() {

        if(!imageKey.isEmpty() && !ImageUrl.isEmpty())
        {
            ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();

            imageKey = getIntent().getExtras().get("imageKeyMAx").toString();

            imageUserId = getIntent().getExtras().get("imageUserId").toString();

            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("WalkinWall");

            mDatabaseRef.child(imageUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        if (postSnapshot.getKey().compareTo(imageKey) == 0)
                        {

                            final String linkView = postSnapshot.child("name").getValue().toString();

                            itemName = linkView;

                            maxViewLink.setText(linkView);

                            Glide.with(PagiisMaxView.this).load(ImageUrl).thumbnail(0.65f).into(maxView);
                            mProgressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });

        }

    }

}