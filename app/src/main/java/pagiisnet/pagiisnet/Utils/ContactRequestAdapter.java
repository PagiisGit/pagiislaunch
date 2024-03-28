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
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.ImageUploads;
//import pagiisnet.pagiisnet.R;

public class  ContactRequestAdapter extends RecyclerView.Adapter<ContactRequestAdapter.ImageViewHolder>  {


    private DatabaseReference dataRef;

    private FirebaseAuth mAuth;

    private String  viewUserId;

    private final Context mContext;
    private final List<ImageUploads> mUploads;
    private ContactRequestAdapter.OnItemClickListener mListener;

    // private  List<String> linIconsView;


    public ContactRequestAdapter(Context context, List<ImageUploads> uploads) {
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
    public ContactRequestAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.meme_item_view, parent, false);
        return new ContactRequestAdapter.ImageViewHolder(v);

    }


    @Override
    public void onBindViewHolder(ContactRequestAdapter.ImageViewHolder holder, final int position) {

        final ImageUploads uploadCurrent = mUploads.get(position);

        String loadImageUrl = uploadCurrent.getImageUrl();

        String raterValue =  uploadCurrent.getExRating();

        String likes = uploadCurrent.getLikes();

        String views = uploadCurrent.getViews();

        String share = uploadCurrent.getShare();



        String FinalValue = "internetLink";

        /*for(int i = 0; i < linIconsView.size(); i++)
        {

            if(linIconsView.get(i).compareTo(raterValue) == 0)
            {

                 FinalValue = linIconsView.get(i);

            }


        }*/

        if(loadImageUrl.compareTo("null") !=0  && loadImageUrl != null || raterValue.compareTo("userDefaultDp") != 0)
        {
            holder.textViewName.setText(uploadCurrent.getName());

            RequestOptions options = new RequestOptions();

            Glide.with(mContext)
                    .load(loadImageUrl)
                    .apply(options.centerCrop())
                    .into(holder.imageView);

            if(views.compareTo("userDefaultDp") != 0 )
            {

                holder.textViewNameView.setText(views);

                if(share.compareTo("userDefaultDp") !=0)
                {
                    holder.textViewNameShare.setText(share);

                    if(likes.compareTo("userDefaultDp") != 0)
                    {
                        holder.textViewNameLikes.setText(likes);
                    }
                }

            }



            if(raterValue.compareTo("userDefaultDp")!=0)
            {

                Glide.with(mContext)
                        .load(raterValue)
                        .apply(options.centerCrop())
                        .into(holder.profileImageView);

            }else
            {
                Glide.with(mContext)
                        .load(loadImageUrl)
                        .apply(options.centerCrop())
                        .into(holder.profileImageView);
            }

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
        public TextView textViewNameLikes;
        public TextView textViewNameChats;
        public TextView textViewNameView;
        public TextView textViewNameShare;
        public ImageView imageViewSirocco;
        public ImageView imageViewRadiusChats;
        public ImageView imageViewRadiusFriends;
        public ImageView imageViewLikes;


        public ImageView profileImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.memeName);
            imageView = itemView.findViewById(R.id.memeImageView);
            profileImageView = itemView.findViewById(R.id.user_item_view_profilepic);
            imageViewLikes = itemView.findViewById(R.id.likes);
            textViewNameView = itemView.findViewById(R.id.viewers);
            textViewNameShare = itemView.findViewById(R.id.share);
            textViewNameChats = itemView.findViewById(R.id.chats);

            textViewNameLikes = itemView.findViewById(R.id.likesText);

            imageViewSirocco = itemView.findViewById(R.id.views);
            imageViewRadiusChats = itemView.findViewById(R.id.pagiis_radius_chat);
            imageViewRadiusFriends = itemView.findViewById(R.id.pagiis_radius_friends);


            imageViewLikes.setVisibility(View.INVISIBLE);


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
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "Posted by");
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
    public void setOnItemClickListener(ContactRequestAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;

    }
}
