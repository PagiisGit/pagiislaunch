package pagiisnet.pagiisnet;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
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

public class ActivityUploadImage extends AppCompatActivity
{
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;


    private Uri finalPicUri;

    private ImageView mButtonChooseImage;

    private FloatingActionButton mButtonUpload;

    private DatabaseReference getUserProfileDataRef;

    private DatabaseReference checkAndminTrue;

    private String raterBarValue;


    private String uploadTimeValue;


    private Calendar calendar;

    private  DatabaseReference locationDetails;

    private  String myLastLocationDetails;


    private EditText mEditTextFileName;

    private FloatingActionButton contentRater;

    private ImageView viewUploads;

    //private ProgressBar mProgressCircle;

    private ImageView mImageView;

    private ImageView userProfileDp;

    private SimpleDateFormat simpleDateFormat;

    private String postTimeDateStamp;

    private ProgressBar mProgressBar;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_x;

    private StorageReference mStorageRef;

    private StorageTask mUploadTask;

    private  String  shareItemId;
    private  String  MyName;


    // private Uri resultUri;

    private FirebaseAuth mAuth;

    private  DatabaseReference getUserInfor;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        FirebaseApp.initializeApp(this);

        mButtonChooseImage = findViewById(R.id.chooseUpload);

        mButtonUpload = findViewById(R.id.uploadChosenFile);

        mEditTextFileName = findViewById(R.id.chosenTitleEdit);

        mImageView = findViewById(R.id.ChosenFilename);

        userProfileDp = findViewById(R.id.writeStatus);

        mProgressBar = findViewById(R.id.progress_circle_upload);

        //userProfileDp = findViewById(R.id.UserProfileDp);

        viewUploads = findViewById(R.id.goToUploads);

        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
        getUserProfileDataRef.keepSynced(true);

        mProgressBar.setVisibility(View.INVISIBLE);

        contentRater = findViewById(R.id.contentRating);

        contentRater.setVisibility(View.INVISIBLE);

        contentRater.setEnabled(false);

        locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation");


        calendar  = Calendar.getInstance();

        simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");


        //simpleDateFormat.format(calendar.getTime());

        mToolbar = findViewById(R.id.appBarLayout);

        setSupportActionBar(mToolbar);

        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);



        actionBar.setDisplayShowHomeEnabled(true);

        //mProgressCircle.setVisibility(View.INVISIBLE);

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowTitleEnabled(true);

        actionBar.setTitle("Gallery Upload");

        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View actionBarView = layoutInflater.inflate(R.layout.custom_bar_upload,null);

        actionBar.setCustomView(actionBarView);

        checkifAdmindTrue();


        //shareItemId = getIntent().getExtras().get("share_item_id").toString();

        // getUserInfor = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.ChosenFilename);
                openFileChooser();
            }
        });



        if(shareItemId != null && shareItemId.compareTo("null")!=0 )
        {
            RequestOptions options = new RequestOptions();
            Glide.with(getApplicationContext()).load(shareItemId).apply(options.centerCrop()).thumbnail(0.65f).into(mImageView);
        }

        mButtonUpload.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

                postTimeDateStamp = simpleDateFormat.format(calendar.getTime());

                v.findViewById(R.id.uploadChosenFile);

                if(mUploadTask != null && mUploadTask.isInProgress())

                {
                    Toast.makeText(ActivityUploadImage.this, "PAGiiS image upload in progress !!", Toast.LENGTH_SHORT).show();


                }else
                {

                    final String own_user_id = mAuth.getUid();

                    mProgressBar.setVisibility(View.VISIBLE);

                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

                    mDatabaseRef.child(own_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            if(dataSnapshot.getChildrenCount() <= 100)
                            {
                                Upload();
                            }else
                            {
                                Toast.makeText(ActivityUploadImage.this, "PAGiiS status upload-max reached !!"+"\n"+"Please delete some of your posts and repost", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        });

        viewUploads.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                v.findViewById(R.id.goToUploads);
                Intent intent = new Intent(ActivityUploadImage.this, MemePage.class);
                startActivity(intent);
            }
        });

        contentRater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.contentRating);
                Intent intent = new Intent(ActivityUploadImage.this,SiroccoOnlineStores.class);
                intent.putExtra("visit_user_id", "fromSirocco");
                startActivity(intent);
            }
        });

        myLastLocationDetailsMetod();


        String onlineUserId = mAuth.getCurrentUser().getUid();


        getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //String name = dataSnapshot.child("userNameAsEmail").getValue().toString();
                //String userStatus = dataSnapshot.child("userDefaultStatus").getValue().toString();

                String myDpUrl = dataSnapshot.child("userImageDp").getValue().toString();
                //userStatusMessage  = userStatus;
                RequestOptions options = new RequestOptions();

                Glide.with(getApplicationContext()).load(myDpUrl).apply(options.centerCrop()).thumbnail(0.65f).into(userProfileDp);
                //myName = name;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                Toast.makeText(ActivityUploadImage.this, "last location not found"+"Results"+":"+e, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void checkifAdmindTrue()
    {

        final String userId = mAuth.getCurrentUser().getUid();
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        mDatabaseRef_x.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.hasChild("admin") && dataSnapshot.child("admin").getValue().equals(true))
                {
                    contentRater.setEnabled(true);
                    contentRater.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                Toast.makeText(ActivityUploadImage.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
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

    private void openFileChooser()
    {

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

            finalPicUri = mImageUri;

            contentRater.setEnabled(true);

            RequestOptions options = new RequestOptions();

            Glide.with(getApplicationContext()).load(mImageUri).apply(options.centerCrop()).thumbnail(0.65f).into(mImageView);
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
        if (mImageUri != null )
        {
            final String raterBarValueDefault = "userDefaultDp";

            final String currentUserId = mAuth.getCurrentUser().getUid();

            mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

            mDatabaseRef_x.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                    raterBarValue = dataSnapshot.child("userImageDp").getValue().toString();
                    MyName = dataSnapshot.child("userNameAsEmail").getValue().toString();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Toast.makeText(ActivityUploadImage.this, "PAGiiS image uploading ...", Toast.LENGTH_SHORT).show();

            //mProgressCircle.setVisibility(View.VISIBLE);

            //final String imageUrl = String.valueOf(mImageUri);

            final String saveRaterBarValue = raterBarValue;

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

            mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            if(mUploadTask != null && !mUploadTask.isInProgress())
            {
                Toast.makeText(this, "Image upload in progress.", Toast.LENGTH_SHORT).show();
            }else
            {

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


                                        ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(),finalImageUrl,raterBarValue,currentUserId,raterBarValueDefault,raterBarValueDefault,raterBarValueDefault,postTimeDateStamp,myLastLocationDetails,MyName);
                                        mDatabaseRef.child(currentUserId)
                                                .push()
                                                .setValue(upload, new DatabaseReference.CompletionListener()
                                                {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError,
                                                                           DatabaseReference databaseReference) {

                                                        //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                        Toast.makeText(ActivityUploadImage.this, "PAGiiS image upload successful !!", Toast.LENGTH_SHORT).show();


                                                        mProgressBar.setVisibility(View.INVISIBLE);
                                                        Intent intent = new Intent(ActivityUploadImage.this, MemePage.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
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

                                TextView messageGrid = findViewById(R.id.customToastText);
                                // Initiate the Toast instance.
                                Toast toast = new Toast(getApplicationContext());

                                messageGrid.setText("Pagiis failed to upload, please check Internet connections.");
                                // Set custom view in toast.
                                toast.setView(toastView);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0,0);
                                toast.show();
                                finish();


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


        } else if(mImageUri == null)
         {


             raterBarValue = "userDefaultDp";

             String own_current_id = mAuth.getCurrentUser().getUid();

            ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(),shareItemId,raterBarValue,own_current_id,raterBarValue,raterBarValue,raterBarValue,"","","");
            mDatabaseRef.child(own_current_id)
                    .push()
                    .setValue(upload, new DatabaseReference.CompletionListener()
                    {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {

                            //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                            Toast.makeText(ActivityUploadImage.this, "PAGiiS image upload successful !!", Toast.LENGTH_SHORT).show();


                            mProgressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(ActivityUploadImage.this, MemePage.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            // String uniqueKey = databaseReference.getKey();
                            //Create the function for Clearing/The ImageView Widget.
                        }
                    });

        } else
         {
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
         }
    }

}
