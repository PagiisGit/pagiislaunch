package pagiisnet.pagiisnet;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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
    private ImageView uploadItem;

    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




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
    } //The end of onCreate functions will be placed outside here

    private void openFileChooserVideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
    }


    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (PICK_IMAGE_REQUEST == requestCode) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                mImageUri = data.getData();
                finalPicUri = mImageUri;

                RequestOptions options = new RequestOptions();
                differentiaterValue = "primary";
                mImageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);

                Glide.with(getActivity()).load(mImageUri).apply(options.centerCrop()).thumbnail(0.65f).into(mImageView);
            }
        } else if (PICK_VIDEO_REQUEST == requestCode) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
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
                                mc = new MediaController(getActivity());
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
            }
        }
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

        Toast.makeText(getActivity(), "All Good", Toast.LENGTH_SHORT).show();

        if (result.getResultCode() == Activity.RESULT_OK)
        {

            Toast.makeText(getActivity(), "All Good", Toast.LENGTH_SHORT).show();

            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                mImageUri = data.getData();
                finalPicUri = mImageUri;

                if(mImageUri != null)
                {
                    Toast.makeText(getActivity(), "this is the image Uri"+ mImageUri, Toast.LENGTH_SHORT).show();

                }else
                {
                    Toast.makeText(getActivity(), "Image Uri is null", Toast.LENGTH_SHORT).show();
                }



                RequestOptions options = new RequestOptions();
                differentiaterValue = "primary";
                mImageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);

                Glide.with(getActivity()).load(mImageUri).apply(options.centerCrop()).thumbnail(0.65f).into(mImageView);
            }else

            {
                Toast.makeText(getActivity(), "Data returned is null", Toast.LENGTH_SHORT).show();

            }

        }else

        {
            Toast.makeText(getActivity(), "Result code had an error", Toast.LENGTH_SHORT).show();

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
                                mc = new MediaController(getActivity());
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
                Toast.makeText(getActivity(), "Data returned is null", Toast.LENGTH_SHORT).show();

            }
        }else

        {
            Toast.makeText(getActivity(), "Result code had an error", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getActivity(), "last location not found" + "Results" + ":" + e, Toast.LENGTH_SHORT).show();

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

                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

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

            Toast.makeText(getActivity(), "PAGiiS image uploading ...", Toast.LENGTH_SHORT).show();

            //mProgressCircle.setVisibility(View.VISIBLE);

            //final String imageUrl = String.valueOf(mImageUri);

            final String saveRaterBarValue = raterBarValue;

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

            mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(Uri.parse(String.valueOf(mImageUri))));

            if (mUploadTask != null && !mUploadTask.isInProgress()) {
                Toast.makeText(getActivity(), "Image upload in progress.", Toast.LENGTH_SHORT).show();
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
                                                        Toast.makeText(getActivity(), "PAGiiS image upload successful !!", Toast.LENGTH_SHORT).show();

                                                        mProgressBar.setVisibility(View.INVISIBLE);
                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.GalleryFragment,new HomeFragment()).commit();
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
                                Toast toast = new Toast(getActivity());

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
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Video upload in progress.", Toast.LENGTH_SHORT).show();
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
                                                        Toast.makeText(getActivity(), " Video upload successful !!", Toast.LENGTH_SHORT).show();
                                                        mProgressBar.setVisibility(View.INVISIBLE);
                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.GalleryFragment,new VideoFragment()).commit();
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
                                Toast.makeText(getActivity(), "Pagiis failed to upload, please check Internet connections", Toast.LENGTH_LONG).show();

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

            Toast.makeText(getActivity(), "Please select file to post", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);


        //mButtonChooseImage = rootView.findViewById(R.id.chooseUpload);

        mButtonUpload = rootView.findViewById(R.id.uploadChosenFile);

        mEditTextFileName = rootView.findViewById(R.id.chosenTitleEdit);

        mImageView = rootView.findViewById(R.id.galleryChosenImageView);

        videoView = rootView.findViewById(R.id.chosenVideoView);

        Videos = rootView.findViewById(R.id.videOption);
        Gallery = rootView.findViewById(R.id.GalleryOption);

        userProfileDp = rootView.findViewById(R.id.writeStatus);

        mProgressBar = rootView.findViewById(R.id.progress_circle_upload);

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
            public void onClick(View v) {

                simpleDateFormat = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

                postTimeDateStamp = simpleDateFormat.format(calendar.getTime());

                v.findViewById(R.id.uploadChosenFile);

                if (differentiaterValue == "primary") {


                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(getActivity(), "PAGiiS image upload in progress !!", Toast.LENGTH_SHORT).show();


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
                                    Toast.makeText(getActivity(), "PAGiiS status upload-max reached !!" + "\n" + "Please delete some of your posts and repost", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                } else {

                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(getActivity(), "Video upload in progress !!", Toast.LENGTH_SHORT).show();


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
                                    Toast.makeText(getActivity(), "PAGiiS status upload-max reached !!" + "\n" + "Please delete some of your posts and repost", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }


                }

            }
        });


        return rootView;


    }
}