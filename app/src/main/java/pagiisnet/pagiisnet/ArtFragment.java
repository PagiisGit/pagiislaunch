package pagiisnet.pagiisnet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import pagiisnet.pagiisnet.Utils.MapsProfileAdapter;
import pagiisnet.pagiisnet.Utils.ViewStoreCatergoryAdaptor;
import pagiisnet.pagiisnet.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtFragment extends Fragment implements ViewStoreCatergoryAdaptor.OnItemClickListener, MapsProfileAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArtFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtFragment newInstance(String param1, String param2) {
        ArtFragment fragment = new ArtFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private RecyclerView mRecyclerView;
    private RecyclerView mapsRecyclerView;
    private ImageView siroccoProfileImage;
    private ImageView siroccoProfileImageR;
    private FloatingActionButton gotoMusic;
    private ViewStoreCatergoryAdaptor mAdapter;
    private ImageView userImageDp;

    private TextView userName;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private ProgressBar mProgressCircle;

    private ImageView UploadMemeButton;

    private FirebaseAuth mAuth;

    private FirebaseStorage mStorage;

    private DatabaseReference mDatabaseRef;

    private DatabaseReference nDatabaseRef;

    private ValueEventListener mDBlistener;

    private ValueEventListener mDataBaseListener;

    private List<ImageUploads> mUploads;

    private List<ImageUploads> mapsUploads;

    private MapsProfileAdapter mapsAdapter;

    private ImageView musicReplacement;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);

        //  mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        mapsRecyclerView.setLayoutManager(linearLayoutManager);


        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() == null) {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

        }


        mUploads = new ArrayList<>();
        mapsUploads = new ArrayList<>();

        mAdapter = new ViewStoreCatergoryAdaptor(getActivity(), mUploads);

        mapsAdapter = new MapsProfileAdapter(getActivity(), mapsUploads);


        mAdapter.setOnItemClickListener(ArtFragment.this);

        mapsAdapter.setOnItemClickListener(ArtFragment.this);

        mRecyclerView.setAdapter(mAdapter);

        mapsRecyclerView.setAdapter(mapsAdapter);


        gotoMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SiroccoMusicActivity.class);
                startActivity(intent);

            }
        });


        final String user_id = mAuth.getCurrentUser().getUid();

        mStorage = FirebaseStorage.getInstance();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoOnlineShop");
        nDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoOnSaleItem");


        mUploads = new ArrayList<>();
        mapsUploads = new ArrayList<>();

        mAdapter = new ViewStoreCatergoryAdaptor(getActivity(), mUploads);

        mapsAdapter = new MapsProfileAdapter(getActivity(), mapsUploads);


        mAdapter.setOnItemClickListener(ArtFragment.this);

        mapsAdapter.setOnItemClickListener(ArtFragment.this);

        mRecyclerView.setAdapter(mAdapter);

        mapsRecyclerView.setAdapter(mapsAdapter);


        getOnlineUploads();
        getOnlineHeadline();
    }//End of OnCreate


    private void getOnlineHeadline() {

        mDataBaseListener = mDatabaseRef.orderByChild("exRating").equalTo("Art").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapsUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                    upload.setKey(postSnapshot.getKey());
                    mapsUploads.add(upload);
                }
                mapsAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void getOnlineUploads() {
        mDBlistener = nDatabaseRef.orderByChild("exRating").equalTo("Art").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    mUploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        mUploads.add(upload);
                    }
                    musicReplacement.setVisibility(View.INVISIBLE);
                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);

                } else {

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

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do your stuff
            } else {
                Toast.makeText(getActivity(), "GET_ACCOUNTS Denied",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(getActivity(), ActivityUploadImage.class);
            intent.putExtra("share_item_id", "null");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

        final ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        final String storeMainKey = selectedImage.getViews();

        final String exRater = selectedImage.getPostName();

        final String imageUrl = selectedImage.getImageUrl();

        if (!imageUrl.isEmpty()) {
            if (selectedKey != null && !exRater.isEmpty() && Patterns.WEB_URL.matcher(exRater).matches()) {
                Intent intent = new Intent(getActivity(), LinkView.class);
                intent.putExtra("share_item_id", exRater);
                startActivity(intent);
            } else {

                Toast.makeText(getActivity(), "Item does not have a direct link", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onWhatEverClick(int position) {
        final ImageUploads selectedImage = mUploads.get(position);


        final String selectedKey = selectedImage.getUserId();

        if (selectedKey != null && !selectedKey.isEmpty() && Patterns.WEB_URL.matcher(selectedKey).matches()) {
            Intent intent = new Intent(getActivity(), LinkView.class);
            intent.putExtra("share_item_id", selectedKey);
            startActivity(intent);

        } else {
            Toast.makeText(getActivity(), "Item does not have a direct link", Toast.LENGTH_SHORT).show();

        }
    }


    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_art, container, false);


        mRecyclerView = rootView.findViewById(R.id.siroccoRecyclerView);
        mapsRecyclerView = rootView.findViewById(R.id.siroccoRecyclerViewTop);
        mapsRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHasFixedSize(true);

        mProgressCircle = rootView.findViewById(R.id.progress_circle);


        gotoMusic = rootView.findViewById(R.id.goToMusic);

        gotoMusic.setVisibility(View.INVISIBLE);


        musicReplacement = rootView.findViewById(R.id.canvasId);

        musicReplacement.setVisibility(View.VISIBLE);

        gotoMusic = rootView.findViewById(R.id.goToMusic);
        gotoMusic.setVisibility(View.INVISIBLE);


        return rootView;
    }
}