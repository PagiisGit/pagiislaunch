package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import pagiisnet.pagiisnet.R;

public class LinkPost extends AppCompatActivity {


    private ImageView facebookIcon, twitterIcon, instagramIcon;
    private LinearLayout linkpreview;

    private FirebaseAuth mAuth;

    private ImageView shareLinkButton;

    private CardView userMemelayout;

    private  String myLastLocationDetails;

    private String LinkUrLogo;
    private String urlLinkvalue;

    private String UrlLogo;

    private Calendar calendar;

    private SimpleDateFormat simpleDateFormat;

    private WebView richLinkView;

    private DatabaseReference getUserProfileDataRef;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private final int raterBarValue = 0;

    private  String  shareItemId;

    private ImageView logoViewLink,LinkPostInstruct;

    private String postTimeDateStamp;
    private  String  shareItemIdKey;

    private TextView PostTitle;

    private DatabaseReference mDatabaseRef_x;

    private  DatabaseReference locationDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_post);


        mAuth = FirebaseAuth.getInstance();

        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");


        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        shareItemId = getIntent().getExtras().get("share_item_id").toString();
        shareItemIdKey = getIntent().getExtras().get("share_item_id_key").toString();


        if(mAuth.getCurrentUser() == null)
        {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Link Post ");


        facebookIcon = findViewById(R.id.linkPostFacebookIcon);
        twitterIcon = findViewById(R.id.linkPostTwitter);
        instagramIcon = findViewById(R.id.linkPostInstagram);
        LinkPostInstruct = findViewById(R.id.linkPostInstruction);

        logoViewLink = findViewById(R.id.user_item_view_profilepic);

        linkpreview = findViewById(R.id.TitleId);
        shareLinkButton = findViewById(R.id.ripple_button);

        richLinkView = findViewById(R.id.memeImageView);
        PostTitle = findViewById(R.id.post_title);

        richLinkView = new WebView(getApplicationContext());

        WebViewClient myWebViewClient = new WebViewClient();

        setContentView(richLinkView);

        richLinkView.setWebViewClient(myWebViewClient);


        userMemelayout = findViewById(R.id.userMemeCardView);

        locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation");



        userMemelayout.setVisibility(View.INVISIBLE);
        userMemelayout.setEnabled(false);


        calendar  = Calendar.getInstance();


        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.facebookLinkIcon);
                openFacebookLinkEdit();
            }
        });



        LinkPostInstruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.linkPostInstruction);
                openAnyLinkPost();
            }
        });

        LinkPostInstruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.linkPostInstruction);
                openAnyLinkPost();
            }
        });


        shareLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.ripple_button);

                checkifLinkShare();
                //shareLinkPost();
            }
        });

        instagramIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.instagramIcon);
                openInstagramLinkEdit();
            }
        });


        twitterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.tweeterIcon);
                openTwitterLinkEdit();
            }
        });

        if(shareItemId != null && shareItemId.compareTo("null")!= 0)
        {

            checkifLinkShare();

        }


    }

    private void openAnyLinkPost() {


        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(LinkPost.this);

        View mView = getLayoutInflater().inflate(R.layout.any_link_post,null);

        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("uploadsLink");
        UrlLogo = "https://firebasestorage.googleapis.com/v0/b/pagiis-ix.appspot.com/o/default_files%2Ffacebook_icon.png?alt=media&token=abd37150-770b-4760-b82d-2b0759b1764a";



        final EditText newStatus = mView.findViewById(R.id.facebookLinkEdit);
        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);

        saveStatus.setOnClickListener(new View.OnClickListener() {

            //String NewStatus = newStatus.getText().toString().trim();

            final String current_userId = mAuth.getCurrentUser().getUid();

            final String saveRaterBarValue = String.valueOf(raterBarValue);

            final String linkUrl = "https://firebasestorage.googleapis.com/v0/b/pagiis-ix.appspot.com/o/default_files%2Ffacebook_icon.png?alt=media&token=abd37150-770b-4760-b82d-2b0759b1764a";


            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveStatus);



                simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

                postTimeDateStamp = simpleDateFormat.format(calendar.getTime());
                if(!(newStatus.getText() == null))
                {


                    userMemelayout.setVisibility(View.VISIBLE);
                    urlLinkvalue = newStatus.getText().toString();
                    setLinkView();
                    AlertDialog dialog = mBuilder.create();
                    dialog.dismiss();
                }else
                {
                    Toast.makeText(LinkPost.this, "Please type in your facebookLink or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void setLinkView() {


        if(urlLinkvalue != null && !urlLinkvalue.isEmpty() && Patterns.WEB_URL.matcher(urlLinkvalue).matches())
        {
            richLinkView.loadUrl(urlLinkvalue);
            PostTitle.setText(urlLinkvalue);
        }else
        {
            Toast.makeText(this, "link invalid", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    private void myLastLocationDetailsMetod()
    {

        String user_id = mAuth.getCurrentUser().getUid();

        locationDetails = FirebaseDatabase.getInstance().getReference().child("MyLastLocation").child(user_id);

        locationDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    myLastLocationDetails = dataSnapshot.getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                String e = databaseError.getMessage();
                Toast.makeText(LinkPost.this, "last location not found"+"Results"+":"+e, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void openFacebookLinkEdit()
    {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(LinkPost.this);

        View mView = getLayoutInflater().inflate(R.layout.alert_facebook_link_edit,null);

        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("uploadsLink");
        UrlLogo = "https://firebasestorage.googleapis.com/v0/b/pagiis-ix.appspot.com/o/default_files%2Ffacebook_icon.png?alt=media&token=abd37150-770b-4760-b82d-2b0759b1764a";



        final EditText newStatus = mView.findViewById(R.id.facebookLinkEdit);
        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);

        saveStatus.setOnClickListener(new View.OnClickListener() {

            //String NewStatus = newStatus.getText().toString().trim();

            final String current_userId = mAuth.getCurrentUser().getUid();

            final String saveRaterBarValue = String.valueOf(raterBarValue);

            final String linkUrl = "https://firebasestorage.googleapis.com/v0/b/pagiis-ix.appspot.com/o/default_files%2Ffacebook_icon.png?alt=media&token=abd37150-770b-4760-b82d-2b0759b1764a";


            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveStatus);



                simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

                postTimeDateStamp = simpleDateFormat.format(calendar.getTime());
                if(!(newStatus.getText() == null))
                {


                    userMemelayout.setVisibility(View.VISIBLE);
                    urlLinkvalue = newStatus.getText().toString();
                    setLinkView();
                    AlertDialog dialog = mBuilder.create();
                    dialog.dismiss();
                }else
                {
                    Toast.makeText(LinkPost.this, "Please type in your facebookLink or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    private void openInstagramLinkEdit()
    {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(LinkPost.this);

        View mView = getLayoutInflater().inflate(R.layout.alert_instagram_link,null);

        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("uploadsLink");

        final EditText newStatus = mView.findViewById(R.id.instagramLinkEdit);

        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);

         UrlLogo = "https://firebasestorage.googleapis.com/v0/b/pagiis-ix.appspot.com/o/default_files%2Finsta_icon.jpg?alt=media&token=c8852f2a-65e6-4ff4-9377-b3c00a88598e";



        saveStatus.setOnClickListener(new View.OnClickListener() {


            //String NewStatus = newStatus.getText().toString().trim();

            final String current_userId= mAuth.getCurrentUser().getUid();

            final String saveRaterBarValue = String.valueOf(raterBarValue);

            final String linkUrl = "https://firebasestorage.googleapis.com/v0/b/pagiis-ix.appspot.com/o/default_files%2Finsta_icon.jpg?alt=media&token=c8852f2a-65e6-4ff4-9377-b3c00a88598e";

            @Override
            public void onClick(View view) {


                view.findViewById(R.id.saveStatus);

                simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

                postTimeDateStamp = simpleDateFormat.format(calendar.getTime());
                if(!(newStatus.getText() == null))
                {
                    userMemelayout.setVisibility(View.VISIBLE);
                    urlLinkvalue = newStatus.getText().toString();
                    setLinkView();
                    AlertDialog dialog = mBuilder.create();
                    dialog.dismiss();


                }else
                {
                    Toast.makeText(LinkPost.this, "Please type in your status or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    private void openTwitterLinkEdit() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(LinkPost.this);
        View mView = getLayoutInflater().inflate(R.layout.alert_twitter_link_edit,null);
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("uploadsLink");

        UrlLogo = "https://firebasestorage.googleapis.com/v0/b/pagiis-ix.appspot.com/o/default_files%2Ftweeter_icon.png?alt=media&token=f4b143f4-69f6-4a0d-8a19-8b236b77c85f";

        final EditText newStatus = mView.findViewById(R.id.twitterLinkEdit);
        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);

        saveStatus.setOnClickListener(new View.OnClickListener() {

           // String NewStatus = newStatus.getText().toString().trim();

            final String current_userId= mAuth.getCurrentUser().getUid();

            final String saveRaterBarValue = String.valueOf(raterBarValue);

            final String urlLink = "https://firebasestorage.googleapis.com/v0/b/pagiis-ix.appspot.com/o/default_files%2Ftweeter_icon.png?alt=media&token=f4b143f4-69f6-4a0d-8a19-8b236b77c85f";

            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveStatus);

                simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

                postTimeDateStamp = simpleDateFormat.format(calendar.getTime());
                if(!(newStatus.getText() == null))
                {
                    userMemelayout.setVisibility(View.VISIBLE);
                    urlLinkvalue = newStatus.getText().toString();
                    setLinkView();
                    AlertDialog dialog = mBuilder.create();
                    dialog.dismiss();
                }else
                {


                    Toast.makeText(LinkPost.this, "Please type in your status or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    private void checkifLinkShare()
    {

        shareItemId = getIntent().getExtras().get("share_item_id").toString();
        shareItemIdKey = getIntent().getExtras().get("share_item_id_key").toString();

        if(shareItemId != null && shareItemId.compareTo("null") != 0)
        {
            shareLinkPost();

        }else
        {
            getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("uploadsLink");

            simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

            String current_userId= mAuth.getCurrentUser().getUid();

            postTimeDateStamp = simpleDateFormat.format(calendar.getTime());
            if(!(urlLinkvalue == null))
            {

                ImageUploads upload = new ImageUploads(urlLinkvalue,urlLinkvalue,urlLinkvalue,current_userId,urlLinkvalue,urlLinkvalue,urlLinkvalue,postTimeDateStamp,myLastLocationDetails,"");

                getUserProfileDataRef.child(current_userId)
                        .push()
                        .setValue(upload, new DatabaseReference.CompletionListener()
                        {
                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   DatabaseReference databaseReference) {

                                //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                //Toast.makeText(getApplicationContext(), "PAGiiS image upload successful !!", Toast.LENGTH_SHORT).show();
                                //Intent intent = new Intent(getApplicationContext(), MemePage.class);
                                //startActivity(intent);
                                // String uniqueKey = databaseReference.getKey();
                                //Create the function for Clearing/The ImageView Widget.

                                Intent intent = new Intent(getApplicationContext(), MemePage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });


            }else
            {
                Toast.makeText(LinkPost.this, "Please type in your status or discard", Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void shareLinkPost()

    {

        simpleDateFormat= new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

        postTimeDateStamp = simpleDateFormat.format(calendar.getTime());


        String current_userId= mAuth.getCurrentUser().getUid();

        shareItemId = getIntent().getExtras().get("share_item_id").toString();
        shareItemIdKey = getIntent().getExtras().get("share_item_id_key").toString();



        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("uploadsLink");

        shareItemId = getIntent().getExtras().get("share_item_id").toString();

        final String userId = mAuth.getCurrentUser().getUid();
        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference().child("uploadsLink").child(shareItemIdKey);


        getUserProfileDataRef.child(shareItemId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String url = dataSnapshot.child("imageUrl").getValue().toString();

                ImageUploads upload = new ImageUploads(url,url,UrlLogo,current_userId,url,url,url,postTimeDateStamp,myLastLocationDetails,"");
                mDatabaseRef_x.child(current_userId).push()
                        .setValue(upload, new DatabaseReference.CompletionListener()
                        {
                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   DatabaseReference databaseReference) {

                                //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                //Toast.makeText(getApplicationContext(), "PAGiiS image upload successful !!", Toast.LENGTH_SHORT).show();
                                //Intent intent = new Intent(getApplicationContext(), MemePage.class);
                                //startActivity(intent);
                                // String uniqueKey = databaseReference.getKey();
                                //Create the function for Clearing/The ImageView Widget.

                                Intent intent = new Intent(getApplicationContext(), MemePage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                Toast.makeText(LinkPost.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }

}
