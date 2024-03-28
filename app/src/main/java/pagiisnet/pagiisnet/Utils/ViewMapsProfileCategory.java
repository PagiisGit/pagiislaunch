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

public class ViewMapsProfileCategory extends RecyclerView.Adapter<ViewMapsProfileCategory .ImageViewHolder> {

    private DatabaseReference dataRef;

    private FirebaseAuth mAuth;

    private String  viewUserId;

    private final Context mContext;

    private final List<ImageUploads> mUploads;

    private ViewMapsProfileCategory .OnItemClickListener mListener;

    // private  List<String> linIconsView;

    public ViewMapsProfileCategory (Context context, List<ImageUploads> uploads)
    {
        mContext = context;
        mUploads = uploads;

    }

    @Override
    public ViewMapsProfileCategory .ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.maps_category_profile_itemview, parent, false);
        return new ViewMapsProfileCategory .ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewMapsProfileCategory .ImageViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final ImageUploads uploadCurrent = mUploads.get(position);

        String loadImageUrl = uploadCurrent.getImageUrl();

        String raterValue = uploadCurrent.getExRating();

        String FinalValue = "internetLink";

        if (loadImageUrl.compareTo("null") != 0 && loadImageUrl != null)
        {
            holder.textViewName.setText(uploadCurrent.getName());
            holder.textViewNameDes.setText(uploadCurrent.getPostName());
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


        }


        holder.imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.profileServiceCategory);
                mListener.onItemClickS(position);

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
        public TextView textViewNameDes;
        public ImageView imageView;
        // public CircularImageView profileImageView;

        public ProgressBar progressImageBar;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.profileServiceCategory);
            textViewNameDes = itemView.findViewById(R.id.profileServiceCategoryDescription);
            imageView = itemView.findViewById(R.id.profileServiceImage);
            //profileImageView = itemView.findViewById(R.id.item_view_profilepic);

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
                    mListener.onItemClickS(position);

                }

            }else
            {
                Toast.makeText(mContext, " Pagiis  failed to open image.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Select Action");
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "View Profile");

            doWhatever.setOnMenuItemClickListener(this);
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    if (item.getItemId() == 1) {
                        mListener.onWhatEverClickS(position);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{

        void onItemClickS(int position);

        void onWhatEverClickS(int position);


    }
    public void setOnItemClickListener(ViewMapsProfileCategory.OnItemClickListener listener)
    {
        this.mListener = listener;

    }
}
