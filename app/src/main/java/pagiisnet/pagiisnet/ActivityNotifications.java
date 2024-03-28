package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pagiisnet.pagiisnet.Utils.LiveEventsAndRipplesAdaptor;
import pagiisnet.pagiisnet.Utils.RequestAdaptor;


public class ActivityNotifications extends AppCompatActivity implements RequestAdaptor.OnItemClickListener,LiveEventsAndRipplesAdaptor.OnItemClickListener {

    private static final String TAG = "ActivityNotifications" ;
    private RecyclerView friendRequestListRecV;

    private RecyclerView LiveRecyclerView;

    private RequestAdaptor mAdapter;

    private LiveEventsAndRipplesAdaptor mapsAdapter;

    String MaxImageUrl;

    private View mMainView;

    private DatabaseReference friendRequestUserRef;
    private DatabaseReference mDatabaseRef_x;




    private  Button createEvent;


    private List<tags> tagedUsers;

    private List<createEventFile> radiusUserNotifications;

    private FirebaseAuth mAuth;

    private ValueEventListener mDBlistener;

    private ProgressBar mProgressCircle;

    private List<createEventFile> mUploadsRequests;


    private androidx.appcompat.widget.Toolbar mToolbar;


    private static final int ACTION_NUM = 0;

    private DatabaseReference usersRef;

    private DatabaseReference mDatabaseRef;

    String online_user_id;

    private DatabaseReference friendsDatabaseRef;
    private DatabaseReference mDatabaseStoreItems;
    private DatabaseReference friendsRequestDataBRef;

    private DatabaseReference mDatabaseEventAttendance;


    private FloatingActionButton AdminRequest;
    private FloatingActionButton AdminRequest_live;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        FirebaseApp.initializeApp(this);


        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("@PAGiiS Notifications");

        //mAdView = findViewById(R.id.adView);

        /*AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");*/
        // TODO: Add adView to your view hierarchy.
        //LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //View actionBarView = layoutInflater.inflate(R.layout.custom_bar_notifications,null);


        //actionBar.setCustomView(actionBarView);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);


        friendRequestListRecV = findViewById(R.id.notificationRecyclerView);
        friendRequestListRecV.setHasFixedSize(true);
        friendRequestListRecV.setLayoutManager(new LinearLayoutManager(this));

        LiveRecyclerView= findViewById(R.id.notificationRecyclerViewInvites);
        LiveRecyclerView.setHasFixedSize(true);
        LiveRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        LiveRecyclerView.setEnabled(false);
        LiveRecyclerView.setVisibility(View.INVISIBLE);

        createEvent = findViewById(R.id.createEvent);


        //friendRequestListRecV.setLayoutManager(gridLayoutManager);

        AdminRequest = findViewById(R.id.adminRequest);
        AdminRequest_live = findViewById(R.id.adminRequest_live);

        AdminRequest_live.setVisibility(View.INVISIBLE);
        AdminRequest_live.setEnabled(false);




        tagedUsers = new ArrayList<>();
        mUploadsRequests = new ArrayList<>();
        radiusUserNotifications = new ArrayList<>();



        mDatabaseEventAttendance =FirebaseDatabase.getInstance().getReference("EventAttendance");

        //friendRequestListRecV.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new RequestAdaptor(ActivityNotifications.this, radiusUserNotifications);

        mapsAdapter = new LiveEventsAndRipplesAdaptor(ActivityNotifications.this,mUploadsRequests );

        mAdapter.setOnItemClickListener(ActivityNotifications.this);


        mapsAdapter.setOnItemClickListener(ActivityNotifications.this);


        friendRequestListRecV.setAdapter(mAdapter);


        LiveRecyclerView.setAdapter(mapsAdapter);


        mAuth = FirebaseAuth.getInstance();

        String online_user_id = mAuth.getCurrentUser().getUid();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgressCircle = findViewById(R.id.progress_circle_user_memes);

        createEvent.setVisibility(View.INVISIBLE);
        createEvent.setEnabled(false);

        String on_maps_visited_user_id = getIntent().getExtras().get("share_item_id").toString();


        friendsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        friendsRequestDataBRef = FirebaseDatabase.getInstance().getReference().child("Friend_Request");

        mDatabaseStoreItems = FirebaseDatabase.getInstance().getReference().child("EventUploadsAndRipples");

        friendRequestUserRef = FirebaseDatabase.getInstance().getReference().child("Friends_Requests").child(online_user_id);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);

        //friendRequestListRecV.setLayoutManager(linearLayoutManager);


        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("PagiisNotifications");

        AdminRequest.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.adminRequest);


                friendRequestListRecV.setVisibility(View.INVISIBLE);
                friendRequestListRecV.setEnabled(false);
                LiveRecyclerView.setVisibility(View.VISIBLE);
                LiveRecyclerView.setEnabled(true);


                AdminRequest.setEnabled(false);
                AdminRequest.setVisibility(View.INVISIBLE);


                AdminRequest_live.setEnabled(true);
                AdminRequest_live.setVisibility(View.VISIBLE);

            }
        });


        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.createEvent);
                Intent intent = new Intent(ActivityNotifications.this,CreateEvent.class);
                startActivity(intent);

            }
        });




        AdminRequest_live.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.adminRequest);


                friendRequestListRecV.setVisibility(View.VISIBLE);
                friendRequestListRecV.setEnabled(true);
                LiveRecyclerView.setVisibility(View.INVISIBLE);
                LiveRecyclerView.setEnabled(false);

                AdminRequest.setEnabled(true);
                AdminRequest.setVisibility(View.VISIBLE);

                AdminRequest_live.setEnabled(false);
                AdminRequest_live.setVisibility(View.INVISIBLE);


            }
        });

        if(on_maps_visited_user_id.compareTo("no") ==0)
        {

            getDataNormallyLive();
        }else if(on_maps_visited_user_id.compareTo("yes") ==0)
        {
            createEvent.setVisibility(View.VISIBLE);
            createEvent.setEnabled(true);
            getDataNormallyFromMemePage();
        }else
        {

            getDataNormally();
        }






    }

    private void getDataNormallyFromMemePage()
    {


        mDatabaseStoreItems = FirebaseDatabase.getInstance().getReference().child("EventUploadsAndRipples");
        mDatabaseStoreItems.orderByChild("status").equalTo("live").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists())
                {
                    mUploadsRequests.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        createEventFile upload = ds.getValue(createEventFile.class);
                        upload.setKey(ds.getKey());
                        mUploadsRequests.add(upload);
                    }

                    Collections.reverse(mUploadsRequests);
                    mapsAdapter.notifyDataSetChanged();
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

                Toast.makeText(ActivityNotifications.this, "someting went wrong  "+"(result)"+":"+e, Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }
        });

    }

    private void getDataNormally()

    {

        final String userIdRef = mAuth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("PagiisNotifications");
        mDatabaseEventAttendance =FirebaseDatabase.getInstance().getReference("EventAttendance");

        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists())
                {
                    radiusUserNotifications.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        createEventFile upload = ds.getValue(createEventFile.class);
                        upload.setKey(ds.getKey());
                        radiusUserNotifications.add(upload);

                    }

                    Collections.reverse(radiusUserNotifications);

                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }else
                {
                    paiisNotice();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ActivityNotifications.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

        });
    }

    private void paiisNotice()
    {

        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("PagiisNotifications");
        mDatabaseEventAttendance =FirebaseDatabase.getInstance().getReference("EventAttendance");

        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        mDatabaseRef_x.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists())
                {
                    radiusUserNotifications.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        createEventFile upload = ds.getValue(createEventFile.class);
                        upload.setKey(ds.getKey());
                        radiusUserNotifications.add(upload);

                    }


                    Collections.reverse(radiusUserNotifications);

                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ActivityNotifications.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

        });
    }


    private void getDataNormallyLive()
    {
        mDatabaseStoreItems = FirebaseDatabase.getInstance().getReference().child("EventUploadsAndRipples");
        mDatabaseStoreItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists())
                {
                    mUploadsRequests.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        createEventFile upload = ds.getValue(createEventFile.class);
                        upload.setKey(ds.getKey());
                        mUploadsRequests.add(upload);
                    }

                    Collections.reverse(mUploadsRequests);
                    mapsAdapter.notifyDataSetChanged();
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

                Toast.makeText(ActivityNotifications.this, "someting went wrong  "+"(result)"+":"+e, Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }
        });
    }



    @Override
    public void onItemClick(int position)

    {
        createEventFile selectedImage = radiusUserNotifications.get(position);

        String selectedKey = selectedImage.getKey();

        String imageUrl = selectedImage.getImageUrl();

        MaxImageUrl = imageUrl;

        String maxImageUserId = selectedImage.getUserId();
        Intent intent = new Intent(ActivityNotifications.this, RequestMaxView.class);
        intent.putExtra("visit_user_id", selectedKey);
        startActivity(intent);
    }

    @Override
    public void onClick(int position)
    {
        createEventFile selectedImage = mUploadsRequests.get(position);

        String selectedKey = selectedImage.getKey();

        String imageUrl = selectedImage.getImageUrl();

        MaxImageUrl = imageUrl;


        String maxImageUserId = selectedImage.getUserId();

        Intent intent = new Intent(ActivityNotifications.this, ViewEvent.class);
        intent.putExtra("visit_user_id", selectedKey);
        startActivity(intent);

    }

    @Override
    public void onWhatEverClick(int position)
    {

        mDatabaseStoreItems = FirebaseDatabase.getInstance().getReference().child("EventUploadsAndRipples");

        mDatabaseEventAttendance =FirebaseDatabase.getInstance().getReference("EventAttendance");

        final createEventFile selectedImage = radiusUserNotifications.get(position);

        final String selectedKey = selectedImage.getKey();

        final  String user_id = mAuth.getCurrentUser().getUid();

        String getUserRef = selectedImage.getUserId();
        mDatabaseStoreItems.child("EventUploadsAndRipples").child(user_id).child(selectedKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists())
                {
                    mDatabaseStoreItems.child("share").setValue("YES").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {


                            mDatabaseEventAttendance.child(selectedKey).child(user_id).child("admin").setValue("false");

                            mDatabaseEventAttendance.child(selectedKey).child("attendin").child(user_id).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {

                                    Intent intent = new Intent(ActivityNotifications.this,ViewEvent.class);
                                    //putValue ere user id
                                    intent.putExtra("visit_user_id",selectedKey);
                                    startActivity(intent);

                                }
                            });


                        }
                    });

                }else
                {
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                String e = databaseError.getMessage();

                Toast.makeText(ActivityNotifications.this, "Error occured " +"_result"+":"+e, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void shareClick(int position)
    {

        final createEventFile selectedImage = radiusUserNotifications.get(position);

        final String selectedKey = selectedImage.getKey();

        String getUserRef = selectedImage.getUserId();
        mDatabaseStoreItems.child("EventUploadsAndRipples").child(selectedKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                final   String user_id = mAuth.getCurrentUser().getUid();

                if(dataSnapshot.exists())
                {
                    mDatabaseStoreItems.child("share").setValue("NO").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                            Intent intent = new Intent(ActivityNotifications.this,ActivityNotifications.class);
                            //putValue ere user id

                            intent.putExtra("user_id",user_id);
                            startActivity(intent);

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                String e = databaseError.getMessage();

                Toast.makeText(ActivityNotifications.this, "request off" + "results"+":"+e, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void chatsClick(int position)
    {

        final createEventFile selectedImage = radiusUserNotifications.get(position);

        final String selectedKey = selectedImage.getKey();

        String getUserRef = selectedImage.getUserId();

        if(!getUserRef.isEmpty())
        {
            Intent intent = new Intent(ActivityNotifications.this, UserProfileActivity.class);
            intent.putExtra("visit_user_id", getUserRef);
            startActivity(intent);
        }else
        {
            Toast.makeText(this, "Pagiis failed to load user profile. please try again", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void onDeleteClick(int position)
    {
        createEventFile selectedImage = radiusUserNotifications.get(position);

        String selectedKey = selectedImage.getKey();

        String imageUrl = selectedImage.getImageUrl();

        String maxImageUserId = selectedImage.getUserId();

        if(!imageUrl.isEmpty() && !selectedKey.isEmpty())
        {
            Intent intent = new Intent(ActivityNotifications.this,ViewUsersMemes.class);
            startActivity(intent);
            finish();

        }else
        {
            Toast.makeText(this, "Press and hold for More options...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }
}


