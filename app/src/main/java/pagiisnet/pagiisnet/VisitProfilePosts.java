package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import pagiisnet.pagiisnet.R;

public class VisitProfilePosts extends AppCompatActivity implements  ViewProfilePicsAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;

    private RecyclerView searchRecyclerView;
    private ViewProfilePicsAdapter mAdapter;
    private ViewProfilePicsAdapter sAdapter;

    private ProgressBar mProgressCircle;
    //private DatabaseReference userRef;


    private EditText mSearchEditText;
    private ImageView searchForResults;
    private ImageView openSearchbar;
    private LinearLayout searchInputLayout;

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

    private FloatingActionButton viewUserVideos;

    private Iterator<tags> iterator;

    private List<tags> tagedUsers;

    private List<lockedImagesTag> tagedImages;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private final int raterBarValue = 0;


    @SuppressLint({"RestrictedApi", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_profile_posts);
        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("-PAGiiS-");

        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance();

        if (mAuth.getCurrentUser() == null) {
            mAuth.signOut();
            Intent intent = new Intent(VisitProfilePosts.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mSearchEditText = findViewById(R.id.searchEdittext);
        searchForResults = findViewById(R.id.LogSearchIconGo);
        openSearchbar = findViewById(R.id.logSearchIcon);

        searchInputLayout = findViewById(R.id.searchTextInputLayout);

        searchInputLayout.setVisibility(View.INVISIBLE);
        searchInputLayout.setEnabled(false);


        mRecyclerView = findViewById(R.id.memeRecyclerView);
        searchRecyclerView = findViewById(R.id.SearchRecyclerViewTop);
        searchRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchRecyclerView.setVisibility(View.INVISIBLE);
        searchRecyclerView.setEnabled(false);

        //returnToMaps = findViewById(R.id.returnToMaps);

        mProgressCircle = findViewById(R.id.progress_circle_user_memes);

        mProgressCircle.setVisibility(View.VISIBLE);

        mUploads = new ArrayList<>();

        tagedUsers = new ArrayList<>();
        tagedImages = new ArrayList<>();

        mAdapter = new ViewProfilePicsAdapter(VisitProfilePosts.this, mUploads);
        sAdapter = new ViewProfilePicsAdapter(VisitProfilePosts.this, mUploads);

        mAdapter.setOnItemClickListener(VisitProfilePosts.this);
        sAdapter.setOnItemClickListener(VisitProfilePosts.this);


        mRecyclerView.setAdapter(mAdapter);
        searchRecyclerView.setAdapter(sAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");

        lockedImages = FirebaseDatabase.getInstance().getReference("Likes");

        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("LockedImages");

        contentRater = findViewById(R.id.contentRating);
        viewUserVideos = findViewById(R.id.goToVideos);
        viewUserVideos.setVisibility(View.INVISIBLE);
        viewUserVideos.setEnabled(false);

        contentRater.setVisibility(View.VISIBLE);

        contentRater.setEnabled(true);

        /*&viewUserVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.goToVideos);
                Intent intent = new Intent(ViewUsersMemes.this, ViewUsersVideos.class);
                startActivity(intent);

            }
        });*/

        openSearchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                getSearchedDataNormally(searchedText);

            }
        });


        contentRater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.findViewById(R.id.contentRating);
                if (tagedUsers.isEmpty()) {
                    updateArrayList();

                    searchInputLayout.setVisibility(View.INVISIBLE);
                    searchInputLayout.setEnabled(false);
                    searchRecyclerView.setVisibility(View.INVISIBLE);
                    searchRecyclerView.setEnabled(false);

                } else {
                    searchInputLayout.setVisibility(View.INVISIBLE);
                    searchInputLayout.setEnabled(false);
                    searchRecyclerView.setVisibility(View.INVISIBLE);
                    searchRecyclerView.setEnabled(false);
                    getDataNormally();
                }

            }
        });

        //userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        if (tagedUsers.isEmpty()) {
            updateArrayList();

        } else {
            getDataNormally();
        }
    }

    private void getSearchedDataNormally(String searchedtext) {

        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        if (!tagedUsers.isEmpty()) {

            for (int i = 0; i < tagedUsers.size(); i++) {
                tags x = tagedUsers.get(i);
                final String taged_id = x.getUser_tagID();

                mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(taged_id);
                Query mSearchQuery = mDatabaseRef.orderByChild("name").startAt(searchedtext).endAt(searchedtext + "\uf8ff");
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
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(VisitProfilePosts.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgressCircle.setVisibility(View.INVISIBLE);
                    }
                });
            }
        } else {
            Toast.makeText(this, "Pagiis online posts currently unavailable.", Toast.LENGTH_SHORT).show();

        }

    }

    private void updateArrayListImages()

    {
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
                            Intent catchup = new Intent(VisitProfilePosts.this, CatchUp.class);
                            startActivity(catchup);
                        }
                    }
                } else {
                    Toast.makeText(VisitProfilePosts.this, "Tags not found.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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

                            getDataNormally();

                            //getDataNormally();

                            //0799506310

                        } else {

                            Toast.makeText(VisitProfilePosts.this, "Online posts currently unavailable.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                } else {
                    Toast.makeText(VisitProfilePosts.this, "Pagiis online users currently unavailable.", Toast.LENGTH_SHORT).show();

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

        String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(on_maps_visited_user_id);
        final String userIdRef = mAuth.getCurrentUser().getUid();
        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    mUploads.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ImageUploads upload = ds.getValue(ImageUploads.class);
                        upload.setKey(ds.getKey());
                        mUploads.add(upload);
                    }
                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);

                }else
                {
                    Toast.makeText(VisitProfilePosts.this, "PAGE empty.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VisitProfilePosts.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


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
                        upload.setKey(String.valueOf(dataSnapshot.getKey()));
                        mUploads.add(upload);

                    }else
                    {
                        Toast.makeText(VisitProfilePosts.this, "Pagiis failed to read database. please retry action", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);

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
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override

            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(VisitProfilePosts.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
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
            Intent intent = new Intent(VisitProfilePosts.this, SiroccoPage.class);
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

        String imageUrl = selectedImage.getImageUrl();

        String userKeyId = selectedImage.getUserId();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(userKeyId).child(selectedKey);

        lockedImages = FirebaseDatabase.getInstance().getReference("Likes").child(selectedKey);
        mDatabaseRef_z = FirebaseDatabase.getInstance().getReference("Views").child(selectedKey);

        if(!imageUrl.isEmpty() && !selectedKey.isEmpty())
        {



            lockedImages.addValueEventListener(new ValueEventListener() {

                String UnitLikes;

                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot)
                {
                    lockedImages.child(own_user_id).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                            UnitLikes = String.valueOf(Objects.requireNonNull(dataSnapshot).getChildrenCount());

                            mDatabaseRef.child("likes").setValue(UnitLikes);
                            mDatabaseRef.child("views").setValue(UnitLikes);


                            //mDatabaseRef.child("likes").setValue(String.valueOf(dataSnapshot.getChildrenCount()));
                            //mDatabaseRef.child("views").setValue(String.valueOf(dataSnapshot.getChildrenCount()));

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

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

            Toast.makeText(this, "Online user post, press and hold to view Profile.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(VisitProfilePosts.this ,PagiisMaxView.class);
            intent.putExtra("imageKeyMAx", selectedKey);
            intent.putExtra("imageUrlMax",imageUrl);
            intent.putExtra("imageUserId", userKeyId);
            startActivity(intent);
        }
    }

    @Override
    public void onWhatEverClick(int position)
    {
        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String getUserRef = selectedImage.getUserId();

        if(!getUserRef.isEmpty() & !selectedKey.isEmpty())
        {
            Intent intent = new Intent(VisitProfilePosts.this,UserProfileActivity.class);
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
            Intent intent = new Intent(VisitProfilePosts.this,ActivityUploadImage.class);
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

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(VisitProfilePosts.this);
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
