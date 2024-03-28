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
import androidx.appcompat.widget.LinearLayoutCompat;
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

public class ChooseProfileServiceCategory extends RecyclerView.Adapter<ChooseProfileServiceCategory.ImageViewHolder> {

    private DatabaseReference dataRef;

    private FirebaseAuth mAuth;

    private String  viewUserId;

    private final Context mContext;

    private final List<ImageUploads> mUploads;

    private ChooseProfileServiceCategory.OnItemClickListener mListener;

    // private  List<String> linIconsView;

    public ChooseProfileServiceCategory(Context context, List<ImageUploads> uploads)
    {
        mContext = context;
        mUploads = uploads;

    }

    @Override
    public ChooseProfileServiceCategory.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.profile_service_item_selector, parent, false);
        return new ChooseProfileServiceCategory.ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ChooseProfileServiceCategory.ImageViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final ImageUploads uploadCurrent = mUploads.get(position);

        String loadImageUrl = uploadCurrent.getImageUrl();

        String raterValue = uploadCurrent.getExRating();

        String FinalValue = "internetLink";

        if (loadImageUrl.compareTo("null") != 0 && loadImageUrl != null)
        {
            holder.textViewName.setText(uploadCurrent.getName());
            holder.textViewNameTwo.setText(uploadCurrent.getPostName());

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

                view.findViewById(R.id.sirocco_image_view);
                mListener.onItemClickMapsProfileCategory(position);

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
        public TextView textViewNameTwo;
        public ImageView imageView;
        public LinearLayoutCompat primarylayoutView;
        //public CircularImageView profileImageView;

        public ProgressBar progressImageBar;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.profileServiceCategory);
            textViewNameTwo = itemView.findViewById(R.id.profileServiceCategoryDescription);
            imageView = itemView.findViewById(R.id.profileServiceImage);

            primarylayoutView = itemView.findViewById(R.id.primaryItem);
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
                    mListener.onItemClickMapsProfileCategory(position);

                    primarylayoutView.setBackgroundResource(R.drawable.bg_item_selcted);

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
                        mListener.onWhatEverClickMapsProfileCategory(position);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{

        void onItemClickMapsProfileCategory(int position);

        void onWhatEverClickMapsProfileCategory(int position);


    }
    public void setOnItemClickListener(ChooseProfileServiceCategory.OnItemClickListener listener)
    {
        this.mListener = listener;

    }
}
