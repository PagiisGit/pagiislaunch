package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import pagiisnet.pagiisnet.R;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import pagiisnet.pagiisnet.Utils.PurchasedItemAdaptor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class CartFragment extends Fragment implements PurchasedItemAdaptor.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    private RecyclerView mRecyclerView;

    private PurchasedItemAdaptor mAdapter;

    private ImageView userImageDp;

    private TextView userName;

    private int adapterPosition;

    private String ProfileUserName;

    private DatabaseReference userProfileInfor;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private ProgressBar mProgressCircle;

    private ImageView UploadMemeButton;

    private FirebaseAuth mAuth;

    private FirebaseStorage mStorage;

    private String DeleteKeyAlternative;

    private FloatingActionButton videosButton;

    private DatabaseReference mDatabaseRef;

    private ValueEventListener mDBlistener;

    private List<ImageUploads> mUploads;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAuth = FirebaseAuth.getInstance();


        String User_id = mAuth.getCurrentUser().getUid();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoPurchasedItem");
        userProfileInfor = FirebaseDatabase.getInstance().getReference("Users").child(User_id);


        userProfileInfor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String pprofileUsername = dataSnapshot.child("userNameAsEmail").getValue().toString();
                ProfileUserName = pprofileUsername;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (mAuth.getCurrentUser() == null) {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

        }


        mStorage = FirebaseStorage.getInstance();


        mUploads = new ArrayList<>();

        mAdapter = new PurchasedItemAdaptor(getActivity(), mUploads);

        mAdapter.setOnItemClickListener(CartFragment.this);

        mRecyclerView.setAdapter(mAdapter);

        final String user_id = mAuth.getCurrentUser().getUid();


        mDatabaseRef.orderByChild("userId").equalTo(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                    upload.setKey(postSnapshot.getKey());
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

    @SuppressLint({"RestrictedApi", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);


        mRecyclerView = rootView.findViewById(R.id.memeRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mProgressCircle = rootView.findViewById(R.id.progress_circle);


        userImageDp = rootView.findViewById(R.id.ImageDP);

        videosButton = rootView.findViewById(R.id.goToVideos);

        videosButton.setVisibility(View.INVISIBLE);
        userName = rootView.findViewById(R.id.profileName);

        videosButton.setEnabled(false);


        return rootView;
    }

    @Override
    public void onItemClick(int position) {

        ImageUploads selectedImage = mUploads.get(position);

        String selectedKey = selectedImage.getKey();

        String imageUrl = selectedImage.getImageUrl();

        String maxImageUserId = selectedImage.getUserId();

        String storeMainKey = selectedImage.getViews();

        if (!imageUrl.isEmpty() && !selectedKey.isEmpty()) {
            Intent intent = new Intent(getActivity(), PagiisMaxView.class);
            intent.putExtra("imageKeyMAx", selectedKey);
            intent.putExtra("imageUrlMax", imageUrl);
            intent.putExtra("imageExRater", maxImageUserId);
            intent.putExtra("imageStoreMainKey", storeMainKey);
            startActivity(intent);

        } else {
            Toast.makeText(getActivity(), "Pagiis failed to open maxview.", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onWhatEverClick(int position) {

        Intent intent = new Intent(getActivity(), ActivityOwnProfile.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        DeleteKeyAlternative = selectedKey;

        String myUserId = mAuth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(myUserId);

        //StorageReference deletImage = mStorage.getReferenceFromUrl(selectedImage.getImageUrl());


        mDatabaseRef.child(selectedKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Item sent to bin..", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {


            String errorText;

            @Override
            public void onFailure(@NonNull Exception e) {

                errorText = String.valueOf(e);
                Toast.makeText(getActivity(), errorText + " Deletion Will restart in few seconds", Toast.LENGTH_LONG).show();
                deleteAlternative();


            }
        });
    }


    private void deleteAlternative() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            String DeleteFinalString;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().compareTo(DeleteKeyAlternative) == 0) {

                        mDatabaseRef.child(DeleteKeyAlternative).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Item sent to bin..", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                DeleteFinalString = String.valueOf(e);
                                Toast.makeText(getActivity(), DeleteFinalString + "PAGiiS Failed to Delete File, please try again :Later", Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "item delete failed.", Toast.LENGTH_SHORT).show();

            }
        });

    }


}