package pagiisnet.pagiisnet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import pagiisnet.pagiisnet.Utils.WakinSlideAdaptorOne;
import pagiisnet.pagiisnet.Utils.WalkinOrizontalSlide;

public class MainActivity extends AppCompatActivity implements WakinSlideAdaptorOne.OnItemClickListener,WalkinOrizontalSlide.OnItemClickListener{

    private RecyclerView mRecyclerView;

    private RecyclerView mapsRecyclerView;

    private ImageView siroccoProfileImage;
    private ImageView siroccoProfileImageR;



    private FloatingActionButton gotoMusic;

    private WakinSlideAdaptorOne mAdapter;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private ImageView userImageDp;

    private TextView userName;

    private String earcTypeValue;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private ProgressBar mProgressCircle;

    private ImageView UploadMemeButton;

    private FirebaseAuth mAuth;

    private FirebaseStorage mStorage;

    private DatabaseReference mDatabaseRef;

    private DatabaseReference nDatabaseRef;

    private ValueEventListener mDBlistener;

    private ValueEventListener mDataBaseListener;

    private List<ImageUploads> mUploads;

    private  Boolean isFirstTimeRun;

    private List<ImageUploads> mapsUploads;

    private WalkinOrizontalSlide mapsAdapter;


    private LinearLayout musicReplacement;

    private  ImageView ii;

    private ImageView cc;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.siroccoRecyclerView);

        FirebaseApp.initializeApp(getApplicationContext());


        mRecyclerView.setHasFixedSize(true);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);

        //  mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));




        mAuth = FirebaseAuth.getInstance();


        if(mAuth.getCurrentUser() == null)
        {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Welcome");

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();
        mapsUploads = new ArrayList<>();

        mAdapter = new WakinSlideAdaptorOne(MainActivity.this,mUploads);



        mAdapter.setOnItemClickListener(MainActivity.this);


        mRecyclerView.setAdapter(mAdapter);

        ii = findViewById(R.id.ii);
        cc = findViewById(R.id.cc);

        gotoMusic = findViewById(R.id.goToMusic);

        gotoMusic.setVisibility(View.VISIBLE);

        gotoMusic.setEnabled(true);


        ii.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.ii);
                mRecyclerView.setVisibility(View.INVISIBLE);

            }
        });


        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.cc);
                Intent intent = new Intent(MainActivity.this,SiroccoPage.class);
                startActivity(intent);

            }
        });


       // musicReplacement = findViewById(R.id.canvasId);

        //musicReplacement.setVisibility(View.VISIBLE);


        gotoMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });


        final String user_id = mAuth.getCurrentUser().getUid();

        mStorage = FirebaseStorage.getInstance();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("WalkinWall");
        nDatabaseRef = FirebaseDatabase.getInstance().getReference("WalkinWelcome");

        getOnlineUploads();
        //getOnlineHeadline();

    }




    private void getOnlineHeadline()
    {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("WalkinWall");

        mDataBaseListener = mDatabaseRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists())
                {
                    mapsUploads.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        mapsUploads.add(upload);
                    }
                    mapsAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }


    private void getOnlineUploads()
    {
        nDatabaseRef = FirebaseDatabase.getInstance().getReference("WalkinWelcome");
        mDBlistener = nDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists())
                {

                    mUploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        mUploads.add(upload);
                    }
                    //musicReplacement.setVisibility(View.INVISIBLE);
                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);

                }else
                {

                    mProgressCircle.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uploads_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do your stuff
            } else {
                Toast.makeText(MainActivity.this, "GET_ACCOUNTS Denied",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add)
        {
            Intent intent = new Intent(MainActivity.this, ActivityUploadImage.class);
            intent.putExtra("share_item_id","null");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String storeMainKey = selectedImage.getViews();

        final String exRater = selectedImage.getExRating();

        final String imageUrl = selectedImage.getImageUrl();

        final String link = selectedImage.getName();

        if(!imageUrl.isEmpty())
        {

            Toast.makeText(this, link, Toast.LENGTH_SHORT).show();


        }else
        {
            Toast.makeText(this, "Pagiis failed to view image.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onWhatEverClick(int position)

    {
        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String storeMainKey = selectedImage.getViews();

        final String exRater = selectedImage.getExRating();

        final String imageUrl = selectedImage.getName();

        final String link = selectedImage.getName();

        if(!imageUrl.isEmpty())
        {
            Intent intent = new Intent(MainActivity.this,SiroccoPage.class);
            startActivity(intent);

        }else
        {
            Toast.makeText(this, "Pagiis failed to view image.", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onStart()
    {
        super.onStart();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
    }


    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBlistener);
        nDatabaseRef.removeEventListener(mDataBaseListener);
    }
}
