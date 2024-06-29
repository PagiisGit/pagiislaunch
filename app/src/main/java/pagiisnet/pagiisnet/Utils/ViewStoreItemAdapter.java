package pagiisnet.pagiisnet.Utils;

import static android.view.View.INVISIBLE;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.ImageUploads;
//import pagiisnet.pagiisnet.R;

public class ViewStoreItemAdapter extends RecyclerView.Adapter<ViewStoreItemAdapter.ImageViewHolder> {

    private DatabaseReference dataRef;

    private FirebaseAuth mAuth;

    private String  viewUserId;

    private final Context mContext;

    private final List<ImageUploads> mUploads;

    private ViewStoreItemAdapter.OnItemClickListener mListener;

    // private  List<String> linIconsView;

    public ViewStoreItemAdapter(Context context, List<ImageUploads> uploads)
    {
        mContext = context;
        mUploads = uploads;

    }

    @Override
    public ViewStoreItemAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.store_item_view, parent, false);
        return new ViewStoreItemAdapter.ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewStoreItemAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final ImageUploads uploadCurrent = mUploads.get(position);

        String loadImageUrl = uploadCurrent.getImageUrl();

        String raterValue = uploadCurrent.getExRating();

        String FinalValue = "internetLink";

        if (loadImageUrl.compareTo("null") != 0 && loadImageUrl != null)
        {
            holder.textViewName.setText(uploadCurrent.getName());

            RequestOptions options = new RequestOptions();

            Glide.with(mContext)
                    .load(loadImageUrl)
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



            holder.profileImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_like_it));

        }


        holder.imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.sirocco_image_view);
                mListener.onItemClick(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,MenuItem.OnMenuItemClickListener,View.OnCreateContextMenuListener
    {
        public TextView textViewName;
        public ImageView imageView;
        public CircularImageView profileImageView;

        public ProgressBar progressImageBar;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.sirocco_brand_model);
            imageView = itemView.findViewById(R.id.sirocco_image_view);
            profileImageView = itemView.findViewById(R.id.item_view_profilepic);

            progressImageBar = itemView.findViewById(R.id.progress_circle_user_memes);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);


            imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    int position = getAdapterPosition();
                    mListener.onWhatEverClick(position);

                }
            });


            textViewName.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    int position = getAdapterPosition();
                    mListener.onWhatEverClick(position);

                }
            });
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

            contextMenu.setHeaderTitle("Delete Post");
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "Action");

            doWhatever.setOnMenuItemClickListener(this);
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    if (item.getItemId() == 1) {
                        mListener.onWhatEverClick(position);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{

        void onItemClick(int position);

        void onWhatEverClick(int position);


    }
    public void setOnItemClickListener(ViewStoreItemAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;

    }
}
