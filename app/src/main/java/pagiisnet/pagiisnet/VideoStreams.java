package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.Utils.UserVideoViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoStreams#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoStreams extends Fragment  implements  UserVideoViewAdapter.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideoStreams() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoStreams.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoStreams newInstance(String param1, String param2) {
        VideoStreams fragment = new VideoStreams();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    private RecyclerView mRecyclerView;
    private UserVideoViewAdapter mAdapter;

    private ProgressBar mProgressCircle;

    //private DatabaseReference userRef;

    private FirebaseAuth mAuth;

    private FirebaseStorage mStorage;

    private  enum  VolumeState {ON,OFF}

    private  String myLastLocationDetails;

    private VideoStreams.VolumeState volumeState;

    private String getKey;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_x;
    private DatabaseReference mDatabaseRef_y;
    private DatabaseReference lockedImages;
    private ValueEventListener mDBlistener;

    private List<ImageUploads> mUploads;

    private DatabaseReference getUserProfileDataRef;

    private Iterator<tags> iterator;

    private List<tags> tagedUsers;

    private VideoView videoPlay;

    private MediaPlayer Mp;

    private ImageView closeVideoPlay;

    private CardView videoPlayCard;

    private int stopPosition;

    private List<pagiisnet.pagiisnet.lockedImagesTag> tagedImages;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private final int raterBarValue = 0;

    private FloatingActionButton viewMyVideos;


    String myName;

    String userStatusMessage;

    String myImageDpUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance();

        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");
        getUserProfileDataRef.keepSynced(true);




        if(mAuth.getCurrentUser() == null)
        {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        mUploads = new ArrayList<>();

        tagedUsers= new ArrayList<>();
        tagedImages= new ArrayList<>();



        mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads");
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");
        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference("LockedImages");


        if(tagedUsers.isEmpty() && !tagedImages.isEmpty() )
        {
            updateArrayList();

        }else
        {

            getDataNormally();

        }

       // startVideoPlay();
    }


    private void updateArrayList()
    {
        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("Tags");

        String currentUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef_x.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        String localID = ds.getKey();

                        tagedUsers.add(new tags(localID));
                        if(dataSnapshot.getChildrenCount() >=1 && tagedUsers.size()>=1 | dataSnapshot.getChildrenCount()==tagedUsers.size() )
                        {
                            mDatabaseRef_x.child(localID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    getDataNormally();
                                }
                            });
                        }else
                        {

                            Toast.makeText( getActivity(), "Online posts currently unavailable.", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }

                }else
                {
                    Toast.makeText(getActivity(), "Pagiis online users currently unavailable.", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                String e = databaseError.getMessage();

                Toast.makeText(getActivity(), "Video play failed "+"results"+":"+e, Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void getDataNormally() {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads");
        final String userIdRef = mAuth.getCurrentUser().getUid();
        //final String on_maps_visited_user_id = String.valueOf(getIntent().getExtras().get("visit_user_id").toString());
        if(!tagedUsers.isEmpty()) {

            for (int i = 0; i < tagedUsers.size(); i++)

            {
                tags x = tagedUsers.get(i);

                String taged_id = x.getUser_tagID();

                if(taged_id.compareTo(userIdRef) !=0)
                {

                    mDBlistener = mDatabaseRef.child(taged_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mUploads.clear();

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ImageUploads upload = ds.getValue(ImageUploads.class);
                                upload.setKey(ds.getKey());
                                mUploads.add(upload);
                            }

                            mAdapter.notifyDataSetChanged();
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }


                    });

                }


            }


        }else
        {
            Toast.makeText(getActivity(), "Pagiis online posts currently unavailable.", Toast.LENGTH_SHORT).show();

        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_streams, container, false);

        videoPlay = rootView.findViewById(R.id.memeImageViewVideo);
        closeVideoPlay = rootView.findViewById(R.id.closeVideoPlay);
        videoPlayCard = rootView.findViewById(R.id.videoViewCard);
        videoPlayCard.setVisibility(View.INVISIBLE);



        viewMyVideos = rootView.findViewById(R.id.contentRating);

        mRecyclerView = rootView.findViewById(R.id.memeRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //returnToMaps = findViewById(R.id.returnToMaps);

        mProgressCircle = rootView.findViewById(R.id.progress_circle_user_memes);

        mProgressCircle.setVisibility(View.VISIBLE);


        mAdapter = new UserVideoViewAdapter(getActivity(), mUploads);

        mAdapter.setOnItemClickListener(VideoStreams.this);

        mRecyclerView.setAdapter(mAdapter);


        viewMyVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.contentRating);

                Intent intent = new Intent(getActivity(),VideowViewing.class);

                startActivity(intent);

            }
        });

        closeVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.closeVideoPlay);

                videoPlayCard.setVisibility(View.INVISIBLE);
                videoPlay.stopPlayback();
                videoPlay.setVideoURI(Uri.parse(""));

            }
        });

        startVideoPlay();


        return rootView;
    }

    @Override
    public void onClick(int position) {


        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String imageUrl = selectedImage.getImageUrl();

        final String userKeyId = selectedImage.getUserId();

        if (imageUrl != null) {

            if(!videoPlay.isPlaying()) {

                videoPlayCard.setVisibility(View.VISIBLE);
                videoPlay.setVideoURI(Uri.parse(imageUrl));
                videoPlay.start();
                mProgressCircle.setVisibility(View.VISIBLE);

                mDatabaseRef = FirebaseDatabase.getInstance().getReference("videoUploads").child(userKeyId).child(selectedKey);

                lockedImages = FirebaseDatabase.getInstance().getReference("Likes").child(selectedKey).child(userKeyId);
                //mDatabaseRef_z = FirebaseDatabase.getInstance().getReference("Views").child(selectedKey);

                if (!imageUrl.isEmpty() && !selectedKey.isEmpty()) {

                    lockedImages.addValueEventListener(new ValueEventListener() {

                        String UnitLikes;

                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                            //String name, String imageUrl, String rateEx, String userId,String views,String likes, String share

                            lockedImages.child("name").setValue(myName);
                            lockedImages.child("imageUrl").setValue(imageUrl);
                            lockedImages.child("rateEx").setValue(userStatusMessage);
                            lockedImages.child("userId").setValue(userKeyId);
                            lockedImages.child("views").setValue("1");
                            lockedImages.child("likes").setValue("1");
                            lockedImages.child("share").setValue(myImageDpUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    UnitLikes = String.valueOf(Objects.requireNonNull(dataSnapshot).getChildrenCount() + 1);

                                    mDatabaseRef.child("likes").setValue(UnitLikes);
                                    mDatabaseRef.child("views").setValue(UnitLikes);
                                    lockedImages.child("userId").setValue(UnitLikes);
                                    lockedImages.child("views").setValue(UnitLikes);
                                    mProgressCircle.setVisibility(View.INVISIBLE);

                                    //mDatabaseRef.child("likes").setValue(String.valueOf(dataSnapshot.getChildrenCount()));
                                    //mDatabaseRef.child("views").setValue(String.valueOf(dataSnapshot.getChildrenCount()));

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getActivity(), "Click to like item.", Toast.LENGTH_SHORT).show();
                        }
                    });


                    videoPlay.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {

                            Mp = mediaPlayer;
                            if (i == MediaPlayer.MEDIA_INFO_BUFFERING_START) {


                                stopPosition = videoPlay.getCurrentPosition();
                                mProgressCircle.setVisibility(View.VISIBLE);
                                videoPlay.pause();
                            } else if (i == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                                mProgressCircle.setVisibility(View.INVISIBLE);
                                videoPlay.seekTo(stopPosition);
                                videoPlay.start();

                            }
                            return false;
                        }
                    });


                }

            }


        }

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClickMute(int position) {

    }


    private MediaController mc;


    private void startVideoPlay() {


        videoPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {

                        mc = new MediaController(getActivity());
                        videoPlay.setMediaController(mc);
                        videoPlay.requestFocus();
                        mc.setAnchorView(videoPlay);

                    }
                });
            }
        });
    }

    @Override
    public void onWhatEverClick(int position) {


        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        String getUserRef = selectedImage.getUserId();

        if(!getUserRef.isEmpty() && !selectedKey.isEmpty())
        {
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            intent.putExtra("visit_user_id", getUserRef);
            startActivity(intent);
        }else
        {
            Toast.makeText(getActivity(), "Pagiis failed to delete file. the system will retry deleting file later ", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public void onStop() {
        super.onStop();
        tagedUsers.clear();
    }

}