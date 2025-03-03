package pagiisnet.pagiisnet.Utils;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;
import pagiisnet.pagiisnet.LoginActivity;
import pagiisnet.pagiisnet.MapsActivity;
import pagiisnet.pagiisnet.ProfileFragment;
import pagiisnet.pagiisnet.ProfileViewHolder;
import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.ImageUploads;
import pagiisnet.pagiisnet.RegisterActivity;

public class ViewProfilePicsAdapter extends RecyclerView.Adapter<ViewProfilePicsAdapter.ImageViewHolder> {

    private final Context mContext;
    private final List<ImageUploads> mUploads;
    private OnItemClickListener mListener;
    private final FirebaseAuth mAuth;
    private final DatabaseReference mDatabaseRefLikes;
    private final DatabaseReference mDatabaseRefNotifications;
    private String onlineUserId;
    private String myImageDpUrl;
    private String myName;
    private String myLastLocationDetails;
    private String userToken;
    private DatabaseReference mDatabaseRef_Tokens;

    private String ProfilePicture;

    public ViewProfilePicsAdapter(Context context, List<ImageUploads> uploads) {
        this.mContext = context;
        this.mUploads = uploads;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabaseRefLikes = FirebaseDatabase.getInstance().getReference("postLikes");
        this.mDatabaseRefNotifications = FirebaseDatabase.getInstance().getReference("PagiisNotification");

        // Initialize AdMob
        MobileAds.initialize(mContext);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_user_meme_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageUploads uploadCurrent = mUploads.get(position);

        // Check if the position is a multiple of 5 (e.g., 5th, 10th, 15th, etc.)
        if ((position + 1) % 5 == 0) {
            // Show the Native Ad
            holder.nativeAdView.setVisibility(VISIBLE);
            holder.imageView.setVisibility(INVISIBLE); // Hide the post image
            holder.linkView.setVisibility(INVISIBLE); // Hide the link preview
            holder.playerView.setVisibility(INVISIBLE); // Hide the video player

            loadNativeAd(holder); // Load the Native Ad
        } else {
            // Show the post content
            holder.nativeAdView.setVisibility(INVISIBLE); // Hide the Native Ad
            holder.bind(uploadCurrent); // Bind the post data
        }
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    // ViewHolder for Posts and Ads
    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, MenuItem.OnMenuItemClickListener {
        private final TextView textViewName, Post_Title, Post_Time, Post_Position, textViewNameLikes;
        private final ImageView imageView;
        private final CircularImageView profileImageView;
        private final ProgressBar loading;
        private final RichLinkView linkView;
        private final SparkButton imageViewLikes;
        private final CardView userMemeCardView;
        private PlayerView playerView; // Add PlayerView for video playback
        private ExoPlayer exoPlayer;

        // Native Ad Views
        private NativeAdView nativeAdView;
        private TextView adHeadline, adBody;
        private ImageView adImage;
        private Button adCallToAction;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.memeName);
            Post_Title = itemView.findViewById(R.id.post_title);
            Post_Time = itemView.findViewById(R.id.postTime);
            Post_Position = itemView.findViewById(R.id.postPosition);
            textViewNameLikes = itemView.findViewById(R.id.likesText);
            imageView = itemView.findViewById(R.id.memeImageView);
            profileImageView = itemView.findViewById(R.id.user_item_view_profilepic);
            loading = itemView.findViewById(R.id.loading);
            linkView = itemView.findViewById(R.id.memeImageViewLink);
            imageViewLikes = itemView.findViewById(R.id.imageViewAnimation);
            userMemeCardView = itemView.findViewById(R.id.userMemeCardView);
            playerView = itemView.findViewById(R.id.videoPlayer);

            // Native Ad Views
            nativeAdView = itemView.findViewById(R.id.nativeAdView);
            adHeadline = itemView.findViewById(R.id.ad_headline);
            adBody = itemView.findViewById(R.id.ad_body);
            adImage = itemView.findViewById(R.id.ad_image);
            adCallToAction = itemView.findViewById(R.id.ad_call_to_action);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                menu.setHeaderTitle("Select Action");
                menu.setHeaderIcon(R.drawable.pagiis_logo_final);
                MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Posted By");
                MenuItem share = menu.add(Menu.NONE, 2, 2, "Share");
                MenuItem chats = menu.add(Menu.NONE, 3, 3, "Chats");

                share.setIcon(R.drawable.location_group);
                chats.setIcon(R.drawable.location_based_chat);
                doWhatever.setIcon(R.drawable.pagiis_profile_icon);

                doWhatever.setOnMenuItemClickListener(this);
                share.setOnMenuItemClickListener(this);
                chats.setOnMenuItemClickListener(this);
            });

            profileImageView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onClick(position);
                }
            });
        }

        public void bind(ImageUploads uploadCurrent) {
            String loadImageUrl = uploadCurrent.getImageUrl();
            String postTitle = uploadCurrent.getName();
            String profileImage = uploadCurrent.getExRating();
            String likes = uploadCurrent.getLikes();
            String postPosition = uploadCurrent.getPostLocation();
            String time = uploadCurrent.getPostTime();
            String postName = uploadCurrent.getPostName();
            String fileExtention = uploadCurrent.getExRating();


            onlineUserId = uploadCurrent.getUserId();

            textViewName.setText(postName);
            Post_Title.setText(postTitle);
            Post_Position.setText(postPosition);
            Post_Time.setText(time);
            textViewNameLikes.setText("likes");




            if (loadImageUrl != null && !loadImageUrl.equals("null")) {
                if (fileExtention != null && !fileExtention.isEmpty() && isVideoUrl(fileExtention) )
                {


                        playerView.setVisibility(VISIBLE);
                        imageView.setVisibility(INVISIBLE);
                        linkView.setVisibility(INVISIBLE);
                        initializeExoPlayer(loadImageUrl);

                    DatabaseReference mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserId);

                    mDatabaseRef_x.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {

                            if(dataSnapshot.exists()  && dataSnapshot.hasChild("userImageDp") )
                            {
                                if(dataSnapshot.child("userImageDp").getValue() != "userDefaultDp")
                                {
                                    ProfilePicture = dataSnapshot.child("userImageDp").getValue().toString();
                                    loadProfileImage(ProfilePicture);
                                }

                            }else {
                                // Set a default image if the URL is null or empty
                                profileImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pagiis_logo_final));
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    // Handle video URL

                } else if (Patterns.WEB_URL.matcher(postTitle).matches()) {
                    // Handle link preview
                    linkView.setVisibility(VISIBLE);
                    imageView.setVisibility(INVISIBLE);
                    playerView.setVisibility(INVISIBLE);




                    linkView.setLink(loadImageUrl, new ViewListener() {
                        @Override
                        public void onSuccess(boolean status)
                        {
                            DatabaseReference mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserId);

                            mDatabaseRef_x.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {

                                    if(dataSnapshot.exists()  && dataSnapshot.hasChild("userImageDp") )
                                    {
                                        if(dataSnapshot.child("userImageDp").getValue() != "userDefaultDp")
                                        {
                                            ProfilePicture = dataSnapshot.child("userImageDp").getValue().toString();
                                            loadProfileImage(ProfilePicture);
                                        }

                                    }else {
                                        // Set a default image if the URL is null or empty
                                        profileImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pagiis_logo_final));
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onError(Exception e) {}
                    });
                } else {
                    // Handle image URL
                    linkView.setVisibility(INVISIBLE);
                    imageView.setVisibility(VISIBLE);
                    playerView.setVisibility(INVISIBLE);
                    loadImageWithGlide(loadImageUrl);

                    DatabaseReference mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserId);

                    mDatabaseRef_x.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {

                            if(dataSnapshot.exists()  && dataSnapshot.hasChild("userImageDp") )
                            {
                                if(dataSnapshot.child("userImageDp").getValue() != "userDefaultDp")
                                {
                                    ProfilePicture = dataSnapshot.child("userImageDp").getValue().toString();
                                    loadProfileImage(ProfilePicture);
                                }

                            }else {
                                // Set a default image if the URL is null or empty
                                profileImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pagiis_logo_final));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pagiis_logo_final));
            }

            setupLikeButton(uploadCurrent.getKey());
        }

        private boolean isVideoUrl(String url)
        {
            // Check if the URL points to a video file

                return url.endsWith(".mp4") || url.endsWith(".3gp") || url.endsWith(".mkv");

        }

        private void initializeExoPlayer(String videoUrl) {
            if (exoPlayer == null) {
                exoPlayer = new ExoPlayer.Builder(mContext).build();
                playerView.setPlayer(exoPlayer);
            }

            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(false); // Autoplay when ready
            exoPlayer.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int state) {
                    if (state == Player.STATE_ENDED) {
                        exoPlayer.seekTo(0); // Loop the video
                        exoPlayer.setPlayWhenReady(true);
                    }
                }
            });
        }

        private void releaseExoPlayer() {
            if (exoPlayer != null) {
                exoPlayer.release();
                exoPlayer = null;
            }
        }

        private void loadImageWithGlide(String imageUrl) {
            Glide.with(mContext)
                    .load(imageUrl)
                    .apply(new RequestOptions().centerCrop())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            loading.setVisibility(INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            loading.setVisibility(INVISIBLE);
                            return false;
                        }
                    })
                    .into(imageView);
        }

        private void loadProfileImage(String imageUrl) {
            if (imageUrl != null && !imageUrl.equals("null")) {
                Glide.with(mContext)
                        .load(imageUrl)
                        .apply(new RequestOptions().centerCrop())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileImageView);
            } else {
                profileImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pagiis_logo_final));
            }
        }

        private void setupLikeButton(String postKey) {
            imageViewLikes.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if (buttonState) {
                        handleLike(postKey,onlineUserId);
                    } else {
                        handleUnlike(postKey);
                    }
                }

                @Override
                public void onEventAnimationEnd(ImageView button, boolean buttonState) {}

                @Override
                public void onEventAnimationStart(ImageView button, boolean buttonState) {}
            });
        }

        private void handleLike(String postKey, String userKeyId) {
            mDatabaseRefLikes.child(postKey).child(mAuth.getCurrentUser().getUid()).setValue("true")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            updateLikesCount(postKey);
                            postNotification(postKey, "Like", userKeyId);
                            Toast.makeText(mContext, "Added to favorites!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        private void handleUnlike(String postKey) {
            mDatabaseRefLikes.child(postKey).child(mAuth.getCurrentUser().getUid()).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            updateLikesCount(postKey);
                            Toast.makeText(mContext, "Removed from favorites!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        private void updateLikesCount(String postKey) {
            mDatabaseRefLikes.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // If likes exist, display the count
                        String likesCount = String.valueOf(snapshot.getChildrenCount());
                        textViewNameLikes.setText(likesCount);
                    } else {
                        // If no likes exist, display "0"
                        textViewNameLikes.setText("Likes");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Failed to update likes: " + error.getMessage());
                    // Optionally, set a default value in case of an error
                    textViewNameLikes.setText("Likes");
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onClick(position);
                }
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.shareClick(position);
                            return true;
                        case 3:
                            mListener.chatsClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }


    private void postNotification(String postKey, String type, String userKeyId) {
        if (type.equals("Like")) {
            DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference("userTokens");
            tokensRef.child(userKeyId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Get the user token from the dataSnapshot
                    String userToken = dataSnapshot.getValue(String.class); // Assuming the token is stored as a string

                    if (userToken != null) {
                        // Create the notification object with the correct data
                        ImageUploads notification = new ImageUploads(
                                myName, myImageDpUrl, "", mAuth.getCurrentUser().getUid(),
                                "", "", "", "", myLastLocationDetails, "Your profile has a new like"
                        );

                        // Push the notification to Firebase Realtime Database
                        mDatabaseRefNotifications.child(postKey).push().setValue(notification, (error, ref) -> {
                            if (error == null) {
                                // Send the FCM notification
                                sendFCMNotification(userToken, "New post like", "Your post just got a new like.");
                            } else {
                                Log.e("Database Error", "Failed to send notification: " + error.getMessage());
                            }
                        });
                    } else {
                        Log.e("FCM Error", "User token not found for user: " + userKeyId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("SendNotifications", "Error fetching FCM tokens: " + databaseError.getMessage());
                }
            });
        }
    }



    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    private void viewProfile(int position) {
        // Ensure we use an Activity context
        if (!(getApplicationContext() instanceof Activity)) {
            return;  // Prevent crashes
        }

        // Get the selected image from the list using the position
        final ImageUploads selectedImage = mUploads.get(position);

        // Extract the required data from the selected image
        final String imageUrl = selectedImage.getImageUrl(); // Assuming this is the correct getter method
        final String userId = selectedImage.getUserId(); // Assuming this is the correct getter method

        // Initialize BottomSheetDialog with Activity context
        @SuppressLint("RestrictedApi") final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog((Activity)getApplicationContext(), R.style.BottomSheetDialogueTheme);
        @SuppressLint("RestrictedApi") View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.botttom_sheet_layout, null);

        // Initialize views from the bottom sheet layout
        ImageView profilePicture = bottomSheetView.findViewById(R.id.mapsItemProfile);
        TextView profileName = bottomSheetView.findViewById(R.id.popUpDescriptionTextViewTwo);
        TextView profileStatus = bottomSheetView.findViewById(R.id.popUpDescriptionTextViewThreee);
        TextView profileView = bottomSheetView.findViewById(R.id.popLocationTexview);
        TextView sharePagiis = bottomSheetView.findViewById(R.id.share);
        Button viewProfileButton = bottomSheetView.findViewById(R.id.visitProfile);
        SparkButton imageViewLikes = bottomSheetView.findViewById(R.id.likes);

        // Set profile name
        if (selectedImage.getName() != null && !selectedImage.getName().isEmpty()) {
            profileName.setText(selectedImage.getName());
        }

        // Load profile picture using Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            RequestOptions options = new RequestOptions();
            Glide.with(getApplicationContext())
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(options.centerCrop())
                    .thumbnail(0.75f)
                    .into(profilePicture);
        } else {
            // Set a default image if the URL is null or empty
            profilePicture.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pagiis_logo_final));
        }

        // View Profile Button Click Listener
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("visited_user_id", userId); // Pass the userId to the ProfileFragment

                ProfileFragment targetFragment = new ProfileFragment();
                targetFragment.setArguments(bundle);


                if (getApplicationContext() instanceof FragmentActivity) {
                    FragmentManager fragmentManager = ((FragmentActivity) getApplicationContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainContainer, targetFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        // Share App Functionality
        bottomSheetView.findViewById(R.id.shareImageView).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family, please check out this amazing App called Pagiis: " + Uri.parse("https://www.pagiis.co.za/"));
                intent.setType("text/plain");

                if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    mContext.startActivity(intent);  // ✅ Works inside Adapter
                }
            }
        });
    }


    private void sendFCMNotification(String fcmToken, String notificationTitle1, String notificationMessage1) {
        String FCM_API = "https://fcm.googleapis.com/fcm/send";
        String serverKey = "AAAA64f0YOg:APA91bEWaRY_bpktQU7HtgIhAVsLjhJCGTwjWVWi1bYutnDkwkmo2QmgKBJf8MO6BJXrpiDEi62-XDWKi8B0ogwQ8PVLoABuRyExDj_kdw4VOGQa-0PzzV_G8toDuzWbcXUqoh6LbBAS"; // Replace with your FCM server key
        String contentType = "application/json";

        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();

        try {
            // Add notification content
            notificationBody.put("title", notificationTitle1);
            notificationBody.put("message", notificationMessage1);

            // Set the "to" field to send to a specific token
            notification.put("to", fcmToken);

            // Add both notification and data to the payload
            notification.put("notification", notificationBody);  // For display on the device
            notification.put("data", notificationBody);  // Optional: can be used to pass custom data

        } catch (JSONException e) {
            Log.e("FCM Error", "JSON Exception: " + e.getMessage());
        }

        // Prepare a JSON object request to send to the FCM API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FCM_API, notification,
                response -> Log.d("FCM Response", "Success: " + response.toString()),
                error -> {
                    if (error.networkResponse != null) {
                        Log.e("FCM Error", "Failed: " + new String(error.networkResponse.data));
                    } else {
                        Log.e("FCM Error", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "key=" + serverKey);  // Correct Authorization header
                headers.put("Content-Type", contentType);
                return headers;
            }
        };

        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(jsonObjectRequest);
    }

    // Load Native Ad

    private void loadNativeAd(ImageViewHolder holder) {
        AdLoader adLoader = new AdLoader.Builder(mContext, "ca-app-pub-1698498156044590/8114080739")
                .forNativeAd(nativeAd -> {
                    holder.nativeAdView.setHeadlineView(holder.adHeadline);
                    holder.nativeAdView.setBodyView(holder.adBody);
                    holder.nativeAdView.setIconView(holder.adImage);
                    holder.nativeAdView.setCallToActionView(holder.adCallToAction);

                    // Set NativeAd data
                    holder.adHeadline.setText(nativeAd.getHeadline());
                    holder.adBody.setText(nativeAd.getBody());
                    holder.adCallToAction.setText(nativeAd.getCallToAction());

                    if (nativeAd.getImages() != null && !nativeAd.getImages().isEmpty()) {
                        holder.adImage.setImageDrawable(nativeAd.getImages().get(0).getDrawable());
                    }

                    // Show the native ad
                    holder.nativeAdView.setNativeAd(nativeAd);
                    holder.nativeAdView.setVisibility(View.VISIBLE);
                }) .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        super.onAdFailedToLoad(adError);
                        // Hide the ad view or replace it with an empty space
                        holder.nativeAdView.setVisibility(View.GONE);
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }



    public interface OnItemClickListener {
        void onClick(int position);
        void onWhatEverClick(int position);
        void shareClick(int position);
        void chatsClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}