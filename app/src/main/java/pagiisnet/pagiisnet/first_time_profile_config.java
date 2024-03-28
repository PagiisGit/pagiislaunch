package pagiisnet.pagiisnet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class first_time_profile_config extends AppCompatActivity {

    private DatabaseReference storeUserDefaultDataRef;

    private EditText enterAge;
    private ImageView savefromDialog;

    private String profileDPUrl;

    private ImageView setFirstTimeUserDP;

    private StorageReference userImageStorageDpRef;

    private DatabaseReference getUserProfileDataRef;

    private ProgressBar mProgressBar;

    private final int GotoMaps = 4;

    private TextInputLayout firstTimeUserNameEditLayout;

    private ImageView saveinfoButton;

    private  final static int  Gallery_Pick = 1;

    private  Uri resultUri;

    private static final int reUseMapkey = 2;
    private final boolean isMapsResponding = true;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private TextInputEditText setFacebookLink,setInstagramLink,setTwitterLink;
    private TextInputEditText setUserName;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_profile_config);

        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        mProgressBar = findViewById(R.id.progress_circle_own_profile);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //storeUserDefaultDataRef = FirebaseDatabase.getInstance().getReference().child("Users");

        setFirstTimeUserDP = findViewById(R.id.setFirstTimeDp);
        firstTimeUserNameEditLayout = findViewById(R.id.firstTimeTextInputLayout);
        saveinfoButton = findViewById(R.id.saveInfo);

        setUserName = findViewById(R.id.setFirstTimeUsername);
        mProgressBar.setEnabled(false);
        mProgressBar.setVisibility(View.INVISIBLE);


      /* setFirstTimeUserDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });*/

        String onlineUserID = getIntent().getExtras().get("visited_user_id").toString();

        saveinfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.equals(saveinfoButton))
                {
                    SaveInfor();
                }
            }
        });

        if(onlineUserID != null && !onlineUserID.isEmpty() && !(onlineUserID.compareTo("fromRegister") ==0))
        {

            openFileChooser();

        }

    }

    private void openFileChooser()
    {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }


    
   /* private void openAgeDialog() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getApplicationContext());
        View mView = getLayoutInflater().inflate(R.layout.age_dialog_view,null);

        enterAge = mView.findViewById(R.id.AgeEditText);
        savefromDialog  = mView.findViewById(R.id.saveFromDialog);

        String Age = enterAge.getText().toString();

        final int FinalAge = Integer.parseInt(Age);

        savefromDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveFromDialog);


                if(enterAge.getText() != null  && FinalAge >= 12 && FinalAge <= 100   )
                {

                    String myId = mAuth.getCurrentUser().getUid();

                    getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users").child(myId);

                    getUserProfileDataRef.child("Age").setValue(String.valueOf(FinalAge)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {

                            Intent mapsIntent = new Intent(first_time_profile_config.this, SplashIntroOne.class);
                            mapsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mapsIntent);
                            finish();

                        }
                    });


                }else
                {
                    Toast.makeText(getApplicationContext(), "Please type in your status or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }*/


    private void SaveInfor()

    {
        setUserName = findViewById(R.id.setFirstTimeUsername);

        //String onlineUserId = mAuth.getCurrentUser().getUid();

        //getUserProfileDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserId);

        //userImageStorageDpRef = FirebaseStorage.getInstance().getReference("dp_Images");

        //String currentUserid = mAuth.getInstance().getCurrentUser().getUid();
        //storeUserDefaultDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);

        //String facebookLink = setFacebookLink.getText().toString().trim();
        //String twitterLink = setTwitterLink.getText().toString().trim();
        //String instagramLink = setInstagramLink.getText().toString().trim();


            final String onlineUserId = mAuth.getCurrentUser().getUid();

            final String username = setUserName.getText().toString().trim();

        if (!TextUtils.isEmpty(username)) {

            getUserProfileDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserId);
            getUserProfileDataRef.child("userNameAsEmail").setValue(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    Intent mapsIntent = new Intent(first_time_profile_config.this, OnBoarding.class);
                    mapsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mapsIntent);
                    //Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                }
            });

        } else
            {
                Toast.makeText(this, "Pagiis username required! ", Toast.LENGTH_SHORT).show();
            }


    }

   /* private void saveAlternatively()

    {

        String onlineUserId = mAuth.getCurrentUser().getUid();

        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserId);

        userImageStorageDpRef = FirebaseStorage.getInstance().getReference("dp_Images");

        //String currentUserid = mAuth.getInstance().getCurrentUser().getUid();
        //storeUserDefaultDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);

        String username = setUserName.getText().toString().trim();

        //String facebookLink = setFacebookLink.getText().toString().trim();
        //String twitterLink = setTwitterLink.getText().toString().trim();
        //String instagramLink = setInstagramLink.getText().toString().trim();

        getUserProfileDataRef.child("userNameAsEmail").setValue(setUserName.getText().toString().trim());
        getUserProfileDataRef.child("facebookLink").setValue("facebook.com");
        getUserProfileDataRef.child("twitterLink").setValue("twitter.com");
        getUserProfileDataRef.child("instagramLink").setValue("instagramLink.com").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                Intent mapsIntent = new Intent(first_time_profile_config.this,SplashIntroOne.class);
                mapsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mapsIntent);
                //Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();

            }
        });


    }

  */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            resultUri = data.getData();
            //Glide.with(getApplicationContext()).load(resultUri).into(setFirstTimeUserDP);
            RequestOptions options = new RequestOptions();
            Glide.with(getApplicationContext()).load(resultUri).apply(options.centerCrop()).thumbnail(0.65f).into(setFirstTimeUserDP);

            if(mUploadTask != null && mUploadTask.isInProgress())
            {
                Toast.makeText(first_time_profile_config.this, "PAGiiS image upload in progress !!", Toast.LENGTH_SHORT).show();
            }else
            {
                //mProgressBar.setVisibility(View.VISIBLE);
                Upload();
            }

        }else
        {
            Toast.makeText(this, "file chooser failed! ", Toast.LENGTH_SHORT).show();
        }
    }


    private void Upload()
    {

        if (resultUri != null && !(resultUri.compareTo(Uri.parse("")) == 0))
        {

            String onlineUserId = mAuth.getCurrentUser().getUid();

            getUserProfileDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserId);

            userImageStorageDpRef = FirebaseStorage.getInstance().getReference().child("dp_Images");

            //final String imageUrl = String.valueOf(resultUri);

            final StorageReference storageReference = userImageStorageDpRef.child(resultUri.getLastPathSegment());

            mUploadTask = storageReference.putFile(resultUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                Uri finalImageUri;

                                String finalImageUrl;

                                @Override
                                public void onSuccess(Uri uri)
                                {


                                    finalImageUri = uri;

                                    finalImageUrl = String.valueOf(uri);


                                    profileDPUrl=  String.valueOf(uri);




                                    getUserProfileDataRef.child("userImageDp").setValue(finalImageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            finalImageUrl = String.valueOf( uri);

                                            Toast.makeText(first_time_profile_config.this, "Profile picture  uploaded successfully", Toast.LENGTH_LONG).show();
                                            //Glide.with(getApplicationContext()).load(resultUri).into(setFirstTimeUserDP);
                                            finish();
                                            //mProgressBar.setVisibility(View.INVISIBLE);
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
                            }, 500);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(first_time_profile_config.this, "failed to upload profile, please try aain ", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(first_time_profile_config.this,first_time_profile_config.class);
                            startActivity(intent);

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
