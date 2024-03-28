package pagiisnet.pagiisnet;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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


import pagiisnet.pagiisnet.R;

public class SirrocoImageUpload extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;

    private ImageView mButtonChooseImage;

    private ImageView mButtonUpload;

    private FloatingActionButton notificationUpload;


    private DatabaseReference checkAndminTrue;

    private final int raterBarValue = 0;

    private EditText mEditTextFileName;

    private EditText mEditTextFileModel;

    private String VisiteduserId;
    private String VisiteduserKey;

    private ImageView viewUploads;
    private ImageView viewUploadsHeadlines;


    //private ProgressBar mProgressCircle;

    private ImageView mImageView;

    private ImageView userProfileDp;

    private ProgressBar mProgressBar;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference nDatabaseRef;

    private ImageView musiUpload;

    private StorageReference mStorageRef;
    private StorageReference nStorageRef;

    private StorageTask mUploadTask;

    // private Uri resultUri;

    private FirebaseAuth mAuth;

    private  DatabaseReference getUserInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sirroco_image_upload);

        mButtonChooseImage = findViewById(R.id.chooseUpload);

        mButtonUpload = findViewById(R.id.uploadChosenFile);

        mEditTextFileName = findViewById(R.id.chosenTitleEdit);

        mEditTextFileModel = findViewById(R.id.chosenTitleModel);

        mImageView = findViewById(R.id.ChosenFilename);

        mProgressBar = findViewById(R.id.progress_circle_upload);

        notificationUpload = findViewById(R.id.goToMusic);

        //userProfileDp = findViewById(R.id.UserProfileDp);

        viewUploads = findViewById(R.id.goToUploads);
        viewUploadsHeadlines = findViewById(R.id.goToUploadsHeadlines);

        mProgressBar.setVisibility(View.INVISIBLE);

        //contentRater = findViewById(R.id.contentRating);

        /// contentRater.setEnabled(false);

        mToolbar = findViewById(R.id.appBarLayout);

        setSupportActionBar(mToolbar);

        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);

        //mProgressCircle.setVisibility(View.INVISIBLE);


        String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();

        VisiteduserId = on_maps_visited_user_id;


        String on_maps_visited_user_key = getIntent().getExtras().get("visit_user_key").toString();

        VisiteduserKey = on_maps_visited_user_key;

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowTitleEnabled(true);

        actionBar.setTitle("Sirocco upload");

        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View actionBarView = layoutInflater.inflate(R.layout.custom_bar_upload,null);

        actionBar.setCustomView(actionBarView);

        checkifAdmindTrue();

        // getUserInfor = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        mButtonUpload.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String on_maps_visited_user_key = getIntent().getExtras().get("visit_user_key").toString();

                VisiteduserKey = on_maps_visited_user_key;

                if(mUploadTask != null && mUploadTask.isInProgress())

                {
                    Toast.makeText(SirrocoImageUpload.this, "Sirocco-Image upload in progress !!", Toast.LENGTH_SHORT).show();
                }else
                {
                    mProgressBar.setVisibility(View.VISIBLE);

                    if(VisiteduserKey.compareTo("fromSirocco") == 0)
                    {
                        Upload();

                    }else if(VisiteduserKey.compareTo("fromSirocco") != 0 )
                    {
                        UploadTags();
                    }


                }
            }
        });

        viewUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.findViewById(R.id.goToUploads);

                if(mUploadTask != null && mUploadTask.isInProgress())

                {
                    Toast.makeText(SirrocoImageUpload.this, "Sirocco-Music upload in progress !!", Toast.LENGTH_SHORT).show();
                }else
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                    musicUpload();
                }
            }
        });

        viewUploadsHeadlines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.findViewById(R.id.goToUploadsHeadlines);

                if(mUploadTask != null && mUploadTask.isInProgress())

                {
                    Toast.makeText(SirrocoImageUpload.this, "SiroccoHeadlines upload in progress !!", Toast.LENGTH_SHORT).show();
                }else
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                    headlinesUpload();
                }
            }
        });



        notificationUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.findViewById(R.id.goToUploads);

                if(mUploadTask != null && mUploadTask.isInProgress())

                {
                    Toast.makeText(SirrocoImageUpload.this, "Sirocco-Music upload in progress !!", Toast.LENGTH_SHORT).show();
                }else
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                    uploadNotifications();
                }
            }
        });


    }

    private void uploadNotifications()
    {
        if(mImageUri != null) {

            Toast.makeText(SirrocoImageUpload.this, "PAGiiS Music uploading ...", Toast.LENGTH_SHORT).show();

            final String currentUserId = mAuth.getCurrentUser().getUid();
            //mProgressCircle.setVisibility(View.VISIBLE);
            //final String imageUrl = String.valueOf(mImageUri);
            final String saveRaterBarValue = String.valueOf(raterBarValue);



            mDatabaseRef = FirebaseDatabase.getInstance().getReference("WalkinWelcome");

            mStorageRef = FirebaseStorage.getInstance().getReference("WalkinWelcome");

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {



                        Uri finalImageUri;

                        String finalImageUrl;
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {


                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    finalImageUri = uri;
                                    finalImageUrl = String.valueOf(uri);

                                    SirrocoImageUploads upload = new SirrocoImageUploads(mEditTextFileName.getText().toString().trim(),
                                            finalImageUrl,VisiteduserId,currentUserId,mEditTextFileModel.getText().toString().trim());

                                    mDatabaseRef
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener()
                                            {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(SirrocoImageUpload.this, "PAGiiS music upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(SirrocoImageUpload.this,MemePage.class);
                                                    startActivity(intent);
                                                    // String uniqueKey = databaseReference.getKey();

                                                    //Create the function for Clearing/The ImageView Widget.
                                                }
                                            });




                                }
                            });

                            if(finalImageUrl == null)
                            {
                                finalImageUrl = String.valueOf(finalImageUri);
                            }

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
                            Toast.makeText(SirrocoImageUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
        } else
        {

            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkifAdmindTrue() {

        final String userId = mAuth.getCurrentUser().getUid();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.child("admin").getValue().equals(true))
                {
                    Intent intent = new Intent(SirrocoImageUpload.this, MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    private void openFileChooser() {
        Intent intent =  new  Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK   && data != null && data.getData() != null) {
            mImageUri = data.getData();

            RequestOptions options = new RequestOptions();
            Glide.with(getApplicationContext()).load(mImageUri).apply(options.centerCrop()).thumbnail(0.75f).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void musicUpload()
    {

        if (mImageUri != null) {

            Toast.makeText(SirrocoImageUpload.this, "PAGiiS notification uploading ...", Toast.LENGTH_SHORT).show();

            final String currentUserId = mAuth.getCurrentUser().getUid();
            //mProgressCircle.setVisibility(View.VISIBLE);
            //final String imageUrl = String.valueOf(mImageUri);
            final String saveRaterBarValue = String.valueOf(raterBarValue);

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoMusicUploads");

            mStorageRef = FirebaseStorage.getInstance().getReference("SiroccoMusicUploads");

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                        Uri finalImageUri;

                        String finalImageUrl;
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {

                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    finalImageUri = uri;

                                    finalImageUrl = String.valueOf(uri);


                                    SirrocoImageUploads upload = new SirrocoImageUploads(mEditTextFileName.getText().toString().trim(),
                                            finalImageUrl,VisiteduserId,currentUserId,mEditTextFileModel.getText().toString().trim());

                                    mDatabaseRef
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener()
                                            {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(SirrocoImageUpload.this, "PAGiiS music upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(SirrocoImageUpload.this,MemePage.class);
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


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SirrocoImageUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
        } else
        {

            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void headlinesUpload()
    {

        if (mImageUri != null)
        {
            Toast.makeText(SirrocoImageUpload.this, "PAGiiS Headlines uploading ...", Toast.LENGTH_SHORT).show();

            final String currentUserId = mAuth.getCurrentUser().getUid();

            //mProgressCircle.setVisibility(View.VISIBLE);

            //final String imageUrl = String.valueOf(mImageUri);

            final String saveRaterBarValue = String.valueOf(raterBarValue);

            nDatabaseRef = FirebaseDatabase.getInstance().getReference("WalkinWall");

            mStorageRef = FirebaseStorage.getInstance().getReference("WalkinWall");

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                        Uri finalImageUri;

                        String finalImageUrl;
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    finalImageUri = uri;
                                    finalImageUrl = String.valueOf(uri);



                                    SirrocoImageUploads upload = new SirrocoImageUploads(mEditTextFileName.getText().toString().trim(),
                                            finalImageUrl,VisiteduserId,currentUserId,mEditTextFileModel.getText().toString().trim());

                                    nDatabaseRef
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener()
                                            {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(SirrocoImageUpload.this, "PAGiiS Headlines upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(SirrocoImageUpload.this,MemePage.class);
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
                            Toast.makeText(SirrocoImageUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
        } else
        {

            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void Upload()
    {

        if (mImageUri != null) {

            Toast.makeText(SirrocoImageUpload.this, "PAGiiS image uploading ...", Toast.LENGTH_SHORT).show();

            final String currentUserId = mAuth.getCurrentUser().getUid();

            //mProgressCircle.setVisibility(View.VISIBLE);

            //final String imageUrl = String.valueOf(mImageUri);

            final String saveRaterBarValue = String.valueOf(raterBarValue);

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoUploads");

            mStorageRef = FirebaseStorage.getInstance().getReference("SiroccoUploads");

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                        Uri finalImageUri;

                        String finalImageUrl;
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    finalImageUri = uri;

                                    finalImageUrl = String.valueOf(uri);


                                    //Toast.makeText(ActivityUploadImage.this, "PAGiiS file selection successful .", Toast.LENGTH_SHORT).show();
                                    SirrocoImageUploads upload = new SirrocoImageUploads(mEditTextFileName.getText().toString().trim(),
                                            finalImageUrl,VisiteduserId,currentUserId,mEditTextFileModel.getText().toString().trim());

                                    mDatabaseRef
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener()
                                            {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(SirrocoImageUpload.this, "PAGiiS image upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(SirrocoImageUpload.this,MemePage.class);
                                                    startActivity(intent);
                                                    // String uniqueKey = databaseReference.getKey();

                                                    //Create the function for Clearing/The ImageView Widget.
                                                }
                                            });


                                }
                            });


                            if(finalImageUrl == null)
                            {
                                finalImageUrl = String.valueOf(finalImageUri);
                            }

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
                            Toast.makeText(SirrocoImageUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
        } else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }




    private void UploadTags()
    {

        if (mImageUri != null) {

            Toast.makeText(SirrocoImageUpload.this, "PAGiiS image uploading ...", Toast.LENGTH_SHORT).show();

            final String currentUserId = mAuth.getCurrentUser().getUid();

            //mProgressCircle.setVisibility(View.VISIBLE);

            //final String imageUrl = String.valueOf(mImageUri);

            String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();

            VisiteduserId = on_maps_visited_user_id;



            String on_maps_visited_user_key = getIntent().getExtras().get("visit_user_key").toString();

            VisiteduserKey = on_maps_visited_user_key;




            final String saveRaterBarValue = String.valueOf(raterBarValue);

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoUploadTags");

            mStorageRef = FirebaseStorage.getInstance().getReference("SiroccoUploadTags");

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                        Uri finalImageUri;

                        String finalImageUrl;
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    finalImageUri = uri;

                                    finalImageUrl = String.valueOf(uri);


                                    //Toast.makeText(ActivityUploadImage.this, "PAGiiS file selection successful .", Toast.LENGTH_SHORT).show();
                                    SirrocoImageUploads upload = new SirrocoImageUploads(mEditTextFileName.getText().toString().trim(),
                                            finalImageUrl,VisiteduserId,currentUserId,mEditTextFileModel.getText().toString().trim());

                                    mDatabaseRef.child(VisiteduserKey)
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener()
                                            {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(SirrocoImageUpload.this, "PAGiiS image upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(SirrocoImageUpload.this,SiroccoPage.class);
                                                    startActivity(intent);
                                                    // String uniqueKey = databaseReference.getKey();

                                                    //Create the function for Clearing/The ImageView Widget.
                                                }
                                            });


                                }
                            });


                            if(finalImageUrl == null)
                            {
                                finalImageUrl = String.valueOf(finalImageUri);
                            }

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
                            Toast.makeText(SirrocoImageUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
        } else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}
