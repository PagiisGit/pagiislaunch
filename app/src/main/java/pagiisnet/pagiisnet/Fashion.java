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
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import pagiisnet.pagiisnet.Utils.MapsProfileAdapter;
import pagiisnet.pagiisnet.Utils.ViewStoreCatergoryAdaptor;

public class Fashion extends AppCompatActivity implements ViewStoreCatergoryAdaptor.OnItemClickListener,MapsProfileAdapter.OnItemClickListener{


    private RecyclerView mRecyclerView;
    private RecyclerView mapsRecyclerView;
    private ImageView siroccoProfileImage;
    private ImageView siroccoProfileImageR;
    private FloatingActionButton gotoMusic;
    private ViewStoreCatergoryAdaptor mAdapter;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private ImageView userImageDp;
    private TextView userName;
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
    private List<ImageUploads> mapsUploads;
    private MapsProfileAdapter mapsAdapter;

    private ImageView fashionReplacement;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashion);
        mRecyclerView = findViewById(R.id.siroccoRecyclerView);
        mapsRecyclerView = findViewById(R.id.siroccoRecyclerViewTop);
        mapsRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHasFixedSize(true);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);

        //  mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        mapsRecyclerView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null)
        {
            mAuth.signOut();
            Intent intent = new Intent(Fashion.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("@sir_occo #fashion");

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();
        mapsUploads = new ArrayList<>();

        mAdapter = new ViewStoreCatergoryAdaptor(Fashion.this,mUploads);

        mapsAdapter = new MapsProfileAdapter(Fashion.this,mapsUploads);


        mAdapter.setOnItemClickListener(Fashion.this);

        mapsAdapter.setOnItemClickListener(Fashion.this);

        mRecyclerView.setAdapter(mAdapter);

        mapsRecyclerView.setAdapter(mapsAdapter);

        gotoMusic = findViewById(R.id.goToMusic);
        gotoMusic.setVisibility(View.INVISIBLE);

        fashionReplacement = findViewById(R.id.fashionId);

        fashionReplacement.setVisibility(View.VISIBLE);


        gotoMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                Intent intent = new Intent(Fashion.this, SiroccoMusicActivity.class);
                startActivity(intent);
                finish();

            }
        });


        final String user_id = mAuth.getCurrentUser().getUid();

        mStorage = FirebaseStorage.getInstance();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoOnlineShop");
        nDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoOnSaleItem");

        getOnlineUploads();
        getOnlineHeadline();

    }

    private void getOnlineHeadline()
    {
         mDatabaseRef.orderByChild("exRating").equalTo("Fashion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


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

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Fashion.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void getOnlineUploads()
    {
         nDatabaseRef.orderByChild("exRating").equalTo("Fashion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists())
                {
                    mUploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        mUploads.add(upload);
                    }
                    fashionReplacement.setVisibility(View.INVISIBLE);
                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);

                }else
                {
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Fashion.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Fashion.this, "GET_ACCOUNTS Denied",
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
            Intent intent = new Intent(Fashion.this, ActivityUploadImage.class);
            intent.putExtra("share_item_id","null");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

        final ImageUploads selectedImage = mUploads.get(position);
        //final ImageUploads selectedImageUpload = mapsUploads.get(position);

        final String selectedKey = selectedImage.getKey();
        //final String selectedKeyUpload = selectedImageUpload.getKey();

        final String storeMainKey = selectedImage.getViews();

        final String exRater = selectedImage.getPostName();
        //final String exRaterUpload = selectedImageUpload.getExRating();

        final String imageUrl = selectedImage.getImageUrl();
        //final String imageUrlUpload = selectedImageUpload.getImageUrl();

        ///String maxImageUserId = selectedImageUpload.getUserId();

        if (!imageUrl.isEmpty()) {
            if(selectedKey !=null  && !exRater.isEmpty()  && Patterns.WEB_URL.matcher(exRater).matches() )
            {
                Intent intent = new Intent(Fashion.this, LinkView.class);
                intent.putExtra("share_item_id", exRater);
                startActivity(intent);
            }else {

                Toast.makeText(this, "Item does not have a direct link", Toast.LENGTH_SHORT).show();
                finish();

            }
        }

    }

    @Override
    public void onWhatEverClick(int position)

    {
        final ImageUploads selectedImage = mUploads.get(position);
        final ImageUploads selectedImageUpload = mapsUploads.get(position);

        final String selectedKey = selectedImage.getUserId();

        if(selectedKey !=null  && !selectedKey.isEmpty() && Patterns.WEB_URL.matcher(selectedKey).matches())
        {
            Intent intent = new Intent(Fashion.this, LinkView.class);
            intent.putExtra("share_item_id", selectedKey);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Item does not have a direct link", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
