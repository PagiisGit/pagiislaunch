package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GalleryUploads extends AppCompatActivity
{



    private Uri mImageUri;

    private static final int PICK_VIDEO_REQUEST = 2;


    private Uri finalPicUri;
    private String differentiaterValue;


    private ImageView mButtonChooseImage;

    private Button mButtonUpload;

    private DatabaseReference getUserProfileDataRef;

    private DatabaseReference checkAndminTrue;

    private String raterBarValue;


    private String uploadTimeValue;


    private Calendar calendar;

    private DatabaseReference locationDetails;

    private String myLastLocationDetails;


    private EditText mEditTextFileName;

    private Fragment oldFragment;

    private FloatingActionButton contentRater;

    private ImageView viewUploads;

    //private ProgressBar mProgressCircle;

    private ImageView mImageView;
    private MediaController mc;
    private VideoView videoView;

    private ImageView userProfileDp;

    private SimpleDateFormat simpleDateFormat;

    private String postTimeDateStamp;

    private ProgressBar mProgressBar;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_x;

    private StorageReference mStorageRef;

    private StorageTask mUploadTask;

    private String shareItemId;
    private String MyName;


    // private Uri resultUri;

    private FirebaseAuth mAuth;

    private DatabaseReference getUserInfor;


    private ImageView Gallery;
    private ImageView Videos;
    private ImageView returnToHomeImageView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_uploads);


        mAuth = FirebaseAuth.getInstance();
        myLastLocationDetailsMetod();


        locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation");
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
        getUserProfileDataRef.keepSynced(true);


        String onlineUserId = mAuth.getCurrentUser().getUid();


        getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //String name = dataSnapshot.child("userNameAsEmail").getValue().toString();
                //String userStatus = dataSnapshot.child("userDefaultStatus").getValue().toString();

                String myDpUrl = dataSnapshot.child("userImageDp").getValue().toString();
                //userStatusMessage  = userStatus;
                RequestOptions options = new RequestOptions();

                //Glide.with(getApplicationContext()).load(myDpUrl).apply(options.centerCrop()).thumbnail(0.65f).into(userProfileDp);
                //myName = name;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        myLastLocationDetailsMetod();


        mButtonUpload = findViewById(R.id.uploadChosenFile);

        mEditTextFileName = findViewById(R.id.chosenTitleEdit);

        mImageView = findViewById(R.id.galleryChosenImageView);

        videoView = findViewById(R.id.chosenVideoView);

        Videos = findViewById(R.id.videOption);
        Gallery = findViewById(R.id.GalleryOption);
        returnToHomeImageView = findViewById(R.id.returnToHome);

        userProfileDp = findViewById(R.id.writeStatus);

        mProgressBar =findViewById(R.id.progress_circle_upload);

        //userProfileDp = findViewById(R.id.UserProfileDp);

        mProgressBar.setVisibility(View.INVISIBLE);

        //contentRater = rootView.findViewById(R.id.contentRating);

        //contentRater.setVisibility(View.VISIBLE);

        //contentRater.setEnabled(false);

        calendar = Calendar.getInstance();

        simpleDateFormat = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

        //simpleDateFormat.format(calendar.getTime());

        //mToolbar = rootView.findViewById(R.id.appBarLayout);

        //LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.galleryChosenImageView);
                openImageChooser();
            }
        });

        returnToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.returnToHome);
                finish();
            }
        });

        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.GalleryOption);
                openImageChooser();
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.chosenVideoView);
                openVideoChooser() ;
            }
        });


        Videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.videOption);
                openVideoChooser();
                mImageView.setVisibility(View.INVISIBLE);
            }
        });




        mButtonUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {



                simpleDateFormat = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

                postTimeDateStamp = simpleDateFormat.format(calendar.getTime());

                v.findViewById(R.id.uploadChosenFile);

                Bundle bundle = getIntent().getExtras();

                final String dataString = bundle.getString("visited_user_id");


                if(bundle != null)
                {

                    String data = bundle.getString("visited_user_id");
                    if (differentiaterValue == "primary" && dataString.compareTo("nulll")!=0)

                    {


                        if (mUploadTask != null && mUploadTask.isInProgress()) {
                            Toast.makeText(getApplicationContext(), "PAGiiS image upload in progress !!", Toast.LENGTH_SHORT).show();


                        } else {

                            final String own_user_id = mAuth.getUid();

                            mProgressBar.setVisibility(View.VISIBLE);



                            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

                            mDatabaseRef.child(own_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() <= 100)
                                    {
                                        addProduct(dataString);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "PAGiiS status upload-max reached !!" + "\n" + "Please delete some of your posts and repost", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                    } else if(differentiaterValue == "primary" && dataString.compareTo("nulll")==0)
                    {

                        if (mUploadTask != null && mUploadTask.isInProgress()) {
                            Toast.makeText(getApplicationContext(), "PAGiiS image upload in progress !!", Toast.LENGTH_SHORT).show();


                        } else {

                            final String own_user_id = mAuth.getUid();

                            mProgressBar.setVisibility(View.VISIBLE);



                            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

                            mDatabaseRef.child(own_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() <= 100)
                                    {
                                        addProduct(dataString);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "PAGiiS status upload-max reached !!" + "\n" + "Please delete some of your posts and repost", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }





                    }else {

                        if (mUploadTask != null && mUploadTask.isInProgress()) {
                            Toast.makeText(getApplicationContext(), "Video upload in progress !!", Toast.LENGTH_SHORT).show();


                        } else {

                            final String own_user_id = mAuth.getUid();

                            mProgressBar.setVisibility(View.VISIBLE);

                            mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads");

                            mDatabaseRef.child(own_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() <= 100) {
                                        getTimeUpload();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "PAGiiS status upload-max reached !!" + "\n" + "Please delete some of your posts and repost", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }


                    }






                }else

                {
                    if (differentiaterValue == "primary") {


                        if (mUploadTask != null && mUploadTask.isInProgress()) {
                            Toast.makeText(getApplicationContext(), "PAGiiS image upload in progress !!", Toast.LENGTH_SHORT).show();


                        } else {

                            final String own_user_id = mAuth.getUid();

                            mProgressBar.setVisibility(View.VISIBLE);

                            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

                            mDatabaseRef.child(own_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() <= 100) {
                                        Upload();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "PAGiiS status upload-max reached !!" + "\n" + "Please delete some of your posts and repost", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                    } else {

                        if (mUploadTask != null && mUploadTask.isInProgress()) {
                            Toast.makeText(getApplicationContext(), "Video upload in progress !!", Toast.LENGTH_SHORT).show();


                        } else {

                            final String own_user_id = mAuth.getUid();

                            mProgressBar.setVisibility(View.VISIBLE);

                            mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads");

                            mDatabaseRef.child(own_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() <= 100) {
                                        getTimeUpload();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "PAGiiS status upload-max reached !!" + "\n" + "Please delete some of your posts and repost", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }


                    }


                }



            }
        });



    }

    private void popLinkEdit()
    {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(GalleryUploads.this);
                View mView = getLayoutInflater().inflate(R.layout.productstorelink, null);

                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText emailField = mView.findViewById(R.id.emailContact);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText cellPhoneNumber = mView.findViewById(R.id.cancelSignOutImage);
                ImageButton sendEmail = mView.findViewById(R.id.sendEmail);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

                sendEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!emailField.getText().toString().isEmpty())

                        {

                            String link = emailField.getText().toString();


                            addProduct(link);


                        }else
                        {

                            Toast.makeText(getApplicationContext(), "Paste a link to your product store.", Toast.LENGTH_LONG).show();

                        }

                    }
                });

    }


    private void openImageChooser()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }



    private void openVideoChooser()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        pickVideoLauncher.launch(intent);
    }


    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> handleImageResult(result));

    private final ActivityResultLauncher<Intent> pickVideoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> handleVideoResult(result));

    // ... Rest of your Fragment code ...

    private void handleImageResult(@NonNull ActivityResult result)
    {

        Toast.makeText(getApplicationContext(), "All Good", Toast.LENGTH_SHORT).show();

        if (result.getResultCode() == Activity.RESULT_OK)
        {

            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                mImageUri = data.getData();
                finalPicUri = mImageUri;

                if(mImageUri != null)
                {
                    Toast.makeText(getApplicationContext(), "selected file successful ", Toast.LENGTH_SHORT).show();

                }else
                {
                    Toast.makeText(getApplicationContext(), "Image Uri is null", Toast.LENGTH_SHORT).show();
                }



                RequestOptions options = new RequestOptions();
                differentiaterValue = "primary";
                mImageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);

                Glide.with(getApplicationContext()).load(mImageUri).apply(options.centerCrop()).thumbnail(0.65f).into(mImageView);
            }else

            {
                Toast.makeText(getApplicationContext(), "Data returned is null", Toast.LENGTH_SHORT).show();

            }

        }else

        {
            Toast.makeText(getApplicationContext(), "Result code had an error", Toast.LENGTH_SHORT).show();

        }
    }

    private void handleVideoResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                mImageUri = data.getData();
                videoView.setVideoURI(mImageUri);
                videoView.requestFocus();
                mImageView.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.VISIBLE);

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                            @Override
                            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
                                mc = new MediaController(getApplicationContext());
                                videoView.setMediaController(mc);
                                videoView.seekTo(100);
                                mc.setAnchorView(videoView);
                                videoView.requestFocus();
                                videoView.start();

                                videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                    @Override
                                    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {

                                        if (i == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                                            videoView.seekTo(100);
                                            mProgressBar.setVisibility(View.VISIBLE);
                                        } else if (i == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                                            mProgressBar.setVisibility(View.INVISIBLE);
                                        }
                                        return false;
                                    }
                                });
                            }
                        });
                    }
                });

                videoView.start();

                // ... Rest of your video handling code ...
            }else

            {
                Toast.makeText(getApplicationContext(), "Data returned is null", Toast.LENGTH_SHORT).show();

            }
        }else

        {
            Toast.makeText(getApplicationContext(), "Result code had an error", Toast.LENGTH_SHORT).show();

        }
    }

















































    private void myLastLocationDetailsMetod() {

        String user_id = mAuth.getCurrentUser().getUid();

        locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation").child(user_id);

        locationDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myLastLocationDetails = dataSnapshot.getValue().toString();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                String e = databaseError.getMessage();
                Toast.makeText(getApplicationContext(), "last location not found" + "Results" + ":" + e, Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void checkifAdmindTrue() {

        final String userId = mAuth.getCurrentUser().getUid();
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        mDatabaseRef_x.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("admin") && dataSnapshot.child("admin").getValue().equals(true)) {
                    contentRater.setEnabled(true);
                    contentRater.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }




    private String getFileExtension(Uri uri) {
        @SuppressLint("RestrictedApi") ContentResolver cR = getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void Upload()
    {
        if (mImageUri != null)
        {
            final String raterBarValueDefault = "userDefaultDp";

            final String currentUserId = mAuth.getCurrentUser().getUid();

            mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

            mDatabaseRef_x.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    raterBarValue = dataSnapshot.child("userImageDp").getValue().toString();
                    MyName = dataSnapshot.child("userNameAsEmail").getValue().toString();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Toast.makeText(getApplicationContext(), "PAGiiS image uploading ...", Toast.LENGTH_SHORT).show();

            //mProgressCircle.setVisibility(View.VISIBLE);

            //final String imageUrl = String.valueOf(mImageUri);

            final String saveRaterBarValue = String.valueOf(raterBarValue);

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

            mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(Uri.parse(String.valueOf(mImageUri))));

            if (mUploadTask != null && !mUploadTask.isInProgress()) {
                Toast.makeText(getApplicationContext(), "Image upload in progress.", Toast.LENGTH_SHORT).show();
            } else {

                mUploadTask = fileReference.putFile(mImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                    Uri finalImageUri;

                                    String finalImageUrl;

                                    @Override
                                    public void onSuccess(Uri uri) {

                                        finalImageUri = uri;
                                        finalImageUrl = String.valueOf(uri);


                                        ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(), finalImageUrl, raterBarValue, currentUserId, raterBarValueDefault, raterBarValueDefault, raterBarValueDefault, postTimeDateStamp, myLastLocationDetails, MyName);
                                        mDatabaseRef.child(currentUserId)
                                                .push()
                                                .setValue(upload, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError,
                                                                           DatabaseReference databaseReference) {

                                                        //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                        Toast.makeText(getApplicationContext(), "PAGiiS image upload successful !!", Toast.LENGTH_SHORT).show();

                                                        mProgressBar.setVisibility(View.INVISIBLE);
                                                        finish();
                                                        // String uniqueKey = databaseReference.getKey();
                                                        //Create the function for Clearing/The ImageView Widget.
                                                    }
                                                });
                                    }

                                });

                                //finalImageUrl = String.valueOf(finalImageUri);

                                Handler handler = new Handler();

                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(0);
                                    }
                                }, 1000);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Get the custom layout view.


                                View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);

                                mProgressBar.setVisibility(View.INVISIBLE);

                                TextView messageGrid = toastView.findViewById(R.id.customToastText);
                                // Initiate the Toast instance.
                                Toast toast = new Toast(getApplicationContext());

                                messageGrid.setText("Pagiis failed to upload, please check Internet connections.");
                                // Set custom view in toast.
                                toast.setView(toastView);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                //Toast.makeText(ActivityUploadImage.this, "", Toast.LENGTH_LONG).show();
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


        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }


    }


    private void addProduct(String link)
    {
        if (mImageUri != null)
        {
            final String raterBarValueDefault = "userDefaultDp";

            final String currentUserId = mAuth.getCurrentUser().getUid();

            mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

            mDatabaseRef_x.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    raterBarValue = dataSnapshot.child("userImageDp").getValue().toString();
                    MyName = dataSnapshot.child("userNameAsEmail").getValue().toString();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Toast.makeText(getApplicationContext(), "PAGiiS image uploading ...", Toast.LENGTH_SHORT).show();

            //mProgressCircle.setVisibility(View.VISIBLE);

            //final String imageUrl = String.valueOf(mImageUri);

            final String saveRaterBarValue = String.valueOf(raterBarValue);

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

            mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(Uri.parse(String.valueOf(mImageUri))));

            if (mUploadTask != null && !mUploadTask.isInProgress()) {
                Toast.makeText(getApplicationContext(), "Image upload in progress.", Toast.LENGTH_SHORT).show();
            } else {

                mUploadTask = fileReference.putFile(mImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                    Uri finalImageUri;

                                    String finalImageUrl;

                                    @Override
                                    public void onSuccess(Uri uri) {

                                        finalImageUri = uri;
                                        finalImageUrl = String.valueOf(uri);


                                        ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(), finalImageUrl, link, currentUserId, raterBarValueDefault, raterBarValueDefault, raterBarValueDefault, postTimeDateStamp, myLastLocationDetails, MyName);
                                        mDatabaseRef.child(currentUserId)
                                                .push()
                                                .setValue(upload, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError,
                                                                           DatabaseReference databaseReference) {

                                                        //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                        Toast.makeText(getApplicationContext(), "PAGiiS image upload successful !!", Toast.LENGTH_SHORT).show();

                                                        mProgressBar.setVisibility(View.INVISIBLE);
                                                        finish();
                                                        // String uniqueKey = databaseReference.getKey();
                                                        //Create the function for Clearing/The ImageView Widget.
                                                    }
                                                });
                                    }

                                });

                                //finalImageUrl = String.valueOf(finalImageUri);

                                Handler handler = new Handler();

                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(0);
                                    }
                                }, 1000);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Get the custom layout view.


                                View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);

                                mProgressBar.setVisibility(View.INVISIBLE);

                                TextView messageGrid = toastView.findViewById(R.id.customToastText);
                                // Initiate the Toast instance.
                                Toast toast = new Toast(getApplicationContext());

                                messageGrid.setText("Pagiis failed to upload, please check Internet connections.");
                                // Set custom view in toast.
                                toast.setView(toastView);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                //Toast.makeText(ActivityUploadImage.this, "", Toast.LENGTH_LONG).show();
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


        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }


    }


    private void getTimeUpload() {
        //String uploadTime = calendar.getTime().toString();

        if(mImageUri != null) {

            simpleDateFormat = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");


            uploadTimeValue = simpleDateFormat.format(calendar.getTime());


            final String currentUserId = mAuth.getCurrentUser().getUid();

            //mProgressCircle.setVisibility(View.VISIBLE);

            final String imageUrl = String.valueOf(mImageUri);

            final String saveRaterBarValue = String.valueOf(raterBarValue);

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads");

            mStorageRef = FirebaseStorage.getInstance().getReference("videoUploads");

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));


            if (mUploadTask != null && !mUploadTask.isInProgress()) {
                Toast.makeText(getApplicationContext(), "Video upload in progress.", Toast.LENGTH_SHORT).show();
            } else {
                mUploadTask = fileReference.putFile(mImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                    Uri finalImageUri;

                                    String finalImageUrl;

                                    @Override
                                    public void onSuccess(Uri uri) {

                                        finalImageUri = uri;

                                        finalImageUrl = String.valueOf(uri);

                                        ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(),
                                                finalImageUrl, saveRaterBarValue, currentUserId, "1", "1", "0", uploadTimeValue, myLastLocationDetails, MyName);

                                        mDatabaseRef.child(currentUserId)
                                                .push()
                                                .setValue(upload, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError,
                                                                           DatabaseReference databaseReference) {

                                                        //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                        Toast.makeText(getApplicationContext(), " Video upload successful !!", Toast.LENGTH_SHORT).show();
                                                        mProgressBar.setVisibility(View.INVISIBLE);
                                                        getSupportFragmentManager().beginTransaction().replace(R.id.GalleryFragment,new VideoFragment()).commit();
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
                                Toast.makeText(getApplicationContext(), "Pagiis failed to upload, please check Internet connections", Toast.LENGTH_LONG).show();

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
        }else{

            mProgressBar.setVisibility(View.INVISIBLE);

            Toast.makeText(getApplicationContext(), "Please select file to post", Toast.LENGTH_SHORT).show();
        }
    }

}