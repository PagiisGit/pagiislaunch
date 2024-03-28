package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
public class UserProfileActivity extends AppCompatActivity {

    private Button addFriendButton;
    private Button declineRequestButton;

    private ImageView mediaView;
    private ImageView contactView;

    private Button likeButton; //Not yet used

    private TextView profileUserName;

    private String VisitedUserId;
    private String contactKey;

    private Button commentButton;//Not yet USed
    private ProgressBar mProgressCircle;
    private FloatingActionButton contentRater;
    private String StoreKey;
    private  TextView contentRaterText;

    private Button logOutButton;

    private ImageView mapsViewuserProfileImage,profileName,profileProffession,profileEmail,profileMobile;

    private TextView   profileNameText,profileProffessionText,profileEmailText,profileMobileText;

    private DatabaseReference usersRef;
    private DatabaseReference friendRequestRef;
    private FirebaseAuth mAuth;
    String  sender_user_id;
    String receiver_user_id;
    private DatabaseReference friendsRef;
    private DatabaseReference notificationsRef;

    private DatabaseReference mDatabaseRef_x;
    private DatabaseReference mDatabaseRef_y;
    private DatabaseReference mDatabaseRef_z;

    private  DatabaseReference locationDetails;
    private TextView facebookLinkTextView,twitterLinkTextView,instagramLinkTextView;
    private String CURRENT_STATE;
    private androidx.appcompat.widget.Toolbar mToolbar;

    private FloatingActionButton facebookLinkButton,InstaramLinkButton,TwitterLinkIcon;

    //private String on_maps_visited_user_id;

    private String MyName;
    private String myLastLocationDetails;
    private String requestDefaultText;
    private String myDpUrl;


    private String instagramLink;
    private String facebookLink;
    private String twitterLink;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseApp.initializeApp(getApplicationContext());

        setContentView(R.layout.activity_user_profile);
        notificationsRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        notificationsRef.keepSynced(true);
        friendsRef = FirebaseDatabase.getInstance().getReference().child("friends");


        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mProgressCircle = findViewById(R.id.progress_view_user_profile);

        locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation");

        //LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


       // View actionBarView = layoutInflater.inflate(R.layout.chats_custom_bar,null);
        //actionBar.setCustomView(actionBarView);


        mAuth = FirebaseAuth.getInstance();

        facebookLinkButton = findViewById(R.id.facebookLinkIcon);
        TwitterLinkIcon = findViewById(R.id.tweeterIcon);
        InstaramLinkButton = findViewById(R.id.instagramIcon);

        //logOutButton = (Button)findViewById(R.id.log_out_button);


        checkUserContactPermission();


        facebookLinkTextView = findViewById(R.id.faceboolinkEdit);
        twitterLinkTextView = findViewById(R.id.tweeterLinkEdit);
        instagramLinkTextView = findViewById(R.id.instagramIconEdit);

        contentRater = findViewById(R.id.visitOnlineShopButton);


        mediaView = findViewById(R.id.goToMedia);

        contactView = findViewById(R.id.profile_contact_details);



        contentRater.setVisibility(View.INVISIBLE);
        contentRater.setEnabled(false);

        contentRaterText = findViewById(R.id.visiOnlineShopText);

        contentRaterText.setVisibility(View.INVISIBLE);

        facebookLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        twitterLinkTextView .setMovementMethod(LinkMovementMethod.getInstance());
        instagramLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        //usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //usersRef.keepSynced(true);

        //friendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend_request");

        //mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoStoreAdmins");

        //mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("ContactRequest");

       // mDatabaseRef_z = FirebaseDatabase.getInstance().getReference().child("Users");

        contentRater.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.visitOnlineShopButton);
                Intent visitstore = new Intent(UserProfileActivity.this, CatchUp.class);
                visitstore.putExtra("visit_user_id",StoreKey);
                startActivity(visitstore);
            }
        });

        //friendRequestRef.keepSynced(true);
        //addFriendButton = (Button) findViewById(R.id.mapsAddFriendButton);
        //declineRequestButton = (Button)findViewById(R.id.declineRequest);
        profileUserName = findViewById(R.id.profileName);
        //commentButton = (Button)findViewById(R.id.view_from_maps_comment);
        mapsViewuserProfileImage = findViewById(R.id.viewfrom_maps_ImageView);
        //likeButton = (Button)findViewById(R.id.viewFromMaps_likeButton);

        //profileName =(ImageView) findViewById(R.id.profileName);
       // profileEmail = (ImageView)findViewById(R.id.profileEmail);
        //profileProffession = (ImageView)findViewById(R.id.profileProffession);
        //profileMobile = (ImageView)findViewById(R.id.profileMobile);

        profileNameText = findViewById(R.id.profileStatus);
        //profileEmailText = (TextView)findViewById(R.id.profileEmailText);
        //profileProffessionText = (TextView)findViewById(R.id.profileProffessionText);
        //profileMobileText = (TextView)findViewById(R.id.profileMobileText);

        //StateCondition

        CURRENT_STATE = "not_friends";

        String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();


        VisitedUserId = on_maps_visited_user_id;

         //This key is Carried from the PagiisMaps
        //receiver_user_id = getIntent().getExtras().get("visited_user_id").toString();//Visited / onClicked user Profile Id.




        mediaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                findViewById(R.id.goToMedia);
                String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();
                Intent intent = new Intent(UserProfileActivity.this,VisitProfilePosts.class);
                intent.putExtra("visited_user_id",on_maps_visited_user_id);
                startActivity(intent);

            }
        });


        contactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                findViewById(R.id.goToMedia);
                Toast.makeText(UserProfileActivity.this, "Please Waite while PAGiiS checks permissions", Toast.LENGTH_SHORT).show();
                checkUserContactPermission();

            }
        });




        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            mAuth.signOut();
            Intent loginIntent = new Intent(UserProfileActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
            //Go to login
        }
        else{

            //String sender_user_id = mAuth.getInstance().getCurrentUser().getUid();
            usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

            final String sender_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

            on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();


            usersRef.child(on_maps_visited_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    String username = dataSnapshot.child("userNameAsEmail").getValue().toString();
                    //String profileDp = dataSnapshot.child("userThumbImageDp").getValue().toString();

                   // String fullName = dataSnapshot.child("full_name").getValue().toString();
                    //String  mobileNumber  = dataSnapshot.child("mobile_number").getValue().toString();
                    //String  email  = dataSnapshot.child("email_address").getValue().toString();
                    //String  proffession  = dataSnapshot.child("proffession").getValue().toString();
                    String status = dataSnapshot.child("userDefaultStatus").getValue().toString();
                    String  userImage = dataSnapshot.child("userImageDp").getValue().toString();

                    facebookLink = dataSnapshot.child("facebookLink").getValue().toString();
                    twitterLink = dataSnapshot.child("twitterLink").getValue().toString();
                    instagramLink = dataSnapshot.child("instagramLink").getValue().toString();

                     profileUserName.setText(username);
                     profileNameText.setText(status);

                    facebookLinkTextView.setText(facebookLink);
                    twitterLinkTextView.setText(twitterLink);
                    instagramLinkTextView.setText(instagramLink);


                     String localString = "userDefaultDp";

                    /* profileNameText.setText(fullName);
                    profileEmailText.setText(email);
                    profileProffessionText.setText(proffession);*/

                        if(userImage.compareTo(localString) != 0) {

                            RequestOptions options = new RequestOptions();

                            Glide.with(UserProfileActivity.this).load(userImage).apply(options.centerCrop()).thumbnail(0.75f).into(mapsViewuserProfileImage);
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
       checkifAdmindTrue();
       myLastLocationDetailsMetod();

       myRequestDetails();

       facebookLinkButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               view.findViewById(R.id.facebookLinkIcon);

               final String link = facebookLink;

               if(link != null &&  !link.isEmpty()  && link.compareTo("null")!= 0 && Patterns.WEB_URL.matcher(link).matches() )
               {
                   Intent intent = new Intent(UserProfileActivity.this, LinkView.class);
                   intent.putExtra("visit_user_id",link);
                   startActivity(intent);

               }else
               {
                   Toast.makeText(UserProfileActivity.this, "User not linked", Toast.LENGTH_SHORT).show();
               }




           }
       });


        InstaramLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.facebookLinkIcon);

                final String link = instagramLink;

                if(link != null &&  !link.isEmpty()  && link.compareTo("null")!= 0 && Patterns.WEB_URL.matcher(link).matches() )
                {
                    Intent intent = new Intent(UserProfileActivity.this, LinkView.class);
                    intent.putExtra("visit_user_id",link);
                    startActivity(intent);

                }else
                {
                    Toast.makeText(UserProfileActivity.this, "User not linked", Toast.LENGTH_SHORT).show();
                }


            }
        });


        TwitterLinkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.facebookLinkIcon);

                final String link = twitterLink;

                if(link != null &&  !link.isEmpty()  && link.compareTo("null")!= 0 && Patterns.WEB_URL.matcher(link).matches() )
                {
                    Intent intent = new Intent(UserProfileActivity.this, LinkView.class);
                    intent.putExtra("visit_user_id",link);
                    startActivity(intent);

                }else
                {
                    Toast.makeText(UserProfileActivity.this, "User not linked", Toast.LENGTH_SHORT).show();
                }



            }
        });


    }


    private void myRequestDetails()
    {
        final  String user_id = mAuth.getCurrentUser().getUid();

        mDatabaseRef_z = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mDatabaseRef_z.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    MyName = dataSnapshot.child("userNameAsEmail").getValue().toString();
                    myDpUrl = dataSnapshot.child("userImageDp").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void myLastLocationDetailsMetod() {

        String user_id = mAuth.getCurrentUser().getUid();

        locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation").child(user_id);

        locationDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()) {
                    myLastLocationDetails = dataSnapshot.getValue().toString();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "last location not found", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }



    private void checkUserContactPermission()

    {
        final String userId = mAuth.getCurrentUser().getUid();
        final String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();
        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("ContactRequest").child(userId);
        mDatabaseRef_y.child(on_maps_visited_user_id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if(ds.hasChild("share") && dataSnapshot.child("share").getValue() != null && dataSnapshot.child("share").getValue().toString().compareTo("YES")==0 )
                        {

                            //contactKey = dataSnapshot.child("permissionToContact").getValue().toString();
                            Intent intent = new Intent(UserProfileActivity.this,ViewUserContactDetails.class);
                            intent.putExtra("visited_user_id",on_maps_visited_user_id);
                            startActivity(intent);
                        }
                    }

                }else
                {

                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(UserProfileActivity.this);
                    final View mView = getLayoutInflater().inflate(R.layout.send_contact_request, null);

                    final String userId = mAuth.getCurrentUser().getUid();
                    String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();
                    DatabaseReference mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("UserContactListRequests").child(userId).child(on_maps_visited_user_id);

                    ImageView logoutImage = mView.findViewById(R.id.sendContactRequest);
                    ImageView cancelLogOutImage = mView.findViewById(R.id.cancelConatctRequest);


                    logoutImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            view.findViewById(R.id.sendContactRequest);
                            sendContactRequest();
                            mBuilder.setView(mView);
                            AlertDialog dialog = mBuilder.create();
                            dialog.dismiss();

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                Toast.makeText(UserProfileActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }

    private void sendContactRequest()
    {

        requestDefaultText = "contactRequest";

        final String userId = mAuth.getCurrentUser().getUid();
        String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();
        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("ContactRequest").child(on_maps_visited_user_id);

        mDatabaseRef_y.child(userId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {


                final createEventFile upload = new createEventFile(MyName, myDpUrl,requestDefaultText, userId,requestDefaultText , requestDefaultText, requestDefaultText,"Would like to contact you, 0k ??","1","1","1","1","1",myLastLocationDetails,myDpUrl,MyName,"live");

                mDatabaseRef_y.setValue(upload, new DatabaseReference.CompletionListener()
                        {
                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   DatabaseReference databaseReference) {

                                //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                Toast.makeText(UserProfileActivity.this, "Request Sent.", Toast.LENGTH_SHORT).show();

                                mProgressCircle.setVisibility(View.INVISIBLE);
                                tagContactRequest();
                                // String uniqueKey = databaseReference.getKey();
                                //Create the function for Clearing/The ImageView Widget.
                            }
                        });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                Toast.makeText(UserProfileActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void tagContactRequest()
    {

        final String userId = mAuth.getCurrentUser().getUid();
        String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();
        DatabaseReference mDatabaseRef_contacta = FirebaseDatabase.getInstance().getReference("UserContactListRequests").child(on_maps_visited_user_id).child(userId);

        mDatabaseRef_contacta.child("permissionToContact").setValue("No").addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                Toast.makeText(UserProfileActivity.this, "Tag successful", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void checkifAdmindTrue()
    {
        final String userId = mAuth.getCurrentUser().getUid();

        String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();

        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("SiroccoStoreAdmins").child(on_maps_visited_user_id);

        mDatabaseRef_x.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if(ds.hasChild("admin") && dataSnapshot.child("admin").getValue() != null)
                        {

                            contentRater.setEnabled(true);
                            contentRater.setVisibility(View.VISIBLE);
                            StoreKey = dataSnapshot.child("admin").getValue().toString();
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                Toast.makeText(UserProfileActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void viewMaxprofileDp()
    {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(UserProfileActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.alert_max_dialog_view,null);


        final String on_maps_visited_user_id = getIntent().getExtras().get("on_maps_visited_user_id").toString();

        final ImageView maxViewImage = mView.findViewById(R.id.maxViewPage);

        String user_id = mAuth.getCurrentUser().getUid();

        //inal StorageReference deletImage = mStorage.getReferenceFromUrl(selectedImage.getImageUrl());

        //Intent intent = new Intent(MemePage.this, MaxView.class);
        //intent.putExtra("visit_user_id", user_id );
        //intent.putExtra("maxImageId", selectedKey);

        usersRef.child(on_maps_visited_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String imageProfileDP = dataSnapshot.child("userImageDp").getValue().toString();
                // Check if The Default Frofile is set or Not

                if (!imageProfileDP.equals("userDefaultDp")) {
                    Picasso.with(UserProfileActivity.this).load(imageProfileDP).placeholder(R.drawable.default_display_pic).into(maxViewImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

}
