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
import pagiisnet.pagiisnet.SirrocoImageUploads;


public class SiroccoPicsAdapter extends RecyclerView.Adapter<SiroccoPicsAdapter.ImageViewHolder> {

    private DatabaseReference dataRef;
    private  FirebaseAuth mAuth;
    private String  viewUserId;
    private final Context mContext;
    private final List<SirrocoImageUploads> mUploads;
    private OnItemClickListener mListener;

    public SiroccoPicsAdapter(Context context, List<SirrocoImageUploads> uploads)
    {
        mContext = context;
        mUploads = uploads;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.sirroce_item_view, parent, false);
        return new ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {

        final SirrocoImageUploads uploadCurrent = mUploads.get(position);

        String loadImageUrl = uploadCurrent.getImageUrl();

        String raterValue =  uploadCurrent.getExRating();

        if(loadImageUrl != null )
        {
            holder.textViewName.setText(uploadCurrent.getName());
            holder.ModelName.setText(uploadCurrent.getModelName());

           RequestOptions options = new RequestOptions();

            Glide.with(mContext)
                    .load(loadImageUrl)
                    .apply(options.centerCrop())
                    .into(holder.imageView);

            /*Picasso.with(mContext).load(loadImageUrl)
                    .centerCrop()
                    .error(R.drawable.ic_action_catchup)
                    .into(holder.imageView);*/

        }else
        {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.linktonet));
        }
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,MenuItem.OnMenuItemClickListener,View.OnCreateContextMenuListener{

        public TextView textViewName,ModelName,LinkView;
        public ImageView imageView;

        //public ImageView profileImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.sirocco_brand_antique);
            imageView = itemView.findViewById(R.id.sirocco_image_view);
            ModelName = itemView.findViewById(R.id.sirocco_brand_model);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }





        @Override

        public void onClick(View view) {

            if (mListener != null)
            {

                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);

                }

            }else
            {
                Toast.makeText(mContext, " Sorry there are no available Images to view ", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Select Action");
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "Visit Store");
            //MenuItem delete = contextMenu.add(Menu.NONE, 2, 2, "Delete");

            doWhatever.setOnMenuItemClickListener(this);
            //delete.setOnMenuItemClickListener((MenuItem.OnMenuItemClickListener) this);
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
    public interface OnItemClickListener
    {
        void onItemClick(int position);

        void onWhatEverClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;

    }
}