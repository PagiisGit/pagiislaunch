package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pagiisnet.pagiisnet.Utils.mapsProfileItemsViewAdaptor;

public class SiroccoMaxView extends AppCompatActivity implements mapsProfileItemsViewAdaptor.OnItemClickListener {


    private ImageView maxView;
    private TextView modelName, brandName, browseText;

    private FirebaseAuth mAuth;

    private ProgressBar mProgressBar;

    private DatabaseReference mDataRef;

    private DatabaseReference mDatabaseRef_x;

    private mapsProfileItemsViewAdaptor mAdapter;
    private List<ImageUploads> uploads;

    private CardView mapsViewCard;
    private RecyclerView mapsRecyclerView;


    private DatabaseReference TagRef_x;

    private androidx.appcompat.widget.Toolbar mToolbar;



    private DatabaseReference storeRef;
    private String ImageUrl;
    private String imageKey;
    private String imageExRater;

    private ImageView openShop;
    private ImageView closeTags;
    private ImageView TagsView;

    private ImageView browseIcon;

    private ImageView ProfileNameIcon;

    private ValueEventListener mDBlistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sirocco_max_view);
        mapsRecyclerView = findViewById(R.id.mapsSearchRecyclerView);

        FirebaseApp.initializeApp(getApplicationContext());

        mapsRecyclerView.setHasFixedSize(true);

        mapsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();


        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);


        if(mAuth.getCurrentUser() == null)
        {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("@Sirocco_");


        maxView = findViewById(R.id.SirrocoMaxProfile);
        modelName = findViewById(R.id.profileNameTextModel);
        brandName = findViewById(R.id.profileEmailTextBrand);
        closeTags = findViewById(R.id.hideSearchRecyclerView);
        browseText = findViewById(R.id.profileNameTextModelTwo);
        TagsView = findViewById(R.id.profileEmail);
        ProfileNameIcon = findViewById(R.id.profileNameIcon);
        browseIcon = findViewById(R.id.profileNameIconTwo);


        mapsViewCard = findViewById(R.id.mapsSearchViewCardView);
        uploads = new ArrayList<>();

        mAdapter = new mapsProfileItemsViewAdaptor(SiroccoMaxView.this, uploads);

        mAdapter.setOnItemClickListener(SiroccoMaxView.this);
        mapsRecyclerView.setAdapter(mAdapter);


        //modelName.setMovementMethod(LinkMovementMethod.getInstance());

        //brandName.setMovementMethod(LinkMovementMethod.getInstance());

        mProgressBar = findViewById(R.id.SirrocoMaxProgressBar);

        openShop = findViewById(R.id.OnlineShop);

        openShop.setVisibility(View.INVISIBLE);

        mProgressBar.setVisibility(View.VISIBLE);
        mapsViewCard.setVisibility(View.INVISIBLE);

        ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();

        imageKey = getIntent().getExtras().get("imageKeyMAx").toString();

        imageExRater = getIntent().getExtras().get("imageExRater").toString();


        //TagRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoUploadTags").child(imageKey);

        getSiroccoMaxView();

        checkIfItemIsOnShop();



        openShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(SiroccoMaxView.this, CustomerPurchase.class);
                intent.putExtra("imageKeyMAx", imageKey);
                intent.putExtra("imageUrlMax",ImageUrl);
                startActivity(intent);


            }
        });


        closeTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.hideSearchRecyclerView);
                mapsViewCard.setVisibility(View.INVISIBLE);

                RequestOptions options = new RequestOptions();
                Glide.with(SiroccoMaxView.this).load(ImageUrl).apply(options.centerCrop()).thumbnail(0.75f).into(maxView);



            }
        });



       browseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(SiroccoMaxView.this, CatchUp.class);
                intent.putExtra("visit_user_id", imageExRater);
                startActivity(intent);

            }
        });



        browseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(SiroccoMaxView.this, CatchUp.class);
                intent.putExtra("visit_user_id", imageExRater);
                startActivity(intent);


            }
        });


        maxView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.SirrocoMaxProfile);

                checkifAdmindTrue();

            }
        });


        TagsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.profileEmail);

                GetMapsOnlineProfile();


            }
        });

        ProfileNameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.profileNameIcon);

                Intent intent = new Intent(SiroccoMaxView.this, VideoStreaming.class);
                startActivity(intent);

            }
        });


    }

    private void checkIfItemIsOnShop()
    {
        storeRef = FirebaseDatabase.getInstance().getReference().child("SiroccoOnSaleItem");

        storeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {

                    if(ds.getKey().compareTo(imageKey) == 0)
                    {
                        openShop.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void GetMapsOnlineProfile()
    {

        String user_id = mAuth.getCurrentUser().getUid();
        TagRef_x  = FirebaseDatabase.getInstance().getReference().child("SiroccoUploadTags").child(imageKey);

        TagRef_x.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())

                {
                    uploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        uploads.add(upload);

                    }

                    mAdapter.notifyDataSetChanged();
                    //recyclerProgressBar.setVisibility(View.INVISIBLE);
                    mapsViewCard.setVisibility(View.VISIBLE);
                    mapsRecyclerView.setVisibility(View.VISIBLE);
                }else
                {
                    Toast.makeText(SiroccoMaxView.this, "Item does not have tags", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SiroccoMaxView.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //recyclerProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void getSiroccoMaxView() {

        if(!imageKey.isEmpty() && !ImageUrl.isEmpty())
        {


            ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();

            imageKey = getIntent().getExtras().get("imageKeyMAx").toString();

            imageExRater = getIntent().getExtras().get("imageExRater").toString();

            mDataRef = FirebaseDatabase.getInstance().getReference().child("SiroccoUploads").child(imageKey);


            mDataRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {


                            RequestOptions options = new RequestOptions();

                            Glide.with(SiroccoMaxView.this).load(ImageUrl).apply(options.centerCrop()).thumbnail(0.75f).into(maxView);
                            mProgressBar.setVisibility(View.INVISIBLE);

                            String BrandName = dataSnapshot.child("name").getValue().toString();

                            String ModelName =dataSnapshot.child("modelName").getValue().toString();

                            brandName.setText(BrandName);

                            modelName.setText(ModelName);


                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)

                {
                }
            });
        }else
        {
            Intent intent = new Intent(SiroccoMaxView.this, ViewUsersMemes.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

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
                    Intent intent = new Intent(getApplicationContext(),SirrocoImageUpload.class);
                    intent.putExtra("visit_user_id",imageExRater);
                    intent.putExtra("visit_user_key",imageKey);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                Toast.makeText(SiroccoMaxView.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void itemClick(int position)
    {
        final ImageUploads selectedImage = uploads.get(position);

        final String imageUrl = selectedImage.getImageUrl();


        RequestOptions options = new RequestOptions();

        Glide.with(SiroccoMaxView.this).load(imageUrl).apply(options.centerCrop()).thumbnail(0.75f).into(maxView);


        mapsViewCard.setVisibility(View.VISIBLE);
        mapsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void WhatEverClick(int position)

    {
        final ImageUploads selectedImage = uploads.get(position);

        final String selectedKey = selectedImage.getUserId();

        Intent intent = new Intent(getApplicationContext(),UserProfileActivity.class);
        intent.putExtra("visit_user_id", selectedKey);
        startActivity(intent);

    }
}
