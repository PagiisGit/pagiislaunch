package pagiisnet.pagiisnet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pagiisnet.pagiisnet.R;

public class RequestMaxView extends AppCompatActivity {


    private TextView InviteName;
    private TextView EventName;
    private TextView inviteText;
    private TextView RequestStatus;
    private Button yes;

    private Button no;
    private ImageView inviteProfilePic;
    private ImageView eventProfilePic;
    private DatabaseReference mDatabaseStoreItems;


    private DatabaseReference mDatabaseEventAttendance;

    private ImageView back;

    private String carrierValue;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_max_view);

        InviteName = findViewById(R.id.inviteName);
        EventName = findViewById(R.id.EventName);
        inviteText = findViewById(R.id.inviteDisplayText);


        no = findViewById(R.id.friendRequestDecline);
        yes = findViewById(R.id.friendRequestAccept);
        inviteProfilePic = findViewById(R.id.inviteProfilePic);
        eventProfilePic = findViewById(R.id.eventProfilePic);

        RequestStatus = findViewById(R.id.requestStatus);

        back = findViewById(R.id.backToRequest);


        mDatabaseStoreItems = FirebaseDatabase.getInstance().getReference("EventUploadsAndRipples");
        mDatabaseEventAttendance =FirebaseDatabase.getInstance().getReference("EventAttendance");



        carrierValue = getIntent().getExtras().get("visit_user_id").toString();




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.backToRequest);

                Intent intent = new Intent(RequestMaxView.this,ActivityNotifications.class );
                startActivity(intent);

            }
        });


        no.setOnClickListener(new View.OnClickListener() {

            final String user_id = mAuth.getCurrentUser().getUid();

            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.friendRequestDecline);

                mDatabaseStoreItems.child(user_id).child(carrierValue).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        if(dataSnapshot.exists())
                        {
                            mDatabaseStoreItems.child("share").setValue("NO").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {

                                    mDatabaseStoreItems.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            Intent intent = new Intent(RequestMaxView.this,ActivityNotifications.class);
                                            //putValue ere user id
                                            startActivity(intent);

                                        }
                                    });

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                        String e = databaseError.getMessage();
                        Toast.makeText(RequestMaxView.this, "error "+" "+"results"+":"+e, Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });


            }
        });



        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.friendRequestAccept);


                final String user_id = mAuth.getCurrentUser().getUid();


                view.findViewById(R.id.friendRequestDecline);



                mDatabaseStoreItems.child("EventUploadsAndRipples").child(user_id).child(carrierValue).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        if(dataSnapshot.exists())
                        {



                            mDatabaseStoreItems.child("share").setValue("YES").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {

                                    // RequestStatus.setText("Attending");
                                    // yes.setText("live");
                                    // no.setText("Now");

                                    mDatabaseEventAttendance.child(carrierValue).child(user_id).child("admin").setValue("false");


                                    mDatabaseEventAttendance.child(carrierValue).child(user_id).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {

                                            Intent intent = new Intent(RequestMaxView.this,ActivityNotifications.class);
                                            //putValue ere user id

                                            intent.putExtra("user_id",user_id);
                                            startActivity(intent);

                                        }
                                    });




                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });

            }
        });








        mAuth = FirebaseAuth.getInstance();


        mDatabaseStoreItems = FirebaseDatabase.getInstance().getReference().child("EventUploads");


        String user_id = mAuth.getCurrentUser().getUid();



        mDatabaseStoreItems.child("EventUploadsAndRipples").child(user_id).child(carrierValue).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String nameProfilePic = dataSnapshot.child("inviteProfilePic").getValue().toString();
                    String inviteDisplayText = dataSnapshot.child("inviteDisplayText").getValue().toString();
                    String eventName = dataSnapshot.child("eventName").getValue().toString();
                    String eventProfilePick = dataSnapshot.child("eventProfielPic").getValue().toString();


                    RequestOptions options = new RequestOptions();


                    EventName.setText(eventName);
                    InviteName.setText(name);
                    inviteText.setText(inviteDisplayText);


                    Glide.with(getApplicationContext())
                            .load(eventProfilePick)
                            .apply(options.centerCrop())
                            .into(eventProfilePic);


                    Glide.with(getApplicationContext())
                            .load(nameProfilePic)
                            .apply(options.centerCrop())
                            .into(inviteProfilePic);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

}