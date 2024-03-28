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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.Utils.PurchasedItemAdaptor;

public class ViewMyCart extends AppCompatActivity implements PurchasedItemAdaptor.OnItemClickListener {

    private RecyclerView mRecyclerView;

    private PurchasedItemAdaptor mAdapter;

    private ImageView userImageDp;

    private TextView userName;
    private int adapterPosition;

    private ImageView cartViewHulder;

    private String ProfileUserName;

    private DatabaseReference userProfileInfor;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private ProgressBar mProgressCircle;

    private ImageView UploadMemeButton;

    private FirebaseAuth mAuth;

    private FirebaseStorage mStorage;

    private  String DeleteKeyAlternative;

    private FloatingActionButton videosButton;

    private DatabaseReference mDatabaseRef;

    private ValueEventListener mDBlistener;

    private List<ImageUploads> mUploads;



    private String StoreMainKey;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_cart);
        mRecyclerView = findViewById(R.id.memeRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        String User_id = mAuth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoPurchasedItem");

        if(mAuth.getCurrentUser() == null)
        {
            mAuth.signOut();
            Intent intent = new Intent(ViewMyCart.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(" @sirocco_ #myKart");
        mStorage = FirebaseStorage.getInstance();
        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();
        cartViewHulder = findViewById(R.id.cartId);

        mAdapter = new PurchasedItemAdaptor(ViewMyCart.this, mUploads);

        mAdapter.setOnItemClickListener(ViewMyCart.this);

        mRecyclerView.setAdapter(mAdapter);

        userImageDp = findViewById(R.id.ImageDP);

        videosButton = findViewById(R.id.goToVideos);

        videosButton.setVisibility(View.INVISIBLE);

        videosButton.setEnabled(false);

        userName = findViewById(R.id.profileName);

        final String user_id = mAuth.getCurrentUser().getUid();


        getCartContent();


    }
    private void getCartContent()

    {
        String user_id = mAuth.getCurrentUser().getUid();

        mDatabaseRef.orderByChild("postLocation").equalTo(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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
                    cartViewHulder.setVisibility(View.INVISIBLE);
                    mProgressCircle.setVisibility(View.INVISIBLE);

                }else
                {
                    cartViewHulder.setVisibility(View.VISIBLE);
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewMyCart.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
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
            Intent intent = new Intent(ViewMyCart.this, ActivityUploadImage.class);
            intent.putExtra("share_item_id","null");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {

        ImageUploads selectedImage = mUploads.get(position);

        String selectedKey = selectedImage.getKey();

        String imageUrl = selectedImage.getImageUrl();

        String storeMainKey = selectedImage.getViews();

        String maxImageUserId = selectedImage.getUserId();

        if(!imageUrl.isEmpty() && !selectedKey.isEmpty())
        {
            Intent intent = new Intent(ViewMyCart.this,BoughtItemMaxView.class);
            intent.putExtra("imageKeyMAx", selectedKey);
            intent.putExtra("imageUrlMax",imageUrl);
            intent.putExtra("imageExRater",maxImageUserId );
            intent.putExtra("imageStoreMainKey",storeMainKey);
            startActivity(intent);

        }else
        {
            Toast.makeText(this, "Pagiis failed to open maxview.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public void onWhatEverClick(int position)
    {

        Intent intent = new Intent(ViewMyCart.this, ActivityOwnProfile.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position)
    {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        DeleteKeyAlternative = selectedKey;

        String myUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoPurchasedItem").child(selectedKey);

        //StorageReference deletImage = mStorage.getReferenceFromUrl(selectedImage.getImageUrl());

        mDatabaseRef.child(DeleteKeyAlternative).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(ViewMyCart.this, "Item sent to bin..", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {


            String errorText;
            @Override
            public void onFailure(@NonNull Exception e)
            {

                errorText = String.valueOf(e);
                Toast.makeText(ViewMyCart.this, errorText + " Deletion Will restart in few seconds", Toast.LENGTH_LONG).show();
                deleteAlternative();


            }
        });


        /*if(deletImage != null)

        {
            deletImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                @Override
                public void onSuccess(Void aVoid) {

                    //mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
                    mDatabaseRef.child(selectedKey).removeValue();
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(MemePage.this, " PAGiiS failed to delete image. try again..", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }else
        {
            Toast.makeText(this, "Pagiis could not locate image in database.", Toast.LENGTH_SHORT).show();
            finish();
        }*/
    }


    private void deleteAlternative()

    {
        mDatabaseRef.addValueEventListener(new ValueEventListener()
        {
            String DeleteFinalString;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot ds :dataSnapshot.getChildren())
                {
                    if(ds.getKey().compareTo(DeleteKeyAlternative)== 0)
                    {

                        mDatabaseRef.child(DeleteKeyAlternative).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(ViewMyCart.this, "Item sent to bin..", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                DeleteFinalString = String.valueOf(e);
                                Toast.makeText(ViewMyCart.this, DeleteFinalString + "PAGiiS Failed to Delete File, please try again :Later", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(ViewMyCart.this, "item delete failed.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }
}
