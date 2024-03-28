package pagiisnet.pagiisnet;

import static android.view.View.INVISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import pagiisnet.pagiisnet.Utils.UserVideoViewAdapter;

public class ViewUsersVideos extends AppCompatActivity implements  UserVideoViewAdapter.OnItemClickListener{


    private RecyclerView mRecyclerView;
    private UserVideoViewAdapter mAdapter;

    private ProgressBar mProgressCircle;
    private ProgressBar mProgressCirclePlay;


    //private DatabaseReference userRef;

    private FirebaseAuth mAuth;

    private FirebaseStorage mStorage;

    private  enum  VolumeState {ON,OFF}

    private  String myLastLocationDetails;

    private VolumeState volumeState;

    private String getKey;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_x;
    private DatabaseReference mDatabaseRef_y;
    private DatabaseReference lockedImages;
    private ValueEventListener mDBlistener;

    private List<ImageUploads> mUploads;

    private DatabaseReference getUserProfileDataRef;

    private Iterator<tags> iterator;


    private List<tags> tagedUsers;



    private VideoView videoPlay;

    private  MediaPlayer Mp;

    private ImageView closeVideoPlay;

    private CardView videoPlayCard;

    private int stopPosition;

    private List<pagiisnet.pagiisnet.lockedImagesTag> tagedImages;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private final int raterBarValue = 0;

    private FloatingActionButton viewMyVideos;


    String myName;

    String userStatusMessage;

    String myImageDpUrl;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users_videos);
        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Video Posts");
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        videoPlay = findViewById(R.id.memeImageViewVideo);
        closeVideoPlay = findViewById(R.id.closeVideoPlay);
        videoPlayCard = findViewById(R.id.videoViewCard);
        videoPlayCard.setVisibility(INVISIBLE);
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
        getUserProfileDataRef.keepSynced(true);


        if(mAuth.getCurrentUser() == null)
        {
            mAuth.signOut();
            Intent intent = new Intent(ViewUsersVideos.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        viewMyVideos = findViewById(R.id.contentRating);

        mRecyclerView = findViewById(R.id.memeRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //returnToMaps = findViewById(R.id.returnToMaps);

        mProgressCircle = findViewById(R.id.progress_circle_user_memes);
        mProgressCirclePlay = findViewById(R.id.progress_circle_play);


        mProgressCircle.setVisibility(View.VISIBLE);
        mProgressCirclePlay.setVisibility(INVISIBLE);

        mUploads = new ArrayList<>();

        tagedUsers= new ArrayList<>();
        tagedImages= new ArrayList<>();

        mAdapter = new UserVideoViewAdapter(ViewUsersVideos.this, mUploads);

        mAdapter.setOnItemClickListener(ViewUsersVideos.this);

        mRecyclerView.setAdapter(mAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads");
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");
        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("LockedImages");


        /*contentRater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if(tagedImages.isEmpty())
                {
                    updateArrayListImages();
                }

            }
        });*/


        viewMyVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.contentRating);

                Intent intent = new Intent(ViewUsersVideos.this,VideowViewing.class);

                startActivity(intent);

            }
        });

        closeVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.closeVideoPlay);

                if(videoPlayCard.getVisibility()== View.VISIBLE) {
                    videoPlay.stopPlayback();
                    videoPlay.destroyDrawingCache();
                    videoPlayCard.setVisibility(INVISIBLE);
                }
            }
        });


        //userRef = FirebaseDatabase.getInstance().getReference().child("Users");


        if(tagedUsers.isEmpty() && !tagedImages.isEmpty() )
        {
            updateArrayList();

        }else
        {

            getDataNormally();

        }

        startVideoPlay();




    }

    private void updateArrayList()
    {
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");

        String currentUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef_x.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        String localID = ds.getKey();

                        tagedUsers.add(new tags(localID));
                        if(dataSnapshot.getChildrenCount() >=1 && tagedUsers.size()>=1 | dataSnapshot.getChildrenCount()==tagedUsers.size() )
                        {
                            mDatabaseRef_x.child(localID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    getDataNormally();
                                }
                            });
                        }else
                        {

                            Toast.makeText( ViewUsersVideos.this, "Online posts currently unavailable.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                }else
                {
                    Toast.makeText(ViewUsersVideos.this, "Pagiis online users currently unavailable.", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                String e = databaseError.getMessage();

                Toast.makeText(ViewUsersVideos.this, "Video play failed "+"results"+":"+e, Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void getDataNormally() {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads");
        final String userIdRef = mAuth.getCurrentUser().getUid();
        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        if(!tagedUsers.isEmpty()) {

            for (int i = 0; i < tagedUsers.size(); i++)

            {
               tags x = tagedUsers.get(i);

                String taged_id = x.getUser_tagID();

                if(taged_id.compareTo(userIdRef) !=0)
                {

                    mDBlistener = mDatabaseRef.child(taged_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mUploads.clear();

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ImageUploads upload = ds.getValue(ImageUploads.class);
                                upload.setKey(ds.getKey());
                                mUploads.add(upload);
                            }

                            mAdapter.notifyDataSetChanged();
                            mProgressCircle.setVisibility(INVISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(ViewUsersVideos.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressCircle.setVisibility(INVISIBLE);
                        }


                    });

                }


            }


        }else
        {
            Toast.makeText(this, "Pagiis online posts currently unavailable.", Toast.LENGTH_SHORT).show();

        }

    }

    private void getDataSnapShot() {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads").child(getKey);

        if(!getKey.isEmpty()) {

            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {

                        ImageUploads upload = dataSnapshot.getValue(ImageUploads.class);
                        upload.setKey(dataSnapshot.getKey());
                        mUploads.add(upload);

                    }else
                    {
                        Toast.makeText(ViewUsersVideos.this, "Pagiis failed to read database. please retry action", Toast.LENGTH_SHORT).show();
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

        String online_user_id = mAuth.getCurrentUser().getUid();



        getUserProfileDataRef.child(online_user_id).addValueEventListener(new ValueEventListener() {
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

    }

    private void getRestrictedData()

    {

        final String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();

        mDBlistener = mDatabaseRef.child(on_maps_visited_user_id).orderByChild("Age").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(INVISIBLE);
            }

            @Override

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewUsersVideos.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(INVISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uploads_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //This button is for the explicit content view
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add)
        {
            if(videoPlayCard.getVisibility()== View.VISIBLE) {
                videoPlay.stopPlayback();
                videoPlay.destroyDrawingCache();
                videoPlayCard.setVisibility(INVISIBLE);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position) {


        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String imageUrl = selectedImage.getImageUrl();

        final String userKeyId = selectedImage.getUserId();

        if (imageUrl != null) {

            if(!videoPlay.isPlaying()) {

                videoPlayCard.setVisibility(View.VISIBLE);
                videoPlay.setVideoURI(Uri.parse(imageUrl));
                videoPlay.start();
                mProgressCircle.setVisibility(View.VISIBLE);

                mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads").child(userKeyId).child(selectedKey);

                lockedImages = FirebaseDatabase.getInstance().getReference("Likes").child(selectedKey).child(userKeyId);
                //mDatabaseRef_z = FirebaseDatabase.getInstance().getReference("Views").child(selectedKey);

                if (!imageUrl.isEmpty() && !selectedKey.isEmpty()) {

                    lockedImages.addValueEventListener(new ValueEventListener() {

                        String UnitLikes;

                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                            //String name, String imageUrl, String rateEx, String userId,String views,String likes, String share

                            lockedImages.child("name").setValue(myName);
                            lockedImages.child("imageUrl").setValue(imageUrl);
                            lockedImages.child("rateEx").setValue(userStatusMessage);
                            lockedImages.child("userId").setValue(userKeyId);
                            lockedImages.child("views").setValue("1");
                            lockedImages.child("likes").setValue("1");
                            lockedImages.child("share").setValue(myImageDpUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    UnitLikes = String.valueOf(Objects.requireNonNull(dataSnapshot).getChildrenCount() + 1);

                                    mDatabaseRef.child("likes").setValue(UnitLikes);
                                    mDatabaseRef.child("views").setValue(UnitLikes);
                                    lockedImages.child("userId").setValue(UnitLikes);
                                    lockedImages.child("views").setValue(UnitLikes);
                                    mProgressCircle.setVisibility(INVISIBLE);

                                    //mDatabaseRef.child("likes").setValue(String.valueOf(dataSnapshot.getChildrenCount()));
                                    //mDatabaseRef.child("views").setValue(String.valueOf(dataSnapshot.getChildrenCount()));

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(ViewUsersVideos.this, "Click to like item.", Toast.LENGTH_SHORT).show();
                        }
                    });


                    videoPlay.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {

                            Mp = mediaPlayer;
                            if (i == MediaPlayer.MEDIA_INFO_BUFFERING_START) {


                                stopPosition = videoPlay.getCurrentPosition();
                                mProgressCircle.setVisibility(View.VISIBLE);
                                videoPlay.pause();
                            } else if (i == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                                mProgressCircle.setVisibility(INVISIBLE);
                                videoPlay.seekTo(stopPosition);
                                videoPlay.start();

                            }
                            return false;
                        }
                    });


                }

            }






        }
    }




    private MediaController mc;


    private void startVideoPlay() {

        mProgressCirclePlay.setVisibility(View.VISIBLE);


        videoPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {


                        mProgressCircle.setVisibility(View.INVISIBLE);

                        if(isFinishing()){

                            mc = new MediaController(getApplicationContext());
                            videoPlay.setMediaController(mc);
                            videoPlay.requestFocus();
                            mc.setAnchorView(videoPlay);
                            mProgressCircle.setVisibility(View.INVISIBLE);


                        }



                    }
                });
            }
        });
    }


    @Override
    public void onItemClick(int position) {


        ImageUploads selectedImage = mUploads.get(position);

        String selectedKey = selectedImage.getKey();

        String imageUrl = selectedImage.getImageUrl();

        String maxImageUserId = selectedImage.getUserId();

        mProgressCircle.setVisibility(View.VISIBLE);


        if(!imageUrl.isEmpty() && !selectedKey.isEmpty())
        {
            if(!videoPlay.isPlaying())
            {

                mProgressCirclePlay.setVisibility(View.VISIBLE);
                videoPlayCard.setVisibility(View.VISIBLE);
                videoPlay.setVideoURI(Uri.parse(imageUrl));
                videoPlay.start();

                mProgressCircle.setVisibility(INVISIBLE);

                videoPlay.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1)
                    {

                        Mp = mediaPlayer;

                        if(i == MediaPlayer.MEDIA_INFO_BUFFERING_START || i == MediaPlayer.MEDIA_INFO_VIDEO_NOT_PLAYING)
                        {


                            stopPosition = videoPlay.getCurrentPosition();
                            mProgressCirclePlay.setVisibility(View.VISIBLE);
                            videoPlay.pause();
                        }else if(i== MediaPlayer.MEDIA_INFO_BUFFERING_END || i !=MediaPlayer.MEDIA_INFO_VIDEO_NOT_PLAYING)
                        {
                            mProgressCirclePlay.setVisibility(INVISIBLE);
                            videoPlay.seekTo(stopPosition);
                            videoPlay.start();

                        }
                        return false;
                    }
                });

            }else
            {
                videoPlay.stopPlayback();
                Toast.makeText(this, "preparing video play ... ", Toast.LENGTH_SHORT).show();
            }


        }

    }

    @Override
    public void onItemClickMute(int position) {

    }

    @Override
    public void onWhatEverClick(int position)
    {
        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        String getUserRef = selectedImage.getUserId();

        if(!getUserRef.isEmpty() && !selectedKey.isEmpty())
        {
            Intent intent = new Intent(ViewUsersVideos.this, UserProfileActivity.class);
            intent.putExtra("visit_user_id", getUserRef);
            startActivity(intent);
        }else
        {
            Toast.makeText(this, "Pagiis failed to delete file. the system will retry deleting file later ", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        tagedUsers.clear();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateArrayList();
    }
}
