package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.Utils.ViewStoreItemAdapter;

public class ViewEvent extends AppCompatActivity implements  ViewStoreItemAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ViewStoreItemAdapter mAdapter;
    private TextView storeNameText;
    private TextView storeAdminText;
    private TextView storeEmailText;

    private TextView storeBankDetailText;
    private TextView storeCellText;
    private TextView storeVisitsText;
    private TextView storePurchasesText;
    private TextView storeCustomersText;



    private ImageView storeAdminIcon;
    private ImageView storeLogoIcon;
    private ProgressBar mProgressCircle;


    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private String getKey;


    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDataRef;

    private DatabaseReference mDatabaseStoreItems;
    private DatabaseReference mDatabaseReTest;

    private ValueEventListener mDBlistener;
    private List<ImageUploads> mUploads;

    private String StoreMainKey;
    private Iterator<lockedImagesTag> iterator;
    private List<tags> tagedUsers;

    private androidx.appcompat.widget.Toolbar mToolbar;
    private final int raterBarValue = 0;

    private TextView  adminTextCOntrol;

    private String on_maps_visited_user_id;

    private String storeAdmin;
    private String storeLogo;
    private String storeName;
    private String storeEmail;
    private String storeBankDetail;
    private String storeCellNumber;
    private String storeCatergory;


    //private DatabaseReference mDataRef;
    //private DatabaseReference mDatabaseRef;

    private ImageView RippleButton;

    private String itemName;

    private Long tagedUsersSizeValue;
    private Long tagedUsersDabaseCount;


    private String MyName;
    private String MyLastLocation;
    private String MyProfileUrl;


    private DatabaseReference mDatabaseRef_x;
    private DatabaseReference lockedImages;
    private DatabaseReference myDetailsRef;
    private DatabaseReference myLocatinLastRef;

    private String StoreViews;
    private String StoreVisits;
    private String StorePurchases;

    private FloatingActionButton contentRater;
    private DatabaseReference getUserProfileDataRef;

    private DatabaseReference getUserAttendanceStatus;


    private  String myLastLocationDetails;


    private  FloatingActionButton rippleButtonAdmin,videoContentView;

    private  ImageView addContentToEvent;

    private  String eventWelcomeNote;


    private final String attendin = "false";
    private final String adminTrue = "false";


    private  String  attendanceValue;


    @SuppressLint("RestrictedApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        mToolbar = findViewById(R.id.appBarLayout);

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("@Sir_occo #Online_Shop");

        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance();

        if (mAuth.getCurrentUser() == null) {
            mAuth.signOut();
            Intent intent = new Intent(ViewEvent.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();

        mRecyclerView = findViewById(R.id.memeRecyclerView);

        mRecyclerView.setHasFixedSize(true);

        rippleButtonAdmin = findViewById(R.id.adminRipple);
        addContentToEvent = findViewById(R.id.VideoAdd);
        videoContentView = findViewById(R.id.adminVideos);


        rippleButtonAdmin.setVisibility(View.INVISIBLE);
        rippleButtonAdmin.setEnabled(false);

        addContentToEvent.setVisibility(View.INVISIBLE);
        addContentToEvent.setEnabled(false);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        storeNameText = findViewById(R.id.storeNameTextView);
        storeAdminText = findViewById(R.id.storeAdminTextView);
        storeEmailText = findViewById(R.id.storeEmailTextView);
        storeBankDetailText =findViewById(R.id.storeBankingDetailsText);
        storeCellText = findViewById(R.id.storeWalletTextView);
        storeVisitsText = findViewById(R.id.visitsTextView);
        storePurchasesText = findViewById(R.id.customerViewsText);
        storeCustomersText = findViewById(R.id.purchasesViewText);
        adminTextCOntrol = findViewById(R.id.eventAdminTextSetup);

        adminTextCOntrol.setVisibility(View.INVISIBLE);

        adminTextCOntrol.setEnabled(false);

        storeLogoIcon = findViewById(R.id.storeLogo);
        storeAdminIcon = findViewById(R.id.storeAdminIcon);
        contentRater = findViewById(R.id.adminVideos);


        contentRater.setEnabled(false);

        contentRater.setVisibility(View.INVISIBLE);

        contentRater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.adminVideos);
                Intent intent = new Intent(ViewEvent.this, ViewStreams.class);
                intent.putExtra("visit_user_id",on_maps_visited_user_id);
                startActivity(intent);

            }
        });

        storeAdminIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.storeAdminIcon);
                Intent intent = new Intent(ViewEvent.this, UserProfileActivity.class);
                intent.putExtra("visit_user_id", storeAdmin);
                startActivity(intent);

            }
        });




        rippleButtonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.adminRipple);
                updateArrayList();


            }
        });

        adminTextCOntrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.eventAdminTextSetup);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewEvent.this);
                View mView = View.inflate(ViewEvent.this,R.layout.activity_user_post,null);

                ImageView logoutImage = mView.findViewById(R.id.cameraOption);
                ImageView postLink = mView.findViewById(R.id.linkEditImageView);
                ImageView cancelLogOutImage = mView.findViewById(R.id.GalleryOption);
                ImageView homebutton = mView.findViewById(R.id.homeButotn);

                on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();


                postLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        view.findViewById(R.id.linkEditImageView);
                        mDatabaseReTest = FirebaseDatabase.getInstance().getReference().child("LiveEvents").child(on_maps_visited_user_id);


                        mDatabaseReTest.child(on_maps_visited_user_id).child("status").setValue("offline").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {

                                Toast.makeText(ViewEvent.this, "Event closed successfully ", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });

                homebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.findViewById(R.id.homeButotn);
                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        ViewEvent.this.startActivity(intent);


                    }
                });


                logoutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.findViewById(R.id.cameraOption);
                        mDatabaseReTest = FirebaseDatabase.getInstance().getReference().child("LiveEvents").child(on_maps_visited_user_id);


                        mDatabaseReTest.child(on_maps_visited_user_id).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {

                                Toast.makeText(ViewEvent.this, "Event closed successfully ", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                });

                cancelLogOutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        view.findViewById(R.id.GalleryOption);
                        Intent intent = new Intent(getApplicationContext(),ActivityUploadImage.class);
                        intent.putExtra("share_item_id","null");
                        ViewEvent.this.startActivity(intent);

                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();

                dialog.show();

            }
        });


        //returnToMaps = findViewById(R.id.returnToMaps);

        mProgressCircle = findViewById(R.id.progress_circle_user_memes);

        mProgressCircle.setVisibility(View.VISIBLE);

        mUploads = new ArrayList<>();

        tagedUsers = new ArrayList<>();
        mAdapter = new ViewStoreItemAdapter(ViewEvent.this, mUploads);
        mAdapter.setOnItemClickListener(ViewEvent.this);
        mRecyclerView.setAdapter(mAdapter);




        mDatabaseStoreItems = FirebaseDatabase.getInstance().getReference().child("SiroccoOnSaleItem");
        mDatabaseReTest = FirebaseDatabase.getInstance().getReference().child("PagiisLiveEvents").child(on_maps_visited_user_id);
        mDataRef = FirebaseDatabase.getInstance().getReference().child("SiroccoStoreVisits").child(on_maps_visited_user_id);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("SiroccoPurchasedItem").child(on_maps_visited_user_id);

        getUserAttendanceStatus = FirebaseDatabase.getInstance().getReference().child("EventAttendance").child(on_maps_visited_user_id);


        mDatabaseReTest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    storeName = dataSnapshot.child("nameShop").getValue().toString();
                    storeEmail = dataSnapshot.child("emailStoreAddress").getValue().toString();
                    MyLastLocation = dataSnapshot.child("location").getValue().toString();
                    storeCatergory = dataSnapshot.child("exRating").getValue().toString();
                    storeLogo = dataSnapshot.child("imageUrlSHopICon").getValue().toString();
                    storeAdmin = dataSnapshot.child("storeAdminId").getValue().toString();
                    eventWelcomeNote = dataSnapshot.child("welcomeNote").getValue().toString();


                    storeNameText.setText(storeName);
                    storeEmailText.setText(storeEmail);
                    storeCellText.setText(storeCatergory);
                    storeBankDetailText.setText(MyLastLocation);
                    storeAdminText.setText(storeAdmin);

                    RequestOptions options = new RequestOptions();

                    Glide.with(ViewEvent.this).load(storeLogo).apply(options.centerCrop()).thumbnail(0.75f).into(storeLogoIcon);


                }else
                {
                    Toast.makeText(ViewEvent.this, "Store currently unavailable. please visit later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        getDataNormally();
        checkInStore();
        myLastLocationDetailsMetod();
        getMyInfor();

        checkIfAdminOrAttendin();

        eventStatus();
    }

    private void eventStatus()
    {

        mDatabaseReTest = FirebaseDatabase.getInstance().getReference().child("LiveEvents").child(on_maps_visited_user_id);


        mDatabaseReTest.child(on_maps_visited_user_id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("status").getValue().toString().compareTo("offline")==0)
                {

                    rippleButtonAdmin.setVisibility(View.INVISIBLE);
                    rippleButtonAdmin.setEnabled(false);

                    addContentToEvent.setVisibility(View.INVISIBLE);
                    addContentToEvent.setEnabled(false);

                    videoContentView.setVisibility(View.INVISIBLE);
                    videoContentView.setEnabled(false);

                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewEvent.this);
                    View mView = View.inflate(ViewEvent.this,R.layout.event_closed_notice,null);


                    ImageView homebutton = mView.findViewById(R.id.homeButotn);



                    homebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            view.findViewById(R.id.homeButotn);
                            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                            ViewEvent.this.startActivity(intent);


                        }
                    });


                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();

                    dialog.show();



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkIfAdminOrAttendin()
    {

        final String user_id = mAuth.getCurrentUser().getUid();

        getUserAttendanceStatus = FirebaseDatabase.getInstance().getReference().child("EventAttendance").child(on_maps_visited_user_id);


        getUserAttendanceStatus.child(user_id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild(user_id) && dataSnapshot.child(user_id).getValue().toString() =="true" )
                    {
                        rippleButtonAdmin.setVisibility(View.VISIBLE);
                        rippleButtonAdmin.setEnabled(true);


                        addContentToEvent.setVisibility(View.VISIBLE);
                        addContentToEvent.setEnabled(true);

                        getUserAttendanceStatus.child("posts").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {

                               attendanceValue = String.valueOf(dataSnapshot.getChildrenCount());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        if(dataSnapshot.child("admin").getValue().toString() == "true" )
                        {


                            adminTextCOntrol.setVisibility(View.VISIBLE);

                            adminTextCOntrol.setEnabled(true);


                        }

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void myLastLocationDetailsMetod()
    {

        String user_id = mAuth.getCurrentUser().getUid();

        myDetailsRef = FirebaseDatabase.getInstance().getReference().child("MyLastLocation").child(user_id);

        myDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    myLastLocationDetails = dataSnapshot.getValue().toString();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                String e = databaseError.getMessage();
                Toast.makeText(ViewEvent.this, "last location not found"+"Results"+":"+e, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void getMyInfor()
    {


        String user_id = mAuth.getCurrentUser().getUid();

        myDetailsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        myDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String myName =  dataSnapshot.child("userNameAsEmail").getValue().toString();

                String myProfileImae = dataSnapshot.child("userImaeDp").getValue().toString();


                MyName = myName;
                MyProfileUrl = myProfileImae;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }


    private void checkInStore()
    {

        String current_user_id = mAuth.getCurrentUser().getUid();
        getUserAttendanceStatus = FirebaseDatabase.getInstance().getReference().child("EventAttendance").child(on_maps_visited_user_id);
        getUserAttendanceStatus.child("visists").child(current_user_id).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                mDataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        String customerVisits = String.valueOf(dataSnapshot.getChildrenCount());
                        storeVisitsText.setText(customerVisits);
                        getStoreSales();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                        String e = databaseError.getMessage();
                        Toast.makeText(ViewEvent.this, "Error Occured"+"<result>"+":"+e, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    private void getStoreSales()
    {
        String current_user_id = mAuth.getCurrentUser().getUid();
        getUserAttendanceStatus = FirebaseDatabase.getInstance().getReference().child("EventAttendance").child(on_maps_visited_user_id);

        getUserAttendanceStatus.child("attendin").orderByValue().equalTo("true").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists())
                {

                    String storeSales = String.valueOf(dataSnapshot.getChildrenCount());
                    storePurchasesText.setText(storeSales);
                    storePosts();


                }else
                {
                    Toast.makeText(ViewEvent.this, "Event currently unavailable. please visit later.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                String e = databaseError.getMessage();
                Toast.makeText(ViewEvent.this, "Error occured "+"results"+e, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void storePosts()

    {

        String current_user_id = mAuth.getCurrentUser().getUid();
        getUserAttendanceStatus = FirebaseDatabase.getInstance().getReference().child("EventAttendance").child(on_maps_visited_user_id);

        getUserAttendanceStatus.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String storeSales = String.valueOf(dataSnapshot.getChildrenCount());
                    storeCustomersText.setText(storeSales);

                }else
                {
                    Toast.makeText(ViewEvent.this, "Event currently unavailable. please visit later.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                String e = databaseError.getMessage();
                Toast.makeText(ViewEvent.this, "Error occured "+"results"+e, Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void getDataNormally()
    {
        mDatabaseStoreItems.orderByChild("EventUploads").equalTo(on_maps_visited_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists())
                {
                    mUploads.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        ImageUploads upload = ds.getValue(ImageUploads.class);
                        upload.setKey(ds.getKey());
                        mUploads.add(upload);
                    }
                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }else
                {
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)

            {
                String e = databaseError.getMessage();
                Toast.makeText(ViewEvent.this, "failed to retrieve file " +"result"+":"+e, Toast.LENGTH_SHORT).show();

            }
        });
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
                                getDataNormal();


                            }


                            //getDataNormally();

                            //0799506310

                        } else {

                            Toast.makeText(ViewEvent.this, "Online posts currently unavailable.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                } else {
                    Toast.makeText(ViewEvent.this, "Pagiis online users currently unavailable.", Toast.LENGTH_SHORT).show();

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

    private void getDataNormal() {


        if (!tagedUsers.isEmpty()) {

            for (int i = 0; i < tagedUsers.size(); i++) {
                tags x = tagedUsers.get(i);
                final String taged_id = x.getUser_tagID();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("LiveEventsAndRipples").child(taged_id);
                //Query mSearchQuery = mDatabaseRef.orderByChild("name").startAt(searchedtext).endAt(searchedtext + "\uf8ff");
                final String userIdRef = mAuth.getCurrentUser().getUid();


                if(tagedUsersSizeValue <= tagedUsersDabaseCount)
                {

                    createEventFile upload = new createEventFile(storeName, storeLogo, "just rippled to you", storeAdmin, "emailAddress", "storecell", "StoreBankinDetails ", eventWelcomeNote, "*", "*", "*", "*", "*", MyLastLocation, userIdRef, MyName,"live");
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
                {
                    Toast.makeText(this, "Rippled successfully !", Toast.LENGTH_SHORT).show();
                    Intent intent  = new Intent(ViewEvent.this,ViewEvent.class);
                    startActivity(intent);
                }



            }


        }else

        {
            Toast.makeText(ViewEvent.this, "No nearby online users found ", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_memes_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //This button is for the explicit content view
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.sirocco_icon)
        {
            Intent intent = new Intent(ViewEvent.this, SiroccoPage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position)
    {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String imageUrl = selectedImage.getImageUrl();

        final String userKeyId = selectedImage.getUserId();

        String user_id = mAuth.getCurrentUser().getUid();

        Intent intent = new Intent(ViewEvent.this ,SiroccoMaxView.class);
        intent.putExtra("imageKeyMAx", selectedKey);
        intent.putExtra("imageUrlMax",imageUrl);
        intent.putExtra("imageUserId", userKeyId);
        intent.putExtra("imageStoreMainKey", StoreMainKey);
        startActivity(intent);
    }

    @Override
    public void onWhatEverClick(int position)
    {
        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        String getUserRef = selectedImage.getUserId();

        if(!getUserRef.isEmpty())
        {
            Intent intent = new Intent(ViewEvent.this,SiroccoPage.class);
            intent.putExtra("visit_user_id", getUserRef);
            startActivity(intent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        eventStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(ViewEvent.this,ActivityNotifications.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }
}
