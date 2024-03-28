package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

import pagiisnet.pagiisnet.Utils.VideoViewAdapter;


public class VideowViewing extends AppCompatActivity implements  VideoViewAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;

    private VideoViewAdapter mAdapter;

    private ImageView userImageDp;

    private TextView userName;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private ProgressBar mProgressCircle,mProgressCirclePlay;


    private ImageView UploadMemeButton;

    private FirebaseAuth mAuth;
    private int stopPosition;

    enum  VolumeState {ON,OFF}

    private  String myLastLocationDetails;

    private VolumeState volumeState;

    private FirebaseStorage mStorage;

    private DatabaseReference mDatabaseRef;

    private ValueEventListener mDBlistener;

    private List<ImageUploads> mUploads;


    private VideoView videoPlay;

    private  MediaPlayer Mp;

    private ImageView closeVideoPlay;

    private CardView videoPlayCard;

    private FloatingActionButton contentRater;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videow_viewing);


        mRecyclerView = findViewById(R.id.memeRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null)
        {
            mAuth.signOut();
            Intent intent = new Intent(VideowViewing.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Live Videos");


        videoPlay = findViewById(R.id.memeImageViewVideo);
        closeVideoPlay = findViewById(R.id.closeVideoPlay);
        videoPlayCard = findViewById(R.id.videoViewCard);
        videoPlayCard.setVisibility(View.INVISIBLE);

        contentRater = findViewById(R.id.goToVideosUpload);
        contentRater.setVisibility(View.INVISIBLE);

        mStorage = FirebaseStorage.getInstance();

        closeVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.closeVideoPlay);

                if(videoPlayCard.getVisibility() == View.VISIBLE) {
                    videoPlay.stopPlayback();
                    videoPlay.setVideoURI(Uri.parse(""));
                    videoPlayCard.setVisibility(View.INVISIBLE);
                }


            }
        });



        /*contentRater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.goToVideosUpload);
                Intent intent = new Intent(VideowViewing.this,ActivityUploadVideo.class);
                intent.putExtra("visit_user_id", "fromVideoView");
                startActivity(intent);
            }
        });*/

        mProgressCircle = findViewById(R.id.progress_circle);
        mProgressCirclePlay = findViewById(R.id.progress_circle_play);
        mProgressCirclePlay.setVisibility(View.INVISIBLE);

        mUploads = new ArrayList<>();

        mAdapter = new VideoViewAdapter(VideowViewing.this, mUploads);

        mAdapter.setOnItemClickListener(VideowViewing.this);

        mRecyclerView.setAdapter(mAdapter);

        userImageDp = findViewById(R.id.ImageDP);

        userName = findViewById(R.id.profileName);

        final String user_id = mAuth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads");

        mDBlistener = mDatabaseRef.child(user_id).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
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
                    mProgressCircle.setVisibility(View.INVISIBLE);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VideowViewing.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


        prepareVideoPlay();


    }


    private void prepareVideoPlay()
    {



        mProgressCirclePlay.setVisibility(View.VISIBLE);


        videoPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height)
                    {

                        mProgressCirclePlay.setVisibility(View.INVISIBLE);
                        if(isFinishing())
                        {
                            mc = new MediaController(getApplicationContext());
                            videoPlay.setMediaController(mc);
                            videoPlay.requestFocus();
                            mc.setAnchorView(videoPlay);


                        }






                    }
                });
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uploads_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {
            if(videoPlayCard.getVisibility()== View.VISIBLE) {
                videoPlay.stopPlayback();
                videoPlay.setVideoURI(Uri.parse(""));
                videoPlayCard.setVisibility(View.INVISIBLE);
            }
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

        mProgressCircle.setVisibility(View.VISIBLE);


        if(!imageUrl.isEmpty() && !selectedKey.isEmpty())
        {
            if(!videoPlay.isPlaying())
            {

                mProgressCirclePlay.setVisibility(View.VISIBLE);


                videoPlayCard.setVisibility(View.VISIBLE);
                videoPlay.setVideoURI(Uri.parse(imageUrl));
                videoPlay.start();

                mProgressCircle.setVisibility(View.INVISIBLE);

                if(!isFinishing())
                {

                    videoPlay.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1)
                        {

                            Mp = mediaPlayer;
                            if(i == MediaPlayer.MEDIA_INFO_BUFFERING_START )
                            {


                                stopPosition = videoPlay.getCurrentPosition();
                                mProgressCirclePlay.setVisibility(View.VISIBLE);
                                videoPlay.pause();
                            }else if(i== MediaPlayer.MEDIA_INFO_BUFFERING_END )
                            {
                                mProgressCircle.setVisibility(View.INVISIBLE);
                                videoPlay.seekTo(stopPosition);
                                videoPlay.start();

                            }
                            return false;
                        }
                    });



                }



            }else
            {
                videoPlay.stopPlayback();
                Toast.makeText(this, "preparing video play ... ", Toast.LENGTH_SHORT).show();
            }




        }
    }

    private MediaController mc;



    @Override
    public void onItemClickMute(int position)
    {
        if (isFinishing())
        {

            setVolumeControl(VolumeState.OFF);
        }



    }

    private void setVolumeControl(VolumeState state)

    {

        if(!isFinishing()){


            AudioManager audioManager = (AudioManager)getSystemService(VideowViewing.AUDIO_SERVICE);

            volumeState = state;

            if(state == VolumeState.ON)
            {

                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,0,0);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
                volumeState = VolumeState.OFF;

            }else if(state == VolumeState.OFF)
            {

                volumeState = VolumeState.ON;
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,100,0);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,100,0);

            }



        }


    }


    @Override
    public void onWhatEverClick(int position) {

        Intent intent = new Intent(VideowViewing.this, ActivityOwnProfile.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        String myUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads").child(myUserId);

        //StorageReference deletImage = mStorage.getReferenceFromUrl(selectedImage.getImageUrl());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot ds :dataSnapshot.getChildren())
                {
                    if(ds.getKey().compareTo(selectedKey)== 0)
                    {

                        mDatabaseRef.child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(VideowViewing.this, "Item sent to bin..", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(VideowViewing.this, (CharSequence) e, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
}
