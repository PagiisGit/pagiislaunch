package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import pagiisnet.pagiisnet.Utils.ProfilePicsAdapter;
import pagiisnet.pagiisnet.Utils.viewMyLinksAdaptor;

public class MemePage extends AppCompatActivity implements  ProfilePicsAdapter.OnItemClickListener, viewMyLinksAdaptor.OnItemClickListener{

    private RecyclerView mRecyclerView;

    private ProfilePicsAdapter mAdapter;
    private viewMyLinksAdaptor mAdapterLink;

    private ImageView userImageDp;

    private TextView userName;

    private int adapterPosition;

    private String ProfileUserName;

    private DatabaseReference userProfileInfor;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private ProgressBar mProgressCircle;

    private ImageView UploadMemeButton;

    private FirebaseAuth mAuth;

    private FirebaseStorage mStorage;

    private  String DeleteKeyAlternative;

    private FloatingActionButton videosButton;

    private DatabaseReference mDatabaseRef;

    private ValueEventListener mDBlistener;

    private List<ImageUploads> mUploads;

    private  FloatingActionButton liveEventsNotification;
    private  FloatingActionButton linkPostsView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_page);
        mRecyclerView = findViewById(R.id.memeRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        FirebaseApp.initializeApp(getApplicationContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();


        String User_id = mAuth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        userProfileInfor = FirebaseDatabase.getInstance().getReference("Users").child(User_id);

        userProfileInfor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String pprofileUsername = dataSnapshot.child("userNameAsEmail").getValue().toString();
                ProfileUserName = pprofileUsername;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(mAuth.getCurrentUser() == null)
        {
            mAuth.signOut();
            Intent intent = new Intent(MemePage.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("PAGiiS");

        mStorage = FirebaseStorage.getInstance();

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mAdapter = new ProfilePicsAdapter(MemePage.this, mUploads);
        mAdapterLink = new viewMyLinksAdaptor(MemePage.this, mUploads);

        mAdapter.setOnItemClickListener(MemePage.this);

        mAdapterLink.setOnItemClickListener(MemePage.this);

        mRecyclerView.setAdapter(mAdapter);

        userImageDp = findViewById(R.id.ImageDP);

        UploadMemeButton = findViewById(R.id.addContent);

        videosButton = findViewById(R.id.goToVideos);

        liveEventsNotification = findViewById(R.id.goToEvents);

        linkPostsView = findViewById(R.id.contentRatingLnk);

        videosButton.setVisibility(View.VISIBLE);

        videosButton.setEnabled(true);

        userName = findViewById(R.id.profileName);

        final String user_id = mAuth.getCurrentUser().getUid();

        videosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.goToVideos);
                Intent intent = new Intent(MemePage.this, VideowViewing.class);
                startActivity(intent);
            }
        });


        linkPostsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mUploads.clear();
                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.VISIBLE);
                getDataNormallyLink();
            }
        });


        liveEventsNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.goToEvents);

                Intent intent = new Intent(MemePage.this,ActivityNotifications.class);
                intent.putExtra("share_item_id","yes");
                startActivity(intent);

            }
        });


        UploadMemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                        view.findViewById(R.id.addContent);

                        Intent intent = new Intent(MemePage.this,ActivityUploadImage.class);
                        intent.putExtra("share_item_id","null");
                        startActivity(intent);


            }
        });



        mDBlistener = mDatabaseRef.child(user_id).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    mUploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        mUploads.add(upload);
                    }

                    mAdapter.notifyDataSetChanged();
                    UploadMemeButton.setVisibility(View.INVISIBLE);
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }else
                {
                    Toast.makeText(MemePage.this, "Items not found", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MemePage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });



    }



    private void getDataNormallyLink() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploadsLink");
        final String userIdRef = mAuth.getCurrentUser().getUid();
        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        mDBlistener = mDatabaseRef.child(userIdRef).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                mAdapterLink.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MemePage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uploads_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add)
        {
            Intent intent = new Intent(MemePage.this, ActivityUploadImage.class);
            intent.putExtra("share_item_id","null");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBlistener);
    }

    @Override
    public void onItemClick(int position) {

        ImageUploads selectedImage = mUploads.get(position);

        String selectedKey = selectedImage.getKey();

        String imageUrl = selectedImage.getImageUrl();

        String maxImageUserId = selectedImage.getUserId();

        if(!imageUrl.isEmpty() && !selectedKey.isEmpty())
        {
            Intent intent = new Intent(MemePage.this,PagiisMaxView.class);
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

        Intent intent = new Intent(MemePage.this, ActivityOwnProfile.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position)
    {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        DeleteKeyAlternative = selectedKey;

        String myUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(myUserId);

        //StorageReference deletImage = mStorage.getReferenceFromUrl(selectedImage.getImageUrl());


        mDatabaseRef.child(selectedKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(MemePage.this, "Item sent to bin..", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {


            String errorText;
            @Override
            public void onFailure(@NonNull Exception e)
            {

                errorText = String.valueOf(e);
                Toast.makeText(MemePage.this, errorText + " Deletion Will restart in few seconds", Toast.LENGTH_LONG).show();
                deleteAlternative();


            }
        });


        /*if(deletImage != null)

        {
            deletImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                @Override
                public void onSuccess(Void aVoid) {

                    //mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
                    mDatabaseRef.child(selectedKey).removeValue();
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(MemePage.this, " PAGiiS failed to delete image. try again..", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }else
        {
            Toast.makeText(this, "Pagiis could not locate image in database.", Toast.LENGTH_SHORT).show();
            finish();
        }*/
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
                                Toast.makeText(MemePage.this, "Item sent to bin..", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                DeleteFinalString = String.valueOf(e);
                                Toast.makeText(MemePage.this, DeleteFinalString + "PAGiiS Failed to Delete File, please try again :Later", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(MemePage.this, "item delete failed.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }

    @Override
    public void onItemClickLink(int position)
    {

    }

    @Override
    public void onWhatEverClickLink(int position)
    {

    }

    @Override
    public void onDeleteClickLink(int position) {

    }
}
