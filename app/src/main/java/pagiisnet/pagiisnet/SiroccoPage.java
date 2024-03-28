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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.Utils.SiroccoPicsAdapter;
import pagiisnet.pagiisnet.Utils.maps_view_adapter;

public class SiroccoPage extends AppCompatActivity implements maps_view_adapter.OnItemClickListener, SiroccoPicsAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;

    private RecyclerView mapsRecyclerView;

    private ImageView siroccoProfileImage;
    private ImageView siroccoProfileImageR;

    private ImageView artWorld;
    private ImageView businessWorld;
    private ImageView musicWorld;
    private ImageView fashionWorld;
    private ImageView myCart;

    private static final String TAG = "SiroccoPage";

    private FloatingActionButton gotoMusic;
    private FloatingActionButton goToOpenStore;
    private FloatingActionButton goToSell;
    private FloatingActionButton goToVideos;

    private static final int ACTION_NUM = 0;


    private SiroccoPicsAdapter mAdapter;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private ImageView userImageDp;

    private TextView userName;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private ProgressBar mProgressCircle;

    private ImageView UploadMemeButton;

    private FirebaseAuth mAuth;

    private FirebaseStorage mStorage;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_x;

    private DatabaseReference nDatabaseRef;

    private ValueEventListener mDBlistener;

    private ValueEventListener mDataBaseListener;

    private List<SirrocoImageUploads> mUploads;

    private List<ImageUploads> mapsUploads;

    private  maps_view_adapter mapsAdapter;




    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sirocco_page);
        /*mRecyclerView = findViewById(R.id.siroccoRecyclerView);
        mapsRecyclerView = findViewById(R.id.siroccoRecyclerViewTop);
        mapsRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHasFixedSize(true);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);

      //  mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        mapsRecyclerView.setLayoutManager(linearLayoutManager);

         */


        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() == null) {
            mAuth.signOut();
            Intent intent = new Intent(SiroccoPage.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("@sir_occo");



       /* mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();
        mapsUploads = new ArrayList<>();

        mAdapter = new SiroccoPicsAdapter(SiroccoPage.this,mUploads);

        mapsAdapter = new maps_view_adapter(SiroccoPage.this,mapsUploads);


        mAdapter.setOnItemClickListener(SiroccoPage.this);

        mapsAdapter.setOnItemClickListener(SiroccoPage.this);

        mRecyclerView.setAdapter(mAdapter);

        mapsRecyclerView.setAdapter(mapsAdapter);

        gotoMusic = findViewById(R.id.goToMusic);
        gotoMusic.setVisibility(View.INVISIBLE);
        //goToOpenStore = findViewById(R.id.goToCreateStore);
       // goToSell = findViewById(R.id.goToSell);
       // goToVideos = findViewById(R.id.goToVideosUpload);





       // artWorld = findViewById(R.id.paint);
       // musicWorld = findViewById(R.id.musicWorld);
       // fashionWorld = findViewById(R.id.fashionWorld);
       // businessWorld = findViewById(R.id.businessWorld);
       // myCart = findViewById(R.id.myCart);



        goToSell.setVisibility(View.INVISIBLE);
        goToOpenStore.setVisibility(View.INVISIBLE);

        goToSell.setEnabled(false);
        goToOpenStore.setEnabled(false);


        gotoMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {

                Intent intent = new Intent(SiroccoPage.this, SiroccoOnlineStores.class);
                intent.putExtra("visit_user_id","fromSirocco");
                startActivity(intent);


            }
        });


        goToOpenStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                Intent intent = new Intent(SiroccoPage.this,CreateOnlineShop.class);
                startActivity(intent);
            }
        });


        goToSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                Intent intent = new Intent(SiroccoPage.this, SellItemPost.class);
                intent.putExtra("visit_item_id", "fromSale");
                startActivity(intent);


            }
        });



        /*artWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                view.findViewById(R.id.artworld);
                Intent intent = new Intent(SiroccoPage.this, Art.class);
                startActivity(intent);
            }
        });*/



       /* businessWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                view.findViewById(R.id.businessWorld);

                Intent intent = new Intent(SiroccoPage.this, Business.class);
                startActivity(intent);


            }
        });*/


        /*goToVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                view.findViewById(R.id.goToVideosUpload);

                Intent intent = new Intent(SiroccoPage.this, SiroccoPromoVideos.class);
                startActivity(intent);


            }
        });*/





        /*musicWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                view.findViewById(R.id.musicWorld);

                Intent intent = new Intent(SiroccoPage.this, Music.class);
                startActivity(intent);


            }
        });*/




        /*fashionWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                view.findViewById(R.id.fashionWorld);

                Intent intent = new Intent(SiroccoPage.this, Fashion.class);
                startActivity(intent);


            }
        });/*


        /*myCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                view.findViewById(R.id.myCart);

                Intent intent = new Intent(SiroccoPage.this, ViewMyCart.class);
                startActivity(intent);
            }
        });*/


        final String user_id = mAuth.getCurrentUser().getUid();

        mStorage = FirebaseStorage.getInstance();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoUploads");
        nDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoUploadHeadlines");

        getOnlineUploads();
        getOnlineHeadline();

        //setupBottomNavigationView();


    }






    /*private void setupBottomNavigationView()
    {
        Log.d(TAG, "Setting up BottomNaviagationView");
        BottomNavigationView bottomNavigationViewEx = findViewById(R.id.bottomNavSir);
        BottomNavHelperSirocco.enableNavigation(SiroccoPage.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTION_NUM);
        menuItem.setChecked(false);

    }*/

    private void getOnlineHeadline()
    {

        mDataBaseListener = nDatabaseRef.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(SiroccoPage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void getOnlineUploads()
    {
        mDBlistener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    SirrocoImageUploads upload = postSnapshot.getValue(SirrocoImageUploads.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SiroccoPage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

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
                Toast.makeText(SiroccoPage.this, "GET_ACCOUNTS Denied",
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
            Intent intent = new Intent(SiroccoPage.this, ActivityUploadImage.class);
            intent.putExtra("share_item_id","null");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

        final SirrocoImageUploads selectedImage = mUploads.get(position);
        //final ImageUploads selectedImageUpload = mapsUploads.get(position);

        final String selectedKey = selectedImage.getKey();
       // final String selectedKeyUpload = selectedImageUpload.getKey();

        final String exRater = selectedImage.getExRating();
        //final String exRaterUpload = selectedImageUpload.getExRating();

        final String imageUrl = selectedImage.getImageUrl();
       // final String imageUrlUpload = selectedImageUpload.getImageUrl();

        //String maxImageUserId = selectedImageUpload.getUserId();

        if (!imageUrl.isEmpty()) {
            if(selectedKey !=null  && !selectedKey.isEmpty()  && Patterns.WEB_URL.matcher(selectedKey).matches() )
            {
                Intent intent = new Intent(SiroccoPage.this, LinkView.class);
                intent.putExtra("share_item_id", selectedKey);
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


        final ImageUploads selectedImage = mapsUploads.get(position);
        //final ImageUploads selectedImageUpload = mapsUploads.get(position);

        final String selectedKey = selectedImage.getUserId();
        //final String selectedKeyUpload = selectedImageUpload.getUserId();

        if(selectedKey !=null  && !selectedKey.isEmpty() && Patterns.WEB_URL.matcher(selectedKey).matches())
        {
            Intent intent = new Intent(SiroccoPage.this, LinkView.class);
            intent.putExtra("share_item_id", selectedKey);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Item does not have a direct link", Toast.LENGTH_SHORT).show();
            finish();
        }




    }

    @Override
    public void ItemClick(int position)
    {

        final SirrocoImageUploads selectedImage = mUploads.get(position);
        //final ImageUploads selectedImageUpload = mapsUploads.get(position);

        final String selectedKey = selectedImage.getExRating();
        //final String selectedKeyUpload = selectedImageUpload.getUserId();

        if(selectedKey !=null )
        {
            Intent intent = new Intent(SiroccoPage.this, CatchUp.class);
            intent.putExtra("visit_user_id", selectedKey);
            startActivity(intent);
        }


    }

    @Override
    public void WhatEverClick(int position)
    {

        final ImageUploads selectedImage = mapsUploads.get(position);
        //final ImageUploads selectedImageUpload = mapsUploads.get(position);

        final String selectedKey = selectedImage.getKey();
        // final String selectedKeyUpload = selectedImageUpload.getKey();

        final String exRater = selectedImage.getExRating();
        //final String exRaterUpload = selectedImageUpload.getExRating();

        final String imageUrl = selectedImage.getImageUrl();
        // final String imageUrlUpload = selectedImageUpload.getImageUrl();

        //String maxImageUserId = selectedImageUpload.getUserId();

        if (!imageUrl.isEmpty()) {
            Intent intent = new Intent(SiroccoPage.this, SiroccoMaxView.class);
            intent.putExtra("imageKeyMAx", selectedKey);
            intent.putExtra("imageUrlMax", imageUrl);
            intent.putExtra("imageExRater", exRater);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Sorry Pagiis could not open Max-view.", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
