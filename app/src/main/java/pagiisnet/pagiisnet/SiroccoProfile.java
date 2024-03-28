package pagiisnet.pagiisnet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;


import pagiisnet.pagiisnet.R;
import com.google.firebase.storage.StorageTask;

public class SiroccoProfile extends AppCompatActivity {

    private ImageView userImageDp;
    private TextView userName;
    private TextView Status;
    private ImageView changeDp;
    private ImageView saveinfo;
    private ImageView editStatus;


    private ImageView siroocUpadateView;

    private TextView logoutText;

    private ProgressBar mProgressBar;

    private DatabaseReference getUserProfileDataRef;

    private DatabaseReference getSirocoProfile;

    private StorageTask mUploadTask;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private StorageReference userImageStorageDpRef;

    private DatabaseReference usernameDataRef;

    private FirebaseAuth mAuth;

    private final static int Gallery_Pick = 1;

    private Uri resultUri;

    private  String FinalHeadlineUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sirocco_profile);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);



        siroocUpadateView = findViewById(R.id.SiroccoProfileHealinesR);

        getSirocoProfile = FirebaseDatabase.getInstance().getReference().child("SiroccoHeadlines").child("headline");

        if(mAuth.getCurrentUser() == null)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(" Sirocco Profile ");

        editStatus = findViewById(R.id.writeStatus);

        String onlineUserId = mAuth.getCurrentUser().getUid();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        getSirocoProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String Healineurl = dataSnapshot.child("imageUrlHeadline").getValue().toString();

                FinalHeadlineUrl = Healineurl;

                RequestOptions options = new RequestOptions();

                Glide.with(SiroccoProfile.this).load(Healineurl).apply(options.centerCrop()).thumbnail(0.75f).into(siroocUpadateView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        siroocUpadateView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.SiroccoProfileHealinesR);


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SiroccoProfile.this);
                View mView = getLayoutInflater().inflate(R.layout.max_view_pop_up,null);

                final ImageView saveStatus = mView.findViewById(R.id.PopUpMaxView);

                if(!FinalHeadlineUrl.isEmpty())
                {

                    RequestOptions options = new RequestOptions();
                    Glide.with(SiroccoProfile.this).load(FinalHeadlineUrl).apply(options.centerCrop()).thumbnail(0.75f).into(saveStatus);

                }

                else
                {

                    getSirocoProfile.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {

                            String Healineurl = dataSnapshot.child("image").getValue().toString();

                            FinalHeadlineUrl = Healineurl;

                            RequestOptions options = new RequestOptions();

                            Glide.with(SiroccoProfile.this).load(Healineurl).apply(options.centerCrop()).thumbnail(0.75f).into(siroocUpadateView);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }


                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });


        getUserProfileDataRef.keepSynced(true);


        userImageDp = findViewById(R.id.ImageDP);
        userName = findViewById(R.id.profileName);
        Status = findViewById(R.id.profileStatus);
        mProgressBar = findViewById(R.id.progress_circle);
        //saveinfo = (ImageView) findViewById(R.id.saveInfo);

        mProgressBar.setVisibility(View.INVISIBLE);

    }
}
