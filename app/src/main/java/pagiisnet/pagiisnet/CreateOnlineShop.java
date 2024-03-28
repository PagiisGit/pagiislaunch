package pagiisnet.pagiisnet;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
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

public class CreateOnlineShop extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;
    private EditText shopName;
    private EditText shopAdmin;
    private EditText shopCell;
    private EditText shopBankDetails;

    private String MyName;
    private String MyProfileUrl;

    private  DatabaseReference locationDetails;

    private FirebaseAuth mAut;
    private EditText shopEmail;

    private  String myLastLocationDetails;

    private TextView catergoryShop;
    private TextView createShopButton;

    private String storeVisitKey;

    private String shopType;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_x;

    private ImageView shopLogo;

    private ImageView Art;
    private ImageView Busimess;
    private ImageView Music;
    private ImageView Fashion;

    private StorageReference mStorageRef;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private StorageTask mUploadTask;

    private ProgressBar mProgressBar;

    private DatabaseReference mDatabaseRef_y;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_online_shop);


        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("@Sirocco_ #Online_Shop");


        shopName = findViewById(R.id.storeName);
        shopAdmin = findViewById(R.id.admin);
        shopCell = findViewById(R.id.cell);
        shopBankDetails = findViewById(R.id.bankDetails);
        shopEmail = findViewById(R.id.email);


        catergoryShop =  findViewById(R.id.catergory);
        createShopButton = findViewById(R.id.openShopButton);

        mProgressBar = findViewById(R.id.progress_circle_upload);

        mProgressBar.setVisibility(View.INVISIBLE);

        shopLogo = findViewById(R.id.businessIcon);


        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoStoreAdmins");
        locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation");
        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference().child("Users");

        shopLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.businessIcon);
                openFileChooser();
            }
        });


        Art = findViewById(R.id.catArt);
        Busimess = findViewById(R.id.catStartup);
        Music = findViewById(R.id.catmusic);
        Fashion = findViewById(R.id.catFashion);


        Art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.catArt);

                shopType = "Art";

                catergoryShop.setText(shopType);

            }
        });


        Busimess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.catArt);

                shopType = "Business";

                catergoryShop.setText(shopType);

            }
        });


        Music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.catArt);

                shopType = "Music";
                catergoryShop.setText(shopType);

            }
        });


        Fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.catArt);

                shopType = "Fashion";
                catergoryShop.setText(shopType);

            }
        });

        createShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.openShopButton);

                opneOnlineShop();
            }
        });
    }



    private void myLastLocationDetailsMetod()
    {

        String user_id = mAut.getCurrentUser().getUid();

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
                Toast.makeText(CreateOnlineShop.this, "last location not found"+"Results"+":"+e, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK   && data != null && data.getData() != null)
        {
            mImageUri = data.getData();
            RequestOptions options = new RequestOptions();
            Glide.with(getApplicationContext()).load(mImageUri).apply(options.centerCrop()).thumbnail(0.65f).into(shopLogo);
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }



    private void getMyInfor()
    {

        String user_id = mAut.getCurrentUser().getUid();

        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference().child("User").child(user_id);

        mDatabaseRef_y.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String myName =  dataSnapshot.child("userNameAsEmail").getValue().toString();

                String myProfileImae = dataSnapshot.child("userImageDp").getValue().toString();


                MyName = myName;
                MyProfileUrl = myProfileImae;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }

    private void opneOnlineShop()
    {
        if (mImageUri != null && shopType != null && mUploadTask ==null) {

            final String raterBarValueDefault = "userDefaultDp";

            final String currentUserId = mAut.getCurrentUser().getUid();

            mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoOnlineShop");

            /*mDatabaseRef_x.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                    raterBarValue = dataSnapshot.child("userImageDp").getValue().toString();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

            Toast.makeText(CreateOnlineShop.this, "PAGiiS image uploading ...", Toast.LENGTH_SHORT).show();


            //mProgressCircle.setVisibility(View.VISIBLE);

            //final String imageUrl = String.valueOf(mImageUri);

            //final String saveRaterBarValue = raterBarValue;

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoOnlineShop");

            mStorageRef = FirebaseStorage.getInstance().getReference("SiroccoOnlineShop");

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
                                public void onSuccess(Uri uri) {

                                    finalImageUri = uri;


                                    finalImageUrl = String.valueOf( uri);


                                    final createEventFile upload = new createEventFile(shopName.getText().toString().trim(), finalImageUrl,shopType, shopAdmin.getText().toString().trim(),shopEmail.getText().toString().trim() , shopCell.getText().toString().trim(), shopBankDetails.getText().toString().trim(),"","","","","","","","","","");
                                    mDatabaseRef.push()
                                            .setValue(upload, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {


                                                    storeVisitKey = upload.getKey();

                                                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                    Toast.makeText(CreateOnlineShop.this, "PAGiiS image upload successful !!", Toast.LENGTH_SHORT).show();

                                                    mDatabaseRef_x.child(storeVisitKey).addValueEventListener(new ValueEventListener()
                                                    {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                                        {

                                                            mDatabaseRef_x.child("storeAdminId").setValue(currentUserId).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task)
                                                                {
                                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                                    Intent intent = new Intent(CreateOnlineShop.this, CatchUp.class);
                                                                    intent.putExtra("visit_user_id",storeVisitKey);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                                    //intent.putExtra("visit_user_id",storeVisitKey);
                                                                   // intent.putExtra("invite_id",currentUserId);
                                                                    //intent.putExtra("inviteProfileUrl",MyProfileUrl);
                                                                   // intent.putExtra("inviteName",MyName);
                                                                   // intent.putExtra("from","sirocco");
                                                                  //  intent.putExtra("eventDp",finalImageUrl);
                                                                  //  intent.putExtra("eventName",shopName.getText().toString().trim());
                                                                   // intent.putExtra("event_location",myLastLocationDetails);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError)
                                                        {

                                                        }
                                                    });



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
                            toast.setGravity(Gravity.CENTER, 0, 0);
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
        }else if(mUploadTask!=null)
        {
            Toast.makeText(this, "Store Opening in progress..", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this, "Please Attach Logo / shop Category to succesfully open shop.", Toast.LENGTH_LONG).show();
        }
    }
}
