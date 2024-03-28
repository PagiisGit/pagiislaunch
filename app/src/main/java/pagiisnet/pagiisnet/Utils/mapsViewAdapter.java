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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.ImageUploads;
//import pagiisnet.pagiisnet.R;

public class mapsViewAdapter  extends RecyclerView.Adapter<mapsViewAdapter.ImageViewHolder> {

    private DatabaseReference dataRef;

    private FirebaseAuth mAuth;

    private String  viewUserId;

    private final Context mContext;
    private final List<ImageUploads> mUploads;
    private OnItemClickListener mListener;



    // private  List<String> linIconsView;


    public mapsViewAdapter(Context context, List<ImageUploads> uploads) {
        mContext = context;
        mUploads = uploads;

        /*linIconsView = new ArrayList<>();

        String facebookLinkImage = "facebook";
        String twitterLinkImage = "twitter";
        String instagramLinkImage = "instagram";

        linIconsView.add(facebookLinkImage);
        linIconsView.add(twitterLinkImage);
        linIconsView.add(instagramLinkImage);*/

    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.maps_searched_item, parent, false);
        return new mapsViewAdapter.ImageViewHolder(v);

    }

    @Override

    public void onBindViewHolder(ImageViewHolder holder, final int position) {

        ImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        //final String own_user_id = mAuth.getCurrentUser().getUid();

        final String imageUrl = selectedImage.getImageUrl();

        final String userKeyId = selectedImage.getUserId();

        final String raterValue = selectedImage.getExRating();

        if(imageUrl!= null && imageUrl.compareTo("null") != 0 && !imageUrl.isEmpty())

        {

            holder.textDescription.setText(selectedImage.getPostName());

            if(selectedImage.getLikes() != null && !selectedImage.getLikes().isEmpty() && selectedImage.getLikes().length() <=6)
            {
                holder.likesView.setText(selectedImage.getLikes());

            }

            if(selectedImage.getName() != null && !selectedImage.getName().isEmpty())
            {
                holder.textViewName.setText(selectedImage.getName());
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

                            holder.progressImageBar.setVisibility(INVISIBLE);
                            return false;
                        }
                    })
                    .into(holder.imageView);

// Now, you can also use a ViewTreeObserver to ensure the ImageView is actually visible on the screen




        }



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,MenuItem.OnMenuItemClickListener,View.OnCreateContextMenuListener
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

        }

        @Override
        public void onClick(final View view) {

            if (mListener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);

                }

            }else
            {
                Toast.makeText(mContext, " Pagiis  failed to open image.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Select Action");
            MenuItem delete = contextMenu.add(Menu.NONE, 1, 1, "untag");
            MenuItem viewProfile = contextMenu.add(Menu.NONE, 2,2, "View profile");
            delete.setOnMenuItemClickListener(this);
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) { int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId())
                    {
                        case 1:
                            mListener.onDeleteClick(position);
                            return true;
                        case 2:
                            mListener.onViewProfile(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(int position);

        void onDeleteClick(int position);

        void onViewProfile(int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}