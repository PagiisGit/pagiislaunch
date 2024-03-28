package pagiisnet.pagiisnet.Utils;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.ImageUploads;
//import pagiisnet.pagiisnet.R;

public class WalkinOrizontalSlide extends RecyclerView.Adapter<WalkinOrizontalSlide.ImageViewHolder>
{

    private DatabaseReference dataRef;

    private FirebaseAuth mAuth;

    private String  viewUserId;

    private final Context mContext;

    private final List<ImageUploads> mUploads;

    private WalkinOrizontalSlide.OnItemClickListener mListener;

    // private  List<String> linIconsView;


    public WalkinOrizontalSlide(Context context, List<ImageUploads> uploads) {
        mContext = context;
        mUploads = uploads;

    }

    @Override
    public WalkinOrizontalSlide.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.walkin_full_screen_item, parent, false);
        return new WalkinOrizontalSlide.ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(WalkinOrizontalSlide.ImageViewHolder holder, final int position) {

        final ImageUploads uploadCurrent = mUploads.get(position);

        String loadImageUrl = uploadCurrent.getImageUrl();

        String raterValue =  uploadCurrent.getExRating();

        String likes = uploadCurrent.getName();

        String userID = uploadCurrent.getUserId();

        String views = uploadCurrent.getViews();

        String share = uploadCurrent.getShare();
        String chats = uploadCurrent.getChats();



        String FinalValue = "internetLink";


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,MenuItem.OnMenuItemClickListener,View.OnCreateContextMenuListener
    {
        //public TextView textViewName;
        public ImageView imageView;
        //public ImageView profileImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            //textViewName = itemView.findViewById(R.id.sirocco_brand_model);
            imageView = itemView.findViewById(R.id.sirocco_image_view);
            //profileImageView = itemView.findViewById(R.id.item_view_profilepic);

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
    public void setOnItemClickListener(WalkinOrizontalSlide.OnItemClickListener listener)
    {
        this.mListener = listener;

    }

}
