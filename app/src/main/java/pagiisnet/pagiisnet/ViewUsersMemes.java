package pagiisnet.pagiisnet;

import static android.view.View.INVISIBLE;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import pagiisnet.pagiisnet.Utils.ViewProfilePicsAdapter;
import pagiisnet.pagiisnet.Utils.linksViewAdapter;
import pagiisnet.pagiisnet.Utils.mapSearchedItemAdaptor;

public class ViewUsersMemes extends AppCompatActivity implements  ViewProfilePicsAdapter.OnItemClickListener,mapSearchedItemAdaptor.OnItemClickListener,linksViewAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView searchRecyclerView;
    private ViewProfilePicsAdapter mAdapter;

    private ViewProfilePicsAdapter mAdapterLink;
    private mapSearchedItemAdaptor sAdapter;

    private ProgressBar mProgressCircle;
    //private DatabaseReference userRef;

    String myName;

    String userStatusMessage;

    String myImageDpUrl;

    private DatabaseReference getUserProfileDataRef;


    private EditText mSearchEditText;
    private ImageView searchForResults;
    private ImageView openSearchbar;
    private ImageView uploadItem;
    private CardView searchInputLayout;

    private  TextView searchNotice;

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


    private FloatingActionButton contentRater;
    private FloatingActionButton contentRaterLink;

    private FloatingActionButton viewUserVideos;

    private Iterator<tags> iterator;

    private List<tags> tagedUsers;

    private List<lockedImagesTag> tagedImages;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private final int raterBarValue = 0;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users_memes);

        FirebaseApp.initializeApp(getApplicationContext());

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("live");


        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
        getUserProfileDataRef.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance();

        if(mAuth.getCurrentUser() == null)
        {
            mAuth.signOut();
            Intent intent = new Intent(ViewUsersMemes.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mSearchEditText = findViewById(R.id.searchEdittext);
        searchForResults = findViewById(R.id.LogSearchIconGo);
        openSearchbar = findViewById(R.id.logSearchIcon);

        uploadItem = findViewById(R.id.businessId);

        searchInputLayout = findViewById(R.id.searchTextInputLayout);

        searchInputLayout.setVisibility(INVISIBLE);
        searchInputLayout.setEnabled(false);

        searchNotice = findViewById(R.id.SearcNotice);
        searchNotice.setVisibility(INVISIBLE);
        searchNotice.setEnabled(false);


        mRecyclerView = findViewById(R.id.memeRecyclerView);
        searchRecyclerView = findViewById(R.id.SearchRecyclerViewTop);
        searchRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchRecyclerView.setVisibility(INVISIBLE);
        searchRecyclerView.setEnabled(false);

        //returnToMaps = findViewById(R.id.returnToMaps);

        mProgressCircle = findViewById(R.id.progress_circle_user_memes);

        mProgressCircle.setVisibility(View.VISIBLE);

        mUploads = new ArrayList<>();

        tagedUsers= new ArrayList<>();
        tagedImages= new ArrayList<>();

        mAdapter = new ViewProfilePicsAdapter(ViewUsersMemes.this, mUploads);
        mAdapterLink = new ViewProfilePicsAdapter(ViewUsersMemes.this, mUploads);
        sAdapter = new mapSearchedItemAdaptor(ViewUsersMemes.this, mUploads);

        mAdapter.setOnItemClickListener(ViewUsersMemes.this);
        sAdapter.setOnItemClickListener(ViewUsersMemes.this);


        mRecyclerView.setAdapter(mAdapter);

        searchRecyclerView.setAdapter(sAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");

        lockedImages = FirebaseDatabase.getInstance().getReference("Likes");

        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("LockedImages");

        contentRater = findViewById(R.id.contentRating);
        contentRaterLink = findViewById(R.id.contentRatingLnk);
        viewUserVideos = findViewById(R.id.goToVideos);
        viewUserVideos.setVisibility(View.VISIBLE);
        viewUserVideos.setEnabled(true);

        contentRater.setVisibility(INVISIBLE);

        contentRater.setEnabled(false);

        contentRaterLink.setVisibility(View.VISIBLE);

        contentRaterLink.setEnabled(true);

        viewUserVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.goToVideos);
                Intent intent = new Intent(ViewUsersMemes.this, ViewUsersVideos.class);
                startActivity(intent);

            }
        });

        viewUserVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.businessId);
                Intent intent = new Intent(ViewUsersMemes.this, ActivityUploadImage.class);
                intent.putExtra("share_item_id","null");
                startActivity(intent);

            }
        });


        String onlineUserId = mAuth.getCurrentUser().getUid();


        getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("userNameAsEmail").getValue().toString();
                String userStatus = dataSnapshot.child("userDefaultStatus").getValue().toString();

                String myDpUrl = dataSnapshot.child("userImageDp").getValue().toString();
                userStatusMessage  = userStatus;
                myImageDpUrl =myDpUrl;
                myName = name;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        openSearchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.logSearchIcon);
                searchInputLayout.setVisibility(View.VISIBLE);
                searchInputLayout.setEnabled(true);

            }
        });


        searchForResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.LogSearchIconGo);

                String searchedText = mSearchEditText.getText().toString();

                if(!tagedUsers.isEmpty())
                {

                    for (int i = 0; i < tagedUsers.size(); i++)
                    {
                        tags x = tagedUsers.get(i);
                        final String taged_id = x.getUser_tagID();

                        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(taged_id);

                        Query mSearchQuery = mDatabaseRef.orderByChild("name").startAt(searchedText).endAt(searchedText + "\uf8ff");
                        final String userIdRef = mAuth.getCurrentUser().getUid();

                        mSearchQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists())
                                {
                                    mUploads.clear();
                                    for (DataSnapshot ds : dataSnapshot.getChildren())
                                    {
                                        ImageUploads upload = ds.getValue(ImageUploads.class);
                                        upload.setKey(ds.getKey());
                                        mUploads.add(upload);
                                    }

                                    searchRecyclerView.setVisibility(View.VISIBLE);
                                    searchRecyclerView.setEnabled(true);
                                    sAdapter.notifyDataSetChanged();
                                    mProgressCircle.setVisibility(INVISIBLE);
                                }else
                                {
                                    searchNotice.setVisibility(View.VISIBLE);
                                    searchNotice.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(ViewUsersMemes.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                mProgressCircle.setVisibility(INVISIBLE);
                            }
                        });
                    }
                }else
                {

                    Toast.makeText(ViewUsersMemes.this, "Pagiis online posts currently unavailable.", Toast.LENGTH_SHORT).show();

                }

            }
        });


        contentRater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.contentRating);
                if(tagedUsers.isEmpty())
                {
                    updateArrayList();

                    searchInputLayout.setVisibility(INVISIBLE);
                    searchInputLayout.setEnabled(false);
                    searchRecyclerView.setVisibility(INVISIBLE);
                    searchRecyclerView.setEnabled(false);


                    searchNotice.setVisibility(INVISIBLE);
                    searchNotice.setEnabled(false);

                }else if(!tagedUsers.isEmpty())
                {
                    searchInputLayout.setVisibility(INVISIBLE);
                    searchInputLayout.setEnabled(false);
                    searchRecyclerView.setVisibility(INVISIBLE);
                    searchRecyclerView.setEnabled(false);

                    searchNotice.setVisibility(INVISIBLE);
                    searchNotice.setEnabled(false);
                    getDataNormally();
                }

            }
        });

        contentRaterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.contentRatingLnk);
                if(!tagedUsers.isEmpty())
                {
                    mUploads.clear();
                    searchInputLayout.setVisibility(INVISIBLE);
                    searchInputLayout.setEnabled(false);
                    searchRecyclerView.setVisibility(INVISIBLE);
                    searchRecyclerView.setEnabled(false);

                    searchNotice.setVisibility(INVISIBLE);
                    searchNotice.setEnabled(false);
                    mAdapter.notifyDataSetChanged();
                    getDataNormallyLink();
                }

            }
        });

        searchNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.SearcNotice);

                searchNotice.setVisibility(INVISIBLE);
                searchNotice.setEnabled(false);
                searchRecyclerView.setVisibility(INVISIBLE);


            }
        });

        //userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        if(tagedUsers.isEmpty() )
        {
            updateArrayList();

        }else
        {
            getDataNormally();
        }
    }


    private void updateArrayListImages()

    {
        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("LockedImages");

        String currentUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef_y.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        String localIDKey = ds.getKey();

                        String localIDKeyx = ds.child("tag_userImage_id").getValue().toString();

                        tagedImages.add(new lockedImagesTag(localIDKey,localIDKeyx));

                        if(dataSnapshot.getChildrenCount() >=1 && tagedImages.size()>=1 | dataSnapshot.getChildrenCount()== tagedImages.size() )
                        {
                            Intent catchup = new Intent(ViewUsersMemes.this, CatchUp.class);
                            startActivity(catchup);
                        }
                    }
                }else
                {
                    Toast.makeText(ViewUsersMemes.this, "Tags not found.", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }

    private void updateArrayList()

    {
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");

        String currentUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef_x.child(currentUserId).limitToFirst(50).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        String localID = ds.getKey();

                        tagedUsers.add(new tags(localID));

                        if(dataSnapshot.getChildrenCount() >=1 && tagedUsers.size()>=1 | dataSnapshot.getChildrenCount()==tagedUsers.size())
                        {

                            contentRater.setVisibility(View.INVISIBLE);

                            contentRater.setEnabled(false);

                            contentRaterLink.setVisibility(View.VISIBLE);

                            contentRaterLink.setEnabled(true);

                                getDataNormally();

                            //getDataNormally();

                            //0799506310

                        }else
                        {

                            Toast.makeText(ViewUsersMemes.this, "Online posts currently unavailable.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                }else
                {
                    Toast.makeText(ViewUsersMemes.this, "Pagiis online users currently unavailable.", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);

                TextView messageGrid = findViewById(R.id.customToastText);


                // Initiate the Toast instance.
                Toast toast = new Toast(getApplicationContext());

                messageGrid.setText("Pagiis online user update failed, Please waite while pagiis restarts activity");
                // Set custom view in toast.
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                //Toast.makeText(MapsActivity.this, "Button switched on.", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }

    private void getDataNormally()
    {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        final String userIdRef = mAuth.getCurrentUser().getUid();
        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        if(!tagedUsers.isEmpty())
        {
            for (int i = 0; i < tagedUsers.size(); i++)
            {

                tags x = tagedUsers.get(i);

                final String taged_id = x.getUser_tagID();

                mDatabaseRef.child(taged_id).limitToLast(50).addValueEventListener(new ValueEventListener() {
                    @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            mUploads.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                ImageUploads upload = ds.getValue(ImageUploads.class);
                                upload.setKey(ds.getKey());
                                mUploads.add(upload);
                            }

                            uploadItem.setVisibility(View.INVISIBLE);
                            uploadItem.setEnabled(false);
                            mAdapter.notifyDataSetChanged();
                            mProgressCircle.setVisibility(INVISIBLE);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ViewUsersMemes.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgressCircle.setVisibility(INVISIBLE);
                    }
                });
            }
        }else
        {
            Toast.makeText(this, "Pagiis online posts currently unavailable.", Toast.LENGTH_SHORT).show();

        }

    }



    private void getDataNormallyLink()
    {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploadsLink");
        final String userIdRef = mAuth.getCurrentUser().getUid();
        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        if(!tagedUsers.isEmpty())
        {
            for (int i = 0; i < tagedUsers.size(); i++)
            {

                tags x = tagedUsers.get(i);

                final String taged_id = x.getUser_tagID();

                mDatabaseRef.child(taged_id).limitToLast(50).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            mUploads.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                ImageUploads upload = ds.getValue(ImageUploads.class);
                                upload.setKey(ds.getKey());
                                mUploads.add(upload);
                            }
                            contentRaterLink.setVisibility(INVISIBLE);
                            contentRaterLink.setEnabled(false);

                            contentRater.setVisibility(View.VISIBLE);
                            contentRater.setEnabled(true);

                            mAdapterLink.notifyDataSetChanged();
                            mProgressCircle.setVisibility(INVISIBLE);

                        }else{
                            Toast.makeText(ViewUsersMemes.this, "Link posts not found.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ViewUsersMemes.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgressCircle.setVisibility(INVISIBLE);
                    }
                });
            }
        }else
        {
            Toast.makeText(this, "Pagiis online posts currently unavailable.", Toast.LENGTH_SHORT).show();

        }

    }

    private void getDataSnapShot() {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(getKey);

        if(!getKey.isEmpty()) {

            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {

                        ImageUploads upload = dataSnapshot.getValue(ImageUploads.class);
                        upload.setKey(valueOf(dataSnapshot.getKey()));
                        mUploads.add(upload);

                    }else
                    {
                        Toast.makeText(ViewUsersMemes.this, "Pagiis failed to read database. please retry action", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(INVISIBLE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else
        {
            Toast.makeText(this, "Pagiis failed to retrieve data. ", Toast.LENGTH_SHORT).show();
        }
    }
    private void getRestrictedData()

    {


        final String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();
         mDatabaseRef.child(on_maps_visited_user_id).orderByChild("Age").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())

                {

                    ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(INVISIBLE);
            }

            @Override

            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(ViewUsersMemes.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(INVISIBLE);
            }
        });
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
            Intent intent = new Intent(ViewUsersMemes.this, SiroccoPage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position)
    {
         ImageUploads selectedImage = mUploads.get(position);

         final String selectedKey = selectedImage.getKey();

         final String own_user_id = mAuth.getCurrentUser().getUid();

         final String imageUrl = selectedImage.getImageUrl();

         final String userKeyId = selectedImage.getUserId();

         mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(userKeyId).child(selectedKey);

         lockedImages = FirebaseDatabase.getInstance().getReference("Likes").child(selectedKey).child(own_user_id);
         mDatabaseRef_z = FirebaseDatabase.getInstance().getReference("Views").child(selectedKey);

        if(!imageUrl.isEmpty() && !selectedKey.isEmpty())
        {

            lockedImages.addValueEventListener(new ValueEventListener() {

                String UnitLikes;

                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot)
                {

                    //String name, String imageUrl, String rateEx, String userId,String views,String likes, String share

                    lockedImages.child("name").setValue(myName);
                    lockedImages.child("imageUrl").setValue(imageUrl);
                    lockedImages.child("rateEx").setValue(userStatusMessage);
                    lockedImages.child("userId").setValue(own_user_id);
                    lockedImages.child("views").setValue("1");
                    lockedImages.child("likes").setValue("1");
                    lockedImages.child("share").setValue(myImageDpUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @SuppressLint("SuspiciousIndentation")
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                            UnitLikes = valueOf(Objects.requireNonNull(dataSnapshot).getChildrenCount()+1);

                                mDatabaseRef.child("likes").setValue(UnitLikes);
                                mDatabaseRef.child("views").setValue(UnitLikes);
                                lockedImages.child("userId").setValue(UnitLikes);
                                lockedImages.child("views").setValue(UnitLikes).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {


                                        Intent intent = new Intent(ViewUsersMemes.this ,PagiisMaxView.class);
                                        intent.putExtra("imageKeyMAx", selectedKey);
                                        intent.putExtra("imageUrlMax",imageUrl);
                                        intent.putExtra("imageUserId", userKeyId);
                                        startActivity(intent);

                                    }
                                });

                                //mDatabaseRef.child("likes").setValue(String.valueOf(dataSnapshot.getChildrenCount()));
                                //mDatabaseRef.child("views").setValue(String.valueOf(dataSnapshot.getChildrenCount()));

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Toast.makeText(ViewUsersMemes.this, "Click to like item.", Toast.LENGTH_SHORT).show();
                }
            });


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


        }
    }

    @Override
    public void onNItemClick(int position)
    {
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

    @Override
    public void onClickLink(int position)
    {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String exRater = selectedImage.getExRating();


        final String imageUrl = selectedImage.getImageUrl();


        if(imageUrl != null &&  !imageUrl.isEmpty()  && imageUrl.compareTo("null")!= 0 && Patterns.WEB_URL.matcher(imageUrl).matches() )
        {
            Intent intent = new Intent(ViewUsersMemes.this, LinkView.class);
            intent.putExtra("visit_user_id",imageUrl);
            startActivity(intent);

        }else
        {
            Toast.makeText(ViewUsersMemes.this, "User not linked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWhatEverClickLink(int position)
    {

    }

    @Override
    public void shareClickLink(int position)
    {


        ImageUploads selectedImage = mUploads.get(position);

        String selectedKey = selectedImage.getKey();

        String getUserRef = selectedImage.getUserId();

        String imageUrl = selectedImage.getImageUrl();

        if(!getUserRef.isEmpty() && !selectedKey.isEmpty())
        {
            Intent intent = new Intent(ViewUsersMemes.this,LinkPost.class);
            intent.putExtra("share_item_id", getUserRef);
            intent.putExtra("share_item_id_key", selectedKey);
            startActivity(intent);
        }else
        {
            Toast.makeText(this, "Pagiis failed to delete file. the system will retry deleting file later ", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @Override
    public void chatsClickLink(int position) {

    }

    @Override
    public void onWhatEverClick(int position)
    {
        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String getUserRef = selectedImage.getUserId();

        if(!getUserRef.isEmpty() & !selectedKey.isEmpty())
        {
            Intent intent = new Intent(ViewUsersMemes.this,ActivityOwnProfile.class);
            intent.putExtra("visit_user_id", getUserRef);
            startActivity(intent);
        }else
        {
            Toast.makeText(this, "Pagiis failed to delete file. the system will retry deleting file later ", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void shareClick(int position)
    {

         ImageUploads selectedImage = mUploads.get(position);

         String selectedKey = selectedImage.getKey();

         String getUserRef = selectedImage.getUserId();

         String imageUrl = selectedImage.getImageUrl();

        if(!getUserRef.isEmpty() && !selectedKey.isEmpty())
        {
            Intent intent = new Intent(ViewUsersMemes.this,ActivityUploadImage.class);
            intent.putExtra("share_item_id", imageUrl);
            startActivity(intent);
        }else
        {
            Toast.makeText(this, "Pagiis failed to delete file. the system will retry deleting file later ", Toast.LENGTH_SHORT).show();
            finish();
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
    public void chatsClick(int position)
    {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String getUserRef = selectedImage.getUserId();

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewUsersMemes.this);
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
                Intent intent = new Intent(getApplicationContext(), ViewUsersMemes.class);
                startActivity(intent);
                finish();

            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    @Override
    protected void onStop()

    {
        super.onStop();
        tagedUsers.clear();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateArrayList();
    }
}
