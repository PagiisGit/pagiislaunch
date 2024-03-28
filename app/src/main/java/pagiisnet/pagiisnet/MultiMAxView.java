package pagiisnet.pagiisnet;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pagiisnet.pagiisnet.R;

public class MultiMAxView extends AppCompatActivity {

    private ImageView maxView;
    private TextView modelName, brandName;

    private FirebaseAuth mAuth;

    private ProgressBar mProgressBar;

    private DatabaseReference mDataRef;

    private androidx.appcompat.widget.Toolbar mToolbar;



    private DatabaseReference storeRef;
    private String ImageUrl;
    private String imageKey;
    private String imageExRater;

    private ImageView openShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        modelName.setMovementMethod(LinkMovementMethod.getInstance());

        brandName.setMovementMethod(LinkMovementMethod.getInstance());

        mProgressBar = findViewById(R.id.SirrocoMaxProgressBar);

        openShop = findViewById(R.id.OnlineShop);

        openShop.setVisibility(View.INVISIBLE);

        mProgressBar.setVisibility(View.VISIBLE);

        ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();

        imageKey = getIntent().getExtras().get("imageKeyMAx").toString();

        imageExRater = getIntent().getExtras().get("imageExRater").toString();

        getSiroccoMaxView();

        checkIfItemIsOnShop();

        openShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(MultiMAxView.this, CustomerPurchase.class);
                intent.putExtra("imageKeyMAx", imageKey);
                intent.putExtra("imageUrlMax",ImageUrl);
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
            public void onCancelled(@NonNull DatabaseError databaseError)

            {

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

                    Glide.with(MultiMAxView.this).load(ImageUrl).apply(options.centerCrop()).thumbnail(0.75f).into(maxView);
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
            Intent intent = new Intent(MultiMAxView.this, ViewUsersMemes.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

    }

}
