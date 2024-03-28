package pagiisnet.pagiisnet;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import pagiisnet.pagiisnet.Utils.ViewStoreItemAdapter;
import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SiroccoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SiroccoFragment extends Fragment implements ViewStoreItemAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private void showBottomSheetDialog()

    {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogueTheme);
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.sirocco_expore, null);

        View rootView = bottomSheetView.findViewById(R.id.siroccoBottomSheetContainer); // Replace with your root layout ID


        GifImageView siroccoArtView = bottomSheetView.findViewById(R.id.siroccoArt);
        GifImageView siroccoMusicView = bottomSheetView.findViewById(R.id.siroccoMusic);
        GifImageView siroccoFashionView = bottomSheetView.findViewById(R.id.siroccoFashion);
        Button homeButton = bottomSheetView.findViewById(R.id.home);
        TextView sharePagiis = bottomSheetView.findViewById(R.id.share);

        siroccoArtView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                getOnlineCategory("Art");
                siroccoCategory.setText("Art");
                bottomSheetDialog.dismiss();
            }
        });

        sharePagiis.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
               Intent intent = new Intent();
               intent.setAction(Intent.ACTION_SEND);
               intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family please care to check out this amazing App called Pagiis:    "+ Uri.parse("https://apkpure.com/pagiis/pagiisnet.pagiisnet"));
               intent.setType("text/plain");

                if (getActivity() != null && getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    // There is an activity that can handle the intent

                    startActivity(intent);


                } else
                {
                    // There is no activity that can handle the intent
                }


            }
        });


        siroccoMusicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                getOnlineCategory("Music");
                siroccoCategory.setText("Music");
                bottomSheetDialog.dismiss();

            }
        });

        siroccoFashionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                getOnlineCategory("Fashion");
                siroccoCategory.setText("Fashion");
                bottomSheetDialog.dismiss();

            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                siroccoCategory.setText("Sirocco");
                bottomSheetDialog.dismiss();

            }
        });




        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String CurrentView;

    private DatabaseReference mDatabaseRef_x;
    private DatabaseReference mDatabaseRef;

    private ValueEventListener mDBlistener;
    private WebView webViewLinks;
    private WebSettings webSettings;

    private List<ImageUploads> mUploads, mUploads1;

    private RecyclerView mRecyclerView;

    private CircularImageView hideStoreList;

    private ViewStoreItemAdapter mAdapter, mAdapter2;

    private String UrlString;

    private String SiroccoUrlString;

    private FloatingActionButton SABRANDS;
    private FloatingActionButton SiroccoHome;

    private ProgressBar mProgressCircle;


    private TextView siroccoCategory;

    private CardView storeItemListCardView;


    public SiroccoFragment() {
        // Required empty public constructor
    }

    private String shareItemId;


    //private WebView webviewLinks;
    private androidx.appcompat.widget.Toolbar mToolbar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SiroccoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SiroccoFragment newInstance(String param1, String param2) {
        SiroccoFragment fragment = new SiroccoFragment();
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

        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference("SiroccoStoreSites");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoSite");

        mUploads = new ArrayList<>();
        mUploads1 = new ArrayList<>();


        //shareItemId = "https://pagiisnetwork.wixsite.com/sirocco";

       /* mDatabaseRef_x.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUploads1.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads1.add(upload);

                    if(dataSnapshot.getKey().compareTo("store1") ==0){
                        UrlString =upload.getExRating().toString();

                        webViewLinks.loadUrl(UrlString);

                    }
                }

                mAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

      /*  switch (CurrentView) {
            case "Art":

               getOnlineCategory();
                break;

            case "Music":

                getOnlineCategory();
                break;

            case "Fashion":

                getOnlineCategory();

                break;


            default:
                throw new IllegalStateException("Unexpected value: " + CurrentView);
        }*/

        retrieveAllOnlineStores();



    }

    private void retrieveAllOnlineStores()

    {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                if(dataSnapshot.exists())
                {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                        upload.setKey(postSnapshot.getKey());
                        mUploads.add(upload);

                        if (postSnapshot.getKey().compareTo("store1") == 0) {
                            UrlString = upload.getExRating();
                            webViewLinks.getSettings().setJavaScriptEnabled(true);
                   	    webViewLinks.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                            webViewLinks.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                            webViewLinks.getSettings().setSupportZoom(true);
                            webViewLinks.getSettings().setLoadsImagesAutomatically(true);
                            webViewLinks.getSettings().setUseWideViewPort(true);
                            webViewLinks.getSettings().setLoadWithOverviewMode(true);
                            webViewLinks.loadUrl(UrlString);

                            webViewLinks.setWebViewClient(new WebViewClient() {
                                @Override
                                public void onPageFinished(WebView view, String url)
                                {

                                    if(view.getProgress() == 100)
                                    {
                                        mProgressCircle.setVisibility(View.INVISIBLE);

                                    }
                                    // This method will be called when the page finishes loading
                                    // You can put your code to check if it's done loading here
                                    // For example, you can set a flag or perform some action
                                }
                            });


                        }
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


    private void getOnlineCategory(String currentView)

    {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoSite");

        Query mSearchQuery = mDatabaseRef.orderByChild("category").startAt(currentView).endAt(currentView + "\uf8ff");


        mSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ImageUploads upload = postSnapshot.getValue(ImageUploads.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);

                    if (postSnapshot.getKey().compareTo("store1") ==0)
                    {
                        UrlString = upload.getExRating();

                        webViewLinks.getSettings().setJavaScriptEnabled(true);
                        webViewLinks.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                        webViewLinks.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                        webViewLinks.getSettings().setSupportZoom(true);
                        webViewLinks.getSettings().setLoadsImagesAutomatically(true);
                        webViewLinks.getSettings().setUseWideViewPort(true);
                        webViewLinks.getSettings().setLoadWithOverviewMode(true);
                        webViewLinks.loadUrl(UrlString);


                        webViewLinks.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageFinished(WebView view, String url)
                            {

                                if(view.getProgress() == 100)
                                {
                                    mProgressCircle.setVisibility(View.INVISIBLE);

                                }
                                // This method will be called when the page finishes loading
                                // You can put your code to check if it's done loading here
                                // For example, you can set a flag or perform some action
                            }
                        });
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sirocco, container, false);


        webViewLinks = rootView.findViewById(R.id.webview);

        siroccoCategory = rootView.findViewById(R.id.siroccoCatergoryText);

        webSettings = webViewLinks.getSettings();

        webSettings.setJavaScriptEnabled(true);

        mRecyclerView = rootView.findViewById(R.id.storeList);

        SABRANDS = rootView.findViewById(R.id.SABrands);

        SiroccoHome = rootView.findViewById(R.id.siroccoHome);

        mProgressCircle = rootView.findViewById(R.id.progress_circle_user_memes);

        mRecyclerView.setHasFixedSize(true);

        hideStoreList = rootView.findViewById(R.id.hideStoreList);
        storeItemListCardView = rootView.findViewById(R.id.storeListCardView);


        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new ViewStoreItemAdapter(getActivity(), mUploads);
        mAdapter2 = new ViewStoreItemAdapter(getActivity(), mUploads);

        mAdapter.setOnItemClickListener(SiroccoFragment.this);

        mRecyclerView.setAdapter(mAdapter);

        SABRANDS.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v)

            {

              if(v.getId() == R.id.SABrands)

                {
                    showBottomSheetDialog();
                    storeItemListCardView.setVisibility(View.VISIBLE);

                }


            }
        });

        SiroccoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(v.getId() == R.id.siroccoHome)
                {

                    retrieveAllOnlineStores();
                    storeItemListCardView.setVisibility(View.VISIBLE);

                }

            }
        });


        hideStoreList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                storeItemListCardView.setVisibility(View.INVISIBLE);

            }
        });




        webViewLinks.setWebViewClient(new LinkView.Callback());


        if (UrlString != null) {
            webViewLinks.loadUrl(UrlString);

        } else {

            mDatabaseRef_x.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // UrlString = dataSnapshot.child("siroccoonlinestoresite").getValue().toString();

                    webViewLinks.getSettings().setJavaScriptEnabled(true);
                    webViewLinks.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    webViewLinks.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                    webViewLinks.getSettings().setSupportZoom(true);
                    webViewLinks.getSettings().setLoadsImagesAutomatically(true);
                    webViewLinks.getSettings().setUseWideViewPort(true);
                    webViewLinks.getSettings().setLoadWithOverviewMode(true);
                    webViewLinks.setInitialScale(1);
                    webViewLinks.loadUrl(UrlString);


                    webViewLinks.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url)
                        {

                            if(view.getProgress() == 100)
                            {
                                mProgressCircle.setVisibility(View.INVISIBLE);

                            }
                            // This method will be called when the page finishes loading
                            // You can put your code to check if it's done loading here
                            // For example, you can set a flag or perform some action
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


        return rootView;
    }

    @Override
    public void onItemClick(int position) {
        ImageUploads selectedImage = mUploads.get(position);

        String selectedKey = selectedImage.getKey();

        String imageUrl = selectedImage.getExRating();

        String maxImageUserId = selectedImage.getUserId();
        mProgressCircle.setVisibility(View.VISIBLE);

        if (!imageUrl.isEmpty() && !selectedKey.isEmpty())
        {

            webViewLinks.loadUrl(imageUrl);
            webViewLinks.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            webViewLinks.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webViewLinks.getSettings().setSupportZoom(true);
            webViewLinks.getSettings().setLoadsImagesAutomatically(true);
            webViewLinks.getSettings().setLoadWithOverviewMode(true);
            webViewLinks.getSettings().setLoadWithOverviewMode(true);
            webViewLinks.setInitialScale(1);


            webViewLinks.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url)
                {

                    if(view.getProgress() == 100)
                    {
                        mProgressCircle.setVisibility(View.INVISIBLE);

                    }
                    // This method will be called when the page finishes loading
                    // You can put your code to check if it's done loading here
                    // For example, you can set a flag or perform some action
                }
            });

        } else {
            Toast.makeText(getActivity(), "Pagiis cant open Store", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onWhatEverClick(int position) {

    }

    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }
}