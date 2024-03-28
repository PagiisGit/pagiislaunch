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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.List;

public class EventPreviewEdit extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;

    private ImageView mButtonChooseImage;

    private ImageView mButtonUpload;

    private DatabaseReference checkAndminTrue;

    private String raterBarValue;

    private EditText mEditTextFileName;

    private FloatingActionButton contentRater;

    //private ImageView viewUploads;

    //private ProgressBar mProgressCircle;

    private ImageView mImageView;

    private ImageView userProfileDp;

    private List<tags> tagedUsers;

    private ProgressBar mProgressBar;

    private String welcomeNoneFinal;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_y;
    private DatabaseReference mDatabaseRef_x;
    private DatabaseReference mDatabaseRef_z;
    private DatabaseReference mDatabaseRef_yx;
    private DatabaseReference mDatabaseRef_yz;

    private StorageReference mStorageRef;

    private String eventVisitedId;

    private String on_maps_visited_user_id_from;

    private StorageTask mUploadTask;

    private  String  shareItemId;




    // private Uri resultUri;

    private FirebaseAuth mAuth;

    private  DatabaseReference getUserInfor;

    private DatabaseReference mDatabaseEventAttendance;


    @SuppressLint("RestrictedApi")

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_preview_edit);
        mButtonChooseImage = findViewById(R.id.chooseUpload);

        mButtonUpload = findViewById(R.id.uploadChosenFile);

        mEditTextFileName = findViewById(R.id.chosenTitleEdit);

        mImageView = findViewById(R.id.ChosenFilename);

        mProgressBar = findViewById(R.id.progress_circle_upload);

        mDatabaseEventAttendance =FirebaseDatabase.getInstance().getReference("EventAttendance");

        //userProfileDp = findViewById(R.id.UserProfileDp);

        //viewUploads = findViewById(R.id.goToUploads);

        mProgressBar.setVisibility(View.INVISIBLE);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("PagiisNotifications");

        contentRater = findViewById(R.id.contentRating);

        contentRater.setVisibility(View.INVISIBLE);

        contentRater.setEnabled(false);

        tagedUsers= new ArrayList<>();

        mToolbar = findViewById(R.id.appBarLayout);

        setSupportActionBar(mToolbar);

        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);

        //mProgressCircle.setVisibility(View.INVISIBLE);

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowTitleEnabled(true);


        final String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();


        on_maps_visited_user_id_from = getIntent().getExtras().get("from").toString();

        final String on_maps_visited_user_id_eventDP = getIntent().getExtras().get("eventDp").toString();
        final String on_maps_visited_user_id__eventName = getIntent().getExtras().get("visit_user_id").toString();
        final String on_maps_visited_user_id__userId = getIntent().getExtras().get("invite_id").toString();

        final String on_maps_visited_user_id_invite_name = getIntent().getExtras().get("inviteName").toString();
        final String on_maps_visited_user_id_inviteDP = getIntent().getExtras().get("inviteProfileUrl").toString();
        final String on_maps_visited_user_id_eventLocation = getIntent().getExtras().get("event_location").toString();

        eventVisitedId = on_maps_visited_user_id;

        actionBar.setTitle("Event Uploads");

        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View actionBarView = layoutInflater.inflate(R.layout.custom_bar_upload,null);

        actionBar.setCustomView(actionBarView);

        checkifAdmindTrue();


        getInviteInfor();



        shareItemId = getIntent().getExtras().get("share_item_id").toString();

        // getUserInfor = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.ChosenFilename);
                openFileChooser();
            }
        });



        if(shareItemId.compareTo("null")!=0 && shareItemId != null)
        {
            RequestOptions options = new RequestOptions();
            Glide.with(getApplicationContext()).load(shareItemId).apply(options.centerCrop()).thumbnail(0.65f).into(mImageView);
        }

        mButtonUpload.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                v.findViewById(R.id.uploadChosenFile);

                if(mUploadTask != null && mUploadTask.isInProgress())

                {
                    Toast.makeText(EventPreviewEdit.this, "PAGiiS event loading!!", Toast.LENGTH_SHORT).show();

                }else
                {

                    final String own_user_id = mAuth.getUid();

                    mProgressBar.setVisibility(View.VISIBLE);

                    mDatabaseRef_yx = FirebaseDatabase.getInstance().getReference("EventUploads");

                    mDatabaseRef_yx.child(on_maps_visited_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            if(dataSnapshot.getChildrenCount() <= 10000)
                            {
                                Upload();
                            }else
                            {
                                Toast.makeText(EventPreviewEdit.this, "Event status upload-max reached !!"+"\n"+"Please delete some of your posts and repost", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
                            Toast.makeText(EventPreviewEdit.this, "upload canclled !", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });

                }
            }
        });

       /* viewUploads.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                v.findViewById(R.id.goToUploads);
                Intent intent = new Intent(EventPreviewEdit.this, MemePage.class);
                startActivity(intent);
            }
        });*/

        contentRater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(EventPreviewEdit.this, " Pagiis live events  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getInviteInfor()

    {

        mDatabaseRef_yz = FirebaseDatabase.getInstance().getReference("PagiisLiveEvents").child(eventVisitedId);


        mDatabaseRef_yz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String eventName=  dataSnapshot.child("eventName").getValue().toString();
                String eventProfilePic =  dataSnapshot.child("eventName").getValue().toString();
                String inviteName=  dataSnapshot.child("eventName").getValue().toString();
                String inviteProfile=  dataSnapshot.child("eventName").getValue().toString();
                String inviteUserId=  dataSnapshot.child("eventName").getValue().toString();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }


    private void checkifAdmindTrue()
    {

        final String userId = mAuth.getCurrentUser().getUid();
        mDatabaseRef_yz = FirebaseDatabase.getInstance().getReference().child("PagiisLiveEvents").child("storeAdminId");

        mDatabaseRef_yz.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("admin") && dataSnapshot.child("admin").getValue().toString().compareTo(userId) == 0)
                    {
                        contentRater.setEnabled(true);
                        contentRater.setVisibility(View.VISIBLE);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                Toast.makeText(EventPreviewEdit.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void Upload() {
        if (mImageUri != null && !mEditTextFileName.getText().toString().isEmpty())
        {

            welcomeNoneFinal = mEditTextFileName.getText().toString();

            mDatabaseRef_yx = FirebaseDatabase.getInstance().getReference("eventUpload").child(eventVisitedId);
            final String raterBarValueDefault = "userDefaultDp";

            final String currentUserId = mAuth.getCurrentUser().getUid();

            mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

            mDatabaseRef_x.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                    raterBarValue = dataSnapshot.child("userImageDp").getValue().toString();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });

            Toast.makeText(EventPreviewEdit.this, "PAGiiS image uploading ...", Toast.LENGTH_SHORT).show();

            //mProgressCircle.setVisibility(View.VISIBLE);

            //final String imageUrl = String.valueOf(mImageUri);

            final String saveRaterBarValue = raterBarValue;

            mDatabaseRef_yx = FirebaseDatabase.getInstance().getReference("EventUploads");

            mStorageRef = FirebaseStorage.getInstance().getReference("EventUploads");

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


                                        createEventFile upload = new createEventFile(mEditTextFileName.getText().toString().trim(),finalImageUrl,raterBarValue,currentUserId,raterBarValueDefault,raterBarValueDefault,raterBarValueDefault,mEditTextFileName.getText().toString().trim(),"","","","","","","","","live");
                                        mDatabaseRef_yx.child(eventVisitedId)
                                                .push()
                                                .setValue(upload, new DatabaseReference.CompletionListener()
                                                {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError,
                                                                           DatabaseReference databaseReference) {

                                                        //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                        Toast.makeText(EventPreviewEdit.this, "PAGiiS event ready !!", Toast.LENGTH_SHORT).show();

                                                        final String uploadId = databaseReference.getKey();


                                                        mDatabaseRef_yz.child("welcomeNote").setValue(welcomeNoneFinal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {

                                                                rippleToNearbyUsers();

                                                            }
                                                        });
                                                        /*mProgressBar.setVisibility(View.INVISIBLE);
                                                        Intent intent = new Intent(EventPreviewEdit.this, MemePage.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);*/


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


        } else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void rippleToNearbyUsers()
    {

        final String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();

        final String on_maps_visited_user_id_eventDP = getIntent().getExtras().get("eventDp").toString();
        final String on_maps_visited_user_id__eventName = getIntent().getExtras().get("visit_user_id").toString();
        final String on_maps_visited_user_id__userId = getIntent().getExtras().get("invite_id").toString();

        final String on_maps_visited_user_id_invite_name = getIntent().getExtras().get("inviteName").toString();
        final String on_maps_visited_user_id_inviteDP = getIntent().getExtras().get("inviteProfileUrl").toString();
        final String on_maps_visited_user_id_eventLocation = getIntent().getExtras().get("event_location").toString();

        if(!tagedUsers.isEmpty()) {

            for (int i = 0; i < tagedUsers.size(); i++) {
                tags x = tagedUsers.get(i);
                final String taged_id = x.getUser_tagID();

                mDatabaseRef = FirebaseDatabase.getInstance().getReference("PagiisNotifications").child(taged_id);
                //Query mSearchQuery = mDatabaseRef.orderByChild("name").startAt(searchedtext).endAt(searchedtext + "\uf8ff");
                final String userIdRef = mAuth.getCurrentUser().getUid();

                createEventFile upload = new createEventFile(on_maps_visited_user_id__eventName,on_maps_visited_user_id_eventDP, "Request", on_maps_visited_user_id__userId,"emailAddress","storecell" , "StoreBankinDetails ", "Just started a live event","","","","","",on_maps_visited_user_id_eventLocation,on_maps_visited_user_id_inviteDP,on_maps_visited_user_id_invite_name,"live");
                mDatabaseRef.child(userIdRef)
                        .push()
                        .setValue(upload, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   DatabaseReference databaseReference) {


                                mDatabaseEventAttendance.child(on_maps_visited_user_id).child(userIdRef).child("attendin").setValue("true");


                                mDatabaseEventAttendance.child(on_maps_visited_user_id).child(userIdRef).child("admin").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)

                                    {

                                        //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                        Toast.makeText(EventPreviewEdit.this, "event rippled succefully. !!", Toast.LENGTH_SHORT).show();


                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(EventPreviewEdit.this, ViewEvent.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        // String uniqueKey = databaseReference.getKey();
                                        //Create the function for Clearing/The ImageView Widget.

                                    }
                                });



                            }
                        });

            }

        }
    }
}
