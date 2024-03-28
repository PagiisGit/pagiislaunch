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
import android.widget.LinearLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import pagiisnet.pagiisnet.R;

public class SellItemPost extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;

    private ImageView mButtonChooseImage;

    private ImageView mButtonUpload;

    private String storeLogo;


    private EditText mSearchEditText;
    private ImageView searchForResults;

    private ImageView openSearchbar;

    private LinearLayout searchInputLayout;

    private FloatingActionButton notificationUpload;


    private DatabaseReference checkAndminTrue;

    private final int raterBarValue = 0;

    private EditText mEditTextFileName;

    private String VisiteduserId;


    private String sellinStoreKey;

    private EditText mEditTextFileModel;

    private ImageView viewUploads;

    private ImageView viewUploadsHeadlines;

    private ImageView storeLogoIcon;


    //private ProgressBar mProgressCircle;

    private ImageView mImageView;

    private ImageView userProfileDp;

    private ProgressBar mProgressBar;
    private ProgressBar mProgressBarPro;

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item_post);



        mButtonChooseImage = findViewById(R.id.chooseUpload);

        mButtonUpload = findViewById(R.id.uploadChosenFile);

        mEditTextFileName = findViewById(R.id.chosenTitleEdit);

        mEditTextFileModel = findViewById(R.id.chosenTitleModel);

        mImageView = findViewById(R.id.ChosenFilename);

        mProgressBar = findViewById(R.id.progress_circle_upload);
        mProgressBarPro = findViewById(R.id.sellinStorePro);

        notificationUpload = findViewById(R.id.goToMusic);

        //userProfileDp = findViewById(R.id.UserProfileDp);

        viewUploads = findViewById(R.id.goToUploads);
        viewUploadsHeadlines = findViewById(R.id.goToUploadsHeadlines);


        mSearchEditText = findViewById(R.id.searchEdittext);
        searchForResults = findViewById(R.id.LogSearchIconGo);
        openSearchbar = findViewById(R.id.logSearchIcon);

        storeLogoIcon = findViewById(R.id.sellinStoreIcon);

        searchInputLayout = findViewById(R.id.searchTextInputLayout);

        //searchInputLayout.setVisibility(View.INVISIBLE);
        //searchInputLayout.setEnabled(false);

        mProgressBar.setEnabled(false);


        mProgressBar.setVisibility(View.INVISIBLE);

        mProgressBarPro.setEnabled(false);

        mProgressBarPro.setVisibility(View.INVISIBLE);

        String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();
        VisiteduserId = on_maps_visited_user_id;

        //contentRater = findViewById(R.id.contentRating);

        /// contentRater.setEnabled(false);

        mToolbar = findViewById(R.id.appBarLayout);

        setSupportActionBar(mToolbar);

        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);

        //mProgressCircle.setVisibility(View.INVISIBLE);

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowTitleEnabled(true);

        actionBar.setTitle("@Sir_occo #online_shopping");

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

                if(mUploadTask != null && mUploadTask.isInProgress())

                {
                    Toast.makeText(SellItemPost.this, "Sirocco-Image upload in progress !!", Toast.LENGTH_SHORT).show();
                }else
                {
                    mProgressBar.setEnabled(true);
                    mProgressBar.setVisibility(View.VISIBLE);
                    Upload();
                }
            }
        });

        viewUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.findViewById(R.id.goToUploads);

                if(mUploadTask != null && mUploadTask.isInProgress())

                {
                    Toast.makeText(SellItemPost.this, "Sirocco-Music upload in progress !!", Toast.LENGTH_SHORT).show();
                }else
                {
                    mProgressBar.setEnabled(true);
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
                    Toast.makeText(SellItemPost.this, "SiroccoHeadlines upload in progress !!", Toast.LENGTH_SHORT).show();
                }else
                {
                    mProgressBar.setEnabled(true);
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
                    Toast.makeText(SellItemPost.this, "Sirocco-Music upload in progress !!", Toast.LENGTH_SHORT).show();
                }else
                {
                    mProgressBar.setEnabled(true);
                    mProgressBar.setVisibility(View.VISIBLE);
                    uploadNotifications();
                }
            }
        });



        searchForResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.LogSearchIconGo);

                String searchedText = mSearchEditText.getText().toString();

                mProgressBarPro.setEnabled(true);

                mProgressBarPro.setVisibility(View.VISIBLE);



                mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoOnlineStores");
                Query mSearchQuery = mDatabaseRef.orderByChild("name").startAt(searchedText).endAt(searchedText + "\uf8ff");
                final String userIdRef = mAuth.getCurrentUser().getUid();

                mSearchQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {

                            RequestOptions options = new RequestOptions();

                            sellinStoreKey =String.valueOf(dataSnapshot.getKey());
                            VisiteduserId = sellinStoreKey;

                            storeLogo = dataSnapshot.child("imageUrlSHopICon").getValue().toString();
                            Glide.with(SellItemPost.this).load(storeLogo).apply(options.centerCrop()).thumbnail(0.75f).into(storeLogoIcon);

                            mProgressBarPro.setEnabled(false);

                            mProgressBarPro.setVisibility(View.INVISIBLE);



                        }else
                        {

                            Toast.makeText(SellItemPost.this, "Pagiis online posts currently unavailable.", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SellItemPost.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }



        });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void uploadNotifications()
    {
        if (mImageUri != null && !storeLogo.isEmpty() && !sellinStoreKey.isEmpty()) {

            Toast.makeText(SellItemPost.this, "Sirocco item sale in progress ...", Toast.LENGTH_LONG).show();

            final String currentUserId = mAuth.getCurrentUser().getUid();
            //mProgressCircle.setVisibility(View.VISIBLE);
            //final String imageUrl = String.valueOf(mImageUri);
            final String saveRaterBarValue = "Sirocco_*art*";

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoOnSaleItem");

            mStorageRef = FirebaseStorage.getInstance().getReference("SiroccoOnSaleItem").child("Art");

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

                                    finalImageUrl = String.valueOf(finalImageUri);

                                    ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(),
                                            finalImageUrl,"Art",currentUserId,mEditTextFileModel.getText().toString().trim(),saveRaterBarValue,saveRaterBarValue,"","",VisiteduserId);

                                    mDatabaseRef
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener()
                                            {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(SellItemPost.this, "@Siocco Art Item upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setEnabled(false);
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(SellItemPost.this,Art.class);
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
                            Toast.makeText(SellItemPost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkifAdmindTrue() {

        final String userId = mAuth.getCurrentUser().getUid();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.child("admin").getValue().equals(true))
                {
                    Intent intent = new Intent(SellItemPost.this, MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void musicUpload()
    {
        if (mImageUri != null && !storeLogo.isEmpty() && !sellinStoreKey.isEmpty()) {

            Toast.makeText(SellItemPost.this, "Sirocco item sale in progress ...", Toast.LENGTH_LONG).show();

            final String currentUserId = mAuth.getCurrentUser().getUid();
            //mProgressCircle.setVisibility(View.VISIBLE);
            //final String imageUrl = String.valueOf(mImageUri);
            final String saveRaterBarValue = "Sirocco_*music*";

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoOnSaleItem");

            mStorageRef = FirebaseStorage.getInstance().getReference("SiroccoOnSaleItem").child("Music");

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

                                    ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(),
                                            String.valueOf(uri),"Music",currentUserId,"",saveRaterBarValue,saveRaterBarValue,"",mEditTextFileModel.getText().toString().trim(),VisiteduserId);

                                    mDatabaseRef
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener()
                                            {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(SellItemPost.this, "item upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setEnabled(false);
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(SellItemPost.this,Music.class);
                                                    startActivity(intent);
                                                    // String uniqueKey = databaseReference.getKey();

                                                    //Create the function for Clearing/The ImageView Widget.
                                                }
                                            });


                                }
                            });


                            finalImageUrl = String.valueOf(finalImageUri);

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
                            Toast.makeText(SellItemPost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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







    ////////////////////////////////////////////////////////////////////////////////////////

    private void headlinesUpload()
    {

        if (mImageUri != null&& !storeLogo.isEmpty() && !sellinStoreKey.isEmpty())
        {
            Toast.makeText(SellItemPost.this, "Sirocco item sale in progress ...", Toast.LENGTH_LONG).show();

            final String currentUserId = mAuth.getCurrentUser().getUid();

            //mProgressCircle.setVisibility(View.VISIBLE);

            //final String imageUrl = String.valueOf(mImageUri);

            final String saveRaterBarValue = "Sirocco_*business*";

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoOnSaleItem");

            mStorageRef = FirebaseStorage.getInstance().getReference("SiroccoOnSaleItem").child("business");

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

                                    ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(),
                                            String.valueOf(uri),"Business",currentUserId,"",saveRaterBarValue,saveRaterBarValue,"",mEditTextFileModel.getText().toString().trim(),VisiteduserId);

                                    mDatabaseRef
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener()
                                            {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(SellItemPost.this, "item upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setEnabled(false);
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(SellItemPost.this,Business.class);
                                                    startActivity(intent);
                                                    // String uniqueKey = databaseReference.getKey();
                                                    //Create the function for Clearing/The ImageView Widget.
                                                }
                                            });

                                }
                            });

                            finalImageUrl = String.valueOf(finalImageUri);

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
                            Toast.makeText(SellItemPost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void Upload() {
        if (mImageUri != null  && !storeLogo.isEmpty() && !sellinStoreKey.isEmpty()) {

            Toast.makeText(SellItemPost.this, "Sirocco item sale in progress ....", Toast.LENGTH_LONG).show();

            final String currentUserId = mAuth.getCurrentUser().getUid();

            final String saveRaterBarValue = "Sirocco_*fashion*";

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoOnSaleItem");

            mStorageRef = FirebaseStorage.getInstance().getReference("SiroccoOnSaleItem").child("Fashion");

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



                                    finalImageUrl = String.valueOf(finalImageUri);

                                    ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(),
                                            finalImageUrl,"Fashion",currentUserId,"",saveRaterBarValue,saveRaterBarValue,"",mEditTextFileModel.getText().toString().trim(),VisiteduserId);

                                    mDatabaseRef
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener()
                                            {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(SellItemPost.this, "Item upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setEnabled(false);
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(SellItemPost.this,Fashion.class);
                                                    startActivity(intent);

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
                            Toast.makeText(SellItemPost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
