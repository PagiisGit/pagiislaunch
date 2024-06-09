package pagiisnet.pagiisnet;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pagiisnet.pagiisnet.Utils.LiveEventsAndRipplesAdaptor;
import pagiisnet.pagiisnet.Utils.NotificationsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment implements NotificationsAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    private static final String TAG = "ActivityNotifications";
    private static final int ACTION_NUM = 0;
    String MaxImageUrl;
    String online_user_id;
    private RecyclerView friendRequestListRecV;
    private RecyclerView LiveRecyclerView;
    private NotificationsAdapter mAdapter;
    private LiveEventsAndRipplesAdaptor mapsAdapter;
    private View mMainView;
    private DatabaseReference friendRequestUserRef;
    private DatabaseReference mDatabaseRef_x;
    private Button createEvent;
    private List<tags> tagedUsers;
    private List<ImageUploads> radiusUserNotifications;
    private FirebaseAuth mAuth;
    private ValueEventListener mDBlistener;
    private ProgressBar mProgressCircle;
    private List<ImageUploads> mUploadsRequests;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private DatabaseReference usersRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference friendsDatabaseRef;
    private DatabaseReference mDatabaseStoreItems;
    private DatabaseReference friendsRequestDataBRef;

    private DatabaseReference mDatabaseEventAttendance;


    private FloatingActionButton AdminRequest;
    private FloatingActionButton AdminRequest_live;


    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        FirebaseApp.initializeApp(getApplicationContext());

        tagedUsers = new ArrayList<>();
        mUploadsRequests = new ArrayList<>();
        radiusUserNotifications = new ArrayList<>();


        mDatabaseEventAttendance = FirebaseDatabase.getInstance().getReference("EventAttendance");

        //friendRequestListRecV.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();

        String online_user_id = mAuth.getCurrentUser().getUid();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        //String on_maps_visited_user_id = getActivity().getIntent().getExtras().get("share_item_id").toString();


        friendsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        friendsRequestDataBRef = FirebaseDatabase.getInstance().getReference().child("Friend_Request");

        mDatabaseStoreItems = FirebaseDatabase.getInstance().getReference().child("EventUploadsAndRipples");

        friendRequestUserRef = FirebaseDatabase.getInstance().getReference().child("Friends_Requests").child(online_user_id);

        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("PagiisNotifications");

        getDataNormally();

    }//OnCreate Ends Here


    private void getDataNormally() {

        final String userIdRef = mAuth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("WalkinWall");
        //mDatabaseEventAttendance =FirebaseDatabase.getInstance().getReference("PagiisNotice");

        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    radiusUserNotifications.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        ImageUploads upload = ds.getValue(ImageUploads.class);
                        upload.setKey(ds.getKey());
                        radiusUserNotifications.add(upload);

                    }

                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                } else {
                    paiisNotice();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

        });
    }

    private void paiisNotice() {

        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("PagiisNotice");
        //mDatabaseEventAttendance =FirebaseDatabase.getInstance().getReference("EventAttendance");

        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        mDatabaseRef_x.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    radiusUserNotifications.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        ImageUploads upload = ds.getValue(ImageUploads.class);
                        upload.setKey(ds.getKey());
                        radiusUserNotifications.add(upload);

                    }

                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

        });
    }

    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rooView = inflater.inflate(R.layout.fragment_notification, container, false);


        friendRequestListRecV = rooView.findViewById(R.id.notificationRecyclerView);
        friendRequestListRecV.setHasFixedSize(true);
        friendRequestListRecV.setLayoutManager(new LinearLayoutManager(getActivity()));


        mAdapter = new NotificationsAdapter(getActivity(), radiusUserNotifications);


        mAdapter.setOnItemClickListener(NotificationFragment.this);


        friendRequestListRecV.setAdapter(mAdapter);


        //friendRequestListRecV.setLayoutManager(gridLayoutManager);


        mProgressCircle = rooView.findViewById(R.id.progress_circle_user_memes);


        return rooView;
    }//This is the End of OnCreateView


    @Override
    public void onNItemClick(int position) {

        ImageUploads selectedImage = radiusUserNotifications.get(position);

        String selectedKey = selectedImage.getKey();

        String imageUrl = selectedImage.getImageUrl();

        String maxImageUserId = selectedImage.getUserId();

        if (!imageUrl.isEmpty() && !selectedKey.isEmpty()) {
            Intent intent = new Intent(getActivity(), PagiisMaxView.class);
            intent.putExtra("imageKeyMAx", selectedKey);
            intent.putExtra("imageUrlMax", imageUrl);
            intent.putExtra("imageUserId", maxImageUserId);
            intent.putExtra("From", "Notification");
            intent.putExtra("orderLink","null");
            startActivity(intent);

        } else {
            Toast.makeText(getActivity(), "Pagiis failed to open maxview.", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onWhatEverClick(int position) {

        mDatabaseStoreItems = FirebaseDatabase.getInstance().getReference().child("PagiisNotification");

        mDatabaseEventAttendance = FirebaseDatabase.getInstance().getReference("EventAttendance");

        final ImageUploads selectedImage = radiusUserNotifications.get(position);

        final String selectedKey = selectedImage.getKey();

        final String user_id = mAuth.getCurrentUser().getUid();

        String getUserRef = selectedImage.getUserId();
        mDatabaseStoreItems.child("EventUploadsAndRipples").child(user_id).child(selectedKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    mDatabaseStoreItems.child("share").setValue("YES").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            mDatabaseEventAttendance.child(selectedKey).child(user_id).child("admin").setValue("false");

                            mDatabaseEventAttendance.child(selectedKey).child("attendin").child(user_id).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Intent intent = new Intent(getActivity(), ViewEvent.class);
                                    //putValue ere user id
                                    intent.putExtra("visit_user_id", selectedKey);
                                    startActivity(intent);

                                }
                            });


                        }
                    });

                } else {
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                String e = databaseError.getMessage();

                Toast.makeText(getActivity(), "Error occured " + "_result" + ":" + e, Toast.LENGTH_SHORT).show();

            }
        });

    }



    @Override
    public void onStart() {
        super.onStart();

    }
}