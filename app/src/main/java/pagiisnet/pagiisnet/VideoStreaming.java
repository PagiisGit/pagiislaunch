package pagiisnet.pagiisnet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

import pagiisnet.pagiisnet.Utils.SiroccoSlideView;
import pagiisnet.pagiisnet.Utils.mapsProfileItemsViewAdaptor;

public class VideoStreaming extends AppCompatActivity implements mapsProfileItemsViewAdaptor.OnItemClickListener,
 SiroccoSlideView.OnItemClickListener {




    private List<SirrocoImageUploads> uploads;

    private List<ImageUploads> mUploads;

    private ImageView userImageDp;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseRef;

    private SiroccoSlideView mAdapter;
    private mapsProfileItemsViewAdaptor  sAdapter;


    private CardView mapsViewCard;
    private RecyclerView mRecyclerView;
    private ValueEventListener mDBlistener;

    private RecyclerView mapsRecyclerView;

    private CardView mapsViewSearcCard;

    private CardView mapsViewSearcCardMax;

    private ImageView exitMaxCardView;


    private DatabaseReference TagRef_x;

    private DatabaseReference lockedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_streaming);

        FirebaseApp.initializeApp(getApplicationContext());



        mapsViewCard = findViewById(R.id.mapsViewCardView);
        mapsViewSearcCard= findViewById(R.id.mapsSearchViewCardView);
        mapsViewSearcCardMax= findViewById(R.id.mapsSearchViewCardViewMax);

        mAdapter = new SiroccoSlideView(VideoStreaming.this, uploads);
        //mapsAdapterNew = new mapSearchedItemAdaptor(MapsActivity.this,mUploads);
        sAdapter = new mapsProfileItemsViewAdaptor(VideoStreaming.this,mUploads);

        userImageDp = findViewById(R.id.mapsMaxView);


        mAuth = FirebaseAuth.getInstance();


        uploads = new ArrayList<>();
        mUploads = new ArrayList<>();


        mRecyclerView = findViewById(R.id.mapsSearchRecyclerViewMax);
        mapsRecyclerView = findViewById(R.id.mapsRecyclerView);

        exitMaxCardView = findViewById(R.id.hideUserRecyclerviewMax);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManagerR = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManagerX = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        mapsRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHasFixedSize(true);

        mAdapter.setOnItemClickListener(VideoStreaming.this);
        //mapsAdapterNew.setOnItemClickListener(MapsActivity.this);
        sAdapter.setOnItemClickListener(VideoStreaming.this);

        mapsRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setLayoutManager(linearLayoutManagerR);

        mapsRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setAdapter(sAdapter);


        GetMapsOnlineProfile();

    }



    public void GetMapsOnlineProfile()
    {

        //String user_id = mAuth.getCurrentUser().getUid();
        TagRef_x  = FirebaseDatabase.getInstance().getReference().child("SiroccoOnlineShop");

         TagRef_x.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())

                {
                    uploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        SirrocoImageUploads upload = postSnapshot.getValue(SirrocoImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        uploads.add(upload);

                    }

                    mAdapter.notifyDataSetChanged();
                    //recyclerProgressBar.setVisibility(View.INVISIBLE);
                    mapsViewCard.setVisibility(View.VISIBLE);
                    mapsRecyclerView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VideoStreaming.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //recyclerProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }



    @Override
    public void onItemClick(int position)
    {

        final SirrocoImageUploads selectedImage = uploads.get(position);

        final String imageUrl = selectedImage.getImageUrl();

        final String  userId = selectedImage.getKey();

        RequestOptions options = new RequestOptions();

        Glide.with(VideoStreaming.this).load(imageUrl).apply(options.centerCrop()).thumbnail(0.75f).into(userImageDp);

        lockedImages  = FirebaseDatabase.getInstance().getReference().child("SiroccoUploads");

        lockedImages.orderByChild("rateEx").equalTo(userId).addValueEventListener(new ValueEventListener() {
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

                    sAdapter.notifyDataSetChanged();
                    //recyclerProgressBar.setVisibility(View.INVISIBLE);
                    mapsViewSearcCardMax.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VideoStreaming.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //recyclerProgressBar.setVisibility(View.INVISIBLE);
            }
        });


    }

    @Override
    public void onDeleteClick(int position)
    {

        final SirrocoImageUploads selectedImage = uploads.get(position);

        final String selectedKey = selectedImage.getExRating();

        String myUserId = mAuth.getCurrentUser().getUid();

        //mDatabaseRef = FirebaseDatabase.getInstance().getReference("Tags").child(myUserId);

        //StorageReference deletImage = mStorage.getReferenceFromUrl(selectedImage.getImageUrl());
        Intent intent = new Intent(VideoStreaming.this,SirrocoImageUpload.class);
        intent.putExtra("visit_user_id", selectedKey);
        startActivity(intent);
    }

    @Override
    public void itemClick(int position)
    {

        final SirrocoImageUploads selectedImage = uploads.get(position);

        final String imageUrl = selectedImage.getImageUrl();

        final String taged_id = selectedImage.getKey();


        RequestOptions options = new RequestOptions();

        Glide.with(VideoStreaming.this).load(imageUrl).apply(options.centerCrop()).thumbnail(0.75f).into(userImageDp);


        //recyclerProgressBar.setVisibility(View.INVISIBLE);
        mapsViewSearcCardMax.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void WhatEverClick(int position)
    {

    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
