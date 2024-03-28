package pagiisnet.pagiisnet;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pagiisnet.pagiisnet.Utils.ChooseProfileServiceCategory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_service#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_service extends Fragment implements ChooseProfileServiceCategory.OnItemClickListener  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private DatabaseReference mDatabaseRef;

    private RecyclerView mRecyclerView;
    private Button nextButton;
    private Button cancelButton;

    private LinearLayoutCompat itemLayout;

    private FirebaseAuth mAuth;

    private List<ImageUploads> mUploads;

    private ChooseProfileServiceCategory mAdapter;

    private DatabaseReference userProfileInfor;

    private DatabaseReference userProfileServiceCategory;

    private Animation fadeLayout;

    private Fragment oldFragment;

    private String selectedItem;

    public profile_service() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_service.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_service newInstance(String param1, String param2) {
        profile_service fragment = new profile_service();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        FirebaseApp.initializeApp(getApplicationContext());

        mUploads = new ArrayList<>();


        /*if(haveNetwork()){

            Toast.makeText(this, "Your Device is Not connected to the internet please check mobile data"+"\n"+"and Wifi Connections", Toast.LENGTH_LONG).show();

        }*/
        mAuth = FirebaseAuth.getInstance();

        if(!mAuth.getCurrentUser().getUid().isEmpty()){

            String User_id = mAuth.getCurrentUser().getUid();

            userProfileInfor = FirebaseDatabase.getInstance().getReference("ProfileServices");

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(User_id);


        }

        retrieveAllOnlineStores();

    }

    private void retrieveAllOnlineStores()

    {
        userProfileInfor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                if(dataSnapshot.exists())
                {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        mUploads.add(upload);

                    }

                }

                mAdapter.notifyDataSetChanged();
                //  AddContent.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                // mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


    }


    private void updateprofileproffession()
    {
        mDatabaseRef.child("proffession").setValue(selectedItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)

            {

                if (task.isSuccessful())
                {
                    oldFragment = new ProfileFragment();

                    if (oldFragment != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, oldFragment).commit();
                    }

                }
            }
        });

    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_service, container, false);

        mRecyclerView = rootView.findViewById(R.id.serviceItemListRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        nextButton = rootView.findViewById(R.id.next);
        cancelButton = rootView.findViewById(R.id.cancel);

        //itemLayout = rootView.findViewById(R.id.primaryItem);

        nextButton.setAlpha(0);
        cancelButton.setAlpha(0);

        fadeLayout = AnimationUtils.loadAnimation(getActivity(),R.anim.fade);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new ChooseProfileServiceCategory(getActivity(), mUploads);

        //mAdapter = new viewServiceCategory(getActivity(), mUploads);

        mAdapter.setOnItemClickListener(profile_service.this);

        mRecyclerView.setAdapter(mAdapter);

        //this code goes to the Adapter that will handle selected Items

        nextButton.setOnClickListener(new View.OnClickListener()
        {



            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.next);

                if(selectedItem !=null && !selectedItem.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Selected Profile service: "+ selectedItem, Toast.LENGTH_LONG).show();

                    mDatabaseRef.child("proffession").setValue(selectedItem)
                            .addOnSuccessListener(task ->
                            {
                                oldFragment = null;
                                oldFragment = new ProfileFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, oldFragment).commit();
                            });

                }else
                {

                    Toast.makeText(getApplicationContext(), "Profile Service is not selected, Please try again.", Toast.LENGTH_LONG).show();
                }


                //itemLayout.setBackgroundDrawable(R.drawable.bg_item_selcted);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.cancel);
                oldFragment = null;
                oldFragment = new ProfileFragment();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, oldFragment).commit();

                //itemLayout.setBackgroundDrawable(R.drawable.bg_item_selcted);

            }
        });
        return rootView;
    }


    @Override
    public CreationExtras getDefaultViewModelCreationExtras()

    {
        return super.getDefaultViewModelCreationExtras();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onItemClickMapsProfileCategory(int position)
    {
        nextButton.setAlpha(1);
        cancelButton.setAlpha(1);
        nextButton.startAnimation(fadeLayout);

        ImageUploads selectedImage = mUploads.get(position);
        selectedItem = String.valueOf(selectedImage.getName());


        Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onWhatEverClickMapsProfileCategory(int position) {

    }
}