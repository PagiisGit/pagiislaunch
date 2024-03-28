package pagiisnet.pagiisnet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityCamera extends AppCompatActivity {

    private ImageView cameraStartButton;

    private ImageView cameraImageView;

    //private ProgressDialog progressDialog;

    private SimpleDateFormat simpleDateFormat;

    private String postTimeDateStamp;

    private DatabaseReference mDatabaseRef_x;

    private Calendar calendar;

    private  DatabaseReference locationDetails;

    private  String myLastLocationDetails;

    private EditText mEditTextFileName;

    private StorageTask mUploadTask;

    private  String VisiteduserId;

    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private String raterBarValue;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private  static final int CAMERA_REQUEST_CODE = 1;

    private StorageReference mStorageRef;

    private DatabaseReference mDatabaseRef;

    private String MyName;

    private FirebaseAuth mAuth;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS= 7;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Camera upload");


        mAuth = FirebaseAuth.getInstance();


        mProgressBar = findViewById(R.id.progress_circle_camera);

        mProgressBar.setVisibility(View.INVISIBLE);

        locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation");


        String on_maps_visited_user_id = getIntent().getExtras().get("idUrl").toString();

        VisiteduserId = on_maps_visited_user_id;


        calendar = Calendar.getInstance();
        simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

        //progressDialog = new ProgressDialog(this);

        cameraImageView = findViewById(R.id.cameraImageView);
        cameraStartButton = findViewById(R.id.uploadChosenFile);
        mEditTextFileName = findViewById(R.id.chosenTitleEdit);

        cameraStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.findViewById(R.id.uploadChosenFile);
                if(mUploadTask != null && mUploadTask.isInProgress())
                {
                    Toast.makeText(ActivityCamera.this, "PAGiiS image upload in progress !!", Toast.LENGTH_SHORT).show();
                }else
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                    Upload();
                }
            }
        });
        //This method is used for the retrievl of the imageUri Field

        checkAndroidVersion();
        myLastLocationDetailsMetod();

        ifValueTure();


    }

    private void ifValueTure()
    {

        if(VisiteduserId.compareTo("null")!=0)
        {
            RequestOptions options = new RequestOptions();

            Glide.with(getApplicationContext()).load(VisiteduserId).apply(options.centerCrop()).thumbnail(0.65f).into(cameraImageView);

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
                Toast.makeText(ActivityCamera.this, "last location not found"+"Results"+":"+e, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    private void checkAndroidVersion()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();

        } else
        {
            Toast.makeText(this, "Please consider manual camera configurations for picture capturing  ", Toast.LENGTH_SHORT).show();
            // code for lollipop and pre-lollipop devices
        }
    }

    private Boolean checkAndRequestPermissions()
    {

        int camera = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }



    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        Log.d("in fragment on request", "Permission callback called-------");
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for both permissions
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                    // process the normal flow
                    //else any one or both the permissions are not granted
                } else {
                    Log.d("in fragment on request", "Some permissions are not granted ask again ");
                    //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                    //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        showDialogOK("Camera and Storage Permission required for this app",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                // proceed with logic by disabling the related features or quit the app.
                                                break;
                                        }
                                    }
                                });
                    }
                    //permission is denied (and never ask again is  checked)
                    //shouldShowRequestPermissionRationale will return false
                    else {
                        Toast.makeText(getApplicationContext(), "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                .show();
                        //                            //proceed with logic by disabling the related features or quit the app.
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uploads_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add)
        {

            StartCamera();


        }

        return super.onOptionsItemSelected(item);

    }

    private void StartCamera()
    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == CAMERA_REQUEST_CODE && resultCode == ActivityCamera.RESULT_OK && data != null && data.getData() != null )
        {

            mImageUri = data.getData();

            VisiteduserId = String.valueOf(mImageUri);

            RequestOptions options = new RequestOptions();

            Glide.with(getApplicationContext()).load(mImageUri).apply(options.centerCrop()).thumbnail(0.65f).into(cameraImageView);



            /*Intent intent = new Intent(ActivityCamera.this,MemePage.class);
            intent.putExtra("idUrl",VisiteduserId);
            startActivity(intent);*/



            // progressDialog.setMessage("PAGiiS preparing image upload...");
            // progressDialog.show();



            //Toast.makeText(this, "This Is the Image returned ! " + mImageUri, Toast.LENGTH_SHORT).show();

            //Bitmap photo = (Bitmap) data.getExtras().get(String.valueOf(mImageUri));
            //cameraImageView.setImageBitmap(photo);

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void Upload() {

        if ( VisiteduserId.compareTo("null")!=0) {

            final String currentUserId = mAuth.getCurrentUser().getUid();

            final String imageUrl = String.valueOf(mImageUri);


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




            calendar = Calendar.getInstance();
            simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
            postTimeDateStamp = simpleDateFormat.format(calendar.getTime());


            mStorageRef =  FirebaseStorage.getInstance().getReference().child("uploads");
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("uploads");

            final String saveRaterBarValue = String.valueOf(raterBarValue);


            //StorageReference fileReference = mStorageRef.child(String.valueOf(System.currentTimeMillis()));

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                Uri finalImageUri;

                                String finalImageUrl;

                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    finalImageUri = uri;
                                    finalImageUrl = String.valueOf( uri);


                                    Toast.makeText(ActivityCamera.this, "Upload successful", Toast.LENGTH_LONG).show();
                                    ImageUploads upload = new ImageUploads(mEditTextFileName.getText().toString().trim(),
                                            finalImageUrl, saveRaterBarValue,currentUserId,currentUserId,currentUserId,currentUserId,postTimeDateStamp,myLastLocationDetails,MyName);

                                    mDatabaseRef.child(currentUserId)
                                            .push()
                                            .setValue(upload, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {


                                                    Toast.makeText(ActivityCamera.this, "PAGiiS image upload successful !!", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    Intent intent = new Intent(ActivityCamera.this,MemePage.class);
                                                    startActivity(intent);
                                                    String uniqueKey = databaseReference.getKey();
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
                            Toast.makeText(ActivityCamera.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
        } else
        {
            StartCamera();
            Toast.makeText(this, " No Image selected !", Toast.LENGTH_SHORT).show();
        }
    }
}
