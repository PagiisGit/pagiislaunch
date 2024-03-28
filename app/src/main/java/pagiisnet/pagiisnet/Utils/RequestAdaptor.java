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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.createEventFile;

public class RequestAdaptor  extends RecyclerView.Adapter<RequestAdaptor.ImageViewHolder>
{

    private final Context mContext;

    private final List<createEventFile> mUploads;

    private RequestAdaptor.OnItemClickListener mListener;

    //private ProgressBar loadbar;

    public RequestAdaptor(Context context, List<createEventFile> uploads)
    {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public RequestAdaptor.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.request_item, parent, false);
        return new RequestAdaptor.ImageViewHolder(v);


    }

    @Override
    public void onBindViewHolder(RequestAdaptor.ImageViewHolder imageViewHolder, final int position) {

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


        imageViewHolder.textViewWelcomeNoteDefault.setText(raterValue);
        imageViewHolder.textViewEventName.setText(EventName);
        imageViewHolder.textViewEventLocation.setText(eventLocation);
        imageViewHolder.textViewInviteName.setText(InviteName);

        RequestOptions options = new RequestOptions();



        if(loadImageUrl != null && loadImageUrl.compareTo("null") !=0 )
        {
            //imageViewHolder.textViewName.setText(name);

            Glide.with(mContext)
                    .load(loadImageUrl)
                    .apply(options.centerCrop())
                    .into(imageViewHolder.imageViewEventDP);



        }else
        {
            imageViewHolder.imageViewEventDP.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.deals_announce));
        }


        if(InviteDp != null && InviteDp.compareTo("null") !=0 )
        {
            //imageViewHolder.textViewName.setText(name);

            Glide.with(mContext)
                    .load(InviteDp)
                    .apply(options.centerCrop())
                    .into(imageViewHolder.imageViewInviteDP);



        }else
        {
            imageViewHolder.imageViewInviteDP.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.user));
        }



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener
    {

        public TextView textViewEventName;
        public TextView textViewInviteName;
        public TextView textViewEventLocation;
        public TextView textViewWelcomeNoteDefault;
        public ImageView imageViewEventDP;
        public ImageView imageViewInviteDP;


        public ImageViewHolder(final View itemView)
        {
            super(itemView);

            textViewEventName = itemView.findViewById(R.id.eventName);
            textViewEventLocation = itemView.findViewById(R.id.eventLocation);

            textViewInviteName = itemView.findViewById(R.id.requestName);
            imageViewInviteDP = itemView.findViewById(R.id.requestImage);
            imageViewEventDP = itemView.findViewById(R.id.requestImageEvent);

            //textViewWelcomeNoteDefault = itemView.findViewById(R.id.requestWelcomeText);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }



        @Override
        public void onClick(View view) {

            if (mListener != null )
            {
                int position = getAdapterPosition();

                int selctecid = view.getId();

                int postionX = (int) getItemId();

                if (position != RecyclerView.NO_POSITION)
                {
                    mListener.onClick(position);

                }

            }

        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {


            contextMenu.setHeaderTitle("Confirm ??");
            contextMenu.setHeaderIcon(R.drawable.pagiis_logo_final);
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "YES");

            MenuItem share= contextMenu.add(Menu.NONE, 2, 2, "N0");
            MenuItem chats = contextMenu.add(Menu.NONE, 3, 3, "from");

            share.setIcon(R.drawable.location_group);
            chats.setIcon(R.drawable.location_based_chat);
            doWhatever.setIcon(R.drawable.pagiis_profile_icon);

            doWhatever.setOnMenuItemClickListener(this);
            share.setOnMenuItemClickListener(this);
            chats.setOnMenuItemClickListener(this);
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
                            mListener.shareClick(position);
                            return true;
                        case 3:
                            mListener.chatsClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }


    public interface OnItemClickListener {

        void onClick(int position);

        void onWhatEverClick(int position);

        void shareClick(int position);

        void chatsClick(int position);

        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(RequestAdaptor.OnItemClickListener listener)

    {
        mListener = listener;
    }
}
