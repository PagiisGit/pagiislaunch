package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pagiisnet.pagiisnet.Utils.ViewStoreItemAdapter;

public class CatchUp extends AppCompatActivity implements  ViewStoreItemAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ViewStoreItemAdapter mAdapter;
    private TextView storeNameText;
    private TextView storeAdminText;
    private TextView storeEmailText;

    private TextView storeBankDetailText;
    private TextView storeCellText;
    private TextView storeVisitsText;
    private TextView storePurchasesText;

    private DatabaseReference mDatabaseRef_x;
    private TextView storeCustomersText;



    private  ImageView storeAdminIcon;
    private ImageView storeLogoIcon;
    private ProgressBar mProgressCircle;


    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private String getKey;


    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDataRef;

    private DatabaseReference mDatabaseStoreItems;
    private DatabaseReference mDatabaseReTest;

    private ValueEventListener mDBlistener;
    private List<ImageUploads> mUploads;

    private String StoreMainKey;
    private Iterator<lockedImagesTag> iterator;
    private List<lockedImagesTag> tagedUsers;

    private androidx.appcompat.widget.Toolbar mToolbar;
    private final int raterBarValue = 0;



    private String on_maps_visited_user_id;

    private String storeAdmin;
    private String storeLogo;
    private String storeName;
    private String storeEmail;
    private String storeBankDetail;
    private String storeCellNumber;
    private String storeCatergory;

    private String StoreViews;
    private String StoreVisits;
    private String StorePurchases;

    private FloatingActionButton contentRater;

    private DatabaseReference getUserProfileDataRef;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_up);

        mToolbar = findViewById(R.id.appBarLayout);

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("@Sir_occo #Online_Shop");

        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance();

        if (mAuth.getCurrentUser() == null) {
            mAuth.signOut();
            Intent intent = new Intent(CatchUp.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mRecyclerView = findViewById(R.id.memeRecyclerView);

        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);

        mRecyclerView.setLayoutManager(gridLayoutManager);

       // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        storeNameText = findViewById(R.id.storeNameTextView);
        storeAdminText = findViewById(R.id.storeAdminTextView);
        storeEmailText = findViewById(R.id.storeEmailTextView);
        storeBankDetailText =findViewById(R.id.storeBankingDetailsText);
        storeCellText = findViewById(R.id.storeWalletTextView);
        storeVisitsText = findViewById(R.id.visitsTextView);
        storePurchasesText = findViewById(R.id.customerViewsText);
        storeCustomersText = findViewById(R.id.purchasesViewText);

        storeLogoIcon = findViewById(R.id.storeLogo);
        storeAdminIcon = findViewById(R.id.storeAdminIcon);
        contentRater = findViewById(R.id.adminCart);


        contentRater.setEnabled(false);

        contentRater.setVisibility(View.INVISIBLE);



        contentRater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.adminCart);
                Intent intent = new Intent(CatchUp.this, AdminCartView.class);
                startActivity(intent);

            }
        });

        storeAdminIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.storeAdminIcon);
                Intent intent = new Intent(CatchUp.this, UserProfileActivity.class);
                intent.putExtra("visit_user_id", storeAdmin);
                startActivity(intent);

            }
        });

        //returnToMaps = findViewById(R.id.returnToMaps);

        mProgressCircle = findViewById(R.id.progress_circle_user_memes);

        mProgressCircle.setVisibility(View.VISIBLE);

        mUploads = new ArrayList<>();

        tagedUsers = new ArrayList<>();
        mAdapter = new ViewStoreItemAdapter(CatchUp.this, mUploads);
        mAdapter.setOnItemClickListener(CatchUp.this);
        mRecyclerView.setAdapter(mAdapter);


        on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();

        mDatabaseStoreItems = FirebaseDatabase.getInstance().getReference().child("SiroccoOnSaleItem");
        mDatabaseReTest = FirebaseDatabase.getInstance().getReference().child("SiroccoOnlineShop").child(on_maps_visited_user_id);
        mDataRef = FirebaseDatabase.getInstance().getReference().child("SiroccoStoreVisits").child(on_maps_visited_user_id);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("SiroccoPurchasedItem").child(on_maps_visited_user_id);




        mDatabaseReTest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    storeName = dataSnapshot.child("nameShop").getValue().toString();
                    storeEmail = dataSnapshot.child("emailStoreAddress").getValue().toString();
                    storeCellNumber = dataSnapshot.child("storeCell").getValue().toString();
                    storeBankDetail = dataSnapshot.child("storebankDetails").getValue().toString();
                    storeLogo = dataSnapshot.child("imageUrlSHopICon").getValue().toString();

                    storeAdmin = dataSnapshot.child("storeAdminId").getValue().toString();


                    storeNameText.setText(storeName);
                    storeEmailText.setText(storeEmail);
                    storeCellText.setText(storeCellNumber);
                    storeBankDetailText.setText(storeBankDetail);
                    storeAdminText.setText(storeAdmin);

                    RequestOptions options = new RequestOptions();

                    Glide.with(CatchUp.this).load(storeLogo).apply(options.centerCrop()).thumbnail(0.75f).into(storeLogoIcon);


                }else
                {
                    Toast.makeText(CatchUp.this, "Store currently unavailable. please visit later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        getDataNormally();
        checkInStore();
        checkifAdmindTrue();

    }

    private void checkInStore()
    {

        String current_user_id = mAuth.getCurrentUser().getUid();

        mDataRef.child(current_user_id).setValue(on_maps_visited_user_id).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                mDataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        String customerVisits = String.valueOf(dataSnapshot.getChildrenCount());
                        storeVisitsText.setText(customerVisits);
                        getStoreSales();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private void getStoreSales()
    {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String storeSales = String.valueOf(dataSnapshot.getChildrenCount());
                    storePurchasesText.setText(storeSales);
                    storeCustomersText.setText(storeSales);

                }else
                {
                    Toast.makeText(CatchUp.this, "Store currently unavailable. please visit later.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                }else
                {
                    contentRater.setEnabled(false);
                    contentRater.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                Toast.makeText(CatchUp.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void getDataNormally()
    {
        mDatabaseStoreItems.orderByChild("postName").equalTo(on_maps_visited_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists())
                {
                    mUploads.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        ImageUploads upload = ds.getValue(ImageUploads.class);
                        upload.setKey(ds.getKey());
                        mUploads.add(upload);
                    }
                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }else
                {
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_memes_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //This button is for the explicit content view
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.sirocco_icon)
        {
            Intent intent = new Intent(CatchUp.this, SiroccoPage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position)
    {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String imageUrl = selectedImage.getImageUrl();

        final String userKeyId = selectedImage.getUserId();

        String user_id = mAuth.getCurrentUser().getUid();

        Intent intent = new Intent(CatchUp.this ,SiroccoMaxView.class);
        intent.putExtra("imageKeyMAx", selectedKey);
        intent.putExtra("imageUrlMax",imageUrl);
        intent.putExtra("imageUserId", userKeyId);
        intent.putExtra("imageStoreMainKey", StoreMainKey);
        startActivity(intent);
    }

    @Override
    public void onWhatEverClick(int position)
    {
        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        String getUserRef = selectedImage.getUserId();

        if(!getUserRef.isEmpty())
        {
            Intent intent = new Intent(CatchUp.this,SiroccoPage.class);
            intent.putExtra("visit_user_id", getUserRef);
            startActivity(intent);
        }
    }
}
