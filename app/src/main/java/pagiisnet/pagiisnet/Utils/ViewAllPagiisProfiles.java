package pagiisnet.pagiisnet.Utils;

import static android.view.View.INVISIBLE;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.ProfileViewHolder;
//import pagiisnet.pagiisnet.R;

public class ViewAllPagiisProfiles extends RecyclerView.Adapter<ViewAllPagiisProfiles.ImageViewHolder> {

    private final Context mContext;

    private final List<ProfileViewHolder> mUploads;

    private OnItemClickProfileListener mListener;

    //private ProgressBar loadbar;

    public ViewAllPagiisProfiles(Context context, List<ProfileViewHolder> uploads)
    {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.maps_searched_item, parent, false);
        return new ViewAllPagiisProfiles.ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ImageViewHolder imageViewHolder, final int position)

    {

        ProfileViewHolder selectedImage = mUploads.get(position);
        final String selectedKey = selectedImage.getKey();
        //final String own_user_id = mAuth.getCurrentUser().getUid();
        final String imageUrl = selectedImage.getUserImageProfilePic();
        final String userKeyId = selectedImage.getKey();
        if(imageUrl!= null && imageUrl.compareTo("null") != 0 && !imageUrl.isEmpty())

        {
            imageViewHolder.textDescription.setText(selectedImage.getUserName());


            if(selectedImage.getUserName() != null && !selectedImage.getUserName().isEmpty())
            {
                imageViewHolder.textViewName.setText(selectedImage.getUserName());
            }


            RequestOptions options = new RequestOptions();

            Glide.with(mContext)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(options.centerCrop())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            imageViewHolder.progressImageBar.setVisibility(INVISIBLE);
                            return false;
                        }
                    })
                    .into(imageViewHolder.imageView);

// Now, you can also use a ViewTreeObserver to ensure the ImageView is actually visible on the screen

            imageViewHolder.imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    // The ImageView is currently visible on the screen
                    // Add your code here
                    return imageViewHolder.imageView.isShown();
                }
            });



        }else
        {
            imageViewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.user_update));
        }


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener
    {
        public TextView textViewName;
        public TextView textDescription;
        public ImageView imageView;
        public ImageView profileImageView;

        public ProgressBar progressImageBar;


        private final RelativeLayout relativeLayout;

        public TextView likesView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.sirocco_brand_model);
            likesView = itemView.findViewById(R.id.likes_two);
            imageView = itemView.findViewById(R.id.sirocco_image_view);
            profileImageView = itemView.findViewById(R.id.item_view_profilepic);


            relativeLayout = itemView.findViewById(R.id.relativeSearcLayout);

            textDescription= itemView.findViewById(R.id.searchedItemDescription);

            progressImageBar = itemView.findViewById(R.id.progress_circle_user_memes);


            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

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
                            mListener.onWhatEverClickProfile(position);
                            return true;
                        case 2:
                            mListener.shareClickProfile(position);
                            return true;
                        case 3:
                            mListener.chatsClickProfile(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }


    public interface OnItemClickProfileListener {

        void onClick(int position);

        void onWhatEverClickProfile(int position);

        void shareClickProfile(int position);

        void chatsClickProfile(int position);

    }
    public void setOnItemProfileClickListener(OnItemClickProfileListener listener)

    {
        mListener = listener;
    }
}

