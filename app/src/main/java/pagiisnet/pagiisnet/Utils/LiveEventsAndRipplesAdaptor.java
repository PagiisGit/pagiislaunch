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
import pagiisnet.pagiisnet.createEventFile;
//import pagiisnet.pagiisnet.R;

public class LiveEventsAndRipplesAdaptor extends RecyclerView.Adapter<LiveEventsAndRipplesAdaptor.ImageViewHolder>
{
    private DatabaseReference dataRef;

    private FirebaseAuth mAuth;

    private String  viewUserId;

    private final Context mContext;

    private final List<createEventFile> mUploads;

    private LiveEventsAndRipplesAdaptor.OnItemClickListener mListener;

    // private  List<String> linIconsView;


    public LiveEventsAndRipplesAdaptor(Context context, List<createEventFile> uploads) {
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
    public LiveEventsAndRipplesAdaptor.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.live_events_and_ripples, parent, false);
        return new LiveEventsAndRipplesAdaptor.ImageViewHolder(v);

    }


    @Override
    public void onBindViewHolder(LiveEventsAndRipplesAdaptor.ImageViewHolder holder, final int position) {

        final createEventFile uploadCurrent = mUploads.get(position);

        String loadImageUrl = uploadCurrent.getImageUrl();
        //String loadEventImageUrl = uploadCurrent.getSt;

        String raterValue =  uploadCurrent.getExRating();

        String EventName = uploadCurrent.getName();
        String eventDP = uploadCurrent.getImageUrl();
        String welcomeText = uploadCurrent.getWelcomeNote();
        String InviteName = uploadCurrent.getInviteName();

        String InviteDp = uploadCurrent.getInviteDpUrl();

        String eventLocation= uploadCurrent.getLocation();


        //String likes = uploadCurrent.getLikes();

        //String views = uploadCurrent.getViews();

        //String share = uploadCurrent.getShare();

        //String FinalValue = "internetLink";


        holder.textViewWelcomeNoteDefault.setText(raterValue);
        holder.textViewEventName.setText(EventName);
        holder.textViewEventLocation.setText(eventLocation);
        holder.textViewInviteName.setText(InviteName);

        RequestOptions options = new RequestOptions();



        if(loadImageUrl != null && loadImageUrl.compareTo("null") !=0 )
        {
            //imageViewHolder.textViewName.setText(name);

            Glide.with(mContext)
                    .load(loadImageUrl)
                    .apply(options.centerCrop())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageViewEventDP);



        }else
        {
            holder.imageViewEventDP.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.deals_announce));
        }


        if(InviteDp != null && InviteDp.compareTo("null") !=0 )
        {
            //imageViewHolder.textViewName.setText(name);

            Glide.with(mContext)
                    .load(InviteDp)
                    .apply(options.centerCrop())
                    .into(holder.imageViewInviteDP);



        }else
        {
            holder.imageViewInviteDP.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.user));
        }

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,MenuItem.OnMenuItemClickListener,View.OnCreateContextMenuListener
    {

        public TextView textViewEventName;
        public TextView textViewInviteName;
        public TextView textViewEventLocation;
        public TextView textViewWelcomeNoteDefault;
        public ImageView imageViewEventDP;
        public ImageView imageViewInviteDP;


        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewEventName = itemView.findViewById(R.id.eventName);
            textViewEventLocation = itemView.findViewById(R.id.eventLocation);

            textViewInviteName = itemView.findViewById(R.id.requestName);
            imageViewInviteDP = itemView.findViewById(R.id.requestImage);
            imageViewEventDP = itemView.findViewById(R.id.requestImageEvent);

            textViewWelcomeNoteDefault = itemView.findViewById(R.id.requestTextType);
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
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "View");

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
    public void setOnItemClickListener(LiveEventsAndRipplesAdaptor.OnItemClickListener listener)
    {
        this.mListener = listener;

    }


}
