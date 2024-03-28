package pagiisnet.pagiisnet.Utils;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.ImageUploads;
//import pagiisnet.pagiisnet.R;

public class PurchasedItemAdaptor extends RecyclerView.Adapter<PurchasedItemAdaptor.ImageViewHolder> {

    private DatabaseReference dataRef;

    private FirebaseAuth mAuth;

    private String  viewUserId;

    private final Context mContext;
    private final List<ImageUploads> mUploads;
    private PurchasedItemAdaptor.OnItemClickListener mListener;

    // private  List<String> linIconsView;


    public PurchasedItemAdaptor(Context context, List<ImageUploads> uploads) {
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
    public PurchasedItemAdaptor.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.maps_view_cart_item, parent, false);
        return new PurchasedItemAdaptor.ImageViewHolder(v);

    }


    @Override
    public void onBindViewHolder(PurchasedItemAdaptor.ImageViewHolder holder, final int position) {

        final ImageUploads uploadCurrent = mUploads.get(position);

        String loadImageUrl = uploadCurrent.getImageUrl();

        String raterValue =  uploadCurrent.getExRating();

        String likes = uploadCurrent.getName();

        String userID = uploadCurrent.getUserId();

        String views = uploadCurrent.getViews();

        String share = uploadCurrent.getShare();
        String chats = uploadCurrent.getChats();

        String FinalValue = "internetLink";

        if(loadImageUrl.compareTo("null") !=0)
        {
            holder.textViewName.setText(uploadCurrent.getName());
            holder.textViewNameView_full.setText(uploadCurrent.getName());
            holder.textViewNameView.setText("Link..");

            RequestOptions options = new RequestOptions();

            Glide.with(mContext)
                    .load(loadImageUrl)
                    .apply(options.centerCrop())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);

        }else
        {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.pagiis_logo_final));
        }
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,MenuItem.OnMenuItemClickListener,View.OnCreateContextMenuListener
    {
        public TextView textViewName;
        public ImageView imageView;
        public TextView textViewNameView;
        public TextView textViewNameView_full;
        public ImageView profileImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);


            textViewName = itemView.findViewById(R.id.sirocco_brand_antique);
            imageView = itemView.findViewById(R.id.sirocco_image_view);

            profileImageView = itemView.findViewById(R.id.user_item_view_profilepic);

            textViewNameView_full = itemView.findViewById(R.id.sirocco_brand_model);

            textViewNameView = itemView.findViewById(R.id.modellink);

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
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "Purchased by");
            MenuItem delete = contextMenu.add(Menu.NONE, 2, 2, "Delete");



            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
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
                            mListener.onDeleteClick(position);
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

        void onDeleteClick(int position);

    }
    public void setOnItemClickListener(PurchasedItemAdaptor.OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}
