package pagiisnet.pagiisnet.Utils;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Patterns;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.List;

import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;
import pagiisnet.pagiisnet.FcmNotificationsSender;
import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.ImageUploads;
//import pagiisnet.pagiisnet.R;


public class ViewProfilePicsAdapter extends RecyclerView.Adapter<ViewProfilePicsAdapter.ImageViewHolder> {

    private final Context mContext;

    private final List<ImageUploads> mUploads;

    private OnItemClickListener mListener;
    private DatabaseReference databaseReferenceLocation;
    private String onlineUserId;
    private String  userStatusMessage;

    private String userToken;

    private String  myImageDpUrl;

    private String myName;
    private DatabaseReference notificationReference;
    private DatabaseReference mDatabaseRef_Tokens;

    private DatabaseReference getUserProfileDataRef;
    private DatabaseReference mDatabaseRef_Y;

    private DatabaseReference mDatabaseRefLikes;

    private FirebaseAuth mAuth;
    private String myLastLocationDetails;

    //private ProgressBar loadbar;

    private String postLikes;

    public ViewProfilePicsAdapter(Context context, List<ImageUploads> uploads)
    {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_user_meme_item, parent, false);
        return new ViewProfilePicsAdapter.ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ImageViewHolder imageViewHolder, final int position) {

        final ImageUploads uploadCurrent = mUploads.get(position);

        String loadImageUrl = uploadCurrent.getImageUrl();

        String raterValue =  uploadCurrent.getExRating();

        String post_tile = uploadCurrent.getName();

        String likes = uploadCurrent.getLikes();

        String postPosition = uploadCurrent.getPostLocation();

        String views = uploadCurrent.getViews();

        String share = uploadCurrent.getShare();

        String time = uploadCurrent.getPostTime();

        String FinalValue = "internetLink";

        String postName =uploadCurrent.getPostName();

        RequestOptions options = new RequestOptions();

        mDatabaseRefLikes = FirebaseDatabase.getInstance().getReference().child("postLikes");




        mDatabaseRefLikes.child(onlineUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)

            {
                if(dataSnapshot.exists())
                {

                    postLikes = String.valueOf(dataSnapshot.getChildrenCount());


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(loadImageUrl != null && loadImageUrl.compareTo("null") !=0  && !Patterns.WEB_URL.matcher(post_tile).matches() )
        {
            imageViewHolder.textViewName.setText(postName);
            imageViewHolder.Post_Title.setText(post_tile);
            imageViewHolder.Post_Position.setText(postPosition);
            imageViewHolder.Post_Time.setText(time);
            imageViewHolder.textViewNameLikes.setText(postLikes);

            imageViewHolder.linkView.setVisibility(View.INVISIBLE);


            Glide.with(mContext)
                    .load(loadImageUrl)
                    .apply(options.centerCrop())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            imageViewHolder.loading.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })
                    .into(imageViewHolder.imageView);






        }else if(loadImageUrl != null && loadImageUrl.compareTo("null") !=0  && Patterns.WEB_URL.matcher(post_tile).matches() )
        {

            imageViewHolder.linkView.setVisibility(View.VISIBLE);
            imageViewHolder.imageView.setVisibility(View.INVISIBLE);

            imageViewHolder.textViewName.setText(postName);
            imageViewHolder.Post_Title.setText(post_tile);
            imageViewHolder.Post_Position.setText(postPosition);
            imageViewHolder.Post_Time.setText(time);

            imageViewHolder.linkView.setLink(loadImageUrl, new ViewListener() {
                @Override
                public void onSuccess(boolean status)
                {

                }

                @Override
                public void onError(Exception e)
                {


                }
            });

        }else
        {
            imageViewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.pagiis_logo_final));
        }



        if(!(likes==null) && likes.compareTo("userDefaultDp") != 0 && views != null)
        {
            imageViewHolder.imageViewLikes.setVisibility(View.VISIBLE);
            imageViewHolder.textViewNameLikes.setText(likes);
        }


        if(!(raterValue==null) && raterValue.compareTo("userDefaultDp") !=0 &&  views != null)
        {



            Glide.with(mContext)
                    .load(raterValue)
                    .apply(options.centerCrop())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewHolder.profileImageView);

        }else
        {
            Glide.with(mContext)
                    .load(loadImageUrl)
                    .apply(options.centerCrop())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewHolder.profileImageView);
        }




        /*if (loadImageUrl != null)

        {
            imageViewHolder.textViewName.setText(uploadCurrent.getName());

            RequestOptions options = new RequestOptions();

            Glide.with(mContext)
                    .load(loadImageUrl)
                    .apply(options.centerCrop())
                    .into(imageViewHolder.imageView);

            Picasso.with(mContext).load(loadImageUrl)
                    .centerCrop()
                    .error(R.drawable.ic_action_catchup)
                    .into(imageViewHolder.imageView);
        }

        else
        {
            imageViewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_profile));
        }*/

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener
    {
        public TextView textViewName;
        public TextView textViewNameChats;
        public TextView textViewNameView;
        public TextView textViewNameShare;
        public TextView textViewNameLikes;
        public SparkButton imageViewLikes;
        public ImageView imageView;
        public CircularImageView imageViewSirocco;
        public CircularImageView imageViewRadiusChats;
        public CircularImageView imageViewRadiusFriends;
        public CircularImageView profileImageView;
        public ImageView ripleButton;

        public ProgressBar loading;

        private RichLinkView linkView;

        public CardView userMemeCardView;

        public TextView Post_Title;

        public TextView Post_Time;

        public TextView Post_Position;



        public void checkLocation()

        {

            databaseReferenceLocation = FirebaseDatabase.getInstance().getReference("myLastLocation");
            databaseReferenceLocation.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {

                        // Retrieve the location value as a string
                        String location = dataSnapshot.getValue(String.class);
                        mDatabaseRef_Tokens = FirebaseDatabase.getInstance().getReference("userTokens");


                        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
                        mDatabaseRef_Y = FirebaseDatabase.getInstance().getReference().child("WalkinWall");
                        mDatabaseRefLikes = FirebaseDatabase.getInstance().getReference().child("postLikes");

                        notificationReference = FirebaseDatabase.getInstance().getReference().child("PagiisNotification");



                                    String userKey = dataSnapshot.getKey().toString();

                                    mDatabaseRef_Tokens.child(userKey).addValueEventListener(new ValueEventListener() {
                                        @SuppressLint("RestrictedApi")
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)

                                        {
                                            if(dataSnapshot.exists())
                                            {

                                                String userId = dataSnapshot.getKey().toString();
                                                getUserProfileDataRef.child(userId).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)

                                                    {
                                                        if(dataSnapshot.exists())
                                                        {

                                                            String name = dataSnapshot.child("userNameAsEmail").getValue().toString();
                                                            String userStatus = dataSnapshot.child("userDefaultStatus").getValue().toString();

                                                            String myDpUrl = dataSnapshot.child("userImageDp").getValue().toString();


                                                            myImageDpUrl = myDpUrl;
                                                            myName = name;

                                                            mDatabaseRefLikes.child(onlineUserId).child(mAuth.getCurrentUser().getUid().toString()).setValue(mAuth);



                                                            postNotification();

                                                        }



                                                    }


                                                    private void postNotification()
                                                    {
                                                        String myUserId = mAuth.getCurrentUser().getUid().toString();
                                                        ImageUploads upload = new ImageUploads(myName, myImageDpUrl, "", myUserId, "", "", "", "", myLastLocationDetails, "Your post has a new like");
                                                        notificationReference.child(userId)
                                                                .push()
                                                                .setValue(upload, new DatabaseReference.CompletionListener() {
                                                                    @Override
                                                                    public void onComplete(DatabaseError databaseError,
                                                                                           DatabaseReference databaseReference) {

                                                                        //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                                                                        //Toast.makeText(getApplicationContext(), "Notification sent to all users in location.", Toast.LENGTH_SHORT).show();
                                                                        // String uniqueKey = databaseReference.getKey();
                                                                        //Create the function for Clearing/The ImageView Widget.
                                                                    }
                                                                });

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                                userToken = dataSnapshot.getValue().toString();

                                                @SuppressLint("RestrictedApi") FcmNotificationsSender notificationSender = new FcmNotificationsSender(userToken,"Share experiences","There are people who would like you to share your experiences in ",getApplicationContext(),
                                                        (Activity) getApplicationContext());

                                                notificationSender.SendNotifications();

                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors
                    System.err.println("Error retrieving location: " + databaseError.getMessage());
                }
            });
        }




        public ImageViewHolder(final View itemView)
        {
            super(itemView);

            textViewName = itemView.findViewById(R.id.memeName);
            //textViewNameView = itemView.findViewById(R.id.viewers);
            //textViewNameShare = itemView.findViewById(R.id.share);
            //textViewNameChats = itemView.findViewById(R.id.chats);
            linkView = itemView.findViewById(R.id.memeImageViewLink);

            userMemeCardView = itemView.findViewById(R.id.userMemeCardView);


            Post_Time = itemView.findViewById(R.id.postTime);

            Post_Title = itemView.findViewById(R.id.post_title);

            Post_Position = itemView.findViewById(R.id.postPosition);

            textViewNameLikes = itemView.findViewById(R.id.likesText);
            imageView = itemView.findViewById(R.id.memeImageView);
            imageViewLikes = itemView.findViewById(R.id.imageViewAnimation);

            loading = itemView.findViewById(R.id.loading);
            //imageViewSirocco = itemView.findViewById(R.id.views);

            //imageViewRadiusChats = itemView.findViewById(R.id.pagiis_radius_chat);
            //imageViewRadiusFriends = itemView.findViewById(R.id.pagiis_radius_friends);
            profileImageView = itemView.findViewById(R.id.user_item_view_profilepic);
            //ripleButton = itemView.findViewById(R.id.ripple_button);

            //imageViewLikes.setVisibility(View.INVISIBLE);

            /*imageViewRadiusChats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    view.findViewById(R.id.pagiis_radius_chat);

                    Toast.makeText(mContext, "Waitin update", Toast.LENGTH_SHORT).show();
                }
            });*/

            int position = getAdapterPosition();
            int postionX = (int) getItemId();


            databaseReferenceLocation.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)

                {
                    if(dataSnapshot.exists())
                    {

                        myLastLocationDetails = dataSnapshot.getValue().toString();


                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




            ImageUploads selectedImage = mUploads.get(position);

            String selectedKey = selectedImage.getKey();

            onlineUserId = selectedKey;



            imageViewLikes.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if(buttonState)
                    {
                        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference().child("Users");

                        getUserProfileDataRef.child(onlineUserId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)

                            {
                                if(dataSnapshot.exists())
                                {

                                    String name = dataSnapshot.child("userNameAsEmail").getValue().toString();
                                    String userStatus = dataSnapshot.child("userDefaultStatus").getValue().toString();

                                    String myDpUrl = dataSnapshot.child("userImageDp").getValue().toString();
                                    userStatusMessage = userStatus;
                                    myImageDpUrl = myDpUrl;
                                    myName = name;

                                    checkLocation();

                                }



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        mDatabaseRefLikes.child(onlineUserId).child(mAuth.getCurrentUser().toString()).setValue("1");



                        Toast.makeText(mContext.getApplicationContext(), "add to favourite!", Toast.LENGTH_SHORT).show();
                    }else{


                        Toast.makeText(mContext.getApplicationContext(), "remove from favourite!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onEventAnimationEnd(ImageView button, boolean buttonState) {

                }

                @Override
                public void onEventAnimationStart(ImageView button, boolean buttonState) {

                }
            });






           /* imageViewLikes.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (imageViewLikes.isSelected()) {
                                imageViewLikes.setSelected(false);
                            } else {
                                // if not selected only
                                // then show animation.
                                imageViewLikes.setSelected(true);
                                imageViewLikes.likeAnimation();

                            }
                        }
                    });*/


            /*ripleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    view.findViewById(R.id.pagiis_radius_chat);

                    int position = getAdapterPosition();
                    int selctecid = view.getId();

                    int postionX = (int) getItemId();

                    if (position != RecyclerView.NO_POSITION)
                    {

                        ImageUploads selectedImage = mUploads.get(position);

                        String selectedKey = selectedImage.getKey();

                        String getUserRef = selectedImage.getUserId();

                        String imageUrl = selectedImage.getImageUrl();

                        if(imageUrl!=null)
                        {
                            Intent intent = new Intent(mContext,ActivityUploadImage.class);
                            intent.putExtra("share_item_id", imageUrl);
                            mContext.startActivity(intent); ///Good Work Marlii
                        }



                    }

                }
            });*/


            /*userMemeCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    view.findViewById(R.id.userMemeCardView);

                    int position = getAdapterPosition();
                    int selctecid = view.getId();

                    int postionX = (int) getItemId();

                    if (position != RecyclerView.NO_POSITION)
                    {

                        ImageUploads selectedImage = mUploads.get(position);

                        String selectedKey = selectedImage.getKey();

                        String getUserRef = selectedImage.getUserId();

                        String imageUrl = selectedImage.getImageUrl();

                        if(imageUrl!=null)
                        {
                            Intent intent = new Intent(mContext,ActivityUploadImage.class);
                            intent.putExtra("imageKeyMAx", selectedKey);
                            intent.putExtra("imageUrlMax",imageUrl);
                            intent.putExtra("imageUserId",getUserRef);
                            mContext.startActivity(intent); ///Good Work Marlii
                        }



                    }

                }
            });*/



            /*imageViewRadiusChats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    view.findViewById(R.id.pagiis_radius_chat);

                    int position = getAdapterPosition();
                    int selctecid = view.getId();

                    int postionX = (int) getItemId();

                    if (position != RecyclerView.NO_POSITION)
                    {

                        ImageUploads selectedImage = mUploads.get(position);

                        String selectedKey = selectedImage.getKey();

                        String getUserRef = selectedImage.getUserId();

                        String imageUrl = selectedImage.getImageUrl();

                        if(imageUrl!=null)
                        {
                            Intent intent = new Intent(mContext,ActivityUploadImage.class);
                            intent.putExtra("share_item_id", imageUrl);
                            mContext.startActivity(intent); ///Good Work Marlii
                        }



                    }

                }
            });*/

            /*imageViewSirocco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    view.findViewById(R.id.pagiis_radius_chat);

                    int position = getAdapterPosition();
                    int selctecid = view.getId();

                    int postionX = (int) getItemId();

                    if (position != RecyclerView.NO_POSITION)
                    {

                        ImageUploads selectedImage = mUploads.get(position);

                        String selectedKey = selectedImage.getKey();

                        String getUserRefId = selectedImage.getUserId();

                        String imageUrl = selectedImage.getImageUrl();

                        if(imageUrl!=null)
                        {
                            Intent intent = new Intent(mContext,ActivityItemViewers.class);
                            intent.putExtra("share_item_url", imageUrl);
                            intent.putExtra("share_item_userId",getUserRefId);
                            intent.putExtra("share_item_userKey",selectedKey);

                            mContext.startActivity(intent); ///Good Work Marlii
                        }



                    }

                }
            });*/


            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mListener != null )
            {
                int position = getAdapterPosition();

                int selctecid = view.getId();

                int postionX = (int) getItemId();

                if (position != RecyclerView.NO_POSITION)
                {
                    mListener.onClick(position);

                }

            }

        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {


            contextMenu.setHeaderTitle("Select Action");
            contextMenu.setHeaderIcon(R.drawable.pagiis_logo_final);
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "Posted By");

            MenuItem share= contextMenu.add(Menu.NONE, 2, 2, "Share");
            MenuItem chats = contextMenu.add(Menu.NONE, 3, 3, "Chats");

            share.setIcon(R.drawable.location_group);
            chats.setIcon(R.drawable.location_based_chat);
            doWhatever.setIcon(R.drawable.pagiis_profile_icon);

            doWhatever.setOnMenuItemClickListener(this);
            share.setOnMenuItemClickListener(this);
            chats.setOnMenuItemClickListener(this);
        }

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


    public interface OnItemClickListener {

        void onClick(int position);

        void onWhatEverClick(int position);

        void shareClick(int position);

        void chatsClick(int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener)

    {
        mListener = listener;
    }

    public void setOnItemClickListener2(OnItemClickListener listener2)

    {
        mListener = listener2;
    }
}

