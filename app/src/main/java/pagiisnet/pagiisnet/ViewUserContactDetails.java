package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pagiisnet.pagiisnet.R;

public class ViewUserContactDetails extends AppCompatActivity {


    private  ImageView profilePicture, visitMedia;
    private  FloatingActionButton visitContactRequestList,visitUserPosts;


    private  TextView cellField,emailField;

    private FirebaseAuth mAuth;

    String onlineUserId;
    private androidx.appcompat.widget.Toolbar mToolbar;

    private DatabaseReference getUserProfileDataRef;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_contact_details);


        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Contact Details");


        profilePicture = findViewById(R.id.ImageDP);
        visitUserPosts = findViewById(R.id.VisitUserPosts);

        cellField = findViewById(R.id.cellLink);
        emailField = findViewById(R.id.emailLink);

        visitContactRequestList = findViewById(R.id.VisitUserContactRequestList);


        mAuth = FirebaseAuth.getInstance();

        onlineUserId = mAuth.getCurrentUser().getUid();



        final String on_maps_visited_user_id = getIntent().getExtras().get("visit_user_id").toString();


        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users").child(on_maps_visited_user_id);

        getUserProfileDataRef.keepSynced(true);



        visitContactRequestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                findViewById(R.id.VisitUserContactRequestList);

                Intent intent = new Intent(ViewUserContactDetails.this, ViewContactRequestList.class);
                intent.putExtra("visited_user_id","yes");
                startActivity(intent);

            }
        });

        visitUserPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                findViewById(R.id.VisitUserPosts);


                Intent intent = new Intent(ViewUserContactDetails.this, VisitProfilePosts.class);
                intent.putExtra("visit_user_id",on_maps_visited_user_id);
                startActivity(intent);

            }
        });


        getUserProfileDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("mobile_number").getValue().toString();
                String status = dataSnapshot.child("email_address").getValue().toString();
                String imageProfileDP = dataSnapshot.child("userImageDp").getValue().toString();

                //String thumbImage = dataSnapshot.child("userThumbImageDp").getValue().toString();

                //String facebookLink = dataSnapshot.child("facebookLink").getValue().toString();
                //String twitterLink = dataSnapshot.child("twitterLink").getValue().toString();
                //String instagramLink = dataSnapshot.child("instagramLink").getValue().toString();

                cellField.setText(name);//Returning The Email as userName for now...
                emailField.setText(status);

                //facebookLinkTextView.setText(facebookLink);

                //twitterLinkTextView.setText(twitterLink);

                //instagramLinkTextView.setText(instagramLink);
                // Check if The Default Frofile is set or Now

                //String imageUrl = imageProfileDP;

                RequestOptions options = new RequestOptions();

                Glide.with(ViewUserContactDetails.this).load(imageProfileDP).apply(options.centerCrop()).thumbnail(0.75f).into(profilePicture);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {


            }
        });


    }
}
