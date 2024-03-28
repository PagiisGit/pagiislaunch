package pagiisnet.pagiisnet.Utils;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.createEventFile;

public class ViewSearchedStoreAdaptor  extends RecyclerView.Adapter<ViewSearchedStoreAdaptor.ImageViewHolder>{


    private final Context mContext;
    private final List<createEventFile> mUploads;
    private DatabaseReference dataRef;
    private FirebaseAuth mAuth;
    private String  viewUserId;
    private OnItemClickListener mListener;

    // private  List<String> linIconsView;


    public ViewSearchedStoreAdaptor(Context context, List<createEventFile> uploads)
    {
        mContext = context;
        mUploads = uploads;


    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.maps_searched_item, parent, false);
        return new ViewSearchedStoreAdaptor.ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        createEventFile selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();


        //final String own_user_id = mAuth.getCurrentUser().getUid();

        final String imageUrl = selectedImage.getImageUrl();

        final String userKeyId = selectedImage.getUserId();

        final String raterValue = selectedImage.getExRating();

        /*holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.relativeSearcLayout);

                Intent intent = new Intent(mContext,PagiisMaxView.class);
                intent.putExtra("imageKeyMAx", selectedKey);
                intent.putExtra("imageUrlMax",imageUrl);
                intent.putExtra("imageUserId", userKeyId);
                mContext.startActivity(intent);

            }
        });*/




        if(imageUrl!= null && imageUrl.compareTo("null") != 0 && !imageUrl.isEmpty())

        {

            holder.textDescription.setText(selectedImage.getName());

            if(selectedImage.getLikes() != null && !selectedImage.getLikes().isEmpty())
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
                    .apply(options.centerCrop())
                    .into(holder.imageView);


            if(raterValue.compareTo("userDefaultDp") !=0 && !raterValue.isEmpty())
            {

                Glide.with(mContext)
                        .load(raterValue)
                        .apply(options.centerCrop())
                        .into(holder.profileImageView);

            }else
            {
                Glide.with(mContext)
                        .load(imageUrl)
                        .apply(options.centerCrop())
                        .into(holder.profileImageView);
            }

        }else
        {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_sirocco_icon));
        }
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;

    }

    public interface OnItemClickListener{

        void onNItemClick(int position);

        void onWhatEverClick(int position);

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, MenuItem.OnMenuItemClickListener,View.OnCreateContextMenuListener
    {
        private final RelativeLayout relativeLayout;
        public TextView textViewName;
        public TextView textDescription;
        public ImageView imageView;
        public ImageView profileImageView;
        public TextView likesView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.sirocco_brand_model);
            likesView = itemView.findViewById(R.id.likes_two);
            imageView = itemView.findViewById(R.id.sirocco_image_view);
            profileImageView = itemView.findViewById(R.id.item_view_profilepic);


            relativeLayout = itemView.findViewById(R.id.relativeSearcLayout);

            textDescription= itemView.findViewById(R.id.searchedItemDescription);


            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(final View view) {

            if (mListener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    mListener.onNItemClick(position);

                }

            }else
            {
                Toast.makeText(mContext, " Pagiis  failed to open image.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Select Action");
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "Posted by");

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
}
