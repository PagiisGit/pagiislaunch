package pagiisnet.pagiisnet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import pagiisnet.pagiisnet.R;

public class ViewUserMaxImage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private ImageView maxView,blackLock,blueLock;
    private DatabaseReference mDataRef;
    private DatabaseReference lockedImages;
    private String ImageUrl;
    private String imageKey;
    private String imageUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_max_image);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        if (mAuth.getCurrentUser() == null) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(" Pagiis ");

        maxView = findViewById(R.id.pagiis_max_view);

        ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();
        imageKey = getIntent().getExtras().get("imageKeyMAx").toString();
        imageUserId = getIntent().getExtras().get("imageUserId").toString();

        mProgressBar = findViewById(R.id.PagiisMaxProgressBar);

        //0

        maxView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.pagiis_max_view);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewUserMaxImage.this);
                View mView = getLayoutInflater().inflate(R.layout.pin_image_dialog, null);
                ImageView logoutImage = mView.findViewById(R.id.SignOutImage);
                ImageView cancelLogOutImage = mView.findViewById(R.id.cancelSignOutImage);
                final String current_userID = mAuth.getCurrentUser().getUid();

                lockedImages = FirebaseDatabase.getInstance().getReference().child("LockedImages").child(current_userID).child(imageKey);
                logoutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        lockedImages.child("tag_userImage_id").setValue(imageUserId);
                        lockedImages.child("tag_userImage_url").setValue(ImageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(ViewUserMaxImage.this, "Image pinned successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                cancelLogOutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), PagiisMaxView.class);
                        startActivity(intent);
                        finish();

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });


        getPagiisMaxView();
    }

    private void checkImageLocked()
    {
        imageUserId = getIntent().getExtras().get("imageUserId").toString();
        String CurrentUserId = mAuth.getCurrentUser().getUid();

        lockedImages.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if (ds.getKey().compareTo(imageUserId) == 0)
                        {
                            getPagiisMaxView();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void tryIntentMaxView()

    {
        Glide.with(ViewUserMaxImage.this).load(ImageUrl).into(maxView);
        mProgressBar.setVisibility(View.INVISIBLE);
    }


    private void getPagiisMaxView() {

        if(!imageKey.isEmpty() && !ImageUrl.isEmpty())
        {

            ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();

            imageKey = getIntent().getExtras().get("imageKeyMAx").toString();

            imageUserId = getIntent().getExtras().get("imageUserId").toString();

            mDataRef = FirebaseDatabase.getInstance().getReference().child("uploads");

            mDataRef.child(imageUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        if (postSnapshot.getKey().compareTo(imageKey) == 0)
                        {

                            Glide.with(ViewUserMaxImage.this).load(ImageUrl).into(maxView);
                            mProgressBar.setVisibility(View.INVISIBLE);

                        } else {
                            tryIntentMaxView();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });

        }else
        {
            Intent intent = new Intent(ViewUserMaxImage.this,ViewUsersMemes.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

}
