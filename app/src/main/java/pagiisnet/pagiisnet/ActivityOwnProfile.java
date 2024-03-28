package pagiisnet.pagiisnet;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import pagiisnet.pagiisnet.Utils.ViewStoreItemAdapter;

public class ActivityOwnProfile extends AppCompatActivity implements ViewStoreItemAdapter.OnItemClickListener
{

    private ImageView userImageDp;

    private ImageView userContactDetails;

    private TextView userName;
    private RecyclerView mRecyclerView;
    private ViewStoreItemAdapter mAdapter;

    private ImageView shareLinkButton;
    private ImageView closeLinkView;


    private CardView userMemelayout;

    private TextView Status;
    private TextView PostTitle;

    private  int position;

    private WebView richLinkView;

    private ImageView changeDp;

    private List<ImageUploads> mUploads;

    private DatabaseReference userProfileInfor;


    private ImageView saveinfo;

    private ImageView editStatus;

    private  ImageView AddContent;
    private String StringCarrier;



    private  String DeleteKeyAlternative;

    private FloatingActionButton videosButton;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_x;
    private DatabaseReference mDatabaseRef_y;

    private ValueEventListener mDBlistener;

    private DatabaseReference mDatabaseUploads;

    private Button logoutText;

    private ProgressBar mProgressBar;



    private StorageTask mUploadTask;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private ImageView profileEMail, profileMobile, profileProffession, profileName;

    private TextView profileNameText, profileEmailText, profileMobileText, profileProffessionText;

    private TextView facebookLinkTextView,twitterLinkTextView,instagramLinkTextView;

    private StorageReference userImageStorageDpRef;
    private DatabaseReference getUserProfileDataRef;

    private FloatingActionButton facebookIcon, twitterIcon, instagramIcon;

    private DatabaseReference usernameDataRef;

    private FirebaseAuth mAuth;

    private final static int Gallery_Pick = 1;

    private Uri resultUri;

    private  TextView profileViews;

    private TextView profilePosts;

    private TextView profileTags;


    private ImageView mProgressCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_profile);
        //mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView = findViewById(R.id.memeRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mUploads = new ArrayList<>();

        /*if(haveNetwork()){

            Toast.makeText(this, "Your Device is Not connected to the internet please check mobile data"+"\n"+"and Wifi Connections", Toast.LENGTH_LONG).show();

        }*/
        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.appBarLayout);


        setSupportActionBar(mToolbar);

        String User_id = mAuth.getCurrentUser().getUid();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        userProfileInfor = FirebaseDatabase.getInstance().getReference("Users").child(User_id);


        if(mAuth.getCurrentUser() == null)
        {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");

        editStatus = findViewById(R.id.writeStatus);
        mAdapter = new ViewStoreItemAdapter(ActivityOwnProfile.this, mUploads);

        mAdapter.setOnItemClickListener(ActivityOwnProfile.this);

        mRecyclerView.setAdapter(mAdapter);

        shareLinkButton = findViewById(R.id.ripple_button);

        //LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //View actionBarView = layoutInflater.inflate(R.layout.chats_custom_bar,null);
        //actionBar.setCustomView(actionBarView);

        editStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.writeStatus);

                openStatusEditText();
            }
        });


         userImageDp = findViewById(R.id.ImageDP);
         closeLinkView = findViewById(R.id.close_link);
         userName = findViewById(R.id.profileName);
         PostTitle = findViewById(R.id.post_title);
         Status = findViewById(R.id.profileStatus);
         logoutText = findViewById(R.id.LogoutText);
         mProgressBar = findViewById(R.id.progress_circle_own_profile);
         facebookIcon = findViewById(R.id.facebookLinkIcon);
         twitterIcon = findViewById(R.id.tweeterIcon);
         instagramIcon = findViewById(R.id.instagramIcon);
         AddContent = findViewById(R.id.addContent);

        //userMemelayout = findViewById(R.id.userMemeCardView);

        //userMemelayout.setVisibility(View.INVISIBLE);
        // mProgressCircle = findViewById(R.id.addContent);

        richLinkView = findViewById(R.id.memeImageView);

        richLinkView = new WebView(getApplicationContext());

        WebViewClient myWebViewClient = new WebViewClient();

        setContentView(richLinkView);

        richLinkView.setWebViewClient(myWebViewClient);



        AddContent.setEnabled(false);
        AddContent.setVisibility(View.INVISIBLE);



        profilePosts = findViewById(R.id.userProfilePosts);
        profileTags = findViewById(R.id.userProfileConnected);
        profileViews = findViewById(R.id.userProfileViews);
        userContactDetails = findViewById(R.id.profile_contact_details);





        //saveinfo = (ImageView) findViewById(R.id.saveInfo);


        mProgressBar.setVisibility(View.INVISIBLE);

        /*userImageDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.findViewById(R.id.ImageDP);

                viewMaxProfile();
            }
        });*/

        changeDp = findViewById(R.id.ChangeOwnDP);

        changeDp.setEnabled(false);
        changeDp.setVisibility(View.INVISIBLE);

        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.facebookLinkIcon);
                openFacebookLinkEdit();
            }
        });


        /*closeLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.close_link);
                userMemelayout.setVisibility(View.INVISIBLE);
                finish();
            }
        });

        shareLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.ripple_button);
               selectPostType();
            }
        });*/

        instagramIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.instagramIcon);
                openInstagramLinkEdit();
            }
        });



        AddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.addContent);
              Intent intent = new Intent(ActivityOwnProfile.this,ActivityUploadImage.class);
              intent.putExtra("share_item","null");
              startActivity(intent);
            }
        });


        twitterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.tweeterIcon);
                openTwitterLinkEdit();
            }
        });


        userImageDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.ImageDP);
                openFileChooser();
            }
        });

        userContactDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.profile_contact_details);
                openFileChooser();
            }
        });

        changeDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.ChangeOwnDP);
                openFileChooser();
            }
        });

        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.LogoutText);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityOwnProfile.this);
                View mView = getLayoutInflater().inflate(R.layout.logout_dialog, null);

                ImageView logoutImage = mView.findViewById(R.id.SignOutImage);
                ImageView cancelLogOutImage = mView.findViewById(R.id.cancelSignOutImage);


                logoutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.findViewById(R.id.SignOutImage);
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();


                    }
                });

                cancelLogOutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ActivityOwnProfile.class);
                        startActivity(intent);

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        //profileEmailText = (TextView) findViewById(R.id.profileEmailText);
        //profileNameText = (TextView) findViewById(R.id.profileNameText);
        //profileProffessionText = (TextView) findViewById(R.id.profileProffessionText);
        //profileMobileText = (TextView) findViewById(R.id.profileMobileText);

        //profileEMail = (ImageView) findViewById(R.id.profileEmail);
        //profileName = (ImageView) findViewById(R.id.profileNameIcon);
        //profileProffession = (ImageView) findViewById(R.id.profileProffession);
        //profileMobile = (ImageView) findViewById(R.id.profileMobile);

//       Intent for the EditText profile Email
        checkifAdmindTrue();

        openFileChooser();

    }



    private void checkifAdmindTrue()
    {

        final String userId = mAuth.getCurrentUser().getUid();
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        String onlineUserID = getIntent().getExtras().get("visit_user_id").toString();

        if (onlineUserID.compareTo(userId)==0)

        {

                        changeDp.setEnabled(true);
                        changeDp.setVisibility(View.VISIBLE);

                        String onlineUserId = mAuth.getCurrentUser().getUid();

                        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

                        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
                        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("uploads").child(onlineUserId);
                        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("Tags").child(onlineUserId);
                        getUserProfileDataRef.keepSynced(true);


                        getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
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


                                if (name.compareTo("UserName") == 0) {
                                    requestNameEdit();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        mDatabaseRef_y.child(onlineUserId).orderByValue().equalTo(onlineUserId).addValueEventListener(new ValueEventListener() {
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


                        String user_id = mAuth.getCurrentUser().getUid();

                        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


                        mDBlistener = mDatabaseRef.child(user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                mUploads.clear();

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                                    upload.setKey(postSnapshot.getKey());
                                    mUploads.add(upload);
                                }

                                mAdapter.notifyDataSetChanged();
                                AddContent.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(ActivityOwnProfile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                mProgressCircle.setVisibility(View.INVISIBLE);
                            }
                        });


                        AddContent.setEnabled(true);
                        AddContent.setVisibility(View.VISIBLE);



        }else
        {


            String onlineUserId = getIntent().getExtras().get("visit_user_id").toString();

            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");

            mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("uploads").child(onlineUserId);
            mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("Tags").child(onlineUserId);
            getUserProfileDataRef.keepSynced(true);


            getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
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


                    if(name.compareTo("UserName")== 0)
                    {
                        requestNameEdit();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



            mDatabaseRef_y.child(onlineUserId).orderByValue().equalTo(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                    if (dataSnapshot.exists())
                    {
                        String x = String.valueOf(dataSnapshot.getChildrenCount());

                        profileViews.setText(x);
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });

            mDatabaseRef_x.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                    if(dataSnapshot.exists())
                    {

                        String x = String.valueOf(dataSnapshot.getChildrenCount());
                        profilePosts.setText(x);

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });


            mDatabaseRef_y.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                    if(dataSnapshot.exists())
                    {
                        String x = String.valueOf(dataSnapshot.getChildrenCount());
                        profileTags.setText(x);

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

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



            mDBlistener = mDatabaseRef.child(onlineUserId).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        mUploads.add(upload);
                    }

                    mAdapter.notifyDataSetChanged();
                    AddContent.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ActivityOwnProfile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
            });



            AddContent.setEnabled(false);
            AddContent.setVisibility(View.INVISIBLE);

            changeDp.setEnabled(false);
            changeDp.setVisibility(View.INVISIBLE);


        }



    }


    private void requestNameEdit()
    {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityOwnProfile.this);
        View mView = getLayoutInflater().inflate(R.layout.alert_facebook_link_edit,null);
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");

        final EditText newStatus = mView.findViewById(R.id.facebookLinkEdit);
        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);
        final String current_userId = mAuth.getCurrentUser().getUid();
        final ImageView fbIcon = mView.findViewById(R.id.fbIcon);
        final TextView instructionText = mView.findViewById(R.id.instruction);

        fbIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.user));
        instructionText.setText("Edit Profile-Name");

        saveStatus.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveStatus);
                if(!(newStatus.getText() == null) && !TextUtils.isEmpty(newStatus.getText()))
                {

                    getUserProfileDataRef.child(current_userId).child("userNameAsEmail").setValue(newStatus.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            AlertDialog dialog = mBuilder.create();
                            dialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), ActivityOwnProfile.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                    });


                }else
                {
                    Toast.makeText(ActivityOwnProfile.this, "Please type in your facebookLink or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private String getFileExtension(Uri uri) {

        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //fucnction for network auth
   /* private Boolean haveNetwork()
    {

        boolean have_Wifi = false;
        boolean have_mobileData= false;

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for(NetworkInfo info:networkInfos)
        {
            if(info.getTypeName().equalsIgnoreCase("WIFI"))

                if(info.isConnected())

                have_Wifi = true;
            if(info.getTypeName().equalsIgnoreCase("MONBILE"))

                if(info.isConnected())

                have_mobileData = true;
        }

        return  have_Wifi || have_mobileData;

    }*/

    private void openStatusEditText() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityOwnProfile.this);
        View mView = getLayoutInflater().inflate(R.layout.alert_status_edit,null);
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");

        final EditText newStatus = mView.findViewById(R.id.statusEdit);
        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);

        final String current_userId = mAuth.getCurrentUser().getUid();

        saveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveStatus);
                if(!(newStatus.getText() == null))
                {
                    getUserProfileDataRef.child(current_userId).child("userDefaultStatus").setValue(newStatus.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            AlertDialog dialog = mBuilder.create();
                            Toast.makeText(ActivityOwnProfile.this, "Status saved successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });


                }else
                {
                    Toast.makeText(ActivityOwnProfile.this, "Please type in your status or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void openFacebookLinkEdit() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityOwnProfile.this);
        View mView = getLayoutInflater().inflate(R.layout.alert_facebook_link_edit,null);
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");

        final EditText newStatus = mView.findViewById(R.id.facebookLinkEdit);
        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);
        final String current_userId = mAuth.getCurrentUser().getUid();

        saveStatus.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveStatus);
                if(!(newStatus.getText() == null))
                {

                    AlertDialog dialog = mBuilder.create();

                    position = 1;
                    StringCarrier = newStatus.getText().toString();
                    userMemelayout.setVisibility(View.VISIBLE);
                    setLinkView();
                    dialog.dismiss();


                }else
                {
                    Toast.makeText(ActivityOwnProfile.this, "Please type in your facebookLink or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    private void selectPostType()
    {
        switch (position)
        {
            case 1:
                saveInforFacebook();
                break;
            case 2:
                saveInforInsta();
                break;
            case 3:
                saveInfor();
                break;
            default:
                break;

        }
    }

    private void setLinkView() {

        if(StringCarrier != null && !StringCarrier.isEmpty() && Patterns.WEB_URL.matcher(StringCarrier).matches())
        {
            richLinkView.loadUrl(StringCarrier);
            PostTitle.setText(StringCarrier);
        }else
        {
            Toast.makeText(this, "Link is invalid", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void openInstagramLinkEdit() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityOwnProfile.this);
        View mView = getLayoutInflater().inflate(R.layout.alert_instagram_link,null);

        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");

        final EditText newStatus = mView.findViewById(R.id.instagramLinkEdit);
        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);

        final String current_userId = mAuth.getCurrentUser().getUid();

        saveStatus.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveStatus);
                if(!(newStatus.getText() == null))
                {

                    AlertDialog dialog = mBuilder.create();
                    position = 2;
                    StringCarrier = newStatus.getText().toString();
                    setLinkView();
                    dialog.dismiss();

                }else
                {
                    Toast.makeText(ActivityOwnProfile.this, "Please type in your status or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    private void openTwitterLinkEdit() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityOwnProfile.this);
        View mView = getLayoutInflater().inflate(R.layout.alert_twitter_link_edit,null);
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");

        final EditText newStatus = mView.findViewById(R.id.twitterLinkEdit);
        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);

        final String current_userId = mAuth.getCurrentUser().getUid();

        saveStatus.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveStatus);
                if(!(newStatus.getText() == null))
                {
                    AlertDialog dialog = mBuilder.create();
                    position = 3;
                    StringCarrier = newStatus.getText().toString();
                    setLinkView();
                    dialog.dismiss();
                }else
                {
                    Toast.makeText(ActivityOwnProfile.this, "Please type in your status or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void viewMaxProfile()
    {
        mAuth = FirebaseAuth.getInstance();

        String onlineUserId = mAuth.getCurrentUser().getUid();

        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference().child("Users");

        userImageStorageDpRef = FirebaseStorage.getInstance().getReference().child("dp_Images");

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityOwnProfile.this);
        View mView = getLayoutInflater().inflate(R.layout.alert_max_dialog_view,null);

        final ImageView maxViewImage = mView.findViewById(R.id.maxViewPage);

        getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String  imageProfileDP = dataSnapshot.child("userImageDp").getValue().toString();
                // Check if The Default Frofile is set or Not
                mBuilder.setView(maxViewImage);
                Glide.with(ActivityOwnProfile.this).load(imageProfileDP).thumbnail(0.65f).into(maxViewImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

    //     setUSerInfo
    private void openDialog()
    {
        setProfileDialogue msetProfileDialogue = new setProfileDialogue();
        msetProfileDialogue.show(getSupportFragmentManager(), "Set Details Dialogue");

    }

    //@Override
    public void applyTexts(String username) {


        String usernameDataRef = username;

        profileNameText.setText(usernameDataRef);
    }


    private void openFileChooser()
    {

        Intent intent =  new  Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
             resultUri = data.getData();

            if(mUploadTask != null && mUploadTask.isInProgress())
            {
                Toast.makeText(ActivityOwnProfile.this, "PAGiiS image upload in progress !!", Toast.LENGTH_SHORT).show();
            }else
            {
                mProgressBar.setVisibility(View.VISIBLE);
                Upload();
            }
        }
    }

    private void saveInforFacebook()
    {
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
        final String current_userId = mAuth.getCurrentUser().getUid();

        if(StringCarrier != null && !StringCarrier.isEmpty())
        {
            getUserProfileDataRef.child(current_userId).child("facebookLink").setValue(StringCarrier).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid)
                {

                    Toast.makeText(ActivityOwnProfile.this, "linked", Toast.LENGTH_SHORT).show();

                }
            });

        }else
        {
            Toast.makeText(this, "Sorry link was not captured.", Toast.LENGTH_SHORT).show();
        }

    }



    private void saveInforInsta()
    {
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
        final String current_userId = mAuth.getCurrentUser().getUid();

        if(StringCarrier != null && !StringCarrier.isEmpty())
        {
            getUserProfileDataRef.child(current_userId).child("instagramLink").setValue(StringCarrier).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid)
                {

                    Toast.makeText(ActivityOwnProfile.this, "linked", Toast.LENGTH_SHORT).show();

                }
            });


        }else
        {
            Toast.makeText(this, "Sorry link was not captured.", Toast.LENGTH_SHORT).show();
        }

    }

   private void saveInfor()
    {
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
        final String current_userId = mAuth.getCurrentUser().getUid();

        if(StringCarrier != null && !StringCarrier.isEmpty())
        {
            getUserProfileDataRef.child(current_userId).child("twitterLink").setValue(StringCarrier).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid)
                {
                    Toast.makeText(ActivityOwnProfile.this, "linked", Toast.LENGTH_SHORT).show();
                }
            });

        }else
        {
            Toast.makeText(this, "Sorry link was not captured.", Toast.LENGTH_SHORT).show();
        }

    }

    private void Upload()
    {
        if (resultUri != null) {

            String onlineUserId = mAuth.getCurrentUser().getUid();

            getUserProfileDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserId);

            userImageStorageDpRef = FirebaseStorage.getInstance().getReference().child("dp_Images");

            //final String imageUrl = String.valueOf(resultUri);

            final StorageReference storageReference = userImageStorageDpRef.child(onlineUserId);

            mUploadTask = storageReference.putFile(resultUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                Uri finalImageUri;

                                String finalImageUrl;

                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    finalImageUrl = String.valueOf( uri);

                                    finalImageUri = uri;


                                    getUserProfileDataRef.child("userImageDp").setValue(finalImageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(ActivityOwnProfile.this, "Upload successful", Toast.LENGTH_LONG).show();
                                            Glide.with(getApplicationContext()).load(resultUri).into(userImageDp);
                                            changeDp.setVisibility(View.VISIBLE);
                                            mProgressBar.setVisibility(View.INVISIBLE);
                                            finish();
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
                            Toast.makeText(ActivityOwnProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.VISIBLE);
                            changeDp.setVisibility(View.INVISIBLE);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(int position)
    {
        ImageUploads selectedImage = mUploads.get(position);

        String selectedKey = selectedImage.getKey();

        String imageUrl = selectedImage.getImageUrl();

        String maxImageUserId = selectedImage.getUserId();

        if(!imageUrl.isEmpty() && !selectedKey.isEmpty())
        {
            Intent intent = new Intent(ActivityOwnProfile.this,PagiisMaxView.class);
            intent.putExtra("imageKeyMAx", selectedKey);
            intent.putExtra("imageUrlMax",imageUrl);
            intent.putExtra("imageUserId",maxImageUserId );
            startActivity(intent);

        }else
        {
            Toast.makeText(this, "Pagiis failed to open maxview.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onWhatEverClick(int position)
    {
        Intent intent = new Intent(ActivityOwnProfile.this, ActivityOwnProfile.class);
        startActivity(intent);
        finish();

    }

    private void deleteAlternative()
    {

        mDatabaseRef.addValueEventListener(new ValueEventListener()
        {
            String DeleteFinalString;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot ds :dataSnapshot.getChildren())
                {
                    if(ds.getKey().compareTo(DeleteKeyAlternative)== 0)
                    {

                        mDatabaseRef.child(DeleteKeyAlternative).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(ActivityOwnProfile.this, "Item sent to bin..", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                DeleteFinalString = String.valueOf(e);
                                Toast.makeText(ActivityOwnProfile.this, DeleteFinalString + "PAGiiS Failed to Delete File, please try again :Later", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(ActivityOwnProfile.this, "item delete failed.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBlistener);
    }


    @Override
    protected void onStart()
    {
        super.onStart();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        mDatabaseRef.removeEventListener(mDBlistener);
    }
}


