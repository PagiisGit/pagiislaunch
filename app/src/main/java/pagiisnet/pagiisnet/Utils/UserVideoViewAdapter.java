package pagiisnet.pagiisnet.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.ImageUploads;
//import pagiisnet.pagiisnet.R;


public class UserVideoViewAdapter extends RecyclerView.Adapter<UserVideoViewAdapter.VideoViewHolder>{
    private DatabaseReference dataRef;

    private FirebaseAuth mAuth;

    private String  viewUserId;

    private final Context mContext;
    private final List<ImageUploads> mUploads;
    private UserVideoViewAdapter.OnItemClickListener mListener;

    private ProgressBar mProgressBar;

    public UserVideoViewAdapter(Context context, List<ImageUploads> uploads)
    {
        mContext = context;
        mUploads = uploads;
    }
    @Override
    public UserVideoViewAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.video_view_item_two, parent, false);
        return new UserVideoViewAdapter.VideoViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final UserVideoViewAdapter.VideoViewHolder holder, final int position)
    {
        final ImageUploads uploadCurrent = mUploads.get(position);

        String loadImageUrl = uploadCurrent.getImageUrl();

        String raterValue =  uploadCurrent.getExRating();

        String timePost = uploadCurrent.getPostTime();

        String postPosition = uploadCurrent.getPostLocation();

        String postName = uploadCurrent.getPostName();

        RequestOptions options = new RequestOptions();

        if(loadImageUrl != null)
        {


            holder.textViewName.setText(postName);
            holder.timePost.setText(timePost+"  @"+postPosition);
            holder.imageView.setVideoURI(Uri.parse(loadImageUrl));

            Glide.with(mContext)
                    .asBitmap()
                    .load(loadImageUrl)
                    .apply(options.centerCrop())
                    .into(holder.viewVideotumb);

            holder.imageView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1)
                {
                    if(i == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                    {
                        holder.imageView.seekTo(100);
                        mProgressBar.setVisibility(View.VISIBLE);
                    }else if(i== MediaPlayer.MEDIA_INFO_BUFFERING_END)
                    {
                        mProgressBar.setVisibility(View.INVISIBLE);

                    }
                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount()
    {
        return mUploads.size();
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,MenuItem.OnMenuItemClickListener,View.OnCreateContextMenuListener{

        public TextView textViewName;
        public VideoView imageView;
        public TextView timePost;
        public TextView locatioPost;
        public  TextView likes_one;
        public  TextView  likes_two;

        public MediaController mc;

        public ImageView profileImageView;
        public ImageView viewVideotumb;
        public ImageView volumeControl;

        public VideoViewHolder(View itemView)
        {

            super(itemView);
            locatioPost = itemView.findViewById(R.id.post_title);
            imageView = itemView.findViewById(R.id.memeImageView);
            mProgressBar = itemView.findViewById(R.id.videoProgressBar);
            timePost = itemView.findViewById(R.id.postPosition);
            textViewName = itemView.findViewById(R.id.postUserName);
            volumeControl = itemView.findViewById(R.id.volumeControlOff);
            viewVideotumb = itemView.findViewById(R.id.sirocco_image_view_tumb);

            likes_one = itemView.findViewById(R.id.likes_one);
            likes_two = itemView.findViewById(R.id.likes_two);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

            mProgressBar.setVisibility(View.INVISIBLE);



            imageView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height)
                        {
                            mc = new MediaController(mContext);
                            imageView.setMediaController(mc);
                            imageView.requestFocus();
                            mc.setAnchorView(imageView);

                        }
                    });
                }
            });

            volumeControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    view.findViewById(R.id.volumeControlOff);

                    int position = getAdapterPosition();
                    int selctecid = view.getId();

                    int postionX = (int) getItemId();

                    if (position != RecyclerView.NO_POSITION)
                    {

                        ImageUploads selectedImage = mUploads.get(position);

                        String selectedKey = selectedImage.getKey();

                        String getUserRef = selectedImage.getUserId();

                        String imageUrl = selectedImage.getImageUrl();

                        if(imageUrl!=null && mListener != null)
                        {

                            volumeControl.setImageResource(R.drawable.mute_sound);

                            onClickMute();

                        }

                    }

                }
            });

        }


        public void onClickMute() {

            if (mListener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {
                    mListener.onItemClickMute(position);

                }

            }else
            {
                Toast.makeText(mContext, " Pagiis  failed to open video.", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        public void onClick(final View view) {

            if (mListener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {
                    mListener.onItemClick(position);

                }

            }else
            {
                Toast.makeText(mContext, " Pagiis  failed to open video.", Toast.LENGTH_SHORT).show();
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

    public interface OnItemClickListener
    {

        void onClick(int position);

        void onItemClick(int position);

        void onItemClickMute(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);

    }
    public void setOnItemClickListener(UserVideoViewAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;

    }
}
