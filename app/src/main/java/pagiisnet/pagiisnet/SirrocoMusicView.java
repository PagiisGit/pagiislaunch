package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SirrocoMusicView extends AppCompatActivity {

    private ImageView maxView;
    private ImageView back;
    private TextView modelName, brandName;

    private FirebaseAuth mAuth;


    private ImageView gmgImage;
    private ImageView YoutubeOImageView;

    private ProgressBar mProgressBar;

    private DatabaseReference mDataRef;

    private androidx.appcompat.widget.Toolbar mToolbar;
    private String ImageUrl;
    private String imageKey;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sirroco_music_view);


        maxView = findViewById(R.id.musicImageView);
        modelName = findViewById(R.id.YouTubeLink);
        brandName = findViewById(R.id.downloadLink);
        back = findViewById(R.id.exitMusicActivity);


        gmgImage = findViewById(R.id.GMG_LOGO);
        YoutubeOImageView = findViewById(R.id.Youtube_Logo);


        gmgImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void
            onClick(View view)
            {
                view.findViewById(R.id.GMG_LOGO);


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SirrocoMusicView.this);
                View mView = getLayoutInflater().inflate(R.layout.max_view_pop_up,null);

                final ImageView saveStatus = mView.findViewById(R.id.PopUpMaxView);

                saveStatus.setImageDrawable(ContextCompat.getDrawable(SirrocoMusicView.this,R.id.GMG_LOGO));

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });


        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Sir_occo **Music**");

        mProgressBar = findViewById(R.id.SirrocoMaxProgressBar);

        mProgressBar.setVisibility(View.VISIBLE);
        ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();
        imageKey = getIntent().getExtras().get("imageKeyMAx").toString();
        getSiroccoMaxView();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.exitMusicActivity);

                Intent intent = new Intent(SirrocoMusicView.this, SiroccoPage.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void getSiroccoMaxView()
    {

        if (!imageKey.isEmpty() && !ImageUrl.isEmpty()) {

            mDataRef = FirebaseDatabase.getInstance().getReference().child("SiroccoMusicUploads");

            ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();

            imageKey = getIntent().getExtras().get("imageKeyMAx").toString();

            mDataRef.child(imageKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {


                            RequestOptions options = new RequestOptions();
                            Glide.with(SirrocoMusicView.this).load(ImageUrl).apply(options.centerCrop()).thumbnail(0.75f).into(maxView);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            String BrandName = dataSnapshot.child("name").getValue().toString();
                            String ModelName = dataSnapshot.child("modelName").getValue().toString();
                            brandName.setText(BrandName);
                            modelName.setText(ModelName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)

                {
                }
            });
        } else
            {

            Intent intent = new Intent(SirrocoMusicView.this, ViewUsersMemes.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

    }
}
