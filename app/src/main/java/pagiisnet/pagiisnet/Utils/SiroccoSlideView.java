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

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.SirrocoImageUploads;

public class SiroccoSlideView extends RecyclerView.Adapter<SiroccoSlideView.ImageViewHolder>
{


    private final Context mContext;
    private final List<SirrocoImageUploads> mUploads;
    private DatabaseReference dataRef;
    private FirebaseAuth mAuth;
    private String  viewUserId;
    private SiroccoSlideView.OnItemClickListener mListener;

    // private  List<String> linIconsView;


    public SiroccoSlideView(Context context, List<SirrocoImageUploads> uploads) {
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
    public SiroccoSlideView.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.maps_view_item, parent, false);
        return new SiroccoSlideView.ImageViewHolder(v);

    }

    @Override

    public void onBindViewHolder(SiroccoSlideView.ImageViewHolder holder, final int position) {

        SirrocoImageUploads selectedImage = mUploads.get(position);

        final String selectedKey = selectedImage.getKey();

        //final String own_user_id = mAuth.getCurrentUser().getUid();

        final String imageUrl = selectedImage.getImageUrl();

        final String userKeyId = selectedImage.getUserId();

        final String raterValue = selectedImage.getExRating();

       /* holder.mapsViewCardView.setOnClickListener(new View.OnClickListener() {
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

        if(imageUrl != null && imageUrl.compareTo("null") != 0)
        {
            holder.textViewName.setText(selectedImage.getName());

            RequestOptions options = new RequestOptions();

            if(imageUrl.compareTo("userDefaultDp") == 0)
            {

                holder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.pagiis_logo_final));

            }else
            {
                Glide.with(mContext)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .apply(options.centerCrop())
                        .into(holder.imageView);

            }




        }else
        {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.pagiis_logo_final));
        }

        /*holder.mapsViewCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                v.findViewById(R.id.mapsViewCardItem);



            }
        });*/


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public void setOnItemClickListener(SiroccoSlideView.OnItemClickListener listener)
    {
        this.mListener = listener;

    }

    public interface OnItemClickListener{

        void onItemClick(int position);

        void onDeleteClick(int position);

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, MenuItem.OnMenuItemClickListener,View.OnCreateContextMenuListener
    {
        public TextView textViewName;
        public ImageView imageView;

        public ImageView profileImageView;

        public CardView mapsViewCardView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.sirocco_brand_model);
            imageView = itemView.findViewById(R.id.sirocco_image_view);
            profileImageView = itemView.findViewById(R.id.item_view_profilepic);
            mapsViewCardView = itemView.findViewById(R.id.mapsViewCardItem);
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
            MenuItem delete = contextMenu.add(Menu.NONE, 1, 1, "visit store");
            delete.setOnMenuItemClickListener(this);
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) { int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    if (item.getItemId() == 1) {
                        mListener.onDeleteClick(position);
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
