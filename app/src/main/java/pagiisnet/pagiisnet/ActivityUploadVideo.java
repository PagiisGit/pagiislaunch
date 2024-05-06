package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityUploadVideo extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1;

    private Uri mImageUri;

    private ImageView mButtonChooseImage;

    private FloatingActionButton mButtonUpload;


    private  enum  VolumeState {ON,OFF}

    private  String myLastLocationDetails;

    private  VolumeState volumeState;

    private DatabaseReference checkAndminTrue;

    private final int raterBarValue = 0;

    private String onlineUserId;

    private EditText mEditTextFileName;

    private FloatingActionButton viewUploads;
    private String myName;

    //private ProgressBar mProgressCircle;

    private VideoView mImageView;

    private ImageView videoView;

    private ImageView userProfileDp;

    private  DatabaseReference locationDetails;

    private DatabaseReference getUserProfileDataRef;

    private ProgressBar mProgressBar;

    private Calendar calendar;

    private SimpleDateFormat simpleDateFormat;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private DatabaseReference mDatabaseRef;

    private StorageReference mStorageRef;

    private DatabaseReference mDatabaseRef_S;

    private StorageReference mStorageRef_S;

    private StorageTask mUploadTask;

    private MediaController mc;

    private String uploadTimeValue;

    private String VisiteduserId;


    // private Uri resultUri;

    private FirebaseAuth mAuth;

    private  DatabaseReference getUserInfor;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        mButtonChooseImage = findViewById(R.id.chooseUpload);

        mButtonUpload = findViewById(R.id.uploadChosenFile);

        mEditTextFileName = findViewById(R.id.chosenTitleEdit);

        mImageView = findViewById(R.id.ChosenFilename);
        videoView = findViewById(R.id.ChosenFilenamImage);

        mProgressBar = findViewById(R.id.progress_circle_upload);

        //userProfileDp = findViewById(R.id.UserProfileDp);

        calendar = Calendar.getInstance();

        simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

        viewUploads = findViewById(R.id.goToUploads);

        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");


        locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation");


        mProgressBar.setVisibility(View.INVISIBLE);


        mToolbar = findViewById(R.id.appBarLayout);

        setSupportActionBar(mToolbar);

        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);

        //mProgressCircle.setVisibility(View.INVISIBLE);

        actionBar.setDisplayHomeAsUpEnabled(true);


        String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();

        VisiteduserId = on_maps_visited_user_id;

        actionBar.setDisplayShowTitleEnabled(true);


        actionBar.setTitle("Pagiis Video Upload");

        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View actionBarView = layoutInflater.inflate(R.layout.custom_bar_upload,null);

        actionBar.setCustomView(actionBarView);


        onlineUserId = mAuth.getCurrentUser().getUid();

        // getUserInfor = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        mButtonUpload.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                if(mUploadTask != null && mUploadTask.isInProgress())

                {
                    Toast.makeText(ActivityUploadVideo.this, "PAGiiS image upload in progress !!", Toast.LENGTH_SHORT).show();
                }else
                {
                    mProgressBar.setVisibility(View.VISIBLE);


                    if(VisiteduserId.compareTo("fromVideoView") == 0 )
                    {

                        Upload();

                    }else if(VisiteduserId.compareTo("fromSirocco") ==0)
                    {
                        UploadPromo();
                    }

                }
            }
        });

        viewUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.findViewById(R.id.goToUploads);
                Intent intent = new Intent(ActivityUploadVideo.this, VideowViewing.class);
                intent.putExtra("visit_user_id","null");
                startActivity(intent);
            }
        });



        getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("userNameAsEmail").getValue().toString();
                myName = name;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myLastLocationDetailsMetod();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uploads_menu,menu);
        return super.onCreateOptionsMenu(menu);  }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add)
        {
            openFileChooser();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFileChooser()
    {

        Intent intent =  new  Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK   && data != null && data.getData() != null) {
            mImageUri = data.getData();
            mImageView.setVideoURI(mImageUri);
            mImageView.requestFocus();

            mImageView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height)
                        {
                            mc = new MediaController(ActivityUploadVideo.this);
                            mImageView.setMediaController(mc);
                            mImageView.seekTo(100);
                            mc.setAnchorView(mImageView);
                            mImageView.requestFocus();
                            mImageView.start();

                            mImageView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                @Override
                                public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1)
                                {

                                    if(i == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                                    {
                                        mImageView.seekTo(100);
                                        mProgressBar.setVisibility(View.VISIBLE);
                                    }else if(i== MediaPlayer.MEDIA_INFO_BUFFERING_END)
                                    {
                                        mProgressBar.setVisibility(View.INVISIBLE);

                                    }
                                    return false;
                                }
                            });

                        }
                    });
                }
            });

            mImageView.start();
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void Upload()
    {

        if (mImageUri != null)
        {

            getTimeUpload();
            Toast.makeText(ActivityUploadVideo.this, "PAGiiS image uploading ...", Toast.LENGTH_SHORT).show();


        }else
        {
            Toast.makeText(this, "No video file selected..!", Toast.LENGTH_SHORT).show();
        }
    }



    private void UploadPromo()
    {

        if (mImageUri != null) {


            getTimeUploadPromo();

            Toast.makeText(ActivityUploadVideo.this, "PAGiiS image uploading ...", Toast.LENGTH_SHORT).show();


        }else
        {
            Toast.makeText(this, "No video file selected..!", Toast.LENGTH_SHORT).show();
        }
    }



    private void getTimeUploadPromo()

    {

        simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");


        uploadTimeValue  = simpleDateFormat.format(calendar.getTime());

        final String currentUserId = mAuth.getCurrentUser().getUid();

        //mProgressCircle.setVisibility(View.VISIBLE);

        final String imageUrl = String.valueOf(mImageUri);

        final String saveRaterBarValue = String.valueOf(raterBarValue);

        mDatabaseRef_S = FirebaseDatabase.getInstance().getReference("SiroccoVideoUploads");

        mStorageRef_S = FirebaseStorage.getInstance().getReference("SiroccoVideoUploads");

        final StorageReference fileReference = mStorageRef_S.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));


        if (mUploadTask != null && !mUploadTask.isInProgress()) {
            Toast.makeText(this, "Image upload in progress.", Toast.LENGTH_SHORT).show();
        } else {
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()

                            {

                                Uri finalImageUri;

                                String finalImageUrl;

                                @Override
                                public void onSuccess(Uri uri)

                                {

                                    finalImageUri = uri;

                                    finalImageUrl = String.valueOf(uri);

                                    ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(),
                                            finalImageUrl, saveRaterBarValue, currentUserId, "1","1","0",uploadTimeValue,myLastLocationDetails,myName );

                                    mDatabaseRef_S.child(currentUserId)
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(ActivityUploadVideo.this, "PAGiiS Video upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(ActivityUploadVideo.this, SiroccoPromoVideos.class);
                                                    startActivity(intent);
                                                    // String uniqueKey = databaseReference.getKey();
                                                    //Create the function for Clearing/The ImageView Widget.
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
                            }, 1000);

                            //Toast.makeText(ActivityUploadImage.this, "PAGiiS file selection successful .", Toast.LENGTH_SHORT).show();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ActivityUploadVideo.this, "Pagiis failed to upload, please check Internet connections", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
        }

    }




    private void getTimeUpload() {
        //String uploadTime = calendar.getTime().toString();

        simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");


        uploadTimeValue  = simpleDateFormat.format(calendar.getTime());



        final String currentUserId = mAuth.getCurrentUser().getUid();

        //mProgressCircle.setVisibility(View.VISIBLE);

        final String imageUrl = String.valueOf(mImageUri);

        final String saveRaterBarValue = String.valueOf(raterBarValue);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads");

        mStorageRef = FirebaseStorage.getInstance().getReference("videoUploads");

        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));


        if (mUploadTask != null && !mUploadTask.isInProgress()) {
            Toast.makeText(this, "Image upload in progress.", Toast.LENGTH_SHORT).show();
        } else {
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()

                            {

                                Uri finalImageUri;

                                String finalImageUrl;

                                @Override
                                public void onSuccess(Uri uri)

                                {

                                    finalImageUri = uri;

                                    finalImageUrl = String.valueOf(uri);

                                    ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(),
                                            finalImageUrl, saveRaterBarValue, currentUserId, "1","1","0",uploadTimeValue,myLastLocationDetails,myName );

                                    mDatabaseRef.child(currentUserId)
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(ActivityUploadVideo.this, "PAGiiS Video upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(ActivityUploadVideo.this, VideowViewing.class);
                                                    startActivity(intent);
                                                    // String uniqueKey = databaseReference.getKey();
                                                    //Create the function for Clearing/The ImageView Widget.
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
                            }, 1000);

                            //Toast.makeText(ActivityUploadImage.this, "PAGiiS file selection successful .", Toast.LENGTH_SHORT).show();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ActivityUploadVideo.this, "Pagiis failed to upload, please check Internet connections", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }



    private void myLastLocationDetailsMetod()
    {

        String user_id = mAuth.getCurrentUser().getUid();

        locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation").child(user_id);

        locationDetails.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(ActivityUploadVideo.this, "last location not found"+"Results"+":"+e, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


}
