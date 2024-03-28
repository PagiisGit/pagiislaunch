package pagiisnet.pagiisnet.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;
import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.ActivityItemViewers;
import pagiisnet.pagiisnet.ActivityUploadImage;
import pagiisnet.pagiisnet.ImageUploads;
import pagiisnet.pagiisnet.LinkPost;
import pagiisnet.pagiisnet.R;

public class linksViewAdapter extends RecyclerView.Adapter<linksViewAdapter.ImageViewHolder> {


    private final Context mContext;

    private final List<ImageUploads> mUploads;

    private linksViewAdapter.OnItemClickListener mListener;

    //private ProgressBar loadbar;

    public linksViewAdapter  (Context context, List<ImageUploads> uploads)
    {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public linksViewAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_user_meme_item_link, parent, false);
        return new linksViewAdapter.ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(linksViewAdapter.ImageViewHolder imageViewHolder, final int position) {

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



        if(loadImageUrl != null && loadImageUrl.compareTo("null") !=0 )
        {
            imageViewHolder.textViewName.setText(postName);
            imageViewHolder.Post_Title.setText(post_tile);
            imageViewHolder.Post_Position.setText(postPosition);
            imageViewHolder.Post_Time.setText(time);

            imageViewHolder.richLinkView.setLink(post_tile, new ViewListener() {

                @Override
                public void onSuccess(boolean status)
                {
                    Toast.makeText(mContext, "Linked", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(Exception e)
                {
                    Toast.makeText(mContext, "error handling link"+":"+ e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });



        }else
        {
            imageViewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.pagiis_logo_final));
        }


        if(views.compareTo("userDefaultDp") != 0 && views != null )
        {
            imageViewHolder.textViewNameView.setText(views);

        }

        if(share.compareTo("userDefaultDp") != 0  && share != null)
        {
            imageViewHolder.textViewNameShare.setText(share);

        }

        if(likes.compareTo("userDefaultDp") != 0 && views != null)
        {
            imageViewHolder.imageViewLikes.setVisibility(View.VISIBLE);
            imageViewHolder.textViewNameLikes.setText(likes);
        }


        if(raterValue.compareTo("userDefaultDp") !=0 &&  views != null)
        {



            Glide.with(mContext)
                    .load(raterValue)
                    .apply(options.centerCrop())
                    .into(imageViewHolder.profileImageView);

        }else
        {
            Glide.with(mContext)
                    .load(loadImageUrl)
                    .apply(options.centerCrop())
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

    public void setOnItemClickListener(linksViewAdapter.OnItemClickListener listener)

    {
        mListener = listener;
    }


    public interface OnItemClickListener {

        void onClickLink(int position);

        void onWhatEverClickLink(int position);

        void shareClickLink(int position);

        void chatsClickLink(int position);

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener
    {
        private final RichLinkView richLinkView;
        public TextView textViewName;
        public TextView textViewNameChats;
        public TextView textViewNameView;
        public TextView textViewNameShare;
        public TextView textViewNameLikes;
        public ImageView imageViewLikes;
        public ImageView imageView;
        public ImageView imageViewSirocco;
        public ImageView imageViewRadiusChats;
        public ImageView imageViewRadiusFriends;
        public ImageView profileImageView;
        public ImageView ripleButton;
        public CardView userMemeCardView;

        public  TextView Post_Title;

        public  TextView Post_Time;

        public  TextView Post_Position;



        public ImageViewHolder(final View itemView)
        {
            super(itemView);

            textViewName = itemView.findViewById(R.id.memeName);
            textViewNameView = itemView.findViewById(R.id.viewers);
            textViewNameShare = itemView.findViewById(R.id.share);
            textViewNameChats = itemView.findViewById(R.id.chats);
            richLinkView = itemView.findViewById(R.id.memeImageView);

            userMemeCardView = itemView.findViewById(R.id.userMemeCardView);




            Post_Time = itemView.findViewById(R.id.postTime);

            Post_Title = itemView.findViewById(R.id.post_title);

            Post_Position = itemView.findViewById(R.id.postPosition);

            textViewNameLikes = itemView.findViewById(R.id.likesText);
            imageView = itemView.findViewById(R.id.memeImageView);
            imageViewLikes = itemView.findViewById(R.id.likes);
            imageViewSirocco = itemView.findViewById(R.id.views);

            imageViewRadiusChats = itemView.findViewById(R.id.pagiis_radius_chat);
            imageViewRadiusFriends = itemView.findViewById(R.id.pagiis_radius_friends);
            profileImageView = itemView.findViewById(R.id.user_item_view_profilepic);
            ripleButton = itemView.findViewById(R.id.ripple_button);

            imageViewLikes.setVisibility(View.INVISIBLE);




            ripleButton.setOnClickListener(new View.OnClickListener() {
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
                            Intent intent = new Intent(mContext, LinkPost.class);
                            intent.putExtra("share_item_id", selctecid);
                            mContext.startActivity(intent); ///Good Work Marlii
                        }



                    }

                }
            });


            userMemeCardView.setOnClickListener(new View.OnClickListener() {
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
            });



            imageViewRadiusChats.setOnClickListener(new View.OnClickListener() {
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
            });

            imageViewSirocco.setOnClickListener(new View.OnClickListener() {
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
                            Intent intent = new Intent(mContext, ActivityItemViewers.class);
                            intent.putExtra("share_item_url", imageUrl);
                            intent.putExtra("share_item_userId",getUserRefId);
                            intent.putExtra("share_item_userKey",selectedKey);

                            mContext.startActivity(intent); ///Good Work Marlii
                        }



                    }

                }
            });


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
                    mListener.onClickLink(position);

                }

            }

        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {


            contextMenu.setHeaderTitle("Select Action");
            contextMenu.setHeaderIcon(R.drawable.pagiis_logo_final);
            MenuItem DoWhatever = contextMenu.add(Menu.NONE, 1, 1, "Posted By");

            MenuItem shareLink= contextMenu.add(Menu.NONE, 2, 2, "ShareLinnk");
            MenuItem chatsLink = contextMenu.add(Menu.NONE, 3, 3, "Chats");

            shareLink.setIcon(R.drawable.location_group);
            chatsLink.setIcon(R.drawable.location_based_chat);
            DoWhatever.setIcon(R.drawable.pagiis_profile_icon);

            DoWhatever.setOnMenuItemClickListener(this);
            shareLink.setOnMenuItemClickListener(this);
            chatsLink.setOnMenuItemClickListener(this);
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClickLink(position);
                            return true;
                        case 2:
                            mListener.shareClickLink(position);
                            return true;
                        case 3:
                            mListener.chatsClickLink(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
}
