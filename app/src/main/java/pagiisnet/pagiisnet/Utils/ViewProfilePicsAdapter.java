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

    public ViewProfilePicsAdapter(Context context, List<ImageUploads> uploads) {
        this.mContext = context;
        this.mUploads = uploads;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabaseRefLikes = FirebaseDatabase.getInstance().getReference("postLikes");
        this.mDatabaseRefNotifications = FirebaseDatabase.getInstance().getReference("PagiisNotification");
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
        holder.bind(uploadCurrent);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

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

            profileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onClick(position);
                        viewProfile(position); // Call viewProfile with the correct position
                    }
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

            textViewName.setText(postName);
            Post_Title.setText(postTitle);
            Post_Position.setText(postPosition);
            Post_Time.setText(time);
            textViewNameLikes.setText("likes");

            if (loadImageUrl != null && !loadImageUrl.equals("null")) {
                if (isVideoUrl(loadImageUrl)) {
                    // Handle video URL
                    playerView.setVisibility(VISIBLE);
                    imageView.setVisibility(INVISIBLE);
                    linkView.setVisibility(INVISIBLE);
                    initializeExoPlayer(loadImageUrl);
                } else if (Patterns.WEB_URL.matcher(postTitle).matches()) {
                    // Handle link preview
                    linkView.setVisibility(VISIBLE);
                    imageView.setVisibility(INVISIBLE);
                    playerView.setVisibility(INVISIBLE);

                    linkView.setLink(loadImageUrl, new ViewListener() {
                        @Override
                        public void onSuccess(boolean status) {}

                        @Override
                        public void onError(Exception e) {}
                    });
                } else {
                    // Handle image URL
                    linkView.setVisibility(INVISIBLE);
                    imageView.setVisibility(VISIBLE);
                    playerView.setVisibility(INVISIBLE);
                    loadImageWithGlide(loadImageUrl);
                }
                loadProfileImage(profileImage);
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pagiis_logo_final));
            }


            setupLikeButton(uploadCurrent.getKey());
        }


        private boolean isVideoUrl(String url) {
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


            if(imageUrl != null && !imageUrl.equals("null"))
            {
                Glide.with(mContext)
                        .load(imageUrl)
                        .apply(new RequestOptions().centerCrop())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(profileImageView);

            }else {
                profileImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pagiis_logo_final));
            }
        }

        private void setupLikeButton(String postKey) {
            imageViewLikes.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if (buttonState) {
                        handleLike(postKey);
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

        private void handleLike(String postKey) {
            mDatabaseRefLikes.child(postKey).child(mAuth.getCurrentUser().getUid()).setValue("true")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            updateLikesCount(postKey);
                            postNotification(postKey, "Like");
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

    @SuppressLint("MissingInflatedId")
    private void viewProfile(int position) {
        // Ensure we use an Activity context
        if (!(mContext instanceof Activity)) {
            return;  // Prevent crashes
        }

        // Get the selected image from the list using the position
        final ImageUploads selectedImage = mUploads.get(position);

        // Extract the required data from the selected image
        final String imageUrl = selectedImage.getImageUrl(); // Assuming this is the correct getter method
        final String userId = selectedImage.getUserId(); // Assuming this is the correct getter method

        // Initialize BottomSheetDialog with Activity context
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog((Activity) mContext, R.style.BottomSheetDialogueTheme);
        View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.botttom_sheet_layout, null);

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
            Glide.with(mContext)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(options.centerCrop())
                    .thumbnail(0.75f)
                    .into(profilePicture);
        } else {
            // Set a default image if the URL is null or empty
            profilePicture.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pagiis_logo_final));
        }

        // View Profile Button Click Listener
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("visited_user_id", userId); // Pass the userId to the ProfileFragment

                ProfileFragment targetFragment = new ProfileFragment();
                targetFragment.setArguments(bundle);

                if (mContext instanceof FragmentActivity) {
                    FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainContainer, targetFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        // Share App Functionality
        bottomSheetView.findViewById(R.id.shareImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family, please check out this amazing App called Pagiis: " + Uri.parse("https://www.pagiis.co.za/"));
                intent.setType("text/plain");

                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);  // ✅ Works inside Adapter
                }
            }
        });

        // Explore Pagiis Action

        // Share Pagiis Button
        sharePagiis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Friends and Family, please check out this amazing App called Pagiis: " + Uri.parse("https://www.pagiis.co.za/"));
                intent.setType("text/plain");

                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);  // ✅ Fixed
                }
            }
        });

        // Like Button Click Event
        imageViewLikes.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    Toast.makeText(mContext, "Added to favourites!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Removed from favourites!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) { }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) { }
        });

        // Show the bottom sheet dialog
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void postNotification(String postKey, String type) {
        if (type.equals("Like")) {
            ImageUploads notification = new ImageUploads(
                    myName, myImageDpUrl, "", mAuth.getCurrentUser().getUid(), "", "", "", "", myLastLocationDetails, "Your profile has a new like"
            );
            mDatabaseRefNotifications.child(postKey).push().setValue(notification, (error, ref) -> {
                if (error == null) {
                    sendFCMNotification(userToken, "New post like", "Your post just got a new like.");
                }
            });
        }
    }

    private void sendFCMNotification(String fcmToken, String title, String message) {
        String FCM_API = "https://fcm.googleapis.com/fcm/send";
        String serverKey = "AAAA64f0YOg:APA91bEWaRY_bpktQU7HtgIhAVsLjhJCGTwjWVWi1bYutnDkwkmo2QmgKBJf8MO6BJXrpiDEi62-XDWKi8B0ogwQ8PVLoABuRyExDj_kdw4VOGQa-0PzzV_G8toDuzWbcXUqoh6LbBAS";
        String contentType = "application/json";

        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();

        try {
            notificationBody.put("title", title);
            notificationBody.put("message", message);
            notification.put("to", fcmToken);
            notification.put("data", notificationBody);
        } catch (JSONException e) {
            Log.e("FCM Error", "JSON Exception: " + e.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_API, notification,
                response -> Log.d("FCM Response", "Success: " + response.toString()),
                error -> Log.e("FCM Error", "Failed: " + error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", serverKey);
                headers.put("Content-Type", contentType);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(request);
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