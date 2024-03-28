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

public class WakinSlideAdaptorOne extends RecyclerView.Adapter<WakinSlideAdaptorOne.ImageViewHolder>
{
    private DatabaseReference dataRef;

    private FirebaseAuth mAuth;

    private String  viewUserId;

    private final Context mContext;

    private final List<ImageUploads> mUploads;

    private OnItemClickListener mListener;

    // private  List<String> linIconsView;


    public WakinSlideAdaptorOne(Context context, List<ImageUploads> uploads) {
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
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.walkin_full_screen_item, parent, false);
        return new WakinSlideAdaptorOne.ImageViewHolder(v);

    }


    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {

        final ImageUploads uploadCurrent = mUploads.get(position);

        String loadImageUrl = uploadCurrent.getImageUrl();
        String loadImageName = uploadCurrent.getName();

        String FinalValue = "internetLink";

        if(loadImageUrl.compareTo("null") !=0)
        {

            RequestOptions options = new RequestOptions();

            Glide.with(mContext)
                    .load(loadImageUrl)
                    .apply(options.centerCrop())
                    .into(holder.imageView);

            holder.textViewName.setText(loadImageName);
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

        public ImageView imageView;
        public TextView textViewName;
        public ImageView imageViewTwo;
        public ImageView imageViewTree;


        public ImageViewHolder(View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.walkinItemView);
            textViewName = itemView.findViewById(R.id.walkinItemViewText);
            imageViewTwo = itemView.findViewById(R.id.ii);
            imageViewTree = itemView.findViewById(R.id.cc);

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
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "Visit Sir_occo");

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
    public void setOnItemClickListener(OnItemClickListener listener)
    {

        this.mListener = listener;

    }

}
